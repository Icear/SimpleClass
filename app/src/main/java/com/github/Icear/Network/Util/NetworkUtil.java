package com.github.Icear.Network.Util;


import com.github.Icear.Network.NameValuePair;
import com.github.Icear.Util.ConvertUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by icear on 2017/10/8.
 */

public class NetworkUtil {
    public static String httpGet(String urlString, Iterable<? extends NameValuePair> params) throws IOException {
        //合并参数
        String urlParams;
        if ((urlParams = generateString(params,"=","&")) != null) {
            urlString += urlParams;
        }

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoOutput(false);//不输出

//        httpURLConnection.setRequestProperty();
        //接收数据
        InputStream inputStream = httpURLConnection.getInputStream();
        String response = ConvertUtil.toString(inputStream);
        inputStream.close();
        return response;
    }

    public static String httpPost(String urlString, Iterable<? extends NameValuePair> params, Iterable<? extends NameValuePair> postParams) throws IOException {
        //合并参数
        String urlParams;
        if ((urlParams = generateString(params,"=","&")) != null) {
            urlString += urlParams;
        }


        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");

        //写入Post数据
        String postData;
        if((postData = generateString(postParams,":","&")) != null){
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postData.getBytes());
            outputStream.flush();
            outputStream.close();
        }

        //接收数据
        InputStream inputStream = httpURLConnection.getInputStream();
        String response = ConvertUtil.toString(inputStream);
        inputStream.close();
        return response;
    }

    public static String generateString(Iterable<? extends NameValuePair> params, String linkSymbol, String delimiter){
        String data = null;
        if(params != null){
            Iterator iterator = params.iterator();
            if(iterator.hasNext()){
                NameValuePair nameValuePair;
                while(iterator.hasNext()){
                    nameValuePair = (NameValuePair) iterator.next();
                    data += (nameValuePair.getName() + linkSymbol + nameValuePair.getValue() + delimiter);
                }
                return data;
            }
        }
        return null;
    }
}
