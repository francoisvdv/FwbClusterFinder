import java.util.ArrayList;

public final class Field extends PointCollection
{
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private Noise noise = Noise.getInstance();
	private Float threshold;
	private ScaledField scaledField = null;
	
	public Field()
	{
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
	
	public int getNumberOfClusters()
	{
		return clusters.size();
	}
	
	// TODO
	public PointCategory getPointCategoryAtPosition(int x, int y)
	{
		return this.scaledField.getCell(x, y).getCategory();
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
		
		for(int i=0; i<this.size(); i++)
		{
			Point p = this.get(i);
			
			if(i == 0)
			{
				r.x1 = p.getX();
				r.x2 = p.getX();
				r.y1 = p.getY();
				r.y2 = p.getY();
			}
			
			if(p.getX() < r.x1)
				r.x1 = p.getX();
			else if(p.getX() > r.x2)
				r.x2 = p.getX();
			
			if(p.getY() < r.y1)
				r.y1 = p.getY();
			else if(p.getY() > r.y2)
				r.y2 = p.getY();
		}
		
		return r;
	}
	
	public void setScaledField(ScaledField scaledField)
	{
		this.scaledField = scaledField;
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
			point.assignToPointCategory(cluster);
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