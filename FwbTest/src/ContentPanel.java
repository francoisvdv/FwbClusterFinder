import java.awt.Color;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

public class ContentPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener
{
	Field field;
	Rectangle bounding;
	
	int pointWidth = 4;
	int pointHeight = 4;
	
	float zoomFactor = 1;
	int offsetX = 0;
	int offsetY = 0;
	
	boolean keyCtrl = false;
	boolean mousePressed = false;
	int startX, startY;
	
	public ContentPanel()
	{
		super();
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher()
		{
			@Override
			public boolean dispatchKeyEvent(KeyEvent e)
			{
				// TODO Auto-generated method stub
				keyCtrl = e.isControlDown();
				return false;
			}
		});
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
		
		int dimension = (int)(zoomFactor * (getWidth() > getHeight() ? getHeight() : getWidth()));
		
		Graphics g = g1.create();
		g.setColor(Color.BLACK);

		for(int i = 0; i < field.size(); i++)
		{
			Point p = field.get(i);
			float relX = AbsoluteToRelativeX(p.getX());
			float relY = AbsoluteToRelativeY(p.getY());

			int x = offsetX + (int)(relX * dimension) + ((getWidth() - dimension) / 2) - (pointWidth / 2);
			int y = offsetY + (int)(relY * dimension) + ((getHeight() - dimension) / 2) - (pointHeight / 2);
			
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
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		// TODO Auto-generated method stub

		if(e.getWheelRotation() > 0)
			zoomFactor /= 1.5f;
		else if(e.getWheelRotation() < 0)
			zoomFactor *= 1.5f;
		
		repaint();
	}
	@Override
	public void mousePressed(MouseEvent e)
	{
		mousePressed = true;
		startX = e.getX();
		startY = e.getY();
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{
		mousePressed = false;
	}
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(keyCtrl)
		{
			offsetX += e.getX() - startX;
			offsetY += e.getY() - startY;
			startX = e.getX();
			startY = e.getY();
			
			repaint();
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{

	}
	@Override
	public void mouseClicked(MouseEvent e)
	{
	}
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}
	@Override
	public void mouseExited(MouseEvent e)
	{
	}
}