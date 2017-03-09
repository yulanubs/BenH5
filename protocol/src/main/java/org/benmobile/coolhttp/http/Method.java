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

/**
 * Created by Yan Zhenjie on 2016/12/18.
 */
public enum Method {

    GET("GET"),

    POST("POST"),

    HEAD("HEAD"),

    DELETE("DELETE"),

    PUT("PUT"),

    TRACE("TRACE"),

    PATCH("PATCH");

    private String value;

    Method(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public boolean isOut() {
        switch (this) {
            case POST:
            case PUT:
            case DELETE:
            case PATCH:
                return true;
            default:
                return false;
        }
    }
}
