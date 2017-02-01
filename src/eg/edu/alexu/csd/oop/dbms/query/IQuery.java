package eg.edu.alexu.csd.oop.dbms.query;

import java.util.HashMap;
import java.util.List;

import eg.edu.alexu.csd.oop.dbms.Database.Condition;
import eg.edu.alexu.csd.oop.dbms.Database.Table;

public interface IQuery {
  /**
   * executes query.
   * @return table after query.
   */
  public Table execute() ;

  public int updateCount();

  /**
   * sets table name of query.
   * @param tabName able name.
   */
  public void setTable(String tabName);

  /**
   * sets table of query.
   * @param table table.
   */
  public void setTable(Table table);
  
  /**
   * returns table name to excute query on.
   * @return table name.
   */
  public String getTableName();
  
  /**
   * sets attributes to use in query.
   * @param attr list of attributes.
   */
  public void setAttributes(HashMap<String,Object> attr);

  /**
   * sets condition to use in query.
   * @param condition to use in query.
   */
  public void setCondition(Condition condition);
}
