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

import java.util.List;
import java.util.Map;

/**
 * <p>同步执行入口，异步执行是基于同步请求。</p>
 * Created by Yan Zhenjie on 2017/1/8.
 */
public enum SyncExecutor {

    INSTANCE;

    private HttpRequestTask httpRequestTask;

    SyncExecutor() {
        httpRequestTask = new HttpRequestTask(CoolHttp.getHttpExecutor());
    }

    public <T> Response<T> execute(Request<T> request) {
        HttpResult httpResult = httpRequestTask.request(request);
        int responseCode = httpResult.getResponseCode();
        Map<String, List<String>> responseHeader = httpResult.getResponseHeader();
        byte[] responseBody = httpResult.getResponseBody();
        Exception exception = httpResult.getException();
        if (exception == null) {
            T result = request.parseResponse(responseHeader, responseBody);
            return new Response<>(request, responseCode, responseHeader, result, null);
        } else {
            return new Response<>(request, responseCode, responseHeader, null, exception);
        }
    }

}
