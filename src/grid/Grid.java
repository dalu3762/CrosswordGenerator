package grid;
import location.Location;

public class Grid
{
	private char [][] grid;	//the crossword grid; [row][column], topmost row is index 0, leftmost column is index 0
	private char blankSymbol;
	private char wallSymbol;
		
	@SuppressWarnings("unused")
	private Grid()
	{				
	}
	
	public Grid(char [][] grid, char blankSymbol, char wallSymbol)
	{
		if (wallSymbol == 'A')
		{
			throw new IllegalArgumentException("Wall symbol cannot be 'A', reserved character");
		}
		else if (blankSymbol == 'A')
		{
			throw new IllegalArgumentException("Blank symbol cannot be 'A', reserved character");
		}
		this.grid = grid;
	}
	
	//Copy constructor
	public Grid(Grid oldGrid)
	{
		char [][] aGrid = oldGrid.getGrid();
		char [][] newGrid = new char[aGrid.length][aGrid[0].length];
		
		for (int row = 0; row < newGrid.length; row ++)
		{
			for (int column = 0; column < newGrid[0].length; column ++)
			{
				newGrid[row][column] = aGrid[row][column];
			}			
		}
		
		this.grid = newGrid;
		this.blankSymbol = oldGrid.getBlankSymbol();
		this.wallSymbol = oldGrid.getWallSymbol();
	}
	
	public Grid(String [] gridLines, char blankSymbol, char wallSymbol)
	{
		this.grid = parseGrid(gridLines);
		
		if (wallSymbol == 'A')
		{
			throw new IllegalArgumentException("Wall symbol cannot be 'A', reserved character");
		}
		else if (blankSymbol == 'A')
		{
			throw new IllegalArgumentException("Blank symbol cannot be 'A', reserved character");
		}
		
		this.blankSymbol = blankSymbol;
		this.wallSymbol = wallSymbol;
	}
	
	public char [][] parseGrid(String [] gridLines)
	{
		char [][] grid = new char[gridLines.length][gridLines[0].length()];
		
		for (int row = 0; row < grid.length; row ++)
		{
			for (int column = 0; column < grid[0].length; column ++)
			{
				grid[row][column] = gridLines[row].charAt(column);
			}
		}
		
		return grid;		
	}	
	
	public int getNumRows()
	{
		return grid.length;
	}
	
	public int getNumColumns()
	{
		return grid[0].length;
	}
	
	public String getRow(int rowNumber)
	{
		return new String(grid[rowNumber]);
	}	
	
	public String getColumn(int columnNumber)
	{
		String columnText = "";
					
		for (int rowNumber = 0; rowNumber < grid.length; rowNumber ++)
		{
			columnText = columnText + String.valueOf(grid[rowNumber][columnNumber]);
		}
		
		return columnText;
	}
	
	public char getSquare(int rowNumber, int columnNumber)
	{
		return grid[rowNumber][columnNumber];
	}
	
	public void setSquare(int rowNumber, int columnNumber, char character)
	{
		this.grid[rowNumber][columnNumber] = character;
	}
	
	public String getWord(Location location)
	{
		String word = "";
		
		if (location.isHorizontal())
		{
			for (int x = 0; x < location.getLength(); x ++)
			{
				word = word + getSquare(location.getRow(), location.getColumn() + x);
			}
		}
		else
		{
			for (int x = 0; x < location.getLength(); x ++)
			{
				word = word + getSquare(location.getRow() + x, location.getColumn());
			}
		}
		
		return word;
	}
	
	public String [] getWords(Location [] locations)
	{
		String [] words = new String[locations.length];
		
		for (int x = 0; x < words.length; x ++)
		{
			words[x] = getWord(locations[x]);
		}
		
		return words;
	}
	
	public void setWord(Location location, String word)
	{
		if (location.isHorizontal())
		{
			for (int x = 0; x < location.getLength(); x ++)
			{
				setSquare(location.getRow(),location.getColumn() + x, word.charAt(x));
			}
		}
		else
		{
			for (int x = 0; x < location.getLength(); x ++)
			{
				setSquare(location.getRow() + x,location.getColumn(), word.charAt(x));
			}
		}
	}
	
	//Deletes word at location (replaces with blank square symbols)
	public void deleteWord(Location location)
	{
		if (location.isHorizontal())
		{
			for (int x = 0; x < location.getLength(); x ++)
			{
				setSquare(location.getRow(), location.getColumn() + x, blankSymbol);
			}
		}
		else
		{
			for (int x = 0; x < location.getLength(); x ++)
			{
				setSquare(location.getRow() + x, location.getColumn(),blankSymbol);
			}
		}
	}
	
