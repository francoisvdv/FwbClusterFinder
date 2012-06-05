import java.io.File;
import java.io.IOException;
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

public final class Graphing
{
	public static void graphPointCounts(HashMap<Float, Integer> pointCounts)
	{
		final XYSeries series = new XYSeries("First");

		Iterator it = pointCounts.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry pairs = (Map.Entry) it.next();
			series.add((Float) pairs.getKey(), (Integer) pairs.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}

		final XYSeriesCollection dataset = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart("Point count switches",
				"Threshold", "Points above threshold", (XYDataset) dataset, PlotOrientation.VERTICAL, false, false, false);

		File f = new File("thresholdoutput.png");
		try
		{
			ChartUtilities.saveChartAsPNG(f, chart, 750, 750);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void graphSortedDensities(HashMap<Integer, Float> densities)
	{
		final XYSeries series = new XYSeries("First");

		Iterator it = densities.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry pairs = (Map.Entry) it.next();
			series.add((Integer)pairs.getKey(), (Float)pairs.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}

		final XYSeriesCollection dataset = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart("Sorted Densities",
				"Key", "Density", (XYDataset) dataset, PlotOrientation.VERTICAL, false, false, false);

		File f = new File("sorted_densities.png");
		try
		{
			ChartUtilities.saveChartAsPNG(f, chart, 11000, 3000);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
