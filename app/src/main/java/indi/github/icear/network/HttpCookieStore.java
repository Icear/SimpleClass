package indi.github.icear.network;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by icear on 2017/10/8.
 * HttpCookieStore，一个实验性的CookieStore实现
 */

public class HttpCookieStore implements CookieStore {

    private Map<URI,List<HttpCookie>> cookieStore;

    public HttpCookieStore(){
        cookieStore = new HashMap<>();
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        if (uri !=  null) {
            List<HttpCookie> cookieList;
            if(cookieStore.containsKey(uri)){
                cookieList = cookieStore.get(uri);
            }else{
                cookieList = new ArrayList<>();
                cookieStore.put(uri,cookieList);
            }
            if(cookie != null){
                cookieList.add(cookie);
            }
        }
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        if (uri != null) {
            return cookieStore.get(uri);
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public List<HttpCookie> getCookies() {
        List<HttpCookie> cookieList = new ArrayList<>();
        for (URI o : cookieStore.keySet()) {
            cookieList.addAll(cookieStore.get(o));
        }
        return cookieList;
    }

    @Override
    public List<URI> getURIs() {
        List<URI> urlList = new ArrayList<>();
        urlList.addAll(cookieStore.keySet());
        return urlList;
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return uri != null && cookie != null && cookieStore.containsKey(uri) && cookieStore.get(uri).remove(cookie);
    }

    @Override
    public boolean removeAll() {
        try {
            cookieStore.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
