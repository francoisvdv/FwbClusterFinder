public abstract class PointCategory extends PointCollection
{
	private static final long serialVersionUID = -2596019949223210809L;
	public static int lastIndex = 0;
	protected int index = 0;
	
	/**
	 * @pre index >= 0
	 * @param index
	 */
	public PointCategory()
	{
		this(true);
	}

	public PointCategory(boolean inc)
	{
		if(inc)
			this.index = ++lastIndex;
	}
	
	protected static void resetIndex()
	{
		lastIndex = 0;
	}
	
	@Override
	public String toString()
	{
		return Integer.toString(this.index);
	}
	
	@Override
	public boolean add(Point point)
	{
		assert point.getPointCategory() == this;
		return super.add(point);
	}
}
