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
		
		//Utils.log(Stopwatch.getResult());
	}
}