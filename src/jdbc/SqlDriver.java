package jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import eg.edu.alexu.csd.oop.dbms.Database.DBSystem;
import eg.edu.alexu.csd.oop.dbms.Database.Json;
import eg.edu.alexu.csd.oop.dbms.Database.XML;

public class SqlDriver implements Driver {

  public SqlDriver(){
    
  }
  @Override
  public boolean acceptsURL(String url) throws SQLException {
    return url.matches("jdbc:(jsondb|xmldb)://localhost");
  }

  @Override
  public Connection connect(String url, Properties info) throws SQLException {
//    if(!acceptsURL(url))
//      return null;
    File workSpace = (File) info.get("path");
    DBSystem dbms = new DBSystem(workSpace);
    if(url.contains("xmldb"))
      dbms.setWriterClass(XML.class);
    else
      dbms.setWriterClass(Json.class);
    SqlConnection newCon = new SqlConnection(dbms);
    return newCon;
  }

  @Override
  public int getMajorVersion() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getMinorVersion() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new UnsupportedOperationException();
  }

  @Override
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
    if(!acceptsURL(url))
      return null;
    
    DriverPropertyInfo[] reqInfo = new DriverPropertyInfo[4];
    reqInfo[0] = new DriverPropertyInfo("username", info.getProperty("username"));

    reqInfo[1] = new DriverPropertyInfo("password", info.getProperty("password"));

    reqInfo[2] = new DriverPropertyInfo("path", info.getProperty("path"));

    reqInfo[3] = new DriverPropertyInfo("parser type", (url.contains("xml"))?"XML":"JSON");
    return reqInfo;
  }

  @Override
  public boolean jdbcCompliant() {
    throw new UnsupportedOperationException();
  }
  public static void main(String args[]) throws SQLException {
    System.out.println(new SqlDriver().acceptsURL("jdbc:jsondb://localhost"));
  }

}
