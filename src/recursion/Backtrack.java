package recursion;
import grid.GridInfo;
import heuristic.Isolation;
import location.Location;
import word.WordFitting;

public class Backtrack
{	
	private Backtrack()
	{		
	}
	
	public static boolean needToBacktrack(GridInfo gridInfo)
	{		
		int index = gridInfo.getCurrentIndex();
		
		return index >= gridInfo.getNumCurrentFittableWords() ? true : false;
	}
	
	public static boolean isFailure(GridInfo gridInfo)
	{		
		return gridInfo.getFillListSize() == 0 && gridInfo.getCurrentIndex() >= gridInfo.getNumCurrentFittableWords() ? true : false;
	}
	
	public static GridInfo backtrack(GridInfo gridInfo, WordFitting fitting)
	{
		Location currentLocation = gridInfo.getCurrentLocation();
		
		int row = currentLocation.getRow();
		int column = currentLocation.getColumn();
		
		//Entry must have a blank, find one
		if (currentLocation.isHorizontal())
		{
			while (gridInfo.getCurrentGrid().getSquare(row, column) != gridInfo.getCurrentGrid().getBlankSymbol())
			{
				column ++;
			}
		}
		else
		{
			while (gridInfo.getCurrentGrid().getSquare(row, column) != gridInfo.getCurrentGrid().getBlankSymbol())
			{
				row ++;
			}
		}			
				
		int initialNumSquares = Isolation.getNumConnected(gridInfo.getCurrentGrid(), row, column);
		int currentNumSquares = initialNumSquares;		
			
		while (currentNumSquares <= initialNumSquares || gridInfo.getCurrentIndex() >= gridInfo.getNumCurrentFittableWords())
		{
			if (gridInfo.getFillListSize() == 0) //should mean impossible to fill grid if it returns true here
			{
				gridInfo.incrementCurrentIndex();				
				return gridInfo;
			}
			
			gridInfo.removeLatestFill();			
  			currentNumSquares = Isolation.getNumConnected(gridInfo.getCurrentGrid(), row, column); 	
		}
		
		gridInfo.incrementCurrentIndex();		
		return gridInfo;	
	}
}