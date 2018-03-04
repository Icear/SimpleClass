package indi.github.icear.simpleclass.module.timescheduledata.contract;

import java.io.InputStream;

/**
 * Created by icear on 2018/3/4.
 * 用于储存TimeSchedule数据实体
 */

public interface ITimeSchedule {
    /**
     * 获得TimeScheduleInputStream，使用后调用{@link InputStream#close()}关闭
     *
     * @return TimeScheduleFileInputStream
     */
    InputStream getTimeScheduleInputStream();

    /**
     * 获得TimeSchedule
     *
     * @return TimeSchedule文件数据
     */
    String getTimeScheduleFileData();
}
