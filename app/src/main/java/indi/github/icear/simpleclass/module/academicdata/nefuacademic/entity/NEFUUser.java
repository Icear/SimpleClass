package indi.github.icear.simpleclass.module.academicdata.nefuacademic.entity;

import java.io.Serializable;

import indi.github.icear.simpleclass.module.academicdata.entity.IUser;

/**
 * Created by icear on 2017/9/25.
 * 教务处用户类，储存相关信息
 */
public class NEFUUser implements IUser, Serializable {
    private String name;
    private String id;
    private String major;//专业
    private String college;//学院

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getMajor() {
        return major;
    }

    @Override
    public void setMajor(String major) {
        this.major = major;
    }

    @Override
    public String getCollege() {
        return college;
    }

    @Override
    public void setCollege(String college) {
        this.college = college;
    }

    @Override
    public String toString() {
        return "NEFUUser{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", major='" + major + '\'' +
                ", college='" + college + '\'' +
                '}';
    }
}
