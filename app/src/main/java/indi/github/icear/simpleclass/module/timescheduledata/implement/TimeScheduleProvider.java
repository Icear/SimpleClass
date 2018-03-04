package indi.github.icear.simpleclass.module.timescheduledata.implement;

import java.io.IOException;
import java.security.InvalidParameterException;

import indi.github.icear.simpleclass.module.timescheduledata.contract.ITimeSchedule;
import indi.github.icear.simpleclass.module.timescheduledata.contract.ITimeScheduleProvider;
import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.SchoolNotFoundException;
import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.SectionNotFoundException;
import indi.github.icear.simpleclass.module.timescheduledata.implement.exception.CacheNotAvailableException;
import indi.github.icear.simpleclass.module.timescheduledata.implement.exception.ServerNotAvailableException;

/**
 * Created by icear on 2018/3/4.
 * TimeScheduleProvider
 * 提供对时间表数据进行操作的函数，负责时间表数据的管理
 */

public class TimeScheduleProvider implements ITimeScheduleProvider {

    @Override
    public ITimeSchedule getTimeSchedule(String school, String section) throws
            SchoolNotFoundException, SectionNotFoundException, IOException {
        if ("".equals(school.trim()) || "".equals(section.trim())) {
            throw new InvalidParameterException("'school' or 'section' should not be null");
        }
        //TODO 这里逻辑总觉得有点不对劲。。。待完善
        //TODO 考虑重新构建配置管理模块
        //先尝试读取本地缓存，在尝试远程数据
        ITimeSchedule timeSchedule;
        try {
            timeSchedule = new CachedTimeSchedule(school, section);
        } catch (CacheNotAvailableException | ServerNotAvailableException e) {
            e.printStackTrace();
            //改为创建RemoteTimeSchedule
            timeSchedule = new RemoteTimeSchedule(school, section);

            //要求缓存该时间表
            CachedTimeSchedule.cacheTimeSchedule(school, section, timeSchedule.getTimeScheduleFileData());
        }

        return timeSchedule;
    }
}
