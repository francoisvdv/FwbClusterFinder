public abstract class Algorithm
{
	protected int minimumClusters;
	protected int maximumClusters;
	protected Field field;
	
	public final static boolean DEBUG = true;
	
	public Algorithm(int minimumClusters, int maximumClusters, Field field)
	{
		this.minimumClusters = minimumClusters;
		this.maximumClusters = maximumClusters;
		this.field = field;
	}
	
	public abstract void run();
	
	public final void printPoints()
	{
		// toArray() is much faster than listIterator()
		Object[] obj = field.toArray();
		for(int i = 0; i < obj.length; i++)
		{
			Point point = (Point) obj[i];
			System.out.println(point);
		}
	}
}
