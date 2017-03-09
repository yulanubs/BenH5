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

import android.text.TextUtils;


import org.benmobile.coolhttp.http.utils.CountOutputStream;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Yan Zhenjie on 2016/12/18.
 */
public abstract class Request<T> {

    private String boundary = createBoundary();
    private String startBoundary = "--" + boundary;
    private String endBoundary = startBoundary + "--";

    private String url;
    private Method method;

    private Proxy proxy;

    private HostnameVerifier hostnameVerifier;
    private SSLSocketFactory sslSocketFactory;

    private int connectionTimeout = CoolHttp.getConnectionTimeout();
    private int readTimeout = CoolHttp.getReadTimeout();

    private String contentType;
    private Map<String, String> requestHeader;
    private List<KeyValue> requestParams;
    private String charset = "utf-8";
    private boolean formData = false;
    private InputStream defineRequestBody;


    public Request(String url, Method method) {
        this.url = url;
        this.method = method;

        requestHeader = new HashMap<>();
        requestParams = new ArrayList<>();

        setHeader("Accept", "*");
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setHeader(String key, String value) {
        requestHeader.put(key, value);
    }

    Map<String, String> getHeader() {
        return requestHeader;
    }

    public String getUrl() {
        if (!needFormData()) {
            String params = buildParams(requestParams, charset);
            // http://www.yanzhenjie.com?name=123&pwd=345
            if (url.contains("?") && url.contains("&")) {
                return url + "&" + params;
            }
            // http://www.yanzhenjie.com?name=123
            else if (url.contains("?")) {
                return url + "&" + params;
            }
            // http://www.yanzhenjie.com
            else {
                return url + "?" + params;
            }
        }
        return url;
    }

    public Method getMethod() {
        return method;
    }

    int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    // -------- 参数的封装 ----------//

    public void addParams(String key, String value) {
        requestParams.add(new KeyValue(key, value));
    }

    public void addParams(String key, long value) {
        requestParams.add(new KeyValue(key, Long.toString(value)));
    }
    public void addParams(KeyValue keyValue) {
        requestParams.add(keyValue);
    }

    public void addParams(String key, int value) {
        requestParams.add(new KeyValue(key, Integer.toString(value)));
    }

    public void addParams(String key, short value) {
        requestParams.add(new KeyValue(key, Short.toString(value)));
    }

    public void addParams(String key, float value) {
        requestParams.add(new KeyValue(key, Float.toString(value)));
    }

    public void addParams(String key, double value) {
        requestParams.add(new KeyValue(key, Double.toString(value)));
    }

    public void addParams(String key, Binary value) {
        requestParams.add(new KeyValue(key, value));
    }

    // ------------ Content-Type & Content-Length---------------//

    String getContentType() {
        // Header
        // 一般请求。
        // Content-Type:application/x-www-form-urlencoded

        // ----------------- 模拟表单 --------------------- //
        // Form请求。
        // Content-Type:multipart/form-data; boundary=...

        // Binary类型Item：
        // --boundary(start boundary)
        // Content-Disposition: form-data; name="key"; filename="filename" // key相当于url?key=value中的key
        // Content-Type: image/png
        // 换行...
        // Binary#writeBinary(OutputStream);
        // String类型Item：
        // --boundary(start boundary)
        // Content-Disposition: form-data; name="key" // key相当于url?key=value中的key
        // Content-Type: text/plain
        // 换行...
        // Write String
        // --boundary--(end boundary)

        if (!TextUtils.isEmpty(contentType)) return contentType;
        else if (needFormData())
            return "multipart/form-data; boundary=" + boundary;
        else
            return "application/x-www-form-urlencoded";
    }

    private boolean needFormData() {
        if (formData) return true;
        for (KeyValue requestParam : requestParams) {
            Object value = requestParam.getValue();
            if (value instanceof Binary) return true;
        }
        return false;
    }

    /**
     * 统计Content-Length。
     *
     * @return
     * @throws Exception
     */
    long getContentLength() throws Exception {
        CountOutputStream outputStream = new CountOutputStream();
        onWriteBody(outputStream);
        return outputStream.get();
    }

    public void setDefineRequestBody(InputStream inputStream, String contentType) {
        this.defineRequestBody = inputStream;
        this.contentType = contentType;
    }

    public void setDefineRequestBodyForJson(String jsonBody) {
        try {
            this.defineRequestBody = new ByteArrayInputStream(jsonBody.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.contentType = "application/json";
    }


    public void setDefineRequestBodyForXml(JSONObject jsonBody) {
        try {
            this.defineRequestBody = new ByteArrayInputStream(jsonBody.toString().getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.contentType = "application/xml";
    }

    public void setDefineRequestBodyForXml(String xmlBody) {
        try {
            this.defineRequestBody = new ByteArrayInputStream(xmlBody.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.contentType = "application/xml";
    }

    public void setDefineRequestBodyForFile(File file) {
        try {
            this.defineRequestBody = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.contentType = "application/xml";
    }

    /**
     * 通知request该写出body了。
     *
     * @param outputStream
     * @throws Exception
     */
    public void onWriteBody(OutputStream outputStream) throws Exception {
        if (defineRequestBody != null) {
            if (outputStream instanceof CountOutputStream) {
                outputStream.write(defineRequestBody.available());
            } else {
                byte[] buffer = new byte[2048];
                int len;
                while ((len = defineRequestBody.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                CloseUtils.close(defineRequestBody);
            }
        } else if (!needFormData()) {
            String params = buildParams(requestParams, charset);
            outputStream.write(params.getBytes());
        } else {
            writeFormBody(outputStream);
        }
    }

    /**
     * 写出表单body。
     *
     * @param outputStream
     * @throws Exception
     */
    private void writeFormBody(OutputStream outputStream) throws Exception {
        for (KeyValue requestParam : requestParams) {
            String key = requestParam.getKey();
            Object value = requestParam.getValue();
            if (value instanceof Binary) {
                onWriteFormBinary(key, (Binary) value, outputStream);
            } else {
                onWriteFormString(key, (String) value, outputStream);
            }
            outputStream.write("\r\n".getBytes());
        }
        // endBoundary.
        if (requestParams.size() > 0) {
            outputStream.write(endBoundary.getBytes());
        }
    }

    private void onWriteFormBinary(String key, Binary binary, OutputStream outputStream) throws Exception {
        // Binary类型Item：
        // --boundary(start boundary)
        // Content-Disposition: form-data; name="key"; filename="filename" // key相当于url?key=value中的key
        // Content-Type: image/png
        // 换行...
        // Binary#writeBinary(OutputStream);
        String data = startBoundary + "\r\n"
                + "Content-Disposition: form-data; name=\"" + key + "\"; filename=\""
                + binary.fileName() + "\"\r\n"
                + "Content-Type: " + binary.mimeType() + "\r\n\r\n";
        outputStream.write(data.getBytes(charset));
        if (outputStream instanceof CountOutputStream)
            ((CountOutputStream) outputStream).write(binary.contentLength());
        else
            binary.writeBinary(outputStream);
    }

    private void onWriteFormString(String key, String value, OutputStream outputStream) throws Exception {
        // String类型Item：
        // --boundary(start boundary)
        // Content-Disposition: form-data; name="key" // key相当于url?key=value中的key
        // Content-Type: text/plain
        // 换行...
        // Write String

        String data = startBoundary + "\r\n"
                + "Content-Disposition: form-data; name=\"" + key + "\"\r\n"
                + "Content-Type: text/plain; charset=\"" + charset + "\"\r\n\r\n" + value;
        outputStream.write(data.getBytes());
    }


    private String createBoundary() {
        return "--http" + UUID.randomUUID();
    }

    public static String buildParams(List<KeyValue> keyValues, String charset) {
        StringBuilder builder = new StringBuilder();
        for (KeyValue keyValue : keyValues) {
            Object value = keyValue.getValue();
            if (value instanceof String) {
                String key = keyValue.getKey();
                try {
                    String stringValue = URLEncoder.encode((String) value, charset);
                    builder.append("&").append(key).append("=").append(stringValue);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    // ------- 自定义请求部分 -------- //

    public abstract T parseResponse(Map<String, List<String>> responseHeader, byte[] responseBody);
}
