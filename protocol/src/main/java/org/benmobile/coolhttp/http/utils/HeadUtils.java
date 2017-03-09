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
package org.benmobile.coolhttp.http.utils;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Yan Zhenjie on 2017/1/8.
 */
public class HeadUtils {

    public static String parseValue(Map<String, List<String>> responseHeader, String parentKey, String valueKey) {
        List<String> contentTypes = responseHeader.get(parentKey);
        if (contentTypes != null && contentTypes.size() > 0) {
            String contentType = contentTypes.get(0);
            StringTokenizer stringTokenizer = new StringTokenizer(contentType, ";");
            while (stringTokenizer.hasMoreTokens()) {
                String token = stringTokenizer.nextToken();
                if (token.contains(valueKey)) {
                    String[] values = token.split("=");
                    if (values.length > 1) {
                        return values[1];
                    }
                }
            }
        }
        return null;
    }

}
