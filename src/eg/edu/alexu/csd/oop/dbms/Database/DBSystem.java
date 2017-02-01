package eg.edu.alexu.csd.oop.dbms.Database;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eg.edu.alexu.csd.oop.dbms.query.AlterQuery;
import eg.edu.alexu.csd.oop.dbms.query.CreateQuery;
import eg.edu.alexu.csd.oop.dbms.query.DdlQuery;
import eg.edu.alexu.csd.oop.dbms.query.IQuery;
import eg.edu.alexu.csd.oop.dbms.query.SelectQuery;

public class DBSystem implements IDBMS {
  private File workSpace;
  private ISQLParser parser;
  private IQuery sqlQuery;
  private Table check;
  private HashMap<String, IDataBase> databases;
  private IDataBase current;
  private Pattern createPattern;
  private Pattern dropDataBasePattern;
  private Pattern dropTablePattern;
  private Matcher matchCreate;
  private Matcher matchDropDB;
  private Matcher matchDropTable;
  private int updateCount;
  private Class<?> writerClass;
  private Log4j logger;

  public DBSystem() {
    updateCount = -1;
    databases = new HashMap<String, IDataBase>();
    createPattern = Pattern.compile("create\\s+database\\s+([a-z_0-9]+)\\s*");
    dropDataBasePattern = Pattern.compile("drop\\s+database\\s+(\\S+)\\s*");
    dropTablePattern = Pattern.compile("drop\\s+table\\s+(\\S+)\\s*\\s*");
    parser = new TreeParser();
    logger  = new Log4j();

  }

  public DBSystem(File workspace) {
    updateCount = -1;
    databases = new HashMap<String, IDataBase>();
    workSpace = workspace;
    workSpace.mkdir();
    createPattern = Pattern.compile("create\\s+database\\s+([a-z_0-9]+)\\s*");
    dropDataBasePattern = Pattern.compile("drop\\s+database\\s+(\\S+)\\s*");
    dropTablePattern = Pattern.compile("drop\\s+table\\s+(\\S+)\\s*\\s*");
    parser = new TreeParser();
    logger = new Log4j();
  }
  

  @Override
  public void setWorkSpace(File workspace) {
    workSpace = workspace;
    workSpace.mkdir();

  }

  public void setWriterClass(Class<?> writerClass) {
    this.writerClass = writerClass;
  }

  @Override
  public void createDB(String name) throws InstantiationException, IllegalAccessException {
    //System.out.println(".. here");
    File newDB = new File(workSpace, name);

    IDataBase newDataBase = new DataBaseO(newDB, writerClass);
    databases.put(name, newDataBase);
    //current = newDataBase;
  }

  @Override
  public void dropDB(String name) {
    IDataBase temp = databases.get(name);
    if (current == temp) {
      current = null;
    }
    if(temp !=null)
      temp.remove();
    databases.remove(name);
  }

  @Override
  public void switchDB(String name) throws RuntimeException {
    IDataBase temp = databases.get(name);
    if (temp == null) {
    	logger.fail("Database not found!");
      throw new RuntimeException();
    }
    current = databases.get(name);
  }

  @Override
  public List<String> databaseNames() {
    List<String> toreturn = new ArrayList<String>();
    toreturn.addAll(databases.keySet());
    return toreturn;
  }

  @Override
  public List<String> tableNames() {
    if (current == null) {
      return null;
    }
    return current.allTables();
  }

  @Override
  public String pushQuery(String query) {
    Table toReturn = null;
    try {
      toReturn = executeQuery(query);
    } catch (Exception e) {
      e.printStackTrace();
      return e.getMessage()+" error";
    }
    if (toReturn == null) {
      if(query.matches("create\\s+database\\s+(\\S+)\\s*"))
        return "database created";
      else if (query.matches("^\\s*create\\s+table\\s+(\\S+)\\s*\\s*"))
        return "table pushed to database";
      else if(query.matches("^\\s*drop\\s+database\\s+(\\w+)\\s*\\s*")) 
        return "database dropped";
      else if(query.matches("^\\s*drop\\s+table\\s+(\\S+)\\s*\\s*"))
        return "table dropped";
      else return "";
    }
    
    return toReturn.getAttributes().size()+" no2at";
  }

  public IDataBase getCurrent() {
    return this.current;
  }

  @Override
  public IDataBase getDataBase(String name) {

    return databases.get(name);
  }

  @Override
  public ITable getTable(String name) {
    if (current == null) {
      return null;
    }
    return current.getTable(name);
  }

  @Override
  public void setWorkSpace(String workspace) {
    workSpace = new File(workspace);

  }

  @Override
  public Table executeQuery(String query) throws Exception {
    matchCreate = createPattern.matcher(query);
    matchDropDB = dropDataBasePattern.matcher(query);
    matchDropTable = dropTablePattern.matcher(query);
    
    if (matchDropDB.matches()) {
      dropDB(matchDropDB.group(1));
      return null;
    }else if(query.matches("use\\s+(.*)")) {
      query = query.replace("use","");
      query =   query.trim();
      if(!databases.containsKey(query)) {
    	  logger.fail("Database NOT FOUND");
    	  throw new RuntimeException();  
      }
        
      switchDB(query);
      return null;
      
    }
    if (matchCreate.matches()) {
      createDB(matchCreate.group(1));
      return null;
    }   else if (matchDropTable.matches()) {
      if(current !=null)
      current.popTable(matchDropTable.group(1));
      return null;
    }
    
      sqlQuery = parser.parse(query);
    
     check = (Table) current.getTable(sqlQuery.getTableName());
     sqlQuery.setTable(check);
    if (sqlQuery instanceof DdlQuery) {
      Table tt = sqlQuery.execute();
      tt.setTableName(sqlQuery.getTableName());
      current.pushTable(sqlQuery.getTableName(), tt);
      current.SaveTable(sqlQuery.getTableName());
      return null;
    }
    try {
      check = sqlQuery.execute();
    }
    catch(Exception e) {
      throw new RuntimeException(e.getMessage() );
    }
      updateCount = sqlQuery.updateCount();
      current.SaveTable(sqlQuery.getTableName());
    
    return check;
  }
  public int getUpdateCount() {
    return updateCount;
  }

}