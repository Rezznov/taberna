package taberna.room;

public class Exit {
	public static final int NORTH = 0;
	public static final int SOUTH = 1;
	public static final int EAST = 2;
	public static final int WEST = 3;
	public static final int UP = 4;
	public static final int DOWN = 5;
	
	Room location;
	int direction;
	
	public Exit(int direction, Room location) {
		this.direction = direction;
		this.location = location;
	}
	
	
	
}
