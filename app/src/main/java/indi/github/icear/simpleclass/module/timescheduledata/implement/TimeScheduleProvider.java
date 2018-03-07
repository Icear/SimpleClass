package indi.github.icear.simpleclass.module.timescheduledata.implement;

import android.content.Context;

import java.security.InvalidParameterException;

import indi.github.icear.simpleclass.module.timescheduledata.contract.ITimeSchedule;
import indi.github.icear.simpleclass.module.timescheduledata.contract.ITimeScheduleProvider;
import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.ServerNotAvailableException;
import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.TargetNotFoundException;
import indi.github.icear.simpleclass.module.timescheduledata.implement.remoteresources.RemoteTimeScheduleHelper;
import indi.github.icear.simpleclass.module.timescheduledata.implement.util.FileUtil;

/**
 * Created by icear on 2018/3/4.
 * TimeScheduleProvider
 * 提供对时间表数据进行操作的函数，负责时间表数据的管理
 */

public class TimeScheduleProvider implements ITimeScheduleProvider {

    private Context context; //用于CachedTimeSchedule

    public TimeScheduleProvider(Context context) {
        this.context = context;
    }

    @Override
    public ITimeSchedule getTimeSchedule(String school, String section) throws
            TargetNotFoundException, ServerNotAvailableException {
        if ("".equals(school.trim()) || "".equals(section.trim())) {
            throw new InvalidParameterException("'school' or 'section' should not be null");
        }

        //对传入的标记过滤非法字符
        school = FileUtil.filterFileName(school);
        section = FileUtil.filterFileName(section);

        //TODO 考虑重新构建配置管理模块

        //先尝试读取本地缓存，在尝试远程数据
//        ITimeSchedule timeSchedule;
//        try {
//            timeSchedule = new CachedTimeSchedule(context, school, section);
//        } catch (CacheNotAvailableException e) {
//            //改为创建RemoteTimeSchedule
//            try {
//                timeSchedule = new RemoteTimeSchedule(school, section);
//                //要求缓存该时间表
//                CachedTimeSchedule.cacheTimeSchedule(context, school, section, timeSchedule.getTimeScheduleFileData());
//            } catch (ServerNotAvailableException | TargetNotFoundException e1) {
//                e1.printStackTrace();
//                throw e1;
//            }
//        }

        //由于缓存功能尚未解决过期问题，暂时改为直接创建RemoteTimeSchedule
        try {
            ITimeSchedule timeSchedule;
            timeSchedule = new RemoteTimeScheduleHelper().getTimeSchedule(school, section);
            return timeSchedule;
        } catch (ServerNotAvailableException | TargetNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
