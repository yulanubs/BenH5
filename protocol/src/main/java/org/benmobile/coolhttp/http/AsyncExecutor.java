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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Yan Zhenjie on 2017/1/8.
 */
public enum AsyncExecutor {

    INSTANCE;

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    AsyncExecutor() {
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    public <T> void execute(final Request<T> request, final HttpListener<T> httpListener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                {
                    Response<T> tResponse = SyncExecutor.INSTANCE.execute(request);
                    ResultMessage<T> tResultMessage = new ResultMessage<>();
                    tResultMessage.tResponse = tResponse;
                    tResultMessage.tHttpListener = httpListener;

                    Poster.getInstance().post(tResultMessage);
                }
            }
        });
    }

    private static class ResultMessage<T> implements Runnable {

        private Response<T> tResponse;
        private HttpListener<T> tHttpListener;

        @Override
        public void run() {
            if (tResponse.isSucceed()) {
                tHttpListener.onSucceed(tResponse);
            } else {
                tHttpListener.onFailed(tResponse);
            }
        }
    }

}
