package libdb.libdb;

import libdb.Image;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class ImageTest {

    private String name;
    private Date date1;
    private Date date2;
    private String json;
    
    @Before
    public void setUp() throws ParseException {
        name = "Bob";
        String strdate1 = "2016-09-15 10:17:25";
        String strdate2 = "2111-11-11 11:11:11";
        json = "{ \"image\": { \"rectangle\": { \"x\": 10, \"y\": 20, \"w\": 10, \"h\": 10 }}}";
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        date1 = format.parse(strdate1);
        date2 = format.parse(strdate2);
    }
    
    @Test
    public void testConstructors() {
        
        final Image image = new Image(name, date1, json);
        
        assertEquals(name, image.getFileName());
        assertEquals(date1, image.getReceiveDate());
    }
    
    @Test
    public void testSetName() {
        final Image image = new Image("A name",  date1, json);
        
        image.setImageName("foo");
        
        assertEquals("foo", image.getFileName());
    }
    
    @Test
    public void testSetDate() {
        final Image image = new Image("A name", date1, json);
                
        image.setReceiveDate(date2);
        
        assertEquals(date2, image.getReceiveDate());
    }
    
    @Test
    public void testGetName() {
        final Image image = new Image("A name", date2, json);
        
        image.setImageName("foo");
        
        assertEquals("foo", image.getFileName());
    }
    
    @Test
    public void testGetDate() {
        final Image image = new Image("A name", date1, json);
        
        image.setReceiveDate(date2);
        
        assertEquals(date2, image.getReceiveDate());
    }
    
    @Test
    public void testSetJSONData() {
        final Image image = new Image("A name", date1, json);
        
        String json_assert = "{}";
        
        image.setJSONData(json_assert);
        
        assertEquals(json_assert, image.getJSONData());
    }
    
    @Test
    public void testGetJSONData() {
        final Image image = new Image("A name", date1, json);
                
        assertEquals(json, image.getJSONData());
    }
}