package com.github.icear.nefu.simpleclass;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by icear on 2017/10/7.
 * ViewModule基础接口
 * 这个部分是为了模拟MVP设计中用于承担将Presenter与View链接的Activity的任务而设计的
 * 因为是一个Activity对应多个Fragment的设计，不存在与之对应的Activity，所以将其抽象成独立的ViewModule
 */

public interface BaseViewModule {
    void init(Bundle bundle);
    Fragment getFragment();
}
