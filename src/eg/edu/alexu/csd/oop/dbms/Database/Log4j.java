package eg.edu.alexu.csd.oop.dbms.Database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j {

	private static final Logger logger = LogManager.getLogger(Log4j.class.getName());

	public void success(String message) {
		logger.debug(message);
	}

	public void fail(String message) {
		logger.error(message);
	}

}
