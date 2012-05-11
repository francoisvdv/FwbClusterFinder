import java.util.ArrayList;
import java.util.ListIterator;

public final class Field extends PointCollection
{
	private static final long serialVersionUID = 7564875408935215437L;
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
	 * Start assigning every point to a cluster.
	 * 
	 * @pre there are no clusters yet
	 * @post all clusters are filled
	 * @param threshold the threshold
	 */
	public void startAssigningClusters(Float threshold)
	{
		ListIterator<Point> iterator = this.listIterator();
		while(iterator.hasNext())
		{
			Point point = iterator.next();
			if(point.getPointCategory() == null)
			{
				if(ScaledField.getCell(point).getDensity() < threshold)
				{
					point.assignToPointCategory(this.getNoise());
				}
				else
				{
					floodFill(point);
				}
			}
		}
	}
	
	/**
	 * Begin the floodfill algorithm from a certain
	 * point.
	 * 
	 * @param point the point from where to start the 
	 * 			floodfill algorithm
	 */
	public void floodFill(Point point) 
	{ 
		Cell cell = ScaledField.getCell(point);
		if(cell.getCategory() == null)
		{
			Cluster cluster = this.createCluster();
			floodFill(cell, cluster);
		}
		else
		{
			point.assignToPointCategory(cell.getCategory());
		}		
	}
	
	/**
	 * Recursive floodfill method that assigns every
	 * cell in a same cluster to that cluster.
	 * @param cell the cell from where to start
	 * @param cluster the cluster
	 */
	public void floodFill(Cell cell, Cluster cluster) 
	{ 
		if()
	}
}