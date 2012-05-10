import java.util.ArrayList;
import java.util.ListIterator;

public final class Field extends PointCollection
{
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private Noise noise = Noise.getInstance();
	
	public Field(ArrayList<Point> points)
	{
		super.addAll(points);
	}
	
	public Cluster createCluster()
	{
		Cluster c = new Cluster();
		clusters.add(c);
		return c;
	}
	
	public PointCollection getPointCollectionAtPosition(int x, int y)
	{
		return this.noise; // omdat KDE e.d. nog niet is toegepast is alles gewoon maar ff noise
	}
	
	public PointCategory getPointCategoryOfPoint(Point point)
	{
		return point.getPointCategory();
	}
	
	public PointCategory getPointCategoryWithIndex(int index)
	{
		try
		{
			return this.clusters.get(index); // hoeft niet de goeie te zijn, dit is ff tijdelijk zo
		}
		catch(IndexOutOfBoundsException e)
		{
			return null;
		}
	}
	
	public Noise getNoise()
	{
		return this.noise;
	}
	
	public void scale(int x, int y) { }
	
	/**
	 * Start assigning every point to a cluster
	 * 
	 * @pre there are no clusters yet
	 * @post all clusters are filled
	 */
	public void startAssigningClusters()
	{
		int step = 1000000000/this.size();
		ListIterator<Point> iterator = this.listIterator();
		while(iterator.hasNext())
		{
			Point point = iterator.next();
			if(point.getPointCategory() == null){
				floodFill(point.getX(), point.getY(), step);
			}
		}
	}
	
	public void floodFill(int x, int y, int step) 
	{ 
		/*
		 * 1. check of deze coördinaten al in een cluster(-lijst) zitten
		 * 1.a ja: simpel toevoegen.
		 * 1.b nee: nieuwe cluster maken, floodfill vanuit deze coördinaten met "kleur" cluster
		 */
		ArrayList<java.awt.Point> a_cluster = new ArrayList<java.awt.Point>();
		
		floodFill(x, y, step, a_cluster);
		
		Cluster cluster = this.createCluster();
		
		
	}
	
	public void floodFill(int x, int y, int step, ArrayList<java.awt.Point> some_cluster) 
	{ 
		
	}
}