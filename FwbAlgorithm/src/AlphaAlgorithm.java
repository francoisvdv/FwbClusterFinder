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
		Stopwatch.Timer KDETimer = Stopwatch.startNewTimer("KDE initialize");
		kde = new KDE(this.field);
		KDETimer.stop();
		
		//First floodfill run
		{
			Stopwatch.Timer floodfillTimer = Stopwatch.startNewTimer("flood fill");
			field.setScaledField(kde.scaledField);
			field.startAssigningClusters(0f);
			floodfillTimer.stop();
		}
		
		int clusterCount1 = field.getNumberOfClusters();

		//Find threshold
		float threshold;
		{
			Stopwatch.Timer ThresholdTimer = Stopwatch.startNewTimer("Finding threshold");
			Threshold thresholdFinder = new Threshold();
			threshold = thresholdFinder.findThreshold(kde);
			ThresholdTimer.stop();
		}
		//Second floodfill run
		{	
			field.reset();
			PointCategory.resetIndex();
		
			Stopwatch.Timer floodfillTimer = Stopwatch.startNewTimer("flood fill");
			field.setScaledField(kde.scaledField);
			field.startAssigningClusters(threshold);
			floodfillTimer.stop();
		}
		
		int clusterCount2 = field.getNumberOfClusters();
		
		System.out.println(clusterCount1 + "||" + clusterCount2);
	}
}
