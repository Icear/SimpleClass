package indi.github.icear.simpleclass.module.academicdata.entity;

import java.util.List;

/**
 * Created by icear on 2017/9/13.
 * 课程数据类
 */
public interface IClass {

    /**
     * 获得课程名称
     *
     * @return 课程名称
     */
    String getName();

    /**
     * 设定课程名称
     *
     * @param name 课程名称
     */
    void setName(String name);

    /**
     * 获得授课教师姓名
     *
     * @return 授课教师姓名
     */
    String getTeachers();

    /**
     * 设置授课教师姓名
     *
     * @param teachers 授课教师姓名
     */
    void setTeachers(String teachers);

    /**
     * 获得课程信息（时间、地点的不同组合）
     *
     * @return 课程信息
     */
    List<IClassInfo> getClassInfo();

    /**
     * 设定课程信息（时间、地点的不同组合）
     *
     * @param classInfo 课程信息
     */
    void setClassInfo(List<IClassInfo> classInfo);

}
