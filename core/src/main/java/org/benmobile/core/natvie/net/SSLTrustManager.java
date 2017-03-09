package org.benmobile.core.natvie.net;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
/**
 * 
	 * @ClassName: SSLTrustManager<BR>
     * @Describe:SSL认证<BR>
     * @Author: Jekshow
	 * @Extends：<BR>
     * @Version:1.0 
     * @date:2016-10-20 下午3:14:21
 */
public class SSLTrustManager implements TrustManager,
            X509TrustManager ,HostnameVerifier{
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
  
        public boolean isServerTrusted(
               X509Certificate[] certs) {
            return true;
        }
  
        public boolean isClientTrusted(
               X509Certificate[] certs) {
            return true;
        }
  
        public void checkServerTrusted(
                X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
  
        public void checkClientTrusted(
              X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
         
            @Override
        public boolean verify(String urlHostName, SSLSession session) { //允许所有主机
            return true;
        }
        
   //封装
public static HttpURLConnection connect(String strUrl) throws Exception {
         
        TrustManager[] trustAllCerts = new TrustManager[1];
         TrustManager tm = new SSLTrustManager();
         trustAllCerts[0] = tm;
        SSLContext sc = SSLContext
                 .getInstance("SSL");
         sc.init(null, trustAllCerts, null);
       HttpsURLConnection.setDefaultSSLSocketFactory(sc
                 .getSocketFactory());
          
         HttpsURLConnection.setDefaultHostnameVerifier((HostnameVerifier) tm);
          
        URL url = new URL(strUrl);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
      
        return urlConn;
    }
        
 }