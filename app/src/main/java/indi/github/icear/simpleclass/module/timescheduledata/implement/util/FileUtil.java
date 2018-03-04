package indi.github.icear.simpleclass.module.timescheduledata.implement.util;

/**
 * Created by icear on 2018/3/4.
 * 文件操作相关工具
 */

public class FileUtil {

    /**
     * 过滤传入的文件名，并替换其中的非法字符
     *
     * @param fileName 要过滤的文件名
     * @return 过滤后的文件名
     */
    public static String filterFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9\\-]", "").replaceAll("\\s+", "-");
    }
}
