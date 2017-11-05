package com.github.Icear.NEFU.SimpleClass.Data.AcademicData.Util;

import java.util.List;

/**
 * Created by icear on 2017/10/30.
 * 提供class数据的处理函数
 */

public class ClassConvertUtil {

    /**
     * 将class的week数字转换为%d-%d的格式
     *
     * @param weeks week信息
     * @return 文本信息
     */
    public static String toString(List<Integer> weeks) {
        StringBuilder text = new StringBuilder();

        //用循环一路递增，如果某个数字被截断了则该数字的前一个作为结束数字
        boolean startFlag = true;//标志，用来标记一段的开始

        //TODO 考虑在循环时外部修改了weeks的值的情况
        for (int i = 0; i < weeks.size(); i++) {
            if (startFlag) {
                if (i != 0) {
                    text.append(",");//分隔前一个
                }
                //起始数字，作为新的范围的首部
                text.append(weeks.get(i));

                //判断是否为最后一个数字
                if (i + 1 < weeks.size()) {
                    //检查下一个数字是否和当前数字连续
                    startFlag = weeks.get(i + 1) - weeks.get(i) != 1;
                }
            } else {
                //判断是否为最后一个数字
                if (i + 1 >= weeks.size()) {
                    text.append("-")
                            .append(weeks.get(i));
                } else if (weeks.get(i + 1) - weeks.get(i) != 1) {
                    //判断当前数字和下一个数字是否连续

                    //不连续,将当前值加入范围的末尾
                    text.append("-")
                            .append(weeks.get(i));
                    startFlag = true;
                }
            }
        }


        return text.toString();
    }
}
