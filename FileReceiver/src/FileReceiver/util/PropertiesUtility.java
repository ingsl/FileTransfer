package FileReceiver.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtility {
	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtility.class);

	public static Properties getProperties(String path) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		try {
			fis = new FileInputStream(path);
			bis = new BufferedInputStream(fis);

			Properties prop = new Properties();
			prop.load(bis);

			return prop;
		} catch (Exception e) {
			logger.error(CommonUtility.getPrintStacTraceString(e));
		} finally {
			CloseUtility.closeInputStream(bis, fis);
		}

		return null;
	}

	public static int getIntProperty(Properties prop, String key) {
		String propertyValue = getStringProperty(prop, key);

		int propertyIntValue = 0;

		try {
			propertyIntValue = Integer.parseInt(propertyValue);
		} catch (NumberFormatException e) {
			logger.error(CommonUtility.getPrintStacTraceString(e));
		}

		return propertyIntValue;
	}

	public static String getStringProperty(Properties prop, String key) {
		Object value = prop.get(key);
		if (value == null) {
			logger.error("properties doesn't contain {}", key);
			return "";
		} else {
			return (String) value;
		}
	}

	public static boolean store(Properties prop, String path, String message) {
		BufferedOutputStream bos = null;

		try {
			bos = new BufferedOutputStream(new FileOutputStream(path));
			prop.store(bos, message);

			return true;
		} catch (Exception e) {
			logger.error(CommonUtility.getPrintStacTraceString(e));
		} finally {
			CloseUtility.closeOutputStream(bos);
		}

		return false;
	}

	public static void checkPropertiesDirectory(String path) {
		File checkPath = new File(path);
		if (!checkPath.exists()) {
			try {
				checkPath.mkdirs();
			} catch (Exception e) {
				logger.error(CommonUtility.getPrintStacTraceString(e));
			}
		}
	}

	public static void checkPropertiesFile(String path) {
		File checkFile = new File(path);
		if (!checkFile.isFile()) {
			try {
				checkFile.createNewFile();
			} catch (Exception e) {
				logger.error(CommonUtility.getPrintStacTraceString(e));
			}
		}
	}
}