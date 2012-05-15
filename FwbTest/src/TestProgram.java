import java.awt.*;
import java.awt.event.*;
import java.util.ListIterator;

import javax.swing.*;

public class TestProgram extends JFrame implements ActionListener
{
	
	private static final long serialVersionUID = 6814435897208431145L;
	
	ContentPanel panel;
	JMenuBar menu;
	JMenu menu_generate, menu_view, menu_cluster;
	JMenuItem menuitem_noise, menuitem_save, menuitem_open, menuitem_circel, menuitem_square;
	
	Field field;
	
	/**
	 * Class constructor - make the JFrame ready
	 */
	public TestProgram() 
	{
		// Set screensize
		Toolkit tk = Toolkit.getDefaultToolkit();  
		int x = (int) tk.getScreenSize().getWidth() - 150;  
		int y = (int) tk.getScreenSize().getHeight() - 100;  
		this.setSize(x, y);
		
		// Some settings
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Friends With Benefits - Test program");
		field = new Field();

		// Set the panel
		// TODO		
		panel = new ContentPanel();
		this.setContentPane(panel);
		
		// Menuknopje "Generate"
		menu_generate = new JMenu("Generate");
		menuitem_noise = new JMenuItem("Noise");
		menuitem_noise.addActionListener(this);
		menu_generate.add(menuitem_noise);
		
		menu_cluster = new JMenu("Cluster");
		menuitem_circel = new JMenuItem("Cirkel");
		menu_cluster.add(menuitem_circel);
		menuitem_square = new JMenuItem("Square");
		menu_cluster.add(menuitem_square);
		menu_generate.add(menu_cluster);
		
		menu_generate.add(new JSeparator());
		
		menuitem_save = new JMenuItem("Save");
		menuitem_save.addActionListener(this);
		menu_generate.add(menuitem_save);
		
		// Menuknopje "View"	
		menu_view = new JMenu("View");
		menuitem_open = new JMenuItem("Open");
		menuitem_open.addActionListener(this);
		menu_view.add(menuitem_open);
		
		menu = new JMenuBar();
		menu.add(menu_generate);
		menu.add(menu_view);
		this.setJMenuBar(menu);
	}
	
	/**
	 * Show at the middle of the screen
	 */
	public void start() 
	{
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	/**
	 * @param args command line params
	 */
	public static void main(String[] args) 
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception x){}
		// Francois lelijk
		//JDialog.setDefaultLookAndFeelDecorated(true);
		//JFrame.setDefaultLookAndFeelDecorated(true);
		new TestProgram().start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == menuitem_open)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(new InputFileFilter(false));
			int returnValue = chooser.showOpenDialog(this);
			if(returnValue == JFileChooser.APPROVE_OPTION) 
			{
				boolean approved = ((InputFileFilter) chooser.getFileFilter())
						.isFileApproved(chooser.getSelectedFile());
				System.out.println(approved);
			}
		}
		else if(e.getSource() == menuitem_save)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(new InputFileFilter(true));
			int returnValue = chooser.showSaveDialog(this);
			if(returnValue == JFileChooser.APPROVE_OPTION) 
			{
				boolean approved = ((InputFileFilter) chooser.getFileFilter())
						.isFileApproved(chooser.getSelectedFile());
				System.out.println(approved);
			}
		}
		else if(e.getSource() == menuitem_noise)
		{
			String s = JOptionPane.showInternalInputDialog(panel, "How many points?", "Add noise", JOptionPane.QUESTION_MESSAGE);
			if(s != null)
			{
				int number;
				try
				{
					number = Integer.parseInt(s);
				}
				catch(Exception ex)
				{
					number = 0;
				}
				
				long time = System.currentTimeMillis();
				
				for(int i = 0; i < number; i++)
				{
					
					int x = (int) Math.round(Math.random() * 1000000000);
					int y;
					
					boolean busy = true;
					while(busy)
					{
						y = (int) Math.round(Math.random() * 1000000000);
						
						// doesn't depend on clusters, but takes LOOOONNGGG time
						// times:
						// add 10.000  noise to an empty   field: 6187   millisec.
						// add 10.000  noise to a  10.000  field: 17771  millisec.
						// add 100.000 noise to an empty   field: >> 5 minutes launch terminated
						//ListIterator<Point> iterator = field.listIterator();
						//boolean inside = false;
						//while(iterator.hasNext())
						//{
						//	Point point = iterator.next();
						//	if(point.getX() == x && point.getY() == y)
						//	{
						//		inside = true;
						//		break;
						//	}
						//}
						//
						//if(!inside)
						//{
						//	field.add(new Point(x, y));
						//	busy = false;
						//}
						
						
						// doesn't depend on clusters
						// times:
						// add 10.000  noise to an empty   field: 1493   millisec.
						// add 10.000  noise to a  10.000  field: 3652   millisec.
						// add 100.000 noise to an empty   field: 97948 & 143442  millisec.
						// add 100	   noise to a  100.000 field: 316   & 281     millisec.
						// add 1.000   noise to a  100.100 field: 1435  & 1462    millisec.
						boolean inside = false;
						Object[] array = field.toArray();
						for(int j = 0; j < field.size(); j++)
						{
							if(((Point) array[j]).compareTo(x, y))
							{
								inside = true;
								break;
							}
						}
						
						if(!inside)
						{
							field.add(new Point(x, y));
							busy = false;
						}
					}
				}
				
				System.out.println("Took: "+ (System.currentTimeMillis() - time));
				System.out.println(field.size());
				
				
				time = System.currentTimeMillis();
				Object[] obj = field.toArray();
				//TOOK 2
				System.out.println(obj.length);
				int count = 0;
				for(int i = 0; i < field.size(); i++)
				{
					((Point) obj[i]).compareTo(0, 0);
				}
				System.out.println(count);
				System.out.println("Array Took: "+ (System.currentTimeMillis() - time));
				
				time = System.currentTimeMillis();
				ListIterator<Point> it = field.listIterator();
				count = 0;
				// TOOK 14
				while(it.hasNext())
				{
					it.next().compareTo(0, 0);
				}
				System.out.println(count);
				System.out.println("Iterator Took: "+ (System.currentTimeMillis() - time));
			}
		}
	}

}
