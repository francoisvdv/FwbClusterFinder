import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;



public class CheckBoxList extends JList
{
	private static final long serialVersionUID = 1847949973071107145L;
	
	public TestProgram tp;
	ClusterInfo[] clusterinfo;
	CheckListener listener;
	
	public CheckBoxList(ArrayList<Cluster> clusters, Noise noise, TestProgram tp) 
	{
		super();
		
		this.tp = tp;
		
		clusterinfo = new ClusterInfo[clusters.size() + 1];
		clusterinfo[0] = new ClusterInfo("Noise", 0, noise);
		for(int i = 1; i <= clusters.size(); i++)
		{
			clusterinfo[i] = new ClusterInfo("Cluster", Integer.parseInt(clusters.get(i - 1).toString()), clusters.get(i - 1));
		}
		this.setListData(clusterinfo);
		
		CheckListCellRenderer renderer = new CheckListCellRenderer();
		this.setCellRenderer(renderer);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		listener = new CheckListener(this);
		this.addMouseListener(listener);
		this.addKeyListener(listener);
	}
	
	public void selectAll()
	{
		listener.selectAll(true);
	}
}

class CheckListCellRenderer extends JCheckBox implements ListCellRenderer
{
	private static final long serialVersionUID = -7676613980443594965L;
	
	protected static Border border = new EmptyBorder(1, 1, 1, 1);
	
	public CheckListCellRenderer() 
	{
		super();
		setOpaque(true);
		setBorder(border);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		setText(value.toString());
		setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
		setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

		ClusterInfo info = (ClusterInfo) value;
		setSelected(info.isSelected());
		setFont(list.getFont());
		setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : border);
		return this;
	}
}

class CheckListener implements MouseListener, KeyListener
{
	CheckBoxList list;
	boolean checkAll = true;

	public CheckListener(CheckBoxList list) 
	{
		this.list = list;
	}
	
	public void selectAll(boolean b)
	{
		checkAll = b;
		ListModel lm = list.getModel();
		for(int i = 0; i < lm.getSize(); i++)
		{
			if(((ClusterInfo) lm.getElementAt(i)).isSelected() != b)
			{
				// If it has to become selected
				if(b == true)
				{
					list.tp.showClusterNoRepaint(((ClusterInfo) lm.getElementAt(i)).getCategory());
				}
				// If it has to become unselected
				else
				{
					list.tp.hideClusterNoRepaint(((ClusterInfo) lm.getElementAt(i)).getCategory());
				}
				((ClusterInfo) lm.getElementAt(i)).setSelected(b);
			}
			
		}
		
		list.repaint();
		list.getParent().validate();
		
		list.tp.repaintContentPanel();
	}

	public void mouseClicked(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			if (e.getX() < 20)
			{
				doCheck();
			}
		}
		else
		{
			selectAll(!checkAll);
		}
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyChar() == ' ')
			doCheck();
	}

	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	protected void doCheck() 
	{

		int index = list.getSelectedIndex();
		if (index < 0)
		{
			return;
		}
		
		ClusterInfo info = (ClusterInfo) list.getModel().getElementAt(index);
		info.checkOrDeCheck();
		if(info.isSelected())
		{
			list.tp.showCluster(info.getCategory());
		}
		else
		{
			list.tp.hideCluster(info.getCategory());
		}
		list.repaint();
	}
}

class ClusterInfo
{
	String name;
	int number;
	PointCategory pc;
	boolean selected;

	public ClusterInfo(String name, int number, PointCategory pc) 
	{
		this.pc = pc;
		this.name = name;
		this.number = number;
		setSelected(true);
	}

	public PointCategory getCategory()
	{
		return pc;
	}
	
	public String getName() 
	{
		return name;
	}

	public int getClusterNumber()
	{
		return number;
	}

	public void setSelected(boolean b)
	{
		this.selected = b;
	}

	public boolean isSelected()
	{
		return selected;
	}
	
	public void checkOrDeCheck()
	{
		selected = !selected;
	}

	public String toString()
	{
		return name +" ("+ number +")";
	}
}