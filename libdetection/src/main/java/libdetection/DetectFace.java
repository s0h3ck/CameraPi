package libdetection;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

import org.opencv.core.Rect;

import org.opencv.objdetect.CascadeClassifier;

/**
 * class DetectFace permet de détecter les visages d'une image contenu dans une matrice. Utilise librairie xml 
 * de lbpcascade_frontalface.xml  et les images png.
 * @author Charles-Étienne
 * @version 1.0
 *  */
public class DetectFace {

	private static int faces;
	private static Rect[] facesArr;
	private ArrayList<Rect> arrayList;
	
	public DetectFace(){
		faces = 0;
	}
	/**
	 * Détection de faces d'une Matrice contenant une image. 
	 * @param source: String contient une chaine avec l'image
	 * */
	public void detectFacesFromPicture(Mat image) {
		arrayList = new ArrayList<Rect>();
		CascadeClassifier faceDetector = new CascadeClassifier("lbpcascade_frontalface.xml");
		
		MatOfRect faceDetections = new MatOfRect();
	    faceDetector.detectMultiScale(image, faceDetections);
	    
	    faces = faceDetections.toArray().length;
	    facesArr = faceDetections.toArray();
	    for (int i = 0; i < faces; i++) {
	    	arrayList.add(facesArr[i]);
	    }
	    
	}
	/** is use for test: wil return a string of faces detected.
	 * @return position in an array
	 */
	public String getPositionArray(){
		String positions = "";
		for (Rect rect : facesArr) {
	    	positions += "position x,y: " + rect.x + ", " + rect.y + "\n";
	    }
		return positions;
	}
	
	/** will return the number of faces in the Matrice.
	 * @return faces: is the number of faces detected in the picture. 
	 */
	public int getFacesDetectNum(){
		return faces;
	}
	
	/**
	 * Use to return all faces detected in form of array.
	 * @return ArrayList<Rect> array or rectangle: Rect contains two positions x, y return the position.
	 */
	public ArrayList<Rect> getArrayListOfPosition(){
		
		return arrayList;
	}
}
