package taberna;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import taberna.room.Room;

public class Server {
	//This Code is missing JANSI, just add the jar back.
	public static Set<Client> clients = new HashSet<Client>();
	static boolean running = true;
	private static ServerSocket serverSocket;
	public static Globals globals;
	public static void main(String[] args) throws IOException {
		
		System.out.println("Server is running at " + Constants.SERVER_RUN_LOCATION);
		
		// Get the location of the Data path & Config file
		Constants.DATA_PATH = args[0];
		Constants.CONFIG_FILE_LOCATION = Constants.DATA_PATH + "/config.txt";
		
		loadConfigs();
		
		//Start the socket
		System.out.println("Starting Server on " + Constants.SERVER_PORT + "...");
		serverSocket = new ServerSocket(Constants.SERVER_PORT);
		System.out.println("Sever has started.");
		
		// create the server's LUA globals
		globals = JsePlatform.standardGlobals();
		Command c = new Command("attack.lua");
		System.out.println(c.cmdName);
		System.out.println(c.cmdCall);
		Command v = new Command("say.lua");
		System.out.println(v.cmdName);
		System.out.println(v.cmdCall);
		
		//Load Rooms
		Room.loadRooms();
		System.out.println("Rooms Loaded.\nLooking for clients...");
		
		//Listen for clients
		while (running) {
			new Thread(new Client(serverSocket)).start();
		}
		//Shut it down!
		serverSocket.close();
		System.out.println("Closing server. Good bye!");
	}
	/**
	 * This command shuts down the server.
	 */
	public static void shutdown() {
		//Shut it down, *on command*~
		System.out.println("Attempting to stop server...");
		System.exit(0);
	}
	/**
	 * Read the config file
	 */
	public static void loadConfigs() {
		Scanner s = null;
		System.out.println(Constants.CONFIG_FILE_LOCATION);
		try {
			s = new Scanner(new File(Constants.CONFIG_FILE_LOCATION));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(s.next().equals("port")) {
			s.next();
			Constants.SERVER_PORT = s.nextInt();
		}
		s.close();
	}
}
