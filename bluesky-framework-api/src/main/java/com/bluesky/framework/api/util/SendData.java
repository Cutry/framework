package com.bluesky.framework.api.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendData {
    public static String readContentFromPost(String url,String content) throws IOException {
        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = new URL(url);  //http://59.202.58.68/gateway/app/refreshTokenByKey.htm
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        // 设置是否向connection输出，因为这个是post请求，参数要放在
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        // http正文内，因此需要设为true
        connection.setDoOutput(true);
        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // 默认是 GET方式
        connection.setRequestMethod("POST");
        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        //设置本次连接是否自动重定向
        connection.setInstanceFollowRedirects(true);
        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
        // 意思是正文是urlencoded编码过的form参数
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
        // 要注意的是connection.getOutputStream会隐含的进行connect。
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection
                .getOutputStream());
               out.writeBytes(content);
        //流用完记得关
        out.flush();
        out.close();
        //获取响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
        String line;
        String str="";
        while ((line = reader.readLine()) != null){
            str+=line;
        }
        reader.close();
        //该干的都干完了,记得把连接断了
        connection.disconnect();
        return str.trim();
    }
    public static String readContentFromGet(String url) throws IOException {
        // Post请求的url，与get不同的是不需要带参数
        URL geturl = new URL(url);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) geturl.openConnection();
        // 设置是否向connection输出，因为这个是post请求，参数要放在
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        // http正文内，因此需要设为true
        connection.setDoOutput(true);
        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // 默认是 GET方式
        connection.setRequestMethod("GET");
        //设置本次连接是否自动重定向
        connection.setInstanceFollowRedirects(true);
        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
        // 意思是正文是urlencoded编码过的form参数
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
        // 要注意的是connection.getOutputStream会隐含的进行connect。
        connection.connect();

        //获取响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
        String line;
        String str="";
        while ((line = reader.readLine()) != null){
            str+=line;
        }
        reader.close();
        //该干的都干完了,记得把连接断了
        connection.disconnect();
        return str.trim();
    }
}
