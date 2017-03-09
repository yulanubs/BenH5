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

import android.content.Context;

import org.benmobile.coolhttp.http.execute.HttpExecutor;
import org.benmobile.coolhttp.http.execute.OkHttpExecutor;


/**
 * Created by Yan Zhenjie on 2017/1/8.
 */
public class CoolHttp {


    /**
     * https://git.oschina.net/ysb/NoHttpUtil
     *
     * https://github.com/LiqiNew/NohttpRxUtils
     */

    private static Context sContext;
    private static HttpConfig sHttpConfig;

    public static void initialize(Context context) {
        initialize(context, new HttpConfig());
    }

    public static void initialize(Context context, HttpConfig httpConfig) {
        sContext = context;
        sHttpConfig = httpConfig;
    }

    private static void hasInitialize() {
        if (sContext == null)
            throw new ExceptionInInitializerError("Please invoke CoolHttp.initialize(Application) in Application#onCreate()");
    }

    public static HttpExecutor getHttpExecutor() {
        hasInitialize();
        return sHttpConfig.httpExecutor;
    }

    public static int getConnectionTimeout() {
        hasInitialize();
        return sHttpConfig.connectionTimeout;
    }

    public static int getReadTimeout() {
        hasInitialize();
        return sHttpConfig.readTimeout;
    }

    public static class HttpConfig {

        private HttpExecutor httpExecutor;
        private int connectionTimeout;
        private int readTimeout;

        public HttpConfig() {
            httpExecutor = OkHttpExecutor.getInstance();
            connectionTimeout = 8 * 1000;
            readTimeout = 8 * 1000;
        }

        public HttpConfig setHttpExecutor(HttpExecutor httpExecutor) {
            this.httpExecutor = httpExecutor;
            return this;
        }

        public HttpConfig setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public HttpConfig setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }
    }

    // ---------- 同步请求 ----------- //

    /**
     * 同步请求。
     *
     * @param request 请求对象。
     * @param <T>     希望请求到的结果类型。
     * @return 响应封装对象。
     */
    public static <T> Response<T> syncRequest(Request<T> request) {
        return SyncExecutor.INSTANCE.execute(request);
    }

    // ---------- 异步请求 ----------- //

    /**
     * 异步请求。
     *
     * @param request      请求对象。
     * @param httpListener 回调接口。
     * @param <T>          希望请求到的结果类型。
     */
    public static <T> void asyncRequest(Request<T> request, HttpListener<T> httpListener) {
        AsyncExecutor.INSTANCE.execute(request, httpListener);
    }
}
