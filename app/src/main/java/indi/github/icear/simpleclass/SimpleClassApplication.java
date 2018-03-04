package indi.github.icear.simpleclass;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import indi.github.icear.simpleclass.module.academicdata.nefuacademic.NEFUAcademicHelper;

/**
 * Created by icear on 2017/10/15.
 * 自定义Application类
 */

public class SimpleClassApplication extends Application {


    //将需要配置的属性放到这里统一管理
    public static String PRESET_ACCOUNT_NAME = "SimpleClass@gmail.com";//单独创建日历时的日历账户
    public static String PRESET_ACADEMIC_DATA_HELPER = NEFUAcademicHelper.class.getName();//要使用的AcademicDataHelper
    public static String PRESET_ORGANIZER = "SimpleClass@gmail.com";//预设的事件组织者
    public static String remoteFileServerPathAddress = "https://raw.githubusercontent.com/Icear/SimpleClass/master/data";//远程文件服务器目录地址

    private static SimpleClassApplication application;


    private List<Activity> activityList;

    /**
     * 获得Application对象
     *
     * @return Application对象
     */
    public static SimpleClassApplication getApplication() {
        return application;
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
