package indi.github.icear.simpleclass.module.timescheduledata.implement;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.Arrays;

import indi.github.icear.simpleclass.SimpleClassApplication;
import indi.github.icear.simpleclass.module.timescheduledata.contract.ITimeSchedule;
import indi.github.icear.simpleclass.module.timescheduledata.implement.exception.CacheNotAvailableException;
import indi.github.icear.simpleclass.module.timescheduledata.implement.exception.ServerNotAvailableException;
import indi.github.icear.util.ConvertUtil;

/**
 * Created by icear on 2018/3/4.
 * 控制本地缓存的指定TimeSchedule
 */

public class CachedTimeSchedule implements ITimeSchedule {
    private static final String cacheDirTemplate = "/TimeScheduleCache/\\s/\\s.cache";
    private String cacheData;

    public CachedTimeSchedule(String school, String section) throws
            CacheNotAvailableException, ServerNotAvailableException {
        if ("".equals(school.trim()) || "".equals(section.trim())) {
            throw new InvalidParameterException("'school' or 'section' should not be null");
        }

        //检查缓存是否可用，不可用则抛出异常
        File externalCacheDir = SimpleClassApplication.getApplication().getExternalCacheDir();
        if (externalCacheDir == null || !externalCacheDir.canWrite()) {
            throw new ServerNotAvailableException
                    ("the externalCacheDir is not exist or not writable");
        }

        //生成目标缓存文件路径
        File targetFile = new File(externalCacheDir + String.format(cacheDirTemplate,
                Arrays.asList(school, section).toArray()));
        //检查文件是否存在
        if (!targetFile.exists()) {
            throw new CacheNotAvailableException("No cache for target is available");
        }

        //存在，将缓存读入内存
        try {
            BufferedInputStream inputStream = new BufferedInputStream(
                    new FileInputStream(targetFile));
            cacheData = ConvertUtil.toString(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            //文件读取时出现错误，尝试删除文件并抛出异常
            throw new CacheNotAvailableException("Read cache failed");
        }
    }

    /**
     * 调用此函数对要缓存的时间表数据进行缓存
     *
     * @param school    时间表所属学校
     * @param section   时间表所属学期
     * @param cacheData 时间表数据
     * @return 操作结果
     */
    public static boolean cacheTimeSchedule(String school, String section, String cacheData) {
        //检查缓存目录是否可用
        File externalCacheDir = SimpleClassApplication.getApplication().getExternalCacheDir();
        if (externalCacheDir == null || !externalCacheDir.canWrite()) {
            return false;
        }

        //生成最终缓存位置
        File cacheFile = new File(externalCacheDir + String.format(cacheDirTemplate,
                Arrays.asList(school, section).toArray()));

        //打开输出流，覆盖输出
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(cacheFile, false));
            bufferedOutputStream.write(cacheData.getBytes("UTF-8"));
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new Error(e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public InputStream getTimeScheduleInputStream() {
        try {
            return new ByteArrayInputStream(cacheData.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new Error(e.getClass().getSimpleName() + ":" + e.getMessage());
            //此异常会是逻辑性错误
        }
    }

    @Override
    public String getTimeScheduleFileData() {
        return cacheData;
    }
}
