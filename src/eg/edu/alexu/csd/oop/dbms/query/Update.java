package eg.edu.alexu.csd.oop.dbms.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.omg.CosNaming.NamingContextPackage.NotFound;

import eg.edu.alexu.csd.oop.dbms.Database.Condition;
import eg.edu.alexu.csd.oop.dbms.Database.Table;
import eg.edu.alexu.csd.oop.dbms.Database.Log4j;

public class Update implements IQuery {

	private Table operand;
	private String tableName;
	private Condition condition;
	private HashMap<String, Object> attr;
	private HashMap<String, Object> newValues;
	private int updateCount;
	private Log4j logger;
	public Update() {
		newValues = new HashMap<>();
		updateCount =0;
	}

	@Override
	public Table execute() {
	  updateCount =0;
		List<String> keys = new ArrayList<String>();

		keys.addAll(attr.keySet());
		if(operand == null) {
			logger.fail("Table not found");
			throw new RuntimeException();
		}
		    
    newValues  = new HashMap<String,Object>();
    for(String key:attr.keySet()) {
      if (! operand.getAttributes().contains(key))
      {
    	  logger.fail("Invalid column");
    	  throw new RuntimeException();  
      }
        
        
      
      newValues.put(key, Insert.parseType((String)attr.get(key), 
          operand.getTypeOf(key)));
    }
		int i = 0, n = 0;

    n = operand.getColumn(keys.get(0)).size();

    while (i < n) {
			boolean flag = true;
				
			  
			for (String key : operand.getAttributes()) {
			flag &= condition.compare(key, operand.getColumn(key).get(i));
			}
			if((condition.column != null))
			  flag &= operand.getAttributes().contains(condition.column);
      
			if (flag) {
				operand.updateRow(i, newValues);
				updateCount ++;
			}

			i++;
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
	public void setAttributes(HashMap<String, Object> attr) {
		if (attr == null) {
			logger.fail("No attributes are set");
			throw new RuntimeException();
		}
		this.attr = attr;
		
//		System.out.println("keys "+attr.keySet());
//		System.out.println("vals "+attr.values());

	}

	@Override
	public void setCondition(Condition condition) {
		this.condition = condition;

		//System.out.println((condition.column) + "c");

	}
/*
	public static void main(String[] args) {

		List<String> atts = new ArrayList<String>();
		atts.add("Name");
		atts.add("Age");
		atts.add("Id");

		Table test = new Table(atts);
		HashMap<String, Object> row = new HashMap<String, Object>();
		row.put("Name", "Ahmed");
		row.put("Age", 20);
		row.put("Id", 15);
		test.insertRow(row);
		row = new HashMap<String, Object>();
		row.put("Name", "Amr");
		row.put("Age", 30);
		row.put("Id", 16);
		test.insertRow(row);
		row = new HashMap<String, Object>();
		row.put("Name", "Adham");
		row.put("Age", 40);
		row.put("Id", 25);
		test.insertRow(row);
		Update query = new Update();
		query.setTable(test);
		query.setCondition(new Greater("Id", "10"));
		row = new HashMap<String, Object>();
		row.put("Id", 50);
		query.setAttributes(row);
		test = query.execute();
		List<String> names = test.getAttributes();
		for (int i = 0; i < names.size(); i++) {
			System.out.println((names.get(i)));
		}
		List<Object> cols = test.getColumn("Id");
		for (int i = 0; i < cols.size(); i++) {
			System.out.println((cols.get(i)));
		}

	}
*/

  @Override
  public int updateCount() {
    return updateCount;
  }
}
