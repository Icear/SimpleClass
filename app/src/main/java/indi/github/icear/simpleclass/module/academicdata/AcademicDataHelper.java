package indi.github.icear.simpleclass.module.academicdata;

import java.io.IOException;
import java.util.List;

import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.module.academicdata.entity.IUser;

/**
 * Created by icear on 2017/11/2.
 * interface for accessing AcademicData
 */

public interface AcademicDataHelper {

    /**
     * 初始化工具
     *
     * @param userName 用户名
     * @param password 密码
     * @return 初始化结果
     * @throws IOException 网络IO或数据处理错误
     */
    boolean init(String userName, String password) throws IOException;

    /**
     * 获得User信息
     *
     * @return User对象
     * @throws IOException 网络IO或数据处理错误
     */
    IUser getUser() throws IOException;

    /**
     * 获得课程数据
     *
     * @return classList
     * @throws IOException 网络IO或数据处理错误
     */
    List<IClass> getClasses() throws IOException;
}
