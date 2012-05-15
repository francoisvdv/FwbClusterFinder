public class Rectangle
{
	int x1, x2, y1, y2;
	
	public int getLeft()
	{
		return x1;
	}
	public int getRight()
	{
		return x2;
	}
	public int getTop()
	{
		return y1;
	}
	public int getBottom()
	{
		return y2;
	}
	public int getWidth()
	{
		return x2 - x1;
	}
	public int getHeight()
	{
		return y2 - y1;
	}
}