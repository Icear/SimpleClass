package indi.github.icear.simpleclass.module.academicdata.nefuacademic.entity;

import java.util.List;

import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.module.academicdata.entity.IClassInfo;

/**
 * Created by icear on 2017/9/13.
 * 课程数据类
 */
public class NEFUClass implements IClass {
    private String name;//课程名
    private String teachers;//上课老师
    private List<IClassInfo> classInfo;//课程时间信息

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTeachers() {
        return teachers;
    }

    @Override
    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

    @Override
    public List<IClassInfo> getClassInfo() {
        return classInfo;
    }

    @Override
    public void setClassInfo(List<IClassInfo> classInfo) {
        this.classInfo = classInfo;
    }

    @Override
    public String toString() {
        return "NEFUClass{" +
                "name='" + name + '\'' +
                ", teachers='" + teachers + '\'' +
                ", classInfo=" + classInfo.toString() +
                '}';
    }
}
