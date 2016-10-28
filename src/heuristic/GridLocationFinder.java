package heuristic;
import java.util.ArrayList;
import java.util.HashMap;

import grid.Grid;
import location.Location;

//Finds row, column, length and direction of grid entries
public class GridLocationFinder
{		
	private GridLocationFinder()
	{		
	}
		
	private static ArrayList<Location> addHorizontalLocations(Grid grid, ArrayList<Location> Locations)
	{		
		String rowText = "";		
		int startIndex = -1;
		int endIndex = -1;
		
		for (int row = 0; row < grid.getNumRows(); row ++)
		{						
			rowText = grid.getRow(row) + grid.getWallSymbol(); //extra block makes it so entry at end of line isn't missed			
							
			for (int column = 0; column < rowText.length(); column ++)
			{				
				if (startIndex == -1 && rowText.charAt(column) != grid.getWallSymbol())
				{										
					startIndex = column; //where word starts
				}
				else if (startIndex != -1 && rowText.charAt(column) == grid.getWallSymbol())
				{
					endIndex = column - 1; //at block past word, so subtract one
					
					if (endIndex - startIndex > 0) //length of 1 is not a word
					{
						Locations.add(new Location(row, startIndex, endIndex - startIndex + 1, "H"));
					}
					
					startIndex = -1;
					endIndex = -1;				
				}
			}
		}
		
		return Locations;
	}
	
	private static ArrayList<Location> addVerticalLocations(Grid grid, ArrayList<Location> Locations)
	{		
		String lineText = "";		
		int startIndex = -1;
		int endIndex = -1;
		
		for (int column = 0; column < grid.getNumColumns(); column ++)
		{
			lineText = grid.getColumn(column) + String.valueOf(grid.getWallSymbol()); //extra block makes it so entry at end of line isn't missed			
				
			for (int row = 0; row < lineText.length(); row ++)
			{
				if (startIndex == -1 && lineText.charAt(row) != grid.getWallSymbol())
				{
					startIndex = row; //where word starts
				}
				else if (startIndex != -1 && lineText.charAt(row) == grid.getWallSymbol())
				{
					endIndex = row - 1; //at block past word, so subtract one
					
					if (endIndex - startIndex > 0) //length of 1 is not a word
					{
						Locations.add(new Location(startIndex, column, endIndex - startIndex + 1, "V"));						
					}
					
					startIndex = -1;
					endIndex = -1;
				}
			}
		}
		
		return Locations;		
	}
	
	//Returns array of starting locations for all grid entries, along with their length and direction
	public static Location [] getLocations(Grid grid)
	{						
		ArrayList<Location> locations = new ArrayList<Location>();
		
		locations = addHorizontalLocations(grid, locations);
		locations = addVerticalLocations(grid, locations);		
		
		return locations.toArray(new Location[locations.size()]); 		
	}
	
	public static Location [] getHasBlanks(Grid grid, Location [] locations)
	{		
		ArrayList<Location> locationList = new ArrayList<Location>();
		
		for (Location location: locations)
		{
			if (grid.hasBlank(location) == true)
			{
				locationList.add(location);
			}
		}
		
		return locationList.toArray(new Location[locationList.size()]); 
	}
		
	public static Location [] getCommonLocations(Location [] locations1, Location [] locations2)
	{		
		ArrayList<Location> locations = new ArrayList<Location>();
		
		for (int x = 0; x < locations1.length; x ++)
		{
			for (int y = 0; y < locations2.length; y ++)			
			{
				if (locations1[x].isEqual(locations2[y]))
				{
					locations.add(locations1[x]);
					break;
				}
			}
		}
		
		return locations.toArray(new Location[locations.size()]); 
	}
	
	public static Location [] getNotCommonLocations(Location [] locations1, Location [] locations2)
	{		
		ArrayList<Location> locations = new ArrayList<Location>();
		boolean isShared = false;
		
		for (int x = 0; x < locations1.length; x ++)
		{
			isShared = false;
			for (int y = 0; y < locations2.length; y ++)			
			{
				if (locations1[x].isEqual(locations2[y]))
				{
					isShared = true;
					y = locations2.length;
					break;
				}
			}
			
			if (isShared == false)
			{
				locations.add(locations1[x]);
			}
		}
		
		return locations.toArray(new Location[locations.size()]); 
	}
	
