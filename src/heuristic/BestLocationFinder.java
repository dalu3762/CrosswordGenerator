package heuristic;
import java.util.ArrayList;
import java.util.HashMap;

import grid.Grid;
import grid.GridInfo;
import location.Location;
import word.WordFitting;

public class BestLocationFinder
{
	private Location [] allLocations;
	private String gridType;
	
	private Location [] partA;
	private Location [] partB;
	private Location [] partC;
	
	private Location dividerA;
	private Location dividerB;
	
	private Location nextLocation;	
		
	public BestLocationFinder(GridInfo gridInfo)
	{
		allLocations = gridInfo.getLocations();
		this.gridType = gridInfo.getGridType();		
	}
	
	public void setup(GridInfo gridInfo, WordFitting fitting)
	{
		Grid grid = gridInfo.getCurrentGrid();
		
		int numAreas = Isolation.getNumIsolatedAreas(grid);
		
		if (gridType.equals("NoForkWithDivider"))
		{
			Location [] dividers = Isolation.getDividers(grid,allLocations, numAreas);
			nextLocation = Isolation.findMostIsolating(grid,dividers);
			
			grid.replace(nextLocation, grid.getBlankSymbol(), 'A');
			
			partA = Isolation.getMostIsolatedLocations(grid, allLocations);
			partB = GridLocationFinder.getNotCommonLocations(allLocations, partA);
			
		}
		else if (gridType.equals("WithForkNoDivider"))
		{
			partA = OneWayPath.getNotBothOneWayPathAndNotDivider(grid, allLocations);
			nextLocation = BestLocationFinder.getLocationLeastOptions(grid,fitting, partA, gridInfo.getIntersectorsAdjacents());
		}
		else if (gridType.equals("WithForkWithDivider"))
		{
			Location [] notForks = OneWayPath.getNotBothOneWayPathAndNotDivider(grid, allLocations);
			Location [] dividers = Isolation.getDividers(grid,notForks, numAreas);
			
			nextLocation = Isolation.findMostIsolating(grid,dividers);
			
			String originalWord = grid.getWord(nextLocation);
			
			grid.replace(nextLocation, grid.getBlankSymbol(), 'A');
			
			partA = Isolation.getMostIsolatedLocations(grid, allLocations);
			partB = GridLocationFinder.getNotCommonLocations(allLocations, partA);
			
			grid.setWord(nextLocation,originalWord);
		}
		else if (gridType.equals("DoubleDivider"))
		{
			Location [] doubleDividers = Isolation.getDoubleDivide(grid, allLocations, numAreas);
			
			Location [] bestDividers = Isolation.getDoubleDivideMostSquares(grid,allLocations, doubleDividers, numAreas);	
						
			dividerA = bestDividers[0];
			dividerB = bestDividers[1];
			
			nextLocation = dividerA;
						
			String originalWord = grid.getWord(dividerA);
			String originalWord2 = grid.getWord(dividerB);
			
			grid.replace(dividerA, grid.getBlankSymbol(), 'A');
			grid.replace(dividerB, grid.getBlankSymbol(), 'A');
			
			partA = Isolation.getMostIsolatedLocations(grid, allLocations);			
			
			ArrayList<Location> partAPlusDivider = new ArrayList<Location>();
			
			for (int x = 0; x < partA.length; x ++)
			{
				partAPlusDivider.add(partA[x]);
			}		
			
			partAPlusDivider.add(dividerA);
			partA = partAPlusDivider.toArray(new Location[partAPlusDivider.size()]);
			
			grid.setWord(dividerA, originalWord);
			grid.setWord(dividerB, originalWord2);
								
			partB = GridLocationFinder.getNotCommonLocations(allLocations, partA);								
		}
		else if (gridType.equals("Misc"))
		{
			partA = allLocations;
			nextLocation = partA[0];
		}
		else
		{
			throw new IllegalArgumentException("Undefined grid type: " + gridType);
		}
		
	}
	
	public static Location getLocationLeastOptions(Grid grid, WordFitting fitting, Location [] locations, HashMap<Location, Location []> intersectorsAdjacents)
	{		
		int lowestOptions = 999999999;
		int options = 0;
		Location best = new Location(-1,-1,-1,"H");		
		String [] allOptions;
		Location [] intersectionsAdjacents;
		String original = "";
		boolean good = false;
			
		for (Location location: locations)
		{
			options = 2000000000;
			
			original = grid.getWord(location);
			
			allOptions = fitting.findFittableWords(grid, location);			
			options = allOptions.length;
			
			if (grid.hasBlank(location) == true && options < 100)
			{				
				options = 0;
				intersectionsAdjacents = intersectorsAdjacents.get(location);
				intersectionsAdjacents = GridLocationFinder.getHasBlanks(grid, intersectionsAdjacents);
											
				for (int x = 0; x < allOptions.length; x ++)
				{
					good = true;
					
					grid.setWord(location, allOptions[x]);
					
					for (int y = 0; y < intersectionsAdjacents.length; y ++)
					{
						if (fitting.getNumFittableWords(grid, intersectionsAdjacents[y]) == 0)
						{
							good = false;
							break;
						}
					}
					
					if (good == true)
					{
						options ++;
					}					
				}
				
				grid.setWord(location,original);								
			}
												
			if (options < lowestOptions && grid.hasBlank(location) == true)
			{				
				best = location;
				lowestOptions = options;
			}
			
			if (options == 0)
			{
				return location;
			}
		}
				
		return best;
	}	
	
	private Location pickLocation(GridInfo gridInfo, WordFitting fitting, Location [] pickableLocations)
	{
		Grid grid = gridInfo.getCurrentGrid();
		Location previousLocation = gridInfo.getCurrentLocation();
		
		Location [] hasBlank = GridLocationFinder.getHasBlanks(grid,pickableLocations);
			
		if (hasBlank.length != 0)
		{
			Location [] intersectorsAdjacents = gridInfo.getIntersectorsAdjacents().get(previousLocation);
							
			Location [] blankIntersectorsAdjacents = GridLocationFinder.getCommonLocations(hasBlank, intersectorsAdjacents);
												
			Location [] locationsWithIncompleteIntersector = GridLocationFinder.getIntersectingOrAdjacentWithIncompleteIntersector(grid, 
				previousLocation, blankIntersectorsAdjacents, gridInfo.getIntersectors());
			
			if (locationsWithIncompleteIntersector.length != 0)
			{
				return getLocationLeastOptions(grid, fitting, blankIntersectorsAdjacents, gridInfo.getIntersectorsAdjacents());
			}
						
			return getLocationLeastOptions(grid,fitting, hasBlank, gridInfo.getIntersectorsAdjacents());
		}
		else
		{
			return null;
		}
	}
			
	public Location findNextLocation(GridInfo gridInfo, WordFitting fitting)
	{					
		if (partA != null)
		{
			Location nextLocation = this.pickLocation(gridInfo, fitting, partA);
			
			if (nextLocation != null)
			{
				return nextLocation;
			}
		}
				
		if (partB != null)
		{
			Location nextLocation = this.pickLocation(gridInfo, fitting, partB);
			
			if (nextLocation != null)
			{
				return nextLocation;
			}
		}		
		
		if (partC != null)
		{
			Location nextLocation = this.pickLocation(gridInfo, fitting, partC);
			
			if (nextLocation != null)
			{
				return nextLocation;
			}
		}
		
		Location nextLocation = this.pickLocation(gridInfo, fitting, allLocations);
			
		return nextLocation != null ? nextLocation: new Location(-1, -1, -1, "H");			
							
	}
	
	public Location getLocationToFill()
	{
		return nextLocation;
	}	
}	