package indi.github.icear.simpleclass.module.timescheduledata.implement.localcache;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import indi.github.icear.simpleclass.module.timescheduledata.contract.ITimeSchedule;

/**
 * Created by icear on 2018/3/4.
 * 控制本地缓存的指定TimeSchedule
 */

public class CachedTimeSchedule implements ITimeSchedule {
    private String cacheData;

    public CachedTimeSchedule(String cacheData) {
        this.cacheData = cacheData;
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
