import java.util.*;

// TODO: scaled field moet ergens gemaakt worden (in KDE of in Field)

public class KDE
{
	public    ScaledField scaledField;
	protected ScaledField.Stats scaledFieldStats;
	protected Field field;
	protected float bandwidth;
	protected Point[] sortedPoints;
	protected float[] densCache;
	protected boolean densCached = false;
	protected float   densCacheStep;
	
	protected int recursiveCount = 0;
	
	public KDE(Field field)
	{
		this.field = field;
		this.initialize();
	}
        
	public float getMaxCellDensity()
	{
		return this.scaledFieldStats.maxCellDens;
	}
	
	public float getMaxPointDensity()
	{
		return this.scaledFieldStats.maxPointDens;
	}
        
	public float getMinCellDensity()
	{
		return this.scaledFieldStats.minCellDens;
	}
	
	public float getMinPointDensity()
	{
		return this.scaledFieldStats.minPointDens;
	}
	
	public float getAvgCellDens()
	{
		return this.scaledFieldStats.avgCellDens;
	}
	
	public float getAvgPointDens()
	{
		return this.scaledFieldStats.avgPointDens;
	}
	
	public ScaledField.Stats getDensityStats()
	{
		return this.scaledFieldStats;
	}
	
	public int getPointCountAboveThreshold(float threshold)
	{
		return this.getPointCountAboveThreshold(threshold, 0, sortedPoints.length-1);
	}
	
	// uses binarysearch
	protected int getPointCountAboveThreshold(float threshold, int start, int end)
	{
		if(start >= end)
			return sortedPoints.length-1-start;
		
		int middle = (int)Math.floor((start+end)/2);
		float dens = scaledField.getCell(sortedPoints[middle]).getDensity();
		if(dens < threshold)
			return getPointCountAboveThreshold(threshold, middle+1, end);
		else if(dens > threshold)
			return getPointCountAboveThreshold(threshold, start, middle-1);
		else
			return getPointCountAboveThreshold_moveLeft(threshold, middle);
	}
	
	public final void initialize()
	{
		Stopwatch.Timer bwTimer = Stopwatch.startNewTimer("calcBandwidth()");
		this.calcBandwidth();
		bwTimer.stop();
		
//		Stopwatch.Timer densInitTimer = Stopwatch.startNewTimer("initializing density calculation");
//		this.initDensityCalculation();
//		densInitTimer.stop();
		
		Stopwatch.Timer SFTimer = Stopwatch.startNewTimer("constructing scaled field");
		this.scaledField = new ScaledField(field.getBoundingRectangle(), this.bandwidth);
		SFTimer.stop();
		
		Stopwatch.Timer foreachTimer = Stopwatch.startNewTimer("for each point...");
		Point[] points = new Point[this.field.size()];
		this.field.toArray(points);
		
		for(int i=0; i<points.length; i++)
		{
			//Hier valt snelheid te winnen: getCellsCloseTo berekent afstanden die in deze methode opnieuw berekend worden.
			
			Point p = (Point) points[i];
			Cell c = this.scaledField.getCell(p);
			Cell[] cells = this.scaledField.getCellsCloseTo(c, Constants.KDE.BWRADIUS*this.bandwidth);
			
			for(int j=0; j<cells.length; j++)
			{
				Cell cell = cells[j];
				float sqdist = Utils.calcSquaredDistance(cell.getMiddleX(), cell.getMiddleY(), p.getX(), p.getY());
				cell.increaseDensity(this.calcDensity(sqdist));
				
//				if(cell.getDensity() > this.getMaxCellDensity())
//					maxCellDensity = cell.getDensity();
			}
		}
		foreachTimer.stop();
		
		Stopwatch.Timer sortTimer = Stopwatch.startNewTimer("Sorting points on density");
		this.sortedPoints = this.sort(points);
		sortTimer.stop();
		
		Stopwatch.Timer StatsTimer = Stopwatch.startNewTimer("retieving cellstats");
		this.scaledFieldStats = this.scaledField.getStats();
		StatsTimer.stop();
		
		Stopwatch.Timer Stats2Timer = Stopwatch.startNewTimer("updating pointstats");
		float total = 0;
		boolean noZero = false;
		int pointCount = points.length;
		
		/* 
		 * TODO: de eerste kan als het goed is niet 0 zijn, moet nog naar gekeken worden.
		 * Het is er ook maar precies 1 (zie grafiek, om deze temaken uncomment
		 * hier beneden)
		 */
		//this.scaledFieldStats.minPointDens = this.scaledField.getCell(sortedPoints[0]).getDensity();
		for(int i = 0; i < sortedPoints.length; i++)
		{
			if(this.scaledField.getCell(sortedPoints[i]).getDensity() != 0)
			{
				this.scaledFieldStats.minPointDens =
						this.scaledField.getCell(sortedPoints[i]).getDensity();
				break;
			}
		}
		this.scaledFieldStats.maxPointDens = this.scaledField.getCell(sortedPoints[sortedPoints.length-1]).getDensity();
		for(int i=0; i<points.length; i++)
		{
			Point p = (Point) points[i];
			float dens = this.scaledField.getCell(p).getDensity();
			total += dens;
			if(noZero && dens == 0)
				pointCount--;
		}
		this.scaledFieldStats.avgPointDens = total/pointCount;
		Stats2Timer.stop();
		
		if(Constants.DEBUG)
		{
			this.scaledField.toFile(this.getMaxCellDensity());
			
			HashMap<Integer, Float> sortedDensities = new HashMap<Integer, Float>();
			for(int i = 0; i < sortedPoints.length; i++)
			{
				sortedDensities.put(i, this.scaledField.getCell(sortedPoints[i]).getDensity());
			}
			Graphing.graphSortedDensities(sortedDensities);
		}
	}
	
