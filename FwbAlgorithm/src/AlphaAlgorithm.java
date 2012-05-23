public class AlphaAlgorithm extends Algorithm
{
	public AlphaAlgorithm(int minimumClusters, int maximumClusters, Field field)
	{
		super(minimumClusters, maximumClusters, field);
	}
	
	@Override
	public void run()
	{
		Stopwatch.Timer KDETimer = Stopwatch.startNewTimer("KDE initialize");
		KDE kde = new KDE(this.field);
		kde.initialize();
		KDETimer.stop();
//		
//		Threshold thresholdFinder = new Threshold();
//		float threshold = thresholdFinder.findThreshold(kde);
//		
//		field.setScaledField(kde.scaledField);
//		field.startAssigningClusters(threshold);
	}
}
