package eg.edu.alexu.csd.oop.dbms.Database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DataBaseO implements IDataBase {

    private File path;
    private HashMap<String, Table> tables;
    private IFileBehaviour objectWriter;
    private File file;
    protected String fileType = "xml";
    public static Class<?> writerClass = XML.class;
    private Log4j logger;

    public DataBaseO(File path) throws InstantiationException, IllegalAccessException {
        this.path = path;
        tables = new HashMap<String, Table>();
        this.path.mkdir();
        File tabk = new File(this.path, "tables.out");
        logger = new Log4j();
        objectWriter = (IFileBehaviour) writerClass.newInstance();
        try {
            if (!tabk.exists())
                tabk.createNewFile();
            file = tabk;
            loadDb(file);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }
    public DataBaseO(File path, Class<?> writerClass) throws InstantiationException, IllegalAccessException {
      this.path = path;
      tables = new HashMap<String, Table>();
      this.path.mkdir();
      File tabk = new File(this.path, "tables.out");
      if(writerClass == null)
        writerClass = XML.class;
      objectWriter = (IFileBehaviour) writerClass.newInstance();
      logger = new Log4j();
      try {
          if (!tabk.exists())
              tabk.createNewFile();
          file = tabk;
          loadDb(file);
      } catch (Exception e) {
          System.out.println("atabase path is"+path + " "+path.exists());
          e.printStackTrace();
      }

  }

    private void loadDb(File tabk) throws Exception {
        Scanner inps = new Scanner(tabk);
        while (inps.hasNextLine()) {
            String tab = inps.nextLine().trim();
            // System.out.println("ss " +path + " "+ tab);
            LoadTable(tab);

        }
        inps.close();

    }

    @Override
    public void pushTable(String name, Table table) {
        tables.put(name, table);
        saveTableNames();
    }

    private void saveTableNames() {
        try {
            FileWriter fileW = new FileWriter(file);
            for (String t : tables.keySet())
                fileW.write(t + "\n");

            fileW.close();
        } catch (IOException e) {}

    }

    @Override
    public ITable popTable(String name) {

        File tod = new File(path, name + ".xml");
        tod.delete();
        tod = new File(path, name + ".dtd");
        tod.delete();
        Table temp = tables.remove(name);
        saveTableNames();
        return temp;
    }

    @Override
    public ITable getTable(String name) {
        return tables.get(name);

    }

    @Override
    public File getPath() {
        return this.path;
    }

    @Override
    public List<String> allTables() {
        List<String> toreturn = new ArrayList<String>();
        toreturn.addAll(tables.keySet());
        return toreturn;
    }

    @Override
    public void SaveTable(String name) throws Exception {

        try {
            objectWriter.setTable(tables.get(name));
            objectWriter.saveTable(path);
                objectWriter.saveTableSchema(path);
            

        } catch (Exception e) {
            e.printStackTrace();
            logger.fail("Error in saving file!");
            throw new Exception("Error in saving file!");
        }

    }

    @Override
    public void LoadTable(String name) throws Exception {
     
        String tableName = name + "." + objectWriter.getExtension();
        Table toReturn  ;//= tables.get(name);
        
       
        try {
            objectWriter.loadTable(new File(path, tableName));
            toReturn = objectWriter.getTable();

        } catch (Exception e) {
        	logger.fail("Failed to load");
           
        }
        toReturn = objectWriter.getTable();
        toReturn.setTableName(name);
        tables.put(name, toReturn);
        

    }

    @Override
    public void remove() {
        List<String> tanme = new ArrayList<String>();
        tanme.addAll(tables.keySet());
        for (String tab : tanme)
            popTable(tab);
        File tod = new File(path, "tables.out");
        tod.delete();
        // tod.deleteOnExit();
        // System.out.println(tod.exists() + " "+tod.delete());

        path.delete();

    }

}
