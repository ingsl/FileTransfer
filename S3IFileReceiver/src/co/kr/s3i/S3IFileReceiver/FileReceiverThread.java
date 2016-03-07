package co.kr.s3i.S3IFileReceiver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.kr.s3i.S3IFileReceiver.util.CloseUtility;
import co.kr.s3i.S3IFileReceiver.util.CommonUtility;
import co.kr.s3i.S3IFileReceiver.util.EnvCode;

public class FileReceiverThread implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(FileReceiverThread.class);

	private Socket ServerSocket;
	private BufferedInputStream bis = null;
	private DataInputStream dis = null;
	private FileOutputStream fos = null;
	private BufferedOutputStream bos = null;
	private DataOutputStream dos = null;
	
	public static final int DEFAULT_BUFFER_SIZE	= EnvCode.DEFAULT_BUFFER_SIZE;
	
	public FileReceiverThread(Socket socket) {
		ServerSocket = socket;
	}

	public void run() {
		logger.debug("FileReceiverThread start");
		System.out.println("FileReceiverThread start");
		boolean isReceiveSuccess = false;

		int dataSeq = 0;
		String fileNm = "";
		String chileFilePath = "";
		String serverReceiveRootDir ="";
		String orignFilePath = "";
		String fileLength = "";
		
		try {
			if (ServerSocket != null) {
				bis = new BufferedInputStream(ServerSocket.getInputStream());
				dis = new DataInputStream(ServerSocket.getInputStream());
				dos = new DataOutputStream(ServerSocket.getOutputStream());
				
				dataSeq = dis.readInt();
				fileNm = dis.readUTF();
				chileFilePath = dis.readUTF();
				serverReceiveRootDir = dis.readUTF();
				orignFilePath = dis.readUTF();
				fileLength = dis.readUTF();

				String originalMD5Value = dis.readUTF();
				long originalCRC32Value = dis.readLong();
				
				System.out.println("dataSe = " + dataSeq);
				System.out.println("fileNm = " + fileNm);
				System.out.println("chileFilePath = " + chileFilePath);
				System.out.println("orignFilePath = " + orignFilePath);
				System.out.println("fileLength = " + fileLength);
				System.out.println("serverReceiveRootDir = " + fileLength);
				System.out.println("originalMD5Value = " + originalMD5Value);
				System.out.println("originalCRC32Value = " + originalCRC32Value);
				File f = new File(chileFilePath);
				File dir = new File(serverReceiveRootDir + f.getParent());

				logger.debug("!!!!!!!!!!!!!!!!!!!!!!!orignFilePath" + orignFilePath);
				logger.debug("@@@@@@@@@@@@@@@@@@@@@@@originalMD5Value = " + originalMD5Value);
				logger.debug("#######################originalCRC32Value = " + originalCRC32Value);

				logger.debug("dataSeq = {}, fileNm = {}, filePah ={}", dataSeq, fileNm, dir);
				if (!dir.isDirectory()) {
					if(!dir.mkdirs()){
					}
				}
				fos = new FileOutputStream(dir + File.separator + fileNm);
				bos = new BufferedOutputStream(fos);

				int bufferSize = DEFAULT_BUFFER_SIZE;
				int readLen = 0;
				byte[] b = new byte[bufferSize];

				Double fileLen = Double.valueOf(fileLength);
				int lastLen = 0;

				while (fileLen > 0) {
					if (fileLen > bufferSize){
						readLen = dis.read(b, 0, bufferSize);
					} else {
						lastLen = fileLen.intValue();
						readLen = dis.read(b, 0, lastLen);
					}

					bos.write(b, 0, readLen);
					bos.flush();
					fileLen = fileLen - readLen;
					System.out.println("fileLen : " + fileLen);
				}

				//CRC체크
				long compareCRC32Value = CommonUtility.getCRC32Value(fileNm);
				String compareMD5Value = CommonUtility.getMD5Value(fileNm);

				boolean isCRC32Valid = true;
				if (originalCRC32Value != compareCRC32Value) {
					isCRC32Valid = false;
				}

				boolean isMD5Valid = true;
				if (!originalMD5Value.equals(compareMD5Value)) {
					isMD5Valid = false;
				}

				int rxResultStatus = 0;

				if (isCRC32Valid && isMD5Valid) {

					rxResultStatus = 1; //자료전송 성공
				} else {
					if (!isCRC32Valid) {
						logger.error("CRC32 CheckSum Invalid : [fileNm = {}, originalCRC32Value = {}, compareCRC32Value = {} ] ", fileNm, originalCRC32Value, compareCRC32Value);
					}
					if (!isMD5Valid) {
						logger.error("MD5 CheckSum Invalid : [fileNm = {}, originalMD5Value = {}, compareMD5Value = {} ] ", fileNm, originalMD5Value, compareMD5Value);
					}

					rxResultStatus = 0;
				}

				if (rxResultStatus == 1) {
					isReceiveSuccess = true;
				}
				dos.writeInt(rxResultStatus);
				dos.flush();
				
			}
		} catch (EOFException ee) {
			logger.debug("EOFException occured. Opposite observer checking....");
		} catch (Exception e) {
			logger.error(CommonUtility.getPrintStacTraceString(e));
		} finally {
			CloseUtility.closeStream(bos, fos, dos, dis, bis, ServerSocket);
		}
		logger.debug("isReceiveSuccess:" + isReceiveSuccess);
		logger.debug("FileReceiverThread end");

	}
}