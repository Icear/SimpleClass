package com.github.Icear.NEFU.SimpleClass.CalendarImport;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.github.Icear.NEFU.SimpleClass.Data.AcademicData.Entity.Class;
import com.github.Icear.NEFU.SimpleClass.Data.AcademicData.Entity.ClassInfo;
import com.github.Icear.NEFU.SimpleClass.Data.CalendarData.CalendarDataHelper;
import com.github.Icear.NEFU.SimpleClass.Data.CalendarData.Entity.CalendarInfo;
import com.github.Icear.NEFU.SimpleClass.Data.CalendarData.Entity.EventInfo;
import com.github.Icear.NEFU.SimpleClass.Data.TimeData.Entity.TimeQuantum;
import com.github.Icear.NEFU.SimpleClass.Data.TimeData.TimeDataProvider;
import com.github.Icear.NEFU.SimpleClass.PermissionQuestActivity;
import com.github.Icear.NEFU.SimpleClass.R;
import com.github.Icear.NEFU.SimpleClass.SimpleClassApplication;

import java.lang.ref.WeakReference;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by icear on 2017/11/3.
 * CalendarImportPresenter
 */
class CalendarImportPresenter implements CalendarImportContract.Presenter, PermissionQuestActivity.OnRequestPermissionResultCallback {
    private static final String TAG = CalendarImportPresenter.class.getSimpleName();
    private static final int REQUEST_CODE_REQUEST_PERMISSION = 126;

    private CalendarImportContract.View mView;
    private boolean isRunning; //防止重复执行
    private CalendarDataHelper calendarDataHelper;
    private List<CalendarInfo> calendarInfoList;
    private List<Class> classes;


