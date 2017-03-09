package org.benmobile.log;

import android.os.Environment;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class BenH5Log {

	private static boolean mFile = false;
	private static boolean mException = false;
	private static String fileName = "/.log.log";
	private static String packageName;

	public static void setEnable(String tag,boolean mIsPrintD, boolean mIsPrintE, boolean mIsPrintI,
								 boolean mIsPrintV, boolean mIsPrintW,boolean mIsPrintA,boolean mIsPrintJ,boolean mIsPrintX,boolean mIsPrintF,boolean exception) {
		Logger.init(tag,mIsPrintD,mIsPrintE,mIsPrintI,mIsPrintV,mIsPrintW,mIsPrintA,mIsPrintJ,mIsPrintX,mIsPrintF);
		mFile=mIsPrintF;
		mException = exception;
	}
	public static void setEnable(){
		Logger.init().logLevel(LogLevel.FULL).logTool(new AndroidLogTool());
	}
	public static void Disable(){
		Logger.init().logLevel(LogLevel.NONE).logTool(new AndroidLogTool());
	}
	public static boolean isException(){
		return mException;
	}
	
	public static void excaption(Exception exception){
		if(exception == null){
			return;
		}
		if(mException){
			exception.printStackTrace();
		}
	}

	private static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}

	public static void setPackageName(String package_name) {
		packageName = package_name;
	}

	public static void setfileLogEnable(boolean enable) {
		mFile = enable;
	}

	public static void debug(String tag, String message) {

		if (isEmpty(message)) {
			message = "null";
		}
		Logger.t(tag);
		Logger.d(message);
		writeToLogFile(tag, message);
	}

	public static void error(String tag, String message) {
		if (isEmpty(message)) {
			message = "null";
		}
		Logger.t(tag);
		Logger.e(message);
		writeToLogFile(tag, message);
	}

	public static void info(String tag, String message) {
		if (isEmpty(message)) {
			message = "null";
		}
		Logger.t(tag);
		Logger.i(message);
		writeToLogFile(tag, message);
	}
	public static void json(String tag, String message) {
		if (isEmpty(message)) {
			message = "null";
		}
		Logger.t(tag);
		Logger.json(message);
		writeToLogFile(tag, message);
	}
	public static void xml(String tag, String message) {
		if (isEmpty(message)) {
			message = "null";
		}
		Logger.t(tag);
		Logger.xml(message);
		writeToLogFile(tag, message);
	}

	public static void verbose(String tag, String message) {
		if (isEmpty(message)) {
			message = "null";
		}
		Logger.t(tag);
		Logger.v(message);
		writeToLogFile(tag, message);
	}

	public static void warn(String tag, String message) {
		if (isEmpty(message)) {
			message = "null";
		}
		Logger.t(tag);
		Logger.w(message);
		writeToLogFile(tag, message);
	}

	public synchronized static void writeToLogFile(String tag, String message) {
		if (!mFile) {
			return;
		}
		File logFile = createOrOpenLogFile();

		if (logFile == null) {
			return;
		}

		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(logFile, "rw");

			raf.seek(raf.length());
			raf.writeBytes(current());
			raf.writeBytes("\t");
			raf.writeBytes("TAG:\t");
			raf.write(tag.getBytes("gbk"));
			raf.writeBytes("\t\t");
			raf.write(message.getBytes("gbk"));
			raf.writeBytes("\n");
			raf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			BenH5Log.excaption(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			BenH5Log.excaption(e);
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					BenH5Log.excaption(e);
				}
			}
		}
	}

	private static String current() {
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssss");
		return sdf.format(new Date(now));
	}

	private static String getLogFileName() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String sdcardRootDir = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			if (packageName != null) {
				return sdcardRootDir + "/" + packageName + fileName;
			}
			return sdcardRootDir + "/.ULE" + fileName;
		}
		return null;
	}

	private static File createOrOpenLogFile() {
		String name = getLogFileName();
		if (name == null) {
			return null;
		}
		boolean suc = false;
		File log = new File(name);

		if (!log.exists()) {
			String parent = log.getParent();
			File dir = new File(parent);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			try {
				suc = log.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				BenH5Log.excaption(e);
				return null;
			}
		} else {
			long size = log.length();
			if (size > 10 * 1000 * 1000) {
				log.delete();
				try {
					suc = log.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					BenH5Log.excaption(e);
				}
			} else {
				suc = true;
			}
		}
		if (suc) {
			return log;
		} else {
			return null;
		}
	}

}
