import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class InputParser
{
	protected int minimumClusters;
	protected int maximumClusters;
	protected Point[] points;
	protected Field field;
	
	Scanner inputScanner;
	
	public InputParser(InputStream stream)
	{
		inputScanner = new Scanner(stream);
	}
	
	public int getMinimumClusters()
	{
		return minimumClusters;
	}
	public int getMaximumClusters()
	{
		return maximumClusters;
	}
	public Point[] getPoints()
	{
		return points;
	}
	public Field getField()
	{
		if(field == null)
			field = new Field(new ArrayList<Point>(Arrays.asList(this.getPoints())));
		
		return field;
	}
	
	public boolean parseInput()
	{
		return parseInputClusters() && parseInputPoints();
	}
	boolean parseInputClusters()
	{
		if(!inputScanner.hasNextLine())
			return false;
		
		String line = inputScanner.nextLine();
		Scanner scanner = new Scanner(line);
		
		String s = scanner.hasNext() ? scanner.next() : "";
		if(!s.equals("find"))
			return false;
		
		if(!scanner.hasNextInt())
			return false;
		int numberOfClusters = scanner.nextInt();
		
		s = scanner.hasNext() ? scanner.next() : "";
		if(s.equals("to"))
		{
			minimumClusters = numberOfClusters;
			
			if(!scanner.hasNextInt())
				return false;
			numberOfClusters = scanner.nextInt();
			
			maximumClusters = numberOfClusters;
			
			s = scanner.hasNext() ? scanner.next() :  "";
			if(!s.equals("clusters"))
				return false;
		}
		else if(s.equals("clusters"))
		{
			minimumClusters = maximumClusters = numberOfClusters;
		}
		else
			return false;

		return true;
	}
	boolean parseInputPoints()
	{
		if(!inputScanner.hasNextLine())
			return false;
		
		Scanner lineScanner = new Scanner(inputScanner.nextLine());
		
		if(!lineScanner.hasNextInt())
			return false;
		int pointCount = lineScanner.nextInt();

		points = new Point[pointCount];
		
		String s = lineScanner.hasNext() ? lineScanner.next() : "";
		if(!s.equals("points"))
			return false;
		
		for(int i = 0; i < points.length; i++)
		{
			if(!inputScanner.hasNextLine())
				return false;
			
			lineScanner = new Scanner(inputScanner.nextLine());
			
			if(!lineScanner.hasNextInt())
				return false;
			int x = lineScanner.nextInt();
			if(!lineScanner.hasNextInt())
				return false;
			int y = lineScanner.nextInt();
			
			points[i] = new Point(x, y);
		}
		
		return true;
	}
}
