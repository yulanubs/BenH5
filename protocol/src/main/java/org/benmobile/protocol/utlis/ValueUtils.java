//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.benmobile.protocol.utlis;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ValueUtils {
    public ValueUtils() {
    }

    public static boolean isNotNullString(String presentStationOrderId) {
        return presentStationOrderId != null && !presentStationOrderId.equals("") && !presentStationOrderId.equals("null");
    }

    public static boolean isListNotEmpty(List<?> noteList) {
        return noteList != null && noteList.size() > 0;
    }

    public static boolean isListEmpty(List<?> noteList) {
        return noteList == null || noteList.size() == 0;
    }

    public static boolean isStrEmpty(String value) {
        return value == null || "".equals(value.trim());
    }

    public static boolean isStrNotEmpty(String value) {
        return !isStrEmpty(value);
    }

    public static boolean isNotEmpty(Object object) {
        return object != null;
    }

    public static boolean isEmpty(Object object) {
        return object == null;
    }

    public static boolean isHasEmptyView(View... views) {
        View[] var4 = views;
        int var3 = views.length;

        for(int var2 = 0; var2 < var3; ++var2) {
            View v = var4[var2];
            if(v.isShown()) {
                if(v instanceof EditText) {
                    EditText tv = (EditText)v;
                    if(TextUtils.isEmpty(tv.getText().toString().trim())) {
                        return true;
                    }
                } else if(v instanceof TextView) {
                    TextView var6 = (TextView)v;
                    if(TextUtils.isEmpty(var6.getText().toString().trim())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static String bolean2String(boolean b) {
        return b?"1":"0";
    }

    public static boolean isChinese(String str) throws PatternSyntaxException {
        String regEx = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(str);
        return matcher.matches();
    }

    public static String format2Percentile(String number) {
        String strFormat = "%,.2f";
        Double doubleMoney = Double.valueOf(0.0D);
        if(number != null && number.length() >= 1) {
            try {
                doubleMoney = Double.valueOf(number);
            } catch (NumberFormatException var4) {
                var4.printStackTrace();
                doubleMoney = Double.valueOf(0.0D);
                return String.format(strFormat, new Object[]{doubleMoney});
            }
        } else {
            doubleMoney = Double.valueOf(0.0D);
        }

        return String.format(strFormat, new Object[]{doubleMoney});
    }

    public static String format2Percentile(double number) {
        return format2Percentile(Double.toString(number));
    }

    public static String formatUpc(String upc) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(upc);

        for(int index = 0; index < buffer.length(); ++index) {
            if(index == 1 || index == 8) {
                buffer.insert(index, ' ');
            }
        }

        char[] tempChar = new char[buffer.length()];
        buffer.getChars(0, buffer.length(), tempChar, 0);
        String str = buffer.toString();
        return str;
    }
}
