package FileSender.util;

import java.util.Properties;

public class EnvCode {
	public static final String S3IFILESENDER_PROPERTIES_PATH = EnvCode.class.getResource("").getPath().split("/util")[0] +"/conf/S3IFileSender.properties";
	//public static final String S3IFILESENDER_PROPERTIES_PATH = "C:\\workspace\\S3IFileSender\\src\\co\\kr\\s3i\\S3IFileSender\\conf\\S3IFileSender.properties";

	public static final Properties FILE_SENDER_PROPERTIES = PropertiesUtility.getProperties(S3IFILESENDER_PROPERTIES_PATH);

	public static final String SERVER_IP = PropertiesUtility.getStringProperty(FILE_SENDER_PROPERTIES, "SERVER_IP");
	public static final String SERVER_RECEIVE_ROOT_DIR = PropertiesUtility.getStringProperty(FILE_SENDER_PROPERTIES, "SERVER_RECEIVE_ROOT_DIR");
	public static final int SERVER_PORT = PropertiesUtility.getIntProperty(FILE_SENDER_PROPERTIES, "SERVER_PORT");
	public static final int SERVER_SOCKET_TIMEOUT = PropertiesUtility.getIntProperty(FILE_SENDER_PROPERTIES, "SERVER_SOCKET_TIMEOUT");
	public static final int SERVER_CONNECT_TIMEOUT = PropertiesUtility.getIntProperty(FILE_SENDER_PROPERTIES, "SERVER_CONNECT_TIMEOUT");
	public static final int DEFAULT_BUFFER_SIZE = PropertiesUtility.getIntProperty(FILE_SENDER_PROPERTIES, "DEFAULT_BUFFER_SIZE");

	public static final String AFTER_SEND_DELETE_TYPE = PropertiesUtility.getStringProperty(FILE_SENDER_PROPERTIES, "AFTER_SEND_DELETE_TYPE");
	public static final String SEND_DIR_PATH = PropertiesUtility.getStringProperty(FILE_SENDER_PROPERTIES, "SEND_DIR_PATH");
	public static final String EMPTY_DIR_DELETE_YN = PropertiesUtility.getStringProperty(FILE_SENDER_PROPERTIES, "EMPTY_DIR_DELETE_YN");
	public static final String ZERO_BYTE_FILE_HANDLE_TYPE = PropertiesUtility.getStringProperty(FILE_SENDER_PROPERTIES, "ZERO_BYTE_FILE_HANDLE_TYPE");
	
	public static final int RETRY_CHECK_FILE_COUNT = PropertiesUtility.getIntProperty(FILE_SENDER_PROPERTIES, "RETRY_CHECK_FILE_COUNT"); 
	public static final int RETRY_CHECK_FILE_SIZE_COUNT = PropertiesUtility.getIntProperty(FILE_SENDER_PROPERTIES, "RETRY_CHECK_FILE_SIZE_COUNT"); 
	public static final int CHECK_FILE_INTERVAL = PropertiesUtility.getIntProperty(FILE_SENDER_PROPERTIES, "CHECK_FILE_INTERVAL"); 
}
