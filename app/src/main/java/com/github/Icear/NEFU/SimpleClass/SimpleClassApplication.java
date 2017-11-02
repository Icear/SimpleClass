package com.github.Icear.NEFU.SimpleClass;

import android.app.Application;

/**
 * Created by icear on 2017/10/15.
 * 自定义Application类
 */

public class SimpleClassApplication extends Application {
    private static SimpleClassApplication application;
    private static AcademicDataProvider academicDataProvider;

    /**
     * 获得Application对象
     *
     * @return Application对象
     */
    public static SimpleClassApplication getApplication() {
        return application;
    }

    /**
     * 获得AcademicDataProvider对象
     *
     * @return AcademicDataProvider对象
     */
    public static AcademicDataProvider getAcademicDataProvider() {
        if (academicDataProvider == null) {
            academicDataProvider = new AcademicDataProvider();
        }
        return academicDataProvider;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //在onCreate的时候获得自己的引用，使得包括presenter在内的Code能够访问Context
    }

}
