package co.kr.s3i.S3IFileSender.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class CommonUtility {
	public static List<String> getPattern(boolean insensitive, String pattern, String input) {
		List<String> matchList = new ArrayList<String>();

		if (input != null) {
			Pattern p = null;
			if (insensitive) {
				p = Pattern.compile(pattern);
			} else {
				p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
			}

			Matcher m = p.matcher(input);
			while (m.find()) {
				if (!matchList.contains(m.group(0))) {
					matchList.add(m.group(0));
				}
			}
		}

		return matchList;
	}

	public static boolean regexCheck(String regex, String input) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		return m.matches();
	}

	public static boolean isValidURL(String url) {
		if (url == null) {
			return false;
		}

		String[] list = url.split("\\.");

		if (list.length < 2) {
			return false;
		}

		String regex = "^[^ -,\\/:-@\\[-`{-}~]+$";

		for (String s : list) {
			if (! regexCheck(regex, s)) {
				return false;
			}
		}

		String suffix = list[list.length - 1];
		regex = "^[a-z]+$";

		if (! regexCheck(regex, suffix)) {
			return false;
		}

		int length = suffix.length();
		if (length < 2 || length > 5) {
			return false;
		}

		return true;
	}

	public static boolean isValidIP(String ip) {
		if (ip == null) {
			return false;
		}

		String regex = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";

		if (! regexCheck(regex, ip)) {
			return false;
		}

		String[] list = ip.split(".");
		for (String s : list) {
			int i = Integer.parseInt(s);
			if (i < 0 || i > 255) {
				return false;
			}
		}

		return true;
	}

	public static boolean emptyStringCheck(String string) {
		return (string == null || string.trim().equals(""));
	}

	public static String emptyValue(String input) {
		if (input == null || input.equals("")) {
			return "&nbsp;";
		}
		return input;
	}

	public static String replaceSearchValue(String input) {
		if (input == null) {
			return null;
		}

		return input.replaceAll("(%|_|'|\\\\)", "\\\\$1");
	}

	public static String replacern(String input, String target) {
		if (input == null) {
			return null;
		}

		return input.replace(target, "\r\n");
	}

	public static String nl2br(String input) {
		if (input == null) {
			return null;
		}

		return input.replaceAll("(\r\n|\r|\n)", "<br />");
	}

	public static String subString(String s, int length) {
		int o = 0, l = 0, j = 0;

		try {
			byte[] bytes = s.getBytes("UTF-8");
			int bl = bytes.length;

			while (j < bl) {
				if ((bytes[j] & 0x80) != 0) {
					if (o + 2 > length) {
						break;
					}
					o += 2;
					l += 3;
					j += 3;
				} else {
					if (o + 1 > length) {
						break;
					}
					++o;
					++l;
					++j;
				}
			}

			s = new String(bytes, 0, l, "UTF-8");

			if (l < bl) {
				s += "...";
			}
		} catch (UnsupportedEncodingException e) {
		}

		return s;
	}

	public static Charset getCharset(String str) {
		String[] charsetsToBeTested = { "UTF-8", "EUC-KR", "UNICODE" };

		Charset charset = null;
		for (String charsetName : charsetsToBeTested) {
			charset = Charset.forName(charsetName);
			CharsetDecoder decoder = charset.newDecoder();
			decoder.reset();
			if (identify(str.getBytes(), decoder)) {
				break;
			}
		}
		return charset;
	}

	public static boolean identify(byte[] bytes, CharsetDecoder decoder) {
		try {
			decoder.decode(ByteBuffer.wrap(bytes));
		} catch (CharacterCodingException e) {
			return false;
		}
		return true;
	}

	public static String newString(String oriStr, String char1, String char2) {
		String str = "";
		if (!emptyStringCheck(char1) && !emptyStringCheck(char2)) {
			try {
				str = new String(oriStr.getBytes(char1), char2);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return str;
	}

	public static boolean isValidRange(String str, int minRange, int maxRange) {
		int i =0;
		try {
			i = Integer.parseInt(str);
		} catch (Exception e) {
			return false;
		}
		
		if(i>minRange && i<maxRange) {
			return true;
		}
		return false;
	}

	public static boolean isValidRange(int i, int minRange, int maxRange) {
		if(i>minRange && i<maxRange) {
			return true;
		}
		return false;
	}

	//checksum 함수
	public static long getCRC32Value(String filename) throws IOException {
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
				throw e;
			} finally {
				if( in != null){
					in.close();
				}
			}
		}
		return crc.getValue();
	}

	public static String getMD5Value(String filename) throws Exception {
		String md5_digest = "";

		MessageDigest md5 = MessageDigest.getInstance("MD5");

		FileInputStream inFile = null;
		InputStream inBuffered = null;
		InputStream inputStream = null;
		try {
			inFile = new FileInputStream(filename);
			inBuffered = new BufferedInputStream(inFile);
			inputStream = new DigestInputStream(inBuffered, md5);

			byte[] buffer = new byte[32768];
			while (inputStream.read(buffer) >= 0)
				;
		} finally {
			if (inFile != null)
				inFile.close();
			if (inBuffered != null)
				inBuffered.close();
			if (inputStream != null)
				inputStream.close();
		}

		byte[] digest = md5.digest();
		for (int i = 0; i < digest.length; i++) {
			String value = Integer.toHexString(digest[i] & 0xFF);
			if (value.length() == 1)
				value = "0" + value;
			md5_digest = md5_digest + value;
		}

		return md5_digest;
	}

	public static String nvl(String input,String def) {
		if (input == null || input.equals("")) {
			if (def == null || def.equals("")) {
				return "";
			}else{
				return def;
			}
		}else{
			return input;
		}
	}

	public static String getPrintStacTraceString(Exception e) {
		String returnValue = "";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(out);
		e.printStackTrace(printStream);

		returnValue = out.toString();
		return returnValue;
	}
}
