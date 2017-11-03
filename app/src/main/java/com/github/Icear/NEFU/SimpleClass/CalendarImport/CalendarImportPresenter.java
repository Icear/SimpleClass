package com.github.Icear.NEFU.SimpleClass.CalendarImport;

import com.github.Icear.NEFU.SimpleClass.Data.Entity.Class;
import com.github.Icear.NEFU.SimpleClass.SimpleClassApplication;

import java.util.List;

/**
 * Created by icear on 2017/11/3.
 * CalendarImportPresenter
 */

public class CalendarImportPresenter implements CalendarImportContract.Presenter {
    @Override
    public void start() {

    }


    @Override
    public void startImportProgress() {
        //从Provider拿到数据，对应插入到日历中，同时UI提供反馈
        //TODO : View反馈

        /*获得数据*/
        List<Class> classes = SimpleClassApplication.getAcademicDataProvider().getClasses();


        /*遍历数据，将每一个数据插入到日历中*/


    }


}
