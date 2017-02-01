package jdbc;

import java.util.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;

import eg.edu.alexu.csd.oop.dbms.Database.DBSystem;
import eg.edu.alexu.csd.oop.dbms.Database.Table;
import eg.edu.alexu.csd.oop.dbms.Database.*;

public class SqlStatement implements java.sql.Statement {

	private List<String> batch;
	private ResultSet result;
	private Connection connect;
	private DBSystem dbms;
	private Log4j logger;

	private enum statementState {
		Closed, Opened
	};

	private statementState current;
	private int updateCount;

	public SqlStatement(Connection connect, DBSystem db) {
		this.dbms = db;
		batch = new ArrayList<String>();
		this.connect = connect;
		current = statementState.Opened;
		updateCount = 0;
		result = null;
		logger = new Log4j();
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {

		if (current == statementState.Closed) {
			logger.fail("Statement is closed.Cannot perform operations");
			throw new SQLException();
		}
		if (!sql.toLowerCase().contains("select")) {
			logger.fail("Use execute instead");
			throw new SQLException();
		}
		try {
			Table check = dbms.executeQuery(sql.toLowerCase().trim());
			// updateCount = dbms.getUpdateCount();
			if (check == null) {
				logger.fail("Query does not return ResultSet Object");
				throw new SQLException();
			}
			result = new Resultset(check, this);
			logger.success("queryExecuted");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}

	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		if (current == statementState.Closed) {
			logger.fail("Statement is closed.Cannot perform operations");
			throw new SQLException();
		}
		try {
			Table check = dbms.executeQuery(sql.toLowerCase().trim());
			updateCount = dbms.getUpdateCount();
			if (sql.toLowerCase().contains("select")) {
				logger.fail("ExecuteUpdate cannot perform select query.Use execute instead");
				throw new SQLException();
			} else {
				result = null;
			}
			if (updateCount == -1) {
				return 0;
			} else {
				logger.success( updateCount + " row(s) modified");
				return updateCount;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public void close() throws SQLException {

		if (result != null)
			result.close();
		this.current = statementState.Closed;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (current == statementState.Closed) {
			logger.fail("Statement is closed.Cannot perform operations");
			throw new SQLException();
		}
		return this.connect;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		if (current == statementState.Closed) {
			logger.fail("Statement is closed.Cannot perform operations");
			throw new SQLException();
		}
		this.batch.add(sql);
	}

	@Override
	public void clearBatch() throws SQLException {
		if (current == statementState.Closed) {
			logger.fail("Statement is closed.Cannot perform operations");
			throw new SQLException();
		}
		this.batch.clear();
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		if (current == statementState.Closed) {
			logger.fail("Statement is closed.Cannot perform operations");
			throw new SQLException();
		}
		return this.result;
	}

	@Override
	public int[] executeBatch() throws SQLException {
		if (current == statementState.Closed) {
			throw new SQLException();
		}
		int[] toReturn = new int[batch.size()];

		for (int i = 0; i < batch.size(); i++) {
			try {
				Table check = dbms.executeQuery(batch.get(i));
				updateCount = dbms.getUpdateCount();
				logger.success("statement executed :" + batch.get(i));
				if (updateCount == -1) {
					toReturn[i] = SUCCESS_NO_INFO;
				}
				toReturn[i] = updateCount;
				if (check == null) {

					result = null;

				} else {
					result = new Resultset(check, this);
				}

			} catch (Exception e) {
				logger.fail(batch.get(i) + "  is invalid");

				throw new SQLException();
			}
		}
		clearBatch();
		return toReturn;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		if (current == statementState.Closed) {
			logger.fail("Statement is closed.Cannot perform operations");
			throw new SQLException();
		}
		return updateCount;
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		if (current == statementState.Closed) {

			
			logger.fail("Statement is closed.Cannot perform operations");
			throw new SQLException();
		}
		try {

			Table check = dbms.executeQuery(sql.toLowerCase().trim());
			updateCount = dbms.getUpdateCount();
			if (check == null || updateCount != -1) {

				result = null;
				return false;
			} else {
				result = new Resultset(check, this);
				return true;
			}
		} catch (Exception e) {
			//e.printStackTrace();
			logger.fail("cannot execute query");
			throw new SQLException(e.getMessage());

		}

	}

	// unsupported methods

	@Override
	public int getQueryTimeout() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxRows() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void cancel() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchDirection() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetType() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isClosed() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isPoolable() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}

}