import java.util.ArrayList;

/**
 * For debugging purposes
 *
 */


public final class Stopwatch
{
	protected static String name;
	protected static ArrayList<Timer> timers = new ArrayList<Timer>();
	
	public static void setName(String name)
	{
		Stopwatch.name = name;
	}
	
	protected static class Timer
	{
		protected String name;
		protected long startTime;
		protected long stopTime = -1;
		protected static int numberOfTimersRunning = 0;
		protected static final int maxNameLength = 48;
		protected int r = 0;
		
		public Timer(String name)
		{
			this.name = name;
		}
		
		public void start()
		{
	        assert stopTime != -1 : "Cannot restart a timer";
	        
	        this.r = Timer.numberOfTimersRunning++;
			this.startTime = System.currentTimeMillis();
	    }
	    
	    public void stop()
	    {
	    	Timer.numberOfTimersRunning--;
	    	this.stopTime = System.currentTimeMillis();
	    }
	    
	    public long getElapsedTime()
	    {
	        return stopTime - startTime;
	    }
	    
	    public String getResult()
	    {
	    	assert Timer.numberOfTimersRunning == 0;
	    	
	    	String space1 = "";
	    	for(int i=0; i<r; i++)
	    		space1 += "  ";
	    	
	    	String time = Long.toString(this.getElapsedTime());
	    	
	    	String space2 = "";
	    	for(int i=0; i<Timer.maxNameLength - this.name.length() - 2*this.r + 10 - time.length(); i++)
	    		space2 += " ";
	    	
	    	return  space1 + "+ " + this.name + space2 + time + "\r\n";
	    }
	}
	
	public static Timer startNewTimer(String name)
	{
		Timer t = new Timer(name);
		Stopwatch.timers.add(t);
		t.start();
		return t;
	}
	
	public static String getResult()
	{
		String result = "-----------------------\r\n";
		result       += "-- Stopwatch results --\r\n";
		result       += "-----------------------\r\n";
		
		for(Timer t : Stopwatch.timers)
		{
			result += t.getResult();
		}
		
		result       += "-----------------------\r\n";
		
		return result;
	}
}
