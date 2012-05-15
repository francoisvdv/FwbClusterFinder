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
		invalidate();
	}
	
	@Override
	public void paintComponent(Graphics g1)
	{
		// TODO Auto-generated method stub
		super.paintComponent(g1);
		
		if(field == null)
			return;
		
		Graphics g = g1.create();
		g.setColor(Color.BLACK);
		
		for(Point p : field)
		{
			System.out.println(bounding.getLeft() + " | " + bounding.getRight() + " | " + (bounding.getRight() - bounding.getLeft()));
			System.out.println(bounding.getTop() + " | " + bounding.getBottom() + " | " + (bounding.getBottom() - bounding.getTop()));
			
			float x = (float)(p.getX() - bounding.x1) / (float)(bounding.x2 - bounding.x1);
			float y = (float)(p.getY() - bounding.y1) / (float)(bounding.y2 - bounding.y1);

			g.fillOval((int)(x * getWidth()) - pointWidth / 2, (int)(y * getHeight()) - pointHeight / 2, pointWidth, pointHeight);
		}
	}
}