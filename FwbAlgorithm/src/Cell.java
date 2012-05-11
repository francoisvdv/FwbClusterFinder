public class Cell
{
	protected PointCategory category;
	protected int x1, y1, x2, y2;
	
	public Cell()
	{
		
	}
	
	public Cell(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public int getMinX()
	{
		return x1;
	}
	
	public int getMinY()
	{
		return y1;
	}
	
	public int getMaxX()
	{
		return x2;
	}
	
	public int getMaxY()
	{
		return y2;
	}
	
	public Float getDensity()
	{
		return null;
	}
	
	public PointCategory getCategory()
	{
		return category;
	}
	
	public void setCategory(PointCategory category)
	{
		this.category = category;
	}
	
	public boolean isInCell(int x, int y)
	{
		return true;
	}

	public boolean isInCell(Point point)
	{
		return isInCell(point.getX(), point.getY());
	}
}