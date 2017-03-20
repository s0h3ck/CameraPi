package libdb.libdb;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;

import libdb.ColumnImage;
import libdb.TableImage;


public class TableImageTest {
    
    @Before
    public void setUp() throws ParseException {
        
    }
    
    @Test
    public void testCreateTable() {
        final TableImage table = new TableImage();
        
        assertEquals("IMAGE", table.getName());
        assertEquals("CREATE TABLE IF NOT EXISTS", table.getProperties());
    }
    
    @Test
    public void testSetName()
    {
        final TableImage table = new TableImage();
        
        table.setName("N_IMAGE");
        assertEquals("N_IMAGE", table.getName());
        
    }
    
    @Test
    public void testGetName()
    {
        final TableImage table = new TableImage();
        
        assertEquals("IMAGE", table.getName());
    }
    
    @Test
    public void testSetProperties()
    {
        final TableImage table = new TableImage();
        
        table.setProperties("CREATE TABLE");
        assertEquals("CREATE TABLE", table.getProperties());
        
    }
    
    @Test
    public void testGetProperties()
    {
        final TableImage table = new TableImage();
        assertEquals("CREATE TABLE IF NOT EXISTS", table.getProperties());
    }
    
    @Test
    public void testCreateColumns() {
        final TableImage table = new TableImage();
        
        ColumnImage id = new ColumnImage("ID", "INTEGER PRIMARY KEY AUTOINCREMENT");
        ColumnImage name = new ColumnImage("NAME", "TEXT NOT NULL");
        ColumnImage time = new ColumnImage("TIME", "DATETIME default current_timestamp");
        ColumnImage json = new ColumnImage("JSON", "TEXT NOT NULL");
        
        ColumnImage notInTable = new ColumnImage("FAKE", "TEXT NOT NULL");
        ColumnImage notInTableProperties = new ColumnImage("NAME", "FAKE PROPERTIES");
        
        assertEquals(id, table.getColumnFromTheTable(id));
        assertEquals(name, table.getColumnFromTheTable(name));
        assertEquals(time, table.getColumnFromTheTable(time));
        assertEquals(json, table.getColumnFromTheTable(json));
        assertNull(table.getColumnFromTheTable(notInTable));
        assertNull(table.getColumnFromTheTable(notInTableProperties));
    }
    
    @Test
    public void testSetAddColumnToTheTable() {
        final TableImage table = new TableImage();
        
        ColumnImage newColumn = new ColumnImage("NEW", "TEXT NOT NULL");
        
        table.addColumnToTheTable(newColumn);
        assertEquals(newColumn, table.getColumnFromTheTable(newColumn));
    }
    
    @Test
    public void testGetSQLCreateTable() {
        final TableImage table = new TableImage();
        
        String supposeCreateTableQuery = "CREATE TABLE IF NOT EXISTS IMAGE (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT NOT NULL,TIME DATETIME default current_timestamp,JSON TEXT NOT NULL);";        
        assertEquals(supposeCreateTableQuery, table.getSQLCreateTable());
    }
    
    @Test
    public void testRemoveColumnFromTheTable() 
    {
        final TableImage table = new TableImage();
        
        ColumnImage id = new ColumnImage("ID", "INTEGER PRIMARY KEY AUTOINCREMENT");
        ColumnImage name_fake_properties = new ColumnImage("NAME", "FAKE PROPERTIES");
        ColumnImage name = new ColumnImage("NAME", "TEXT NOT NULL");
        ColumnImage time = new ColumnImage("TIME", "DATETIME default current_timestamp");
        ColumnImage json = new ColumnImage("JSON", "TEXT NOT NULL");
        
        assertTrue(table.removeColumnFromTheTable(id));
        assertFalse(table.removeColumnFromTheTable(name_fake_properties));
        assertTrue(table.removeColumnFromTheTable(name));
        assertTrue(table.removeColumnFromTheTable(time));
        assertTrue(table.removeColumnFromTheTable(json));
        assertFalse(table.removeColumnFromTheTable(json));
        
    }
    
    @Test
    public void testGetEmptySQLCreateTable() {
        final TableImage table = new TableImage();
        
        ColumnImage id = new ColumnImage("ID", "INTEGER PRIMARY KEY AUTOINCREMENT");
        ColumnImage name = new ColumnImage("NAME", "TEXT NOT NULL");
        ColumnImage time = new ColumnImage("TIME", "DATETIME default current_timestamp");
        ColumnImage json = new ColumnImage("JSON", "TEXT NOT NULL");
        
        table.removeColumnFromTheTable(id);
        table.removeColumnFromTheTable(time);
        table.removeColumnFromTheTable(name);
        table.removeColumnFromTheTable(json);
        
        String supposeCreateTableQuery = "CREATE TABLE IF NOT EXISTS IMAGE ();";        
        assertEquals(supposeCreateTableQuery, table.getSQLCreateTable());
    }
    
}
