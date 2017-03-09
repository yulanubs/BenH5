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
package org.benmobile.coolhttp.http.execute;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.internal.huc.OkHttpURLConnection;
import okhttp3.internal.huc.OkHttpsURLConnection;

/**
 * Created by Yan Zhenjie on 2017/1/8.
 */
public class OkHttpExecutor implements HttpExecutor {


    private static OkHttpExecutor instance;

    public static HttpExecutor getInstance() {
        if (instance == null)
            synchronized (OkHttpExecutor.class) {
                if (instance == null)
                    instance = new OkHttpExecutor();
            }
        return instance;
    }

    private OkHttpClient okHttpClient;

    private OkHttpExecutor() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public HttpURLConnection openUrl(URL url, Proxy proxy) throws IOException {
        String protocol = url.getProtocol(); // http or https.
        OkHttpClient copy = okHttpClient.newBuilder().proxy(proxy).build();

        if (protocol.equals("http")) return new OkHttpURLConnection(url, copy);
        if (protocol.equals("https")) return new OkHttpsURLConnection(url, copy);
        throw new IllegalArgumentException("Unexpected protocol: " + protocol);
    }

}
