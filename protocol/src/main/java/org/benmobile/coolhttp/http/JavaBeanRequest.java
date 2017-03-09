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

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

/**
 * Created by Yan Zhenjie on 2017/1/9.
 */
public class JavaBeanRequest<Entity> extends Request<Entity> {

    private Class<Entity> entityClass;

    public JavaBeanRequest(String url, Method method, Class<Entity> entityClass) {
        super(url, method);
        this.entityClass = entityClass;
    }

    @Override
    public Entity parseResponse(Map<String, List<String>> responseHeader, byte[] responseBody) {
        String jsonString = StringRequest.parseString(responseHeader, responseBody);
        return JSON.parseObject(jsonString, entityClass);
    }
}
