package com.github.Icear.Network;


import com.github.Icear.Network.Util.NetworkUtil;
import com.github.Icear.Util.ConvertUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;



/**
 * Created by icear on 2017/10/8.
 */

public class HttpClient {

    private CookieManager cookieManager;

    public HttpClient(){
        this(null);
    }

    public HttpClient(CookieStore cookieStore){
        //TODO 提供选项设定要接收Cookie的域
        //TODO ???CookieHandle的作用范围还不清楚，暂时当成大范围杀器（官方文档的System-Wide到底是个什么鬼....)

        /*
         * 直接使用CookieManager内部的CookieStore实现，在外部对其进行持有，以实现跨连接的Cookie储存
         * 这种方法只能实现单次运行周期内的Cookie持有，无法长期使用
         * TODO 待改进
         */

        this.cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
    }

    public CookieStore getCookieStore(){
        return cookieManager.getCookieStore();
    }



    public String httpGetForString(String urlString, Iterable<? extends NameValuePair> params) throws IOException, URISyntaxException {
        //设置CookieHandle
//        CookieHandler.setDefault(cookieManager);

        //合并参数
        String urlParams;
        if ((urlParams = NetworkUtil.generateString(params)) != null) {
            urlString += urlParams;
        }

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoOutput(false);//不输出

//        CookieHandler.setDefault(null);

        NetworkUtil.loadCookieForConnection(cookieManager,httpURLConnection);

        //接收数据
        InputStream inputStream = httpURLConnection.getInputStream();
        String response = ConvertUtil.toString(inputStream);
        inputStream.close();

        NetworkUtil.updateCookieForConnection(cookieManager,httpURLConnection);
//        CookieHandler.setDefault(null);

        return response;
    }

    public byte[] httpGetForByteArray(String urlString, Iterable<? extends NameValuePair> params) throws IOException, URISyntaxException {
        //设置CookieHandle
//        CookieHandler.setDefault(cookieManager);

        //合并参数
        String urlParams;
        if ((urlParams = NetworkUtil.generateString(params)) != null) {
            urlString += urlParams;
        }

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoOutput(false);//不输出

//        CookieHandler.setDefault(null);

        NetworkUtil.loadCookieForConnection(cookieManager,httpURLConnection);

        //接收数据
        InputStream inputStream = httpURLConnection.getInputStream();
        byte[] response = ConvertUtil.toByteArray(inputStream);
        NetworkUtil.updateCookieForConnection(cookieManager,httpURLConnection);
        inputStream.close();

//        CookieHandler.setDefault(null);

        return response;
    }

    public String httpPostForString(String urlString, Iterable<? extends NameValuePair> params, Iterable<? extends NameValuePair> postParams) throws IOException, URISyntaxException {
        //设置CookieHandle
//        CookieHandler.setDefault(cookieManager);

        //合并参数
        String urlParams;
        if ((urlParams = NetworkUtil.generateString(params)) != null) {
            urlString += ("?" + urlParams);
        }

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");

        NetworkUtil.loadCookieForConnection(cookieManager,httpURLConnection);

        //写入Post数据
        String postData;
        if((postData = NetworkUtil.generateString(postParams)) != null){
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postData.getBytes());
            outputStream.flush();
            outputStream.close();
        }

        //接收数据
        InputStream inputStream = httpURLConnection.getInputStream();
        String response = ConvertUtil.toString(inputStream);
        NetworkUtil.updateCookieForConnection(cookieManager,httpURLConnection);
        inputStream.close();

//        CookieHandler.setDefault(null);

        return response;
    }

    public byte[] httpPostForByteArray(String urlString, Iterable<? extends NameValuePair> params, Iterable<? extends NameValuePair> postParams) throws IOException, URISyntaxException {
        //设置CookieHandle
        CookieHandler.setDefault(cookieManager);

        //合并参数
        String urlParams;
        if ((urlParams = NetworkUtil.generateString(params)) != null) {
            urlString += ("?" + urlParams);
        }

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        NetworkUtil.loadCookieForConnection(cookieManager,httpURLConnection);

        //写入Post数据
        String postData;
        if((postData = NetworkUtil.generateString(postParams)) != null){
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postData.getBytes());
            outputStream.flush();
            outputStream.close();
        }

        //接收数据
        InputStream inputStream = httpURLConnection.getInputStream();
        byte[] response = ConvertUtil.toByteArray(inputStream);
        NetworkUtil.updateCookieForConnection(cookieManager,httpURLConnection);
        inputStream.close();

//        CookieHandler.setDefault(null);

        return response;
    }
}
