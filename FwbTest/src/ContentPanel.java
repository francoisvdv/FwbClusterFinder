import java.awt.Graphics;

import javax.swing.JPanel;

public class ContentPanel extends JPanel
{
	
	Field field;
	
	public ContentPanel()
	{
	}
	
	public void setField(Field field)
	{
		this.field = field;
		invalidate();
	}
	
	@Override
	public void paint(Graphics g)
	{
		// TODO Auto-generated method stub
		super.paint(g);
	}
}