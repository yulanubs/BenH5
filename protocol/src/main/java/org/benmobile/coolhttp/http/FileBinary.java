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

import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Yan Zhenjie on 2017/1/8.
 */
public class FileBinary implements Binary {

    private File file;

    public FileBinary(File file) {
        if (file == null || !file.exists())
            throw new IllegalArgumentException("The file is not found.");
        if (file.isDirectory())
            throw new IllegalArgumentException("The file can not be directory.");
        this.file = file;
    }

    @Override
    public String mimeType() {
        String type = "application/octet-stream";
        String name = file.getName();
        // 判断文件是否有扩展名，有则根据扩展名拿到type。
        if (MimeTypeMap.getSingleton().hasExtension(name)) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(name);
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    public String fileName() {
        return file.getName();
    }

    @Override
    public long contentLength() throws Exception {
        return file.length();
    }

    @Override
    public void writeBinary(OutputStream outputStream) throws Exception {
        InputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[2048];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        CloseUtils.close(inputStream);
    }
}