	protected int getPointCountAboveThreshold_moveLeft(float threshold, int middle)
	{
		while(middle >= 0 && scaledField.getCell(sortedPoints[middle--]).getDensity() == threshold);
		
		return middle+1;
	}
	
	protected class DensityComparator implements Comparator<Point>
	{
		protected ScaledField ScaledField;
		
		public DensityComparator(ScaledField SF)
		{
			this.ScaledField = SF;
		}
		
		@Override
		public int compare(Point p1, Point p2)
		{
			float dens1 = ScaledField.getCell(p1).getDensity();
			float dens2 = ScaledField.getCell(p2).getDensity();
			if(dens1 < dens2)
				return -1;
			else if(dens2 < dens1)
				return 1;
			else
				return 0;
		}
	}
	
	// uses quicksort
	// maby change to some form of counting sort
	protected Point[] sort(Point[] unsorted)
	{
		ArrayList<Point> sorted = new ArrayList<Point>(Arrays.asList(unsorted));
		Collections.sort(sorted, new DensityComparator(this.scaledField));
//		int start = this.partition(sorted, 0, sorted.length-1, 0.0f);
//		this.sort_recursive(sorted, start+1, sorted.length-1);
		Point[] points = new Point[sorted.size()];
		return sorted.toArray(points);
	}
	
	// in-place version of quicksort
	protected void sort_recursive(Point[] partiallySorted, int start, int end)
	{
		recursiveCount++;
		String s;
		if(recursiveCount > 64)
			s = "breakpoint here";
		
		if(start >= end)
		{
			recursiveCount--;
			return;
		}
		
		int pivotPos = getPivotPos(start, end);
		pivotPos = partition(partiallySorted, start, end, pivotPos);
		this.sort_recursive(partiallySorted, start, pivotPos-1);
		this.sort_recursive(partiallySorted, pivotPos+1, end);
		recursiveCount--;
	}
	
	// Given
	//    a array of Points: partiallySorted
	//    the start value and
	//    the ending value of the part of the array that we should partition
	//    and the position of a pivot we use to partition this part,
	// This method returns
	//    the new position of the pivot, after the given part of the array is partitioned.
	protected int partition(Point[] partiallySorted, int start, int end, int pivotPos)
	{
		assert start <= pivotPos;
		assert pivotPos <= end;
		assert start < end;
		
		// make a pivot
		float pivotVal = scaledField.getCell(partiallySorted[pivotPos]).getDensity();
		// place pivot at the end
		Point temp = partiallySorted[pivotPos];
		partiallySorted[pivotPos] = partiallySorted[end];
		partiallySorted[end] = temp;
		// "remove" pivot from array
		end--;
		// partition
		int middle = this.partition(partiallySorted, start, end, pivotVal);
		// move pivot
		temp = partiallySorted[++middle];
		partiallySorted[middle] = partiallySorted[end+1];
		partiallySorted[end+1] = temp;
		
		return middle;
	}
	
