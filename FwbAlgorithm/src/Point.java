public class Point
{
	private int x;
	private int y;
	private PointCategory pointCategory;
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

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
	
	public boolean compareTo(int x, int y)
	{
		if(this.x == x && this.y == y)
		{
			return true;
		}
		
		return false;
	}
	
	public PointCategory getPointCategory()
	{
		return pointCategory;
	}
	/**
	 * Should only be called by the Field class.
	 * @param category
	 */
	public void assignToPointCategory(PointCategory category)
	{
		this.pointCategory = category;
		category.add(this);
	}
	
	@Override
	public String toString()
	{
		return this.getX() + " " + this.getY() + " " + this.getPointCategory();
	}
}
