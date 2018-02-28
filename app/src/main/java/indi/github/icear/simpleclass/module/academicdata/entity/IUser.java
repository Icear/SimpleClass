package indi.github.icear.simpleclass.module.academicdata.entity;

/**
 * Created by icear on 2017/9/25.
 * 教务处用户类，储存相关信息
 */
public interface IUser {

    /**
     * 读取用户姓名
     *
     * @return 用户姓名
     */
    String getName();

    /**
     * 设置用户姓名
     * @param name 用户姓名
     */
    void setName(String name);

    /**
     * 读取用户编号（学生学号或教师工号）
     * @return 用户编号
     */
    String getId();

    /**
     * 设置用户编号（学生学号或教师工号
     * @param id 用户编号
     */
    void setId(String id);

    /**
     * 获得用户专业信息（仅学生拥有，教师暂不支持）
     * @return 用户专业信息
     */
    String getMajor();

    /**
     * 设置用户专业信息
     * @param major 用户专业信息
     */
    void setMajor(String major);

    /**
     * 获得用户学院信息（仅学生拥有，教师暂不支持）
     * @return 用户学院信息
     */
    String getCollege();

    /**
     * 设置用户学院信息（仅学生拥有，教师暂不支持）
     * @param college 用户学院信息
     */
    void setCollege(String college);


}
