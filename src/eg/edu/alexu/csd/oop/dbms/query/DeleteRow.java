package eg.edu.alexu.csd.oop.dbms.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eg.edu.alexu.csd.oop.dbms.Database.Condition;
import eg.edu.alexu.csd.oop.dbms.Database.Table;
import eg.edu.alexu.csd.oop.dbms.Database.Log4j;

public class DeleteRow implements IQuery{
    
	private Log4j logger;
    private Table operand;
    private String tableName;
    private Condition condition;
    private HashMap <String,Object> attr;
    private int updateCount ;
    public DeleteRow() {
      updateCount = 0;
    }
    

    @Override
    public Table execute() {
      if(operand == null) {
    	  logger.fail("Table not found");
    	  throw new RuntimeException();  
      }
        
        updateCount = 0;
        List<String> keys = new ArrayList<String>();
        List<Integer> indices = new ArrayList<>();
        keys = operand.getAttributes(); 
        int i = 0, n = 0;

        do {
            boolean flag = true;
            for (String key : keys) {
                n = operand.getColumn(key).size();
                flag &= condition.compare(key, operand.getColumn(key).get(i));
                //System.out.print(key +" "+flag);
            }
            if((condition.column != null))
            flag &= operand.getAttributes().contains(condition.column);
            if (flag) {
                indices.add(i);
            }
            i++;
        } while (i < n);
        
        for(int j = indices.size()-1 ; j >=0  ; j--) {
            operand.deleteRow(indices.get(j));
            updateCount++;
        }
        
        return operand;
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
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public void setAttributes(HashMap<String, Object> attr) {
     this.attr = attr;
        
    }


    @Override
    public int updateCount() {
      return updateCount;
    }
    
//    public static void main(String[] args) {
//
//        List<String> atts = new ArrayList<String>();
//        atts.add("Name");
//        atts.add("Age");
//        atts.add("Id");
//
//        Table test = new Table(atts);
//        HashMap<String, Object> row = new HashMap<String, Object>();
//        row.put("Name", "Ahmed");
//        row.put("Age", 20);
//        row.put("Id", 15);
//        test.insertRow(row);
//        row = new HashMap<String, Object>();
//        row.put("Name", "Amr");
//        row.put("Age", 30);
//        row.put("Id", 16);
//        test.insertRow(row);
//        row = new HashMap<String, Object>();
//        row.put("Name", "Adham");
//        row.put("Age", 40);
//        row.put("Id", 25);
//        test.insertRow(row);
//        DeleteRow query = new DeleteRow();
//        query.setTable(test);
//        query.setCondition(new Equivalence("Id", 16));
//        row = new HashMap<String, Object>();
//        row.put("Id", null);
//        query.setAttributes(row);
//        test = query.execute();
//        List<String> names = test.getAttributes();
//        for (int i = 0; i < names.size(); i++) {
//            System.out.println((names.get(i)));
//        }
//        List<Object> cols = test.getColumn("Id");
//        for (int i = 0; i < cols.size(); i++) {
//            System.out.println((cols.get(i)));
//        }
//
//    }

}
