package fwbalgorithm;

public abstract class Algorithm
{
	protected int minimumClusters;
	protected int maximumClusters;
	protected Point[] points;
	
	public Algorithm(int minimumClusters, int maximumClusters, Point[] points)
	{
		this.minimumClusters = minimumClusters;
		this.maximumClusters = maximumClusters;
		this.points = points;
	}
	
	public abstract void run();
}
