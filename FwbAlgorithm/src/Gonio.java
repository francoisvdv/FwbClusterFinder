
public class Gonio
{
	protected Gonio() {}
	
	protected static float calcDistance(int x1, int y1, int x2, int y2)
	{
		return (float) Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
	}
	
	protected static float calcSquaredDistance(int x1, int y1, int x2, int y2)
	{
		return (float) (Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
	}
}
