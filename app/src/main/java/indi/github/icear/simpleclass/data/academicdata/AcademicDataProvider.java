package indi.github.icear.simpleclass.data.academicdata;

import java.io.IOException;
import java.util.List;

import indi.github.icear.simpleclass.SimpleClassApplication;
import indi.github.icear.simpleclass.data.academicdata.entity.Class;
import indi.github.icear.simpleclass.data.academicdata.entity.User;

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
    private AcademicDataHelper academicDataHelper;
    private User user;
    private List<Class> classList;

    /**
     * 建议通过{@link SimpleClassApplication#getAcademicDataProvider()}函数获得实例
     * 以保证AcademicDataProvider可以获得完整的生命周期
     *
     * @see SimpleClassApplication#getAcademicDataProvider()
     */
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
    public User getUser() throws IOException {
        if (user == null) {
            user = academicDataHelper.getUser();
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
        classList = academicDataHelper.getClasses();
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
