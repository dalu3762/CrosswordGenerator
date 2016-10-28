package grid;
import java.util.ArrayList;
import java.util.HashMap;

import heuristic.GridLocationFinder;
import heuristic.GridTypeIdentifier;
import heuristic.Isolation;
import location.Location;
import location.LocationPair;
import word.WordFitting;

public class GridInfo
{
	private FillInfo currentFill;
		
	private ArrayList<FillInfo> allFills;
	
	private final Location [] locations;
		
	private final HashMap <Location, Location []> intersectors;
	private final HashMap <Location, Location []> nonintersectors;
	private final HashMap <Location, Location []> intersectorsAdjacents;
	private final HashMap <Location, Location []> doubleDivider;
	private final HashMap <LocationPair, Location[]> doubleDividerIsolated;
	
	private String gridType;
			
	public GridInfo(Grid grid)
	{		
		this.locations = GridLocationFinder.getLocations(grid);
		
		this.currentFill = new FillInfo();
		this.currentFill.setGrid(grid);
		
		this.allFills = new ArrayList<FillInfo>();
		
		this.intersectors = GridLocationFinder.getIntersectors(this.currentFill.getGrid(), locations);
		this.nonintersectors = GridLocationFinder.getNonintersectors(this.currentFill.getGrid(),locations);    	
    	this.intersectorsAdjacents = GridLocationFinder.getIntersectorsAndAdjacents(this.currentFill.getGrid(),locations);
    	this.doubleDivider = Isolation.getDoubleDividerMap(this.currentFill.getGrid(), locations);    	    	
    	this.doubleDividerIsolated = Isolation.getDoubleDividerIsolationMap(this.currentFill.getGrid(), locations);
    	
    	this.gridType = GridTypeIdentifier.getType(this.currentFill.getGrid(), locations);    	
	}
	
	public Grid getCurrentGrid()
	{
		return this.currentFill.getGrid();
	}
	
	public Location [] getLocations()
	{
		return this.locations;
	}
	
	public Location getCurrentLocation()
	{
		return this.currentFill.getLocation();
	}
	
	public int getCurrentIndex()
	{
		return this.currentFill.getIndex();
	}
	
	public String getWordAtCurrentIndex()
	{
		return this.currentFill.getWordAtIndex();
	}
	
	public String [] getCurrentFittableWords()
	{
		return this.currentFill.getFittableWords();
	}
	
	public String getGridType()
	{
		return this.gridType;
	}
	
	public int getNumCurrentFittableWords()
	{
		return this.currentFill.getNumFittableWords();
	}
	
	public void setCurrentFill(FillInfo fill)
	{
		this.currentFill = fill;
	}
	
	public void setCurrentGrid(Grid newGrid)
	{
		this.currentFill.setGrid(newGrid);
	}
	
	public void	setCurrentLocation(Location newLocation)
	{
		this.currentFill.setLocation(newLocation);
	}
	
	public void setCurrentIndex(int index)
	{
		this.currentFill.setIndex(index);
	}
	
	public void incrementCurrentIndex()
	{
		this.currentFill.incrementIndex();
	}
	
	public void setGridType(String gridType)
	{
		this.gridType = gridType;
	}
	
	public void findCurrentFittableWords(WordFitting fitting)
	{
		this.currentFill.setFittableWords(fitting.findFittableWords(this.getCurrentGrid(), this.getCurrentLocation()));
	}
	
	public HashMap<Location, Location []> getIntersectors()
	{
		return this.intersectors;
	}
	
	public HashMap<Location, Location []> getNonintersectors()
	{
		return this.nonintersectors;
	}
	
	public HashMap<Location, Location []> getIntersectorsAdjacents()
	{
		return this.intersectorsAdjacents;
	}
	
	public HashMap<Location, Location []> getDoubleDivider()
	{
		return this.doubleDivider;
	}
	
	public HashMap<LocationPair, Location []> getDoubleDividerIsolated()
	{
		return this.doubleDividerIsolated;
	}
	
	public int getFillListSize()
	{
		return this.allFills.size();
	}
	
	public void addCurrentFillToList()
	{
		this.allFills.add(currentFill);
	}
	
	public void removeLatestFill()
	{
		this.currentFill = allFills.remove(this.allFills.size() - 1);
	}	
}