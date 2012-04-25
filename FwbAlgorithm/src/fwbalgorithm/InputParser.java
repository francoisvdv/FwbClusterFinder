package fwbalgorithm;

import java.util.Scanner;

public class InputParser
{
	private int minimumClusters;
	private int maximumClusters;
	private Point[] points;
	
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
	
	public boolean parseInput()
	{
		return parseInputClusters() && parseInputPoints();
	}
	public boolean parseInputClusters()
	{
		String line = new Scanner(System.in).nextLine();
		Scanner scanner = new Scanner(line);
		
		String s = scanner.hasNext() ? scanner.next() : "";
		if(!s.equals("find"))
			return false;

		int numberOfClusters = scanner.hasNextInt() ? scanner.nextInt() : 0;
		if(numberOfClusters == 0)
			return false;
		
		s = scanner.hasNext() ? scanner.next() : "";
		if(s.equals("to"))
		{
			minimumClusters = numberOfClusters;
			
			numberOfClusters = scanner.hasNextInt() ? scanner.nextInt() : 0;
			if(numberOfClusters == 0)
				return false;
			
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
	public boolean parseInputPoints()
	{
		Scanner inputScanner = new Scanner(System.in);
		Scanner lineScanner = new Scanner(inputScanner.nextLine());

		int pointCount = lineScanner.hasNextInt() ? lineScanner.nextInt() : 0;
		if(pointCount == 0)
			return false;

		points = new Point[pointCount];
		
		String s = lineScanner.hasNext() ? lineScanner.next() : "";
		if(!s.equals("points"))
			return false;
		
		for(int i = 0; i < points.length; i++)
		{
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
