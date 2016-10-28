package heuristic;
import java.util.ArrayList;

import grid.Grid;
import location.Location;

public class OneWayPath
{
	/*This class finds one-way paths in Grids
	 *
	 *    ---W----
	 *    ---W----
	 *    ---W----
	 * -> --------
	 *    WWW-----
	 *    --------
	 *    --------
	 *
	 *	  - = empty space, W = wall
	 *
	 *    Marked row has a one-way path, as a square below the entry is a wall, and a square one column over on the other side 
	 *    of the entry also has a wall, and there are not filled-in letters on both the left and right sides of where the two walls meet
	 */
			
	private OneWayPath()
	{		
	}
	
	public static Location [] getOneWayPath(Grid grid, Location [] locations)
	{		
		ArrayList<Location> oneWays = new ArrayList<Location>();
		
		for (Location location: locations)
		{
			if (isOneWayPath(grid,location))
			{
				oneWays.add(location);
			}
		}
		
		return oneWays.toArray(new Location[oneWays.size()]);
	}
		
	public static Location [] getNotBothOneWayPathAndNotDivider(Grid grid, Location [] locations)
	{		
		ArrayList<Location> newLocations = new ArrayList<Location>();
		int numAreas = Isolation.getNumIsolatedAreas(grid);
			
		for (Location location: locations)
		{
			if (OneWayPath.isNotBothOneWayPathAndNotDivider(grid, location, numAreas) == true)
			{
				newLocations.add(location);
			}
			
		}
		
		return newLocations.toArray(new Location[newLocations.size()]);
	}
	
	public static boolean isNotBothOneWayPathAndNotDivider(Grid grid, Location location, int numAreas)
	{
		if (OneWayPath.isOneWayPath(grid,location) == true && Isolation.isolates(grid, numAreas, location) == false)
		{
			return false;
		}
		else
		{
			return true;
		}
	}	
	
	//Row and column should be the square that's farther up/left	
	public static boolean isOneWayPath(Grid grid, Location location)
	{		
		int row = location.getRow();
		int column = location.getColumn();
		String direction = location.getDirection();
		int length = location.getLength();
		
		return isOneWayPath(grid, row, column, direction, length);		
	}
	
	//Row and column should be the square that's farther up/left
	public static boolean isOneWayPath(Grid grid, int row, int column, String direction, int length)
	{
		int midpoint = 0;
		int midpoint2 = 0;
		
		int numColumns = grid.getNumColumns();
		int numRows = grid.getNumRows();
		
		boolean backHasLetter = false;
		boolean frontHasLetter = false;
		
		if (direction.equals("H"))
		{									
			if (row == 0 || row == numRows - 1)
			{
				return false;
			}			
			
			for (int x = 0; x < length - 1; x ++)
			{				
				if ( (grid.getSquare(row - 1, column + x) == grid.getWallSymbol() && grid.getSquare(row + 1, column + x + 1) == grid.getWallSymbol()) || (grid.getSquare(row + 1, column + x) == grid.getWallSymbol() && grid.getSquare(row - 1, column + x + 1) == grid.getWallSymbol()) )
				{
					midpoint = column + x;
					midpoint2 = column + x + 1;
					
					while (midpoint >= 0)
					{
						if (grid.getSquare(row,midpoint) == grid.getWallSymbol())
						{
							break;
						}
						else if (grid.getSquare(row, midpoint) != grid.getBlankSymbol())
						{
							backHasLetter = true;
							break;
						}
						
						midpoint --;
					}
					
					if (backHasLetter == false)
					{
						return true;
					}
					
					while (midpoint2 < numColumns)
					{
						if (grid.getSquare(row, midpoint2) == grid.getWallSymbol())
						{
							break;	
						}
						
						else if (grid.getSquare(row,midpoint2) != grid.getBlankSymbol())
						{
							frontHasLetter = true;
							break;
						}
						
						midpoint2 ++;
					}
					
					return frontHasLetter == false ? true : false;			
				}
			}
			
		}
		else
		{
			if (column == 0 || column == numColumns - 1)
			{
				return false;
			}
			
			for (int x = 0; x < length - 1; x ++)
			{
				if ( (grid.getSquare(row + x,column - 1) == grid.getWallSymbol() && grid.getSquare(row + x + 1, column + 1) == grid.getWallSymbol()) || (grid.getSquare(row + x,column + 1) == grid.getWallSymbol() && grid.getSquare(row + x + 1,column - 1) == grid.getWallSymbol()) )
				{
					midpoint = row + x;
					midpoint2 = row + x + 1;
					
					while (midpoint >= 0)
					{
						if (grid.getSquare(midpoint,column) == grid.getWallSymbol())
						{
							break;
						}
						else if (grid.getSquare(midpoint,column) != grid.getBlankSymbol())
						{
							backHasLetter = true;
							break;	
						}
						
						midpoint --;
					}
					
					if (backHasLetter == false)
					{
						return true;
					}
					
					while (midpoint2 < numRows)
					{
						if (grid.getSquare(midpoint2,column) == grid.getWallSymbol())
						{
							break;	
						}
						else if (grid.getSquare(midpoint2,column) != grid.getBlankSymbol())
						{
							frontHasLetter = true;
							break;	
						}
						
						midpoint2 ++;
					}
					
					return frontHasLetter == false ? true : false;			
				}
			}
			
						
		}
		
		return false;
	}				
}