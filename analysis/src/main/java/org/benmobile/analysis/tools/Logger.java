package org.benmobile.analysis.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

import org.benmobile.analysis.MobileLogConsts;
import org.benmobile.analysis.SyknetMobileLog;

public class Logger {

	public static void error(String tag, String msg) {
		if (SyknetMobileLog.DEBUG) {
			Log.e(tag, msg);
			writeToLogFile(tag, msg);
		}

	}

	public static void verbose(String tag, String msg) {
		if (SyknetMobileLog.DEBUG) {
			Log.v(tag, msg);
			writeToLogFile(tag, msg);
		}

	}

	public static void debug(String tag, String msg) {
		if (SyknetMobileLog.DEBUG) {
			Log.d(tag, msg);
			writeToLogFile(tag, msg);
		}

	}

	public static void info(String tag, String msg) {
		if (SyknetMobileLog.DEBUG) {
			Log.i(tag, msg);
			writeToLogFile(tag, msg);
		}

	}

	public static void warn(String tag, String msg) {
		if (SyknetMobileLog.DEBUG) {
			Log.w(tag, msg);
			writeToLogFile(tag, msg);
		}

	}

	public static void exception(Exception e) {
		if (e == null) {
			return;
		}
		if (SyknetMobileLog.DEBUG) {
			e.printStackTrace();
			StringBuilder sb = new StringBuilder();
			StackTraceElement[] trace = e.getStackTrace();
			for (int i = 0; i < trace.length; i++) {
				sb.append(trace[i].toString()).append("\n");
			}
			writeToLogFile(e.getMessage(), sb.toString());
		}

	}

	public synchronized static void writeToLogFile(String tag, String message) {
		File logFile = initLogFile();

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
			exception(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			exception(e);
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					exception(e);
				}
			}
		}
	}

	private static String current() {
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssss");
		return sdf.format(new Date(now));
	}

	private static File initLogFile() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return null;
		}
		File dir;
		String sdPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		if (MobileLogConsts.packageName != null
				&& !MobileLogConsts.packageName.equals("")) {
			dir = new File(sdPath + "/" + MobileLogConsts.packageName + "/");
		} else {
			return null;
		}
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File log = new File(dir.getAbsolutePath() + "/"
				+ MobileLogConsts.LOG_FILE_NAME);
		if (!log.exists()) {
			try {
				log.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				exception(e);
				return null;
			}
		}

		return log;
	}

}
