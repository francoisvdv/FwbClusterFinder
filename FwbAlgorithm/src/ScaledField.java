import java.util.LinkedList;

public class ScaledField
{
	protected Rectangle rectangle;
	protected Cell[][] grid;
	protected final float SCALE_X, SCALE_Y;
	protected final int GRID_WIDTH,
						GRID_HEIGHT;
	
	public ScaledField(Rectangle rect)
	{
		int maxS = (int)Math.pow(Constants.KDE.MAXGRIDSIZE, 2);
		
		int width = (int) rect.getWidth();
		int height= (int) rect.getHeight();
		
		if(width*height > maxS)
		{
			double r = ((double)width/(double)height);
			double r2= ((double)height/(double)width);
			
			width = (int)Math.sqrt(maxS*r);
			height= (int)Math.sqrt(maxS*r2);
		}
		
		this.GRID_WIDTH = width;
		this.GRID_HEIGHT= height;
		
		this.SCALE_X = (float)rect.getWidth() /this.GRID_WIDTH;
		this.SCALE_Y = (float)rect.getHeight()/this.GRID_HEIGHT;

		this.rectangle = rect;
		this.initialize();
	}
	
	public ScaledField(Rectangle rect, float bandwidth)
	{
		long maxS = (int)Math.pow(Constants.KDE.MAXGRIDSIZE, 2);
		
		float scale = bandwidth/Constants.KDE.CELLS_PER_BANDWIDTH;
		long width = (long) (rect.getWidth() /scale);
		long height= (long) (rect.getHeight()/scale);
		
		if(width*height > maxS)
		{
			double r = ((double)width/(double)height);
			double r2= ((double)height/(double)width);
			
			width = (int)Math.sqrt(maxS*r);
			height= (int)Math.sqrt(maxS*r2);
		}
		
		this.GRID_WIDTH = (int)width;
		this.GRID_HEIGHT= (int)height;
		
		this.SCALE_X = (float)rect.getWidth() /this.GRID_WIDTH;
		this.SCALE_Y = (float)rect.getHeight()/this.GRID_HEIGHT;

		this.rectangle = rect;
		this.initialize();
	}
	
	protected void initialize()
	{
		Utils.log(GRID_WIDTH + "x" + GRID_HEIGHT);
		
		Stopwatch.Timer gridTimer = Stopwatch.startNewTimer("make emty grid");
		this.grid = new Cell[GRID_WIDTH][GRID_HEIGHT];
		gridTimer.stop();
		
		Stopwatch.Timer fillTimer = Stopwatch.startNewTimer("filling the grid");
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
		fillTimer.stop();
	}
	
