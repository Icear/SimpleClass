package indi.github.icear.simpleclass.module.timescheduledata.contract;


import java.io.IOException;

import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.SchoolNotFoundException;
import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.SectionNotFoundException;

/**
 * Created by icear on 2018/3/4.
 * 用于提供TimeSchedule数据
 */

public interface ITimeScheduleProvider {
    /**
     * 获得指定学校下对应学期的时间表
     *
     * @param school  学校
     * @param section 学期（只支持数字与字母，其它字符会被过滤或替换，请注意）
     * @return ITimeSchedule 时间表数据
     * @throws SchoolNotFoundException  没有查找到指定学校存在已配置的时间表
     * @throws SectionNotFoundException 没有查找到指定学期存在已配置的时间表
     * @throws IOException              读取时间表数据时发生网络通信错误
     */
    ITimeSchedule getTimeSchedule(String school, String section) throws SchoolNotFoundException, SectionNotFoundException, IOException;
}
