package com.github.Icear.NEFU.SimpleClass.Data;

import com.github.Icear.NEFU.SimpleClass.Data.Entity.Class;
import com.github.Icear.NEFU.SimpleClass.Data.Entity.User;

import java.io.IOException;
import java.util.List;

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
    User getUser() throws IOException;

    /**
     * 获得课程数据
     *
     * @return classList
     * @throws IOException 网络IO或数据处理错误
     */
    List<Class> getClasses() throws IOException;
}
