package indi.github.icear.simpleclass.module.timedata.entity;

import java.sql.Time;


/**
 * Created by icear on 2017/9/13.
 * 时间段数据
 */
public class TimeQuantum {
    private Time startTime;
    private Time endTime;

    /**
     * 获得开始时间
     *
     * @return 开始时间
     */
    public Time getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     * @param startTime 开始时间
     */
    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    /**
     * 获得结束时间
     *
     * @return 结束时间
     */
    public Time getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     * @param endTime 结束时间
     */
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
