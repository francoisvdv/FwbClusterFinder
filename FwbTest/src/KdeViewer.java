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
			in = FwbParser.parse(new FileInputStream(new File("../FwbAlgorithm/outputscaledfield.fwb")));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(in == null || in.densities == null)
			return;
		
		BufferedImage img = new BufferedImage(in.width, in.height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		
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
		//bottom is green, middle is blue, top is red
		float rel = Math.max(0, Math.min(density / maxDensity, 1));
		Color c = Color.BLACK;

		if(rel <= (1/3f))
			c = new Color(0, rel / (1/3f), 0);
		else if(rel > (1/3f) && rel <= (2/3f))
			c = new Color(0, 1, (rel - (1/3f)) / (1/3f));
		else if(rel > (2/3f))
			c = new Color((rel - (2/3f)) / (1/3f), 1, 1);
		
		return c;
	}
}
