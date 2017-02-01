package tests;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import eg.edu.alexu.csd.TestRunner;
import jdbc.SqlDriver;
public class SmokeTest {

	private static String protocol = "xmldb";
	private static String tmp = System.getProperty("java.io.tmpdir");

	public static Class<?> getSpecifications() {
		return Driver.class;
	}

	private Connection createUseDatabase(String databaseName) throws SQLException {
	 
		//Driver driver = (Driver) TestRunner.getImplementationInstance();
	  Driver driver = new SqlDriver();
	  
		Properties info = new Properties();
		
		File dbDir = new File(System.getProperty("user.home")+ File.separator+"Dbms"+File.separator + Math.round((((float) Math.random()) * 100000)));
		//System.out.println(dbDir.getPath());
		info.put("path", dbDir.getAbsoluteFile());

		Connection connection = driver.connect("jdbc:" + protocol + "://localhost", info); // Establish
		Statement statement = connection.createStatement(); // create a
															// statement object
															// to execute next
															// statements.
		statement.execute("DROP DATABASE " + databaseName); // delete
															// "databaseName" it
															// if it exists.
		statement.execute("CREATE DATABASE " + databaseName); // you should now
																// create a
																// folder for
																// that database
																// within dbDir.
		statement.execute("USE " + databaseName); // Set the state of your
													// connection to use
													// "databaseName", all next
													// created statements
		// (like selects and inserts) should be applied to this database.
		statement.close();
		return connection;
	}

	@Test
	public void testCreateAndOpenAndDropDatabase() throws SQLException {
		File dummy = null;
		 Driver driver = new SqlDriver();
	   Properties info = new Properties();
	   File dbDir = new File(System.getProperty("user.home")+File.separator+ "DBMS"+File.separator + Math.round((((float) Math.random()) * 100000)));
	    
		info.put("path", dbDir.getAbsoluteFile());
		Connection connection = driver.connect("jdbc:" + protocol + "://localhost", info);

		{
			Statement statement = connection.createStatement();
			statement.execute("DROP DATABASE SaMpLe");
			statement.execute("CREATE DATABASE SaMpLe");
			statement.execute("USE SaMpLe");
			String files[] = dbDir.list();
			Assert.assertFalse("Databases directory is empty!" + dbDir.getPath(), files == null || files.length == 0);
			dummy = new File(dbDir, "dummy");
			try {
				boolean created = dummy.createNewFile();
				Assert.assertTrue("Failed to create file into DB", created && dummy.exists());
			} catch (IOException e) {
				TestRunner.fail("Failed to create file into DB", e);
			}
			statement.close();
		}

		{
			Statement statement = connection.createStatement();
			statement.execute("CREATE DATABASE sAmPlE");
			statement.execute("USE SaMpLe");
			String files[] = dbDir.list();
			Assert.assertTrue("Database directory is empty after opening!", files.length > 0);
			Assert.assertTrue("Failed to create find a previously created file into DB", dummy.exists());
			statement.close();
		}
		connection.close();
	}

