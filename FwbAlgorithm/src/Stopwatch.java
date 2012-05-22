import java.util.ArrayList;

/**
 * For debugging purposes
 *
 */


public class Stopwatch
{
	protected String name;
	protected ArrayList<Timer> timers = new ArrayList<Timer>();
	
	public Stopwatch(String name)
	{
		this.name = name;
	}
	
	protected class Timer
	{
		protected String name;
		protected long startTime;
		protected long stopTime;
		
		public Timer(String name)
		{
			this.name = name;
		}
		
		public void start()
		{
	        this.startTime = System.currentTimeMillis();
	    }
	    
	    public void stop()
	    {
	        this.stopTime = System.currentTimeMillis();
	    }
	    
	    public long getElapsedTime() {
	        return stopTime - startTime;
	    }
	}
	
	public Timer StartNewTimer(String name)
	{
		Timer t = new Timer(name);
		this.timers.add(t);
		t.start();
		return t;
	}
}
