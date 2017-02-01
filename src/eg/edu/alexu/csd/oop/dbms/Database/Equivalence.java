package eg.edu.alexu.csd.oop.dbms.Database;

import java.sql.Date;

public class Equivalence extends Condition {

  private String base;

  /**
   * creates new equivalence condition.
   * @param colName column name to test.
   * @param base base object to compare to.
   */
  public Equivalence(String colName, Object base) {
    super(colName);
    this.base = (String)base;
    //System.out.println("condition "+colName+" "+base);

  }

  @Override
  public boolean compare(String col, Object inst) {
    if (inst instanceof Integer) {
      return (!col.equals(column) || ((Integer)inst).equals(Integer.valueOf(base)));
    }else if (inst instanceof Double) {
      return (!col.equals(column) || ((Double)inst).equals(Double.valueOf(base)));
    }
    else if (inst instanceof Date ) {
      return (!col.equals(column) || ((Date)inst).equals(Date.valueOf(base)));
    }
    else {
      return (!col.equals(column) || ((String)inst).equals(base));
    }
    
  }

}
