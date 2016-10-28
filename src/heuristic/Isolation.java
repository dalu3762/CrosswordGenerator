package heuristic;
import java.util.ArrayList;
import java.util.HashMap;

import grid.Grid;
import location.Location;
import location.LocationPair;

public class Isolation
{	
	private Isolation()
	{		
	}
		
	public static int getNumIsolatedAreas(Grid grid)
	{		
		int frequency = 0;
				
		for (int row = 0; row < grid.getNumRows(); row ++)
		{
			for (int column = 0; column < grid.getNumColumns(); column ++)
			{				
				if (grid.getSquare(row,column) == grid.getBlankSymbol())
				{
					frequency ++;					
					grid = travelAllDirections(grid,row,column);					
				}
			}
		}
				
		grid.replace('t',grid.getBlankSymbol());
		
		return frequency;
	}
	
	//Returns upper-left square coordinate of most isolated area with at least one letter. Return value: index 0 is row, index 1 is column
	public static int [] getMostIsolatedCoordinate(Grid grid)
	{		
		int bestRow = -1;
		int bestColumn = -1;
		int numSquares = 2000000;
		int frequency = 0;		
		
		for (int row = 0; row < grid.getNumRows(); row ++)
		{
			for (int column = 0; column < grid.getNumColumns(); column ++)
			{								
				if (grid.getSquare(row,column) == grid.getBlankSymbol())
				{
					grid = travelAllDirections(grid,row,column);
					
					frequency = grid.getFrequency('t');
					
					if (frequency < numSquares)
					{
						numSquares = frequency;
						bestRow = row;
						bestColumn = column;						
					}
					
					grid.replace('t','q');
				}
			}
		}
		
		int [] coordinate = {bestRow,bestColumn};
		
		grid.replace('q',grid.getBlankSymbol());
		
		return coordinate;
	}
	
	private static Grid travelAllDirections(Grid grid, int row, int column)
	{		
		if (grid.getSquare(row,column) == grid.getBlankSymbol())
		{
			grid.setSquare(row,column, 't');
		}
		
		if (column != 0)
		{
			grid = travelLeft(grid,row, column);
		}
				
		if (row != 0)
		{
			grid = travelUp(grid,row, column);
		}
		
		if (row < grid.getNumRows() - 1)
		{
			grid = travelDown(grid,row, column);
		}
		
		if (column < grid.getNumColumns() - 1)
		{
			grid = travelRight(grid,row, column);
		}
		
		return grid;
	}
	
	private static Grid travelLeft(Grid grid, int row, int column)
	{
		char character = ' ';
		column --;
		
		while (column >= 0)
		{
			character = grid.getSquare(row,column);
			
			if (character == grid.getWallSymbol())
			{
				return grid;
			}
			
			if (character == grid.getBlankSymbol())
			{				
				grid = travelAllDirections(grid,row,column);
			}
			
			column --;			
		}
		
		return grid;
	}
	
	private static Grid travelRight(Grid grid, int row, int column)
	{
		char character = ' ';
		column ++;
		
		while (column < grid.getNumColumns())
		{
			character = grid.getSquare(row,column);
			
			if (character == grid.getWallSymbol())
			{
				return grid;
			}
			
			if (character == grid.getBlankSymbol())
			{				
				grid = travelAllDirections(grid,row,column);
			}
			
			column ++;			
		}
		
		return grid;
	}
	
	private static Grid travelUp(Grid grid, int row, int column)
	{		
		char character = ' ';
		row --;
				
		while (row >= 0)
		{
			character = grid.getSquare(row,column);
			
			if (character == grid.getWallSymbol())
			{
				return grid;
			}		
			
			if (character == grid.getBlankSymbol())
			{
				grid = travelAllDirections(grid,row,column);
			}
			
			row --;			
		}
		
		return grid;
	}
	
	private static Grid travelDown(Grid grid, int row, int column)
	{		
		char character = ' ';
		row ++;
		
		while (row < grid.getNumRows())
		{
			character = grid.getSquare(row,column);
			
			if (character == grid.getWallSymbol())
			{
				return grid;
			}		
			
			if (character == grid.getBlankSymbol())
			{				
				grid = travelAllDirections(grid,row,column);				
			}
			
			row ++;			
		}
		
		return grid;
	}
	
	public static boolean isolates(Grid grid, int numAreas, Location...locations)
	{		
		String [] originals = new String[locations.length];
		
		for (int x = 0; x < originals.length; x ++)
		{
			originals[x] = grid.getWord(locations[x]);
		}
		
		for (int x = 0; x < locations.length; x ++)
		{
			grid.replace(locations[x], grid.getBlankSymbol(), 'A');
		}
		
		int newNumAreas = Isolation.getNumIsolatedAreas(grid);
		
		for (int x = 0; x < originals.length; x ++)
		{
			grid.setWord(locations[x], originals[x]);
		}
		
		return newNumAreas > numAreas ? true : false;
	}
	
