package com.github.Icear.NEFU.SimpleClass;

import android.app.Activity;
import android.app.Application;

import com.github.Icear.NEFU.SimpleClass.Data.AcademicData.AcademicDataProvider;
import com.github.Icear.NEFU.SimpleClass.Data.TimeData.TimeDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icear on 2017/10/15.
 * 自定义Application类
 */

//Done 将Provider切换为非静态属性
public class SimpleClassApplication extends Application {
    private static SimpleClassApplication application;
    private AcademicDataProvider academicDataProvider;
    private TimeDataProvider timeDataProvider;
    private List<Activity> activityList;

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
    public AcademicDataProvider getAcademicDataProvider() {
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
    public TimeDataProvider getTimeDataProvider() {
        if (timeDataProvider == null) {
            timeDataProvider = new TimeDataProvider();
        }
        return timeDataProvider;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        activityList = new ArrayList<>();
        //在onCreate的时候获得自己的引用，使得包括presenter在内的Code能够访问Context
    }

    /**
     * 向Application注册Activity
     *
     * @param activity 要注册的Activity
     */
    public void registerActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 调用此项以退出APP
     */
    public void exitAPP() {
        for (Activity activity :
                activityList) {
            activity.finish();
        }
    }

}
