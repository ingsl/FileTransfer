package FileReceiver.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseUtility {
	private static final Logger logger = LoggerFactory.getLogger(CloseUtility.class);

	public static void closeStream(Object... objArray) {
		for (Object obj : objArray) {
			if (obj != null) {
				if (obj instanceof InputStream) {
					closeInputStream((InputStream) obj);
				} else if (obj instanceof OutputStream) {
					closeOutputStream((OutputStream) obj);
				} else if (obj instanceof Socket || obj instanceof ServerSocket) {
					closeSocket(obj);
				}
			}
		}
	}

	public static void closeSocket(Object... socketArray) {
		for (Object socket : socketArray) {
			if (socket != null) {
				try {
					if (socket instanceof Socket) ((Socket) socket).close();
					if (socket instanceof ServerSocket) ((ServerSocket) socket).close();
				} catch (IOException e) {
					logger.error(CommonUtility.getPrintStacTraceString(e));
				}
			}
		}
	}

	public static void closeOutputStream(OutputStream... osArray) {
		for (OutputStream os : osArray) {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error(CommonUtility.getPrintStacTraceString(e));
				}
			}
		}
	}

	public static void closeInputStream(InputStream... isArray) {
		for (InputStream is : isArray) {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error(CommonUtility.getPrintStacTraceString(e));
				}
			}
		}
	}
}