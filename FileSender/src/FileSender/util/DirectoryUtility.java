package FileSender.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DirectoryUtility {
	private static final Logger logger = LoggerFactory.getLogger(DirectoryUtility.class);

	public static boolean exists(String target) {
		return exists(new File(target));
	}

	public static boolean exists(File target) {
		return target.isDirectory() && target.exists();
	}

	public static boolean mkdirs(String target) {
		return mkdirs(new File(target));
	}

	public static boolean mkdirs(File target) {
		if (target.exists()) {
			if (target.isDirectory()) {
				logger.debug("already exists directory [{}]", target.getAbsolutePath());
				return true;
			} else {
				logger.error("already exists file [{}]", target.getAbsolutePath());
				return false;
			}
		} else {
			if (target.mkdirs()) {
				return true;
			} else {
				logger.error("directory make failed: [{}]", target.getAbsolutePath());
				return false;
			}
		}
	}

	public static boolean mkdir(String target) {
		return mkdir(new File(target));
	}

	public static boolean mkdir(File target) {
		if (target.exists()) {
			if (target.isDirectory()) {
				logger.debug("already exists directory [{}]", target.getAbsolutePath());
				return true;
			} else {
				logger.error("already exists file [{}]", target.getAbsolutePath());
				return false;
			}
		} else {
			if (target.mkdir()) {
				return true;
			} else {
				logger.error("directory make failed [{}]", target.getAbsolutePath());
				return false;
			}
		}
	}

	public static boolean delete(String target) {
		return delete(new File(target));
	}

	public static boolean delete(File target) {
		if (target.isDirectory()) {
			File[] fs = target.listFiles();

			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isFile()) {
					fs[i].delete();
				} else {
					delete(fs[i]);
				}
			}
		}

		boolean result = target.delete();

		logger.debug("delete directory [{}],[{}], result[{}]", (target.isDirectory() ? "Directory" : "File"), target.getAbsolutePath(), result);

		return result;
	}

	public static boolean copy(String source, String target) {
		return copy(new File(source), new File(target));
	}

	public static boolean copy(File source, File target) {
		File fs[] = source.listFiles();

		if (!source.exists()) {
			logger.error("source Directory Not Found![{}]", source.getAbsolutePath());
			return false;
		}

		for (int i = 0; i < fs.length; i++) {
			if (fs[i].isDirectory()) {
				File copyFile = new File(target.getAbsolutePath()
						+ File.separatorChar + fs[i].getName());
				if (!copyFile.exists()) {
					copyFile.mkdir();
				}

				copy(fs[i], copyFile);
			} else {
				FileUtility.copy(fs[i], new File(target, fs[i].getName()));

				logger.debug("in Directory file copy [{}]", target.getAbsolutePath());
			}
		}

		return true;
	}
}
