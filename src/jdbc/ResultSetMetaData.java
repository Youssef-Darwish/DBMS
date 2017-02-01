package jdbc;
 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
 
public class ResultSetMetaData implements java.sql.ResultSetMetaData {
    HashMap <String,Object> needs;
    ArrayList<String> list;
   
    public ResultSetMetaData(HashMap<String,Object> h){
        needs = h;
       
    }
 
    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }
 
    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        throw new UnsupportedOperationException();
   
    }
 
    @Override
    public String getCatalogName(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
       
    }
 
    @Override
    public String getColumnClassName(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
       
    }
 
   
 
    @Override
    public int getColumnDisplaySize(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
       
    }
   
    @Override
    public int getColumnCount() throws SQLException {
        int count = (int) needs.get("ColumnCount");
        return count;
    }
 
    @Override
    public String getColumnLabel(int column) throws SQLException {
        list = (ArrayList) needs.get("ColumnName");
        return list.get(column-1);
    }
 
    @Override
    public String getColumnName(int column) throws SQLException {
        list = (ArrayList) needs.get("ColumnName");
       
        return list.get(column-1);
    }
 
    @Override
    public int getColumnType(int column) throws SQLException {
        ArrayList<Integer> List = (ArrayList) needs.get("ColumnType");
        return List.get(column-1);
    }
   
    @Override
    public String getTableName(int column) throws SQLException {
        String tableName = (String) needs.get("TableName");
        return tableName;
    }
 
    @Override
    public String getColumnTypeName(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }
 
    @Override
    public int getPrecision(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }
 
    @Override
    public int getScale(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
   
    }
 
    @Override
    public String getSchemaName(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }
 
   
 
    @Override
    public boolean isAutoIncrement(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }
 
    @Override
    public boolean isCaseSensitive(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }
 
    @Override
    public boolean isCurrency(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }
 
    @Override
    public boolean isDefinitelyWritable(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }
 
    @Override
    public int isNullable(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
   
    }
 
    @Override
    public boolean isReadOnly(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
   
    }
 
    @Override
    public boolean isSearchable(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
 
    }
 
    @Override
    public boolean isSigned(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }
 
    @Override
    public boolean isWritable(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }
 
}