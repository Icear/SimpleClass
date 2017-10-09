package com.github.Icear.NEFU.SimpleClass;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import com.github.Icear.NEFU.SimpleClass.Util.ModuleUtil;
import com.github.Icear.NEFU.SimpleClass.Welcome.WelcomeViewModule;

//TODO 处理程序在中途离开的情况
//TODO 配置Log4j
//TODO 将MainActivity的toolbar去掉，改为由每个fragment进行控制，避免标题无法即使更新
public class MainActivity extends AppCompatActivity {

    /*
     * 概要：
     *      计划以MVP模式来制作APP
     *      APP的交互设定为流水式，通过一步一步的操作来引导用户完成目标功能，即：将林大的课程导入至手机日历中
     *      流程如下：
     *          介绍 -> 登陆 -> 选择要导入的课程 -> 导入
     *      待扩展的特性：
     *          TODO 支持对已经导入的课程日历进行清除
     *          TODO 支持对课程日历的格式进行自定义
     *
     */

    /*
     * <del>将MainActivity作为presenter与view进行关联的区域</del>
     *
     * 抽象出独立的ModuleUtil与ViewModule来承担关联presenter与view
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ModuleUtil.initModule(getSupportFragmentManager(), WelcomeViewModule.class.getName(), null, false);
    }
}
