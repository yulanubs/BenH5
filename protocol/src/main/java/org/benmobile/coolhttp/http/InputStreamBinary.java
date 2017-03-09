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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Yan Zhenjie on 2017/1/8.
 */
public class InputStreamBinary implements Binary {

    private String mimeType;
    private String fileName;
    private InputStream inputStream;

    public InputStreamBinary(String mimeType, String fileName, InputStream inputStream) {
        if (TextUtils.isEmpty(mimeType)) throw new IllegalArgumentException("The mimeType can not be null.");
        if (TextUtils.isEmpty(fileName)) throw new IllegalArgumentException("The fileName can not be null.");
        if (inputStream == null) throw new IllegalArgumentException("The inputStream can not be null.");

        if (!(inputStream instanceof ByteArrayInputStream) && !(inputStream instanceof FileInputStream))
            throw new IllegalArgumentException("The inputStream can not be support.");

        this.mimeType = mimeType;
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    @Override
    public String mimeType() {
        return mimeType;
    }

    @Override
    public String fileName() {
        return fileName;
    }

    @Override
    public long contentLength() throws Exception {
        if (inputStream instanceof ByteArrayInputStream)
            return inputStream.available();
        else {
            return ((FileInputStream) inputStream).getChannel().size();
        }
    }

    @Override
    public void writeBinary(OutputStream outputStream) throws Exception {
        byte[] buffer = new byte[2048];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        CloseUtils.close(inputStream);
    }

}
