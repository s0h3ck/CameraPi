package libdetectionTest;

import static org.junit.Assert.*;

import javax.swing.JFrame;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.opencv.highgui.Highgui;

import libdetection.Detection;

public class DetectionTest {
	Detection detection;
	
	@Before
	public void executedBeforeEach() {
		detection = new Detection();
	}

	@Test
	public void testPrintImage() {
		detection.detectFacesAndMovements();
		detection.printImage(0);
		detection.printImage(1);
		detection.printImage(3);
		assert(detection.getImageWithRects() != null);
		assert(detection.getFrame() != null);
	}

	@Test
	public void testCreateJSONObject() {
		JSONObject JSONObj = detection.createJSONObject();
		try{
			System.out.println(JSONObj);
		}catch(Exception e){}
	}

	@Test
	public void testMovementDetected() {
		detection.detectFacesAndMovements();
		JSONObject JSONObj = detection.createJSONObject();
		detection.getDetecteMovement().setOldImageForTests("oldImage.jpg");
		detection.detectFacesAndMovements();
		JSONObject JSONObjd = detection.createJSONObject();
		
		assert(JSONObjd.toString().length() > JSONObj.toString().length());
	}
	
	@Test
	public void testMovementNotDetected() {
		detection.detectFacesAndMovements();
		JSONObject JSONObj = detection.createJSONObject();
		detection.detectFacesAndMovements();
		JSONObject JSONObjd = detection.createJSONObject();
		
		assert(JSONObjd.toString().length() == JSONObj.toString().length());

	}
	

	@Test
	public void testFacesDetected() {
		//est-ce qu'il y a une face dans image
		detection.detectFacesAndMovements();
		assert(detection.getFacesAmount() > 0);
	}
	
	@Test
	public void testIntegration(){
		detection.detectFacesAndMovements();
		JSONObject JSONObj = detection.createJSONObject();
		detection.getDetecteMovement().setOldImageForTests("oldImage.jpg");
		detection.detectFacesAndMovements();
		JSONObject JSONObjd = detection.createJSONObject();
		
		assert(JSONObjd.toString().length() > JSONObj.toString().length());
	}
	
	@Test
	public void testmovementDetected(){
		detection.detectFacesAndMovements();
		detection.getDetecteMovement().setOldImageForTests("oldImage.jpg");
		detection.detectFacesAndMovements();
		
		assert(detection.movementDetected());
	}
	
	@Test
	public void testfacesDetected(){
		detection.detectFacesAndMovements();
		assert(detection.facesDetected());
	}
	
	
	@Test
	public void testgetMovementsAmount(){
		detection.detectFacesAndMovements();
		detection.getDetecteMovement().setOldImageForTests("oldImage.jpg");
		detection.detectFacesAndMovements();
		
		assert(detection.getMovementsAmount() == 5);
	}
	
	@Test
	public void testgetFacesAmount(){
		detection.detectFacesAndMovements();
		assert(detection.getFacesAmount() == 1);
	}
	
	

}
