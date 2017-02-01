package eg.edu.alexu.csd.oop.dbms.Database;

import java.sql.Date;

public class Greater extends Condition {

  private String base;

  public Greater(String colName,String string) {
      super(colName);
      this.base = string;
      
    }

  @Override
  public boolean compare(String col, Object inst) {
    
    if(inst instanceof Integer){
      return (!col.equals(column) || ((Integer) inst > Integer.valueOf(base)));
    } else if(inst instanceof Double){
      return (!col.equals(column) || ((Double) inst > Double.valueOf(base)));
    } else if(inst instanceof Date){
      return (!col.equals(column) || (((Date) inst).after(Date.valueOf(base))));
    }
    else
      return  (!col.equals(column) || (base.compareTo((String)inst) <0));
    
  }

}
