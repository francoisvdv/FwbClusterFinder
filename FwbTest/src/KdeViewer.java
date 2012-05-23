import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class KdeViewer
{	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		File f = new File("../FwbAlgorithm/outputscaledfield.fwb");
		if(!f.exists())
		{
			System.out.println("File doesn't exist");
			return;
		}
		
		System.out.print("Parsing input file... 0%");
		
		FwbParser.Result in = FwbParser.parse(new FileInputStream(f), true);
		if(in == null || in.densities == null)
		{
			System.out.println("Wrong input format");
			return;
		}
		
		System.out.println("100%");
		
		System.out.print("Writing png file... 0%");
		
		BufferedImage img = new BufferedImage(in.width, in.height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		
		int lastPercentage = 0;
		
		for(int i = 0; i < in.width; i++)
		{
			for(int j = 0; j < in.height; j++)
			{
				g.setColor(densityToColor(in.densities[i][j], in.maxDensity));
				g.drawLine(i, j, i, j);
			}

			//Output parse percentage
			int newPercentage = Math.round(i / (float)in.width * 100);
			if(newPercentage != lastPercentage)
			{
				System.out.print(".");
				lastPercentage = newPercentage;
			}
		}

		System.out.print("100%");
		
	    // retrieve image
	    File outputfile = new File("outputscaledfield.png");
	    try
		{
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    Desktop.getDesktop().open(outputfile);
	}
	
	static Color densityToColor(float density, float maxDensity)
	{
		//Kleuring 1:
		//bottom is green, middle is blue, top is red
		float rel = Math.max(0, Math.min(density / maxDensity, 1));
		Color c = Color.BLACK;

		if(rel <= (1/3f))
			c = new Color(0, rel / (1/3f), 0);
		else if(rel > (1/3f) && rel <= (2/3f))
			c = new Color(0, 0, (rel - (1/3f)) / (1/3f));
		else if(rel > (2/3f))
			c = new Color((rel - (2/3f)) / (1/3f), 0, 0);
		
//		//Kleuring 2:
//		float h = (1 - rel) * (80/360f) - (20/360f); //red to yellow
//		if(h < 0)
//			h = 1 - h;
//		float s = 1;
//		float v = 1;//rel == 0 ? 0 : 1;
//
//		c = Color.getHSBColor(h, s, v);
//
//		//Kleuring 3:
//		int r = 0, g = 0,b = 0;
//		if(rel <= (1/2f))
//		{
//			g = 0;
//			r = (int)((rel / 0.5f) * 255);
//			b = 0;
//		}
//		else
//		{
//			g = (int)(((rel - 0.5f) / 0.5f) * 255);
//			r = 255;
//			b = 0;
//		}
//		
//		r = Math.max(0, Math.min(r, 255));
//		g = Math.max(0, Math.min(g, 255));
//		
//		c = new Color(r, g, b);
		
		return c;
	}
}
