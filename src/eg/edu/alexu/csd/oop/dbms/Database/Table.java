package eg.edu.alexu.csd.oop.dbms.Database;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Iterator;
import java.util.Set;

//import dnl.utils.text.table.TextTable;

public class Table implements ITable {
    private HashMap<String, List<Object>> tableMap;
    private List<String> attributes;
    private HashMap<String, String> defaults;
    private String tableName;
    private String[][] printedTable;
    private static String[] types = { "varchar", "int", "date", "float" };
    private int rowCursor;
    private Log4j logger;

    // ResultSet object maintains a cursor pointing to its current row of data
    // initially positioned before the first row

    /**
     * creates empty table.
     */
    public Table() {
    	
    }

    public void setTableName(String name) {
        tableName = name;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTypeOf(String key) {
        return defaults.get(key);
    }

    /**
     * creates table with given column names.
     * @param columns list of columns.
     */
    public Table(List<String> columns) {
        this.rowCursor = -1;
        tableMap = new HashMap<String, List<Object>>();
        defaults = new HashMap<String, String>();
        attributes = columns;
        for (int i = 0; i < columns.size(); i++) {
            tableMap.put(columns.get(i), new ArrayList<Object>());
            defaults.put(columns.get(i), "both");
            logger = new Log4j();
        }
    }

    public Table(HashMap<String, String> columns) {
        this.rowCursor = -1;
        List<String> atts = new ArrayList<String>();
        defaults = new HashMap<String, String>();
        atts.addAll(columns.keySet());
        logger = new Log4j();
        tableMap = new HashMap<String, List<Object>>();
        attributes = atts;
        for (int i = 0; i < atts.size(); i++) {
            tableMap.put(atts.get(i), new ArrayList<Object>());

            defaults.put(atts.get(i), columns.get(atts.get(i)));
            boolean b = false;
            for (int j = 0; j < types.length; j++) {
                b |= types[j].equals(columns.get(atts.get(i)));
            }
            if (!b) {
                throw new RuntimeException("type not supported");
            }
        }
    }

    /*
     * get the column by index
     */

    public List<Object> getColumn(int index) {
        return tableMap.get(this.getAttributes().get(index));
    }

    /*
     * get the column by name
     */

    @Override
    public List<Object> getColumn(String name) {
        return tableMap.get(name);
    }

    @Override
    public void insertRow(List<String> newRow) throws RuntimeException {
        if (newRow.size() > attributes.size()) {
        	logger.fail("Columns numbers does no match");
            throw new RuntimeException();
        }
        int i = 0;
        for (i = 0; i < newRow.size(); i++) {
            List<Object> list = tableMap.get(attributes.get(i));
            if (defaults.get(attributes.get(i)).equals("int")) {
                int tint = Integer.valueOf(newRow.get(i++));
                list.add(tint);
            } else
                list.add(newRow.get(i++));
        }
    }

    public void insertRow(HashMap<String, Object> newRow)
            throws RuntimeException {
        for (String col : attributes) {
            
            tableMap.get(col).add(newRow.get(col));
        }
    }

    public void insertRowSure(HashMap<String, Object> newRow) {
        for (String col : attributes) {

            tableMap.get(col).add(newRow.get(col));

        }
    }

    @Override
    public void updateRow(int index, HashMap<String, Object> newRowV)
            throws RuntimeException, ArrayIndexOutOfBoundsException {
        for (String key : newRowV.keySet()) {
            if (!attributes.contains(key))
            {
            	logger.fail("invalid column name!");
            	throw new RuntimeException();
            }
                
        }
        for (String key : attributes) {
            if (newRowV.get(key) == null) {
                continue;
            } else {
                tableMap.get(key).set(index, newRowV.get(key));

            }
        }
    }

    public void orderatts(List<String> ats) {
        if (ats.size() != attributes.size())
            return;
        for (String att : ats) {
            if (!attributes.contains(att))
                return;
        }
        attributes = ats;
    }

    @Override
    public void deleteRow(int index) throws ArrayIndexOutOfBoundsException {
        for (List<Object> list : tableMap.values()) {
            list.remove(index);
        }
    }

    public HashMap<String, Object> getRow(int index) {
        HashMap<String, Object> row = new HashMap<String, Object>();
        for (String name : attributes) {
            row.put(name, tableMap.get(name).get(index));
        }
        return row;
    }

    @Override
    public List<String> getAttributes() {
        return attributes;
    }

    public String toString() {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();   
//        PrintStream outStrem = null;
//          outStrem = new PrintStream(baos);
//        
//
//        int row = this.getColumn(this.getAttributes().get(0)).size();
//        int column = this.getAttributes().size();
//        printedTable = new String[row][column];
//        String[] printedAttributes = new String[column];
//
//        for (int i = 0; i < column; i++) {
//            printedAttributes[i] = this.attributes.get(i);
//        }
//        for (int i = 0; i < row; i++) {
//
//            for (int j = 0; j < column; j++) {
//                Object ob = this.tableMap.get(this.getAttributes().get(j)).get(
//                        i);
//                String s;
//                if (ob != null)
//                    s = this.tableMap.get(this.getAttributes().get(j)).get(i)
//                            .toString();
//                else
//                    s = "null";
//                printedTable[i][j] = s;
//            }
//        }
////        TextTable t = new TextTable(printedAttributes, printedTable);
////        t.printTable(outStrem, 0);
////        String tore = baos.toString(); 
//        //System.out.println("table aho"+tore+"end");
//        return tore;
        return null;
    }

}
