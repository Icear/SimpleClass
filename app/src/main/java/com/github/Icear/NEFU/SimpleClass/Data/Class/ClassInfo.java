package com.github.Icear.NEFU.SimpleClass.Data.Class;

import java.util.List;

/**
 * Created by icear on 2017/9/13.
 * 储存课程时间信息
 */
public class ClassInfo {

    /*
     * 课程
     * 上课地点、上课时间、上课周数 三者关联，其中上课周数不一定连续，故独立成单独的数据
     */
    private String location;//上课地点
    private String room;//教室编号
    private List<Integer> week;//周数
    private int section;//节数
    private int weekDay;//星期天数（1-7）

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public List<Integer> getWeek() {
        return week;
    }

    public void setWeek(List<Integer> week) {
        this.week = week;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "location='" + location + '\'' +
                ", room='" + room + '\'' +
                ", week=" + week +
                ", section=" + section +
                ", weekDay=" + weekDay +
                '}';
    }
}
