package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import eg.edu.alexu.csd.oop.dbms.Database.DBSystem;
import eg.edu.alexu.csd.oop.dbms.Database.Table;
import jdbc.ResultSetMetaData;
import jdbc.Resultset;
import jdbc.SqlConnection;
import jdbc.SqlStatement;

public class ResultSetTest {

    Table table;
    HashMap<String, String> atts;
    HashMap<String, Object> row;
    private DBSystem dbms;
    private SqlConnection conn;
    SqlStatement test;
    Resultset resultSet;
    
    public ResultSetTest() {
        
        atts = new HashMap<String, String>();
        dbms = new DBSystem(new File(System.getProperty("user.home"), "DBMS"));
        conn = new SqlConnection(dbms);
        test = new SqlStatement(conn, dbms);
        
    }
    
    public void add() throws SQLException {
        atts.put("Name", "varchar");
        atts.put("Age", "int");
        atts.put("Id", "int");
        atts.put("Date", "date");
        atts.put("Floats", "float");
        
        List<String> order = new ArrayList<String> ();
        order.add("Name");
        order.add("Age");
        order.add("Id");
        order.add("Date");
        order.add("Floats");

        table = new Table(atts);
        table.orderatts(order);
        row = new HashMap<String, Object>();
        row.put("Name", "'Ahmed'");
        row.put("Age", 20);
        row.put("Id", 15);
        row.put("Date", "2012-12-30");
        row.put("Floats", 2.23);
        table.insertRowSure(row);
        
        row = new HashMap<String, Object>();
        row.put("Name", "'Amr'");
        row.put("Age", 30);
        row.put("Id", 16);
        row.put("Date",  "2010-9-30");
        row.put("Floats", 4.6);
        table.insertRow(row);
        
        row = new HashMap<String, Object>();
        row.put("Name", "'Adham'");
        row.put("Age", 40);
        row.put("Id", 25);
        row.put("Date",  "2016-1-3");
        row.put("Floats", 45.98);
        table.insertRowSure(row);
        
        row = new HashMap<String, Object>();
        row.put("Name", "'Mohamed'");
        row.put("Age", 34);
        row.put("Id", 12);
        row.put("Date",  "2011-6-8");
        row.put("Floats", 1.25);
        table.insertRowSure(row);
        table.setTableName("table");
        resultSet = new Resultset(table, test);

    }

    @Test
    public void nextTest() throws SQLException {
        add();
        resultSet.absolute(2);
        resultSet.next();
        assertEquals(resultSet.getCursor()+1, 3);
        assertEquals(table.getColumn("Age").get(resultSet.getCursor()),40);
        assertTrue(table.getTypeOf("Date").equals("date"));
        resultSet.next();
        assertEquals(table.getColumn("Date").get(resultSet.getCursor()),"2011-6-8");
    }
    
    @Test(expected=Exception.class)
     public void exceptionTest() throws SQLException {
        resultSet.absolute(4);
        resultSet.next();
        resultSet.next();
        resultSet.absolute(1);
        assertTrue(table.getColumn(resultSet.getString(2)).equals(20));
        resultSet.close();
        resultSet.first();
        resultSet.findColumn("Cat");
    }

    @Test
    public void getStringTest() throws SQLException {
        add();
        resultSet.absolute(1);
        assertEquals(resultSet.getString(1),"'Ahmed'");
        resultSet.absolute(2);
        assertEquals(resultSet.getString("Name"), "'Amr'");
        
    }

    @Test 
    public void getFloatTest() throws SQLException{
        add();
        resultSet.absolute(3);
        assertTrue(resultSet.getFloat(5) <= 45.98);
        assertFalse(resultSet.getFloat("Floats") <= 1.25);
        
        }

    @Test
    public void getDateTest() throws SQLException{
        add();
        resultSet.absolute(4);
        assertTrue(resultSet.getDate("Date").equals(Date.valueOf("2011-6-8")));
           
    }

    @Test
    public void isClosedTest() throws SQLException{
        add();
        resultSet.absolute(2);
        resultSet.close();
        assertTrue(resultSet.isClosed());
    }

    @Test
    public void getStatementTest() throws SQLException{
        add();
        assertEquals(resultSet.getStatement(), test);
    }

    @Test 
    public void previousTest() throws SQLException{
        add();
        resultSet.absolute(2);
        resultSet.previous();
        assertEquals(resultSet.getCursor()+1, 1);
        assertEquals(table.getColumn("Age").get(resultSet.getCursor()),20);
         
    }

    @Test
    public void absoluteTest() throws SQLException{
      add();
      assertTrue(resultSet.absolute(2));
      assertFalse(resultSet.absolute(0));
      assertFalse(resultSet.absolute(resultSet.getMetaData().getColumnCount()));
    }

    @Test
    public void lastTest() throws SQLException{
        add();
        assertTrue(resultSet.last());
        assertTrue(resultSet.isLast());
        assertEquals(resultSet.getCursor(),table.getColumn(0).size()-1);
    }

    @Test
    public void firstTest() throws SQLException{
        add();
        assertTrue(resultSet.first());
        assertTrue(resultSet.isFirst());
        assertEquals(resultSet.getCursor(),0);
    }

    @Test
    public void afterLastTest() throws SQLException{
        add();
        resultSet.afterLast();
        assertTrue(resultSet.isAfterLast());
        assertEquals(resultSet.getCursor(), table.getColumn(0).size());
    }

    @Test
    public void beforeFirstTest() throws SQLException{
        add();
        resultSet.beforeFirst();
        assertTrue(resultSet.isBeforeFirst());
        assertEquals(resultSet.getCursor(), -1);
    }
    
    @Test
    public void findColumnTest() throws SQLException{
        add();
        assertEquals(resultSet.findColumn("Name"), 1);
    }

    @Test
    public void getObjectTest() throws SQLException{
        add();
        resultSet.absolute(1);
        assertEquals(resultSet.getObject(1),"'Ahmed'");
        assertEquals(resultSet.getObject(3),15);
        
    }

    int dataType(String type) {
        if (type.equals("int"))
            return java.sql.Types.INTEGER;
        else if (type.equals("float"))
            return java.sql.Types.FLOAT;
        else if (type.equals("varchar"))
            return java.sql.Types.VARCHAR;
        else
            return java.sql.Types.DATE;

    }
    
    @Test
    public void getMetaDataTest() throws SQLException{
       add();
       HashMap<String, Object> data = new HashMap<>();
       data.put("ColumnName", table.getAttributes());
       data.put("ColumnCount", table.getAttributes().size());
       List<Integer> Types = new ArrayList<Integer>();
       for (int i = 0; i < table.getAttributes().size(); i++) {
           String type = table.getTypeOf(table.getAttributes().get(i));
           Types.add(dataType(type));
       }
       data.put("ColumnType", Types);
       data.put("TableName", table.getTableName());
       assertEquals(resultSet.getMetaData().getTableName(3), data.get("TableName"));
    }

}