    CalendarImportPresenter(CalendarImportContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isRunning) {
            isRunning = true;
            if (!checkPermission()) {
                requestPermission();
            } else {
                askToConfirmCalendar();
            }
        }
    }


    /**
     * 当用户确认要导入课程的日历时触发
     *
     * @param calendar 日历对象，null时将创建新日历
     */
    @Override
    public void onCalendarConfirm(@Nullable CalendarInfo calendar) {
        //把剥离的变量初始化和程序块合并，优化的事以后再做，先保证代码的清晰

        /*确定要插入的日历id*/
        long calendarId = getCalendarIdWithCreate(calendar);

        /*获得数据*/
        classes = SimpleClassApplication.getAcademicDataProvider().getClasses();
        mView.showWorkingItems(classes);

        /*使用异步线程完成剩余操作*/
        new CalendarEventAsyncTask(calendarId, classes, SimpleClassApplication.getTimeDataProvider(), new PresenterMessageHandler(new WeakReference<>(this))).execute();


//        /*获得TimeManager*/
//        timeDataProvider = SimpleClassApplication.getTimeDataProvider();
//
//        /*获得数据*/
//        List<Class> classes = SimpleClassApplication.getAcademicDataProvider().getClasses();
//        mView.showWorkingItems(classes);
//
//        /*获得日历*/
//        long calendarId = getCalendarIdWithCreate(calendar);
//
//        /*遍历数据，将每一个数据插入到日历中*/
//        for (Class aClass :
//                classes) {
//
//            mView.showWorkingItem(aClass);
//            boolean isImported = true;
//
//            for (ClassInfo classinfo :
//                    aClass.getClassInfo()) {
//
//                /*准备事件模板*/
//                EventInfo eventTemplate = new EventInfo();
//
//                //事件所属日历的id
//                eventTemplate.setCalendarId(calendarId);
//
//                //事件的标题,这里用课程名@上课地点，方面查看
//                eventTemplate.setTitle(aClass.getName() + "@" + classinfo.getLocation() + classinfo.getRoom());
//
//                //事件的发生地点，即上课地点
//                eventTemplate.setEventLocation(classinfo.getLocation() + classinfo.getRoom());
//
//                //事件的描述，这里附上授课教师
//                eventTemplate.setDescription(SimpleClassApplication.getApplication().getString(R.string.teacher) + ": " + aClass.getTeachers());
//
//                //事件的时区，这里使用默认导入者的时区即可
//                eventTemplate.setEventTimeZone(timeDataProvider.getTimeZone());
//
//                //将此事件视为忙碌时间
//                eventTemplate.setAvailability(CalendarContract.Events.AVAILABILITY_BUSY);
//
//
//                /*填充剩余的数据*/
//
//                /*
//                 * 这里以周一为一周起点 TODO 考虑英语环境下以周日为起点的情况
//                 * 完整时间 =
//                 *      时间 + 日期 =
//                 *      时间 + 开学第一天日期 + 上课第一个周（第几周）的数字 * 7 + 上课的天（周几） - 1
//                 */
//
//                //取出上课的时间段数据
//                TimeQuantum classTimeQuantum;
//                try {
//                    classTimeQuantum = timeDataProvider
//                            .getClassTime(classinfo.getLocation(), classinfo.getSection());
//                } catch (TimeDataProvider.DataNotProvidedException e) {
//                    e.printStackTrace();
//                    isImported = false;//导出失败
//                    continue;
//                }
//
//                //获得开学的第一天日期
//                Date firstSemesterDay = timeDataProvider.getFirstSemesterDay();
//                Calendar gregorianCalendar = new GregorianCalendar();
//                gregorianCalendar.set(firstSemesterDay.getYear(),//设定日期为开学第一日日期，然后再做日期加法
//                        firstSemesterDay.getMonth(),
//                        firstSemesterDay.getDate()
//                );
//
//                //设定时间模板
//                Calendar classStartTemplate = (Calendar) gregorianCalendar.clone();
//                classStartTemplate.set(Calendar.HOUR, classTimeQuantum.getStartTime().getHours());
//                classStartTemplate.set(Calendar.MINUTE, classTimeQuantum.getStartTime().getMinutes());
//
//                Calendar classEndTemplate = (Calendar) gregorianCalendar.clone();
//                classEndTemplate.set(Calendar.HOUR, classTimeQuantum.getEndTime().getHours());
//                classEndTemplate.set(Calendar.MINUTE, classTimeQuantum.getEndTime().getMinutes());
//
//                //为每一个class的每一个课时设定一个事件 TODO 忘了有没有对classInfo 的week做有效检查。。。
//                for (int week : classinfo.getWeek()) {
//                    try {
//                        EventInfo newEvent = eventTemplate.clone();
//
//                        //日期偏移量 = （第一个上课周 - 1）* 7天 + （第一个上课天 - 1）
//                        int amount = (week - 1) * 7 + classinfo.getWeekDay() - 1;
//
//                        // 设定课程节开始时间
//                        Calendar classStart = (Calendar) classStartTemplate.clone();
//                        classStart.add(Calendar.DAY_OF_YEAR, amount);
//                        newEvent.setDtStart(classStart.getTimeInMillis());//事件的开始时间
//
//                        //设定课程节结束时间
//                        Calendar classEnd = (Calendar) classEndTemplate.clone();
//                        classStart.add(Calendar.DAY_OF_YEAR, amount);
//                        newEvent.setDtEnd(classEnd.getTimeInMillis());//事件的结束时间
//
//                        calendarDataHelper.createNewEvent(newEvent);//创建
//                        eventCount++;
//                    } catch (CloneNotSupportedException e) {
//                        Log.w(TAG, "skip one event");
//                        e.printStackTrace();
//                    }
//                }
//            }
//            mView.showItemWorkResult(aClass, isImported);
//        }
//        isRunning = false;
//        mView.hideProgress();
//        mView.showProgressFinished();
//        Log.i(TAG, "create " + eventCount + " event(s)");
    }

    /**
     * 获得应写入的日历id，如果传入值为null则创建新的日历
     *
     * @param calendar 目标日历
     * @return 日历id
     */
    private long getCalendarIdWithCreate(@Nullable CalendarInfo calendar) {
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
                newCalendar.setCalendarTimeZone(SimpleClassApplication.getTimeDataProvider().getTimeZone());
                calendarId = calendarDataHelper.createNewCalendarAccount(newCalendar);
            }
        } else {
            calendarId = calendar.getCalendarId();
        }
        return calendarId;
    }

    /**
     * 要求用户确认要导入的日历
     */
    private void askToConfirmCalendar() {
        //从Provider拿到数据，对应插入到日历中，同时UI提供反馈

        /*获得要插入的日历项*/
        calendarDataHelper = new CalendarDataHelper(SimpleClassApplication.getApplication().getContentResolver());
        calendarInfoList = calendarDataHelper.queryCalendar();

        //传递给view，要求用户选择或创建一个新的日历
        mView.chooseOrCreateNewCalendar(calendarInfoList);
    }


    /**
     * 检查相关权限
     *
     * @return 是否有权限
     */
    private boolean checkPermission() {
         /* 权限检查 */
        if (ActivityCompat.checkSelfPermission(SimpleClassApplication.getApplication(), Manifest.permission.WRITE_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SimpleClassApplication.getApplication(), Manifest.permission.READ_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return false;
        }
        return true;
    }

    /**
     * 请求相关权限
     */
    private void requestPermission() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.READ_CALENDAR
        };
        ActivityCompat.requestPermissions(new PermissionQuestActivity(this), permissions, REQUEST_CODE_REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean GRANT_PERMISSION_READ_CALENDAR = false;
        boolean GRANT_PERMISSION_WRITE_CALENDAR = false;

        switch (requestCode) {
            case REQUEST_CODE_REQUEST_PERMISSION:
                for (int i = 0; i < permissions.length; i++) {
                    if (Manifest.permission.READ_CALENDAR.equals(permissions[i])) {
                        GRANT_PERMISSION_READ_CALENDAR = (grantResults[i] == PackageManager.PERMISSION_GRANTED);
                    } else if (Manifest.permission.WRITE_CALENDAR.equals(permissions[i])) {
                        GRANT_PERMISSION_WRITE_CALENDAR = (grantResults[i] == PackageManager.PERMISSION_GRANTED);
                    }
                }
                isRunning = false;
                if (GRANT_PERMISSION_READ_CALENDAR && GRANT_PERMISSION_WRITE_CALENDAR) {
                    start();
                } else {
                    //没有权限时的处理
                    mView.showWarningMessage(R.string.oh_no_we_cant_access_your_calendar_app_exit);
//                    mView.quitAll();
                }
                break;
        }
    }

    /**
     * 用于接收来自子线程的UI操作请求
     */
    private static class PresenterMessageHandler extends Handler {

        static final int ACTION_SHOW_ITEM_WORK_RESULT = 568;
        static final String ACTION_SHOW_ITEM_WORK_RESULT_ITEM_INDEX = "ACTION_SHOW_ITEM_WORK_RESULT_ITEM_INDEX";
        static final String ACTION_SHOW_ITEM_WORK_RESULT_RESULT_INDEX = "ACTION_SHOW_ITEM_WORK_RESULT_RESULT_INDEX";
        private WeakReference<CalendarImportPresenter> calendarImportPresenterWeakReference;//使用WeakReference来避免内存泄漏

        PresenterMessageHandler(WeakReference<CalendarImportPresenter> calendarImportPresenterWeakReference) {
            this.calendarImportPresenterWeakReference = calendarImportPresenterWeakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case ACTION_SHOW_ITEM_WORK_RESULT:
                    Bundle bundle = msg.getData();
                    CalendarImportPresenter calendarImportPresenter = calendarImportPresenterWeakReference.get();
                    if (calendarImportPresenter != null) {
                        calendarImportPresenter.mView.showItemWorkResult(
                                calendarImportPresenter.classes.get(bundle.getInt(ACTION_SHOW_ITEM_WORK_RESULT_ITEM_INDEX)),
                                bundle.getBoolean(ACTION_SHOW_ITEM_WORK_RESULT_RESULT_INDEX));
                    }
                    break;
            }
        }

    }

    /**
     * 用于完成向日历插入事件的任务
     */
    private class CalendarEventAsyncTask extends AsyncTask<Long, Object, Object> {

        private int eventCount = 0;//统计插入了多少事件
        private long calendarId;
        private List<Class> classes;
        private TimeDataProvider timeDataProvider;
        private Handler mHandler;

        CalendarEventAsyncTask(long calendarId, List<Class> classes, TimeDataProvider timeDataProvider, Handler receiver) {
            this.calendarId = calendarId;
            this.classes = classes;
            this.timeDataProvider = timeDataProvider;
            mHandler = receiver;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mView.showProgress();
        }

        @Override
        protected Object doInBackground(Long... params) {


            /*遍历数据，将每一个数据插入到日历中*/
            for (Class aClass :
                    classes) {

                mView.showWorkingItem(aClass);
                boolean isImported = true;

                for (ClassInfo classinfo :
                        aClass.getClassInfo()) {

                    /*准备事件模板*/
                    EventInfo eventTemplate = new EventInfo();

                    //事件所属日历的id
                    eventTemplate.setCalendarId(calendarId);

                    //事件的标题,这里用课程名@上课地点，方面查看
                    eventTemplate.setTitle(aClass.getName());

                    //事件的发生地点，即上课地点
                    eventTemplate.setEventLocation(classinfo.getLocation() + classinfo.getRoom());

                    //事件的描述，这里附上授课教师
                    eventTemplate.setDescription(SimpleClassApplication.getApplication().getString(R.string.teacher) + ": " + aClass.getTeachers());

                    //事件的时区，这里使用默认导入者的时区即可
                    eventTemplate.setEventTimeZone(timeDataProvider.getTimeZone());

                    //将此事件视为忙碌时间
                    eventTemplate.setAvailability(CalendarContract.Events.AVAILABILITY_BUSY);


                    /*填充剩余的数据*/

                    /*
                     * 这里以周一为一周起点 TODO 考虑英语环境下以周日为起点的情况
                     * 完整时间 =
                     *      时间 + 日期 =
                     *      时间 + 开学第一天日期 + 上课第一个周（第几周）的数字 * 7 + 上课的天（周几） - 1
                     */

                    //取出上课的时间段数据
                    TimeQuantum classTimeQuantum;
                    try {
                        classTimeQuantum = timeDataProvider
                                .getClassTime(classinfo.getLocation(), classinfo.getSection());
                    } catch (TimeDataProvider.DataNotProvidedException e) {
                        e.printStackTrace();
                        isImported = false;//导出失败
                        continue;
                    }

                    //获得开学的第一天日期
                    Date firstSemesterDay = timeDataProvider.getFirstSemesterDay();
                    Calendar gregorianCalendar = new GregorianCalendar();
                    Log.d(TAG, String.valueOf(firstSemesterDay.getYear() + 1900));
                    Log.d(TAG, String.valueOf(firstSemesterDay.getMonth() + 1));
                    Log.d(TAG, String.valueOf(firstSemesterDay.getDate()));

                    //设定日期为开学第一日日期，然后再做日期加法
                    gregorianCalendar.set(firstSemesterDay.getYear() + 1900,
                            firstSemesterDay.getMonth() + 1,
                            firstSemesterDay.getDate()
                    );

                    //设定时间模板
                    Calendar classStartTemplate = (Calendar) gregorianCalendar.clone();
                    classStartTemplate.set(Calendar.HOUR, classTimeQuantum.getStartTime().getHours());
                    classStartTemplate.set(Calendar.MINUTE, classTimeQuantum.getStartTime().getMinutes());
                    classStartTemplate.set(Calendar.SECOND, classTimeQuantum.getStartTime().getSeconds());

                    Calendar classEndTemplate = (Calendar) gregorianCalendar.clone();
                    classEndTemplate.set(Calendar.HOUR, classTimeQuantum.getEndTime().getHours());
                    classEndTemplate.set(Calendar.MINUTE, classTimeQuantum.getEndTime().getMinutes());
                    classStartTemplate.set(Calendar.SECOND, classTimeQuantum.getStartTime().getSeconds());

                    //为每一个class的每一个课时设定一个事件
                    for (int week : classinfo.getWeek()) {
                        try {
                            EventInfo newEvent = eventTemplate.clone();

                            //日期偏移量 = （第一个上课周 - 1）* 7天 + （第一个上课天 - 1）
                            int amount = (week - 1) * 7 + classinfo.getWeekDay() - 1;

                            // 设定课程节开始时间
                            Calendar classStart = (Calendar) classStartTemplate.clone();
                            classStart.add(Calendar.DATE, amount);
                            newEvent.setDtStart(classStart.getTimeInMillis());//事件的开始时间

                            //设定课程节结束时间
                            Calendar classEnd = (Calendar) classEndTemplate.clone();
                            classEnd.add(Calendar.DATE, amount);
                            newEvent.setDtEnd(classEnd.getTimeInMillis());//事件的结束时间

                            calendarDataHelper.createNewEvent(newEvent);//创建
                            eventCount++;
                        } catch (CloneNotSupportedException e) {
                            Log.w(TAG, "skip one event");
                            e.printStackTrace();
                        }
                    }
                }
                showItemWorkResult(aClass, isImported);
            }

            Log.i(TAG, "create " + eventCount + " event(s)");
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            isRunning = false;
            mView.hideProgress();
            mView.showProgressFinished();
        }

        /**
         * 通过MessageHandle要求主线程做出UI相关操作
         *
         * @param aClass     要变化的参数
         * @param isImported 要变化的参数，
         */
        private void showItemWorkResult(Class aClass, boolean isImported) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(PresenterMessageHandler.ACTION_SHOW_ITEM_WORK_RESULT_RESULT_INDEX, isImported);
            bundle.putInt(PresenterMessageHandler.ACTION_SHOW_ITEM_WORK_RESULT_ITEM_INDEX, classes.indexOf(aClass));
            Message message = new Message();
            message.setData(bundle);
            message.arg1 = PresenterMessageHandler.ACTION_SHOW_ITEM_WORK_RESULT;//设置标志
            mHandler.sendMessage(message);
        }

    }

}
