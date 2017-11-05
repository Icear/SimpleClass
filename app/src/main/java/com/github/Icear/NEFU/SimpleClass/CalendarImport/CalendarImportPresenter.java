package com.github.Icear.NEFU.SimpleClass.CalendarImport;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;

import com.github.Icear.NEFU.SimpleClass.Data.AcademicData.Entity.Class;
import com.github.Icear.NEFU.SimpleClass.Data.AcademicData.Entity.ClassInfo;
import com.github.Icear.NEFU.SimpleClass.Data.TimeData.Entity.TimeQuantum;
import com.github.Icear.NEFU.SimpleClass.Data.TimeData.TimeDataProvider;
import com.github.Icear.NEFU.SimpleClass.R;
import com.github.Icear.NEFU.SimpleClass.SimpleClassApplication;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by icear on 2017/11/3.
 * CalendarImportPresenter
 */

class CalendarImportPresenter implements CalendarImportContract.Presenter {
    private CalendarImportContract.View mView;
    private boolean isRunning; //防止重复执行

    CalendarImportPresenter(CalendarImportContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isRunning) {
            isRunning = true;
            startImportProgress();
        }
    }


    private void startImportProgress() {
        //从Provider拿到数据，对应插入到日历中，同时UI提供反馈

        mView.showProgress();

        /* 权限检查 */
        if (ActivityCompat.checkSelfPermission(SimpleClassApplication.getApplication(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //TODO 添加权限申请代码


            //没有权限时的处理
            mView.showWarningMessage(R.string.oh_no_we_cant_access_your_calendar_app_exit);
//            mView.quitAll();
            isRunning = false;
            return;
        }


        /*获得TimeManager*/
        TimeDataProvider timeDataProvider = SimpleClassApplication.getTimeDataProvider();

        /*获得ContentResolver*/
        ContentResolver cr = SimpleClassApplication.getApplication().getContentResolver();

        /*获得数据*/
        List<Class> classes = SimpleClassApplication.getAcademicDataProvider().getClasses();
        mView.showWorkingItems(classes);

        /*获得开学的第一天日期*/
        Date firstSemesterDay = timeDataProvider.getFirstSemesterDay();


        /*遍历数据，将每一个数据插入到日历中*/

        Calendar calendar = new GregorianCalendar();//准备容器
        Calendar calendarStart;
        Calendar calendarEnd;

        for (Class aClass :
                classes) {

            mView.showWorkingItem(aClass);

            for (ClassInfo classinfo :
                    aClass.getClassInfo()) {
                //为每一个class的每一个classInfo添加一个日历事件


                /* 填充数据 */
                ContentValues values = new ContentValues();

                //事件的标题,这里用课程名@上课地点，方面查看
                values.put(CalendarContract.Events.TITLE, aClass.getName() + "@" +
                        classinfo.getLocation() + classinfo.getRoom());

                //事件的发生地点，即上课地点
                values.put(CalendarContract.Events.EVENT_LOCATION, classinfo.getLocation()
                        + classinfo.getRoom());

                //事件的描述，这里附上授课教师
                values.put(CalendarContract.Events.DESCRIPTION,
                        SimpleClassApplication.getApplication().getString(R.string.teacher) + ": " + aClass.getTeachers());


                /*准备容器*/
                TimeQuantum classTimeQuantum = timeDataProvider
                        .getClassTime(classinfo.getLocation(), classinfo.getSection());


                /*
                 * 这里以周一为一周起点 TODO 考虑英语环境下以周日为起点的情况
                 * 完整时间 =
                 *      时间 + 日期 =
                 *      时间 + 开学第一天日期 + 上课第一个周（第几周）的数字 * 7 + 上课的天（周几） - 1
                 */

                //设定日期为开学第一日日期，然后再做日期加法
                calendar.set(firstSemesterDay.getYear(),//预设日期
                        firstSemesterDay.getMonth(),
                        firstSemesterDay.getDate()
                );

                //日期偏移量 = （第一个上课周 - 1）* 7天 + （第一个上课天 - 1）TODO 忘了有没有对classInfo 的week做有效检查。。。
                calendar.add(Calendar.DAY_OF_YEAR,
                        (classinfo.getWeek().get(0) - 1) * 7 + classinfo.getWeekDay() - 1);


                //设定课程节开始时间
                calendarStart = (Calendar) calendar.clone();
                calendarStart.set(Calendar.HOUR, classTimeQuantum.getStartTime().getHours());
                calendarStart.set(Calendar.MINUTE, classTimeQuantum.getStartTime().getMinutes());

                //事件的开始时间,配置为classInfo的第一个时间，然后按上课周数设定重复发生规则
                values.put(CalendarContract.Events.DTSTART, calendarStart.getTimeInMillis());


                //设定课程节结束时间
                calendarEnd = (Calendar) calendar.clone();
                calendarEnd.set(Calendar.HOUR, classTimeQuantum.getEndTime().getHours());
                calendarEnd.set(Calendar.MINUTE, classTimeQuantum.getEndTime().getMinutes());

                //事件的结束时间，规则同上
                values.put(CalendarContract.Events.DTEND, calendarEnd.getTimeInMillis());

                //事件的时区，这里使用默认导入者的时区即可
//                values.put(CalendarContract.Events.EVENT_TIMEZONE, "");

                values.put(CalendarContract.Events.RRULE, "");//事件的重复发生规则
                values.put(CalendarContract.Events.RDATE, "");//和上一个组合使用

                //将此事件视为忙碌时间
                values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

                cr.insert(CalendarContract.Events.CONTENT_URI, values);
            }
            mView.showItemWorkResult(aClass, true);
        }
        isRunning = false;
        mView.hideProgress();
        mView.showProgressFinished();
    }
}
