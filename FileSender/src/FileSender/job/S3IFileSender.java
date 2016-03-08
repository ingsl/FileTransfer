package FileSender.job;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FileSender.common.FileSenderThread;
import FileSender.util.CommonUtility;
import FileSender.util.EnvCode;

public class S3IFileSender implements Job {

	private static final Logger logger = LoggerFactory.getLogger(S3IFileSender.class);

	public static ArrayList<File> sendFileList = new ArrayList<File>();
	public Map<String, Integer> demoFileValues = new HashMap<String, Integer>();
	public Map<String, Integer> fileValues = new HashMap<String, Integer>();

	public static int THREADCOUNT = 1000;
	private static ExecutorService executor = Executors.newFixedThreadPool(THREADCOUNT);
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// file 갯수, 바이트 체크
		try {
			fileValuesCheck();
			logger.debug("Check the file List");
			makeSendFileList();
			logger.debug("make a file sender list");
			System.out.println(sendFileList);
			if (!sendFileList.isEmpty()) {
				for (int i = 0; i < sendFileList.size(); i++) {
					executor.execute(new FileSenderThread(sendFileList.get(i), i+1));
				}
			}
		} catch (Exception e) {
			logger.error(CommonUtility.getPrintStacTraceString(e));
		} finally {
			sendFileList.clear();
			demoFileValues.clear();
			fileValues.clear();
			logger.debug("File List Clear");
		}
	}

	private void fileValuesCheck() {
		int checkFileCount = 0;
		int checkFileSzieCount = 0;

		try {
			while (EnvCode.RETRY_CHECK_FILE_COUNT > checkFileCount) {
				checkFileList(EnvCode.SEND_DIR_PATH);
				if (fileValues != null && demoFileValues != null) {
					if (fileValues.size() == demoFileValues.size()) {
						checkFileCount += 1;
						//System.out.println(checkFileCount);
					}
					demoFileValues = fileValues;
				}
				Thread.sleep(EnvCode.CHECK_FILE_INTERVAL * 1000);
			}

			if (EnvCode.RETRY_CHECK_FILE_COUNT == checkFileCount) {
				while (EnvCode.RETRY_CHECK_FILE_SIZE_COUNT == checkFileSzieCount) {
					checkFileList(EnvCode.SEND_DIR_PATH);
					for (int i = 0; i < fileValues.size(); i++) {
						if (fileValues.values() == demoFileValues.values()) {
							checkFileSzieCount += 1;
						}
					}
					demoFileValues = fileValues;
				}
			}
		} catch (Exception e) {
			logger.error(CommonUtility.getPrintStacTraceString(e));
		}
	}

	private void checkFileList(String path) {
		File rootDir = new File(path);
		File[] fileList = rootDir.listFiles();
		try {
			if (fileList.length > 0) {
				for (int i = 0; i < fileList.length; i++) {
					File file = fileList[i];
					if (file.isFile()) {
						fileValues.put(file.getCanonicalPath(), (int) file.length());
					} else if (file.isDirectory()) {
						checkFileList(file.getCanonicalPath().toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void makeSendFileList() {
		try {
			getFileList(EnvCode.SEND_DIR_PATH);
			System.out.println(EnvCode.SEND_DIR_PATH);
		} catch (Exception e) {
			logger.error(CommonUtility.getPrintStacTraceString(e));
		}
	}

	public static void getFileList(String rootDirPath) {
		try {
			File rootDir = new File(rootDirPath);

			if (rootDir.isDirectory()) {
				File[] fileList = rootDir.listFiles();

				if (fileList.length > 0) {
					for (File childFile : fileList) {
						if (childFile.isFile()) {
							if (childFile.length() > 0) {
								//System.out.println("path : " + rootDirPath + File.separator + childFile.getName() + ",   fileSize : " + childFile.length());
								logger.debug("path : " + rootDirPath + File.separator + childFile.getName() + ",   fileSize : " + childFile.length() + "\n");
								sendFileList.add(childFile);
							} else {
								switch (EnvCode.ZERO_BYTE_FILE_HANDLE_TYPE) {
								case "0":
									break;
								case "1":
									childFile.delete();
									break;
								case "2":
									sendFileList.add(childFile);
									break;
								default:
									logger.error("ZERO_BYTE_FILE_DELETE_YN property error. Check properties file.");
									System.exit(1);
									break;
								}
							}
						} else if (childFile.isDirectory()) {
							if ("Y".equalsIgnoreCase(EnvCode.EMPTY_DIR_DELETE_YN)) {
								childFile.delete();
							}
							getFileList(childFile.getPath());
						} // end if(searchFile)
					}// end for
				} else {
					//System.out.println(rootDirPath + "	have not files");
					logger.debug(rootDirPath + "have not files");
				} // end if(fileList)
			} else {
				//System.out.println("Path Missing : " + rootDir);
				logger.error("Path Missing : {}", rootDir);
				Thread.sleep(2000);
				System.exit(1);
			} // end if(dirCheck)
		} catch (Exception e) {
			logger.error(CommonUtility.getPrintStacTraceString(e));
		}
	}
}