package taberna;

import java.io.File;

public class Constants {
	public static final File SERVER_RUN_LOCATION = new File(Server.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	
	public static String DATA_PATH;
	public static String CONFIG_FILE_LOCATION;
	
	public static int SERVER_PORT;
}
