package FileSender.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckSumUtility {
	private static final Logger logger = LoggerFactory.getLogger(CheckSumUtility.class);

	public static long getCRC32Value(String filename) {
		File f = new File(filename);
		Checksum crc = new CRC32();

		if (f.exists()) {
			BufferedInputStream in = null;
			try {
				in = new BufferedInputStream(new FileInputStream(filename));
				byte buffer[] = new byte[32768];
				int length = 0;

				while ((length = in.read(buffer)) >= 0)
					crc.update(buffer, 0, length);
			} catch (IOException e) {
				LoggerUtility.error(logger, e);
			} finally {
				CloseUtility.closeInputStream(in);
			}
		}
		return crc.getValue();
	}

	public static String getMD5Value(String filename) {
		String md5_digest = "";

		MessageDigest md5  = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DigestInputStream dis = null;

		try {
			md5 = MessageDigest.getInstance("MD5");

			fis = new FileInputStream(filename);
			bis = new BufferedInputStream(fis);
			dis = new DigestInputStream(bis, md5);

			byte[] buffer = new byte[32768];
			while (dis.read(buffer) >= 0);

			byte[] digest = md5.digest();
			for (int i = 0; i < digest.length; i++) {
				String value = Integer.toHexString(digest[i] & 0xFF);
				if (value.length() == 1)
					value = "0" + value;
				md5_digest = md5_digest + value;
			}

		} catch (Exception e) {
			LoggerUtility.error(logger, e);
		} finally {
			CloseUtility.closeInputStream(fis, bis, dis);
		}

		return md5_digest;
	}
}