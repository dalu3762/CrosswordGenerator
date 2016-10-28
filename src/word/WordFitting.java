package word;
import grid.Grid;
import location.Location;

public class WordFitting
{
	private WordFinder finder;		
			
	public WordFitting(String [] wordList)
	{
		finder = new WordFinder(wordList);			
	}
	
	public String [] findFittableWords(Grid grid, Location location)
    {		  	
    	String pattern = grid.getWord(location);
    	return finder.getWordsPattern(grid, pattern);    	
    }    
    
    public int getNumFittableWords(Grid grid, Location location)
    {		
    	return findFittableWords(grid,location).length;
    }  
    
	public boolean isValid(Grid grid, Location [] locations)
	{		
		for (int x = 0; x < locations.length; x ++)
		{
			if (getNumFittableWords(grid, locations[x]) == 0)
			{
				return false;
			}
		}
		
		return true;
	}	
}