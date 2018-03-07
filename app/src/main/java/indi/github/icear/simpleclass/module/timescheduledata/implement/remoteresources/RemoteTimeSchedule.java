package indi.github.icear.simpleclass.module.timescheduledata.implement.remoteresources;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import indi.github.icear.simpleclass.module.timescheduledata.contract.ITimeSchedule;

/**
 * Created by icear on 2018/3/4.
 * 读取网络提供TimeSchedule数据
 */

public class RemoteTimeSchedule implements ITimeSchedule {

    private String timeScheduleData;

    public RemoteTimeSchedule(String timeScheduleData) {
        this.timeScheduleData = timeScheduleData;
    }

    @Override
    public InputStream getTimeScheduleInputStream() {
        try {
            return new ByteArrayInputStream(timeScheduleData.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new Error(e.getClass().getSimpleName() + ":" + e.getMessage());
            //此异常会是逻辑性错误
        }
    }

    @Override
    public String getTimeScheduleFileData() {
        return timeScheduleData;
    }
}
