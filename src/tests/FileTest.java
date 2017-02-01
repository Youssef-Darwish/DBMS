package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import eg.edu.alexu.csd.oop.dbms.Database.DataBaseO;
import eg.edu.alexu.csd.oop.dbms.Database.Table;

public class FileTest {

    @Test
    public void testSave() {
        
       // List<String> attributes = new ArrayList<String>();
        HashMap < String,String> attributes = new HashMap < String,String>(); 
        attributes.put("Name","varchar");
        attributes.put("Age","int");
        attributes.put("Address","varchar");
        attributes.put("number","int");
        Table t = new Table(attributes);
        t.setTableName("Pupils");
       // t.toString();
        
        HashMap<String,Object> Rows = new HashMap<>();
        Rows.put("Name", "'Haya'");
        Rows.put("Age", 20);
        Rows.put("Address", "'anywhere'");
        Rows.put("number", 1234);
        t.insertRow(Rows);
        Rows = new HashMap<>();
        Rows.put("Name", "'Rowan'");
        Rows.put("Age", 25);
        Rows.put("Address", "'everywhere'");
        Rows.put("number", 1234);
        t.insertRow(Rows);
        Rows = new HashMap<>();
        Rows.put("Name", "'Ahmed'");
        Rows.put("Age", 22);
        Rows.put("Address", "'somewhere'");
        Rows.put("number", 1234);
        t.insertRow(Rows);
        Rows = new HashMap<>();
        Rows.put("Name", "'Youssef'");
        Rows.put("Age", 23);
        Rows.put("Address", "null");
        Rows.put("number", 1234);
        t.insertRow(Rows);
        Rows = new HashMap<>();
        Rows.put("Name", "'Amira'");
        Rows.put("Age", 24);
        Rows.put("Address", "'there'");
        Rows.put("number", 1234);
        t.insertRow(Rows);
        DataBaseO object = null;
        try {
          object = new DataBaseO(new File("Students"));
        } catch (InstantiationException | IllegalAccessException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
        //t.toString();
        object.pushTable("Pupils", t);
    
        try {
             object.SaveTable("Pupils");
             object.LoadTable("Pupils");
       //      object.getTable("Pupils").toString();
     //       t.deleteRow(0);
//            tableLoaded = object.LoadTable("Pupils");
//            tableLoaded.toString();
//            assertEquals("Not Match",t.getAttributes().size(), tableLoaded.getAttributes().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
  
    }

}
