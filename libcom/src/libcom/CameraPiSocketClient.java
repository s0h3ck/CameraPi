package libcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CameraPiSocketClient extends CameraPiSocket implements Runnable{
	protected String host;
	protected Socket socket;
	
	public CameraPiSocketClient(String host_arg, int port_arg) {
		super(port_arg);
		host = host_arg;
	}
	
	public void run() {
		try {
			Socket socket = new Socket(host, port);
			
			// Initialize string streams for in/out communication
			out = new PrintWriter(socket.getOutputStream(), true);
			InputStreamReader instream = new InputStreamReader(socket.getInputStream());
			BufferedReader in = new BufferedReader(instream);

			String fromServer;
			System.out.println("client listening");
			
			// Eternal read loop
			while ((fromServer = in.readLine()) != null) {
				sl.MessageReceived(fromServer);
			}
			
			System.out.println("client end");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
