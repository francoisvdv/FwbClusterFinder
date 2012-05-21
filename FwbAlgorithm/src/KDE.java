// TODO: scaled field moet ergens gemaakt worden (in KDE of in Field)

public class KDE
{
	public    ScaledField scaledField;
	protected Field field;
	protected float bandwidth;
	protected Point[] sortedPoints;
	
	public KDE(Field field)
	{
		this.field = field;
		Rectangle r = field.getBoundingRectangle();
		this.scaledField = new ScaledField(r);
	}
	
	public void initialize()
	{
		this.calcBandwidth();
		
		Point[] points = new Point[this.field.size()];
		this.field.toArray(points);
		
		for(int i=0; i<points.length; i++)
		{
			//Hier valt snelheid te winnen: getCellsCloseTo berekent afstanden die in deze methode opnieuw berekend worden.
			
			Point p = (Point) points[i];
			Cell c = this.scaledField.getCell(p);
			Cell[] cells = this.scaledField.getCellsCloseTo(c, 3*this.bandwidth);
			
			for(int j=0; j<cells.length; j++)
			{
				Cell cell = cells[j];
				float sqdist = Utils.calcSquaredDistance(cell.getMiddleX(), cell.getMiddleY(), p.getX(), p.getY());
				cell.increaseDensity(this.calcDensity(sqdist));
			}
		}
		
		this.sortedPoints = this.sort(points);
	}
	
	public float getMaxDensity()
	{
		return scaledField.getCell(sortedPoints[sortedPoints.length-1]).getDensity();
	}
	
	public int getPointCountAboveThreshold(float threshold)
	{
		return this.getPointCountAboveThreshold(threshold, 0, sortedPoints.length-1);
	}
	
	// uses binarysearch
	public int getPointCountAboveThreshold(float threshold, int start, int end)
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
	
	protected int getPointCountAboveThreshold_moveLeft(float threshold, int middle)
	{
		while(middle >= 0 && scaledField.getCell(sortedPoints[middle--]).getDensity() == threshold);
		
		return middle+1;
	}
	
	// uses quicksort
	// change to some form of counting sort
	protected Point[] sort(Point[] unsorted)
	{
		Point[] sorted = unsorted.clone();
		this.sort_recursive(sorted, 0, sorted.length-1);
		return sorted;
	}
	
	// in-place version of quicksort
	protected void sort_recursive(Point[] partiallySorted, int start, int end)
	{
		if(start >= end)
			return;
		
		int pivotPos = getPivotPos(start, end);
		pivotPos = partition(partiallySorted, start, end, pivotPos);
		this.sort_recursive(partiallySorted, start, pivotPos-1);
		this.sort_recursive(partiallySorted, pivotPos+1, end);
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
		
		// var used for swapping
		Point temp;
		
		// make a pivot
		float pivot = scaledField.getCell(partiallySorted[pivotPos]).getDensity();
		// place pivot at the end
		temp = partiallySorted[pivotPos];
		partiallySorted[pivotPos] = partiallySorted[end];
		partiallySorted[end] = temp;
		// "remove" pivot from array
		end--;
		// make two empty lists: smaller and greater or equal
		//  list smaller:          between start and middle, including both borders
		//  list greater or equal: between middle+1 and i-1, including both borders
		int middle = start-1;
		
		for(int i=start; i<=end; i++)
		{
			// density at point
			float dens = scaledField.getCell(partiallySorted[i]).getDensity();
			if(dens < pivot)
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
		// move pivot
		temp = partiallySorted[++middle];
		partiallySorted[middle] = partiallySorted[partiallySorted.length-1];
		partiallySorted[partiallySorted.length-1] = temp;
		
		return middle;
	}
	
	// chooses a pivot between start and end (random or not)
	protected int getPivotPos(int start, int end)
	{
		return end;
	}
	
	//TODO
	// http://en.wikipedia.org/wiki/Kernel_density_estimation#Practical_estimation_of_the_bandwidth
	protected void calcBandwidth()
	{
		this.bandwidth = 500;
	}
	
	//TODO
	protected float calcDensity(float squaredDistance)
	{
		double a = 1/(this.bandwidth*Math.sqrt(2*Math.PI));
		
		return (float) (a * Math.pow(
				Math.E,
				//-1 * (Math.pow(distance ,2) / (2*Math.pow(this.bandwidth, 2)))
				-1 * (squaredDistance / (2*Math.pow(this.bandwidth, 2))) // distance is al squared
			));
	}
}
