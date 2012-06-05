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
		Stopwatch.Timer KdeTimer = Stopwatch.startNewTimer("KDE initialize");
		kde = new KDE(this.field);
		KdeTimer.stop();
		
		/*
		 * First try a floodfill run with the minimum point density. If the
		 * KDE is correctly setup, we get single clusters in high density areas,
		 * and if there is noise, each noise point itself becomes a cluster.
		 */
		{
			Stopwatch.Timer timer = Stopwatch.startNewTimer("Floodfill");
			field.setScaledField(kde.scaledField);
			field.startAssigningClusters(kde.getMinPointDensity());
			timer.stop();
		}
		
		int clusterCount = field.getNumberOfClusters();
		if(clusterCount >= minimumClusters && clusterCount <= maximumClusters)
		{
			//This is an acceptable result, so leave it at this.
			return;
		}
		
		//Reset the field to perform new operations on it.
		field.reset();
		PointCategory.resetIndex();
		
		/*
		 * We are now at a point at which we know that we have noise. So, cut
		 * off the last 'cluster' (which is noise) and rerun floodfill.
		 */
		float threshold;
		{
			Stopwatch.Timer timer = Stopwatch.startNewTimer("Finding threshold");
			Threshold thresholdFinder = new Threshold();
			threshold = thresholdFinder.findThreshold(kde);
			timer.stop();
		}
		
		/*
		 * Now do a floodfill run with the threshold value for cutting off
		 * the last 'cluster' (noise).
		 */
		{	
			Stopwatch.Timer floodfillTimer = Stopwatch.startNewTimer("Floodfill");
			field.setScaledField(kde.scaledField);
			field.startAssigningClusters(threshold);
			floodfillTimer.stop();
		}
	}
}