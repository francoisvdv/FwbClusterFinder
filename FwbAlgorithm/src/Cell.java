public class Cell
{
	protected PointCategory category;
	//protected list/array/something points; 
	protected float density;
	protected int x1, y1, x2, y2;
	
	public Cell(int x1, int y1, int x2, int y2)
	{
		assert x1 < x2;
		assert y1 < y2;
		
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public PointCategory getCategory()
	{
		return category;
	}
	
	public void setCategory(PointCategory category)
	{
		this.category = category;
	}
	
	// slechte naam, cell is niet in cell. Beter "contains", went cell contains a point with these coords
	public boolean isInCell(int x, int y)
	{
		return 
				(this.x1 < x && x < this.x2)
			&&	(this.y1 < y && y < this.x2);
	}

	public boolean isInCell(Point point)
	{
		return isInCell(point.getX(), point.getY());
	}
	
	public float getDensity()
	{
		return this.density;
	}
	
	public void increaseDensity(float increment)
	{
		this.density += increment;
	}
	/*
	public void addPoint(Point p)
	{
		this.points.add(p);
	}*/
}