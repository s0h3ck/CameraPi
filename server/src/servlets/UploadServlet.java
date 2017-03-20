package servlets;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import ImagePostData.ImagePostData;
import libdb.Image;
import libdb.SQLiteJDBC;
import libdb.TableImage;

/**
 * Manages picture upload & saving to the database & filesystem.
 * @author Johany Camirand
 * @author Antoine Morin-Paulhus
 * 
 * To test under linux when camera is not available
 * 
 * curl -F "image=@test.jpg" -F "jsonData={}" http://localhost:8080/cameraPi_server/UploadServlet
 *  
 * windows: do the same, but with git cmd
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    private static final String UPLOAD_DIRECTORY = "upload";
    private static final int THRESHOLD_SIZE     = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 3; // 3MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 3; // 3MB
 
    private DiskFileItemFactory factory;
    private String uploadPath;
	private SQLiteJDBC db;
    private TableImage tableImage;
    
    private boolean isTesting = false;
    
    public UploadServlet(){
    	// configures upload settings
		factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        
        uploadPath = "./photos";
    	
        // creates the directory if it does not exist
        CreateUploadDirectory(uploadPath);
    	
    	// Init DB connection
 		db = new SQLiteJDBC();
 		// Create table if it does not exist yet
 	    tableImage = new TableImage();
 	    db.createTable(tableImage);
 	    
 	    // Connect to database
 	    db.connect();
 		
    	System.out.println("init upload servlet");
    	initializeUploadPath();
    }
    
    public SQLiteJDBC getDB(){
    	return db;
    }
    
    /**
     * For the picture upload path
     * @return the current picture upload path
     */
    public String getUploadPath() {
		return uploadPath;
	}

	/**
	 * Change picture upload path
	 * @param uploadPath the new path
	 */
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	/**
	 * Constructs the directory path to store upload file
	 */
	public void initializeUploadPath(){
		try{
			uploadPath = getServletContext().getRealPath("/photos");
        } catch(Exception e){
        	// Do nothing, path stays "./photos"
        }	
	}
	
	/**
     * handles file upload via HTTP POST method
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
    	ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);
        
        initializeUploadPath();
        
        // parses the request's content to extract file data
        List<Object> formItems = null;
        
		try {
			if(!isTesting()){
				formItems = upload.parseRequest(request);
			}
			
			ImagePostData impd = new ImagePostData();
			
			if(!isTesting()){
				impd.extract(formItems);
			}
			
			FileItem imageItem = impd.getImageItem();
			String JSONstr = impd.getJSONdata();
			
			if(JSONstr == null || JSONstr.isEmpty()){
				JSONstr = "{}";
			}
			
			saveImage(imageItem, JSONstr);
			
			try {
				response.getWriter().write("File upload success");
			} catch (IOException e) {
				System.out.println("http write fail");
			}
		} catch (FileUploadException e) {
			// We can't do anything about it
			// Hope next file works
			try {
				response.getWriter().write("File upload failed");
			} catch (IOException e1) {
				System.out.println("http write fail");
			}
		}
    }

    
    
    /**
     * Saves image to the DB and filesystem
     * @param imageItem the image form item
     * @param JSONstr the JSON string containing opencv detection information
     * @return error/message string
     */
    public String saveImage(FileItem imageItem, String JSONstr){
    	String resp = "";
    	// processes only fields that are not form fields
        
    	Date fileDate = GenerateFileDate();
    	String fileName = FormatDateAsFileName(fileDate);
    	String filePath = generateUploadFilePath(uploadPath, fileName);
		File storeFile = new File(filePath);
		
		// Insert data into DB
		db.insertImageInTable(new Image(fileName, fileDate, JSONstr));
		
		try{
    		// saves the file on disk
            imageItem.write(storeFile);
        } catch (Exception ex) {
            resp += "There was an error: " + ex.getMessage();
        }
        
        return resp;
    }
    
    /**
     * @return a date used to eventually create a timestamp
     */
    public java.util.Date GenerateFileDate() 
	{
		java.util.Date date = new java.util.Date();
		return date;
	}

    /**
     * Converts a date to a filename
     * @param date the date to use
     * @return the formatted filename
     */
    public String FormatDateAsFileName(java.util.Date date) 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_S");
    	String fileName = formatter.format(date) + ".jpg";
    	return fileName;
	}
    
	/**
	 * Utility function that combines uploadPath and filename
	 * @param uploadPath
	 * @param fileName
	 * @return the path: path/to/upload_folder/filename.extension
	 */
	public String generateUploadFilePath(String uploadPath, String fileName) 
	{
		// Pour mettre la date dans le nom de fichier
		String filePath = uploadPath + File.separator + fileName;
		return filePath;
	}

	/**
	 * Create upload directory if it does not already exist
	 * @param uploadPath the given folder (may include filename)
	 * @return
	 */
	public File CreateUploadDirectory(String uploadPath) {
		File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) 
        {
            uploadDir.mkdir();
        }
        return uploadDir;
	}

	public boolean isTesting() {
		return isTesting;
	}

	public void setTesting(boolean isTesting) {
		this.isTesting = isTesting;
	}
}