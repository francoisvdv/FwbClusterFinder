import java.awt.Color;
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
	 */
	public static void main(String[] args)
	{
		FwbParser.Result in = null;
		try
		{
			File f = new File("../FwbAlgorithm/outputscaledfield.fwb");
			assert f.exists();
			
			in = FwbParser.parse(new FileInputStream(f));
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(in == null || in.densities == null)
			return;
		
		BufferedImage img = new BufferedImage(in.width, in.height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		
		for(int i = 0; i < in.width; i++)
		{
			for(int j = 0; j < in.height; j++)
			{
				g.setColor(densityToColor(in.densities[i][j], in.maxDensity));
				g.drawLine(i, j, i, j);
			}
		}

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
