package com.github.icear.nefu.simpleclass.Data;

import com.github.Icear.NEFU.AcademicAdmin;
import com.github.Icear.NEFU.Class.Class;
import com.github.Icear.NEFU.User;

import java.io.IOException;
import java.util.List;

/**
 * Created by icear on 2017/10/7.
 * 用于承载Academic的数据，与ViewModule进行交互
 */

public class AcademicDataProvider {
    /**
     * 为了能在不同的ViewModule之间同步数据
     * 所以使得AcademicDataProvider唯一，并且不与ViewModule关联
     */
    private static AcademicDataProvider instance;
    private AcademicAdmin academicAdmin;
    private User user;
    private List<Class> classList;

    private AcademicDataProvider(){}

    public static AcademicDataProvider getInstance(){
        if(instance == null){
            instance = new AcademicDataProvider();
        }
        return instance;
    }

    public boolean init(String account, String password) throws IOException {
        academicAdmin = new AcademicAdmin();
        return academicAdmin.init(account,password);
    }

    public User getUser(){
        if(user == null){
            user = academicAdmin.getUser();
        }
        return user;
    }

    public List<Class> getClasses() throws IOException {
        if(classList == null){
            classList = academicAdmin.getClasses();
        }
        return classList;
    }
}
