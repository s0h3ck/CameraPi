package libdb;

import java.util.Stack;

/**
 * A class to describe a table of images in the database
 * @author came
 *
 */
public class TableImage {

    private String name;
    private String properties;
    public Stack<ColumnImage> listOfColumns;
    
    /**
     * Create the main structure of a table of images
     */
    public TableImage()
    {
        createTable();
        createColumns();
    }
    
    /**
     * Create the name of the table and set the properties
     * of that table when it will be inserted in the database
     * By default, the name of the table will be IMAGE
     */
    public void createTable()
    {
        setName("IMAGE");
        setProperties("CREATE TABLE IF NOT EXISTS");
    }

    /**
     * Create four image columns in the image table.
     * The id column with primary key and autoincremented field.
     * The name column for the name about the image
     * The time column for the receive date  on the server about the image
     * The json column for additional details about the image
     */
    public void createColumns() {
        listOfColumns = new Stack<ColumnImage>();
        addColumnToTheTable(new ColumnImage("ID", "INTEGER PRIMARY KEY AUTOINCREMENT"));
        addColumnToTheTable(new ColumnImage("NAME", "TEXT NOT NULL"));
        addColumnToTheTable(new ColumnImage("TIME", "DATETIME default current_timestamp")); // current_timestamp = YYYY-MM_DD HH:MM:SS
        addColumnToTheTable(new ColumnImage("JSON", "TEXT NOT NULL"));
    }
    
    /**
     * Change the name of the image table
     * @param name The name of the image table
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Set the name of the image table
     * @return The image table
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Set the properties of the table
     * @param properties The image properties of 
     */
    public void setProperties(String properties)
    {
        this.properties = properties;
    }
    
    /**
     * Get the properties of the table
     * @return The properties of the table
     */
    public String getProperties()
    {
        return this.properties;
    }
    
    /**
     * Add a new column in the table 
     * @param column The column to be added in the table
     */
    public void addColumnToTheTable(ColumnImage column) {
        listOfColumns.add(column);
    }
    
    /**
     * Search a column in the table. If it does exist,
     * the column found will be returned. Otherwise, null returned.
     * @param column The ColumnImage to search in the table
     * @return The ColumnImage of null if not found
     */
    public ColumnImage getColumnFromTheTable(ColumnImage column) {
        for(ColumnImage c : listOfColumns) {
            if(c.getColumnName() == column.getColumnName())
            {
                if(c.getProperties() == column.getProperties())
                {
                    return column;
                }
            }
        }
        return null;
    }
    
    /**
     * Remove a column in the table.
     * @param column The ColumnImage to search in the table
     * @return A boolean true if the column was removed
     *         A boolean false if the column 
     */
    public boolean removeColumnFromTheTable(ColumnImage column) {
        for(ColumnImage c : listOfColumns) {
            if(c.getColumnName() == column.getColumnName())
            {
                if(c.getProperties() == column.getProperties())
                {
                    listOfColumns.remove(c);
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * The query string to create a table for the database
     * @return The "creatable" string sql request
     */
    public String getSQLCreateTable()
    {
        String SQLCreateTableRequest = "";
        
        SQLCreateTableRequest += properties + " " + name + " (";
        
        for (ColumnImage column : listOfColumns)
        {
            SQLCreateTableRequest += column.getColumnName() + " " + column.getProperties() + ",";
        }
        
        int length = SQLCreateTableRequest.length();
        
        if (SQLCreateTableRequest.charAt(length-1) == ',')
        {
            SQLCreateTableRequest = SQLCreateTableRequest.substring(0, length-1);
        }
        
        SQLCreateTableRequest += ");";
        
        System.out.println(SQLCreateTableRequest);
        return SQLCreateTableRequest;
    }
}
