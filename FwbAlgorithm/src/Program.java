import java.util.ArrayList;
import java.util.Arrays;

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
		
		Field field = new Field(new ArrayList<Point>(Arrays.asList(ip.getPoints())));
		
		Algorithm algo1 = new RandomClusterAlgorithm(ip.getMinimumClusters(), ip.getMaximumClusters(), field);
		algo1.run();
		algo1.printPoints();
	}
}
