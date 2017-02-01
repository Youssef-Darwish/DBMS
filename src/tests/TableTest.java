package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import eg.edu.alexu.csd.oop.dbms.Database.DataBaseO;
import eg.edu.alexu.csd.oop.dbms.Database.Table;

public class TableTest {

    Table loadedTable;
    DataBaseO DBTest;
    Table t;
    HashMap<String, Object> Rows;
    List<String> attributes;

    public TableTest() {
        try {
          DBTest = new DataBaseO(new File("Alexandria University"));
        } catch (InstantiationException | IllegalAccessException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
    }
    
    public void createTable() {
        attributes = new ArrayList<String>();
        attributes.add("Name");
        attributes.add("Age");
        attributes.add("Address");
        attributes.add("number");
        t = new Table(attributes);
        t.setTableName("Students");

        addRow("Name", "Age", "Address", "number", "'Haya'", "201","'anywhere'", "'1234'");
        addRow("Name", "Age", "Address", "number", "'Haya'", "220","'anywhere'", "'1234'");
        addRow("Name", "Age", "Address", "number", "'Haya'", "201132","'anywhere'", "'1234'");
        addRow("Name", "Age", "Address", "number", "'Haya'", "209","'anywhere'", "'1234'");
        addRow("Name", "Age", "Address", "number", "'Haya'", "205456","'anywhere'", "'1234'");
        
        DBTest.pushTable("Students", t);

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
    public void testTableFunctionalities() {

        createTable();
        assertTrue(t.getColumn("Age").size() == 5);
        Rows = new HashMap<>();
        Rows.put("Name", "'Amira'");
        Rows.put("Age", "30");
        Rows.put("Address", "'there'");
        Rows.put("number", "'1'");
        t.updateRow(4, Rows);
        assertTrue(t.getColumn("Age").size() == 5);
        assertEquals(t.getColumn("Age").get(4), "30");
        t.deleteRow(3);
        assertTrue(t.getColumn("Age").size() == 4);
        assertEquals(t.getColumn("Age").get(3), "30");
    }
    
    @Test(expected=Exception.class)
    public void testExceptions(){
        t.getColumn("Car");
        t.deleteRow(7);
        t.getColumn("Me").get(1);
        t.deleteRow(0);
        t.getRow(4);
        
    }

}