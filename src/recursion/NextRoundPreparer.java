package recursion;
import grid.FillInfo;
import grid.Grid;
import grid.GridInfo;
import heuristic.BestLocationFinder;
import word.WordFitting;

public class NextRoundPreparer
{	
	private NextRoundPreparer()
	{		
	}
		
	public static GridInfo getNextRound(GridInfo gridInfo, BestLocationFinder locationFinder, WordFitting fitting)
	{		
		gridInfo.addCurrentFillToList();
		
  		Grid newGrid = new Grid(gridInfo.getCurrentGrid());
  		newGrid.setWord(gridInfo.getCurrentLocation(), gridInfo.getWordAtCurrentIndex()); 
		
		gridInfo.setCurrentFill(new FillInfo(newGrid, gridInfo.getCurrentLocation()));	
						
		gridInfo.setCurrentLocation(locationFinder.findNextLocation(gridInfo, fitting));
								 
		if (gridInfo.getCurrentLocation().getRow() == -1)
		{			
			return gridInfo;					 					
		} 
				
  		gridInfo.setCurrentIndex(-1);
  					    	  				    	
    	gridInfo.findCurrentFittableWords(fitting);
    			
		return gridInfo;   	
	}	
}