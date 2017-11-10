package indi.github.icear.network.util;


import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import indi.github.icear.network.NameValuePair;
import indi.github.icear.util.ConvertUtil;

/**
 * Created by icear on 2017/10/8.
 * NetworkUtil，提供网络访问相关工具函数
 */

public class NetworkUtil {
    private static String TAG = NetworkUtil.class.getSimpleName();

    /**
     * 使用Get请求访问指定url并获得String类型的回复
     * @param urlString 要访问的url
     * @param params url请求参数，null表示空
     * @return 服务器返回的数据
     * @throws IOException 网络IO异常
     */
    public static String httpGetForString(String urlString, Iterable<? extends NameValuePair> params) throws IOException {
        return httpGetForString(null, urlString, params);
    }

    /**
     * 使用Get请求访问指定url并获得String类型的回复
     * @param cookieStore 执行网络访问时要使用的cookieStore，null表示不使用
     * @param urlString 要访问的url
     * @param params url请求参数，null表示空
     * @return 服务器返回的数据
     * @throws IOException 网络IO异常
     */
    public static String httpGetForString(CookieStore cookieStore, String urlString, Iterable<? extends NameValuePair> params) throws IOException {
        //设置CookieHandle
//        CookieHandler.setDefault(cookieManager);

        CookieManager cookieManager = null;
        if (cookieStore != null) {
            cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        }

        //合并参数
        String urlParams;
        if ((urlParams = NetworkUtil.generateString(params)) != null) {
            urlString += urlParams;
        }

        Log.d(TAG,"execute request to " + urlString);
        Log.d(TAG,"method: Get");

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoOutput(false);//不输出

//        CookieHandler.setDefault(null);

        if (cookieManager != null) {
            NetworkUtil.loadCookieForConnection(cookieManager,httpURLConnection);
        }

        //接收数据
        InputStream inputStream = httpURLConnection.getInputStream();
        String response = ConvertUtil.toString(inputStream);
        inputStream.close();

        Log.d(TAG, response);
        Log.d(TAG,"response code: " + httpURLConnection.getResponseCode() );

        if (cookieManager != null) {
            NetworkUtil.updateCookieForConnection(cookieManager,httpURLConnection);
        }
//        CookieHandler.setDefault(null);

        return response;
    }

    /**
     * 使用Get请求访问指定url并获得byte Array类型的回复
     * @param urlString 要访问的url
     * @param params url请求参数，null表示空
     * @return 服务器返回的数据
     * @throws IOException 网络IO异常
     */
    public static byte[] httpGetForByteArray(String urlString, Iterable<? extends NameValuePair> params) throws IOException {
        return httpGetForByteArray(null, urlString, params);
    }

    /**
     * 使用Get请求访问指定url并获得byte Array类型的回复
     * @param cookieStore 执行网络访问时要使用的CookieStore，null表示不使用
     * @param urlString 要访问的url
     * @param params url请求参数，null表示空
     * @return 服务器返回的数据
     * @throws IOException 网络IO异常
     */
    public static byte[] httpGetForByteArray(CookieStore cookieStore, String urlString, Iterable<? extends NameValuePair> params) throws IOException {
        //设置CookieHandle
//        CookieHandler.setDefault(cookieManager);

        CookieManager cookieManager = null;
        if (cookieStore != null) {
            cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        }

        //合并参数
        String urlParams;
        if ((urlParams = NetworkUtil.generateString(params)) != null) {
            urlString += urlParams;
        }

        Log.d(TAG,"execute request to " + urlString);
        Log.d(TAG,"method: Get");

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoOutput(false);//不输出

//        CookieHandler.setDefault(null);


        if (cookieManager != null) {
            NetworkUtil.loadCookieForConnection(cookieManager,httpURLConnection);
        }

        //接收数据
        InputStream inputStream = httpURLConnection.getInputStream();
        byte[] response = ConvertUtil.toByteArray(inputStream);

        Log.d(TAG,"response code: " + httpURLConnection.getResponseCode() );

        if (cookieManager != null) {
            NetworkUtil.updateCookieForConnection(cookieManager,httpURLConnection);
        }
        inputStream.close();

//        CookieHandler.setDefault(null);

        return response;
    }

