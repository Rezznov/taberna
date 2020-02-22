package taberna;

import java.io.OutputStream;
import java.io.PrintWriter;
/**
 * The UniversalPrintWriter is a child of the PrintWriter class that is only designed to make the MUD's output compatable with
 * windows.
 * @author rezznov
 *
 */
public class UniversalPrintWriter extends PrintWriter {
	
	public UniversalPrintWriter(OutputStream out, boolean autoFlush) {
		super(out, autoFlush);
	}
	
	public void println(String s) {
		super.println(s + "\r");
	}
	
}