	protected int partition(Point[] partiallySorted, int start, int end, float pivotVal)
	{
		assert start <= end;
		
		// var used for swapping
		Point temp;
		
		// make two empty lists: smaller and greater or equal
		//  list smaller or equal: between start and middle, including both borders
		//  list greater:          between middle+1 and i-1, including both borders
		int middle = start-1;
		
		if(start < 0)
			Utils.log("KDE", ":(");
		
		for(int i=start; i<=end; i++)
		{
			// density at point
			float dens = scaledField.getCell(partiallySorted[i]).getDensity();
			if(dens <= pivotVal)
			{
				// swap point at i with the point at middle
				// (and increase middle)
				temp = partiallySorted[++middle];
				partiallySorted[middle] = partiallySorted[i];
				partiallySorted[i] = temp;
			}
			//else
				// swap point at i with the point at i => do nothing
		}
		
		return middle;
	}
	
	// chooses a pivot between start and end (random or not)
	protected int getPivotPos(int start, int end)
	{
		assert start <= end;
		
		if(start == end)
			return end;
		
		java.util.Random r = new java.util.Random();
		return r.nextInt(end-start)+start;
	}
	
	//TODO
	// http://en.wikipedia.org/wiki/Kernel_density_estimation#Practical_estimation_of_the_bandwidth
	protected void calcBandwidth()
	{
		float c_1 = -0.08f; //Constants.KDE.BWFACTOR;
		float c_2 = 1.60f;
		float s   = this.field.getBoundingRectangle().getSurface();
		float n   = this.field.size();
		float c   = (float)(c_1*Math.log(Math.sqrt(s/n))+c_2);
		this.bandwidth = (float)(c*Math.sqrt(s/n));
		//this.bandwidth = (float)(c_1*Math.log(Math.sqrt(s/n))+c_2);
		
		Utils.log("ScaledField", "S="+s+"; N="+n+"; bw="+this.bandwidth+"; c="+c);
		Utils.log("ScaledField", ""+Math.sqrt(s/n));
//		this.bandwidth = (float) (c*Math.sqrt((double)s)/Math.sqrt(n) * c_2/n);
		
//		System.out.println("w: " + field.getBoundingRectangle().getWidth() + ", h: " + field.getBoundingRectangle().getHeight());
//		System.out.println(c + "*sqrt(" + s + ")/" + n + " = " + this.bandwidth);
	}
	
	protected void initDensityCalculation()
	{
		int steps = (int)(5*Constants.KDE.CELLS_PER_BANDWIDTH*Constants.KDE.BWRADIUS);
		float step= Constants.KDE.BWRADIUS*this.bandwidth/steps;
		this.densCache = new float[steps];
		
		for(int i=0; i<steps; i++)
		{
			this.densCache[i] = this.calcDensity((float)Math.pow(i*step, 2));
		}
		this.densCacheStep = step;
		this.densCached = true;
	}
	
	//TODO
	protected float calcDensity(float squaredDistance)
	{
		if(!this.densCached)
		{
			double a = 1/(this.bandwidth*Math.sqrt(2*Math.PI));

			return (float) (a * Math.pow(
					Math.E,
					//-1 * (Math.pow(distance ,2) / (2*Math.pow(this.bandwidth, 2)))
					-1 * (squaredDistance / (2*Math.pow(this.bandwidth, 2))) // distance is already squared
				));
		}
		else
		{
			int lowerBound = (int)(Math.sqrt(squaredDistance)/this.densCacheStep);
			int upperBound = lowerBound+1;
			float upperWeight = (float)(Math.sqrt(squaredDistance)/this.densCacheStep - lowerBound);
			float lowerWeight = 1-upperWeight;
			
			return lowerWeight*this.densCache[lowerBound] + upperWeight*this.densCache[upperBound];
		}
	}

}
