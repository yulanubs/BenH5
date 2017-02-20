package org.benmobile.analysis.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.util.Log;

import org.benmobile.analysis.MobileLogConsts;
import org.benmobile.analysis.secret.SignEnc;
import org.benmobile.analysis.secret.SignEncException;
import org.benmobile.analysis.tools.Logger;

public class HttpEntity {

	private String url;
	private String result;
	
	public HttpEntity(String url){
		this.url = url;
	}
	
	public boolean run(Map<String, String> params){
		System.setProperty("http.keepAlive", "false");
		HttpURLConnection conn = connect();
		if (conn == null){
			return false;
		}
		byte[] data = processData(params);
		if (data == null){
			conn.disconnect();
			return false;
		}
		conn.setRequestProperty("Content-Length",
				String.valueOf(data.length));
		if (!sendData(data, conn)){
			conn.disconnect();
			return false;
		}
		result = readResponse(conn);
		conn.disconnect();
		if (result == null){
			return false;
		}
		Logger.debug("HttpEntity", "result: "+result);
		return true;
	}
	
	public String getResult(){
		return result;
	}
	
	private HttpURLConnection connect(){
		String mapedUrl = MobileLogConsts.server + url;
		try {
			URL U = new URL(mapedUrl);
			HttpURLConnection conn = (HttpURLConnection) U.openConnection();
			setHeaders(conn);
			return conn;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Logger.exception(e);
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.exception(e);
			return null;
		}
	}
	
	private void setHeaders(HttpURLConnection conn){
		conn.setDoInput(true);
		conn.setDoOutput(true);
		try {
			conn.setRequestMethod("POST");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			Logger.exception(e);
		}
		conn.setUseCaches(false);
		
		conn.setRequestProperty("Accept", "*/* ");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("appkey", MobileLogConsts.appkey);
		conn.setRequestProperty("version_no",
				MobileLogConsts.API_GATEWAY_version_no);
		conn.setRequestProperty("session_id", MobileLogConsts.deviceId);
		conn.setRequestProperty("user_token", "");
		conn.setRequestProperty("uuid", "");
		conn.setRequestProperty("Connection","close");
		conn.setReadTimeout(30000);
		conn.setConnectTimeout(30000);
		
	}
	
	private byte[] processData(Map<String, String> params){
		Set<String> keys = params.keySet();
		Iterator<String> keyIterator = keys.iterator();
		StringBuilder sb = new StringBuilder();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			String value = params.get(key);
			sb.append("&").append(key).append("=").append(value);
		}
		byte[] data = null;
		try {
			String p = "";
			if (sb.length() > 1){
				p = sb.substring(1);
			}
			
			Logger.debug("HttpEntity", "Entity: "+p);
			data = p.getBytes("UTF-8");
			String temp = new String(data);
			if (!temp.equals(p)){
				Log.e("processData", "getBytesError");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Logger.exception(e);
		}
		return data;
	}
	
	public static String sign(String params){
		Logger.info("HttpEntity sign", "params: "+params);
		Logger.info("HttpEntity sign", "appSercet: "+MobileLogConsts.appSercet);
		try {
			return SignEnc.sign(params, MobileLogConsts.appSercet);
		} catch (SignEncException e) {
			// TODO Auto-generated catch block
			Logger.exception(e);
		}
		return "";
	}
	
	private boolean sendData(byte[] data,HttpURLConnection conn){
		OutputStream os = null;
		try {
			conn.connect();
			os = conn.getOutputStream();
			os.write(data);
			os.flush();
			os.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.exception(e);
			return false;
		}finally{
			if (os != null){
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private String readResponse(HttpURLConnection conn){
		try {
			int responseCode = conn.getResponseCode();
			if (responseCode != 200){
				return null;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while((readLine = reader.readLine()) != null){
				sb.append(readLine).append("\n");
				if (!reader.ready()){
					break;
				}
			}
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.exception(e);
			return null;
		}
		
	}
}
