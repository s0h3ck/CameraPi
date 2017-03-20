package libdetectionTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;

import libdetection.DetecteMouvement;


public class DetecteMouvementTest {
	static Mat image;
	static DetecteMouvement mouv;
	@Before
	public void executedBeforeEach() {
		mouv = new DetecteMouvement();
		mouv.setOldImageForTests("oldImage.jpg");
		
		image = mouv.imageToGrayAndBlur(Highgui.imread("image.jpg"));
		
	}
	

	@Test
	public void testCompareImages() {
		int detection;
		mouv.compareImages(image);
		detection = mouv.getRects().size();
		assert(detection > 0);
		
		//fail("Not yet implemented");
	}

	@Test
	public void testGetRects() {
		mouv.compareImages(image);
		ArrayList<Rect> arr = mouv.getRects();
		assert(arr.size() > 0);
		
	}

}
