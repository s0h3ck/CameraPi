package libdb;

import java.util.Date;

/**
 * 
 * @author came
 */
public class Image {

    private String fileName;
    private Date receiveDate;
    private String imageJSON;
    
    /**
     * 
     * @param name The filename of the image
     * @param receiveDate The local date stored on the server
     * @param imageJSON The details of the JSON string (just a string)
     */
    public Image(String name, Date receiveDate, String imageJSON)
    {        
        this.setImageName(name);
        this.setReceiveDate(receiveDate);
        this.setJSONData(imageJSON);
    }

    /**
     * Get the filename of an image
     * @return The filename
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set the filename of an image
     * @param name The filename of the image
     */
    public void setImageName(String name) {
        this.fileName = name;
    }

    /**
     * Get the date where the image was received on the server
     * @return The received date
     */
    public Date getReceiveDate() {
        return this.receiveDate;
    }

    /**
     * Set the date where the image was received on the server
     * @param receiveDate The received date of the image stored on server 
     */
    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }
    
    /**
     * Set additional properties about the image
     * The JSON is not verified in the database.
     * Considered the JSON as a string only.
     * @param imageJSON The image data JSON in string
     */
    public void setJSONData(String imageJSON) {
        this.imageJSON = imageJSON;
    }
    
    /**
     * Get the additional properties about the image
     * The return JSON is not verified in the database.
     * Considered the JSON as a string only.
     * @return The image data JSON in string
     */
    public String getJSONData() {
        return this.imageJSON;
    }
}
