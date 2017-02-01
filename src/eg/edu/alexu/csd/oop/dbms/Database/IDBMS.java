package eg.edu.alexu.csd.oop.dbms.Database;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface IDBMS {
  
  /**
   * sets workspace of the System.
   * @param workspace path of workspace.
   */
  public void setWorkSpace(String workspace);
  
  
  /**
   * sets workspace of the System.
   * @param workspace path of workspace.
   */
  public void setWorkSpace(File workspace);
  
  /**
   * creates new data base if not exists.
   * @param name name of the new database.
   * @throws InstantiationException exception at creation.
   * @throws IllegalAccessException if writer is in accessable to instantiate.
   */
  public void createDB(String name) throws InstantiationException, IllegalAccessException ;
  
  /**
   * drops database.
   * @param name name of database to drop.
   */
  public void dropDB(String name);
  
  /**
   * switches to 
   * @param name
   * @throws RuntimeException
   */
  public void switchDB(String name) throws RuntimeException;

  /**
   * returns name of created databases.
   * @return list of names of databases.
   */
  public List<String> databaseNames();
  
  
  /**
   * shows tables in used database.
   * @return list of names of table in database.
   */
  public List<String > tableNames();
  
  /**
   * pushes new query to execute.
   * @param query new query as a string.
   */
  public String pushQuery(String query) ;
  
  /**
   * executes an SQL query.
   * @param query stringified query to execute.
   * @return result table or null if no reslut table; 
   * @throws Exception 
   */
  public Table executeQuery(String query) throws Exception;
  
  /**
   * 
   * @param name
   * @return
   */
  public IDataBase getDataBase(String name);
  
  /**
   * gets table of given name from assigned database.
   * @param name name of table.
   * @return table required.
   */
  public ITable getTable(String name);
}
