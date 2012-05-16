public class AlphaAlgorithm extends Algorithm
{
	public AlphaAlgorithm(int minimumClusters, int maximumClusters, Field field)
	{
		super(minimumClusters, maximumClusters, field);
	}
	
	@Override
	public void run()
	{
		KDE kde = new KDE(this.field);
		kde.initialize();
		
		Threshold thresholdFinder = new Threshold();
		float threshold = thresholdFinder.findThreshold(kde);
		
		field.setScaledField(kde.scaledField);
		field.startAssigningClusters(threshold);
	}
}
