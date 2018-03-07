package indi.github.icear.simpleclass.module.timescheduledata.contract;


import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.ServerNotAvailableException;
import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.TargetNotFoundException;

/**
 * Created by icear on 2018/3/4.
 * 用于提供TimeSchedule数据
 */

public interface ITimeScheduleProvider {
    /**
     * 获得指定学校下对应学期的时间表
     *
     * @param school  学校（传入的数据会保留数字与字母，其它字符会被过滤或替换，请注意）
     * @param section 学期（传入的数据会保留数字与字母，其它字符会被过滤或替换，请注意）
     * @return ITimeSchedule 时间表数据
     * @throws TargetNotFoundException     没有查找到指定的学校与学期存在已配置的时间表
     * @throws ServerNotAvailableException 服务不可用异常
     */
    ITimeSchedule getTimeSchedule(String school, String section)
            throws TargetNotFoundException, ServerNotAvailableException;
}
