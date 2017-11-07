package com.github.Icear.NEFU.SimpleClass.Data.TimeData.Entity;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by icear on 2017/11/4.
 * 读取Json数据的对应实体类
 */

public class TimeData {
    private String timeZone;
    private Date semesterStartDay;
    private Map<String, List<TimeQuantum>> timeSchedule;

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Date getSemesterStartDay() {
        return semesterStartDay;
    }

    public void setSemesterStartDay(Date semesterStartDay) {
        this.semesterStartDay = semesterStartDay;
    }

    public Map<String, List<TimeQuantum>> getTimeSchedule() {
        return timeSchedule;
    }

    public void setTimeSchedule(Map<String, List<TimeQuantum>> timeSchedule) {
        this.timeSchedule = timeSchedule;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("TimeData{").append("\n")
                .append("semesterStartDay=").append(semesterStartDay).append("\n")
                .append("timeSchedule=");
        for (String key :
                timeSchedule.keySet()) {
            List<TimeQuantum> list = timeSchedule.get(key);
            stringBuilder.append("\t").append(key).append(":").append("\n");

            for (TimeQuantum t :
                    list) {
                stringBuilder.append("\t\t").append(t.toString()).append("\n");
            }
        }
        stringBuilder.append('}');

        return stringBuilder.toString();
    }
}
