package indi.github.icear.simpleclass.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import indi.github.icear.simpleclass.viewmodule.BaseViewModule;

/**
 * Created by icear on 2017/10/7.
 * ModuleUtil，提供与Module操作有关的工具函数
 */

public class ModuleUtil {
    /**
     * 对Module初始化以完成对应模块的启动
     * @param fragmentManager fragmentManager
     * @param moduleName 要启动的module
     * @param bundle 要传入的数据
     * @param isAddToBackStack 是否将引入的UI加入到BackStack
     */
    public static void initModule(
            @NonNull android.support.v4.app.FragmentManager fragmentManager,
            @NonNull String moduleName,
            @Nullable Bundle bundle,
            boolean isAddToBackStack){
        /* 利用反射调用对应module，通过init初始化，再获取fragment以通知fragmentManager更新 */
        try {
            final Class<?> moduleClass = Class.forName(moduleName);
            BaseViewModule module = (BaseViewModule) moduleClass.newInstance();
            module.init(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(indi.github.icear.simpleclass.R.id.container, module.getFragment());

            if(isAddToBackStack){
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }  catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
