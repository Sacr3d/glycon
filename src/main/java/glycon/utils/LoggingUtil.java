package glycon.utils;

import java.util.logging.Logger;

public class LoggingUtil {

	private static final Logger LOGGER = Logger.getLogger(LoggingUtil.class.getName());

	public static void msg(String string) {

		System.out.println(string);

	}

	public static void warn(String string) {

		LOGGER.warning(string);

	}

}
