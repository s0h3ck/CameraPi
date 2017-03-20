package libdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The class to manage the CameraPI database
 * @author came
 */
public class SQLiteJDBC {
    
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";  
    private String database_file = null;
    
    private static Connection connexionDatabase;
    private static ResultSet resultDatabase;
    private static String sqlRequest;
    private static boolean connected = false;

    /**
     * Setup the driver for the database server
     * The database is stored in the home folder of camerapi.
     * By default, the database name is camerapi.db
     */
	public SQLiteJDBC()
    {
        connexionDatabase = null;
        resultDatabase = null;
        sqlRequest = "";
        setDatabaseFile("camerapi.db");
    }
    
	/**
	 * Connect to the database camerapi.db.
	 * If the database doesn't exist, it will be created.
	 */
    public void connect()
    {
        try {
            tryToConnect();
            System.out.println("Opened database successfully");
            connected = true;
        } catch (Exception e ) {
            logError(e);
        }
    }

    /**
     * Try to connect with the SQLite JDBC_DRIVER and the path
     * specified by default or the path configured manually
     * @throws Exception The driver or database is wrong.
     */
    private void tryToConnect() throws Exception {
        Class.forName(JDBC_DRIVER);
        connexionDatabase = DriverManager.getConnection(getDatabaseFile());
    }
    
    /**
     * Configured the database file path.
     * @return The path to the database.
     */
    public String getDatabaseFile() {
        return this.database_file;
    }

    /**
     * Set the path of the database file with a different name
     * Often used for testing purpose
     * @param database_file The name of the database file
     */
    public void setDatabaseFile(String database_file) {
        String homeFolder = System.getProperty("user.home");
        this.database_file = "jdbc:sqlite:"+ homeFolder + "/" + database_file;
    }
    
    /**
     * Create an image table in the database.
     * @param table The image table
     */
    public void createTable(TableImage table)
    {
        try {
            tryToCreateTable(table);
            System.out.println("Table created successfully");
        } catch (Exception e ) {
            logError(e);
        }
    }

    /**
     * Try to create an image table in the database
     * @param table The image table to add as a query
     * @throws SQLException The SQL create table query is wrong or impossible to execute.
     */
    private void tryToCreateTable(TableImage table) throws SQLException
    {
        prepareTheSQLCreateTableRequest(table);
        PreparedStatement preparedStatement = connexionDatabase.prepareStatement(sqlRequest);
        preparedStatement.executeUpdate();
    }

    /**
     * Get the SQL query request to create an image table.
     * @param table The table image containing the SQL request in his instance.
     */
    private void prepareTheSQLCreateTableRequest(TableImage table)
    {
        sqlRequest = table.getSQLCreateTable();
    }
    
    /**
     * Insert an image in the image table in the database
     * @param image The image to be inserted
     */
    public void insertImageInTable(Image image)
    {
        try {
            tryToInsert(image);
        } catch (Exception e ) {
            logError(e);
        }
    }

    /**
     * Try to execute the INSERT query to insert an image in the image table
     * @param image The image to be inserted
     * @throws Exception The SQL query could not be executed. 
     *                   It could happens if there is no table.
     */
    private void tryToInsert(Image image) throws Exception {
        sqlRequest = "INSERT INTO IMAGE (NAME,TIME,JSON) VALUES(?,?,?);";
        PreparedStatement preparedStatement = connexionDatabase.prepareStatement(sqlRequest);
        preparedStatement.setString(1, image.getFileName());
        preparedStatement.setDate(2, new java.sql.Date(image.getReceiveDate().getTime()));
        preparedStatement.setString(3, image.getJSONData());
        preparedStatement.execute();
        sendCloseResult();
    }
    
    /**
     * Get all images from the database.
     * @return A map of id and Image containing all images or null if there is any.
     */
    public Map<Integer, Image> selectAll()
    {
        Map<Integer, Image> resultImages = null;
        resultImages = new HashMap<Integer, Image>();
        try {
            tryToSelectAll();
            resultImages = sendPrepareResult();
            sendCloseResult();
        } catch (Exception e ) {
            logError(e);
        }
        return resultImages;  
    }
    
    /**
     * Try to execute the select all image query on the database
     * @throws SQLException The SQLException is throw if there is no table or connection established
     */
    private void tryToSelectAll() throws SQLException {
        sqlRequest = "SELECT * FROM IMAGE;";
        PreparedStatement preparedStatement = connexionDatabase.prepareStatement(sqlRequest);
        resultDatabase = preparedStatement.executeQuery();
    }
    
    /**
     * Select image between two dates.
     * @param from The older date to select first between the range
     * @param to The second date to select between the range
     * Between [from] to [to]
     * @return A map of id and Image containing all images or null if there is any.
     */
    public Map<Integer, Image> select(Date from, Date to)
    {
        Map<Integer, Image> resultImages = null;
        resultImages = new HashMap<Integer, Image>();
        try {
            tryToSelect(from, to);
            resultImages = sendPrepareResult();
        } catch (Exception e ) {
            logError(e);
        }
        return resultImages;  
    }

    /**
     * Try to select a range of image between two dates
     * @param from The older date to select first between the range
     * @param to The second date to select between the range
     * @throws Exception A map of id and Image containing all images
     */
    private void tryToSelect(Date from, Date to) throws Exception {
        sqlRequest = "SELECT * FROM IMAGE WHERE TIME >= ? and TIME <= ?";
        PreparedStatement preparedStatement = connexionDatabase.prepareStatement(sqlRequest);
        preparedStatement.setDate(1, new java.sql.Date(from.getTime()));
        preparedStatement.setDate(2, new java.sql.Date(to.getTime()));
        resultDatabase = preparedStatement.executeQuery();
    }
    
