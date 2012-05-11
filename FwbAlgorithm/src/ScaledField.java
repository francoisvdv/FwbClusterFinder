public class ScaledField
{
	protected Cell[][] grid;
	protected final float SCALE_X, SCALE_Y;
	protected final int GRID_WIDTH  = 10000,
						GRID_HEIGHT = 10000;
	
	public ScaledField(int width, int height)
	{
		this.SCALE_X = width /this.GRID_WIDTH;
		this.SCALE_Y = height/this.GRID_HEIGHT;
		
		this.grid = new Cell[GRID_WIDTH][GRID_HEIGHT];
		
		for(int x=0; x<GRID_WIDTH; x++)
		{
			for(int y=0; y<GRID_HEIGHT; y++)
			{
				this.grid[x][y] =  new Cell((int)Math.floor(x*SCALE_X),
											(int)Math.floor(y*SCALE_Y),
											(int)Math.floor((x+1)*SCALE_X),
											(int)Math.floor((y+1)*SCALE_Y));
			}
		}
	}
	
	public Cell getCell(int x, int y)
	{
		int scaled_x = (int) Math.floor(x/this.SCALE_X);
		int scaled_y = (int) Math.floor(y/this.SCALE_Y);
		
		Cell cell = this.grid[scaled_x][scaled_y];
		assert cell.isInCell(x, y);
		
		return cell;
	}
	
	public static Cell getCell(Point point)
	{
		return getCell(point.getX(), point.getY());
	}
}