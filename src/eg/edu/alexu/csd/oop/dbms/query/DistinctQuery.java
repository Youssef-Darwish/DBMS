package eg.edu.alexu.csd.oop.dbms.query;

import java.util.HashMap;
import java.util.HashSet;
import eg.edu.alexu.csd.oop.dbms.Database.Table;
import eg.edu.alexu.csd.oop.dbms.Database.Log4j;
public class DistinctQuery extends SelectQuery {
  
  private HashSet<String> duplicates;
  private Log4j logger;
  /**
   * creates new select query.
   */
  public DistinctQuery() {
    duplicates = new HashSet<String>();
    logger = new Log4j();
  }
  private String stringify(HashMap<String,Object> row) {
    String val= "";
    for (String key:row.keySet() )
      val+=row.get(key);
    return val;
  }
  
  @Override
  public Table execute() {
    Table toreturn  = super.execute();
    
    int n = toreturn.getColumn(0).size();
    
    for(int i = 0 ; i<n ; i++) {
      HashMap<String,Object> nrow = toreturn.getRow(i);
      String hash = stringify(nrow);
      if(duplicates.contains(hash)){
        toreturn.deleteRow(i);
        i--;
        n--;
      } else {
        duplicates.add(hash);
      }
    }
    return toreturn;
  }

  

}
