package indi.github.icear.util;


import java.security.InvalidParameterException;
import java.util.Locale;

/**
 * Created by icear on 2017/10/31.
 * 用于实现简单的Date操作
 * 支持英语和中文
 */

public class DateUtil {

    private static String[] initWeeks() {
        if (Locale.getDefault().getLanguage().equals(new Locale("zh").getLanguage())) {
            //中文设定
            return new String[]{
                    "星期一",
                    "星期二",
                    "星期三",
                    "星期四",
                    "星期五",
                    "星期六",
                    "星期日"
            };
        } else {
            //其余均按英语设定
            return new String[]{
                    "Monday",
                    "Tuesday",
                    "Wednesday",
                    "Thursday",
                    "Friday",
                    "Saturday",
                    "Sunday"
            };
        }
    }

    /**
     * 按照传入的数字转换为对应的文本格式
     * 包含中文和英语两种格式
     *
     * @param weekNumber 数字（必须在1-7之间
     * @return 文本格式的星期
     */
    public static String formatWeek(int weekNumber) {
        if (0 >= weekNumber && weekNumber > 7) {
            throw new InvalidParameterException("weekNumber must be between 1 and 7");
        }
        return initWeeks()[weekNumber - 1];
    }
}
