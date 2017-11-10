package org.benmobile.analysis.secret;

import java.util.Vector;

import org.benmobile.analysis.tools.StringHelper;

public class SignEnc {
	

	public static final String CHARACTER_TYPE = "UTF-8";
	public static final String KEY_MD5 = "MD5";
	public static final String KEY_MAC = "HmacSHA1";
	public static final String PARAMS_ERROR = "params error!";
	public static final String SIGN_ERROR = "sign error!";
	public static final String VERIFY_ERROR = "verify error!";
	public static final String SECRETTYPE = "0";
	/**
	 */
	public static final String[]SIGN_TYPE = new String[]{"1","2" };
	
	
	/**
	 * @param params
	 * @param secret
	 * @throws Exception 
	 */
	public static String sign(String params, String secret) throws SignEncException {
		String result = null;
		result = hmacSign(params,secret);
		
		return result;
	}
	
	

	/**
	 * hmac
	 * @param params
	 * @param secret APP_SECRET
	 */
	public static String hmacSign(String params, String secret) throws SignEncException {
		String result = null;
		try {
			StringBuffer orgin = getBeforeSign(params, new StringBuffer());
			if (orgin == null){
				return result;
			}			
        	result = StringHelper.doBase64URLEncode(StringHelper.byte2hex(encryptHMAC(orgin.toString(), secret, CHARACTER_TYPE)),CHARACTER_TYPE);
		} catch (Exception e) {
//			Logger.exception(e);
			throw new SignEncException(SIGN_ERROR+","+e.getMessage());
		}
		return result;
	}
	
	/**
	 * HMAC
	 * @param data
	 * @param key
	 * @param characterCodeType
	 * @return
	 */
	private static byte[] encryptHMAC(String data, String key, String characterCodeType) {
		byte[] b = HMACSHA1.getHmacSHA1(data, key);
		return b;
	}
	/**
	 *
	 * @param params  
	 * @param orgin
	 * @return
	 */
	private static StringBuffer getBeforeSign(String params, StringBuffer orgin) throws Exception {
		try{
			if (params == null || "".equals(params))
				return null;
			Vector<String[]> paramVector = new Vector<String[]>();
			String[] paramArray = params.split("&");
			for (int i = 0; i < paramArray.length; i++) {
				if(paramArray[i].split("=").length == 2) {
					String[] temp = new String[]{paramArray[i].split("=")[0], paramArray[i].split("=")[1]}; 
					paramVector.addElement(temp);
				} else {
					throw new SignEncException(PARAMS_ERROR);
				}
			}
			if(paramVector != null && paramVector.size() > 0) {
				for (int i = 0; i < paramVector.size(); i++) {
					
					orgin.append(((String[])paramVector.elementAt(i))[0]).append(((String[])paramVector.elementAt(i))[1]);
				}
			}
		}catch(Exception e){
			throw new SignEncException(e);
		}
		return orgin;
	}
	
	
	/**
	 * test only!!!!!!
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			String data = "userID=663&orderId=100031634&payType=2&payPwd=123123";

			String key = "bff6c876dbda4ea49ec9df557ac7a197";
//			our(data, key);
//			standard(data, key);
//			encryptHMAC(data, key);
			
			sign(data,key);
			//sign("userID=663&orderId=100031622&payPwd=123123&payType=2","bff6c876dbda4ea49ec9df557ac7a197");
		} catch (Exception e) {
		}
	}
	
	
	
}
