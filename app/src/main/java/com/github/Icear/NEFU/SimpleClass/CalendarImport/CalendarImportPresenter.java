package com.github.Icear.NEFU.SimpleClass.CalendarImport;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.github.Icear.NEFU.SimpleClass.Data.AcademicData.Entity.Class;
import com.github.Icear.NEFU.SimpleClass.Data.AcademicData.Entity.ClassInfo;
import com.github.Icear.NEFU.SimpleClass.Data.CalendarData.CalendarDataHelper;
import com.github.Icear.NEFU.SimpleClass.Data.CalendarData.Entity.CalendarInfo;
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
    //TODO 将所有SimpleClassApplication的getApplication替换成Context

    private CalendarImportContract.View mView;
    private boolean isRunning; //防止重复执行
    private CalendarDataHelper calendarDataHelper;
    private ContentResolver cr;
    private List<CalendarInfo> calendarInfoList;


    CalendarImportPresenter(CalendarImportContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isRunning) {
            isRunning = true;
            askToConfirmCalendar();
        }
    }


    private void askToConfirmCalendar() {
        //从Provider拿到数据，对应插入到日历中，同时UI提供反馈

        mView.showProgress();

        /*权限检查*/
        if (!checkPermission()) {
            //没有权限时的处理
            mView.showWarningMessage(R.string.oh_no_we_cant_access_your_calendar_app_exit);
//            mView.quitAll();
            isRunning = false;
            mView.hideProgress();
            return;
        }


        /*获得ContentResolver*/
        cr = SimpleClassApplication.getApplication().getContentResolver();

        /*获得要插入的日历项*/
        calendarDataHelper = new CalendarDataHelper(cr);
        calendarInfoList = calendarDataHelper.queryCalendar();

        //传递给view
        mView.chooseOrCreateNewCalendar(calendarInfoList);

    }

    /**
     * 当用户确认要导入课程的日历时触发
     *
     * @param calendar 日历对象，null时将创建新日历
     */
    @Override
    public void onCalendarConfirm(@Nullable CalendarInfo calendar) {

        /*权限检查*/
        if (!checkPermission()) {
            //没有权限时的处理
            mView.showWarningMessage(R.string.oh_no_we_cant_access_your_calendar_app_exit);
//            mView.quitAll();
            isRunning = false;
            mView.hideProgress();
            return;
        }

        /*获得TimeManager*/
        TimeDataProvider timeDataProvider = SimpleClassApplication.getTimeDataProvider();

        /*检查calendar条件*/
        //TODO 将这些字符串转到配置文件中，还有AcademicDataHelper
        long calendarId = -1;
        String presetAccountName = "SimpleClass@gmail.com";
        String presetAccountType = "com.gmail";
        if (calendar == null) {
            //创建独立的帐户进行写入
            //检查预设帐户是否存在
            for (CalendarInfo calendarInfo : calendarInfoList) {
                if (calendarInfo.getAccountName().equals(presetAccountName)
                        && calendarInfo.getAccountType().equals(presetAccountType)
                        && calendarInfo.getOwnerAccount().equals(presetAccountName)) {
                    calendarId = calendarInfo.getCalendarId();
                    break;
                }
            }
            if (calendarId == -1) {
                //不存在，创建之
                CalendarInfo newCalendar = new CalendarInfo();
                newCalendar.setAccountName(presetAccountName);
                newCalendar.setAccountType(presetAccountType);
                newCalendar.setOwnerAccount(presetAccountName);
                newCalendar.setName(SimpleClassApplication.getApplication().getString(R.string.app_name));
                newCalendar.setCalendarDisplayName(SimpleClassApplication.getApplication().getString(R.string.app_name));
                newCalendar.setCalendarColor(Color.BLUE);//允许个性化？
                newCalendar.setSyncEvent(true);
                newCalendar.setCalendarAccessLevel(CalendarContract.Calendars.CAL_ACCESS_OWNER);
                newCalendar.setCalendarTimeZone(timeDataProvider.getTimeZone());
                calendarId = calendarDataHelper.createNewCalendarAccount(newCalendar);
            }
        } else {
            calendarId = calendar.getCalendarId();
        }


        /*获得数据*/
        List<Class> classes = SimpleClassApplication.getAcademicDataProvider().getClasses();
        mView.showWorkingItems(classes);

        /*获得开学的第一天日期*/
        Date firstSemesterDay = timeDataProvider.getFirstSemesterDay();


        /*遍历数据，将每一个数据插入到日历中*/

        Calendar gregorianCalendar = new GregorianCalendar();//准备容器
        Calendar classStart;
        Calendar classEnd;

        for (Class aClass :
                classes) {

            mView.showWorkingItem(aClass);

            for (ClassInfo classinfo :
                    aClass.getClassInfo()) {
                //为每一个class的每一个classInfo添加一个日历事件


                /* 开始填充数据 */
                ContentValues values = new ContentValues();

                values.put(CalendarContract.Events.CALENDAR_ID, calendarId);

                //事件的标题,这里用课程名@上课地点，方面查看
                values.put(CalendarContract.Events.TITLE, aClass.getName() + "@" +
                        classinfo.getLocation() + classinfo.getRoom());

                //事件的发生地点，即上课地点
                values.put(CalendarContract.Events.EVENT_LOCATION, classinfo.getLocation()
                        + classinfo.getRoom());

                //事件的描述，这里附上授课教师
                values.put(CalendarContract.Events.DESCRIPTION,
                        SimpleClassApplication.getApplication().getString(R.string.teacher) + ": " + aClass.getTeachers());


                /*取出上课的时间段数据*/
                TimeQuantum classTimeQuantum = timeDataProvider
                        .getClassTime(classinfo.getLocation(), classinfo.getSection());


                /*
                 * 这里以周一为一周起点 TODO 考虑英语环境下以周日为起点的情况
                 * 完整时间 =
                 *      时间 + 日期 =
                 *      时间 + 开学第一天日期 + 上课第一个周（第几周）的数字 * 7 + 上课的天（周几） - 1
                 */

                //设定日期为开学第一日日期，然后再做日期加法
                gregorianCalendar.set(firstSemesterDay.getYear(),//预设日期
                        firstSemesterDay.getMonth(),
                        firstSemesterDay.getDate()
                );

                //日期偏移量 = （第一个上课周 - 1）* 7天 + （第一个上课天 - 1）TODO 忘了有没有对classInfo 的week做有效检查。。。
                gregorianCalendar.add(Calendar.DAY_OF_YEAR,
                        (classinfo.getWeek().get(0) - 1) * 7 + classinfo.getWeekDay() - 1);


                //设定课程节开始时间
                classStart = (Calendar) gregorianCalendar.clone();
                classStart.set(Calendar.HOUR, classTimeQuantum.getStartTime().getHours());
                classStart.set(Calendar.MINUTE, classTimeQuantum.getStartTime().getMinutes());

                //事件的开始时间,配置为classInfo的第一个时间，然后按上课周数设定重复发生规则
                values.put(CalendarContract.Events.DTSTART, classStart.getTimeInMillis());


                //设定课程节结束时间
                classEnd = (Calendar) gregorianCalendar.clone();
                classEnd.set(Calendar.HOUR, classTimeQuantum.getEndTime().getHours());
                classEnd.set(Calendar.MINUTE, classTimeQuantum.getEndTime().getMinutes());

                //事件的结束时间，规则同上
                values.put(CalendarContract.Events.DTEND, classEnd.getTimeInMillis());

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

    private boolean checkPermission() {
         /* 权限检查 */
        if (ActivityCompat.checkSelfPermission(SimpleClassApplication.getApplication(), Manifest.permission.WRITE_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SimpleClassApplication.getApplication(), Manifest.permission.READ_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request nthe missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //TODO 添加权限申请代码

//            ActivityCompat.requestPermissions(SimpleClassApplication.getApplication(),);

            return false;
        }
        return true;
    }
}
