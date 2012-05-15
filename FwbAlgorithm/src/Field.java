import java.util.ArrayList;
import java.util.ListIterator;

public final class Field extends PointCollection
{
	private static final long serialVersionUID = 7564875408935215437L;
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private Noise noise = Noise.getInstance();
	private Float threshold;
	private ScaledField scaledField; // TODO
	
	public Field(){
		super();
	}
	
	public Field(ArrayList<Point> points)
	{
		super.addAll(points);
	}
	
	public Cluster createCluster()
	{
		Cluster c = new Cluster(clusters.size());
		clusters.add(c);
		return c;
	}
	
	// TODO
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
	
	/**
	 * 
	 * @return a rectangle with the bounds of the field.
	 */
	public Rectangle getBoundingRectangle()
	{
		Rectangle r = new Rectangle();
		
		for(Point p : this)
		{
			if(p.getX() < r.x)
				r.x = p.getX();
			else if(p.getX() > r.x + r.width)
				r.width = p.getX() - r.x;
			else if(p.getY() < r.y)
				r.y = p.getY();
			else if(p.getY() > r.y + r.height)
				r.y = p.getY() - r.y;
		}
		
		return r;
	}
	
	public void scale(int x, int y) { }
	
	/**
	 * Generate and print the output of the algorithm
	 */
	public void generateOutput()
	{
		// toArray() is much faster than listIterator()
		Object[] obj = this.toArray();
		for(int i = 0; i < obj.length; i++)
		{
			Point point = (Point) obj[i];
			System.out.println(
					point.getX() +
					" " +
					point.getY() +
					" " +
					point.getPointCategory().getNumber());
		}
	}
	
	/**
	 * Start assigning every point to a cluster.
	 * 
	 * @precondition there are no clusters yet
	 * @post all clusters are filled
	 * @param threshold the threshold
	 */
	public void startAssigningClusters(Float threshold)
	{
		this.threshold = threshold;
		
		// toArray() is much faster than listIterator()
		Object[] obj = this.toArray();
		for(int i = 0; i < obj.length; i++)
		{
			Point point = (Point) obj[i];
			if(point.getPointCategory() == null)
			{
				if(scaledField.getCell(point).getDensity() < threshold)
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
	 * Begin the floodFill algorithm from a certain
	 * point.
	 * 
	 * @param point the point from where to start the 
	 * 			floodFill algorithm
	 */
	public void floodFill(Point point) 
	{ 
		Cell cell = scaledField.getCell(point);
		if(cell.getCategory() == null)
		{
			Cluster cluster = this.createCluster();
			floodFill(cell, cluster, point);
		}
		else
		{
			point.assignToPointCategory(cell.getCategory());
		}		
	}
	
	/**
	 * Recursive floodFill method that assigns every
	 * cell in a same cluster to that cluster.
	 * @param cell the cell from where to start
	 * @param cluster the cluster
	 */
	public void floodFill(Cell cell, Cluster cluster, Point point) 
	{ 
		if(cell == null)
		{
			return;
		}
		
		// If the cell density is below threshold: return
		if(cell.getDensity() < threshold)
		{
			return;
		}
		
		// If the cell is already equal to the cluster: return
		if(cell.getCategory() == cluster)
		{
			return;
		}
	
		cell.setCategory(cluster);
		
		// floodFill in 4 directions
		floodFill(scaledField.	// Up
				getCell(point.getX(), cell.getMinY() - 1), 
				cluster, point);
		floodFill(scaledField.	// Right
				getCell(cell.getMaxX() + 1, point.getY()), 
				cluster, point);
		floodFill(scaledField.	// Down
				getCell(point.getX(), cell.getMaxY() + 1), 
				cluster, point);
		floodFill(scaledField.	// Left
				getCell(cell.getMinX() - 1, point.getY()), 
				cluster, point);
	}
}