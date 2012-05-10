public class Cell
{
	protected PointCategory category;
	protected int x1, y1, x2, y2;
	
	public Cell(int x1, int y1, int x2, int y2)
	{
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
	
	public boolean isInCell(int x, int y)
	{
		return true;
	}

	public boolean isInCell(Point point)
	{
		return isInCell(point.getX(), point.getY());
	}
}