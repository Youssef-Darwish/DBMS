package eg.edu.alexu.csd.oop.dbms.Database;

public class Condition {
  public String column;
  
  /**
   * creates new condition.
   */
  public Condition(String colName) {
    column = colName;
  }
  
  public Condition() {
    column = null;
  }

  /**
   * executes  the condition.
   * @param inst value to compare.
   * @return the value of the condition.
   */
  public boolean compare(String col,Object inst) {
    return true;
  }
  
}