	public void printGrid()
	{
		System.out.println();
		for (int row = 0; row < grid.length; row ++)
		{
			for (int column = 0; column < grid[0].length; column ++)
			{
				System.out.print(grid[row][column]);
			}
			
			System.out.println();
		}
	}
	
	public boolean inGrid(Location [] locations, String word)
	{
		for(Location location: locations)
		{
			if (getWord(location).equals(word))
			{
				return true;
			}
		}
		
		return false;
	}
	
	//Return true if all squares in grid have been filled
	public boolean isComplete()
	{
		for (int row = 0; row < grid.length; row ++)
		{
			for (int column = 0; column < grid[0].length; column ++)
			{
				if (grid[row][column] == blankSymbol)
				{
					return false;
				}
			}
		}
		
		return true;
	}		
	
	//Returns true if word at location contains blank symbol	
	public boolean hasBlank(Location location)
	{
		String word = getWord(location);
		
		return word.contains(String.valueOf(blankSymbol)) ? true : false;
	}	
	
	//Returns true if all squares at location are empty (blank symbols)
	public boolean isAllBlank(Location location)
	{
		String word = getWord(location);
		word = word.replaceAll(String.valueOf(blankSymbol),"");
		
		return word.length() == 0 ? true : false;		
	}	
    
    //True if duplicate words; entries with blank symbols ignored
    public boolean containsDuplicate(Location [] locations)
    {
    	for (int x = 0; x < locations.length - 1; x ++)
    	{
    		if (hasBlank(locations[x]) == false)
    		{
    			for (int y = x + 1; y < locations.length; y ++)
	    		{
	    			if (getWord(locations[x]).equals(getWord(locations[y])))
    				{
    					return true;
    				}
	    		}
    			
    		}    		    		    		
    		
    	}
    	
    	return false;    	
    }
    
    //Checks word at location against array of locations for duplicate word; ignores blank entries; returns true if duplicate entry
    //If word contains blank, will return false
    public boolean containsDuplicate(Location [] locations, Location location)
    {
    	if (hasBlank(location) == true)
    	{
    		return false;
    	}
    	
    	for (int x = 0; x < locations.length - 1; x ++)
    	{    		
    		if (getWord(locations[x]).equals(getWord(location)))
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    //Checks words at locations in first array without blanks versus words at locations in second array without blanks; true if duplicate
    public boolean containsDuplicate(Location [] locations1, Location [] locations2)
    {    	
    	//Intersectors against nonintersectors
    	for (int x = 0; x < locations1.length; x ++)
    	{
    		if (hasBlank(locations1[x]) == false)
    		{
    			for (int y = 0; y < locations2.length; y ++)
	    		{    			
	    			if (getWord(locations1[x]).equals(getWord(locations2[y])))
	    			{
	    				return true;
	    			}
	    		}
    		}		
    	}
    	
    	return false;    	
    }      
	
	public char [][] getGrid()
	{
		return this.grid;
	}
	
	public void setGrid(char [][] grid)
	{
		this.grid = grid;
	}

	//Returns frequency of particular character in grid
	public int getFrequency(char character)
	{
		int frequency = 0;
		
		for (int row = 0; row < getNumRows(); row ++)
		{
			for (int column = 0; column < getNumColumns(); column ++)
			{
				if (grid[row][column] == character)
				{
					frequency ++;
				}
			}
		}
		
		return frequency;
	}
	
	public void replace (Location location, char find, char replace)
	{
		if (location.isHorizontal())
		{
			for (int x = 0; x < location.getLength(); x ++)
			{
				if (getSquare(location.getRow(),location.getColumn() + x) == find)
				{
					setSquare(location.getRow(),location.getColumn() + x, replace);
				}
				
			}
		}
		else
		{
			for (int x = 0; x < location.getLength(); x ++)
			{
				if (getSquare(location.getRow() + x,location.getColumn()) == find)
				{
					setSquare(location.getRow() + x,location.getColumn(), replace);
				}
			}
		}
	}
	
	public void replace (char find, char replace)
	{
		for (int row = 0; row < getNumRows(); row ++)
		{
			for (int column = 0; column < getNumColumns(); column ++)
			{
				if (grid[row][column] == find)
				{
					grid[row][column] = replace;
				}
			}
		}
	}
	
	public char getBlankSymbol()
	{
		return blankSymbol;
	}
	
	public char getWallSymbol()
	{
		return wallSymbol;
	}					
}