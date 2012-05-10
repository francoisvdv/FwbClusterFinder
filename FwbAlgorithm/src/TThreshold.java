import java.util.ArrayList;

public class TThreshold
{
	public static class KDE
	{
		public static float GetMaxThreshold()
		{
			return 0.8f;
		}
		public static int GetPointCountAboveThreshold(float threshold)
		{
			return 0;
		}
	}

	int stepCount = 100;
	
	int previousNumberOfPoints;
	int numberOfPoints;
	float currentThreshold;
	float maxThreshold;

	//step = MaxThreshold / 100
	//
	
	public void FindSwitches()
	{
		//Tussen element 0 en 1 zit een switch, tussen element 2 en 3 een switch, etc.
		ArrayList<Float> switches = new ArrayList<Float>();
		
		maxThreshold = KDE.GetMaxThreshold();
		previousNumberOfPoints = 0;
		
		boolean inCluster = false;
		
		float step = maxThreshold / (float)stepCount;
		
		for(currentThreshold = maxThreshold; currentThreshold >= 0; currentThreshold -= step)
		{
			numberOfPoints = KDE.GetPointCountAboveThreshold(currentThreshold);
			
			if(numberOfPoints > previousNumberOfPoints)
			{
				//Er is een verandering
				if(!inCluster)
				{
					//Beginpunt van nieuwe cluster
					switches.add(currentThreshold);
				}
				
				inCluster = true;
			}
			else if(inCluster)
			{
				//Er is geen verandering meer, dus einde van cluster gevonden
				switches.add(currentThreshold);
				
				inCluster = false;
			}
		}

		/* Om de noise eraf te knallen:
		 * We detecteren of de laatste cluster veel 'steps' bevat in vergelijking met de rest, en de cluster
		 * die daarvoor komt kunnen we samenvoegen wanneer de hoeveelheid steps tussen die twee klein is.
		 */
	}
	
	public void RecursiveMethod(float threshold)
	{
		
	}
}
