public class Noise extends PointCategory
{
	private static Noise instance = null;
	
	private Noise() //singleton
	{
		super(false);
		index = 0;
	}
	
	public String toString()
	{
		return "0";
	}
	
	public static Noise getInstance() //singleton
	{
		if(instance == null)
			instance = new Noise();
		
		return instance;
	}
}
