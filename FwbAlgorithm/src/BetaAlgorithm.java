public class BetaAlgorithm extends Algorithm
{
	public KDE kde;
	
	public BetaAlgorithm(int minimumClusters, int maximumClusters, Field field)
	{
		super(minimumClusters, maximumClusters, field);
	}
	
	@Override
	public void run()
	{
		//Run KDE
		Stopwatch.Timer kdeTimer = Stopwatch.startNewTimer("KDE initialize");
		kde = new KDE(this.field);
		kdeTimer.stop();

		Stopwatch.Timer timer = Stopwatch.startNewTimer("Finding threshold");
		Threshold thresholdFinder = new Threshold();
		float threshold = thresholdFinder.findThreshold(kde);
		timer.stop();

		timer = Stopwatch.startNewTimer("Floodfill 1");
		field.setScaledField(kde.scaledField);
		field.startAssigningClusters(threshold);
		timer.stop();
	}
}