    /**
     * 使用Get请求访问指定url并获得byte Array类型的回复
     * @param urlString 要访问的url
     * @param params url请求参数，null表示空
     * @param postParams post数据集
     * @return 服务器返回的数据
     * @throws IOException 网络IO异常
     */
    public static String httpPostForString(String urlString, Iterable<? extends NameValuePair> params, Iterable<? extends NameValuePair> postParams) throws IOException {
        return httpPostForString(null, urlString, params, postParams);
    }

    /**
     * 使用Get请求访问指定url并获得byte Array类型的回复
     * @param cookieStore 执行网络访问时要使用的CookieStore，null表示不使用
     * @param urlString 要访问的url
     * @param params url请求参数，null表示空
     * @param postParams post数据集
     * @return 服务器返回的数据
     * @throws IOException 网络IO异常
     */
    public static String httpPostForString(CookieStore cookieStore, String urlString, Iterable<? extends NameValuePair> params, Iterable<? extends NameValuePair> postParams) throws IOException{
        //设置CookieHandle
//        CookieHandler.setDefault(cookieManager);

        CookieManager cookieManager = null;
        if (cookieStore != null) {
            cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        }

        //合并参数
        String urlParams;
        if ((urlParams = NetworkUtil.generateString(params)) != null) {
            urlString += ("?" + urlParams);
        }

        Log.d(TAG,"execute request to " + urlString);
        Log.d(TAG,"method: Post");

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");

        if (cookieManager != null) {
            NetworkUtil.loadCookieForConnection(cookieManager,httpURLConnection);
        }

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

        Log.d(TAG,"response code: " + httpURLConnection.getResponseCode() );

        if (cookieManager != null) {
            NetworkUtil.updateCookieForConnection(cookieManager,httpURLConnection);
        }
        inputStream.close();



//        CookieHandler.setDefault(null);

        return response;
    }

    /**
     * 使用Get请求访问指定url并获得byte Array类型的回复
     * @param urlString 要访问的url
     * @param params url请求参数
     * @param postParams post数据集
     * @return 服务器返回的数据
     * @throws IOException 网络IO异常
     */
    public static byte[] httpPostForByteArray(String urlString, Iterable<? extends NameValuePair> params, Iterable<? extends NameValuePair> postParams) throws IOException, URISyntaxException {
        return httpPostForByteArray(null, urlString, params, postParams);
    }

    /**
     * 使用Get请求访问指定url并获得byte Array类型的回复
     * @param cookieStore 执行网络访问时要使用的CookieStore
     * @param urlString 要访问的url
     * @param params url请求参数
     * @param postParams post数据集
     * @return 服务器返回的数据
     * @throws IOException 网络IO异常
     */
    public static byte[] httpPostForByteArray(CookieStore cookieStore, String urlString, Iterable<? extends NameValuePair> params, Iterable<? extends NameValuePair> postParams) throws IOException, URISyntaxException {
        //设置CookieHandle
//        CookieHandler.setDefault(cookieManager);

        CookieManager cookieManager = null;
        if (cookieStore != null) {
            cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        }

        //合并参数
        String urlParams;
        if ((urlParams = NetworkUtil.generateString(params)) != null) {
            urlString += ("?" + urlParams);
        }

        Log.d(TAG,"execute request to " + urlString);
        Log.d(TAG,"method: Post");

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        if (cookieManager != null) {
            NetworkUtil.loadCookieForConnection(cookieManager,httpURLConnection);
        }

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

        Log.d(TAG,"response code: " + httpURLConnection.getResponseCode() );

        if (cookieManager != null) {
            NetworkUtil.updateCookieForConnection(cookieManager,httpURLConnection);
        }

        inputStream.close();

//        CookieHandler.setDefault(null);

        return response;
    }


//    public static String httpGet(String urlString, Iterable<? extends NameValuePair> params) throws IOException {
//        合并参数
//        String urlParams;
//        if ((urlParams = generateString(params)) != null) {
//            urlString += ("?" + urlParams);
//        }
//
//        URL url = new URL(urlString);
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        httpURLConnection.setRequestMethod("GET");
//        httpURLConnection.setDoOutput(false);//不输出
//
//        httpURLConnection.setRequestProperty();
//        接收数据
//        InputStream inputStream = httpURLConnection.getInputStream();
//        String response = ConvertUtil.toString(inputStream);
//        inputStream.close();
//        return response;
//    }
//
//    public static String httpPost(String urlString, Iterable<? extends NameValuePair> params, Iterable<? extends NameValuePair> postParams) throws IOException {
//        合并参数
//        String urlParams;
//        if ((urlParams = generateString(params)) != null) {
//            urlString += ("?" + urlParams);
//        }
//
//        URL url = new URL(urlString);
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        httpURLConnection.setRequestMethod("POST");
//
//        写入Post数据
//        String postData;
//        if((postData = generateString(postParams)) != null){
//            httpURLConnection.setDoOutput(true);
//            OutputStream outputStream = httpURLConnection.getOutputStream();
//            outputStream.write(postData.getBytes());
//            outputStream.flush();
//            outputStream.close();
//        }
//
//        接收数据
//        InputStream inputStream = httpURLConnection.getInputStream();
//        String response = ConvertUtil.toString(inputStream);
//        inputStream.close();
//        return response;
//    }

