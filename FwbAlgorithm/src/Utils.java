import java.io.File;

public final class Utils
{	
    public final static String input = "in";
    public final static String output = "fwbout";

    /**
     * Get the extension of a file.
     * @param f the file
     * @return the extension
     */
    public static String getExtension(File f) 
    {	
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
 
        if(i > 0 &&  i < s.length() - 1) 
        {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
	
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
	
	public static void Log(String message)
	{
		System.out.println(message);
	}
}
