package indi.github.icear.simpleclass.module;

import org.junit.Test;

import java.util.List;

import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.module.academicdata.nefuacademic.NEFUAcademicHelper;

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
        List<IClass> classList = nefuAcademicHelper.getClasses();
        System.out.println(classList);
        System.out.println("read " + classList.size() + " classes");
        int classInfoCount = 0;
        for (IClass aClass : classList) {
            classInfoCount += aClass.getClassInfo().size();
        }
        System.out.println("read " + classInfoCount + " classInfos");

    }

}