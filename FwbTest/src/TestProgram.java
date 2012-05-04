import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class TestProgram extends JFrame implements ActionListener
{
	
	private static final long serialVersionUID = 6814435897208431145L;
	
	JPanel panel;
	JMenuBar menu;
	JMenu menu_generate, menu_view, menu_cluster;
	JMenuItem menuitem_noise, menuitem_save, menuitem_open, menuitem_circel, menuitem_square;
	
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
		
		// Close operation
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set the panel
		panel = new JPanel();
		this.setContentPane(panel);
		
		// Menuknopje "Generate"
		menu_generate = new JMenu("Generate");
		menuitem_noise = new JMenuItem("Noise");
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
		JFrame.setDefaultLookAndFeelDecorated(true);
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
	}

}

