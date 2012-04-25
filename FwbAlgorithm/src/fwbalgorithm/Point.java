package fwbalgorithm;

public class Point
{
	int x;
	int y;
	int cluster;
	
	public int getX()
	{
		return x;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public int getY()
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public int getCluster()
	{
		return cluster;
	}
	public void setCluster(int cluster)
	{
		this.cluster = cluster;
	}
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
