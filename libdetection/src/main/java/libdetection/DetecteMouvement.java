package libdetection;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

/**
 * @author pelm2528
 * @version 1.0
 * @since December 3rd, 2016
 * Allows the user to detect movements using a new image that is constantly overwritten and an older one that is kept in the memory in an array.
 */
public class DetecteMouvement {
	
	private static final int MIN_BOX_SIZE_FILTER = 10;
	
	private Mat oldMat;
	private Mat currentMat;
	private Mat matriceDiff;
	private ArrayList<Rect> coordsArr;
	private boolean isTesting;
	
	/**
	 * Initialize the matrices and the arrayList. Also loads the native library needed for movement detection.
	 */
	public DetecteMouvement(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		oldMat = new Mat();
		currentMat = new Mat();
		matriceDiff = new Mat();
		isTesting = false;
	}
	
	/**
	 * Detect if the two images are the same size, if not, resize the larger one to the size of the smaller one.
	 */
	private void resizeIfNeeded() {
		if (oldMat.width() != currentMat.width() || oldMat.height() != currentMat.height()) {
			int width = oldMat.width() < currentMat.width()? oldMat.width() : currentMat.width();
			int height = oldMat.height() < currentMat.height()? oldMat.height() : currentMat.height();
			Size size = new Size(width, height);
			
			Imgproc.resize(oldMat, oldMat, size);
			Imgproc.resize(currentMat, currentMat, size);
		}
	}
	
	/**
	 * Detect the outline of the movements (of the treshold matrix).
	 * @param outmat The treshold matrix, in grayscale
	 * @return An array list containing all of the rectangles of the movements.
	 */
	private ArrayList<Rect> detection_contours(Mat outmat) {
        Mat v = new Mat();
        Mat vv = outmat.clone();
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(vv, contours, v, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
 
        double maxArea = 100;
        int maxAreaIdx = -1;
        Rect r = null;
        ArrayList<Rect> rect_array = new ArrayList<Rect>();
 
        for (int idx = 0; idx < contours.size(); idx++) { Mat contour = contours.get(idx); double contourarea = Imgproc.contourArea(contour); if (contourarea > maxArea) {
                maxAreaIdx = idx;
                r = Imgproc.boundingRect(contours.get(maxAreaIdx));
                rect_array.add(r);
            }
        }
        v.release();
        return rect_array;
 
    }
	
	/**
	 * Chooses which movement rectangles we keep and put them in another array that contains only the "valid" movements.
	 * @param diff Inital coords array containing valid and invalid movements.
	 */
	private void creationCoordsArray(Mat diff) {
		coordsArr = new ArrayList<Rect>();
		ArrayList<Rect> mvmCoordsArray = detection_contours(diff);
		for (int i = 0; i < mvmCoordsArray.size(); i++) {
			Rect rect = mvmCoordsArray.get(i);
			if (!(rect.height < MIN_BOX_SIZE_FILTER || rect.width < MIN_BOX_SIZE_FILTER)) { //enlèves les mouvements de moins de 10 pixels - incertitude
				coordsArr.add(rect); 
			}
		}
	}
	
	/**
	 * Compares the old image and the new image.
	 * @param newMat The new image in a matrix.
	 */
	public void compareImages(Mat newMat) {
		this.currentMat = newMat;
		if (oldMat.empty()) { //seulement la première fois car il n'y aura pas de données dans oldMat
			oldMat = currentMat.clone();
		}
		
		resizeIfNeeded();
//		System.out.println("OldMat : H: " + oldMat.size().height + " W: " + oldMat.size().width);
//		System.out.println("CurrentMat : H: " + currentMat.size().height + " W: " + currentMat.size().width);
//		System.out.println(oldMat.channels() + " " + currentMat.channels());
		Core.subtract(oldMat, currentMat, matriceDiff);
		
		Mat diffWithThreshold = matriceDiff.clone();	
		Imgproc.adaptiveThreshold(diffWithThreshold, diffWithThreshold, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY_INV, 3, 3);
		
		creationCoordsArray(diffWithThreshold);
		
		oldMat = currentMat.clone(); //image courante devient l'ancienne à comparer prochaine fois
	}
	
	/**
	 * Getter for the array list containing the valid movements rectangles
	 * @return The array list.
	 */
	public ArrayList<Rect> getRects() {
		return coordsArr;
	}
	
	//FUNCTIONS USED FOR TESTS ONLY
	public void setOldImageForTests(String image) {
		isTesting = true;
		oldMat = imageToGrayAndBlur(Highgui.imread(image));
	}
	
	public Mat imageToGrayAndBlur(Mat mat) {
		Mat matGray = new Mat(mat.size(), CvType.CV_8UC1);
		Imgproc.cvtColor(mat, matGray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(matGray, matGray, new Size(3, 3), 0);
		return matGray;
	}
	/////////////////
}
