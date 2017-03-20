package client;

import Hardware.ControleServo;
import Hardware.Hardware;
import Hardware.PiButton;
import libcom.CameraPiSocketClient;
import libcom.SocketListener;

/**
 * The Class ClientPi.
 */
public class ClientPi {
	
	private static String host = "http://127.0.0.1";
	
	private static ControleServo servoMoteur;
	
	private static Hardware hardware;
	
	private static PiButton btn;
	
	private static int port = 8080;
	
	/** Default socket port. */
	private static int comm_port = 3077;
	
	private static CameraPiSocketClient socketClient;
	
	private static CaptureThread captureThread;
	
	/**
	 * The main method. Integrates all hardware components and communicate 
	 * with the server by socket an post request
	 *
	 * @param args the arguments in command line 
	 */
	public static void main(String[] args) {
		// First parameter: ip adress
		if (args != null && args.length > 0) {
			host = args[0];
		}
		// Second arg: libcomm port number
		if (args != null && args.length > 1) {
			comm_port = Integer.parseInt(args[1]);
		}

		// Print host
		System.out.println("host: " + host);

		try 
		{
			hardware = new Hardware();
			
			servoMoteur = hardware.getServo();
			
			btn = hardware.getBtnListener();
			
			btn.listen();	
			
			// Start capture thread
			captureThread = new CaptureThread("http://" + host, port, btn);
			(new Thread(captureThread)).start();
			

			System.out.println("Using port " + comm_port);
			
			// Socket connection to server
			System.out.println("Begin client socket");
			socketClient = new CameraPiSocketClient(host, comm_port);
			Thread thread_cli = new Thread(socketClient);
			thread_cli.start();
			
			// Initialize string streams for in/out communication
			
			SocketListener sl_client = new SocketListener() 
			{
				public void MessageReceived(String fromServer) 
				{
					System.out.println("test client received : " + fromServer);
					if (fromServer.contains("--begin--")) 
					{
						socketClient.send("--client ready--");
					}
					else if (fromServer.contains("--mv-left--"))
					{
						servoMoteur.softPwmCreate();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
						servoMoteur.goLeft();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
						servoMoteur.close();
						captureThread.setServoIsMoving();
						System.out.println("test: servo.goLeft()");
					}
					else if (fromServer.contains("--mv-right--"))
					{
						servoMoteur.softPwmCreate();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
						servoMoteur.goRight();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
						servoMoteur.close();
						captureThread.setServoIsMoving();
						System.out.println("test: servo.goRight()");
					}
				}
			};
			socketClient.addMessageListener(sl_client);	

			System.out.println("End client socket");
		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}

	}
}
