public class ScaledField
{
	public static Cell getCell(int x, int y)
	{
		return new Cell();
	}
	
	public static Cell getCell(Point point)
	{
		return getCell(point.getX(), point.getY());
	}
}