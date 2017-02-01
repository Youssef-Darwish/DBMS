package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import eg.edu.alexu.csd.oop.dbms.Database.ISQLParser;
import eg.edu.alexu.csd.oop.dbms.Database.TreeParser;
import eg.edu.alexu.csd.oop.dbms.query.DeleteRow;
import eg.edu.alexu.csd.oop.dbms.query.IQuery;
import eg.edu.alexu.csd.oop.dbms.query.Insert;
import eg.edu.alexu.csd.oop.dbms.query.SelectQuery;

public class ParserTest {

  ISQLParser parser = new TreeParser();
  @Test
  public void testselect() throws Exception {
    IQuery query = parser.parse("select some from _tname where dog='y'");
    assertEquals(query instanceof SelectQuery ,true);
    assertEquals(query.getTableName(),"_tname");
  }
  @Test(expected=RuntimeException.class)
  public void testwrong() throws Exception {
	    IQuery query = parser.parse("select some from 6_tname where dog=y");
    
  }
  @Test
  public void testinsert() throws Exception {
    IQuery query = parser.parse("insert into _forn values ('bread','cakes','muffins')");
    assertEquals(query instanceof Insert ,true);
    assertEquals(query.getTableName(),"_forn");
  }
  @Test
  public void testdelete() throws Exception {
    IQuery query = parser.parse("delete from _tname where dog='y';");
    assertEquals(query instanceof DeleteRow,true);
    assertEquals(query.getTableName(),"_tname");
  }
  

}
