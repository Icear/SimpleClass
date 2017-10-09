package com.github.Icear.NEFU.SimpleClass.Data.Class;

import java.sql.Time;


/**
 * Created by icear on 2017/9/13.
 * 时间段数据
 */
public class TimeQuantum {
    private Time startTime;
    private Time endTime;

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TimeQuantum{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
