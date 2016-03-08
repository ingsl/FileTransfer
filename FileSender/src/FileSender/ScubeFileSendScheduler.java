package FileSender;
import java.io.File;

import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FileSender.util.CommonUtility;

public class ScubeFileSendScheduler implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(ScubeFileSendScheduler.class);

	public static final String PROPERTIES_PATH = ScubeFileSendScheduler.class.getResource("").getPath() + "conf/scheduler.properties";
	public static final String PID_PATH = System.getenv().get("PID_PATH");

	private File pidFile;

	private boolean shutdownRequested = false;

	public ScubeFileSendScheduler() {
		addDaemonShutdownHook();
	}

	private void addDaemonShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (!isShutdownRequested()) {
					shutdown();
				}
			}
		});
	}

	public void run() {
		logger.info("Start ScubeSheduler....");
		try {
			getConfig();
			startup();
		} catch (Exception e) {
			logger.error(CommonUtility.getPrintStacTraceString(e));
			e.printStackTrace();
		} finally {
			shutdown();
		}
	}

	private void startup() throws Exception{
		SchedulerFactory factory = new StdSchedulerFactory(PROPERTIES_PATH);
		Scheduler scheduler = factory.getScheduler();
		scheduler.start();
	}

	private void getConfig() throws Exception {
		pidFile = new File(PID_PATH);

		if (isExistPidFile()) {
			logger.error("Could not make pid File");
			System.exit(1);
		}
	}

	private boolean isShutdownRequested() {
		return shutdownRequested;
	}

	private boolean isExistPidFile() {
		return pidFile.exists();
	}

	private void shutdown() {
		if (logger.isInfoEnabled()) {
			logger.info("## Stop ScubeScheduler....");
		}

		if (isExistPidFile()) {
			pidFile.delete();
		}
	}

	public static void main(String[] args) throws Exception {
		SchedulerFactory factory = new StdSchedulerFactory(PROPERTIES_PATH);
		Scheduler scheduler = factory.getScheduler();
		scheduler.start();
	}
}