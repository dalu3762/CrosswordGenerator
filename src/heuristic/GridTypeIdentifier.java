package heuristic;
import grid.Grid;
import location.Location;

public class GridTypeIdentifier
{	
	private GridTypeIdentifier()
	{		
	}
	
	public static String getType(Grid grid, Location [] locations)
	{
		int numAreas = Isolation.getNumIsolatedAreas(grid);
		
		Location [] oneWayPaths = OneWayPath.getOneWayPath(grid, locations);
		Location [] oneWayPathsNotDivide = Isolation.getNotIsolating(grid, locations);
		Location [] oneWayPathsDivide = Isolation.getDividers(grid, locations, numAreas);
		Location [] doubleDivide = Isolation.getDoubleDivide(grid, locations, numAreas);		
		
		if (oneWayPaths.length == 0 && doubleDivide.length == 0)
		{
			//To Do: Expand
			return "Misc";
		}
		else if (doubleDivide.length == 0)
		{
			if (oneWayPathsNotDivide.length == 0 && oneWayPathsDivide.length > 0)
			{
				return "NoForkWithDivider";
			}
			else if (oneWayPathsNotDivide.length > 0 && oneWayPathsDivide.length == 0)
			{
				return "WithForkNoDivider";
			}
			else
			{
				return "WithForkWithDivider";
			}
		}
		else if (oneWayPaths.length == 0)
		{		
			return "DoubleDivider";
		}
		else
		{
			String originalWord = "";
			String originalWord2 = "";
			
			int halfTotalBlanks = grid.getFrequency(grid.getBlankSymbol()) / 2;
			
			int closestToHalfSingle = 1000000;
			int closestToHalfDouble = 1000000;
			
			int amountIsolated = 0;
			
			for (int x = 0; x < oneWayPathsDivide.length; x ++)
			{
				originalWord = grid.getWord(oneWayPathsDivide[x]);				
				grid.replace(oneWayPathsDivide[x], grid.getBlankSymbol(), 'A');
				
				int [] coordinate = Isolation.getMostIsolatedCoordinate(grid);
				
				amountIsolated = Math.abs(halfTotalBlanks - Isolation.getNumConnected(grid,coordinate));
				
				if (amountIsolated < closestToHalfSingle)
				{
					closestToHalfSingle = amountIsolated;
				}
				
				grid.setWord(oneWayPathsDivide[x], originalWord);							
			}
			
			for (int x = 0; x < doubleDivide.length; x += 2)
			{
				if (Isolation.isolates(grid, numAreas, doubleDivide[x]) || Isolation.isolates(grid, numAreas , doubleDivide[x + 1]))
				{
					continue;
				}
				
				originalWord = grid.getWord(doubleDivide[x]);
				originalWord2 = grid.getWord(doubleDivide[x + 1]);
				
				grid.replace(doubleDivide[x], grid.getBlankSymbol(), 'A');
				grid.replace(doubleDivide[x + 1], grid.getBlankSymbol(), 'A');
				
				int [] coordinate = Isolation.getMostIsolatedCoordinate(grid);
				
				amountIsolated = Math.abs(halfTotalBlanks - Isolation.getNumConnected(grid,coordinate));
				
				if (amountIsolated < closestToHalfDouble)
				{
					closestToHalfDouble = amountIsolated;
				}
				
				grid.setWord(doubleDivide[x], originalWord);
				grid.setWord(doubleDivide[x + 1], originalWord2);
			}
			
			if (closestToHalfSingle < closestToHalfDouble)
			{
				if (oneWayPathsNotDivide.length == 0)
				{
					return "NoForkWithDivider";
				}
				else
				{
					return "WithForkWithDivider";
				}
			}
			else
			{
				return "DoubleDivider";
			}
		}		
	}	
}