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


import org.benmobile.coolhttp.http.utils.HeadUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by Yan Zhenjie on 2017/1/8.
 */
public class StringRequest extends Request<String> {

    public StringRequest(String url, Method method) {
        super(url, method);
        setHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
    }

    @Override
    public String parseResponse(Map<String, List<String>> responseHeader, byte[] responseBody) {
        return parseString(responseHeader, responseBody);
    }

    public static String parseString(Map<String, List<String>> responseHeader, byte[] responseBody) {
        if (responseBody == null || responseBody.length <= 0) return "";

        String charset = HeadUtils.parseValue(responseHeader, "Content-Type", "charset");
        if (!TextUtils.isEmpty(charset))
            try {
                return new String(responseBody, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        return new String(responseBody);
    }
}
