public class KDE
{
	protected ScaledField scaledField;
	protected Field field;
	protected float bandwidth;
	
	public KDE(Field field)
	{
		this.field = field;
	}
	
	public void initialize()
	{
		this.calcBandwidth();
		
		Object[] points = this.field.toArray();
		for(int i=0; i<points.length; i++)
		{
			//Hier valt snelheid te winnen: getCellsCloseTo berekend afstanden die in deze methode opnieuw berekend worden.
			
			Point p = (Point) points[i];
			Cell c = this.scaledField.getCell(p);
			Cell[] cells = this.scaledField.getCellsCloseTo(c, 3*this.bandwidth);
			
			for(int j=0; j<cells.length; j++)
			{
				Cell cell = cells[j];
				float sqdist = Gonio.calcSquaredDistance(cell.getMiddleX(), cell.getMiddleY(), p.getX(), p.getY());
				cell.increaseDensity(this.calcDensity(sqdist));
			}
		}
	}
	
	//TODO
	// http://en.wikipedia.org/wiki/Kernel_density_estimation#Practical_estimation_of_the_bandwidth
	protected void calcBandwidth()
	{
		this.bandwidth = 2;
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
