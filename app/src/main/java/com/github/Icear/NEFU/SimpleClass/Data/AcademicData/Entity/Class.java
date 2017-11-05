package com.github.Icear.NEFU.SimpleClass.Data.AcademicData.Entity;

import java.util.List;

/**
 * Created by icear on 2017/9/13.
 * 课程数据类
 */
public class Class {
    private String name;//课程名
    private String teachers;//上课老师
    private List<ClassInfo> classInfo;//课程时间信息

    /**
     * 获得课程名称
     *
     * @return 课程名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设定课程名称
     *
     * @param name 课程名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获得授课教师姓名
     *
     * @return 授课教师姓名
     */
    public String getTeachers() {
        return teachers;
    }

    /**
     * 设置授课教师姓名
     *
     * @param teachers 授课教师姓名
     */
    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

    /**
     * 获得课程信息（时间、地点的不同组合）
     *
     * @return 课程信息
     */
    public List<ClassInfo> getClassInfo() {
        return classInfo;
    }

    /**
     * 设定课程信息（时间、地点的不同组合）
     *
     * @param classInfo 课程信息
     */
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
