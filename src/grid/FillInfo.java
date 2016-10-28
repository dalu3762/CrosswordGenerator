package grid;
import location.Location;
import word.WordFitting;

public class FillInfo
{
	private Grid grid;	
	private Location location;
		
	private String [] fittableWords;		
	private int index;
	
	public FillInfo()
	{
		
	}
	
	public FillInfo(Grid grid, Location location)
	{
		this.grid = grid;
		this.location = location;
	}	
			
	public FillInfo(Grid grid, Location location, int index, WordFitting fitting)
	{
		this.grid = grid;
		this.location = location;
		this.fittableWords = fitting.findFittableWords(grid, location);
		this.index = index;
	}
	
	public Grid getGrid()
	{
		return this.grid;
	}
	
	public Location getLocation()
	{
		return this.location;
	}
	
	public int getIndex()
	{
		return this.index;
	}
	
	public int getNumFittableWords()
	{
		return this.fittableWords.length;
	}
	
	public String [] getFittableWords()
	{
		return this.fittableWords;
	}
	
	public String getWordAtIndex(int listIndex)
	{
		return this.fittableWords[this.index];
	}
	
	public String getWordAtIndex()
	{
		return this.fittableWords[this.index];
	}
		
	public void setGrid(Grid grid)
	{
		this.grid = grid;
	}
	
	public void setLocation(Location location)
	{
	    this.location = location;
	}
	
	public void setIndex(int index)
	{
		this.index = index;
	}
	
	public void setFittableWords(String [] words)
	{
		this.fittableWords = words;
	}	
	
	public void incrementIndex()
	{
		this.index ++;
	}
}