package eg.edu.alexu.csd.oop.dbms.query;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eg.edu.alexu.csd.oop.dbms.Database.Condition;
import eg.edu.alexu.csd.oop.dbms.Database.Table;
import eg.edu.alexu.csd.oop.dbms.Database.Log4j;

public class Insert implements IQuery {


  private Table operand;
  private String tableName;
  private Condition condition;
  private HashMap <String,Object> attributes;
  private List <String> vals;
  private Log4j logger;
  public Insert(){

  }
  public static Object parseType(String val , String type) {
    Object toret = null;
    if(type.equals("varchar")) {
      if(!val.contains("'"));
        //throw new RuntimeException();
       toret = val.replaceAll("'", "");
    } else if(type.equals("int")) {
      toret = Integer.valueOf(val);
    }else if(type.equals("float")) {
      toret = Double.valueOf(val);
    }else if(type.equals("date")) {
      val = val.replaceAll("'", "");
      toret = Date.valueOf(val);
    }
    
    return toret;
  }
  @Override
  public Table execute() {
    if(operand == null) {
    	logger.fail("table not found");
    	throw new RuntimeException("");
    }
      
    if (attributes != null ) {
      HashMap<String,Object> newRow = new HashMap<String,Object>();
      for(String key:attributes.keySet()) {
        if (! operand.getAttributes().contains(key)) {
        	logger.fail("invalid Column");
        	throw new RuntimeException("column not found");
        }
          
          
        
        newRow.put(key, parseType((String)attributes.get(key),
            operand.getTypeOf(key)));
      }
      operand.insertRow(newRow);
	    return operand;
    }
    attributes = new HashMap<String,Object>();
    List<String> cols = operand.getAttributes();
    
    for (int i = 0 ;i < vals.size() ;i++) {
      String col = cols.get(i);
      
      
        attributes.put(col,  parseType((String)vals.get(i),
            operand.getTypeOf(col)));
      
    }
    operand.insertRow(attributes);
    return operand;
  }

  @Override
  public void setTable(String tabName) {
    this.tableName = tabName;
	
  }

  @Override 
  public void setTable(Table table) {
    this.operand = table;
  }

  @Override
  public String getTableName() {
    return tableName;
  }

  @Override
  public void setAttributes(HashMap<String, Object> attr) {
    this.attributes = attr;

  }
  

  public void setAttributes(List<String> vals) {
    this.vals = vals;
  }
  @Override
  public void setCondition(Condition condition) {
    this.condition = condition;
  }
  @Override
  public int updateCount() {
    return 1;
  }
 
}
