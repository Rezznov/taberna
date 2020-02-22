package taberna.room;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import taberna.Constants;
import taberna.Util;
import taberna.user.User;

public class Room {
	public static Room startRoom = new Room("Start Room", "This is the room you start in.");
	public static Set<Room> rooms = new HashSet<Room>();
	
	int id;
	String title;
	String desc;
	
	
	public Set<User> usersInRoom = new HashSet<User>();
	
	public Room(String title, String desc) {
		this.title = title;
		this.desc = desc;
	}
	public Room(File f) throws Exception { //Create Room from a File
		Scanner s = new Scanner(new FileInputStream(f));
		if(!s.nextLine().equals("[ROOM DATA]")) {
			throw new Exception("Not a Room Data File");
		}
		id = new Integer(s.nextLine());
		title = s.nextLine();
		if(s.nextLine().equals("[DESC]")) {
			desc = "";
			String nextLn = "";
			while(!((nextLn = s.nextLine()).equals("[/DESC]"))) {
				desc += nextLn;
			}
		} else {
			desc = "No Description";
		}
	}
	
	public String getLook() {
		String out = title + Util.n() + desc;
		for(User u : usersInRoom) {
			out += "\n * You can see " + u.getName() + " here.";
		}
		
		return out;
	}
	public static void loadRooms() {
		File[] roomFiles = new File(Constants.DATA_PATH + "/rooms/").listFiles();
		for(File f : roomFiles) {
			System.out.print("Room " + f.getName() + " Loading... ");
			try {
				rooms.add(new Room(f));
			} catch (Exception e) {
				System.out.print("!Failed! ");
			}
			System.out.println("Complete.");
		}
	}
	
	public int getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
