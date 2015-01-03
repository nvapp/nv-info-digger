package com.nvapp.crawler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class DownLoadFile {
    public String getFileNameByUrl(String url, String contentType) {
        url = url.substring(7);
        if (url.indexOf("html") != -1 && contentType.indexOf("html") != -1) {
            // url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
            url = url.replaceAll("[\\?/:*|<>\"]", "_");
            return url;
        } else {
            return url.replaceAll("[\\?/:*|<>\"]", "_") + "." + contentType.substring(contentType.lastIndexOf("/") + 1);
        }
    }

    private void saveToLocal(String data, String filePath) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
            out.writeBytes(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String downloadFile(String url) {
//        System.out.println("download:" + url);
        String filePath = null;
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + getMethod.getStatusLine());
                filePath = null;
            }

            // byte[] responseBody = getMethod.getResponseBody();
            InputStream inputStream = getMethod.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            StringBuffer stringBuffer = new StringBuffer();
            String str = "";
            while ((str = br.readLine()) != null) {
                stringBuffer.append(str);
            }
            filePath = "D:\\temp\\" + getFileNameByUrl(url, getMethod.getResponseHeader("Content-Type").getValue());
            String s = stringBuffer.toString();
            saveToLocal(s, filePath);
        } catch (HttpException e) {
            System.out.println("Please check your provided http address!");
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            getMethod.releaseConnection();
        }
        return filePath;
    }
}
