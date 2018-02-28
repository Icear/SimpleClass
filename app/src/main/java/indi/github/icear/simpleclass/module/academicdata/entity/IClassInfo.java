package indi.github.icear.simpleclass.module.academicdata.entity;

import java.util.List;

/**
 * Created by icear on 2017/9/13.
 * 储存课程时间信息
 */
public interface IClassInfo {

    /*
     * 课程
     * 上课地点、上课时间、上课周数 三者关联，其中上课周数不一定连续，故独立成单独的数据
     */

    /**
     * 获得课程上课地点
     *
     * @return 上课地点
     */
    String getLocation();

    /**
     * 设置课程上课地点
     *
     * @param location 上课地点
     */
    void setLocation(String location);

    /**
     * 获得课程上课教室
     *
     * @return 上课教室
     */
    String getRoom();

    /**
     * 设置课程上课教室
     *
     * @param room 上课教室
     */
    void setRoom(String room);

    /**
     * 获得课程上课序号（当天第几节课）
     *
     * @return 上课序号
     */
    int getSection();

    /**
     * 设置课程上课序号（当天第几节课）
     *
     * @param section 上课序号
     */
    void setSection(int section);

    /**
     * 获得上课星期
     *
     * @return 上课星期
     */
    List<Integer> getWeek();

    /**
     * 设置上课星期
     *
     * @param week 上课星期
     */
    void setWeek(List<Integer> week);

    /**
     * 获得上课时间的天序号（星期几，从星期一开始为1）
     *
     * @return 天序号
     */
    int getWeekDay();

    /**
     * 设置上课时间的天序号（星期几，从星期一开始为1）
     *
     * @param weekDay 天序号
     */
    void setWeekDay(int weekDay);

}
