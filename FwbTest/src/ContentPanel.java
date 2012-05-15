import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class ContentPanel extends JPanel
{
	Field field;
	Rectangle bounding;
	
	int pointWidth = 4;
	int pointHeight = 4;
	float zoomFactor = 1;
	
	public ContentPanel()
	{
		super();
	}
	
	public Field getField()
	{
		return field;
	}
	public void setField(Field field)
	{
		this.field = field;
		this.bounding = field.getBoundingRectangle();
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g1)
	{
		// TODO Auto-generated method stub
		super.paintComponent(g1);
		
		if(field == null)
			return;
		
		int dimension = getWidth() > getHeight() ? getHeight() : getWidth();
		
		Graphics g = g1.create();
		g.setColor(Color.BLACK);

		for(Point p : field)
		{
			float relX = AbsoluteToRelativeX(p.getX());
			float relY = AbsoluteToRelativeY(p.getY());

			int x = (int)(relX * dimension) + ((getWidth() - dimension) / 2) - (pointWidth / 2);
			int y = (int)(relY * dimension) + ((getHeight() - dimension) / 2) - (pointHeight / 2);
			
			g.fillOval(x, y, pointWidth, pointHeight);
		}
	}
	
	/**
	 * Get relative position on screen. E.g. you have leftmost point x=0 and the rightmost x=10.000, then
	 * this function will return 0.5f for point at x=5.000.
	 * @param x
	 * @return
	 */
	public float AbsoluteToRelativeX(float x)
	{
		return (float)(x - bounding.x1) / (float)(bounding.x2 - bounding.x1);
	}
	/**
	 * Get relative position on screen. E.g. you have leftmost point with y=0 and the rightmost with y=10.000, then
	 * this function will return 0.5f for point at y=5.000.
	 * @param x
	 * @return
	 */
	public float AbsoluteToRelativeY(float y)
	{
		return (float)(y - bounding.y1) / (float)(bounding.y2 - bounding.y1);
	}
}