    /**
     * Get the last image in the database
     * @return A image or null if there is no image in the database
     */
    public Image selectLast()
    {
        Map<Integer, Image> resultImages = null;
        resultImages = new HashMap<Integer, Image>();
        try {
            tryToSelectLast();
            resultImages = sendPrepareResult();
        } catch (Exception e ) {
            logError(e);
        }
        
        Object[] resultArr = resultImages.keySet().toArray();
        
        if(resultArr.length == 1){
        	return resultImages.get(resultArr[0]);
        }
        
        return null;
    }
    
    /**
     * Try to execute the SELECT query to select the last image in the database
     * @throws Exception The SQLException is throw if there is no table or no results found
     */
    private void tryToSelectLast() throws Exception {
        sqlRequest = "SELECT * FROM IMAGE ORDER BY time DESC LIMIT 1";
        PreparedStatement preparedStatement = connexionDatabase.prepareStatement(sqlRequest);
        resultDatabase = preparedStatement.executeQuery();
    }

    /**
     * Prepare the image found by the queries executed on the database. 
     * @return A map of id and Image containing all images or null if there is any.
     * @throws Exception If the query could not be executed, an exception is raised.
     */
    private Map<Integer, Image> sendPrepareResult() throws Exception
    {
        Map<Integer, Image> resultImages = null;
        resultImages = new HashMap<Integer, Image>();
        
        resultImages = tryToPrepareResult();
            
        return resultImages; 
    }

    /**
     * Manage the preparation of the image and extracted the id, the name, the date
     * and the properties of each image found with the query executed previous by
     * a SELECT or UPDATE methods.
     * @return A map of id and Image containing all images or null if there is any.
     * @throws Exception An exception is raised if the informations about the image
     * could not be retrieved.
     */
    private Map<Integer, Image> tryToPrepareResult() throws Exception
    {
        Map<Integer, Image> resultImages = null;
        resultImages = new HashMap<Integer, Image>();
        
        while (resultDatabase.next()){
        	int id = resultDatabase.getInt("id");	
        	String  name = resultDatabase.getString("name");
            Date time  = resultDatabase.getDate("time");
            String json = resultDatabase.getString("JSON");
            Image image = new Image(name, time, json);
            resultImages.put(id, image);
        };
        return resultImages;
    }
    
    /**
     * Delete an image by its name in the table.
     * @param imageName The name of the image to be deleted.
     */
    public void deleteImageInTable(String imageName)
    {
        try {
            tryToDelete(imageName);
        } catch (Exception e ) {
            logError(e);
        }
    }
    
    /**
     * Try to execute the DELETE query on the database
     * @param imageName The name of the image
     * @throws Exception An exception is raised if the table doesn't exist or no connection established with the database.
     */
    private void tryToDelete(String imageName) throws Exception {
        sqlRequest = "DELETE from IMAGE where NAME = ?";
        PreparedStatement preparedStatement = connexionDatabase.prepareStatement(sqlRequest);
        preparedStatement.setString(1, imageName);
        preparedStatement.executeUpdate();
        sendCloseResult();
    }

    /**
     * Delete all the images that are three days older than the most recent one in the database.
     */
    public void deleteOld()
    {
        try {
            tryToDeleteOld();
        } catch (Exception e ) {
            logError(e);
        }
    }
    
    /**
     * Try to execute the DELETE query for all the images that are three days older than the most recent one in the database.
     * @throws Exception An exception is raised if there is no table or no connection established
     */
    private void tryToDeleteOld() throws Exception {
        
        Image image = null;
        image = selectLast();
        
        if (image != null)
        {
            System.out.println(image.getReceiveDate().getTime());
            int numberOfDayBeforeOldImage = 259200000; // 259.200 = 3 days 60*60*24*3*1000
            System.out.println(image.getReceiveDate().getTime()-259200000);
            sqlRequest = "DELETE from IMAGE where TIME < ?";
            PreparedStatement preparedStatement = connexionDatabase.prepareStatement(sqlRequest);
            preparedStatement.setDate(1, new java.sql.Date(image.getReceiveDate().getTime() - numberOfDayBeforeOldImage));
            preparedStatement.executeUpdate();
            sendCloseResult();
        }
    }
    
    /**
     * Close the result set from the database when it's no longer in use.
     */
    private void sendCloseResult()
    {
        try {
            tryToCloseResult();
        } catch (Exception e ) {
            logError(e);
        }
    }
    
    /**
     * Try to close the result from the database
     * @throws Exception
     */
    private void tryToCloseResult() throws Exception {
        if(resultDatabase != null){
        	resultDatabase.close();
        }
    }

    /**
     * Flag to verify if the database is connected or not.
     * @return A true if the database established a connection.
     *         A false if there is no database connection established.
     */
    public boolean isConnected() {
        return connected;
    }
    
    /**
     * Close the connection to the database.
     */
    public void closeConnection()
    {
        try {
            tryToCloseConnection();
            System.out.println("Closed database connection successfully");
            connected = false;
        } catch (Exception e ) {
            logError(e);
        }
    }

    /**
     * Try to close the connection in the database.
     * @throws Exception An exception is raised if the connection could not be closed or already closed.
     */
    private void tryToCloseConnection() throws Exception {
        connexionDatabase.close();
    }
    
    /**
     * State the error on the query methods if there is any.
     * @param e
     */
    private void logError(Exception e)
    {
    	e.printStackTrace();
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
    }
}