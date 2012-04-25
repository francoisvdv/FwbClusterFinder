import java.util.Random;

public class RandomClusterAlgorithm extends Algorithm
{
	public RandomClusterAlgorithm(int minimumClusters, int maximumClusters, Point[] points)
	{
		super(minimumClusters, maximumClusters, points);
		
	}

	@Override
	public void run()
	{
		Random random = new Random();
		
		for(Point p : points)
		{
			p.cluster = random.nextInt(maximumClusters);
		}
	}
}