	public static Location [] getDividers(Grid grid, Location [] locations, int numAreas)
	{		
		String originalWord = "";
		String newWord = "";
		int newNumAreas = 0;
		ArrayList<Location> dividers = new ArrayList<Location>();
		
		for (Location location: locations)
		{
			originalWord = grid.getWord(location);
			newWord = "";
			
			while (newWord.length() < originalWord.length())
			{
				newWord = newWord + "A";
			}
			
			grid.setWord(location,newWord);
			
			newNumAreas = Isolation.getNumIsolatedAreas(grid);
			
			if (newNumAreas > numAreas)
			{
				dividers.add(location);
			}
			
			grid.setWord(location, originalWord);			
		}
		
		return dividers.toArray(new Location[dividers.size()]);
	}
	
	public static Location [] getDoubleDivideMostSquares(Grid grid, Location [] locations, Location [] dividers, int numAreas)
	{		
		int targetNumSlots = GridLocationFinder.getLocations(grid).length / 2;		
				
		Location bestDivideA = new Location();
		Location bestDivideB = new Location();
				
		String originalWord1;
		String originalWord2;
		
		int bestNumIsolatedLocations = 10000000;
		int numIsolatedLocations = 1000000;
		
		for (int x = 0; x < dividers.length; x += 2)
		{			
			if (Isolation.isolates(grid, numAreas, dividers[x]) || Isolation.isolates(grid, numAreas, dividers[x + 1]))
			{
				continue;
			}
			
			originalWord1 = grid.getWord(dividers[x]);
			originalWord2 = grid.getWord(dividers[x + 1]);
			
			grid.replace(dividers[x], grid.getBlankSymbol(), 'A');
			grid.replace(dividers[x + 1], grid.getBlankSymbol(), 'A');
			
			int [] coordinate = Isolation.getMostIsolatedCoordinate(grid);			
			
			numIsolatedLocations = Isolation.getIsolatedLocations(grid, coordinate, locations).length;
						
			if (Math.abs(targetNumSlots - numIsolatedLocations) < bestNumIsolatedLocations)
			{			
				bestDivideA = dividers[x];
				bestDivideB = dividers[x + 1];
				
				bestNumIsolatedLocations = Math.abs(targetNumSlots - numIsolatedLocations);
			}
			
			grid.setWord(dividers[x], originalWord1);
			grid.setWord(dividers[x + 1], originalWord2);
		}
		
		Location [] bests = {bestDivideA, bestDivideB};
		return bests;
	}
	
	public static Location [] getDoubleDivide(Grid grid, Location [] locations, int numAreas)
	{		
		ArrayList<Location> doubleDivide = new ArrayList<Location>();
		
		for (int x = 0; x < locations.length - 1; x ++)
		{				
			for (int y = x + 1; y < locations.length; y ++)
			{				
				if (Isolation.isolates(grid, numAreas, locations[x]) == false && Isolation.isolates(grid, numAreas, locations[y]) == false && Isolation.isolates(grid, numAreas, locations[x], locations[y]) == true)
				{
					doubleDivide.add(locations[x]);
					doubleDivide.add(locations[y]);
				}
			}			
		}
		
		return doubleDivide.toArray(new Location[doubleDivide.size()]);
	}
	
	public static Location [] getIsolatedLocations(Grid grid, int [] coordinate, Location [] locations)
	{		
		return getIsolatedLocations(grid,coordinate[0],coordinate[1], locations);
	}
	
	public static Location [] getIsolatedLocations(Grid grid, int isolationRow, int isolationColumn, Location [] locations)
	{				
		ArrayList<Location> isolatedLocations = new ArrayList<Location>();
		grid = travelAllDirections(grid,isolationRow, isolationColumn);
		
		for (Location location: locations)
		{
			if (grid.getWord(location).contains("t"))
			{
				isolatedLocations.add(location);
			}
		}
		
		grid.replace('t',grid.getBlankSymbol());
		
		return isolatedLocations.toArray(new Location[isolatedLocations.size()]);
	}
	
	public static Location [] getMostIsolatedLocations(Grid grid, Location [] locations)
	{		
		int [] coordinate = Isolation.getMostIsolatedCoordinate(grid);
		Location [] isolatedLocations = Isolation.getIsolatedLocations(grid,coordinate[0],coordinate[1],locations);
		return isolatedLocations;
	}
	
	public static int getNumConnected(Grid grid, int [] coordinate)
	{		
		return getNumConnected(grid,coordinate[0],coordinate[1]);
	}
	
