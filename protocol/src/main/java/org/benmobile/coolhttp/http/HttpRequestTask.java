/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.benmobile.coolhttp.http;

import android.util.Log;


import org.benmobile.coolhttp.http.execute.HttpExecutor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Yan Zhenjie on 2016/12/18.
 */
public class HttpRequestTask {

    private HttpExecutor httpExecutor;

    public HttpRequestTask(HttpExecutor httpExecutor) {
        this.httpExecutor = httpExecutor;
    }

    public HttpResult request(Request<?> request) {
        String urlString = request.getUrl();
        Method method = request.getMethod();
        Proxy proxy = request.getProxy();
        HttpURLConnection urlConnection = null;
        OutputStream outputStream = null;

        int responseCode = -1;
        Map<String, List<String>> responseHeader = null;
        byte[] responseBody = null;
        Exception exception = null;
        try {
            // 1. 设置基础请求信息。
            URL url = new URL(urlString);// http://www.yanzhenjie.com
            urlConnection = httpExecutor.openUrl(url, proxy);

            if (urlConnection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsURLConnection = ((HttpsURLConnection) urlConnection);
                HostnameVerifier hostnameVerifier = request.getHostnameVerifier();
                if (hostnameVerifier != null)
                    httpsURLConnection.setHostnameVerifier(hostnameVerifier);
                SSLSocketFactory sslSocketFactory = request.getSslSocketFactory();
                if (sslSocketFactory != null)
                    httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
            }

            urlConnection.setRequestMethod(method.value());
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(method.isOut());
            urlConnection.setConnectTimeout(request.getConnectionTimeout());
            urlConnection.setReadTimeout(request.getReadTimeout());
            setHeader(urlConnection, request, request.getHeader());

            // 2. 发送数据，连接服务器。
            urlConnection.connect();
            if (method.isOut()) {
                outputStream = urlConnection.getOutputStream();
                request.onWriteBody(outputStream);
            }

            // 3. 拿到服务器的响应数据。
            responseCode = urlConnection.getResponseCode();
            responseHeader = urlConnection.getHeaderFields();
            Log.i("CoolHttp", "ResponseCode:" + responseCode);
            // has body.
            if (hasBody(responseCode)) {
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                InputStream inputStream = getRealInputStream(responseCode, urlConnection);

                byte[] buffer = new byte[2048];
                int len;
                try {
                    while ((len = inputStream.read(buffer)) != -1) {
                        arrayOutputStream.write(buffer, 0, len);
                    }
                    responseBody = arrayOutputStream.toByteArray();
                } finally {
                    //noinspection ThrowFromFinallyBlock
                    inputStream.close();
                    //noinspection ThrowFromFinallyBlock
                    arrayOutputStream.close();
                }

                if (responseBody == null)
                    Log.w("CoolHttp", "Response body is null");
                else
                    Log.i("CoolHttp", "Response body length: " + responseBody.length);

            }
        } catch (Exception e) {
            exception = e;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return new HttpResult(responseCode, responseHeader, responseBody, exception);
    }

    private boolean hasBody(int responseCode) {
        // http://www.w3school.com.cn/tags/html_ref_httpmessages.asp
        return !(responseCode >= 100 && responseCode < 200) // [100, 200)
                && responseCode != 204 && responseCode != 205 // 204, 205
                && !(responseCode >= 300 && responseCode < 400); // [300, 400)
    }

    /**
     * 根据响应码的不同拿到服务器的输出流，也就是我们要的输入流。
     *
     * @param responseCode  服务器响应码。
     * @param urlConnection 连接对象。
     * @return 真正的输入流。
     * @throws Exception 可能发生的异常。
     */
    private InputStream getRealInputStream(int responseCode, HttpURLConnection urlConnection) throws Exception {
        InputStream inputStream;
        if (responseCode >= 400) {
            inputStream = urlConnection.getErrorStream();
        } else {
            inputStream = urlConnection.getInputStream();
        }
        String contentEncoding = urlConnection.getContentEncoding();
        if (contentEncoding != null && contentEncoding.contains("gzip")) {
            inputStream = new GZIPInputStream(inputStream);
        }
        return inputStream;
    }

    /**
     * 设置http头。
     * <p>为了限制Content-Type，Content-Length的准确性。</p>
     *
     * @param urlConnection 服务器连接对象。
     * @param request       请求对象。
     * @param header        请求头。
     * @throws Exception 可能会发生的异常。
     */
    private void setHeader(HttpURLConnection urlConnection, Request<?> request, Map<String, String> header)
            throws Exception {
        // Content-Type.
        header.put("Content-Type", request.getContentType());

        // Content-Length.
        header.put("Content-Length", Long.toString(request.getContentLength()));

        for (Map.Entry<String, String> headerEntry : header.entrySet()) {
            String key = headerEntry.getKey();
            String value = headerEntry.getValue();
            Log.i("HttpFramework", key + " = " + value);
            urlConnection.setRequestProperty(key, value);
        }
    }

}
