package co.kr.s3i.S3IFileSender.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.Logger;

public class LoggerUtility {

	public static void printAndLogging(Logger logger, String message) {
		System.out.println(message);
		logger.info(message);
	}

	public static void error(Logger logger, Exception e) {
		logger.error(getPrintStacTraceString(e));
	}

	public static void debug(Logger logger, Exception e) {
		logger.debug(getPrintStacTraceString(e));
	}

	public static String getPrintStacTraceString(Exception e) {
		String returnValue = "";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(out);
		e.printStackTrace(printStream);

		returnValue = out.toString();
		return returnValue;
	}
}
