package libdb.libdb;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

import libdb.Image;
import libdb.SQLiteJDBC;
import libdb.TableImage;

public class SQLiteJDBCDriverConnectionTest  {
    
    private final static String test_database_path = "test.db";
    
    @Test
    public void testMockDefaultDB() throws Exception {
        final SQLiteJDBC database = new SQLiteJDBC();
        
        String homeFolder = System.getProperty("user.home");
        String default_db_path = "jdbc:sqlite:"+ homeFolder + "/camerapi.db";
        assertEquals(default_db_path, database.getDatabaseFile());
    }
        
    @Test
    public void testMockTestDB() throws Exception {
        final SQLiteJDBC database = new SQLiteJDBC();
        
        database.setDatabaseFile(test_database_path);
        String homeFolder = System.getProperty("user.home");
        String test_db_path = "jdbc:sqlite:"+ homeFolder + "/" + test_database_path;
        assertEquals(test_db_path, database.getDatabaseFile());
    }
    
    @Test
    public void testMockTestDBConnection() throws Exception {
        final SQLiteJDBC database = new SQLiteJDBC();
        database.connect();
        assertEquals(database.isConnected(), true);
        database.closeConnection();
    }
    
    @Test
    public void testCreateTable() {
        final SQLiteJDBC database = new SQLiteJDBC();
        final TableImage tableImage = new TableImage();
        
        database.createTable(tableImage);
    }
    
    @Test
    public void testMockTestDBSelectAll() throws Exception {
        final SQLiteJDBC database = new SQLiteJDBC();
        final TableImage tableImage = new TableImage();
        
        database.setDatabaseFile(test_database_path);
        
        database.connect();
        database.createTable(tableImage);
        
        String dateString[] = {"2016-09-14 10:17:25", "2016-11-22 10:17:25", "2016-11-24 10:17:25"};
        Date date[] = {null, null, null};
        String emptyJSONString = "{}";
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for( int i = 0 ; i < 3 ; i = i + 1) {
            date[i] = null;
            date[i] = format.parse(dateString[i]);
            database.insertImageInTable(new Image("image" + Integer.toString(i), date[i], emptyJSONString));
        }        
        
        Map<Integer, Image> testAll = new HashMap<Integer, Image>();
        testAll = database.selectAll();
        assertEquals(testAll.isEmpty(), false);
        int i = 0;
        for (Image entry : testAll.values()) {
            assertEquals(entry.getFileName(), "image" + Integer.toString(i));
            assertEquals(entry.getReceiveDate(), date[i]);
            assertEquals(entry.getJSONData(), emptyJSONString);
            i = i + 1;
        }
        database.closeConnection();
    }
    
    @Test
    public void testMockTestDBSelectLastNull() throws Exception {
        final SQLiteJDBC database = new SQLiteJDBC();
        final TableImage tableImage = new TableImage();
        
        database.setDatabaseFile(test_database_path);
        
        database.connect();
        database.createTable(tableImage);
        
        Image image = database.selectLast();
        
        assertEquals(image, null);
        
        
    }
    
    @Test
    public void testMockTestDBSelectLast() throws Exception {
        final SQLiteJDBC database = new SQLiteJDBC();
        final TableImage tableImage = new TableImage();
        
        database.setDatabaseFile(test_database_path);
        
        database.connect();
        database.createTable(tableImage);
        
        String dateFirstString = "2016-09-14 10:17:25";
        String dateLastString = "2016-11-22 10:17:25";
        Date dateFirst = null;
        Date dateLast = null;
        String emptyJSONString = "{}";
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        dateFirst = format.parse(dateFirstString);
        dateLast = format.parse(dateLastString);
        
        database.insertImageInTable(new Image("imageFirst", dateFirst, emptyJSONString));
        database.insertImageInTable(new Image("imageLast", dateLast, emptyJSONString));
        
        Image image = database.selectLast();
        assertEquals(image.getFileName(), "imageLast");
        assertEquals(image.getReceiveDate(), dateLast);
        assertEquals(image.getJSONData(), emptyJSONString);
        
        database.closeConnection();
    }
    
