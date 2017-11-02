package com.github.Icear.NEFU.SimpleClass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.Icear.NEFU.SimpleClass.Util.ModuleUtil;
import com.github.Icear.NEFU.SimpleClass.Welcome.WelcomeViewModule;

//TODO 改掉itemView的名字（item_class ->item_class)
//TODO 处理程序在中途离开的情况
//TODO 添加动画效果
//TODO 重新安排对话以符合google的建议
//TODO 添加英语支持

/*
 * 重新恢复：
 * //Done 将MainActivity完全抽象出来作为一个容器，将显示部分放到对应的fragment中
 * //Done 引入自定义Style
 * //Done 修正Presenter的触发位置
 * //Done 将ItemHelper抽出去
 * //Done 修改ClassListItem样式
 * //Done 为Academic等相关类补完注释
 * //TODO 补完ClassDetailViewModule
 * //TODO 修正部分warning
 * //TODO 确认presenter和View的职责，逻辑应该写在哪里
 * //Done 从ClassDetailViewModule回退到ClassListViewModule时会出现noAdapter的情况
 */
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

        ModuleUtil.initModule(getSupportFragmentManager(), WelcomeViewModule.class.getName(), null, false);
    }
}
