package taberna;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import taberna.room.Room;

public class Command {
	
	public static ArrayList<Command> commands = new ArrayList<Command>();
	
	String cmdName;
	String cmdCall;
	LuaValue scriptFile;
	Globals cmdGlobals;
	
	public static void initCommands() {
		
	}
	
	public static void parse(Client caller, String input) {
		
		String[] arrayInput = input.split(" ");
		
		if (arrayInput[0].equals("quit")) {
			caller.out.print("Cya!");
			caller.connected = false;
		}
		if (arrayInput[0].equals("shutdown")) {
			caller.out.println("Attempting to stop server...");
			Server.shutdown();
		}
		
		if (input.startsWith("say")) {
			for (Client c : Server.clients) {
				c.out.println(caller.user.getName() + ": " + input.substring(4));
			}
		}
		
		if(arrayInput[0].equals("tell") || arrayInput[0].equals("whisper")) {
			int told = 0;
			String message = "";
			for(int i = 2; i < arrayInput.length; i++) {
				message += arrayInput[i] + " ";
			}
			for(Client c : Server.clients) {
				if(c.user.getName().equals(arrayInput[1])) {
					told++;
					c.out.println(Ansi.ansi().fgBrightBlack() + caller.user.getName() + " whispers to you: " + message + Ansi.ansi().fgDefault());
				}
			}
			if(told == 0) {
				caller.out.println(Ansi.ansi().fgRed() + "Sorry, we couldn't find " + arrayInput[1] + Ansi.ansi().fgDefault());
			}
		}
		
		if (arrayInput[0].equals("me")) {
			caller.out.println(caller.user.getName());
		}
		if (arrayInput[0].equals("look")) {
			caller.out.println(caller.user.getRoom().getLook());
		}
		if (arrayInput[0].equals("who")) {
			caller.out.println("Users online: ");
			for (Client c : Server.clients) {
				caller.out.println(" * " + c.user.getName());
			}
		}
		if (arrayInput[0].equals("rooms")) {
			caller.out.println("ID\t║TITLE\t\t║");
			for (Room r : Room.rooms) {
				caller.out.println(r.getId() + "\t║" + r.getTitle() + "\t║");
			}
		}
		if (arrayInput[0].equals("ping")) {
			caller.out.println("pong!");
		}
		if (arrayInput[0].equals("clear")) {
			caller.out.println(Ansi.ansi().eraseScreen());
		}
		if (arrayInput[0].equals("motd")) {
			caller.out.println(getMOTD());
		}
		if (arrayInput[0].equals("test")) {
			caller.out.println(Ansi.ansi().fgBrightBlack().bgRed() + "██▓▒░☺" + Util.n() + "#*" + Ansi.ansi().fgDefault().bgDefault());
		}
		if (arrayInput[0].equals("attack")) {
			commands.get(0).cmdGlobals.get("onCall").call(caller.luaClient);
		}
	}

	public static String getMOTD() {
		String motd = Ansi.ansi().bold().fgBrightBlue() + "═Message═of═the═day═════════════════════"
				+ Ansi.ansi().boldOff().fgDefault() + Util.n();
		try {
			Scanner s = new Scanner(new FileInputStream(Constants.DATA_PATH + "/MOTD.txt"));
			while (s.hasNext()) {
				motd += s.nextLine() + Util.n();
			}
		} catch (FileNotFoundException e) {
			motd += Ansi.ansi().fgBrightRed() + "!ERROR! MOTD.txt could not be found." + Ansi.ansi().fgDefault()
					+ Util.n();
		}
		motd += Ansi.ansi().bold().fgBrightBlue() + "════════════════════════════════════════"
				+ Ansi.ansi().boldOff().fgDefault();
		return motd;
	}
	
	public Command(String fileLocation) {
		// Make the Globals
		cmdGlobals = JsePlatform.standardGlobals();
		
		// Load and call the file
		scriptFile = cmdGlobals.loadfile(Constants.DATA_PATH + "/scripts/commands/" + fileLocation);
		scriptFile.call();
		
		// Add Atributes
		cmdName = cmdGlobals.get("cmdName").tojstring();
		cmdCall = cmdGlobals.get("cmdCall").tojstring();
		
		//if(!cmdGlobals.get("onCall").isnil())
		//	cmdGlobals.get("onCall").call();
		
		commands.add(this);
	}

}
