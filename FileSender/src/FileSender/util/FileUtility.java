package FileSender.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtility {
	private static final Logger logger = LoggerFactory.getLogger(FileUtility.class);

	public static final int DEFAULT_BUFFER_SIZE = 1448;
	private static final String DEFAULT_CHARSET = "UTF-8";

	public static boolean copy(String source, String target) {
		return copy(new File(source), new File(target));
	}

	public static boolean copy(File source, File target) {
		boolean result = false;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			File file = new File(target.getParent());
			if (!file.exists()) {
				file.mkdirs();
			}

			bis = new BufferedInputStream(new FileInputStream(source),
					DEFAULT_BUFFER_SIZE);
			bos = new BufferedOutputStream(new FileOutputStream(target),
					DEFAULT_BUFFER_SIZE);

			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

			int readBytes;
			while ((readBytes = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, readBytes);
			}

			bis.close();
			bos.close();

			result = true;
		} catch (IOException e) {
			logger.error("copy error[{}][{}]\n {}", source, target, e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
		}

		return result;
	}

	public static boolean copy(InputStream input, File target) {
		boolean result = false;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			File file = new File(target.getParent());
			if (!file.exists()) {
				file.mkdirs();
			}

			bis = new BufferedInputStream(input, DEFAULT_BUFFER_SIZE);
			bos = new BufferedOutputStream(new FileOutputStream(target),
					DEFAULT_BUFFER_SIZE);

			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

			int readBytes;
			while ((readBytes = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, readBytes);
			}

			bis.close();
			bos.close();

			result = true;
		} catch (IOException e) {
			logger.error("copy error [{}] \r\n {}", target, e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
		}

		return result;
	}

	public static boolean appendCopy(File source, File target) {
		boolean result = false;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			File file = new File(target.getParent());
			if (!file.exists()) {
				file.mkdirs();
			}

			bis = new BufferedInputStream(new FileInputStream(source),
					DEFAULT_BUFFER_SIZE);
			bos = new BufferedOutputStream(new FileOutputStream(target, true),
					DEFAULT_BUFFER_SIZE);

			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

			int readBytes;
			while ((readBytes = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, readBytes);
			}

			bis.close();
			bos.close();

			result = true;
		} catch (IOException e) {
			logger.error("copy error [{}] to [{}] \n {}", source, target, e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
		}

		return result;
	}

	public static boolean renameTo(String source, String target) {
		return renameTo(new File(source), new File(target));
	}

	public static boolean renameTo(File source, File target) {
		boolean result = false;

		if (source.exists()) {
			File file = new File(target.getParent());
			if (!file.exists()) {
				file.mkdirs();
			}

			if (target.exists()) {
				target.delete();
			}

			result = source.renameTo(target);
			if (result) {
					logger.debug("renameTo success [{}]==>[{}]", source, target);
			} else {
				result = copy(source, target);
				if (result) {
					result = delete(source);
					if (result) {
						logger.debug("copy/delete success [{}]==>[{}]", source, target);
					} else {
						logger.error("renameTo failed [{}]==>[{}]", source, target);

						delete(target);
					}
				} else {
					logger.error("renameTo failed [{}]==>[{}]", source, target);
				}
			}
		} else {
			logger.error("renameTo error (source file not found) [{}]", source);
		}

		return result;
	}

	public static boolean write(String source, InputStream is) {
		return write(new File(source), is);
	}

	public static boolean write(File source, InputStream is) {
		boolean result = false;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(is, DEFAULT_BUFFER_SIZE);
			bos = new BufferedOutputStream(new FileOutputStream(source),
					DEFAULT_BUFFER_SIZE);

			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

			int readBytes;
			while ((readBytes = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, readBytes);
			}

			bis.close();
			bos.close();

			result = true;
		} catch (IOException e) {
			if (logger.isErrorEnabled()) {
				logger.error("write error: " + e);
			}
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
		}

		return result;
	}

	public static boolean write(String source, String body) {
		return write(new File(source), body, DEFAULT_CHARSET);
	}

	public static boolean write(String source, String body, String charset) {
		return write(new File(source), body, charset);
	}

	public static boolean write(File source, String body) {
		return write(source, body, DEFAULT_CHARSET);
	}

	public static boolean write(File source, String body, String charset) {
		boolean result = false;

		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(source), charset), DEFAULT_BUFFER_SIZE);
			writer.write(body);
			writer.flush();

			result = true;
		} catch (IOException e) {
				logger.error("write error: {}", e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}

		return result;
	}

	public static boolean write(String source, List<String> bodies,
			String charset, String seperation) {
		String[] body = new String[bodies.size()];
		return write(new File(source), bodies.toArray(body), charset,
				seperation);
	}

	public static boolean write(File source, String[] bodies, String charset,
			String seperation) {
		boolean result = false;

		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(source), charset), DEFAULT_BUFFER_SIZE);
			for (String body : bodies) {
				writer.write(body + (seperation == null ? "" : seperation));
			}
			writer.flush();

			result = true;
		} catch (IOException e) {
			logger.error("write error: {}", e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}

		return result;
	}

	public static boolean delete(String target) {
		return delete(new File(target));
	}

	public static boolean delete(File target) {
		if (target.exists() && target.isFile()) {
			boolean result = target.delete();

			logger.debug("delete [{}][{}]", target.getAbsolutePath(), (result ? "success" : "failed"));

			return result;
		} else {
			logger.warn("delete error(File Not Found) [{}]", target.getAbsolutePath());
			return false;
		}
	}

	public static void delete(File[] target) {
		for (int i = 0; i < target.length; i++) {
			if (target[i].exists() && target[i].isFile()) {
				boolean result = target[i].delete();

				logger.debug("delete [{}][{}]", target[i].getName(), (result ? "success" : "failed"));
			} else {
				if (logger.isErrorEnabled()) {
					logger.error("delete error(File Not Found) [{}]", target[i].getAbsolutePath());
				}
			}
		}
	}

	public static long size(String target) {
		return size(new File(target));
	}

	public static long size(File target) {
		long fileSize = 0;

		if (target.exists() && target.isFile()) {
			fileSize = target.length();
		}

		return fileSize;
	}
}