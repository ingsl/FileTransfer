package FileReceiver;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FileReceiver.util.EnvCode;

public class S3IFileReceiver {
	private static final Logger logger = LoggerFactory.getLogger(S3IFileReceiver.class);

	public static void main(String[] args) {
		try {
			System.out.println("자바자바");
			System.out.println(EnvCode.SERVER_PORT);
			ServerSocket serverSocket = new ServerSocket(EnvCode.SERVER_PORT);
			while (true) {
				Socket socket = serverSocket.accept();
				logger.debug("Socket Connect");
				System.out.println("Socket Connect");
				FileReceiverThread fileReceiverThread = new FileReceiverThread(socket);
				Thread thread = new Thread(fileReceiverThread);
				thread.setDaemon(true);
				thread.start();
			}
		} catch (Exception e) {
			System.out.println(e);
			logger.error("FileReceiverThreadManager error : " + e);
		}
	}

}
