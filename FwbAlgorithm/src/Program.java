public final class Program
{	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		InputParser ip = new InputParser(System.in);
		if(!ip.parseInput())
		{
			System.out.println("Invalid input");
			return;
		}
		
		Field field = ip.getField();
		
		//Algorithm algo1 = new RandomClusterAlgorithm(ip.getMinimumClusters(), ip.getMaximumClusters(), field);
		Algorithm algo1 = new AlphaAlgorithm(ip.getMinimumClusters(), ip.getMaximumClusters(), field);
		algo1.run();
		algo1.printPoints();
	}
}
