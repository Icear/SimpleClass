package com.github.Icear.Network;

import com.github.Icear.Util.ConvertUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;


/**
 * Created by icear on 2017/10/8.
 */

public class HttpClient {

    private CookieManager cookieManager;

    public HttpClient(HttpCookieStore cookieStore){
        //TODO 提供选项设定要接收Cookie的域
        //TODO ???CookieHandle的作用范围还不清楚，暂时当成大范围杀器（官方文档的System-Wide到底是个什么鬼....)
        this.cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    public CookieStore getCookieStore(){
        return cookieManager.getCookieStore();
    }
//
//    /**
//     * 传入httpUrlConnection以载入储存的cookie
//     * @param httpURLConnection httpUrlConnection
//     */
//    private void loadCookieForConnection(URI uri, HttpURLConnection httpURLConnection) throws IOException {
//        Map<String,List<String>> stringCookieMap = cookieManager.get(uri,httpURLConnection.getHeaderFields());
//
//    }

//    /**
//     * 传入httpUrlConnection以更新connection中的cookie
//     * @param httpURLConnection httpUrlConnection
//     */
//    private void updateCookieForConnection(String host, HttpURLConnection httpURLConnection){
//
//    }

    public String httpGetForString(String urlString, Iterable<? extends NameValuePair> params) throws IOException {
        //设置CookieHandle
//        CookieHandler.setDefault(cookieManager);

        //合并参数
        String urlParams;
        if ((urlParams = generateString(params,"=","&")) != null) {
            urlString += urlParams;
        }

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoOutput(false);//不输出

//        CookieHandler.setDefault(null);

//        loadCookieForConnection(URI,httpURLConnection);

        //接收数据
        InputStream inputStream = httpURLConnection.getInputStream();
        String response = ConvertUtil.toString(inputStream);
        inputStream.close();

//        updateCookieForConnection(url,httpURLConnection);
//        CookieHandler.setDefault(null);

        return response;
    }

    public byte[] httpGetForByteArray(String urlString, Iterable<? extends NameValuePair> params) throws IOException {
        //设置CookieHandle
//        CookieHandler.setDefault(cookieManager);

        //合并参数
        String urlParams;
        if ((urlParams = generateString(params,"=","&")) != null) {
            urlString += urlParams;
        }

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoOutput(false);//不输出

//        CookieHandler.setDefault(null);

//        loadCookieForConnection(URI,httpURLConnection);

        //接收数据
        InputStream inputStream = httpURLConnection.getInputStream();
        byte[] response = ConvertUtil.toByteArray(inputStream);
        inputStream.close();

//        updateCookieForConnection(url,httpURLConnection);
//        CookieHandler.setDefault(null);

        return response;
    }

    public String httpPostForString(String urlString, Iterable<? extends NameValuePair> params, Iterable<? extends NameValuePair> postParams) throws IOException {
        //设置CookieHandle
//        CookieHandler.setDefault(cookieManager);

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

//        CookieHandler.setDefault(null);

        return response;
    }

    public byte[] httpPostForByteArray(String urlString, Iterable<? extends NameValuePair> params, Iterable<? extends NameValuePair> postParams) throws IOException {
        //设置CookieHandle
//        CookieHandler.setDefault(cookieManager);

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
        byte[] response = ConvertUtil.toByteArray(inputStream);
        inputStream.close();

//        CookieHandler.setDefault(null);

        return response;
    }

    private static String generateString(Iterable<? extends NameValuePair> params, String linkSymbol, String delimiter){
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
