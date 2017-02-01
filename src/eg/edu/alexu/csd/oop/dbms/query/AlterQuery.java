package eg.edu.alexu.csd.oop.dbms.query;

import java.util.ArrayList;
import java.util.HashMap;

import eg.edu.alexu.csd.oop.dbms.Database.Condition;
import eg.edu.alexu.csd.oop.dbms.Database.Table;
import eg.edu.alexu.csd.oop.dbms.Database.Log4j;

public class AlterQuery implements IQuery,DdlQuery {
  private Table operand;
  private String tableName;
  private int isAdd;
  private Log4j logger = new Log4j();;
  private int index ;
  private Table toreturn;
  private  HashMap<String, Object> attr;
  
  
  public AlterQuery(){
	  index = 0;
  }

  @Override
  public Table execute() {
    if(operand == null) {
    	logger.fail("Table not Found");
    	throw new RuntimeException();
    }
      
    HashMap<String, String> temp = new HashMap<String,String>();

    if (isAdd == 1) {
      ArrayList<String> order = new ArrayList<String>();
      for (String key : operand.getAttributes()) {
        temp.put(key, operand.getTypeOf(key));
      }
      for (String key : attr.keySet()) {
        temp.put(key, (String) attr.get(key));
        order.add(key);
      }

      toreturn = new Table(temp);
      toreturn.orderatts(order);
      int n = operand.getColumn(0).size();
      for (int i = 0; i < n; i++) {
        HashMap<String, Object> nrow = operand.getRow(i);
        for (String key : attr.keySet()) {
          nrow.put(key, null);
        }
        toreturn.insertRow(nrow);
      }
      toreturn.setTableName(operand.getTableName());
      return toreturn;

    }

    if (isAdd == 0) {
      for (String key : operand.getAttributes()) {
        temp.put(key, operand.getTypeOf(key));

      }
      for (String key : attr.keySet()) {
        temp.remove(key);
      }
      toreturn = new Table(temp);
      int n = operand.getColumn(0).size();
      for (int i = 0; i < n; i++) {
        HashMap<String, Object> nrow = new HashMap<String, Object>();
        for (String key : operand.getAttributes()) {
          if (!attr.keySet().contains(key)) {
            nrow.put(key, operand.getRow(i).get(key));
          }
        }
        toreturn.insertRow(nrow);
      }
    }

    toreturn.setTableName(tableName);
    return toreturn;

  }

  @Override
  public void setTable(String tabName) {
    tableName = tabName;
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
  public void setAttributes(HashMap<String, Object> attr) {
    this.attr = attr;
    System.out.println(attr);
  }

  @Override
  public void setCondition(Condition condition) {

  }

  public void addOrDrop(int isAdd) {
    this.isAdd = isAdd;

    System.out.println(isAdd);
  }

  @Override
  public int updateCount() {
    
    return -1;
  }

}