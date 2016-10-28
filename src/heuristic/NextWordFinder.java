package heuristic;
import grid.Grid;
import grid.GridInfo;
import location.Location;
import word.WordFitting;

public class NextWordFinder
{	
	public NextWordFinder()
	{		
	}	
		
	public static GridInfo updateToNextWordIndex(GridInfo gridInfo, WordFitting fitting)
	{
		Grid grid = gridInfo.getCurrentGrid();
		Location currentLocation = gridInfo.getCurrentLocation();
							
		String originalWord = grid.getWord(currentLocation);
		
		if (gridInfo.getCurrentIndex() == -1)
		{
			gridInfo.incrementCurrentIndex();			
		}
		
		String [] fittableWords = gridInfo.getCurrentFittableWords();		
			  		 		    		    		
		//Check each word to see if it fits
		for (int x = gridInfo.getCurrentIndex(); x < fittableWords.length; x ++)
		{
			grid.setWord(currentLocation,fittableWords[x]);	
			
			if (ValidityChecker.isValidGrid(grid,fitting, gridInfo.getIntersectors(),gridInfo.getNonintersectors(), currentLocation))
			{
				grid.setWord(currentLocation, originalWord);
				gridInfo.setCurrentGrid(grid);
				return gridInfo; 
			}
			else
			{
				grid.setWord(currentLocation, originalWord);
				gridInfo.setCurrentGrid(grid);
			}		
			
			gridInfo.incrementCurrentIndex();		   			
		} 			
		
		return gridInfo;	
	}
}