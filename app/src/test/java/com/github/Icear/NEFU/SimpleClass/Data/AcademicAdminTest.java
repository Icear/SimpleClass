package com.github.Icear.NEFU.SimpleClass.Data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by icear on 2017/11/1.
 * 测试
 */
public class AcademicAdminTest {
    private AcademicAdmin instance;

    @Test
    public void newInstance() throws Exception {
        instance = AcademicAdmin.newInstance("","");
    }

    @Test
    public void getUser() throws Exception {
        newInstance();
        System.out.println(instance.getUser().toString());
    }

    @Test
    public void getClasses() throws Exception {
        newInstance();
        System.out.println(instance.getClasses());
    }

}