	public static boolean areAdjacent(Grid grid, Location [] locations, Location location1, Location location2)
	{				
		if (location1.isHorizontal() && location2.isHorizontal())
		{
			if (location1.getRow() - 1 == location2.getRow() || location2.getRow() - 1 == location1.getRow())
			{
				for (int x = location1.getColumn(); x < location1.getColumn() + location1.getLength(); x ++)
				{
					for (int y = location2.getColumn(); y < location2.getColumn() + location2.getLength(); y ++)
					{
						if (x == y)
						{
							Location [] connections1 = GridLocationFinder.getIntersecting(grid, locations, location1);
							Location [] connections2 = GridLocationFinder.getIntersecting(grid, locations, location2);
							Location [] shared = GridLocationFinder.getCommonLocations(connections1, connections2);
								
							for (int z = 0; z < shared.length; z ++)
							{
								if (OneWayPath.isOneWayPath(grid,shared[z]) == false)
								{
									return true;
								}
								
							}
							return false;
						}
					}
				}
			}
		}
		else if (location1.isVertical() && location2.isVertical())
		{
			if (location1.getColumn() - 1 == location2.getColumn() || location2.getColumn() - 1 == location1.getColumn())
			{
				for (int x = location1.getRow(); x < location1.getRow() + location1.getLength(); x ++)
				{
					for (int y = location2.getRow(); y < location2.getRow() + location2.getLength(); y ++)
					{
						if (x == y)
						{
							Location [] connections1 = GridLocationFinder.getIntersecting(grid, locations, location1);
							Location [] connections2 = GridLocationFinder.getIntersecting(grid, locations, location2);
							Location [] shared = GridLocationFinder.getCommonLocations(connections1, connections2);
								
							for (int z = 0; z < shared.length; z ++)
							{
								if (OneWayPath.isOneWayPath(grid,shared[z]) == false)
								{
									return true;
								}
							}
							return false;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public static boolean intersects(Location location1, Location location2)
	{				
		if (location1.isHorizontal() && location2.isVertical())
		{
			for (int x = 0; x < location1.getLength(); x ++)
			{
				for (int y = 0; y < location2.getLength(); y ++)
				{
					if (location1.getRow() == location2.getRow() + y && location1.getColumn() + x == location2.getColumn())
					{
						return true;							
					}
				}
			}			
			
		}
		else if (location1.isVertical() && location2.isHorizontal())
		{
			for (int x = 0; x < location1.getLength(); x ++)
			{
				for (int y = 0; y < location2.getLength(); y ++)
				{
					if (location1.getRow() + x == location2.getRow() && location1.getColumn() == location2.getColumn() + y)
					{
						return true;
					}
				}
			}
		}
		
		return false;	    
	}
		
	//Return locations that intersect location, NOT including the location itself
	public static Location [] getIntersectingOrAdjacent(Grid grid, Location [] locations, Location location)
	{		
		ArrayList<Location> intersectors = new ArrayList<Location>();
		
		for (Location locationToTest: locations)
		{
			if (GridLocationFinder.intersects(location,locationToTest) == true || GridLocationFinder.areAdjacent(grid, locations, location,locationToTest) == true && location.isEqual(locationToTest) == false)
			{
				intersectors.add(locationToTest);
			}			
		}	
		
		return intersectors.toArray(new Location[intersectors.size()]); 
	}
	
	public static Location [] getIntersecting(Grid grid, Location [] locations, Location location)
	{		
		ArrayList<Location> intersectors = new ArrayList<Location>();
				
		for (Location locationToTest: locations)
		{			
			if (GridLocationFinder.intersects(location,locationToTest) == true && location.isEqual(locationToTest) == false)			
			{
				intersectors.add(locationToTest);
			}			
		}
		
		return intersectors.toArray(new Location[intersectors.size()]); 
	}
	
	public static Location [] getNotIntersecting(Grid grid, Location [] locations, Location location)
	{		
		ArrayList<Location> nonintersectors = new ArrayList<Location>();
		
		for (Location locationToTest: locations)
		{
			if (GridLocationFinder.intersects(location,locationToTest) == false && location.isEqual(locationToTest) == false)
			{
				nonintersectors.add(locationToTest);
			}			
		}
		
		return nonintersectors.toArray(new Location[nonintersectors.size()]); 
	}	
	
	public static HashMap<Location,Location[]> getIntersectors(Grid grid, Location [] locations)
	{		
		HashMap <Location,Location[]> intersectors = new HashMap<Location,Location[]>();
		Location [] intersections;
		
		for (int x = 0; x < locations.length; x ++)
    	{
    		intersections = GridLocationFinder.getIntersecting(grid,locations,locations[x]);
    		intersectors.put(locations[x],intersections);    		
    	}
    	
    	return intersectors;
	}
	
	public static HashMap<Location,Location[]> getNonintersectors(Grid grid, Location [] locations)
	{		
		HashMap <Location,Location[]> nonintersectors = new HashMap<Location,Location[]>();
		Location [] nonintersections;
		
		for (int x = 0; x < locations.length; x ++)
    	{
    		nonintersections = GridLocationFinder.getNotIntersecting(grid,locations,locations[x]);
    		nonintersectors.put(locations[x],nonintersections);    		
    	}
    	
    	return nonintersectors;
	}
	
	public static HashMap<Location,Location[]> getIntersectorsAndAdjacents(Grid grid, Location [] locations)
	{		
		HashMap <Location,Location[]> intersectorsAdjacents = new HashMap<Location,Location[]>();
		Location [] intersectionsAdjacents;
		
		for (int x = 0; x < locations.length; x ++)
    	{
    		intersectionsAdjacents = GridLocationFinder.getIntersectingOrAdjacent(grid,locations,locations[x]);
    		intersectorsAdjacents.put(locations[x], intersectionsAdjacents);    		
    	}
    	
    	return intersectorsAdjacents;
	}	
	
	public static Location [] getIntersectingOrAdjacentWithIncompleteIntersector(Grid grid, Location location, Location [] locations, HashMap<Location, Location []> intersectors)
	{		
		ArrayList<Location> goodLocations = new ArrayList<Location>();
		
		Location [] intersections1 = intersectors.get(location);		
		intersections1 = GridLocationFinder.getHasBlanks(grid, intersections1);
		intersections1 = GridLocationFinder.getCommonLocations(intersections1, locations);
			
		for (Location testLocation: locations)
		{
			if (location.getDirection().equals(testLocation.getDirection()))
			{				
				Location [] intersections2 = intersectors.get(testLocation);
				intersections2 = GridLocationFinder.getHasBlanks(grid, intersections2);
				intersections2 = GridLocationFinder.getCommonLocations(intersections1, intersections2);
				
				if (intersections2.length != 0)
				{
					goodLocations.add(testLocation);
				}
			}
			else
			{
				goodLocations.add(testLocation);
			}
		}
		
		return goodLocations.toArray(new Location[goodLocations.size()]);
	}				
}