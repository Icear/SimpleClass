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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by icear on 2017/10/8.
 */

public class HttpClient {

    private CookieManager cookieManager;

    public HttpClient(CookieStore cookieStore){
        //TODO 提供选项设定要接收Cookie的域
        //TODO ???CookieHandle的作用范围还不清楚，暂时当成大范围杀器（官方文档的System-Wide到底是个什么鬼....)
//        this.cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ORIGINAL_SERVER);

        /*
         * 直接使用CookieManager内部的CookieStore实现，在外部对其进行持有，以实现跨连接的Cookie储存
         * 这种方法只能实现单次运行周期内的Cookie持有，无法长期使用
         * TODO 待改进
         */

        this.cookieManager = new CookieManager();

    }

    public CookieStore getCookieStore(){
        return cookieManager.getCookieStore();
    }

    /**
     * 传入httpUrlConnection以载入储存的cookie
     * @param httpURLConnection httpUrlConnection
     */
    private void loadCookieForConnection(URL url, HttpURLConnection httpURLConnection) throws IOException, URISyntaxException {
        Map<String,List<String>> stringCookieMap = cookieManager.get(new URI(url.getHost()),httpURLConnection.getHeaderFields());
        if (stringCookieMap.containsKey("Cookie")) {
            for (String s : stringCookieMap.get("Cookie")) {
                httpURLConnection.addRequestProperty("Cookie", s);
            }
        }
        if(stringCookieMap.containsKey("Cookie2")){
            for (String s : stringCookieMap.get("Cookie2")) {
                httpURLConnection.addRequestProperty("Cookie2", s);
            }
        }
    }
//
    /**
     * 传入httpUrlConnection以更新connection中的cookie
     * @param httpURLConnection httpUrlConnection
     */
    private void updateCookieForConnection(URL url, HttpURLConnection httpURLConnection){

    }

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
        CookieHandler.setDefault(null);

        return response;
    }

    public byte[] httpGetForByteArray(String urlString, Iterable<? extends NameValuePair> params) throws IOException {
        //设置CookieHandle
        CookieHandler.setDefault(cookieManager);

        //合并参数
        String urlParams;
        if ((urlParams = generateString(params,"=","&")) != null) {
            urlString += urlParams;
        }

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoOutput(false);//不输出

        CookieHandler.setDefault(null);

//        loadCookieForConnection(URI,httpURLConnection);

        //接收数据
        InputStream inputStream = httpURLConnection.getInputStream();
        byte[] response = ConvertUtil.toByteArray(inputStream);
        inputStream.close();

//        updateCookieForConnection(url,httpURLConnection);
        CookieHandler.setDefault(null);

        return response;
    }

    public String httpPostForString(String urlString, Iterable<? extends NameValuePair> params, Iterable<? extends NameValuePair> postParams) throws IOException {
        //设置CookieHandle
        CookieHandler.setDefault(cookieManager);

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

        CookieHandler.setDefault(null);

        return response;
    }

    public byte[] httpPostForByteArray(String urlString, Iterable<? extends NameValuePair> params, Iterable<? extends NameValuePair> postParams) throws IOException {
        //设置CookieHandle
        CookieHandler.setDefault(cookieManager);

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

        CookieHandler.setDefault(null);

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
