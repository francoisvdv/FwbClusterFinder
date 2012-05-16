import java.util.LinkedList;

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
	
	// Moet nog ff gecheckt worden, kan zijn dat er aan de randen net iets niet goed zit.
	public Cell[] getCellsCloseTo(Cell cell, float radius)
	{
		int r_x    = (int)Math.floor(radius/SCALE_X);
		int r_y    = (int)Math.floor(radius/SCALE_Y);
		int cell_x = (int)Math.floor(cell.getMiddleX()/SCALE_X);
		int cell_y = (int)Math.floor(cell.getMiddleY()/SCALE_Y);
		
		LinkedList<Cell> closeCells = new LinkedList<Cell>();
		
		for(int x=cell_x-r_x; x<cell_x+r_x; x++)
		{
			for(int y=cell_y-r_y; y<cell_y+r_y; y++)
			{
				Cell c = this.getCell((int)(x*SCALE_X), (int)(y*SCALE_Y));
				Float dist = Gonio.calcDistance(cell.getMiddleX(), cell.getMiddleY(), c.getMiddleX(), c.getMiddleY());
				if(dist < radius)
				{
					closeCells.add(c);
				}
			}
		}
		
		return (Cell[])closeCells.toArray();
	}
	
	public Cell getCell(int x, int y)
	{
		int scaled_x = (int) Math.floor(x/this.SCALE_X);
		int scaled_y = (int) Math.floor(y/this.SCALE_Y);
		
		Cell cell = this.grid[scaled_x][scaled_y];
		assert cell.isInCell(x, y);
		
		return cell;
	}
	
	public Cell getCell(Point p)
	{
		return getCell(p.getX(), p.getY());
	}
}