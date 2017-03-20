package libdb;

/**
 * The class structure for the column of the image in the database. 
 * @author came
 *
 */
public class ColumnImage {

    public String name;
    public String properties;
    
    /**
     * Create a new column for the table image.
     * @param name The name of the column in the image table
     * @param properties The properties of that column
     */
    public ColumnImage(String name, String properties)
    {
        this.name = name;
        this.properties = properties;
    }
    
    /**
     * Set the name of the column
     * @param name The name of the column in the image table
     */
    public void setColumnName(String name)
    {
        this.name = name;
    }
    
    /**
     * Get the name of the column
     * @return The column name
     */
    public String getColumnName()
    {
        return name;
    }
    
    /**
     * Set the properties of the column
     * @param properties The properties of that column
     */
    public void setProperties(String properties)
    {
        this.properties = properties;
    }
    
    /**
     * Get the properties of the column
     * @return The properties of the instance of that
     */
    public String getProperties()
    {
        return properties;
    }
}