	public static int getNumConnected(Grid grid, int row, int column)
	{
		grid = Isolation.travelAllDirections(grid, row, column);
		int frequency = grid.getFrequency('t');
		
		grid.replace('t',grid.getBlankSymbol());
		return frequency;
	}	
	
	public static boolean isConnected(Grid grid, int row, int column, Location location)
	{								    	
    	while (grid.getSquare(row,column) != grid.getBlankSymbol())
		{
			if (location.isHorizontal())
			{
				column ++;
			}
			else
			{
				row ++;
			}
		}
		
    	grid.setSquare(row,column,'t');
    	
		grid = travelAllDirections(grid,row,column);
		
		if (grid.getWord(location).contains("t"))
		{
			grid.replace('t',grid.getBlankSymbol());
			return true;
		}
		else
		{
			grid.replace('t',grid.getBlankSymbol());
			return false;
		}		
	}
	
	public static Location findMostIsolating(Grid grid, Location [] dividers)
	{
		String originalWord = "";
		int lowestNumSquares = 1000000;
		int numSquares;
		Location mostDividing = new Location();
		
		for (int x = 0; x < dividers.length; x ++)
		{
			originalWord = grid.getWord(dividers[x]);
			
			grid.replace(dividers[x],grid.getBlankSymbol(),'A');
			
			int [] coordinate = Isolation.getMostIsolatedCoordinate(grid);
			numSquares = Isolation.getNumConnected(grid,coordinate);
			
			if (numSquares < lowestNumSquares)
			{
				mostDividing = dividers[x];
				lowestNumSquares = numSquares;
			}
			
			grid.setWord(dividers[x],originalWord);
		}
		
		return mostDividing;
	}
	
	public static Location [] getNotIsolating(Grid grid, Location [] locations)
	{
		int numAreas = Isolation.getNumIsolatedAreas(grid);
		ArrayList<Location> notIsolating = new ArrayList<Location>();
		
		for (Location location: locations)
		{
			if (Isolation.isolates(grid, numAreas, location) == false)
			{
				notIsolating.add(location);
			}
		}
		
		return notIsolating.toArray(new Location[notIsolating.size()]);
	}	
	
	public static HashMap<Location, Location[]> getDoubleDividerMap(Grid grid, Location [] locations)
	{		
		HashMap<Location, Location[]> doubleDividerMap = new HashMap<Location, Location[]>();
		
		int numAreas = Isolation.getNumIsolatedAreas(grid);
				
		for (int x = 0; x < locations.length; x ++)
		{
			if (Isolation.isolates(grid, numAreas, locations[x]))
			{
				continue;
			}
			
			ArrayList<Location> isolatedLocations = new ArrayList<Location>();
			
			for (int y = 0; y < locations.length; y ++)
			{				
				if (x == y || Isolation.isolates(grid, numAreas, locations[y]))
				{
					continue;
				}
				
				if (Isolation.isolates(grid, numAreas, locations[x], locations[y]))
				{
					isolatedLocations.add(locations[y]);
				}				
			}
			
			doubleDividerMap.put(locations[x], isolatedLocations.toArray(new Location[isolatedLocations.size()]));
		}
				
		return doubleDividerMap;
	}
	
	public static HashMap<LocationPair, Location[]> getDoubleDividerIsolationMap (Grid grid, Location [] locations)
	{
		int numAreas = Isolation.getNumIsolatedAreas(grid);
				
		Location [] doubleDividers = getDoubleDivide(grid, locations, numAreas);
			
		HashMap<LocationPair, Location[]> doubleDividerIsolationMap = new HashMap<LocationPair, Location[]>();
		String originalWord1 = "";
		String originalWord2 = "";
		
		for (int x = 0; x < doubleDividers.length; x += 2)
		{
			originalWord1 = grid.getWord(doubleDividers[x]);
			originalWord2 = grid.getWord(doubleDividers[x + 1]);
			
			grid.replace(doubleDividers[x], grid.getBlankSymbol(), 'A');
			grid.replace(doubleDividers[x + 1], grid.getBlankSymbol(), 'A');
			
			Location [] mostIsolated = Isolation.getMostIsolatedLocations(grid, locations);
			
			LocationPair pair = new LocationPair(doubleDividers[x], doubleDividers[x + 1]);
			
			doubleDividerIsolationMap.put(pair, mostIsolated);
			
			pair = new LocationPair(doubleDividers[x + 1], doubleDividers[x]);
			
			doubleDividerIsolationMap.put(pair, mostIsolated);
			
			grid.setWord(doubleDividers[x], originalWord1);
			grid.setWord(doubleDividers[x + 1], originalWord2);			
		}
		
		return doubleDividerIsolationMap;		
	}	
}	