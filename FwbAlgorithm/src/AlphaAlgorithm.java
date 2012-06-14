
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlphaAlgorithm extends Algorithm
{
	public KDE kde;
	
	public AlphaAlgorithm(int minimumClusters, int maximumClusters, Field field)
	{
		super(minimumClusters, maximumClusters, field);
	}
	
	@Override
	public void run()
	{
		boolean noiseDetected = false;
		
		//Run KDE
		Stopwatch.Timer timer = Stopwatch.startNewTimer("KDE initialize");
		kde = new KDE(this.field);
		timer.stop();
		
		/*
		 * First try a floodfill run with a little less than the minimum point
		 * density. If the KDE is correctly setup, we get single clusters in
		 * high density areas, and if there is noise, each noise point
		 * itself becomes a cluster.
		 */
		{
			timer = Stopwatch.startNewTimer("Floodfill 1");
			field.setScaledField(kde.scaledField);
			field.startAssigningClusters(kde.getMinPointDensity() * 0.9f);
			timer.stop();
		}

		/*
		 * If the amount of clusters is less than the minimum or higher than
		 * the maximum we assume we have noise.
		 */
		int clusterCount = field.getNumberOfClusters();
		if(clusterCount < minimumClusters || clusterCount > maximumClusters)
		{
			/*
			 * This is an unacceptable result, so we assume we have detected
			 * noise.
			 */
			noiseDetected = true;
		}
		else
		{
			
		}
		
		//Reset the field to perform new operations on it.
		PointCategory.resetIndex();
		field.reset();
		
		Utils.log("AlphaAlgorithm", "NoiseDetected: " + noiseDetected);
		Utils.log("AlphaAlgorithm", "clusterCount: "  + clusterCount);
		
		float threshold;
		if(noiseDetected)
		{
			/*
			* We are now at a point at which we know that we have noise. So,
			* the threshold must be set to cut off the 'cluster' (which is noise).
			*/
			timer = Stopwatch.startNewTimer("Finding threshold");
			Threshold thresholdFinder = new Threshold();
			threshold = thresholdFinder.findThreshold(kde);
			timer.stop();
		}
		else	
		{
			/*
			 * We know we have no noise, so rerun floodfill with a threshold that's
			 * a little above the minimum point density.
			 */
			threshold = kde.getMinPointDensity() * 1.1f;
		}
		
		Utils.log("AlphaAlgorithm: Threshold", "" + threshold);
		
		/*
		* Now do a floodfill run with the threshold value for cutting off
		* the last 'cluster' (noise).
		*/
		{	
			timer = Stopwatch.startNewTimer("Floodfill 2");
			field.setScaledField(kde.scaledField);
			field.startAssigningClusters(threshold);
			timer.stop();
		}
		
		/*
		 * If there are clusters that don't have the minimal amount of
		 * points in them, we consider them noise.
		 */
		boolean resetClusterIndices = false;
		int minimalClusterSize = Math.max(3, (int)(0.005f * field.size())); //0.5%
		ArrayList<Cluster> clusters = field.getClusters();
		for(int i = 0; i < clusters.size(); i++)
		{
			if(clusters.get(i).size() <= minimalClusterSize)
			{
				for(int j = 0; j < clusters.get(i).size(); j++)
				{
					clusters.get(i).get(j).assignToPointCategory(field.getNoise());
				}
				
				clusters.remove(i);
				i--;
				resetClusterIndices = true;
			}
		}

		/*
		 * If we have more than the maximum amount of clusters, consider the ones
		 * with the minimum amount of points as noise.
		 */
		if(clusters.size() > maximumClusters)
		{
			Collections.sort(clusters, new ClusterSizeComparator());
			int rm = clusters.size() - maximumClusters;
			for(int i = 0; i < rm; i++)
			{
				for(int j = 0; j < clusters.get(0).size(); j++)
				{
					clusters.get(0).get(j).assignToPointCategory(field.getNoise());
				}
				
				clusters.remove(0);
				
				resetClusterIndices = true;
			}
		}
		
		/*
		 * If we have less than the minimum amount of clusters, we assign the first
		 * points of noise to be clusters.
		 */
		if(clusters.size() < minimumClusters)
		{
			int rm = minimumClusters - clusters.size();
			for(int i = 0; i < rm && i < field.getNoise().size(); i++)
			{
				field.getNoise().get(i).assignToPointCategory(field.createCluster());		
				resetClusterIndices = true;
			}
		}
		
		//Need to reset cluster indices
		if(resetClusterIndices)
		{
			for(int i = 0; i < clusters.size(); i++)
			{
				clusters.get(i).index = i + 1;
			}
		}
		//Utils.log(Stopwatch.getResult());
	}
	public class ClusterSizeComparator implements Comparator<PointCategory>
	{
		@Override
		public int compare(PointCategory o1, PointCategory o2)
		{
			return (o1.size() > o2.size() ? 1 : (o1.size() == o2.size() ? 0 : -1));
		}
	}
}