    @Test
    public void testMockTestDBDeleteImage() throws Exception {
        final SQLiteJDBC database = new SQLiteJDBC();
        final TableImage tableImage = new TableImage();
        
        database.setDatabaseFile(test_database_path);
        
        database.connect();
        database.createTable(tableImage);
        
        String dateString[] = {"2016-09-14 10:17:25", "2016-11-22 10:17:25", "2016-11-24 10:17:25"};
        Date date[] = {null, null, null};
        String emptyJSONString = "{}";
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for( int i = 0 ; i < 3 ; i = i + 1) {
            date[i] = null;
            date[i] = format.parse(dateString[i]);
            database.insertImageInTable(new Image("image" + Integer.toString(i), date[i], emptyJSONString));
        }        
        
        database.deleteImageInTable("image0");
        
        Map<Integer, Image> testAll = new HashMap<Integer, Image>();
        testAll = database.selectAll();
        assertEquals(testAll.isEmpty(), false);
        int i = 1;
        for (Image entry : testAll.values()) {
            assertEquals(entry.getFileName(), "image" + Integer.toString(i));
            assertEquals(entry.getReceiveDate(), date[i]);
            assertEquals(entry.getJSONData(), emptyJSONString);
            i = i + 1;
        }
        database.closeConnection();
    }
    
    @Test
    public void testMockTestDBDeleteOldNull() throws Exception {
        final SQLiteJDBC database = new SQLiteJDBC();
        final TableImage tableImage = new TableImage();
        
        database.setDatabaseFile(test_database_path);
        
        database.connect();
        database.createTable(tableImage);
        
        database.deleteOld();
        
        database.closeConnection();
    }
    
    @Test
    public void testMockTestDBDeleteOld() throws Exception {
        final SQLiteJDBC database = new SQLiteJDBC();
        final TableImage tableImage = new TableImage();
        
        database.setDatabaseFile(test_database_path);
        
        database.connect();
        database.createTable(tableImage);
        
        String dateString[] = {"2016-09-14 10:17:25", "2016-11-22 10:17:25", "2016-11-24 10:17:25"};
        Date date[] = {null, null, null};
        String emptyJSONString = "{}";
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for( int i = 0 ; i < 3 ; i = i + 1) {
            date[i] = null;
            date[i] = format.parse(dateString[i]);
            database.insertImageInTable(new Image("image" + Integer.toString(i), date[i], emptyJSONString));
        }        
        
        database.deleteOld();
        
        Map<Integer, Image> testAll = new HashMap<Integer, Image>();
        testAll = database.selectAll();
        assertEquals(testAll.isEmpty(), false);
        int i = 1;
        for (Image entry : testAll.values()) {
            assertEquals(entry.getFileName(), "image" + Integer.toString(i));
            assertEquals(entry.getReceiveDate(), date[i]);
            assertEquals(entry.getJSONData(), emptyJSONString);
            i = i + 1;
        }
        database.closeConnection();
    }
    
    @Test
    public void testMockTestDBDeletePhysical() throws Exception {
        final SQLiteJDBC database = new SQLiteJDBC();
        final TableImage tableImage = new TableImage();
        
        database.setDatabaseFile(test_database_path);
        
        database.connect();
        database.createTable(tableImage);
        
        database.closeConnection();
    }
    
    @Test
    public void testMockTestDBDeleteImageInTable() throws Exception {
        final SQLiteJDBC database = new SQLiteJDBC();
        
        database.setDatabaseFile(test_database_path);
        
        database.connect();
        database.deleteImageInTable("fake image");
        
        database.closeConnection();
    }
    
    
    @Test
    public void testMockTestDBSelectBetweenTwoDate() throws Exception {
        final SQLiteJDBC database = new SQLiteJDBC();
        final TableImage tableImage = new TableImage();
        
        database.setDatabaseFile(test_database_path);
        
        database.connect();
        database.createTable(tableImage);
        
        String dateString[] = {"2016-08-14 10:17:25", "2016-09-14 10:17:25", "2016-11-22 10:17:25", "2016-11-24 10:17:25", "2016-12-02 10:17:25"};
        Date date[] = {null, null, null, null, null};
        String emptyJSONString = "{}";
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for( int i = 0 ; i < 5 ; i = i + 1) {
            date[i] = null;
            date[i] = format.parse(dateString[i]);
            database.insertImageInTable(new Image("image" + Integer.toString(i), date[i], emptyJSONString));
        }      
        
        Map<Integer, Image> testSelectBetweenTwoDates = new HashMap<Integer, Image>();
        testSelectBetweenTwoDates = database.select(date[1], date[3]);
        
        int i = 1;
        for (Image entry : testSelectBetweenTwoDates.values()) {
            assertEquals(entry.getFileName(), "image" + Integer.toString(i));
            assertEquals(entry.getReceiveDate(), date[i]);
            assertEquals(entry.getJSONData(), emptyJSONString);
            i = i + 1;
        }
        database.closeConnection();
    }
       
    @After
    public void RemoveDatabase() throws IOException {
       String homeTestDatabase = System.getProperty("user.home") + "/" + test_database_path;
       File file = new File(homeTestDatabase);
       file.delete();
    }
}