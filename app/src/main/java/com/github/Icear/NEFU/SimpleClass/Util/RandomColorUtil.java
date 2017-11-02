package com.github.Icear.NEFU.SimpleClass.Util;

import android.app.Application;
import android.graphics.Color;

import com.github.Icear.NEFU.SimpleClass.SimpleClassApplication;

import java.util.Random;

/**
 * Created by Icear on 2017/10/14.
 * 生成随机颜色的工具类
 */

public class RandomColorUtil {
    private static Random randomSeed = new Random();
    private static int[] colors;

    static{
        //用context读取res中预置的randomColor数组
        Application application = SimpleClassApplication.getApplication();
        int arrayId = application.getResources().getIdentifier("randomColor","array",application.getPackageName());
        colors = application.getResources().getIntArray(arrayId);
    }

    /**
     * 利用随机数随机返回一个预设定的Color
     * @return 预置randomColor数组中的某一个,某些未初始化情况下可能返回Color.GRAY
     */
    public static int getRandomColor(){
        int color = Color.GRAY;
        if (colors != null) {
            color = colors[randomSeed.nextInt(colors.length)];
        }
        return color;
    }
}
