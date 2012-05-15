import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class ContentPanel extends JPanel
{
	Field field;
	Rectangle bounding;
	
	float zoomFactor = 1;
	
	public ContentPanel()
	{
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
		
		Graphics g = g1.create();
		g.clearRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
		g.setColor(Color.BLACK);
		
		for(Point p : field)
		{
			g.drawLine(p.getX(), p.getY(), p.getX(), p.getY());
		}
	}
}