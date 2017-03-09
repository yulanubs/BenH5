package org.benmobile.utils;



import org.benmobile.log.BenH5Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jekshow on 2016/8/9.
 */
public class UtilTools {
    public static String updateMessageFormat(String input) {
        if (input == null || input.equals("")) {
            return "";
        }
        String[] inputs = input.split("##");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inputs.length; i++) {
            sb.append(inputs[i]).append("\n");
        }
        return sb.toString();
    }
    public static String exec(String[] args) {
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            // baos.write('/n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (IOException e) {
            BenH5Log.excaption(e);
        } catch (Exception e) {
            BenH5Log.excaption(e);
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                BenH5Log.excaption(e);
            }

            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }
}
