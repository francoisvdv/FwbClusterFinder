public class Rectangle
{
	int x, y, width, height;
	
	public int getLeft()
	{
		return x;
	}
	public int getRight()
	{
		return x + width;
	}
	public int getTop()
	{
		return y;
	}
	public int getBottom()
	{
		return y + height;
	}
}