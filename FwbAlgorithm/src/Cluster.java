public class Cluster extends PointCategory
{
	int index;
	
	/**
	 * @pre index > 0
	 * @param index
	 */
	public Cluster(int index)
	{
		assert index > 0;
		
		this.index = index;
	}

	@Override
	public String toString()
	{
		return Integer.toString(index);
	}
}
