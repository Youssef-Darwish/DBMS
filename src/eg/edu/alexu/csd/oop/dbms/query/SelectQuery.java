package eg.edu.alexu.csd.oop.dbms.query;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eg.edu.alexu.csd.oop.dbms.Database.Condition;
import eg.edu.alexu.csd.oop.dbms.Database.Table;
import eg.edu.alexu.csd.oop.dbms.Database.Log4j;
public class SelectQuery implements IQuery {

  private Table operand;
  private String tableName;
  private Condition condition;
  private HashMap < String , Object> attr;
  private List<String>attorder;
  private Log4j logger;
  /**
   * creates new select query.
   */
  public SelectQuery() {
    logger = new Log4j();
  }
  
  @Override
  public Table execute() {
    List<String> keys = new ArrayList<String>();
    keys.addAll(attr.keySet());
    if(operand == null) {
    	logger.fail("Table not found");
    	throw new RuntimeException();
    }
      
    if(keys.get(0).equals("*"))
      keys = operand.getAttributes();
    HashMap<String,String> inse = new  HashMap<String,String>();
    for(String key : keys) {
      if ( operand.getAttributes().contains(key))
        inse.put(key, operand.getTypeOf(key));
      else
      {
    	  logger.fail("Invalid column");
    	  throw new RuntimeException("column not found");  
      }
        
      
    }
    Table toreturn = new Table(inse);
    toreturn.orderatts(attorder);
    int i = 0,n = 0;
    
      n = operand.getColumn(0).size();
    while(i < n) {
      HashMap<String,Object> temp = new HashMap<String,Object>();
      boolean insert = true;
    for (String key: keys) {
      temp.put(key, operand.getColumn(key).get(i));
    }
    for (String key: operand.getAttributes()) {
      
    insert &= condition.compare(key, operand.getColumn(key).get(i));
    }
    if((condition.column != null))
    insert &= operand.getAttributes().contains(condition.column);
    
    i++;
    if (insert)
      toreturn.insertRowSure(temp);
    }
    toreturn.setTableName(operand.getTableName());
    if(toreturn.getColumn(toreturn.getAttributes().get(0)).size() ==0)
      return null;
    return toreturn;
  }

  @Override
  public void setTable(String tabName) {
    tableName = tabName;
//    System.out.println("table name "+tabName);
  }

  @Override
  public void setTable(Table table) {
    operand = table;
    
  }

  @Override
  public String getTableName() {
    return tableName;
  }

  @Override
  public void setAttributes(HashMap<String,Object> attr) {
   this.attr = attr;
 //  System.out.println("keys "+attr.keySet());
 //  System.out.println(attr.values());
  }
  
  public void setAttributes(List<String> atts) {
    attorder = atts;
  //  System.out.println("keys "+attr.keySet());
  //  System.out.println(attr.values());
   }
  @Override
  public void setCondition(Condition condition) {
    this.condition = condition;
    
  }

  @Override
  public int updateCount() {
    return -1;
  }
  
  
}
