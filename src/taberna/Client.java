package taberna;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.fusesource.jansi.Ansi;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import taberna.room.Room;
import taberna.user.User;

public class Client implements Runnable {
	Socket s;
	UniversalPrintWriter out;
	BufferedReader in;
	User user = new User();
	LuaValue luaClient;
	
	boolean connected = true;
	
	public Client(ServerSocket connection) {
		try {
			s = connection.accept();
			out = new UniversalPrintWriter(s.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Server.clients.add(this);
		System.out.println("New Client @ " + s.getLocalSocketAddress().toString());
	}

	/**
	 * "The client loop"
	 * This method will continually loop while the client is connected to get the user's input.
	 */
	public void run() {
		try {
			onJoin();
			String inputLine = "";
			
			while (connected) {
				inputLine = in.readLine();
				
				Command.parse(this, inputLine);
				
				System.out.println(this.s.getLocalAddress() + ": " + inputLine);
			}
			in.close();
			out.close();
			s.close();
		} catch (Exception e) {
			
		}
	}
	/**
	 * Things to do when the Client connects to the server.
	 * Creates a User class & attaches a LUAClient object to the java client
	 * @throws IOException
	 */
	public void onJoin() throws IOException {
		out.println(Ansi.ansi().fgMagenta().bold() + "Welcome!" + Util.n() + "Please Enter a username:"
					+ Ansi.ansi().fgDefault().boldOff());
		
		System.out.println("Loading Client file...");
		LuaValue chunk = Server.globals.loadfile(Constants.DATA_PATH + "/scripts/client.lua");
		chunk.call();
		System.out.println("Coercing Object...");
		LuaValue jc = CoerceJavaToLua.coerce(this);
		System.out.println("Loading Constructor...");
		LuaValue construct = Server.globals.get("newClient");
		System.out.println("Calling Constructor...");
		luaClient = construct.call(jc);
		System.out.println("Done!");
		
		user.setName(Util.cleanString(in.readLine()));
		out.println(User.login(user.getName(), this));
		user.moveRoom(Room.startRoom);
		out.println(Command.getMOTD());
		out.println("Welcome, " + user.getName());
		
		//LuaValue say = Server.globals.get("sayHi");
		//say.call(luaClient);
	}
	
	/**
	 * Get the client's Universal Print Writer
	 * @return the client's output
	 */
	public UniversalPrintWriter getOut() {
		return out;
	}
	/**
	 * Get the client's User -- their character
	 * @return the client's output
	 */
	public User getUser() {
		return user;
	}
	
	
}