	// Moet nog ff gecheckt worden, kan zijn dat er aan de randen net iets niet goed zit.
	public Cell[] getCellsCloseTo(Cell cell, float radius)
	{
		int r_x    = scaleX(radius) - scaleX(0);
		int r_y    = scaleY(radius) - scaleY(0);
		int cell_x = scaleX(cell.getMiddleX());
		int cell_y = scaleY(cell.getMiddleY());
		
		int leftBorder = Math.max(cell_x-r_x, 0);
		int rightBorder = Math.min(cell_x+r_x, GRID_WIDTH-1);
		int topBorder = Math.max(cell_y-r_y, 0);
		int botBorder = Math.min(cell_y+r_y, GRID_HEIGHT-1);
		
		LinkedList<Cell> closeCells = new LinkedList<Cell>();
		
		for(int x=leftBorder; x<=rightBorder; x++)
		{
			for(int y=topBorder; y<=botBorder; y++)
			{
				Cell c = this.getCell_scaled(x, y);
				if(c != null)
				{
					float dist = Utils.calcDistance(cell.getMiddleX(), cell.getMiddleY(), c.getMiddleX(), c.getMiddleY());
					if(dist <= radius)
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
	
	public Cell getCell_scaled(int scaledX, int scaledY)
	{
		int x = unscaleX(scaledX);
		int y = unscaleY(scaledY);

		if(!this.rectangle.contains(x, y))
			return null;
		
		Cell cell = this.grid[scaledX][scaledY];
		assert cell.isInCell(x, y);
		
		return cell;
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
		
		if((int)x == GRID_WIDTH)
			return GRID_WIDTH-1;
		
		return (int)x;
	}
	public int scaleY(float y)
	{
		y-=(float)this.rectangle.getTop();
		y/=this.SCALE_Y;
		
		if((int)y == GRID_HEIGHT)
			return GRID_HEIGHT-1;
		
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
	
	public boolean toFile()
	{
		return this.toFile(-1f);
	}
	
	public boolean toFile(float maxDens)
	{
		return this.toFile("outputscaledfield.fwb", maxDens);
	}
	
	public boolean toFile(String filename, float maxDens)
	{
		java.io.File file = new java.io.File(filename);
		if(file.exists());
		{
			try
			{
				java.io.PrintWriter out = new java.io.PrintWriter(new java.io.FileWriter(filename));
				
				out.println("[head]");
				out.println("type: output");
				out.println("source: scaledField");
				out.println("width: " + this.GRID_WIDTH);
				out.println("height: " + this.GRID_HEIGHT);
				if(maxDens >= 0)
					out.println("maxDensity: " + maxDens);
				
				out.println("[body]");
				for(Cell[] row : this.grid)
				{
					for(Cell c : row)
					{
						out.print(c.getDensity() + " ");
					}
					
					out.println();
				}
				
				out.close();
				
				return true;
			}
			catch(java.io.IOException e)
			{
				System.out.println("errooooor, put your computer upside down and try again");
				return false;
			}
			
		}
	}
	
	public Cell getLeftCell(Cell cell)
	{
		int x = scaleX(cell.getMiddleX()) - 1;
		
		if(x < 0)
		{
			return null;
		}
		
		return grid[x][scaleY(cell.getMiddleY())];
	}
	
	public Cell getRightCell(Cell cell)
	{
		int x = scaleX(cell.getMiddleX()) + 1;

		if(x >= GRID_WIDTH)
		{
			return null;
		}
		
		return grid[x][scaleY(cell.getMiddleY())];
	}
	
	public Cell getUpperCell(Cell cell)
	{
		int y = scaleY(cell.getMiddleY()) - 1;
		
		if(y < 0)
		{
			return null;
		}
		
		return grid[scaleX(cell.getMiddleX())][y];
	}
	
	public Cell getBottomCell(Cell cell)
	{
		int y = scaleY(cell.getMiddleY()) + 1;
		
		if(y >= GRID_HEIGHT)
		{
			return null;
		}
		
		return grid[scaleX(cell.getMiddleX())][y];
	}
	
	public Stats getStats()
	{
		Stats stats = new Stats();
		this.updateCellStats(stats);
		return stats;
	}
	
	protected void updateCellStats(Stats stats)
	{
		boolean noZero = false;
		
		int numberCells = GRID_WIDTH*GRID_HEIGHT;
		float totalDens = 0;
		float maxDens = 0;
		float minDens = 0;
		
		for(int i=0;i<this.GRID_WIDTH;i++)
		{
			for(int j=0;j<this.GRID_HEIGHT;j++)
			{
				float dens = grid[i][j].getDensity();
				totalDens += dens;
				maxDens = Math.max(dens, maxDens);
				minDens = Math.min(dens, minDens);
				
				if(noZero && grid[i][j].getDensity() == 0)
					numberCells--;
			}
		}
		
		stats.avgCellDens = totalDens/numberCells;
		stats.maxCellDens = maxDens;
		stats.minCellDens = minDens;
	}
	
	public class Stats
	{
		public float maxPointDens = -1;
		public float minPointDens = -1;
		public float avgPointDens = -1;
		public float maxCellDens = -1;
		public float avgCellDens = -1;
		public float minCellDens = -1;
		
		public Stats() {}
	}
}