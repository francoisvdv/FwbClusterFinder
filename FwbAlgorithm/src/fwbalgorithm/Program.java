package fwbalgorithm;

public final class Program
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		InputParser ip = new InputParser();
		if(!ip.parseInput())
		{
			System.out.println("Invalid input");
			return;
		}

		Algorithm algo1 = new RandomClusterAlgorithm(ip.getMinimumClusters(), ip.getMaximumClusters(), ip.getPoints());
		algo1.run();
		algo1.printPoints();
	}
}
