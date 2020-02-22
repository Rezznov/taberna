package taberna.user;

import java.io.File;

import org.fusesource.jansi.Ansi;

import taberna.Client;
import taberna.Constants;
import taberna.room.Room;

public class User {
	static String[] users = {"john", "rezznov", "exampleuser"};
	String name;
	Room currentRoom;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Room getRoom() {
		return currentRoom;
	}
	public void setRoom(Room room) {
		currentRoom = room;
	}
	public void moveRoom(Room room) {
		if(currentRoom != null) {
			currentRoom.usersInRoom.remove(this);
		}
		currentRoom = room;
		currentRoom.usersInRoom.add(this);
	}
	
	
	public static boolean login(String username, Client caller) {
		boolean found = false;
		File userFile = getUserFile(username);
		if(userFile == null) {
			caller.getOut().println(Ansi.ansi().fgBrightRed().bold() + "User not found!" + Ansi.ansi().fgDefault().boldOff());
			return false;
		}
		
		
		return found;
	}
	public static File getUserFile(String username) {
		File[] userFiles = new File(Constants.DATA_PATH + "/users/").listFiles();
		for(File f : userFiles) {
			if(f.getName().equals(username + ".txt")) {
				return f;
			}
		}
		return null;
	}
	
}
