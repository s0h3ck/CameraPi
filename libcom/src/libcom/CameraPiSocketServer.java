package libcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CameraPiSocketServer extends CameraPiSocket implements Runnable{
	private ServerSocket serverSocket = null;
	
	public CameraPiSocketServer(int port_arg) {
		super(port_arg);
	}

	/*
	 * Thread starter for the server
	 */
	public void run() {
		try {
			// Connection
			System.out.println("Using port: " + port);
			serverSocket = new ServerSocket(port);

			while (true) {
				// Accepter une connection
				// (Mettre dans une boucle avec le reste pour accepter plusieurs
				// clients)
				Socket clientSocket = serverSocket.accept();

				// Obtient le flux de lecture
				out = new PrintWriter(clientSocket.getOutputStream(), true);

				// Obtient le flux d'écriture
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				System.out.println("envoi message");
				out.println("--begin--");
				
				String fromServer;
				
				while ((fromServer = in.readLine()) != null) {
					sl.MessageReceived(fromServer);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
