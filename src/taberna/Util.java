package taberna;

public class Util {
	/**
	 * Use this instead of \n when sending a new line to the user.
	 * @return Return's the user's system line separator
	 */
	public static String n() {
		return System.lineSeparator() + "\r";
	}
	
	/**
	 * Some telnet clients will send gibberish characters when the user types in special characters.
	 * This method will clean any string the user inputs of gibberish.
	 * "Gibberish" is anything outside the visible character set.
	 * @param s	the input string to clean.
	 * @return
	 */
	public static String cleanString(String s) {
		char[] chararr = s.toCharArray();
		for(char c : chararr) {
			if(c < 33 || c > 126 || c == '\'') {
				System.out.println("Char " + c + "(" + Character.toString(c).codePointAt(0) + ") removed from " + s);
				s = s.replace(Character.toString(c), "");
			}
		}
		return s;
		
	}
}
