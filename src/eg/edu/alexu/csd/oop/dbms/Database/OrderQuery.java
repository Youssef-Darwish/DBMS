package eg.edu.alexu.csd.oop.dbms.Database;

import java.util.HashMap;

import eg.edu.alexu.csd.oop.dbms.query.IQuery;

public class OrderQuery implements IQuery {

    int orderDirection;
    Table operand;
    String tableName;
    HashMap<String, Object> attr;
    
    
    public OrderQuery() {
        // TODO Auto-generated constructor stub
        //orderDirection is 1 when ascending and 0 otherwise.
        //orderDirection is ascending by default.
        orderDirection = 1;
    }
    
    
    @Override
    public Table execute() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTable(String tabName) {
        // TODO Auto-generated method stub
        tableName = tabName;
    }

    @Override
    public void setTable(Table table) {
        // TODO Auto-generated method stub
        operand = table;
    }

    @Override
    public String getTableName() {
        // TODO Auto-generated method stub
        return tableName;
    }

    @Override
    public void setAttributes(HashMap<String, Object> attr) {
        // TODO Auto-generated method stub
        this.attr = attr;
    }

    @Override
    public void setCondition(Condition condition) {
        // TODO Auto-generated method stub
        
    }
    
    public void setSortDirection(String order) {
        if(order.equals("desc")) {
            orderDirection = 0 ;
        }
    }


    @Override
    public int updateCount() {
      // TODO Auto-generated method stub
      return 0;
    }

}
