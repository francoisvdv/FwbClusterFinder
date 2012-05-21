public final class Utils
{
	public static boolean floatAlmostEquals(float float1, float float2)
	{
		return floatAlmostEquals(float1, float2,  0.00001f);
	}
	public static boolean floatAlmostEquals(float float1, float float2, float precision)
    {
        return (Math.abs(float1 - float2) <= precision);
    }
	
	protected static float calcDistance(int x1, int y1, int x2, int y2)
	{
		return (float) Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
	}
	
	protected static float calcSquaredDistance(int x1, int y1, int x2, int y2)
	{
		return (float) (Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
	}
}
