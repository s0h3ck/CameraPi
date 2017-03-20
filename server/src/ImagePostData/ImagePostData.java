package ImagePostData;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;

/**
 * @author Antoine Morin-Paulhus
 * */
public class ImagePostData {
	
	private FileItem imageItem;
	
	public FileItem getImageItem() {
		return imageItem;
	}

	public void setImageItem(FileItem imageItem) {
		this.imageItem = imageItem;
	}

	private String JSONdata;
	
	public String getJSONdata() {
		return JSONdata;
	}

	public void setJSONdata(String jSONdata) {
		JSONdata = jSONdata;
	}
	
	/**
     * Parses the image & json data from the form
     * @param formItems an html form
     */
    public void extract(List<Object> formItems){
    	Iterator<Object> iter = formItems.iterator();
    	
        // iterates over form's fields
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            if(item.getFieldName().equals("jsonData")){
            	JSONdata = item.getString();
            } else if (!item.isFormField()) {
        		// this means we found the image
            	imageItem = item;
        	}
        }
    }
	
}
