package FileReceiver.util;

import java.util.Properties;

public class EnvCode {
	public static final String S3IFILERECEIVER_PROPERTIES_PATH = EnvCode.class.getResource("").getPath().split("/util")[0] + "/conf/S3IFileReceiver.properties";
	
	public static final Properties FILE_RECEIVER_PROPERTIES = PropertiesUtility.getProperties(S3IFILERECEIVER_PROPERTIES_PATH);
	
	public static final int SERVER_PORT = PropertiesUtility.getIntProperty(FILE_RECEIVER_PROPERTIES, "SERVER_PORT");
	public static final int DEFAULT_BUFFER_SIZE = PropertiesUtility.getIntProperty(FILE_RECEIVER_PROPERTIES, "DEFAULT_BUFFER_SIZE");
}
