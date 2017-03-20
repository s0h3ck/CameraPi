package test;

import Hardware.ControleServo;
import Hardware.Hardware;
import Hardware.PiButton;
import client.CaptureThread;

public class testIntegrationCaptureThread {
	
	private static int port = 8080;
	private static int comm_port = 3077;
	private static String host = "http://127.0.0.1";
	private static CaptureThread captureThread;
	private static ControleServo servoMoteur;
	private static Hardware hardware;
	private static PiButton btn;
	
	public static void main(String[] args) {
		// First parameter: ip adress
		if (args != null && args.length > 0) {
			host = args[0];
		}
		// Second arg: libcomm port number
		if (args != null && args.length > 1) {
			comm_port = Integer.parseInt(args[1]);
		}
		
		hardware = new Hardware();
		
		servoMoteur = hardware.getServo();
		
		btn = hardware.getBtnListener();
		
		btn.listen();	
		
		// Start capture thread
		captureThread = new CaptureThread("http://" + host, port, btn);
		(new Thread(captureThread)).start();
		
	}

}
