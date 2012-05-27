public final class Program
{
	public final static boolean DEBUG = true;
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Stopwatch.setName("De naam wordt toch nog niet gebruikt");
		Stopwatch.Timer totalTimer = Stopwatch.startNewTimer("Total");
		Stopwatch.Timer parseTimer = Stopwatch.startNewTimer("Parsing input");
		
		InputParser ip = new InputParser(System.in);
		if(!ip.parseInput())
		{
			System.out.println("Invalid input");
			return;
		}
		
		parseTimer.stop();
		
		Stopwatch.Timer getFieldTimer = Stopwatch.startNewTimer("ip.getField");
		Field field = ip.getField();
		getFieldTimer.stop();
		
		//Algorithm algo1 = new RandomClusterAlgorithm(ip.getMinimumClusters(), ip.getMaximumClusters(), field);
		Algorithm algo1 = new AlphaAlgorithm(ip.getMinimumClusters(), ip.getMaximumClusters(), field);
		
		Stopwatch.Timer runTimer = Stopwatch.startNewTimer("Running algo");
		algo1.run();
		runTimer.stop();
		Stopwatch.Timer printTimer = Stopwatch.startNewTimer("print output");
		//algo1.printPoints();
		printTimer.stop();
		totalTimer.stop();
		
		System.out.print(Stopwatch.getResult());
	}
}
