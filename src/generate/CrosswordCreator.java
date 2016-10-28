package generate;
import java.io.IOException;

import grid.Grid;
import grid.GridInfo;
import heuristic.BestLocationFinder;
import heuristic.NextWordFinder;
import io.FileIO;
import location.Location;
import recursion.Backtrack;
import recursion.NextRoundPreparer;
import word.WordFitting;

public class CrosswordCreator {		
		
	private CrosswordCreator()
	{		
	}
        
    public static Grid generateCrossword(String gridFile, String wordListFile, char blankSymbol, char wallSymbol, int maxSeconds) throws IOException
    {
    	long startTime = System.currentTimeMillis() / 1000;
    	long endTime = 0;    	
    		
    	Grid grid = FileIO.getGridFromFile(gridFile, blankSymbol, wallSymbol);
    	GridInfo gridInfo = new GridInfo(grid); 
    	
    	String [] wordList = FileIO.readFileByLine(wordListFile);
    	WordFitting fitting = new WordFitting(wordList);   	   	
    	    	
    	BestLocationFinder locationFinder = new BestLocationFinder(gridInfo);
    	 	    	
    	locationFinder.setup(gridInfo, fitting);
    	
    	Location firstLocation = locationFinder.getLocationToFill();
    	
    	gridInfo.setCurrentLocation(firstLocation);
    	gridInfo.setCurrentIndex(-1);   		  	    	
    	gridInfo.findCurrentFittableWords(fitting);
    	gridInfo.addCurrentFillToList();   	
    	
    	do
    	{    		
    		while (true)
    		{
    			gridInfo = NextWordFinder.updateToNextWordIndex(gridInfo, fitting);   
    			
    			if (Backtrack.needToBacktrack(gridInfo) == true)
	    		{	    				    			    			
	    			gridInfo = Backtrack.backtrack(gridInfo, fitting);	    			
	    	  			    			
	    			if (Backtrack.isFailure(gridInfo))
	    			{
	    				return grid;
	    			}  			
	    		}
	    		else
	    		{	    					    				    			
	    		   	gridInfo = NextRoundPreparer.getNextRound(gridInfo, locationFinder, fitting);	  			    					    	    			    			    					    	     			
	    	  			    	  			    	  			    	  		  			    	  		
			    	if (gridInfo.getCurrentLocation() == null || grid.hasBlank(gridInfo.getCurrentLocation()) == false)
			    	{
			    		return gridInfo.getCurrentGrid();
			    	}
			    	
			    	break; 		 	 
		    	}
    		}
    		    	
	    	endTime = System.currentTimeMillis() / 1000;	    		  	    	    	  	  		    		   		    		 		
    	}while (endTime - startTime < maxSeconds);
    	
    	//Timed out
    	return grid;
    }      
}