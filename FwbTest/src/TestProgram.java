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
import java.util.Random;

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
	
	boolean progressWorks = false;
	JProgressBar progress;
	JButton open, save, addnoise, addacluster, clear, center;
	JFileChooser chooser;
	ButtonGroup squarecircle, placeOfCluster;
	JRadioButton circle, square, everywhere, inRectangle;
	JSlider fillFactor;
	
	File loadedFile;
	Field field;
	Random random;
	
	/**
	 * Class constructor - make the JFrame ready
	 */
	public TestProgram() 
	{
		// Set screen size
		Toolkit tk = Toolkit.getDefaultToolkit();  
		int x = (int) tk.getScreenSize().getWidth() - 150;  
		int y = (int) tk.getScreenSize().getHeight() - 100;  
		this.setSize(x, y);
		
		// Some settings
		this.setTitle("Friends With Benefits - Test program");
		field = new Field();
		random = new Random(System.currentTimeMillis());

		// Set the panel
		c = this.getContentPane();
		c.setLayout(new BorderLayout());
		
		contentpanel = new ContentPanel();
		contentpanel.setField(field);
		c.add(contentpanel, BorderLayout.CENTER);
		
		// Menupanel
		menupanel = new JPanel();
		layout = new GroupLayout(menupanel);
		menupanel.setLayout(layout);
		c.add(menupanel, BorderLayout.EAST);
		
		progress = new JProgressBar(0, 99);
		progress.setValue(99);
		
		open = new JButton("Open");
		open.setFocusPainted(false);
		save = new JButton("Save");
		save.setFocusPainted(false);
		open.addActionListener(this);
		save.addActionListener(this);

		everywhere = new JRadioButton("Everywhere");
		everywhere.setFocusPainted(false);
		inRectangle = new JRadioButton("In bounding rectangle");
		inRectangle.setFocusPainted(false);
		inRectangle.setSelected(true);
		
		placeOfCluster = new ButtonGroup();
		placeOfCluster.add(everywhere);
		placeOfCluster.add(inRectangle);
		
		addnoise = new JButton("Add noise");
		addnoise.setFocusPainted(false);
		addnoise.addActionListener(this);
		
		circle = new JRadioButton("Circle");
		circle.setFocusPainted(false);
		square = new JRadioButton("Square");
		square.setFocusPainted(false);
		circle.setSelected(true);
		
		squarecircle = new ButtonGroup();
		squarecircle.add(circle);
		squarecircle.add(square);
		
		fillFactor = new JSlider();
		fillFactor.setMajorTickSpacing(20);
	    fillFactor.setMinorTickSpacing(5);
	    fillFactor.setPaintTicks(true);
		
		addacluster = new JButton("Add simple cluster");
		addacluster.setFocusPainted(false);
		addacluster.addActionListener(this);
		
		clear = new JButton("Clear field");
		clear.addActionListener(this);
		clear.setFocusPainted(false);
		
		center = new JButton("Center field");
		center.addActionListener(this);
		center.setFocusPainted(false);
	    
		JSeparator sep1 = new JSeparator();
		JSeparator sep2 = new JSeparator();
		JSeparator sep3 = new JSeparator();
		JPanel empty = new JPanel();
		JLabel addcluster = new JLabel("Add simple cluster:");
		JLabel addNoise = new JLabel("Add noise");
		JLabel fillf = new JLabel("Fill factor:");
		addcluster.setFont(f);
		addNoise.setFont(f);
		
		fillFactor.setPreferredSize(new Dimension(menupanel.getPreferredSize().width / 2, fillFactor.getPreferredSize().height));
		
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
					.addComponent(open)
					.addGap(2)
					.addComponent(save))
				.addComponent(sep1)
				.addComponent(addNoise)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(everywhere)
					.addComponent(inRectangle))
				.addComponent(addnoise)
				.addComponent(sep2)
				.addComponent(addcluster)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(circle)
					.addComponent(square))
				.addComponent(fillf)
				.addComponent(fillFactor)
				.addComponent(addacluster)
				.addComponent(sep3)
				.addComponent(center)
				.addComponent(clear)
				.addComponent(empty)
				.addComponent(progress)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(open)
					.addComponent(save))
				.addGap(5)
				.addComponent(sep1)
				.addGap(5)
				.addComponent(addNoise)
				.addComponent(everywhere)
				.addComponent(inRectangle)
				.addGap(3)
				.addComponent(addnoise)
				.addGap(5)
				.addComponent(sep2)
				.addGap(5)
				.addComponent(addcluster)
				.addComponent(circle)
				.addComponent(square)
				.addGap(3)
				.addComponent(fillf)
				.addGap(3)
				.addComponent(fillFactor)
				.addGap(3)
				.addComponent(addacluster)
				.addGap(5)
				.addComponent(sep3)
				.addGap(5)
				.addComponent(center)
				.addGap(3)
				.addComponent(clear)
				.addComponent(empty)
				.addComponent(progress)
		);
		
		int width = menupanel.getPreferredSize().width;
		
		addNoise.setMinimumSize(new Dimension(width - 10, addNoise.getPreferredSize().height));
		everywhere.setMinimumSize(new Dimension(width - 20, everywhere.getPreferredSize().height));
		inRectangle.setMinimumSize(new Dimension(width - 20, inRectangle.getPreferredSize().height));
		addnoise.setMinimumSize(new Dimension(width, addnoise.getPreferredSize().height));
		
		addcluster.setMinimumSize(new Dimension(width - 10, addcluster.getPreferredSize().height));
		circle.setMinimumSize(new Dimension(width - 20, circle.getPreferredSize().height));
		square.setMinimumSize(new Dimension(width - 20, square.getPreferredSize().height));
		addacluster.setMinimumSize(new Dimension(width, addacluster.getPreferredSize().height));
		
		center.setMinimumSize(new Dimension(width, center.getPreferredSize().height));
		clear.setMinimumSize(new Dimension(width, clear.getPreferredSize().height));
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
	
	protected synchronized void startProgress()
	{
		progressWorks = true;
		new Thread(new Runnable(){
			public void run()
			{
				int value = 0;
				while(progressWorks)
				{
					try
					{
						progress.setValue(value);
						value++;
						value %= 100;
						Thread.sleep(10);
					}
					catch(Exception ex){}
				}
			}
		}).start();
	}
	
	protected synchronized boolean isInProgress()
	{
		return progressWorks;
	}
	
	protected synchronized void stopProgress()
	{
		progressWorks = false;
		progress.setValue(99);
		updateContentPanel();
	}
	
	protected synchronized void stopProgressRepaint()
	{
		progressWorks = false;
		progress.setValue(99);
		contentpanel.repaint();
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
	
	private void systemIsBusy()
	{
		JOptionPane.showInternalMessageDialog(c, "The system is currently busy.", "Busy", JOptionPane.INFORMATION_MESSAGE);
	}

	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == open)
		{
			if(isInProgress())
			{
				systemIsBusy();
				return;
			}
			
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
					startProgress();
					new MultiThread(new Runnable(){
						public void run()
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
							contentpanel.center();
							stopProgress();
						}
					}).start();
					
				}
			}
		}
		else if(e.getSource() == center)
		{
			if(isInProgress())
			{
				systemIsBusy();
				return;
			}
			startProgress();
			new MultiThread(new Runnable(){
				public void run()
				{
					contentpanel.removeMouseWheelListener(contentpanel);
					contentpanel.center();
					contentpanel.addMouseWheelListener(contentpanel);
					stopProgressRepaint();
				}
			}).start();
		}
		else if(e.getSource() == save)
		{
			if(isInProgress())
			{
				systemIsBusy();
				return;
			}
			
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
		else if(e.getSource() == clear)
		{
			if(isInProgress())
			{
				systemIsBusy();
				return;
			}
			int i = JOptionPane.showInternalConfirmDialog(c,
                    "Are you sure you want to clear the field?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
			if(i == JOptionPane.YES_OPTION)
			{
				field = new Field();
				updateContentPanel();
			}
		}
		else if(e.getSource() == circle)
		{
			contentpanel.setSelectionMode(ContentPanel.SELECT_CIRCLE);
		}
		else if(e.getSource() == square)
		{
			contentpanel.setSelectionMode(ContentPanel.SELECT_SQUARE);
		}
		else if(e.getSource() == addacluster)
		{
			if(isInProgress())
			{
				systemIsBusy();
				return;
			}
			
			contentpanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
		else if(e.getSource() == addnoise)
		{	
			if(isInProgress())
			{
				systemIsBusy();
				return;
			}
			final String s = JOptionPane.showInternalInputDialog(c, "How many points?", "Add noise", JOptionPane.QUESTION_MESSAGE);
			
			startProgress();
			new MultiThread(new Runnable(){
				public void run()
				{
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
						
						int tillX = 1000000000;
						int tillY = 1000000000;
						int addX = 0;
						int addY = 0;
						if(inRectangle.isSelected())
						{
							Rectangle r = field.getBoundingRectangle();
							if(!(r.x1 == 0 && r.y1 == 0 && r.x2 == 0 && r.y2 == 0))
							{
								tillX = r.x2 - r.x1;
								addX = r.x1;
								tillY = r.y2 - r.y1;
								addY = r.y1;
							}
							else
							{
								tillX = 500000000;
								tillY = 500000000;
							}
						}
						
						for(int i = 0; i < number; i++)
						{
							int x = random.nextInt(tillX);
							x += addX;
							int y;
							
							boolean busy = true;
							while(busy)
							{
								y = random.nextInt(tillY);
								y += addY;
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
					
					stopProgress();
				}
			}).start();
		}
	}

	public void updateContentPanel()
	{
		contentpanel.setField(field);
	}
	
	class MultiThread extends Thread
	{	
		public MultiThread(Runnable r)
		{
			super(r);
		}
	}
}
