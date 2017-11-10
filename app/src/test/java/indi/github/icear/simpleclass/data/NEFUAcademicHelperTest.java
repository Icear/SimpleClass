package indi.github.icear.simpleclass.data;

import org.junit.Test;

import indi.github.icear.simpleclass.data.academicdata.NEFUAcademicHelper;

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
        System.out.println(nefuAcademicHelper.getClasses());
    }

}