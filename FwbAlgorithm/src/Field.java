import java.util.ArrayList;

public final class Field extends PointCollection
{
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private Noise noise = new Noise();
	
	public Field(ArrayList<Point> points)
	{
		super.addAll(points);
	}
	
	public Cluster createCluster()
	{
		Cluster c = new Cluster(0);
		clusters.add(c);
		return c;
	}
	
	public void setCluster(Point point, Cluster c)
	{
		removeFromOldCategory(point);
		
		point.setPointCategory(c);
		c.add(point);
	}
	public void setNoise(Point point)
	{
		removeFromOldCategory(point);
		
		point.setPointCategory(noise);
		noise.add(point);
	}
	
	private void removeFromOldCategory(Point point)
	{
		if(point == null)
			return;
		
		PointCategory category = point.getPointCategory();
		
		if(category == null)
			return;
		
		category.remove(point);
		if(category instanceof Cluster && category.size() == 0 && clusters.contains(category))
			clusters.remove(category);
	}
}