	@Test
	public void testCreateTable() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 date)");
			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to create table", e);
		}
		try {
			Statement statement = connection.createStatement();
			boolean created = statement
					.execute("CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 date)");
			Assert.assertFalse("create table returned value",created);
			Assert.fail("Create table succeed when table already exists");
		} catch (Throwable e) {
			//TestRunner.fail("Failed to create existing table", e);
		}
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE incomplete_table_name1");
			Assert.fail("Create invalid table succeed");
		} catch (SQLException e) {
		} catch (Throwable e) {
			TestRunner.fail("Unknown Exception thrown", e);
		}
		connection.close();
	}

	@Test
	public void testInsertWithoutColumnNames() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name3(column_name1 varchar, column_name2 int, column_name3 float)");
			int count = statement.executeUpdate("INSERT INTO table_name3 VALUES ('value1', 3, 1.3)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count);
			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to insert into table", e);
		}
		connection.close();
	}

	@Test
	public void testInsertWithColumnNames() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name4(column_name1 varchar, column_name2 int, column_name3 date)");
			int count = statement.executeUpdate(
					"INSERT INTO table_name4(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', '2011-01-25', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count);
			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to insert into table", e);
		}
		connection.close();
	}

	@Test
	public void testInsertWithWrongColumnNames() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name5(column_name1 varchar, column_name2 int, column_name3 varchar)");
			statement.executeUpdate(
					"INSERT INTO table_name5(invalid_column_name1, column_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.fail("Inserted with invalid column name!!");
			statement.close();
		} catch (Throwable e) {
		}
		connection.close();
	}

	@Test
	public void testInsertWithWrongColumnCount() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name6(column_name1 varchar, column_name2 int, column_name3 varchar)");
			statement.executeUpdate("INSERT INTO table_name6(column_name1, column_name2) VALUES ('value1', 4)");
			Assert.fail("Inserted with invalid column count!!");
			statement.close();
		} catch (Throwable e) {
		}
		connection.close();
	}

	@Test
	public void testUpdate() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name7(column_name1 varchar, column_name2 int, column_name3 varchar)");
			int count1 = statement.executeUpdate(
					"INSERT INTO table_name7(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count1);
			int count2 = statement.executeUpdate(
					"INSERT INTO table_name7(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count2);
			int count3 = statement.executeUpdate(
					"INSERT INTO table_name7(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count3);
			int count4 = statement.executeUpdate(
					"UPDATE table_name7 SET column_name1='1111111111', COLUMN_NAME2=2222222, column_name3='333333333'");
			Assert.assertEquals("Updated returned wrong number", count1 + count2 + count3, count4);
			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to update table", e);
		}
		connection.close();
	}

	@Test
	public void testConditionalUpdate() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement.execute(
					"CREATE TABLE table_name8(column_name1 varchar, column_name2 int, column_name3 date, column_name4 float)");

			int count1 = statement.executeUpdate(
					"INSERT INTO table_name8(column_NAME1, COLUMN_name3, column_name2, column_name4) VALUES ('value1', '2011-01-25', 3, 1.3)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count1);

			int count2 = statement.executeUpdate(
					"INSERT INTO table_name8(column_NAME1, COLUMN_name3, column_name2, column_name4) VALUES ('value1', '2011-01-28', 3456, 1.01)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count2);

			int count3 = statement.executeUpdate(
					"INSERT INTO table_name8(column_NAME1, COLUMN_name3, column_name2, column_name4) VALUES ('value2', '2011-02-11', -123, 3.14159)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count3);

			int count4 = statement.executeUpdate(
					"UPDATE table_name8 SET COLUMN_NAME2=222222, column_name3='1993-10-03' WHERE coLUmn_NAME1='value1'");
			Assert.assertEquals("Updated returned wrong number", count1 + count2, count4);

			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to update table", e);
		}
		connection.close();
	}

	@Test
	public void testUpdateEmptyOrInvalidTable() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name9(column_name1 varchar, column_name2 int, column_name3 varchar)");
			int count = statement.executeUpdate(
					"UPDATE table_name9 SET column_name1='value1', column_name2=15, column_name3='value2'");
			Assert.assertEquals("Updated empty table retruned non-zero count!", 0, count);
			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to update table", e);
		}
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(
					"UPDATE wrong_table_name9 SET column_name1='value1', column_name2=15, column_name3='value2'");
			Assert.fail("Updated empty table retruned non-zero count!");
			statement.close();
		} catch (SQLException e) {
		} catch (Throwable e) {
			TestRunner.fail("Invalid exception was thrown", e);
		}
		connection.close();
	}

	@Test
	public void testDelete() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name10(column_name1 varchar, column_name2 int, column_name3 date)");
			int count1 = statement.executeUpdate(
					"INSERT INTO table_name10(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', '2011-01-25', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count1);
			int count2 = statement.executeUpdate(
					"INSERT INTO table_name10(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', '2011-01-28', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count2);
			int count3 = statement.executeUpdate(
					"INSERT INTO table_name10(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', '2011-02-11', 5)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count3);
			int count4 = statement.executeUpdate("DELETE From table_name10");
			Assert.assertEquals("Delete returned wrong number", 3, count4);
			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to delete from table", e);
		}
		connection.close();
	}

	@Test
	public void testConditionalDelete() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE table_name11(column_name1 varchar, column_name2 int, column_name3 DATE)");
			int count1 = statement.executeUpdate(
					"INSERT INTO table_name11(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', '2011-01-25', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count1);
			int count2 = statement.executeUpdate(
					"INSERT INTO table_name11(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', '2013-06-30', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count2);
			int count3 = statement.executeUpdate(
					"INSERT INTO table_name11(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', '2013-07-03', 5)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count3);
			int count4 = statement.executeUpdate("DELETE From table_name11  WHERE coLUmn_NAME3>'2011-01-25'");
			Assert.assertEquals("Delete returned wrong number", 2, count4);
			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to delete from table", e);
		}
		connection.close();
	}

	@Test
	public void testSelect() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement
					.execute("CREATE TABLE table_name12(column_name1 varchar, column_name2 int, column_name3 varchar)");
			int count1 = statement.executeUpdate(
					"INSERT INTO table_name12(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count1);
			int count2 = statement.executeUpdate(
					"INSERT INTO table_name12(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count2);
			int count3 = statement.executeUpdate(
					"INSERT INTO table_name12(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count3);
			int count4 = statement.executeUpdate(
					"INSERT INTO table_name12(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count4);
			ResultSet result = statement.executeQuery("SELECT * From table_name12");
			int rows = 0;
			while (result.next())
				rows++;
			Assert.assertNotNull("Null result retruned", result);
			Assert.assertEquals("Wrong number of rows", 4, rows);
			Assert.assertEquals("Wrong number of columns", 3, result.getMetaData().getColumnCount());
			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to select from table", e);
		}
		connection.close();
	}

	@Test
	public void testConditionalSelect() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement
					.execute("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)");
			int count1 = statement.executeUpdate(
					"INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count1);
			int count2 = statement.executeUpdate(
					"INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 4, 'value3')");
			Assert.assertNotEquals("Insert returned zero rows", 0, count2);
			int count3 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count3);
			int count4 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count4);
			ResultSet result = statement.executeQuery("SELECT column_name1 FROM table_name13 WHERE coluMN_NAME2 < 5");
			int rows = 0;
			while (result.next())
				rows++;
			Assert.assertNotNull("Null result retruned", result);
			Assert.assertEquals("Wrong number of rows", 2, rows);
			Assert.assertEquals("Wrong number of columns", 1, result.getMetaData().getColumnCount());
			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to select from table", e);
		}
		connection.close();
	}

	@Test
	public void testExecute() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement
					.execute("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)");
			int count1 = statement.executeUpdate(
					"INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count1);
			boolean result1 = statement.execute(
					"INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 8, 'value3')");
			Assert.assertFalse("Wrong return for insert record", result1);
			int count3 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count3);
			int count4 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count4);
			boolean result2 = statement.execute("SELECT column_name1 FROM table_name13 WHERE coluMN_NAME2 = 8");
			Assert.assertTrue("Wrong return for select existing records", result2);
			boolean result3 = statement.execute("SELECT column_name1 FROM table_name13 WHERE coluMN_NAME2 > 100");
			Assert.assertFalse("Wrong return for select non existing records", result3);
			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to select from table", e);
		}
		connection.close();
	}

	@Test
	public void testDistinct() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement
					.execute("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)");
			int count1 = statement.executeUpdate(
					"INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count1);
			boolean result1 = statement.execute(
					"INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 4, 'value5')");
			Assert.assertFalse("Wrong return for insert record", result1);
			int count3 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count3);
			int count4 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count4);

			boolean result2 = statement.execute("SELECT DISTINCT column_name2 FROM table_name13");
			Assert.assertTrue("Wrong return for select existing records", result2);
			ResultSet res1 = statement.getResultSet();

			int rows = 0;
			while (res1.next())
				rows++;
			Assert.assertEquals("Wrong number of rows", 3, rows);

			boolean result3 = statement
					.execute("SELECT DISTINCT column_name2, column_name3 FROM table_name13 WHERE coluMN_NAME2 < 5");
			Assert.assertTrue("Wrong return for select existing records", result3);
			ResultSet res2 = statement.getResultSet();

			int rows2 = 0;
			while (res2.next())
				rows2++;
			Assert.assertEquals("Wrong number of rows", 2, rows2);

			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to select distinct from table", e);
		}
		connection.close();
	}
  @Test
  public void testAlterDrop() throws SQLException {
    Connection connection = createUseDatabase("TestDB_Create");
    try {
      Statement statement = connection.createStatement();
      statement
          .execute("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)");
      int count1 = statement.executeUpdate(
          "INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
      Assert.assertNotEquals("Insert returned zero rows", 0, count1);
      boolean result1 = statement.execute(
          "INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 4, 'value5')");
      Assert.assertFalse("Wrong return for insert record", result1);
      int count3 = statement.executeUpdate(
          "INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
      Assert.assertNotEquals("Insert returned zero rows", 0, count3);
      int count4 = statement.executeUpdate(
          "INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
      Assert.assertNotEquals("Insert returned zero rows", 0, count4);

      boolean result2 = statement.execute("ALTER TABLE table_name13 DROP COLUMN column_name3");
      Assert.assertFalse("Wrong return for ALTER TABLE", result2);

      boolean result3 = statement.execute("SELECT * FROM table_name13");
      Assert.assertTrue("Wrong return for select existing records", result3);
      ResultSet res2 = statement.getResultSet();
      Assert.assertEquals(2,res2.getMetaData().getColumnCount());
      int rows2 = 0;
      while (res2.next())
        rows2++;
      Assert.assertEquals("Wrong number of rows", 4, rows2);

      while (res2.previous())
        ;
      res2.next();


      statement.close();
    } catch (Throwable e) {
      TestRunner.fail("Failed to test ALTER TABLE from table", e);
    }
    connection.close();
  }


  @Test //
  public void testAlterTable() throws SQLException {
    Connection connection = createUseDatabase("TestDB_Create");
    try {
      Statement statement = connection.createStatement();
      statement
          .execute("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)");
      int count1 = statement.executeUpdate(
          "INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
      Assert.assertEquals("Insert returned a number != 1", 1, count1);
      boolean result1 = statement.execute(
          "INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 4, 'value5')");
      Assert.assertFalse("Wrong return for insert record", result1);
      int count3 = statement.executeUpdate(
          "INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
      Assert.assertEquals("Insert returned a number != 1", 1, count3);
      int count4 = statement.executeUpdate(
          "INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
      Assert.assertEquals("Insert returned a number != 1", 1, count4);

      boolean result2 = statement.execute("ALTER TABLE table_name13 ADD COLUMN column_name4 date");
      Assert.assertFalse("Wrong return for ALTER TABLE", result2);

      boolean result3 = statement.execute("SELECT column_name4 FROM table_name13 WHERE coluMN_NAME2 = 5");
      Assert.assertTrue("Wrong return for select existing records", result3);
      ResultSet res2 = statement.getResultSet();
      int rows2 = 0;
      while (res2.next())
        rows2++;
      Assert.assertEquals("Wrong number of rows", 1, rows2);

      while (res2.previous())
        ;
      res2.next();

      Assert.assertNull("Retrieved date is not null", res2.getDate("column_name4"));

      statement.close();
    } catch (Throwable e) {
      TestRunner.fail("Failed to test ALTER TABLE from table", e);
    }
    connection.close();
  }
	
	public void testUnion() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement
					.execute("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)");
			int count1 = statement.executeUpdate(
					"INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count1);
			boolean result1 = statement.execute(
					"INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 4, 'value5')");
			Assert.assertFalse("Wrong return for insert record", result1);
			int count3 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count3);
			int count4 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count4);

			boolean result3 = statement.execute(
					"SELECT * FROM table_name13 WHERE coluMN_NAME2 = 4 UNION SELECT * FROM table_name13 WHERE coluMN_NAME3 < 'value6'");
			Assert.assertTrue("Wrong return for select UNION existing records", result3);
			ResultSet res2 = statement.getResultSet();
			int rows2 = 0;
			while (res2.next())
				rows2++;
			Assert.assertEquals("Wrong number of rows", 3, rows2);

			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to test SELECT from table UNION", e);
		}
		connection.close();
	}

	
	public void testOrderBy() throws SQLException {
		Connection connection = createUseDatabase("TestDB_Create");
		try {
			Statement statement = connection.createStatement();
			statement
					.execute("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)");
			int count1 = statement.executeUpdate(
					"INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count1);
			boolean result1 = statement.execute(
					"INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 4, 'value5')");
			System.out.println(result1+"boolean" );
			//result1= false;
			
			Assert.assertFalse("Wrong return for insert record", result1);
			int count3 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count3);
			int count4 = statement.executeUpdate(
					"INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
			Assert.assertNotEquals("Insert returned zero rows", 0, count4);

			boolean result3 = statement
					.execute("SELECT * FROM table_name13 ORDER BY column_name2 ASC, COLUMN_name3 DESC");
			Assert.assertTrue("Wrong return for select UNION existing records", result3);
			ResultSet res2 = statement.getResultSet();
			int rows2 = 0;
			while (res2.next())
				rows2++;
			Assert.assertEquals("Wrong number of rows", 4, rows2);

			while (res2.previous())
				;

			res2.next();
			Assert.assertEquals("Wrong order of rows", 4, res2.getInt("column_name2"));
			Assert.assertEquals("Wrong order of rows", "value5", res2.getString("column_name3"));

			res2.next();
			Assert.assertEquals("Wrong order of rows", 4, res2.getInt("column_name2"));
			Assert.assertEquals("Wrong order of rows", "value3", res2.getString("column_name3"));

			res2.next();
			Assert.assertEquals("Wrong order of rows", 5, res2.getInt("column_name2"));

			statement.close();
		} catch (Throwable e) {
			TestRunner.fail("Failed to test ORDER By", e);
		}
		connection.close();
	}
}