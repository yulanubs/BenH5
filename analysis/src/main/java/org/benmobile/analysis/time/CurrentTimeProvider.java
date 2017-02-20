package org.benmobile.analysis.time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

import org.benmobile.analysis.MobileLogConsts;

public class CurrentTimeProvider {

	private static Object lock = new Object();

	public static String getCurrentTime(Context context) {
		synchronized (lock) {
			long now = System.currentTimeMillis();
			long timeOffect = context.getSharedPreferences(
					MobileLogConsts.SHARED_PREFERENCE_NAME,
					Context.MODE_PRIVATE).getLong(MobileLogConsts.TIME_DIFF, 0);
			long realNow = now + timeOffect;
			SimpleDateFormat sdf = new SimpleDateFormat(
					MobileLogConsts.TIME_FORMATE, Locale.CHINA);
			Date d = new Date(realNow);
			return sdf.format(d);
		}
	}

	public static void updateTimeOffect(long current, Context context) {
		synchronized (lock) {
			long now = System.currentTimeMillis();
			long timeOffect = current - now;
			context.getSharedPreferences(
					MobileLogConsts.SHARED_PREFERENCE_NAME,
					Context.MODE_PRIVATE).edit()
					.putLong(MobileLogConsts.TIME_DIFF, timeOffect).commit();
		}
	}

	public static long getCurrentTimeMillis(Context context) {
		synchronized (lock) {
			long now = System.currentTimeMillis();
			long timeOffect = context.getSharedPreferences(
					MobileLogConsts.SHARED_PREFERENCE_NAME,
					Context.MODE_PRIVATE).getLong(MobileLogConsts.TIME_DIFF, 0);
			return now + timeOffect;
		}
	}

	public static String getCurrrentSystemTime() {
		synchronized (lock) {
			long now = System.currentTimeMillis();
			Date d = new Date(now);
			SimpleDateFormat sdf = new SimpleDateFormat(
					MobileLogConsts.TIME_FORMATE, Locale.CHINA);
			return sdf.format(d);
		}
	}
	
	public static boolean isDateZero(String date){
		if (date.startsWith("1970")){
			return true;
		}
		return false;
	}
}
