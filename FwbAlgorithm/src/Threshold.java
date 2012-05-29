import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Threshold
{
	int previousNumberOfPoints;
	int numberOfPoints;
	float currentThreshold;
	float maxThreshold;

	/**
	 * Returns a Threshold value. Everything BELOW this threshold should be cut
	 * off (noise).
	 * 
	 * @return
	 */
	public float findThreshold(KDE KDE)
	{
		//Array that stores the 'switch-densities' between point counts. So between element 0 and 1 there
		//are changes in the point counts, between element 2 and 3, etc.
		ArrayList<Float> switches = new ArrayList<Float>();
		
		//Used for generating a graph of densities/pointcounts
		HashMap<Float, Integer> pointCounts = new HashMap<Float, Integer>();
		
		maxThreshold = KDE.getMaxDensity();
		previousNumberOfPoints = 0;

		boolean inCluster = false; //Variable indicating whether we are currently 'in' a cluster in our algorithm.
		float step = maxThreshold / Constants.Threshold.STEPCOUNT; //The step value indicates how fine we should partition the density range.

		if (maxThreshold == 0)
		{
			log("Step too small: " + step + " maxThreshold: " + maxThreshold + " - Threshold stopped.");
			return 0; //If the step value is 0, it we can stop right away because the algorithm will fail.
		}
		
		//Start looping through the thresholds. We start at the max density and go down one step value each iteration.
		for (currentThreshold = maxThreshold; currentThreshold >= 0; currentThreshold -= step)
		{
			numberOfPoints = KDE.getPointCountAboveThreshold(currentThreshold);
			pointCounts.put(currentThreshold, numberOfPoints);
			
			//If the current number of points is larger than the previous number of points we are apparently iterating
			//in a cluster.
			if (numberOfPoints > previousNumberOfPoints)
			{
				//If we are not yet iterating in a cluster, we apparently started in a new one.
				if (!inCluster)
					switches.add(currentThreshold);

				inCluster = true;
			}
			else
			{
				//There was no change in the number of points, so if we were iterating in a cluster we have now found the end of it.
				if(inCluster)
				{
					switches.add(currentThreshold);
					inCluster = false;
				}
			}
			
			previousNumberOfPoints = numberOfPoints;
		}
		if(inCluster && !Utils.floatAlmostEquals(switches.get(switches.size() - 1), currentThreshold))
			switches.add(currentThreshold); //The 'closing' density hasn't been added yet.
		
		assert switches.size() % 2 == 0; //The amount of switches should be equal because we have a start and end value for each switch.
		
		//Because we subtract step each time, we will eventually might get to a negative value. Since we don't have negative
		//densities, make it zero.
		if(switches.get(switches.size() - 1) < 0)
			switches.set(switches.size() - 1, 0f);
		
		log("Switches size: " + switches.size());
		for(int i = 0; i <= switches.size() - 2; i += 2)
		{
			log("Switch " + i + ": " + switches.get(i) + " | Switch " + (i+1) + ": " + switches.get(i+1));
		}

		if(Constants.DEBUG)
			graph(pointCounts);
		
		/* 
		 * To cut off the noise, we check if the last switch contains lots of 'steps' compared to previous
		 * switches. If this is the case, we can be certain that this is noise. One other thing we apply
		 * is that if the amount of steps between two consecutive switches is very small, we merge these two
		 * according to the mergeThreshold parameter.
		 */

		//TODO: aanpassen door testen.
		float mergeThreshold = 0;//(float) Math.PI / 3 / Constants.Threshold.STEPCOUNT * maxThreshold;
		log("MergeThreshold: " + mergeThreshold);
		
		boolean noiseDetected = false;
		
		//Loop through all the switches, starting from the back.
		for (int i = switches.size() - 2; i >= 0; i -= 2)
		{
			//If the following breaks, we found a cluster.
			if (Math.abs(switches.get(i) - switches.get(i + 1)) > mergeThreshold)
				break; // Als het een cluster is dan breakt ie altijd. Als het
						// noise is kan hij breaken.

			// Als hij niet breekt is het sowieso noise.
			noiseDetected = true;

			switches.remove(i + 1);
			switches.remove(i);
		}

		if (noiseDetected) // we hebben sowieso noise, dus laatste eraf halen
		{
			// Hak laatste eraf
			//switches.remove(switches.size() - 1);
			//switches.remove(switches.size() - 1);
		} else
		// het is niet zeker of we noise hebben, bepaal dit
		{
			//calculate average
			float totalDifference = 0;
			int totalSteps = 0;
			for (int i = 0; i < switches.size() - 3; i += 2)
			{
				totalDifference += Math.abs(switches.get(i)
						+ switches.get(i + 1));
				totalSteps++;
			}

			// de average van alle switches behalve de laatste
			int averageSteps = (int) Math.ceil(totalDifference / totalSteps);
			float maximalDeviation = averageSteps * 0f; // TODO: Deviation
															// 1.4f bepalen door
															// testen
			
			if (switches.size() >= 2 && Math.abs(switches.get(switches.size() - 1)
					- switches.get(switches.size() - 2)) > maximalDeviation)
			{
				// Laatste is noise dus die hakken we eraf
				switches.remove(switches.size() - 1);
				switches.remove(switches.size() - 1);
			}
		}
		
		return switches.size() == 0 ? 0 : switches.get(switches.size() - 1);
	}
	
	void log(String message)
	{
		Utils.log("Threshold", message);
	}
	
	void graph(HashMap<Float, Integer> pointCounts)
	{
		final XYSeries series = new XYSeries("First");
		
		Iterator it = pointCounts.entrySet().iterator();
	    while (it.hasNext())
	    {
	        Map.Entry pairs = (Map.Entry)it.next();
	        series.add((float)pairs.getKey(), (int)pairs.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
		final XYSeriesCollection dataset = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart("Point count switches", 
				"Threshold", "Points above threshold", (XYDataset)dataset, PlotOrientation.VERTICAL, false, false, false);
		
		File f = new File("thresholdoutput.png");
	    try
		{
			ChartUtilities.saveChartAsPNG(f, chart, 750, 750);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
