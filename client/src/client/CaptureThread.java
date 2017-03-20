package client;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;

import org.json.JSONObject;

import Hardware.PiButton;
import libcom.ImagePoster;
import libdetection.Detection;

/**
 * The Class CaptureThread.
 */
public class CaptureThread implements Runnable {
	private static int WAIT_TIME = 300;
	
	private String server_url;
	
	private int server_port;
	
	private Detection detect;
	
	/** The button listener. */
	private PiButton btn;
	
	private boolean alarmActivated;
	
	/** True if the alarm is inactive. */
	private boolean servoIsMoving;
	
	/**
	 * Instantiates a new capture thread.typically
	 *
	 * @param server_url_a the server url (ip:/camerapi)
	 * @param server_port_a the server port (typically 8080)
	 * @param buttons the button listener
	 */
	public CaptureThread(String server_url_a, int server_port_a, PiButton buttons) {		
		server_url = server_url_a;
		server_port = server_port_a;
		detect = new Detection();
		btn = buttons;
		
	}

	/**
	 * Stop the thread.
	 */
	void stopThread(){
		
	}
	
	/**
	 * Take a picture and send by post request to the server.
	 */
	private void takeAndSend(){
		
		String urlWithPort = server_url+":"+server_port + "/camerapi/UploadServlet"; 
		
		try {
			
			takePicture();
			
			JSONObject JSONObj = imageDetection();
			
			postImageToServer(urlWithPort, JSONObj);
			
		} catch (ConnectException e) {
			System.out.println("erreur connexion");
			stopThread();
		} catch (URISyntaxException e) {
			System.out.println("erreur syntaxe d'url dans : "+urlWithPort);
			stopThread();
		} catch (IOException e) {
			System.out.println("Command can't run. Perhaps not on a raspberry pi?");
		}	
	}

	/**
	 * Post image to server.
	 *
	 * @param urlWithPort String of the url and the port
	 * @param JSONObj the JSON object
	 * @throws ConnectException the connect exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	public void postImageToServer(String urlWithPort, JSONObject JSONObj) throws ConnectException, URISyntaxException {
		File file = new File("image.jpg");
		ImagePoster imp = new ImagePoster(file);			
		imp.post(urlWithPort, JSONObj.toString());
		System.out.println("Posted image");
		try {
			Thread.sleep(WAIT_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Take picture from camera on the raspberry pi.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void takePicture() throws IOException {
		PictureTaker pictaker = new PictureTaker();
		pictaker.takePicture("image.jpg");
		System.out.println("Took image");
	}

	/**
	 * Image detection.
	 *
	 * @return the JSON object
	 */
	public JSONObject imageDetection() {
		
		detect.detectFacesAndMovements();
		
		alarmManagement();
		
		//create JSON for faces, movements and alarm
		JSONObject JSONObj = detect.createJSONObject();
		JSONObj.put("alarm_activated", alarmActivated);
		return JSONObj;
	}
	

	/**
	 * Starts the thread & loop
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while(true){
			takeAndSend();
		}
	}
	
	/**
	 * Set off the alarm if movements are detected.
	 */
	public void alarmManagement() {
		alarmActivated = false;
		if (btn.getLed().systemIsArmed() && detect.movementDetected() && !servoIsMoving) {
			alarmActivated = true;
			btn.getBuzzer().ActivateBuzzer();
		}
		servoIsMoving = false;
	}
	
	/**
	 * Sets the servoIsMoving to true.
	 */
	public void setServoIsMoving() {
		this.servoIsMoving = true;
	}
	
	public boolean getServoIsMoving(){
		return servoIsMoving;
	}

}
