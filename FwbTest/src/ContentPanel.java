import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class ContentPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private static final long serialVersionUID = 7727860461987332520L;
	
	public static final int SELECT_SQUARE = 0;
	public static final int SELECT_CIRCLE = 1;
	
	Field field;
	Rectangle bounding;
	
	//View vars
	int pointWidth = 4;
	int pointHeight = 4;
	
	float zoomFactor = 1;
	int offsetX = 0;
	int offsetY = 0;
	
	Random random = new Random();
	HashMap<PointCategory, Color> colors = new HashMap<PointCategory, Color>();
	
	//Input vars
	int selectionMode = SELECT_CIRCLE;
	boolean keyCtrl = false;
	boolean mousePressed = false;
	int startX, startY;
	int currentX, currentY;
	
	
	public ContentPanel()
	{
		super();
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		setLayout(new BorderLayout());
		setIgnoreRepaint(true);
		
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
	
	public void center()
	{
		offsetX = 0;
		offsetY = 0;
		zoomFactor = 1;
	}
	
	public void setField(Field field)
	{
		colors.clear();
		colors.put(null, Color.WHITE);
		colors.put(field.getNoise(), Color.WHITE);
		
		this.field = field;
		this.bounding = field.getBoundingRectangle();
		repaint();
	}
	public void setSelectionMode(int mode)
	{
		selectionMode = mode;
	}
	
	public Color createOrGetColor(PointCategory category)
	{
		if(!colors.containsKey(category))
			return colors.put(category, new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
		
		return colors.get(category);
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
		g.fillRect(0, 0, getWidth(), getHeight());
		
		for(int i = 0; i < field.size(); i++)
		{
			Point p = field.get(i);
			
			g.setColor(createOrGetColor(p.getPointCategory()));
			
			float relX = AbsoluteToRelativeX(p.getX());
			float relY = AbsoluteToRelativeY(p.getY());

			int x = offsetX + (int)(relX * dimension) + ((getWidth() - dimension) / 2) - (pointWidth / 2);
			int y = offsetY + (int)(relY * dimension) + ((getHeight() - dimension) / 2) - (pointHeight / 2);
			
			g.fillOval(x, y, pointWidth, pointHeight);
		}

		if(!keyCtrl && mousePressed)
		{
			int x1, x2, y1, y2, w, h;
			x1 = currentX > startX ? startX : currentX;
			x2 = currentX > startX ? currentX : startX;
			y1 = currentY > startY ? startY : currentY;
			y2 = currentY > startY ? currentY : startY;
			w = x2 - x1;
			h = y2 - y1;
			
			g.setColor(Color.GRAY);
			
			//if(selectionMode == SELECT_SQUARE)
				g.drawRect(x1, y1, w, h);
			if(selectionMode == SELECT_CIRCLE)
				g.drawOval(x1, y1, w, h);
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
		currentX = startX;
		currentY = startY;
		repaint();
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{
		mousePressed = false;
		repaint();
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
		}
		else
		{
			currentX = e.getX();
			currentY = e.getY();
		}
		
		repaint();
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