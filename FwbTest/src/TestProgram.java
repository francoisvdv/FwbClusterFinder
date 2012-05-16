import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class TestProgram extends JFrame implements ActionListener
{
	
	static final long serialVersionUID = 6814435897208431145L;
	static Font f;
	
	ContentPanel contentpanel;
	JPanel menupanel;
	Container c;
	GroupLayout layout;
	
	JButton open, save, addnoise, addacluster;
	JFileChooser chooser;
	JTextField clustersize;
	ButtonGroup buttons;
	JRadioButton circle, square;
	
	File loadedFile;
	
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
		this.setTitle("Friends With Benefits - Test program");
		field = new Field();

		// Set the panel
		c = this.getContentPane();
		c.setLayout(new BorderLayout());
		
		contentpanel = new ContentPanel();
		c.add(contentpanel, BorderLayout.CENTER);
		
		// Menupanel
		menupanel = new JPanel();
		layout = new GroupLayout(menupanel);
		menupanel.setLayout(layout);
		c.add(menupanel, BorderLayout.EAST);
		
		open = new JButton("Open");
		open.setFocusPainted(false);
		save = new JButton("Save");
		save.setFocusPainted(false);
		open.addActionListener(this);
		save.addActionListener(this);

		
		addnoise = new JButton("Add noise");
		addnoise.setFocusPainted(false);
		addnoise.addActionListener(this);
		
		circle = new JRadioButton("Circle");
		circle.setFocusPainted(false);
		square = new JRadioButton("Square");
		square.setFocusPainted(false);
		circle.setSelected(true);
		
		buttons = new ButtonGroup();
		buttons.add(circle);
		buttons.add(square);
		
		clustersize = new JTextField();
		addacluster = new JButton("Add");
		
		JSeparator sep1 = new JSeparator();
		JSeparator sep2 = new JSeparator();
		JSeparator sep3 = new JSeparator();
		JPanel empty = new JPanel();
		JLabel addcluster = new JLabel("Add cluster:");
		JLabel addclustersize = new JLabel("Size:");
		addcluster.setFont(f);
		
		contentpanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
					.addComponent(open)
					.addGap(2)
					.addComponent(save))
				.addComponent(sep1)
				.addComponent(addnoise)
				.addComponent(sep2)
				.addComponent(addcluster)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(circle)
					.addComponent(square))
				.addComponent(addclustersize)
				.addComponent(clustersize)
				.addComponent(addacluster)
				.addComponent(sep3)
				.addComponent(empty)
				
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(open)
					.addComponent(save))
				.addGap(5)
				.addComponent(sep1)
				.addGap(5)
				.addComponent(addnoise)
				.addGap(5)
				.addComponent(sep2)
				.addGap(5)
				.addComponent(addcluster)
				.addComponent(circle)
				.addComponent(square)
				.addGap(3)
				.addComponent(addclustersize)
				.addComponent(clustersize)
				.addGap(3)
				.addComponent(addacluster)
				.addGap(5)
				.addComponent(sep3)
				.addComponent(empty)
		);
		
		int width = menupanel.getPreferredSize().width;
		addacluster.setMinimumSize(new Dimension(width, addacluster.getPreferredSize().height));
		clustersize.setMaximumSize(new Dimension(width, clustersize.getPreferredSize().height));
		addcluster.setMinimumSize(new Dimension(width - 10, addcluster.getPreferredSize().height));
		circle.setMinimumSize(new Dimension(width - 20, circle.getPreferredSize().height));
		square.setMinimumSize(new Dimension(width - 20, square.getPreferredSize().height));
		addnoise.setMinimumSize(new Dimension(width, addnoise.getPreferredSize().height));
		empty.setPreferredSize(new Dimension(width, 1000));
		
		menupanel.setBorder(BorderFactory
				.createTitledBorder(BorderFactory
					.createLineBorder(Color.BLACK), "Menu", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, f));
	}
	
	/**
	 * Show at the middle of the screen
	 */
	public void start() 
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		circle.requestFocusInWindow();
	}
	
	/**
	 * @param args command line params
	 */
	public static void main(String[] args) 
	{
		try
		{	
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			f = UIManager.getDefaults().getFont("TabbedPane.font");
			f = new Font(f.getFamily(), Font.BOLD, f.getSize());
		}
		catch(Exception x){}
		new TestProgram().start();
	}

	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == open)
		{
			chooser = new JFileChooser();
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(new InputFileFilter(false));
			if(loadedFile != null)
				chooser.setSelectedFile(loadedFile);
			int returnValue = chooser.showOpenDialog(this);
			if(returnValue == JFileChooser.APPROVE_OPTION) 
			{
				boolean approved = ((InputFileFilter) chooser.getFileFilter())
						.isFileApproved(chooser.getSelectedFile());
				if(approved)
				{
					try
					{
						loadedFile = chooser.getSelectedFile();
						FileInputStream fis = new FileInputStream(loadedFile);
						InputParser ip = new InputParser(fis);
						if(ip.parseInput())
						{
							field = new Field(new ArrayList<Point>(Arrays.asList(ip.getPoints())));
						}
					} 
					catch (FileNotFoundException e1){}
					updateContentPanel();
				}
			}
		}
		else if(e.getSource() == save)
		{
			chooser = new JFileChooser();
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(new InputFileFilter(true));
			if(loadedFile != null)
				chooser.setSelectedFile(loadedFile);
			int returnValue = chooser.showSaveDialog(this);
			if(returnValue == JFileChooser.APPROVE_OPTION) 
			{
				boolean approved = ((InputFileFilter) chooser.getFileFilter())
						.isFileApproved(chooser.getSelectedFile());
				if(approved)
				{
					loadedFile = chooser.getSelectedFile();
					boolean halt = loadedFile.exists();
					if(halt)
					{
						int i = JOptionPane.showInternalConfirmDialog(c,
                                "Are you sure you want to override this file?",
                                "Warning",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);
						if(i == JOptionPane.YES_OPTION)
						{
							halt = false;
						}
					}
					
					if(!halt)
					{
						try 
						{
							PrintWriter pw = new PrintWriter(new FileWriter(loadedFile));
							pw.println("find "+ field.getNumberOfClusters() +" clusters");
							pw.println(field.size() +" points");
							Object[] obj = field.toArray();
							for(int i = 0; i < obj.length; i++)
							{
								Point p = (Point) obj[i];
								pw.println(p.getX() +" "+ p.getY());
							}
							pw.close();
						} catch (IOException e1) {}
					}
				}
			}
		}
		else if(e.getSource() == addnoise)
		{		
			System.out.println(field.size());
			String s = JOptionPane.showInternalInputDialog(c, "How many points?", "Add noise", JOptionPane.QUESTION_MESSAGE);

			if(s != null)
			{
				System.out.println(field.size());
				int number;
				try
				{
					number = Integer.parseInt(s);
				}
				catch(Exception ex)
				{
					number = 0;
				}
				
				System.out.println(field.size());
				
				for(int i = 0; i < number; i++)
				{
					
					int x = (int) (Math.random() * 1000000000);
					int y;
					
					boolean busy = true;
					while(busy)
					{
						y = (int) (Math.random() * 1000000000);
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
				System.out.println(field.size());
				
			}
			
			updateContentPanel();
		}
	}

	public void updateContentPanel()
	{
		contentpanel.setField(field);
	}
}
