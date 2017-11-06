package org.benmobile.analysis.tools;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ValueUtils {

	/**
	 *
	 * @param noteList
	 * @return
	 */
	public static boolean isListNotEmpty(List<?> noteList) {
		return null != noteList && noteList.size() > 0;
	}

	/**
	 *
	 * @param noteList
	 * @return
	 */
	public static boolean isListEmpty(List<?> noteList) {
		return null == noteList || noteList.size() == 0;
	}

	/**
	 *
	 * @param value
	 * @return
	 */
	public static boolean isStrEmpty(String value) {
		if (null == value || "".equals(value.trim())) {
			return true;
		} 
		return false;
	}

	/**
	 *
	 * @param value
	 * @return
	 */
	public static boolean isStrNotEmpty(String value) {
		return !isStrEmpty(value);
	}
	/**
	 * @param object
	 * @return boolean
	 */
	public static boolean isNotEmpty(Object object) {
		return null != object;
	}

	/**
	 *
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(Object object) {
		return null == object;
	}


	/**
	 *
	 * @param b
	 * @return
	 */
	public static String bolean2String(boolean b) {
		return b ? "1" : "0";
	}

	/**
	 *
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static boolean isChinese(String str) throws PatternSyntaxException {
		String regEx = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$";
		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(str);  
		return matcher.matches();
	}

	/**
	 *
	 * @param number
	 * @return
	 */
	public static String format2Percentile(String number) {
		String strFormat = "%,.2f";
		Double doubleMoney = 0.00;
		
		if (number == null || number.length() < 1) {
			doubleMoney = 0.00;
		} else {
			try {
				doubleMoney = Double.valueOf(number);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				doubleMoney = 0.00;
				return String.format(strFormat, doubleMoney);
			}
		}
		return String.format(strFormat, doubleMoney);
	}

	/**
	 *
	 * @param number
	 * @return
	 */
	public static String format2Percentile(double number) {
		return format2Percentile(Double.toString(number));
	}

	/**
	 *
	 * @param upc
	 * @return
	 */
	public static String formatUpc(String upc) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(upc);
		int index = 0;
		while (index < buffer.length()) {
			if ((index == 1 || index == 8)) {
				buffer.insert(index, ' ');
			}
			index++;
		}
		char[] tempChar = new char[buffer.length()];
		buffer.getChars(0, buffer.length(), tempChar, 0);
		String str = buffer.toString();
		return str;
	};
	
}
