package com.github.Icear.NEFU.SimpleClass;

import android.app.Application;

/**
 * Created by icear on 2017/10/15.
 * 自定义Application类
 */

public class SimpleClassApplication extends Application {
    private static SimpleClassApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //在onCreate的时候获得自己的引用，使得包括presenter在内的Code能够访问Context
    }

    public static SimpleClassApplication getInstance(){
        return instance;
    }

}