    /**
     * 更新CookieManager中的cookie
     * @param cookieManager CookieManager
     * @param httpURLConnection 要更新的HttpUrlConnection
     * @throws IOException 更新Cookie失败
     */
    public static void updateCookieForConnection(CookieManager cookieManager, HttpURLConnection httpURLConnection) throws IOException {
        try {
            cookieManager.put(new URI(httpURLConnection.getURL().getHost()),httpURLConnection.getHeaderFields());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            //理论上不应该出现这个异常，通过URL提供的字符串应该是能够完成URI的构造
        }
    }

    /**
     * 载入CookieManager中的cookie
     * @param cookieManager cookieManager
     * @param httpURLConnection 要载入cookie的connection
     * @throws IOException 网络IO错误
     */
    public static void loadCookieForConnection(CookieManager cookieManager, HttpURLConnection httpURLConnection) throws IOException {
        Map<String,List<String>> stringCookieMap = null;
        try {
            stringCookieMap = cookieManager.get(new URI(httpURLConnection.getURL().getHost()),httpURLConnection.getHeaderFields());
            if (stringCookieMap.containsKey("Cookie")) {
                for (String s : stringCookieMap.get("Cookie")) {
                    httpURLConnection.addRequestProperty("Cookie", s);
                }
            }
            if (stringCookieMap.containsKey("Cookie2")) {
                for (String s : stringCookieMap.get("Cookie2")) {
                    httpURLConnection.addRequestProperty("Cookie2", s);
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            //理论上不应该出现这个异常，通过URL提供的字符串应该是能够完成URI的构造
        }
    }


    /**
     * 将NameValuePair转化成String
     * @param params NameValuePair集合
     * @return 最终成品，传入params为空时返回null
     */
    public static String generateString(Iterable<? extends NameValuePair> params) {
        return generateString(params,"=","&");
    }

    /**
     * 将NameValuePair转化成String
     * @param params NameValuePair集合
     * @param linkSymbol 链接Name和Value的符号
     * @param delimiter 分割多个NameValuePair的符号
     * @return 最终成品，传入params为空时返回null
     */
    public static String generateString(Iterable<? extends NameValuePair> params, String linkSymbol, String delimiter){
        String data = "";
        if(params != null){
            boolean head = true;//用于绕过第一组数据前的链接符号
            for (NameValuePair nameValuePair :
                    params) {
                if (head){
                    head = false;
                }else{
                    data += delimiter;
                }
                data += (nameValuePair.getName() + linkSymbol + nameValuePair.getValue());
            }
            return data;
        }
        return null;
    }
}
