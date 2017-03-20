package libcom;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/** Socket class that manages the communication between two processes
 * @author Antoine Morin-Paulhus
 * @version 1.0
 *
 */
public class CameraPiSocket {
	protected PrintWriter out;
	protected int port;
	protected Socket socket;
	protected SocketListener sl;
	protected BufferedReader in;
	/** Constructor with the port number
	 * @param port_arg: 
	 */
	public CameraPiSocket(int port_arg){
		port = port_arg;
	}
	
	/** Get the BufferedReader
	 * @return BufferedReader
	 */
	public BufferedReader getIn() {
		return in;
	}
	

	
	/**Set the BufferedReader
	 * @param in BufferedReader
	 */
	public void setIn(BufferedReader in) {
		this.in = in;
	}
	
	/** Add a dessage listener to the Socket
	 * @param sl_arg
	 */
	public void addMessageListener(SocketListener sl_arg){
		sl = sl_arg;
	}
	
	/** Send a string into the Socket
	 * @param str
	 */
	public void send(String str){
		if(out != null){
			out.println(str);
		}
	}
}
