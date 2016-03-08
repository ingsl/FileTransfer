package FileSender.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FileSender.util.CloseUtility;
import FileSender.util.CommonUtility;
import FileSender.util.DirectoryUtility;
import FileSender.util.EnvCode;
import FileSender.util.FileUtility;

public class FileSenderThread implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(FileSenderThread.class);

	private File transFile = null;
	private int dataSeq;

	private Socket senderSocket;
	private BufferedOutputStream bos;
	private DataOutputStream dos;
	private FileInputStream fis;
	private BufferedInputStream bis;
	private DataInputStream dis;

	private String serverIp;
	private int serverPort;
	private int connetTimeout;
	private int inputStreamTimeout;

	private int fileSendMaxCount = 5;
	private int retryConnectionTimeInterval = 5000;

	public static final int FILE_SEND_STATUS_WAIT		= 0;		// 첨부파일 전송 대기
	public static final int FILE_SEND_STATUS_FAIL		= 1;		// 첨부파일 전송 실패
	public static final int FILE_SEND_STATUS_COMPLETE	= 2;		// 첨부파일 전송 완료

	public static final int DEFAULT_BUFFER_SIZE	= EnvCode.DEFAULT_BUFFER_SIZE;

	public FileSenderThread(File file, int dataSeq) {
		this.transFile = file;
		this.dataSeq =dataSeq;
		getServerInfo();
	}

	private void getServerInfo() {
		serverIp = EnvCode.SERVER_IP;
		serverPort = EnvCode.SERVER_PORT;
		connetTimeout = EnvCode.SERVER_SOCKET_TIMEOUT;
		inputStreamTimeout = EnvCode.SERVER_CONNECT_TIMEOUT;
	}

	public void run() {
		logger.debug("Connecting FileSender server ip = [" + serverIp + "], server port = [" + serverPort +"], connetTimeout = [" + connetTimeout + "], inputStreamTimeout = [" + inputStreamTimeout +"]");

		try {
			int fileSendCount = 0;
			int fileSendStatus = FILE_SEND_STATUS_WAIT;

			while (fileSendStatus < FILE_SEND_STATUS_COMPLETE && fileSendCount < fileSendMaxCount) {
				
				senderSocket = connection();
				if (senderSocket.isConnected()) {
					logger.debug("====  파일전송 시작 ====");
					int sendResultStatus = sendFile();
					if (sendResultStatus == 1) {
						fileSendStatus = BaseCode.FILE_SEND_STATUS_COMPLETE;
					} else {
						logger.error("Try Count : {}", fileSendCount);
						fileSendCount++;
					}
				} else {
					logger.error("Connecting FileReciever server fail. [" + serverIp + "], server port = [" + serverPort +"], inputStreamTimeout = [" + inputStreamTimeout +"]");
					fileSendCount++;
				}
				CloseUtility.closeStream(dis, bis,fis, dos, bos, senderSocket);
			}
			if (fileSendCount == fileSendMaxCount) {
				logger.error("file resend count limited. limit count = {}", fileSendMaxCount);
				fileSendStatus = FILE_SEND_STATUS_FAIL;
			}
			if (fileSendStatus == FILE_SEND_STATUS_COMPLETE) {
				logger.info("############## 파일전송 완료 #############");
				switch (EnvCode.AFTER_SEND_DELETE_TYPE) {
				case "0":
					break;
				case "1":
					File parentDir = transFile.getParentFile();
					FileUtility.delete(transFile);
					break;
				case "2":
					parentDir = transFile.getParentFile();
					FileUtility.delete(transFile);
					
					if (parentDir != null && parentDir.listFiles().length == 0) {
						DirectoryUtility.delete(parentDir);
					}
					break;
				default:
					logger.error("AFTER_SEND_DELETE_TYPE property error. Check properties file.");
					break;
				}
			}
		} catch (Exception e) {
			logger.error(CommonUtility.getPrintStacTraceString(e));
		} finally{
			CloseUtility.closeStream(dis, bis,fis, dos, bos, senderSocket);
		}

	}

	private int sendFile() throws Exception {
		bos = new BufferedOutputStream(senderSocket.getOutputStream());
		dos = new DataOutputStream(senderSocket.getOutputStream());

		long originalCRC32Value = CommonUtility.getCRC32Value(transFile.getCanonicalPath().replace(EnvCode.SEND_DIR_PATH, EnvCode.SERVER_RECEIVE_ROOT_DIR));
		String originalMD5Value = CommonUtility.getMD5Value(transFile.getCanonicalPath().replace(EnvCode.SEND_DIR_PATH, EnvCode.SERVER_RECEIVE_ROOT_DIR));

		System.out.println(originalCRC32Value);
		System.out.println(originalMD5Value);
		logger.debug("@@@@@@@@@@@@@@@@@@@@@@@originalMD5Value = " + originalMD5Value);
		logger.debug("#######################originalCRC32Value = " + originalCRC32Value);


		dos.writeInt(dataSeq);
		dos.writeUTF(transFile.getName()); 
		dos.writeUTF(transFile.getCanonicalPath().substring(EnvCode.SEND_DIR_PATH.length())); 
		dos.writeUTF(EnvCode.SERVER_RECEIVE_ROOT_DIR);
		dos.writeUTF(transFile.getCanonicalPath());
		dos.writeUTF(transFile.length() + "");

		dos.writeUTF(originalMD5Value);
		dos.writeLong(originalCRC32Value);

		fis = new FileInputStream(transFile);
		bis = new BufferedInputStream(fis);

		byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
		int nread = 0;

		while ((nread = bis.read(buf, 0, DEFAULT_BUFFER_SIZE)) != -1) {
			bos.write(buf, 0, nread);
			bos.flush();
		}

		int rxResultStatus = 0;
		dis = new DataInputStream(senderSocket.getInputStream());
		rxResultStatus = dis.readInt();

		return rxResultStatus;
	}

	private Socket connection() throws InterruptedException {
		int connectionTryCount = 0;
		
		while (connectionTryCount < fileSendMaxCount && (senderSocket == null || senderSocket.isClosed() || !senderSocket.isConnected())) {
			try {
				senderSocket = new Socket();
				SocketAddress socketAddress = new InetSocketAddress(serverIp, serverPort);
				senderSocket.setSoTimeout(inputStreamTimeout);	// inputstream에서 timeout
				senderSocket.connect(socketAddress, connetTimeout);	// socket 연결 timeout
			} catch (Exception e) {
				logger.error(CommonUtility.getPrintStacTraceString(e));
				connectionTryCount++;
				Thread.sleep(retryConnectionTimeInterval);
			}
		}
		return senderSocket;
	}
}
