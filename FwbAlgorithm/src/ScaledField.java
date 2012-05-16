import java.util.LinkedList;

public class ScaledField
{
	protected Rectangle rectangle;
	protected Cell[][] grid;
	protected final float SCALE_X, SCALE_Y;
	protected final int GRID_WIDTH  = 1000,
						GRID_HEIGHT = 1000;
	
	public ScaledField(Rectangle rect)
	{
		this.rectangle = rect;
		
		this.SCALE_X = (float)rect.getWidth() /this.GRID_WIDTH;
		this.SCALE_Y = (float)rect.getHeight()/this.GRID_HEIGHT;
		
		this.grid = new Cell[GRID_WIDTH][GRID_HEIGHT];
		
		for(int x=0; x<GRID_WIDTH; x++)
		{
			for(int y=0; y<GRID_HEIGHT; y++)
			{
				this.grid[x][y] =  new Cell(unscaleX(x),
											unscaleY(y),
											unscaleX(x+1),
											unscaleY(y+1));
			}
		}
	}
	
	// Moet nog ff gecheckt worden, kan zijn dat er aan de randen net iets niet goed zit.
	public Cell[] getCellsCloseTo(Cell cell, float radius)
	{
		int r_x    = scaleX(radius);
		int r_y    = scaleY(radius);
		int cell_x = scaleX(cell.getMiddleX());
		int cell_y = scaleY(cell.getMiddleY());
		
		LinkedList<Cell> closeCells = new LinkedList<Cell>();
		
		for(int x=cell_x-r_x; x<cell_x+r_x; x++)
		{
			for(int y=cell_y-r_y; y<cell_y+r_y; y++)
			{
				Cell c = this.getCell(unscaleX(x), unscaleY(y));
				if(c != null)
				{
					Float dist = Gonio.calcDistance(cell.getMiddleX(), cell.getMiddleY(), c.getMiddleX(), c.getMiddleY());
					if(dist < radius)
					{
						closeCells.add(c);
					}
				}
			}
		}
		
		Cell[] cells = new Cell[closeCells.size()];
		return closeCells.toArray(cells);
	}
	
	public Cell getCell(Point point)
	{
		return getCell(point.getX(), point.getY());
	}
	
	public Cell getCell(int x, int y)
	{
		if(!this.rectangle.contains(x, y))
			return null;

		int scaled_x = scaleX(x);
		int scaled_y = scaleY(y);

		Cell cell = this.grid[scaled_x][scaled_y];
		assert cell.isInCell(x, y);
		
		return cell;
	}
	
	public int scaleX(int x)
	{
		return scaleX((float)x);
	}
	public int scaleY(int y)
	{
		return scaleY((float)y);
	}
	public int unscaleX(int x)
	{
		return unscaleX((float)x);
	}
	public int unscaleY(int y)
	{
		return unscaleY((float)y);
	}
	
	public int scaleX(float x)
	{
		x-=(float)this.rectangle.getLeft();
		x/=this.SCALE_X;
		return (int)x;
	}
	public int scaleY(float y)
	{
		y-=(float)this.rectangle.getTop();
		y/=this.SCALE_Y;
		return (int)y;
	}
	public int unscaleX(float x)
	{
		x*=this.SCALE_X;
		x+=(float)this.rectangle.getLeft();
		return (int)x;
	}
	public int unscaleY(float y)
	{
		y*=this.SCALE_Y;
		y+=(float)this.rectangle.getTop();
		return (int)y;
	}
}