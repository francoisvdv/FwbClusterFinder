import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.UIManager;

public final class KdeViewer
{	
	public static void main(String[] args) 
	{
		try
		{
			
			KdeVisualizer.writeToFile(KdeVisualizer.run("../FwbAlgorithm/outputscaledfield.fwb"), "outputscaledfield.png");;
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
