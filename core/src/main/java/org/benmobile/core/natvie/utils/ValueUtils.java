package org.benmobile.core.natvie.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/*
 * Class description: value judgment helper classes
 * @author  tianshuguang@tomstaff.com
 * @date    2014-7-25
 * @version 1.0
 */
public class ValueUtils {
	/*
	 The method name：isNotNullString<BR>
	 * This method describes：The string; Sentenced to empty
	 * @param presentStationOrderId
	 * @return
	 */
	public static boolean isNotNullString(String presentStationOrderId) {
		return null != presentStationOrderId
				&& !presentStationOrderId.equals("")
				&& !presentStationOrderId.equals("null");
	}

	/*
	 *  To judge whether the List is not empty
	 * @param noteList
	 * @param noteList
	 * @return
	 */
	public static boolean isListNotEmpty(List<?> noteList) {
		return null != noteList && noteList.size() > 0;
	}

	/*
	 * To judge whether the List is empty
	 * @param noteList
	 * @param noteList
	 * @return
	 */
	public static boolean isListEmpty(List<?> noteList) {
		return null == noteList || noteList.size() == 0;
	}

	/*
	 *  To determine whether a String String is empty
	 * @param value
	 * @return
	 */
	public static boolean isStrEmpty(String value) {
		if (null == value || "".equals(value.trim())) {
			return true;
		} 
		return false;
	}

	/*
	 *  To judge whether the object is not empty
	 * @param value
	 * @return
	 */
	public static boolean isStrNotEmpty(String value) {
		return !isStrEmpty(value);
	}

	/*
	 *  To judge whether the object is not empty
	 * @param object
	 * @return
	 */
	public static boolean isNotEmpty(Object object) {
		return null != object;
	}

	/*
	 * To judge whether the object is not empty
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(Object object) {
		return null == object;
	}

	/*
	 * In the content of judgment in multiple EditText or TextView have a null return true
	 * @param views
	 * @return
	 */
	public static boolean isHasEmptyView(View... views) {
		for (View v : views) {
			if (!v.isShown()) {// Not visible do not judge
				continue;
			}
			if (v instanceof EditText) {
				EditText et = (EditText) v;
				if (TextUtils.isEmpty(et.getText().toString().trim())) {
					return true;
				}
			} else if (v instanceof TextView) {
				TextView tv = (TextView) v;
				if (TextUtils.isEmpty(tv.getText().toString().trim())) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	*The Boolean true into "1" false into "0"
	 * @param b
	 * @return
	 */
	public static String bolean2String(boolean b) {
		return b ? "1" : "0";
	}
	

	public static boolean isChinese(String str) throws PatternSyntaxException {
		/**The regular expression to determine whether the input into Chinese*/
		String regEx = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$";
		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(str);  
		return matcher.matches();
	}

	/*
	 * Accurate to percentile, decimal places less than zero.
	 * 	For example: 20 > 20.00, 21.1111 > 21.11, 21.55555 > 21.56
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

	/*
	* Accurate to percentile, decimal places less than zero.
	 * 	For example: 20 > 20.00, 21.1111 > 21.11, 21.55555 > 21.56
	 * @param number
	 * @return
	 */
	public static String format2Percentile(double number) {
		return format2Percentile(Double.toString(number));
	}

	/*
	 * Upc format to add Spaces
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
