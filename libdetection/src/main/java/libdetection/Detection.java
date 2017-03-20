package libdetection;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 * @author pelm2528
 * @version 1.0
 * @since December 3rd, 2016
 * Detects both the movements and the faces. Sends a JSON Object for the client to use.
 */
public class Detection {

	private final static String CURRENT_IMAGE_NAME = "image.jpg";
	private final static int FACES = 0;
	private final static int MOVEMENTS = 1;

	private DetecteMouvement detecteMouvement;
	private DetectFace detectFace;
	private ArrayList<Rect> facesArr;
	private ArrayList<Rect> mouvementArr;
	private Mat coloredMat;
	private BufferedImage imageWithRects;
	private JFrame frame;
	
	/**
	 * Creates the Movement detection object and the detect face objects. 
	 */
	public Detection() {
		detecteMouvement = new DetecteMouvement();
		detectFace = new DetectFace();
		facesArr = new ArrayList<Rect>();
		mouvementArr = new ArrayList<Rect>();
		
	}
	
	/**
	 * Creates the matrix of the current image and pass it to the movement detection and the face detection objects.
	 * Detect everything and creates the two arrays of Rects.
	 */
	public void detectFacesAndMovements() {

		coloredMat = loadImagesToMatrix();
		Mat grayMat = imageToGrayAndBlur(coloredMat);
		
		detectFace.detectFacesFromPicture(coloredMat);
		detecteMouvement.compareImages(grayMat);
		
		facesArr = detectFace.getArrayListOfPosition();
		mouvementArr = detecteMouvement.getRects();
	}
	
	/**
	 * Prints the image in a JFrame with Rectangles around either the faces or the movements.
	 * @param rectanglesLocation Decides whether the rectangles will be shown around the faces or the movements.
	 */
	public void printImage(int rectanglesLocation) {
		ArrayList<Rect> arr = rectanglesLocation == MOVEMENTS? mouvementArr : facesArr;
		if (arr == null) {
			System.out.println("No image to print.");
			return;
		}
		for (int i = 0; i < arr.size(); i++) {
			Rect rect = arr.get(i);
			if (!(rect.height < 10 || rect.width < 10)) { //enlèves les mouvements de moins de 10 pixels - incertitude
				Core.rectangle(coloredMat, rect.br(), rect.tl(), new Scalar(0, 255, 0), 1);
			}
		}

		imageWithRects = convertMatToImage(coloredMat);
		createFrameToPrint(imageWithRects);
	}
	
	/**
	 * Get the Image with rectangles, either around the faces or the movements.
	 * @return The image.
	 */
	public BufferedImage getImageWithRects() {
		return imageWithRects;
	}
	
	/**
	 * Converts a matrix to image.
	 * @param mat The matrix to be converted.
	 * @return A bufferedImage.
	 */
	private BufferedImage convertMatToImage(Mat mat){
	    MatOfByte bytemat = new MatOfByte();
	    Highgui.imencode(".png", mat, bytemat);
	    byte[] bytes = bytemat.toArray();
	    InputStream in = new ByteArrayInputStream(bytes);
	    BufferedImage img = null;
	    try {
	        img = ImageIO.read(in);
	    } catch (IOException e) {
	        System.out.println("Erreur dans la conversion de matrice à image.");
	    }
	    return img;
	}
	
	/**
	 * Creates a JFrame to print an image on the screen.
	 * @param buff The image to print.
	 */
	private void createFrameToPrint(BufferedImage buff) {
		ImageIcon imgIco = new ImageIcon(buff);
		
		frame = new JFrame("Server");
		JLabel lab = new JLabel();
		
		lab.setIcon(imgIco);
		frame.add(lab);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Get the frame in which the image was printed
	 * @return The frame in which the image is printed.
	 */
	public JFrame getFrame() {
		return frame;
	}
	
	/**
	 * Creates the JSONObject, containing the count of faces, the rectangles of the faces, 
	 * the count of movements and the rectangles of the movements.
	 * @return The JSONObject created.
	 */
	public JSONObject createJSONObject() {
		JSONObject detectObject = new JSONObject();
		JSONArray detectFaceArray = new JSONArray();
		JSONArray detectMoveArray = new JSONArray();
		JSONObject currentData;
		
		detectObject.put("faces_count", facesArr.size());
		for (int i = 0; i < facesArr.size(); i++) {
			currentData = new JSONObject();
			
			currentData.put("x", facesArr.get(i).x);
			currentData.put("y", facesArr.get(i).y);
			currentData.put("w", facesArr.get(i).width);
			currentData.put("h", facesArr.get(i).height);
			
			detectFaceArray.put(currentData);
		}
		detectObject.put("faces", detectFaceArray);
		
		detectObject.put("movements_count", mouvementArr.size());
		for (int i = 0; i < mouvementArr.size(); i++) {
			currentData = new JSONObject();
			
			currentData.put("x", mouvementArr.get(i).x);
			currentData.put("y", mouvementArr.get(i).y);
			currentData.put("w", mouvementArr.get(i).width);
			currentData.put("h", mouvementArr.get(i).height);
			
			detectMoveArray.put(currentData);
		}
		detectObject.put("movements", detectMoveArray);
		
		return detectObject;
	}
	
	/**
	 * Check if one movement or more was detected.
	 * @return True or false depending if there were movements detected.
	 */
	public boolean movementDetected() {
		return mouvementArr.size() > 0;
	}
	
	/**
	 * Check if one face or more was detected.
	 * @return True or false depending if there were faces detected.
	 */
	public boolean facesDetected() {
		return facesArr.size() > 0;
	}
	
	/**
	 * Checks how many movements were detected.
	 * @return The number of movements that were detected.
	 */
	public int getMovementsAmount() {
		return mouvementArr.size();
	}
	
	/**
	 * Checks how many faces were detected.
	 * @return The number of faces that were detected.
	 */
	public int getFacesAmount() {
		return facesArr.size();
	}
	
	/**
	 * Loads an image from the files into a matrix.
	 * @return
	 */
	private Mat loadImagesToMatrix(){
		return Highgui.imread(CURRENT_IMAGE_NAME);
	}
	
	/**
	 * Recolor an image in the matrix to gray and blur it.
	 * @param mat The matrix to transform.
	 * @return The matrix transformed.
	 */
	private Mat imageToGrayAndBlur(Mat mat) {
		Mat matGray = new Mat(mat.size(), CvType.CV_8UC1);
		Imgproc.cvtColor(mat, matGray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(matGray, matGray, new Size(3, 3), 0);
		return matGray;
	}
	
	/**
	 * Get the detecteMouvement object.
	 * @return The detecteMouvement object.
	 */
	public DetecteMouvement getDetecteMovement() {
		return detecteMouvement;
	}
	
	/**
	 * Main used for an integration test.
	 * @param args Never used.
	 */
/*	public static void main(String[] args) {
		Detection detect = new Detection();

		System.out.println("Test with two identical images.");
		detect.detectFacesAndMovements();
//		detect.printImage(Detection.MOVEMENTS); 
//		detect.printImage(Detection.FACES);
		JSONObject JSONObj = detect.createJSONObject();
		System.out.println(JSONObj + "\n");
		
		System.out.println("Test with two different images.");
		detect.getDetecteMovement().setOldImageForTests("oldImage.jpg");
		detect.detectFacesAndMovements();
//		detect.printImage(Detection.MOVEMENTS); 
//		detect.printImage(Detection.FACES);
		JSONObj = detect.createJSONObject();
		System.out.println(JSONObj);
	}*/
}
