package libdb.libdb;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import libdb.ColumnImage;
import libdb.Image;

public class ColumnImageTest {

    private String name;
    private String properties;
    
    @Before
    public void setUp() throws ParseException {
        name = "NAME";
        properties = "TEXT NOT NULL";
    }
    
    @Test
    public void testConstructors() {
        final ColumnImage columnImage = new ColumnImage(name, properties);
    }
    
    @Test
    public void testSetColumnName() {
        final ColumnImage columnImage = new ColumnImage(name, properties);
                
        columnImage.setColumnName("plop");
        
        assertEquals("plop", columnImage.getColumnName());
    }
    
    @Test
    public void testSetProperties() {
        final ColumnImage columnImage = new ColumnImage(name, "wrong");
                
        columnImage.setProperties("TEXT NOT NULL");
        
        assertEquals("TEXT NOT NULL", columnImage.getProperties());
    }
    
    
    
}
