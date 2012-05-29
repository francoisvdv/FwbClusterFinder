import java.util.ArrayList;
import java.util.LinkedList;

public final class Field extends PointCollection
{
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private Noise noise = Noise.getInstance();
	private Float threshold;
	private ScaledField scaledField = null;
	protected Rectangle boundingRectangle = null;
	
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
		Cluster c = new Cluster();
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
	
	/**
	 * Get the noise
	 * @return the noise
	 */
	public Noise getNoise()
	{
		return this.noise;
	}
	
	/**
	 * Get the clusters
	 * @return the clusters in an ArrayList
	 */
	public ArrayList<Cluster> getClusters()
	{
		return clusters;
	}
	
	public void reset()
	{
		noise.clear();
		for(Cluster cluster : clusters)
		{
			cluster.clear();
		}
		clusters.clear();
		
		for(Point p : this)
		{
			p.resetPointCategory();
		}
	}
	
	/**
	 * 
	 * @return a rectangle with the bounds of the field.
	 */
	public Rectangle getBoundingRectangle()
	{
		Stopwatch.Timer boundingTimer = Stopwatch.startNewTimer("getBoundingRectangle()");
		
		if(this.boundingRectangle == null)
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
			
			this.boundingRectangle = r;
		}

		boundingTimer.stop();
		return this.boundingRectangle;
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
		long time = System.currentTimeMillis();
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
		
		Utils.log("FloodFill", "took: "+ (System.currentTimeMillis() - time));
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
			floodFill(cell, cluster);
		}
		else
		{
			point.assignToPointCategory(cell.getCategory());
		}		
	}
	
	/**
	 * Non-recursive floodFill method that assigns every
	 * cell in a same cluster to that cluster.
	 * @param cell the cell from where to start
	 * @param cluster the cluster
	 */	
	public void floodFill(Cell cell, Cluster cluster)
	{
		//cell.setCategory(cluster);
		LinkedList<Cell> list = new LinkedList<Cell>();
		list.addLast(cell);
		
		// While the queue is not empty
		while(!list.isEmpty())
		{
			// Get the last element of the queue (n)
			Cell n = list.getLast();
			list.removeLast();
			
			// If the density of (n) is bigger than the threshold
			if(n.getDensity() >= threshold && n.getCategory() != cluster)
			{
				// Get the leftmost cell (w) of this row
				Cell w = floodFill_moveToWest(n, cluster);
				// Get the rightmost cell (e) of this row
				Cell e = floodFill_moveToEast(n, cluster);
				
				// While the west cell is not equal to the east cell
				while(w != e){
					// Set the category of the cell
					w.setCategory(cluster);
					
					// Get the cell (up) above (w)
					Cell up = scaledField.getUpperCell(w);
					// If the upper cell is is all right
					if(floodFill_checkCell(up, cluster))
					{
						// Add that cell to the queue
						list.addLast(up);
					}
					
					// Get the cel (down) below (w)
					Cell down = scaledField.getBottomCell(w);
					// If (down) is all right
					if(floodFill_checkCell(down, cluster))
					{
						// Add (down) to the queue
						list.addLast(down);
					}
					w = scaledField.getRightCell(w);
				}
				// Set the category 
				e.setCategory(cluster);
				
				Cell up = scaledField.getUpperCell(e);
				if(floodFill_checkCell(up, cluster))
				{
					list.addLast(up);
				}
				
				Cell down = scaledField.getBottomCell(e);
				if(floodFill_checkCell(down, cluster))
				{
					list.addLast(down);
				}	
			}
		}
	}
	
	private Cell floodFill_moveToWest(Cell cell, Cluster cluster)
	{
		Cell west = scaledField.getLeftCell(cell);
		
		if(!floodFill_checkCell(west, cluster))
		{
			return cell;
		}
	
		return floodFill_moveToWest(west, cluster);
	}
	
	private Cell floodFill_moveToEast(Cell cell, Cluster cluster)
	{
		Cell east = scaledField.getRightCell(cell);
		
		if(!floodFill_checkCell(east, cluster))
		{
			return cell;
		}
	
		return floodFill_moveToEast(east, cluster);
	}
	
	private boolean floodFill_checkCell(Cell cell, Cluster cluster)
	{
		if(cell == null)
		{
			return false;
		}
		
		// If the cell density is below threshold: return
		if(cell.getDensity() < threshold)
		{
			return false;
		}
		
		// If the cell is already equal to the cluster: return
		if(cell.getCategory() == cluster)
		{
			return false;
		}
		
		return true;
	}
}