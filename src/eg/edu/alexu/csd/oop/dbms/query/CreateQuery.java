package eg.edu.alexu.csd.oop.dbms.query;

import java.util.HashMap;
import java.util.List;

import eg.edu.alexu.csd.oop.dbms.Database.Condition;
import eg.edu.alexu.csd.oop.dbms.Database.Table;
import eg.edu.alexu.csd.oop.dbms.Database.Log4j;
public class CreateQuery implements IQuery,DdlQuery {

  private String tableName;
  private Table tocopy;
  private HashMap<String , String> columns;
  private List<String> atts;
  private Log4j logger;
  public CreateQuery() {
    tocopy = null;
    columns = new HashMap<String , String>();
    logger = new Log4j();
  }
  @Override
  public Table execute() {
    Table toreturn ;
    if (tocopy != null) {
    	logger.fail("Table already exists");
      throw new RuntimeException();
    }
    else toreturn =  new Table(columns);
    toreturn.orderatts(atts);
    return toreturn;
   
  }
  @Override
  public void setTable(String tabName) {
    tableName = tabName;

  }

  @Override
  public void setTable(Table table) {
    tocopy = table;

  }

  @Override
  public String getTableName() {
    return tableName;
  }

  @Override
  public void setAttributes(HashMap<String, Object> attr) {
    columns = new HashMap<String,String>();
    for(String key : attr.keySet()) {
      columns.put(key, (String)attr.get(key));
    }

  }
  
  public void setAttributes(List<String> attrs) {
    atts  = attrs;
  }

  @Override
  public void setCondition(Condition condition) {
    //TODO : Use this function to copy tables conidtionally :') 
    return ;

  }
  @Override
  public int updateCount() {
    return -1;
  }

}
