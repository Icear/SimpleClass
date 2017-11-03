package com.github.Icear.NEFU.SimpleClass;

import android.app.Application;

/**
 * Created by icear on 2017/10/15.
 * 自定义Application类
 */

public class SimpleClassApplication extends Application {
    private static SimpleClassApplication application;
    private static AcademicDataProvider academicDataProvider;
    private static TimeManager timeManager;

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

    /**
     * 获得TimeManager对象
     *
     * @return TimeManager对象
     */
    public static TimeManager getTimeManager() {
        if (timeManager == null) {
            timeManager = new TimeManager();
        }
        return timeManager;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //在onCreate的时候获得自己的引用，使得包括presenter在内的Code能够访问Context
    }

}
