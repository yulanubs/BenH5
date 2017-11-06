package org.benmobile.analysis.secret;

import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

import org.benmobile.analysis.tools.Logger;

public class DESSecret {
	/**
	 *
	 * @param encryptString
	 * @param encryptKey
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	public static String encryptDES(String encryptString, String encryptKey,byte[] iv)
			throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes("UTF-8"),
				"DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));
		String temp = Base64.encodeToString(encryptedData, Base64.DEFAULT);
		String secret = URLEncoder.encode(URLEncoder.encode(temp));
		Logger.debug("DESSecret", "secret is:"+secret);
		return secret;
	}
}
