package location;
import java.util.Objects;

enum Direction
{
	H, V; //Horizontal, vertical		
}

//This class defines the starting coordinates, direction, and length of a grid entry 	
public class Location 
{
	private int row; //topmost row index 0
	private int column; //leftmost column index 0)
	private int length; //how many letters are in the grid entry
	private Direction direction; //what way the grid entry goes; horizontal (H) or vertical (V))
	
	public Location()
	{
				
	}
			
	public Location(int row, int column, int length, String direction)	
	{		
		this.row = row;
		this.column = column;
		this.length = length;		
		this.direction = direction.equals("H") ? Direction.H : Direction.V;
	}
		
	public int getRow()
	{
		return row;
	}
	
	public int getColumn()
	{
		return column;
	}
	
	public int getLength()
	{
		return length;
	}
	
	public String getDirection()  
	{
		return this.direction == Direction.H ? "H" : "V";
	}
	
	public void setRow(int row)
	{
		this.row = row;
	}

	public void setColumn(int column)
	{
		this.column = column;
	}
	
	public void setLength(int length)
	{
		this.length = length;
	}
	
	public void setDirection(String direction)
	{
		this.direction = direction.equals("H") ? Direction.H: Direction.V;
	}
	
	public boolean isHorizontal()
	{
		return getDirection().equals("H") ? true : false;
	}
	
	public boolean isVertical()
	{
		return getDirection().equals("V") ? true : false;
	}
	
	public boolean isEqual(Location otherLocation)
	{
		return row == otherLocation.getRow() && column == otherLocation.getColumn() && length == otherLocation.getLength() 
				&& getDirection().equals(otherLocation.getDirection()) ? true: false;			
	}
		
	public void print()
	{
		System.out.println("Row: " + getRow());
		System.out.println("Column: " + getColumn());
		System.out.println("Length: " + getLength());
		System.out.println("Direction: " + getDirection());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(this.getRow(), this.getColumn(), this.getLength(), this.getDirection());
	} 
	
	@Override	
	public boolean equals(Object object)
	{
		if (this == object)
		{
			return true;
		}
		else if (object instanceof Location == false)
		{
			return false;
		}
		
		Location location = (Location) object;
		
		return this.isEqual(location);
	}     
}