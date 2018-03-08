package indi.github.icear.simpleclass.module.academicdata.nefuacademic.entity;

import java.io.Serializable;
import java.util.List;

import indi.github.icear.simpleclass.module.academicdata.entity.IClassInfo;

/**
 * Created by icear on 2017/9/13.
 * 储存课程时间信息
 */
public class NEFUClassInfo implements IClassInfo, Serializable {

    /*
     * 课程
     * 上课地点、上课时间、上课周数 三者关联，其中上课周数不一定连续，故独立成单独的数据
     */
    private String location;//上课地点
    private String room;//教室编号
    private List<Integer> week;//周数
    private int section;//节数
    private int weekDay;//星期天数（1-7）

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getRoom() {
        return room;
    }

    @Override
    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public int getSection() {
        return section;
    }

    @Override
    public void setSection(int section) {
        this.section = section;
    }

    @Override
    public List<Integer> getWeek() {
        return week;
    }

    @Override
    public void setWeek(List<Integer> week) {
        this.week = week;
    }

    @Override
    public int getWeekDay() {
        return weekDay;
    }

    @Override
    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    @Override
    public String toString() {
        return "NEFUClassInfo{" +
                "location='" + location + '\'' +
                ", room='" + room + '\'' +
                ", week=" + week +
                ", section=" + section +
                ", weekDay=" + weekDay +
                '}';
    }
}
