package com.github.Icear.NEFU.SimpleClass.Util;

import com.github.Icear.NEFU.SimpleClass.R;

import java.util.Random;

/**
 * Created by icear on 2017/10/14.
 * 生成随机颜色的工具类
 */

public class RandomColorUtil {
    private static int[] colorArray = new int[]{
            R.color.red,

            R.color.pink,

            R.color.purple,

            R.color.deepPurple,

            R.color.indigo,

            R.color.blue,

            R.color.lightBlue,

            R.color.cyan,

            R.color.teal,

            R.color.green,

            R.color.lightGreen,

            R.color.lime,

            R.color.yellow,

            R.color.amber,

            R.color.orange,

            R.color.deepOrange,

            R.color.brown,

            R.color.grey,

            R.color.blueGrey
    };

    private static  Random randomSeed = new Random();

    /**
     * 利用随机数随机返回一个预设定的Color
     * @return
     */
    public static int getRandomColor(){
        return colorArray[randomSeed.nextInt(colorArray.length)];
    }
}
