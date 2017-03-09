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

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Yan Zhenjie on 2017/1/8.
 */
public class Poster extends Handler {

    private static Poster poster;

    public static Poster getInstance() {
        if (poster == null)
            synchronized (Poster.class) {
                if (poster == null)
                    poster = new Poster();
            }
        return poster;
    }

    private Poster() {
        super(Looper.getMainLooper());
    }
}
