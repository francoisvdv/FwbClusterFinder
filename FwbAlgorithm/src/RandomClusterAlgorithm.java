import java.util.Random;

public class RandomClusterAlgorithm extends Algorithm
{
	public RandomClusterAlgorithm(int minimumClusters, int maximumClusters, Field field)
	{
		super(minimumClusters, maximumClusters, field);
	}

	@Override
	public void run()
	{
		Random random = new Random();
		
		for(Point p : field)
		{
			int index = random.nextInt(maximumClusters);
			if(index == 0)
			{
				field.setNoise(p);
			}
			else
			{
				Cluster c = field.createCluster();
				c.index = index;
				field.setCluster(p, c);
			}
		}
	}
}
