package tests;

import static org.junit.Assert.*;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import jdbc.*;
import eg.edu.alexu.csd.oop.dbms.Database.*;;

public class StatementJTest {

	private DBSystem dbms;
	private SqlConnection conn;
	private File workspace;

	public SqlStatement init() {

		workspace = new File(System.getProperty("user.home"), "DBMS");
		dbms = new DBSystem(workspace);
		conn = new SqlConnection(dbms);
		Log4j x = new Log4j();
		// x.success("connection established");
		SqlStatement test = new SqlStatement(conn, dbms);
		return test;
	}

	@Test
	public void testExecute1() {
		// testing create , drop
		SqlStatement test = init();
		try {
			test.execute("create database University");
			test.execute("use University ");

			File dbDirectory = new File(workspace, "university");
			assertTrue(dbDirectory.exists() && dbDirectory.isDirectory());

			test.execute("drop table students");
			test.execute("CREATE TABLE students(Name varchar," + " Id int, Admission date)");

			String files[] = dbDirectory.list();
			int count = files.length;
			assertFalse(count == 0);
			int updatedRowNum = test.executeUpdate(
					"insert into students " + "(Name , Id , Admission) values('stud1' , 1 , '2011-01-25')");
			assertEquals(1, updatedRowNum);
			boolean returnedValue = test.execute("insert into students "

					+ "(Name , Id , Admission) values('stud2' , 2 , '2012-12-30')");
			assertFalse(returnedValue);
			count = test.getUpdateCount();
			assertEquals(1, count);
			test.execute("select * from students");
			count = test.getUpdateCount();
			assertEquals(-1, count);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("error in execute test");

		}
	}

	@Test

	public void executeBatchTest() {
		SqlStatement test = init();
		try {

			test.execute("create database University");
			test.execute("use University ");
			test.execute("drop table students");
			test.execute("CREATE TABLE students(Name varchar," + " Id int, Admission date)");

			test.addBatch("insert into students " + "(Name , Id , Admission)" 
			+ " values('stud1' , 1 , '2011-01-25')");
			test.addBatch("insert into students " + 
			"(Name , Id , Admission) values('stud2' , 2 , '2012-12-30')");
			test.addBatch("select * from students");
			test.addBatch("delete from students");

			int[] returned = test.executeBatch();
			int updatedRowNum = returned[returned.length - 1];
			// test updateCount
			assertEquals(updatedRowNum, test.getUpdateCount());

			int[] expected = { 1, 1, -1, 2 };
			for (int i = 0; i < returned.length; i++)
				assertEquals(expected[i], returned[i]);

			assertTrue(test.executeBatch().length == 0);

			test.clearBatch();
			assertEquals(conn, test.getConnection());
			test.close();
		} catch (Exception e) {
			throw new RuntimeException();
		}

	}

	@Test(expected = SQLException.class)
	public void closureTest() throws SQLException {
		SqlStatement test = init();
		try {
			test.close();
			test.execute("create database University");
		} catch (Exception e) {
			throw new SQLException();
		}

	}

	@Test
	public void ResultSetTest() {
		SqlStatement test = init();
		try {
			test.execute("create database University");
			test.execute("use University ");
			test.execute("drop table students");
			test.execute("CREATE TABLE students(Name varchar," + " Id int, Admission date)");
			test.executeUpdate("insert into students " + "(Name , Id , Admission) values('stud1' , 1 , '2011-01-25')");
			test.execute("select * from students");
			ResultSet check = test.getResultSet();
			assertFalse(check == null);
			test.execute("insert into students " + "(Name , Id , Admission) values('stud2' , 2 , '2012-12-30')");
			check = test.getResultSet();
			assertTrue(check == null);
			assertEquals(test.getConnection(),conn);

		} catch (SQLException e) {

		}
	}
	
	@Test
	public void executeQueryTest (){
		SqlStatement test = init();
		try {
			test.execute("create database University");
			test.execute("use University ");
			test.execute("drop table students");
			test.execute("C varchar," + " Id int, Admission date)");
			
			test.executeUpdate("insert into students " + 
			"(Name , Id , Admission) values('stud1' , 1 , '2011-01-25')");
			test.executeUpdate("insert into students " + 
					"(Name , Id , Admission) values('stud2' ,2 , '2011-02-11')");
			
			ResultSet check = test.executeQuery("select * students");
			assertFalse(check ==null);
			

		}catch (SQLException e) {
			
		}
		
		
	}
	@Test (expected = SQLException.class)
	public void invalidexecuteQueryTest ()throws  SQLException{
		SqlStatement test = init();
		try {
			test.execute("create database University");
			test.execute("use University ");
			test.execute("drop table students");
			test.execute("CREATE TABLE students(Name varchar," + " Id int, Admission date)");
			
			test.executeUpdate("insert into students " + 
			"(Name , Id , Admission) values('stud1' , 1 , '2011-01-25')");
			test.executeQuery("insert into students " + 
					"(Name , Id , Admission) values('stud2' ,2 , '2011-02-11')");
		}catch (SQLException e) {
			throw new SQLException();
		}
	}
	

}