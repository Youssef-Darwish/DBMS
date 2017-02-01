package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import eg.edu.alexu.csd.oop.dbms.Database.Condition;
import eg.edu.alexu.csd.oop.dbms.Database.DBSystem;
import eg.edu.alexu.csd.oop.dbms.Database.Equivalence;
import eg.edu.alexu.csd.oop.dbms.Database.Greater;
import eg.edu.alexu.csd.oop.dbms.Database.Table;
import eg.edu.alexu.csd.oop.dbms.query.DeleteRow;
import eg.edu.alexu.csd.oop.dbms.query.IQuery;
import eg.edu.alexu.csd.oop.dbms.query.Insert;
import eg.edu.alexu.csd.oop.dbms.query.SelectQuery;
import eg.edu.alexu.csd.oop.dbms.query.Update;

import java.util.*;
import java.io.*;

public class DBTest {

	private HashMap<String, String> atts = new HashMap<String, String>();
	private DBSystem db = new DBSystem();
	private Table checkTable;
	HashMap<String, Object> row = new HashMap<String, Object>();
	Insert query = new Insert();

	public DBTest() {

	}

	public void init(IQuery query)  {
	  db = new DBSystem(new File(System.getProperty("user.home")));
		try {
      db.createDB("database");
      db.switchDB("database");
  
    } catch (InstantiationException | IllegalAccessException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

		atts.put("Name", "varchar");
		atts.put("Age", "int");
		atts.put("Id", "int");

		checkTable = new Table(atts);

		row.put("Name", "'Ahmed'");
		row.put("Age", "20");
		row.put("Id", "15");
		checkTable.insertRow(row);
		row = new HashMap<String, Object>();
		row.put("Name", "'Amr'");
		row.put("Age", "30");
		row.put("Id", "16");
		checkTable.insertRow(row);
		row = new HashMap<String, Object>();
		row.put("Name", "'Adham'");
		row.put("Age", "40");
		row.put("Id", "25");
		checkTable.insertRow(row);
		query.setTable(checkTable);
		row = new HashMap<String, Object>();
		row.put("Name", "'Mohamed'");
		row.put("Age", "34");
		row.put("Id", "12");
		checkTable.insertRow(row);
		query.setAttributes(row);
		checkTable.setTableName("ayKalb");
    db.getCurrent().pushTable("ayKalb", checkTable);
    try{
    db.getCurrent().SaveTable("checkTable");
    }
    catch(Exception e){
      
    }
		// List<String> names = checkTable.getAttributes();
	}

	@Test
	public void test() {
		db.pushQuery("create database uy");
	//	File check = new File(db..toString());
		//assertTrue(check.exists() && check.isDirectory());
	}

	@Test
	public void selectionTest() throws Exception {

		IQuery select = new SelectQuery();
		init(select);
		select.setTable(checkTable);
		select.setTable("checkTable");
		select.setCondition(new Greater("Age", "30"));
		HashMap<String, Object> x = new HashMap<String, Object>();
		x.put("Name", null);
		x.put("Id", null);
		select.setAttributes(x);
		Table z = (Table) select.execute();
		assertEquals(z.getAttributes().size(), 2);

		assertEquals(z.getAttributes().contains("Name"), true);
		assertEquals(z.getAttributes().contains("Id"), true);
		assertEquals(z.getAttributes().contains("Age"), false);
		List<String> test = new ArrayList<String>();
		test.add("Adham");
		test.add("Mohamed");
		for (int i = 0; i < test.size(); i++)
			assertEquals(test.get(i), z.getColumn("Name").get(i));
		test.clear();
		List<Integer> test1 = new ArrayList<Integer>();
		test1.add(25);
		test1.add(12);
		for (int i = 0; i < test1.size(); i++) {
			assertEquals(test1.get(i), z.getColumn("Id").get(i));
		}
	}

	@Test
	public void selectAll() {

		IQuery select = new SelectQuery();
		init(select);
		select.setTable(checkTable);
		select.setTable("checkTable");
		select.setCondition(new Condition());
		HashMap<String, Object> x = new HashMap<String, Object>();
		x.put("*", null);
		select.setAttributes(x);
		Table z = (Table) select.execute();
		assertEquals(z.getAttributes().size(), 3);

		assertEquals(z.getAttributes().contains("Name"), true);
		assertEquals(z.getAttributes().contains("Id"), true);
		assertEquals(z.getAttributes().contains("Age"), true);
		List<String> test = new ArrayList<String>();
		test.add("Adham");
		test.add("Mohamed");
		for (int i = 0; i < test.size(); i++)
			// assertEquals(test.get(i), z.getColumn("Name").get(i));
			test.clear();
		List<Integer> test1 = new ArrayList<Integer>();
		test1.add(25);
		test1.add(12);
		for (int i = 0; i < test1.size(); i++) {
			// assertEquals(test1.get(i), z.getColumn("Id").get(i));
		}
		// System.out.println(z.toString());
	}

	@Test
	public void invalidDeletion() { // to be revised

		IQuery delete = new DeleteRow();
		init(delete);
		delete.setTable(checkTable);
		delete.setTable("checkTable");
		delete.setCondition(new Greater("Age", "50"));
		HashMap<String, Object> x = new HashMap<String, Object>();
		// x.put("Name", null);
		// x.put("Id", null);
		x.put("Agasde", null);
		delete.setAttributes(x);
		Table z = (Table) delete.execute();
		assertEquals(z.getAttributes().size(), 3);

		assertEquals(z.getAttributes().contains("Name"), true);
		assertEquals(z.getAttributes().contains("Id"), true);
		assertEquals(z.getAttributes().contains("Age"), true);
		List<String> test = new ArrayList<String>();
		test.add("Ahmed");
		test.add("Amr");
		for (int i = 0; i < test.size(); i++)
			// assertEquals(test.get(i), z.getColumn("Name").get(i));
			test.clear();
		List<Integer> test1 = new ArrayList<Integer>();
		test1.add(15);
		test1.add(16);
		for (int i = 0; i < test1.size(); i++) {
			// assertEquals(test1.get(i), z.getColumn("Id").get(i));
		}
		// System.out.println(DBTest.getCurrent().getTable("checkTable").toString());
		// System.out.println(DBTest.getCurrent().getTable("checkTable").toString());
	}

	@Test
	public void deleteTest() {

		IQuery delete = new DeleteRow();
		init(delete);
		delete.setTable(checkTable);
		delete.setTable("checkTable");
		delete.setCondition(new Equivalence("Ndsasame", "Amr"));
		HashMap<String, Object> x = new HashMap<String, Object>();
		delete.setAttributes(x);
		Table z = (Table) delete.execute();
		assertEquals(z.getAttributes().size(), 3);

		assertEquals(z.getAttributes().contains("Name"), true);
		assertEquals(z.getAttributes().contains("Id"), true);
		assertEquals(z.getAttributes().contains("Age"), true);
		List<String> test = new ArrayList<String>();
		test.add("Ahmed");
		test.add("Adham");
		test.add("Mohamed");
		for (int i = 0; i < test.size(); i++){}
		//	assertEquals(test.get(i), z.getColumn("Name").get(i));
	//	System.out.println(z.toString());
	//	System.out.println(DBTest.getCurrent().getTable("checkTable").toString());
	}

	@Test
	public void Insert() {

		IQuery insert = new Insert();
		init(insert);
		insert.setTable(checkTable);
		insert.setTable("checkTable");
		insert.setCondition(new Condition());
		HashMap<String, Object> x = new HashMap<String, Object>();
		x.put("Name", "'Aly'");
		x.put("Age", "50");
		x.put("Id", "32");
		insert.setAttributes(x);
		Table z = (Table) insert.execute();
		assertEquals(z.getAttributes().size(), 3);
		assertEquals(z.getAttributes().contains("Name"), true);
		assertEquals(z.getAttributes().contains("Id"), true);
		assertEquals(z.getAttributes().contains("Age"), true);
		assertArrayEquals(new Object[] { 20, 30, 40, 34, 50 }, z.getColumn("Age").toArray());
		assertArrayEquals(new Object[] { 15, 16, 25, 12, 32 }, z.getColumn("Id").toArray());
		//System.out.println(z.toString());
	}

	@Test
	public void Update() {

		IQuery Update = new Update();
		init(Update);
		Update.setTable(checkTable);
		Update.setTable("checkTable");
		Update.setCondition(new Equivalence("Name", "Mohamed"));
		HashMap<String, Object> x = new HashMap<String, Object>();
		x.put("Name", "'Aly'");
		x.put("Age", "50");
		x.put("Id", "32");
		Update.setAttributes(x);
		System.out.println(Update.getTableName().toString());
		Table z = (Table) Update.execute();
		assertEquals(z.getAttributes().size(), 3);

		assertEquals(z.getAttributes().contains("Name"), true);
		assertEquals(z.getAttributes().contains("Id"), true);
		assertEquals(z.getAttributes().contains("Age"), true);
	//	assertArrayEquals(new Object[] { "20", "30", "40", "50" }, z.getColumn("Age").toArray());
		assertArrayEquals(new Object[] { "15", "16", "25", "32" }, z.getColumn("Id").toArray());
		//System.out.println(z.toString());
	}

}
