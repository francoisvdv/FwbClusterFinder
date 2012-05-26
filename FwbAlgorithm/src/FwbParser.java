import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class FwbParser
{	
	public static class Result
	{
		int width;
		int height;
		float maxDensity;

		float[][] densities;
	}

	static Scanner inputScanner;

	public static Result parse(InputStream stream, boolean output)
	{
		inputScanner = new Scanner(stream);
		inputScanner.useLocale(Locale.US);

		Result r = new Result();

		if(!inputScanner.nextLine().startsWith("[head]")) return r;
		if(!inputScanner.nextLine().startsWith("type")) return r;
		if(!inputScanner.nextLine().startsWith("source")) return r;

		if(!inputScanner.next().startsWith("width:")) return r;
		r.width = inputScanner.nextInt();

		if(!inputScanner.next().startsWith("height:")) return r;
		r.height = inputScanner.nextInt();

		if(!inputScanner.next().startsWith("maxDensity:")) return r;
		r.maxDensity = inputScanner.nextFloat(); inputScanner.nextLine(); //next line is necessary after nextFloat

		if(!inputScanner.nextLine().startsWith("[body]")) return r;

		r.densities = new float[r.width][r.height];

		int lastPercentage = 0;

		for(int i = 0; i < r.width; i++)
		{
			for(int j = 0; j < r.height; j++)
			{
				r.densities[i][j] = inputScanner.nextFloat();
			}

			if(output)
			{
				//Output parse percentage
				int newPercentage = Math.round(i / (float)r.width * 100);
				if(newPercentage != lastPercentage)
				{
					System.out.print(".");
					lastPercentage = newPercentage;
				}
			}
		}

		return r;
	}
}
