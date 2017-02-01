package tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import eg.edu.alexu.csd.oop.dbms.Database.Table;
import jdbc.ResultSetMetaData;
import jdbc.Resultset;

public class MetaDataTest {
    private Table table;
    private HashMap<String, String> atts;
    private HashMap<String, Object> row;
    ArrayList<String> Types;
    ResultSetMetaData metaDataObject;
    Resultset resultSet;
    HashMap<String, Object> data;

    public MetaDataTest() {
        atts = new HashMap<String, String>();

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
        row.put("Name", "Ahmed");
        row.put("Age", 20);
        row.put("Id", 15);
        row.put("Date", "2012-12-30");
        row.put("Floats", 2.23);
        table.insertRowSure(row);

        row = new HashMap<String, Object>();
        row.put("Name", "Amr");
        row.put("Age", 30);
        row.put("Id", 16);
        row.put("Date", "15-12-5422");
        row.put("Floats", 4.6);
        table.insertRow(row);

        row = new HashMap<String, Object>();
        row.put("Name", "Adham");
        row.put("Age", 40);
        row.put("Id", 25);
        row.put("Date", "45-48-1205");
        row.put("Floats", 45.98);
        table.insertRowSure(row);

        row = new HashMap<String, Object>();
        row.put("Name", "Mohamed");
        row.put("Age", 34);
        row.put("Id", 12);
        row.put("Date", "2-5-2511");
        row.put("Floats", 1.25);
        table.insertRowSure(row);
        table.setTableName("table");
        data = new HashMap<>();
        data.put("ColumnName", table.getAttributes());
        data.put("ColumnCount", table.getAttributes().size());
        List<Integer> Types = new ArrayList<Integer>();
        for (int i = 0; i < table.getAttributes().size(); i++) {
            String type = table.getTypeOf(table.getAttributes().get(i));
            Types.add(dataType(type));
        }
        data.put("ColumnType", Types);
        data.put("TableName", table.getTableName());
        metaDataObject = new ResultSetMetaData(data);

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
    public void getTableNameTest() throws SQLException {
        add();
        assertEquals(metaDataObject.getTableName(2), "table");
    }

    @Test
    public void getColumnTypeTest() throws SQLException {
        add();
        assertEquals(metaDataObject.getColumnType(1), java.sql.Types.VARCHAR);
    }

    @Test
    public void getColumnNameTest() throws SQLException {
        add();
        assertEquals(metaDataObject.getColumnName(1), "Name");
    }

    @Test
    public void getColumnLabelTest() throws SQLException {
        add();

        assertEquals(metaDataObject.getColumnLabel(1), "Name");
    }

    @Test
    public void getColumnCountTest() throws SQLException {
        add();

        assertEquals(metaDataObject.getColumnCount(), 5);
    }

}
