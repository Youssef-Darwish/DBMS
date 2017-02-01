package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import eg.edu.alexu.csd.oop.dbms.Database.DBSystem;
import eg.edu.alexu.csd.oop.dbms.Database.DataBaseO;
import eg.edu.alexu.csd.oop.dbms.Database.Table;

public class DeletionTest {
    Table loadedTable;
    DataBaseO DBTest;
    DBSystem dataBase;
    Table t;
    HashMap<String, Object> Rows;
    HashMap<String, String> attributes = new HashMap<String, String>();
    
    public DeletionTest() {
    }
    
    public void createTable() {
        
        attributes.put("Name", "varchar");
        attributes.put("Age", "int");
        attributes.put("Address", "varchar");
        attributes.put("number", "varchar");
        t = new Table(attributes);
        t.setTableName("Students");
        
        
        addRow("Name", "Age", "Address", "number", "'Haya'", "20","'anywhere'", "'1234'");
        addRow("Name", "Age", "Address", "number", "'Rowan'", "21","'everywhere'","'1234'");
        addRow("Name", "Age", "Address", "number", "'Haya'", "2021","'anywhere'", "'1234'");
        addRow("Name", "Age", "Address", "number", "'Rowana'", "21","'everywhere'","'1234'");
        addRow("Name", "Age", "Address", "number", "'Haya'", "200","'anywhere'", "'1234'");
        
        try{
            dataBase.getCurrent().SaveTable("Students");
            }
            catch(Exception e){
              
            }
 
    }
    
    void addRow(String name, String age, String address, String number,
            String nameValue, String ageValue, String addressValue,
            String numberValue) {
        Rows = new HashMap<>();
        Rows.put(name, nameValue);
        Rows.put(age,ageValue);
        Rows.put(address, addressValue);
        Rows.put(number, numberValue);
       
        t.insertRowSure(Rows);
    }
    
    @Test
    public void testInsert(){
        dataBase = new DBSystem(new File("Hello"));
        try {
          dataBase.createDB("myData");
          dataBase.switchDB("myData");
        } catch (InstantiationException | IllegalAccessException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        File path = dataBase.getCurrent().getPath();
        createTable();
        
        File check = new File(path,t.getTableName()+".xml");
        assertTrue(check.exists());
    }
    
    
}