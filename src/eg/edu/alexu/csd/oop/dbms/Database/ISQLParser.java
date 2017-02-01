package eg.edu.alexu.csd.oop.dbms.Database;

import eg.edu.alexu.csd.oop.dbms.query.IQuery;

public interface ISQLParser {
	
	/**
	 * parser valid query into query.
	 * @param query string containing sql query.
	 * @return query to be executed.
	 */
	public IQuery parse(String query) throws Exception;

}
