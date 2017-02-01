package eg.edu.alexu.csd.oop.dbms.Database;

import java.io.File;

/**
 * The Interface IFileBehaviour.
 */
public interface IFileBehaviour {
    
    /**
     * Sets the table.
     *
     * @param table the new table
     */
    void setTable(Table table);
    
    /**
     * Gets the table.
     *
     * @return the table
     */
    Table getTable();
    
    /**
     * Save table.
     *
     * @param file the file
     */
    void saveTable(File file);
    
    /**
     * Load table.
     *
     * @param file the file
     */
    void loadTable(File file);
    
    /**
     * Save table schema.
     *
     * @param file the file
     */
    void saveTableSchema(File file);
    
    /**
     * get file extension.
     */
    public String getExtension();
}
