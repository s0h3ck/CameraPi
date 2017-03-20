package libdetectionTest;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;

import libdetection.DetectFace;

//import org.junit.Before;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DetectFaceTests {
	
	Mat image;
	DetectFace testDetect;
	@Before
	public void executedBeforeEach()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		image = Highgui.imread("lena.png");
		testDetect = new DetectFace();
	}

	@Test
	public void testDetectFaceImage(){
		testDetect.detectFacesFromPicture(image);
		assert(testDetect.getFacesDetectNum()>0);
	}
	@Test
	public void testReturnPositionArray(){
		
		ArrayList<Rect> arr = new ArrayList<Rect>();
		testDetect.detectFacesFromPicture(image);
		arr = testDetect.getArrayListOfPosition();
		Rect rect = new Rect();
				rect = arr.get(0);
		System.out.println(testDetect.getPositionArray());
		
		assert((rect.x == 207) && (rect.y == 200));
	}



}
