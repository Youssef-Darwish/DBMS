package eg.edu.alexu.csd.oop.dbms.Database;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface IDataBase {
  /**
   * adds new table into database.
   * @param name
   * @param table
   */
  public void pushTable(String name,Table table);
  
  /**
   * removes table of given name.
   * @param name name of table to be removed.
   * @return table removed.
   */
  public ITable popTable(String name); 
  
  /**
   * gets table of given name.and null if there is none.
   * @param name name of table.
   * @return table desired.
   */
  public ITable getTable(String name);
  
  /**
   * gets database directory.
   * @return directory of database.
   */
  public File getPath();
  
  /**
   * returns names of all tables.
   * @return names of tables wallahy.
   */
  public List<String> allTables();
  
  /**
   * Save table.
   *
   * @param file the file
   * @throws Exception the exception
   */
  void SaveTable(String name) throws Exception;
  
  /**
   * Load table.
   *
   * @param file the file
   * @throws Exception the exception
   */
  void LoadTable(String name) throws Exception;
  
  /**
   * removes current db
   */
  void remove();
  
  
}
