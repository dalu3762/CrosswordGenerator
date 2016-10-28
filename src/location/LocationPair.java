package location;
import java.util.Objects;

public class LocationPair
{
	private Location location1;
	private Location location2;
			
	public LocationPair(Location location1, Location location2)
	{
		this.location1 = location1;
		this.location2 = location2;		
	}
	
	public Location getFirstLocation()
	{
		return location1;
	}
	
	public Location getSecondLocation()
	{
		return location2;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(location1.getRow(), location1.getColumn(), location1.getLength(), location1.getDirection(), location2.getRow(), location2.getColumn(), location2.getLength(), location2.getDirection());
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (this == object)
		{
			return true;
		}		
		else if (object instanceof LocationPair == false)
		{
			return false;
		}
		
		LocationPair locationPair = (LocationPair) object;
		
		return this.location1.equals(locationPair.location1) && this.location2.equals(locationPair.location2);		
	}	    
}