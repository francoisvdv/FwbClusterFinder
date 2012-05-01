import java.awt.*;
import javax.swing.*;


public class TestProgram extends JFrame {
	
	private static final long serialVersionUID = 6814435897208431145L;
	
	JPanel panel;
	JMenuBar menu;
	
	public TestProgram() {
		Toolkit tk = Toolkit.getDefaultToolkit();  
		int x = (int) tk.getScreenSize().getWidth() - 150;  
		int y = (int) tk.getScreenSize().getHeight() - 100;  
		this.setSize(x, y);
		
		panel = new JPanel();
		this.setContentPane(panel);
	}
	
	public void start() {
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new TestProgram().start();
	}

}
