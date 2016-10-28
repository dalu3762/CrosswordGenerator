package heuristic;
import java.util.HashMap;

import grid.Grid;
import location.Location;
import word.WordFitting;

public class ValidityChecker
{
	private ValidityChecker()
	{		
	}	
		
	public static boolean isValidGrid(Grid grid, WordFitting fitting, HashMap<Location, Location []> intersectors, HashMap<Location, Location []> nonintersectors, Location currentLocation)
	{
		Location [] intersections = intersectors.get(currentLocation);
    	Location [] nonintersections = nonintersectors.get(currentLocation); 
    		
		//Still completable grid check/duplicate checker
		if (fitting.isValid(grid,intersections) == false  || grid.containsDuplicate(intersections) == true
				|| grid.containsDuplicate(intersections,currentLocation) == true
				|| grid.containsDuplicate(nonintersections,currentLocation) == true
				|| grid.containsDuplicate(intersections,nonintersections) == true)					
		{
			return false; 				
		}
		else
		{
			return true;		    		
			   				   				
		}
	}	
}	