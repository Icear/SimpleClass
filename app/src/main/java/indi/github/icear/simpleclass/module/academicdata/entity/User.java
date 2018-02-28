package indi.github.icear.simpleclass.module.academicdata.entity;

/**
 * Created by icear on 2017/9/25.
 * 教务处用户类，储存相关信息
 */
public class User {
    private String name;
    private String id;
    private String major;//专业
    private String college;//学院

    /**
     * 读取用户姓名
     *
     * @return 用户姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户姓名
     * @param name 用户姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 读取用户编号（学生学号或教师工号）
     * @return 用户编号
     */
    public String getId() {
        return id;
    }

    /**
     * 设置用户编号（学生学号或教师工号
     * @param id 用户编号
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获得用户专业信息（仅学生拥有，教师暂不支持）
     * @return 用户专业信息
     */
    public String getMajor() {
        return major;
    }

    /**
     * 设置用户专业信息
     * @param major 用户专业信息
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * 获得用户学院信息（仅学生拥有，教师暂不支持）
     * @return 用户学院信息
     */
    public String getCollege() {
        return college;
    }

    /**
     * 设置用户学院信息（仅学生拥有，教师暂不支持）
     * @param college 用户学院信息
     */
    public void setCollege(String college) {
        this.college = college;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", major='" + major + '\'' +
                ", college='" + college + '\'' +
                '}';
    }
}
