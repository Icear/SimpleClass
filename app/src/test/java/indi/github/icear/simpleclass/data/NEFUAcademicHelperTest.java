package indi.github.icear.simpleclass.data;

import org.junit.Test;

import java.util.List;

import indi.github.icear.simpleclass.data.academicdata.NEFUAcademicHelper;
import indi.github.icear.simpleclass.data.academicdata.entity.Class;

/**
 * Created by icear on 2017/11/1.
 * 测试
 */
public class NEFUAcademicHelperTest {

    @Test
    public void getUser() throws Exception {
        NEFUAcademicHelper nefuAcademicHelper = new NEFUAcademicHelper();
        nefuAcademicHelper.init("", "");
        System.out.println(nefuAcademicHelper.getUser().toString());
    }

    @Test
    public void getClasses() throws Exception {
        NEFUAcademicHelper nefuAcademicHelper = new NEFUAcademicHelper();
        nefuAcademicHelper.init("", "");
        List<Class> classList = nefuAcademicHelper.getClasses();
        System.out.println(classList);
        System.out.println("read " + classList.size() + " classes");
        int classInfoCount = 0;
        for (Class aClass : classList) {
            classInfoCount += aClass.getClassInfo().size();
        }
        System.out.println("read " + classInfoCount + " classInfos");

    }

}