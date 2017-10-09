package com.github.Icear.NEFU.SimpleClass.Data.Class;

import java.util.List;

/**
 * Created by icear on 2017/9/13.
 * 课程数据类
 */
public class Class {
    private String name;//课程名
    private String teachers;//上课老师
    private List<ClassInfo> classInfo;//课程时间信息

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeachers() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

    public List<ClassInfo> getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(List<ClassInfo> classInfo) {
        this.classInfo = classInfo;
    }

    @Override
    public String toString() {
        return "Class{" +
                "name='" + name + '\'' +
                ", teachers='" + teachers + '\'' +
                ", classInfo=" + classInfo.toString() +
                '}';
    }
}
