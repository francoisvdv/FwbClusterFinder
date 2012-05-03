import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Line2D;

////////////////////////////////////////////////////////////// PaintDemo
class FwbClusterViewer
{
    public static void main(String[] args)
    {
        MainWindow window = new MainWindow();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}

//////////////////////////////////////////////////////////// PaintWindow
class MainWindow extends JFrame
{
    PaintPanel canvas = new PaintPanel();
    
    public MainWindow() {
        Container content = this.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(canvas     , BorderLayout.CENTER);
        this.setTitle("FWB cluster view");
        this.pack();
    }
}

///////////////////////////////////////////////////////////// PaintPanel
class PaintPanel extends JPanel implements MouseListener, MouseMotionListener
{
    private int _currentStartX = 0;
    private int _currentStartY = 0;
    private int _currentEndX   = 0;
    private int _currentEndY   = 0;
    
    //--- BufferedImage to store the underlying saved painting.
    //    Will be initialized first time paintComponent is called.
    private BufferedImage _bufImage = null;
    
    //--- Private constant for size of paint area.
    private static final int WIDTH = 1200; // size of paint area
    private static final int HEIGHT = 800; // size of paint area
    
    public PaintPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.gray);
        //--- Add the mouse listeners.
        this.addMouseListener(this); 
        this.addMouseMotionListener(this);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        if (_bufImage == null) {
            //--- This is the first time, initialize _bufImage
            int w = this.getWidth();
            int h = this.getHeight();
            _bufImage = (BufferedImage)this.createImage(w, h);
            Graphics2D gc = _bufImage.createGraphics();
            gc.setColor(Color.white);
            gc.fillRect(0, 0, w, h);
        }
        g2.drawImage(_bufImage, null, 0, 0);
        
        drawCurrentShape(g2);
    }
    
    //================================================= drawCurrentShape
    private void drawCurrentShape(Graphics2D g2) {
        Line2D lin = new Line2D.Float(_currentStartX, _currentStartY,
                                 _currentEndX, 
                                 _currentEndY);
        g2.draw(lin);
    }
    
    //===================================================== mousePressed
    public void mousePressed(MouseEvent e) {
        _currentStartX = e.getX(); // save x coordinate of the click
        _currentStartY = e.getY(); // save y
        _currentEndX   = _currentStartX;   // set end to same pixel
        _currentEndY   = _currentStartY;
    }
    
    //===================================================== mouseDragged
    public void mouseDragged(MouseEvent e) {
        _currentStartX = _currentEndX;
        _currentStartY = _currentEndY;
        _currentEndX = e.getX();
        _currentEndY = e.getY();
        
        //--- Draw the current shape onto the buffered image.
        Graphics2D grafarea = _bufImage.createGraphics();
        drawCurrentShape(grafarea);
        
        this.repaint();            // show new shape
    }
    
    //==================================================== mouseReleased
    public void mouseReleased(MouseEvent e) {
        _currentEndX = e.getX();
        _currentEndY = e.getY();
        
        //--- Draw the current shape onto the buffered image.
        Graphics2D grafarea = _bufImage.createGraphics();
        drawCurrentShape(grafarea);
        
        this.repaint();
    }
    
    //========================================== ignored mouse listeners
    public void mouseMoved   (MouseEvent e) {}
    public void mouseEntered (MouseEvent e) {}
    public void mouseExited  (MouseEvent e) {}
    public void mouseClicked (MouseEvent e) {}
}