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
 * Created by Yan Zhenjie on 2017/1/8.
 */
public class Response<T> {

    private Request<T> tRequest;

    private int responseCode;
    private Map<String, List<String>> responseHeader;
    private T result;
    private Exception exception;

    public Response(Request<T> request, int responseCode, Map<String, List<String>> responseHeader, T result, Exception e) {
        this.tRequest = request;
        this.responseCode = responseCode;
        this.responseHeader = responseHeader;
        this.result = result;
        this.exception = e;
    }

    public Request<T> getRequest() {
        return tRequest;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public Map<String, List<String>> getResponseHeader() {
        return responseHeader;
    }

    public T getResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isSucceed() {
        return exception == null;
    }
}
