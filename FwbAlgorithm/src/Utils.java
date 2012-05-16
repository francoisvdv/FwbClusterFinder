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
}
