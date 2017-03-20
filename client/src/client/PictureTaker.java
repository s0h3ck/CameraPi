package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Wrapper around raspberry-pi native command line tool raspistill
 * Watches the output of the command and logs errors to stdout if any.
 */
public class PictureTaker {
	
	private static String command = "/opt/vc/bin/raspistill -hf -vf -w 170 -h 170 -t 700 -o";
	
	private Process process;
	
	private BufferedReader input;
	
	private String linetoPrint;
	
	/**
	 * Take a picture and save it at a given filename.
	 * Listens to error messages made by native function.
	 *
	 * @param filename the filename
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void takePicture(String filename) throws IOException {
		executeCommand(filename);
		input = createBufferedReader();
		printData();
	}
	
	/**
	 * Execute native command.
	 *
	 * @param filename the filename
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void executeCommand(String filename) throws IOException {
		process = getRuntime().exec(command + " " + filename);
	}
	
	/**
	 * Gets the runtime.
	 *
	 * @return the runtime
	 */
	public Runtime getRuntime() {
		return Runtime.getRuntime();
	}
	
	/**
	 * Creates the buffered reader.
	 *
	 * @return the buffered reader
	 */
	public BufferedReader createBufferedReader() {
		return new BufferedReader(readInputStream());
	}
	
	/**
	 * Read input stream of the process.
	 *
	 * @return the input stream reader
	 */
	public InputStreamReader readInputStream() {
		return new InputStreamReader(process.getInputStream());
	}
	
	/**
	 * Prints the data to stdout.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void printData() throws IOException {
		while (!lineToPrintIsNull()) {
			System.out.println(linetoPrint);
		}
		input.close();
	}
	
	/**
	 * A utility function to verify if the line is null
	 *
	 * @return true, if line is null
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean lineToPrintIsNull() throws IOException {
		return readLine() == null;
	}
	
	/**
	 * Utility function to read a line from the input.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String readLine() throws IOException {
		linetoPrint = input.readLine();
		return linetoPrint;
	}
}
