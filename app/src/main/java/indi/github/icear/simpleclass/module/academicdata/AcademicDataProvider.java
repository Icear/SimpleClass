package indi.github.icear.simpleclass.module.academicdata;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.List;

import indi.github.icear.simpleclass.SimpleClassApplication;
import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.module.academicdata.entity.IUser;

/**
 * Created by icear on 2017/10/7.
 * 用于承载Academic的数据，与ViewModule进行交互
 * 使用前需先使用{@link AcademicDataProvider#init(String, String)}函数进行初始化
 */

public class AcademicDataProvider implements Serializable {
    private AcademicDataHelper academicDataHelper;
    private IUser user;
    private List<IClass> classList;
    private List<String> sectionList;

    public AcademicDataProvider() {
    }

    /**
     * 初始化AcademicDataProvider，传入用户名及密码以访问用户信息，函数包含网络访问操作
     *
     * @param account  用户名
     * @param password 密码
     * @return 初始化结果
     * @throws IOException 网络IO错误
     */
    public boolean init(String account, String password) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        academicDataHelper = (AcademicDataHelper) java.lang.Class.forName(
                SimpleClassApplication.PRESET_ACADEMIC_DATA_HELPER).newInstance();//使用预设的helper
        return academicDataHelper.init(account, password);
    }

    /**
     * 获得User信息
     * 需先使用{@link AcademicDataProvider#init(String, String)}函数初始化
     * @return User对象
     * @exception IOException 网络IO错误或数据处理错误
     */
    public IUser getUser() throws IOException {
        if (user == null) {
            user = academicDataHelper.getUser();
        }
        return user;
    }

    /**
     * 从网络读取课程信息数据，函数包含网络访问代码
     *
     * @param section 目标学期的课程数据
     * @return 课程信息
     * @throws IOException 网络IO或数据解析错误
     * @see AcademicDataProvider#getClasses()
     */
    public List<IClass> getClassesFromNetwork(String section) throws IOException {
        if (sectionList == null) {
            sectionList = academicDataHelper.getSectionList();
        }

        if (sectionList.contains(section)) {
            classList = academicDataHelper.getClasses(section);
            return classList;
        } else {
            throw new InvalidParameterException("not available section: " + section);
        }
    }

    /**
     * 从本地读取上一次课程信息数据
     * 数据来自网络缓存，要求曾调用过{@link AcademicDataProvider#getClassesFromNetwork(String section)}函数
     *
     * @return 课程信息
     */
    public List<IClass> getClasses() {
        return classList;
    }

    /**
     * 获得可读取数据的学期列表
     *
     * @return 学期列表
     */
    public List<String> getSectionList() throws IOException {
        sectionList = academicDataHelper.getSectionList();
        return sectionList;
    }

    /**
     * 获得当前操作的学校代号
     *
     * @return 学校代号
     */
    public String getSchool() {
        return academicDataHelper.getSchool();
    }
}
