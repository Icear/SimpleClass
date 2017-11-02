package com.github.Icear.NEFU.SimpleClass.Data;

import com.github.Icear.NEFU.SimpleClass.Data.Entity.Class;
import com.github.Icear.NEFU.SimpleClass.Data.Entity.User;

import java.io.IOException;
import java.util.List;

/**
 * Created by icear on 2017/10/7.
 * 用于承载Academic的数据，与ViewModule进行交互
 * 使用前需先使用{@link AcademicDataProvider#init(String, String)}函数进行初始化
 */

public class AcademicDataProvider {
    /**
     * 为了能在不同的ViewModule之间同步数据
     * 所以使得AcademicDataProvider唯一，并且不与ViewModule关联
     */
    //TODO 将Provider交由Application进行持有，以保证完整的生命周期
    private static AcademicDataProvider instance;
    private AcademicAdmin academicAdmin;
    private User user;
    private List<Class> classList;

    private AcademicDataProvider() {
    }

    public static AcademicDataProvider getInstance() {
        if (instance == null) {
            instance = new AcademicDataProvider();
        }

        return instance;
    }

    /**
     * 初始化AcademicDataProvider，传入用户名及密码以访问用户信息，函数包含网络访问操作
     *
     * @param account  用户名
     * @param password 密码
     * @return 初始化结果
     * @throws IOException 网络IO错误
     */
    public boolean init(String account, String password) throws IOException {
        academicAdmin = AcademicAdmin.newInstance(account, password);
        return academicAdmin != null;
    }

    /**
     * 获得User信息
     * 需先使用{@link AcademicDataProvider#init(String, String)}函数初始化
     *
     * @return 返回User信息，未初始化时返回Null
     */
    public User getUser() {
        if (user == null) {
            user = academicAdmin.getUser();
        }
        return user;
    }

    /**
     * 从网络读取课程信息数据，函数包含网络访问代码
     *
     * @return 课程信息
     * @throws IOException 网络IO或数据解析错误
     * @see AcademicDataProvider#getClasses()
     */
    public List<Class> getClassesFromNetwork() throws IOException {
        classList = academicAdmin.getClasses();
        return classList;
    }

    /**
     * 从本地读取课程信息数据
     * 数据来自网络缓存，要求曾调用过{@link AcademicDataProvider#getClassesFromNetwork()}函数
     *
     * @return 课程信息
     */
    public List<Class> getClasses() {
        return classList;
    }
}
