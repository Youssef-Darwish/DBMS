package eg.edu.alexu.csd.oop.dbms.Database;

import java.util.HashMap;
import java.util.List;

/**
 * The Interface ITable.
 */
public interface ITable {
  /**
   * returns the column of the given name, returns null if the name doesn't exist.
   * @param name name of the column.
   * @return list of values in the column.
   */
  public List<Object> getColumn(String name);
  
  /**
   * Inserts new Row to table.
   * @param newRow new row values to be added.
   * @throws RuntimeException if the row was malformed. 
   */
  void insertRow(List<String> newRow) throws RuntimeException;
  
  /**
   * updates value in a certain row.
   * @param index index of row to be updated.
   * @param newRowV the values in the new row.
   * A null value would be ignored and the original value remains as it is.
   * @throws RuntimeException when row is malformed.
   * @throws ArrayIndexOutOfBoundsException the array index out of bounds exception
   */
  void updateRow(int index,HashMap<String, Object> newRowV) throws RuntimeException,
      ArrayIndexOutOfBoundsException;
  
  /**
   * deletes row of given index.
   * @param index of the row to be deleted.
   * @throws ArrayIndexOutOfBoundsException if index is out of bounds.
   */
  void deleteRow(int index) throws ArrayIndexOutOfBoundsException;
  
  /**
   * returns attributes of the table.
   * @return list of names of the columns.
   */
  List<String> getAttributes();
  
  
  
  
}
