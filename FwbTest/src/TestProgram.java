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

public class TestProgram extends JFrame implements ActionListener
{
	
	private static final long serialVersionUID = 6814435897208431145L;
	
	ContentPanel contentpanel;
	JPanel menupanel;
	Container c;
	GridLayout layout;
	
	JButton open, save, addnoise;
	JFileChooser chooser;
	
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
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Friends With Benefits - Test program");
		field = new Field();

		// Set the panel
		c = this.getContentPane();
		c.setLayout(new BorderLayout());
		
		contentpanel = new ContentPanel();
		c.add(contentpanel, BorderLayout.CENTER);
		
		// Menupanel
		layout = new GridLayout(20, 1);
		
		menupanel = new JPanel();
		menupanel.setBackground(Color.CYAN);
		menupanel.setLayout(layout);
		c.add(menupanel, BorderLayout.EAST);
		
		JPanel p1 = new JPanel();
		open = new JButton("Open");
		save = new JButton("Save");
		open.addActionListener(this);
		save.addActionListener(this);
		p1.add(open);
		p1.add(save);
		menupanel.add(p1);
		
		addnoise = new JButton("Add noise");
		addnoise.addActionListener(this);
		menupanel.add(addnoise);
		// TODO
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
			String s = JOptionPane.showInternalInputDialog(c, "How many points?", "Add noise", JOptionPane.QUESTION_MESSAGE);

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
				
				for(int i = 0; i < number; i++)
				{
					
					int x = (int) Math.round(Math.random() * 1000000000);
					int y;
					
					boolean busy = true;
					while(busy)
					{
						y = (int) Math.round(Math.random() * 1000000000);
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
			}
			
			updateContentPanel();
		}
	}

	public void updateContentPanel()
	{
		contentpanel.setField(field);
	}
}
