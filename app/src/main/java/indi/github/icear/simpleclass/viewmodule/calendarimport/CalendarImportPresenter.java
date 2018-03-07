package indi.github.icear.simpleclass.viewmodule.calendarimport;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import indi.github.icear.simpleclass.R;
import indi.github.icear.simpleclass.SimpleClassApplication;
import indi.github.icear.simpleclass.module.academicdata.AcademicDataProvider;
import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.module.academicdata.entity.IClassInfo;
import indi.github.icear.simpleclass.module.calendardata.CalendarDataProvider;
import indi.github.icear.simpleclass.module.calendardata.entity.CalendarInfo;
import indi.github.icear.simpleclass.module.calendardata.entity.EventInfo;
import indi.github.icear.simpleclass.module.timedata.TimeDataProvider;
import indi.github.icear.simpleclass.module.timedata.entity.TimeQuantum;
import indi.github.icear.simpleclass.module.timedata.exception.DataNotProvidedException;
import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.ServerNotAvailableException;
import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.TargetNotFoundException;
import indi.github.icear.simpleclass.viewmodule.MainActivity;

/**
 * Created by icear on 2017/11/3.
 * CalendarImportPresenter
 */
class CalendarImportPresenter implements CalendarImportContract.Presenter, MainActivity.OnRequestPermissionResultCallback {
    private static final String TAG = CalendarImportPresenter.class.getSimpleName();
    private static final int REQUEST_CODE_REQUEST_PERMISSION = 126;

    private CalendarImportContract.View mView;
    private boolean isRunning = false; //防止重复执行
    private boolean isFinished = false;//防止在操作结束后再次触发

    private Context context;
    private CalendarDataProvider calendarDataProvider;
    private TimeDataProvider timeDataProvider;
    private AcademicDataProvider academicDataProvider;

    private List<CalendarInfo> calendarInfoList;
    private List<IClass> classes;
    private String school;
    private String section;

    CalendarImportPresenter(CalendarImportContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void onCreate(Context context, Bundle bundle) {
        this.context = context;
        calendarDataProvider = new CalendarDataProvider(context.getContentResolver());
        timeDataProvider = new TimeDataProvider(context);
        if (bundle != null) {
            school = bundle.getString("school");
            section = bundle.getString("section");
            academicDataProvider = (AcademicDataProvider) bundle.getSerializable("AcademicDataProvider");
            Log.d(TAG, "bundle received, load school: " + school + " ,section: " + section);
        } else {
            Log.d(TAG, "bundle is null");
        }
    }

    @Override
    public void onStart() {
        if (!isRunning && !isFinished) {
            isRunning = true;
            if (!checkPermission()) {
                requestPermission();
            } else {
                initializeTimeData();
            }
        }
    }

    /**
     * 初始化TimeData
     */
    private void initializeTimeData() {
        //开始流程，初始化TimeData
        new InitializeTimeDataAsyncTask(timeDataProvider, school, section).execute();
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
        classes = academicDataProvider.getClasses();
        mView.showWorkingItems(classes);

        /*使用异步线程完成剩余操作*/
        new CalendarEventAsyncTask(
                calendarId, classes, timeDataProvider,
                new PresenterMessageHandler(new WeakReference<>(this))).execute();
    }


    private void onInitialTimeDataProviderSucceed() {
        askToConfirmCalendar();
    }

    /**
     * 获得应写入的日历id，如果传入值为null则创建新的日历
     *
     * @param calendar 目标日历
     * @return 日历id
     */
    private long getCalendarIdWithCreate(@Nullable CalendarInfo calendar) {
        long calendarId = -1;
        //因为没有关联账户所以设置为Local以避免因没有关联而被回收
        //调试时可以将这里改成自定义的字符串，从而触发系统的回收功能，表现效果为重启后或一段时间内导入的日历会自动消失，方便查看效果
        String presetAccountType = CalendarContract.ACCOUNT_TYPE_LOCAL;
        if (calendar == null) {
            //创建独立的帐户进行写入
            //检查预设帐户是否存在
            for (CalendarInfo calendarInfo : calendarInfoList) {
                if (calendarInfo.getAccountName().equals(SimpleClassApplication.PRESET_ACCOUNT_NAME)) {
                    calendarId = calendarInfo.getCalendarId();
                    break;
                }
            }
            if (calendarId == -1) {
                //不存在，创建之
                CalendarInfo newCalendar = new CalendarInfo();
                newCalendar.setAccountName(SimpleClassApplication.PRESET_ACCOUNT_NAME);
                newCalendar.setAccountType(presetAccountType);
                newCalendar.setOwnerAccount(SimpleClassApplication.PRESET_ACCOUNT_NAME);
                newCalendar.setName(context.getString(indi.github.icear.simpleclass.R.string.app_name));
                newCalendar.setCalendarDisplayName(context.getString(indi.github.icear.simpleclass.R.string.app_name));
                newCalendar.setCalendarColor(context.getResources().getColor(R.color.lightBlue));//允许个性化？
                newCalendar.setSyncEvent(true);
                newCalendar.setCalendarAccessLevel(CalendarContract.Calendars.CAL_ACCESS_OWNER);
                newCalendar.setCalendarTimeZone(timeDataProvider.getTimeZone());
                calendarId = calendarDataProvider.createNewCalendarAccount(newCalendar);
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
        calendarInfoList = calendarDataProvider.queryCalendar();

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
        return !(ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED);
    }

    /**
     * 请求相关权限
     */
    private void requestPermission() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.READ_CALENDAR
        };
        //TODO 等待重构
        MainActivity activity = MainActivity.getInstance();
        activity.setCallback(this);
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_REQUEST_PERMISSION);
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
                    onStart();
                } else {
                    //没有权限时的处理
                    mView.showWarningMessage(indi.github.icear.simpleclass.R.string.oh_no_we_cant_access_your_calendar_app_exit);
                    isFinished = true; //结束流程
//                    mView.quitAll();//退出程序
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
     * 初始化时间数据的任务
     */
    private class InitializeTimeDataAsyncTask extends AsyncTask<Long, Object, Object> {

        private String school;
        private String section;
        private TimeDataProvider timeDataProvider;

        private boolean succeed = false;
        private String errorMessage;

        InitializeTimeDataAsyncTask(TimeDataProvider timeDataProvider, String school, String section) {
            this.school = school;
            this.section = section;
            this.timeDataProvider = timeDataProvider;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isRunning = true;//强制更新状态
            isFinished = false;
            mView.showProgress();
        }

        @Override
        protected Object doInBackground(Long... params) {
            /*初始化时间数据*/
            try {
                initialTimeDataProvider(school, section);
                succeed = true;
            } catch (TargetNotFoundException | ServerNotAvailableException e) {
                e.printStackTrace();
                succeed = false;
                errorMessage = e.getClass().getSimpleName() + ": " + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (succeed) {
                onInitialTimeDataProviderSucceed();
                mView.hideProgress();
            } else {
                mView.showWarningMessage(errorMessage);
                isRunning = false;
                isFinished = false;//允许重新开始
                mView.hideProgress();
            }
        }

        /**
         * 初始化TimeDataProvider
         *
         * @param school  目标学校
         * @param section 目标学期
         */
        private void initialTimeDataProvider(String school, String section) throws
                TargetNotFoundException, ServerNotAvailableException {
            if (timeDataProvider.isInitialized() &&
                    timeDataProvider.getSchool().equals(school) &&
                    timeDataProvider.getSection().equals(section)) {
                //数据完整，无需重新初始化
                return;
            }
            timeDataProvider.init(school, section);
        }
    }


    /**
     * 用于完成向日历插入事件的任务
     */
    private class CalendarEventAsyncTask extends AsyncTask<Long, Object, Object> {

        private int eventCount = 0;//统计插入了多少事件
        private long calendarId;

        private List<IClass> classes;
        private TimeDataProvider timeDataProvider;
        private Handler mHandler;

        CalendarEventAsyncTask(long calendarId, List<IClass> classes,
                               TimeDataProvider timeDataProvider,
                               Handler receiver) {
            this.calendarId = calendarId;
            this.classes = classes;
            this.timeDataProvider = timeDataProvider;
            mHandler = receiver;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isRunning = true;//强制更新状态
            isFinished = false;
            mView.showProgress();
        }

        @Override
        protected Object doInBackground(Long... params) {

            /*遍历数据，将每一个数据插入到日历中*/
            for (IClass aClass :
                    classes) {

                mView.showWorkingItem(aClass);
                boolean isImported = true;

                for (IClassInfo classinfo :
                        aClass.getClassInfo()) {

                    /*准备事件模板*/
                    EventInfo eventTemplate = new EventInfo();

                    //事件所属日历的id
                    eventTemplate.setCalendarId(calendarId);

                    //设定事件的组织者，固定为SimpleClass@gmail.com，同时用作标记事件来自本程序的记号（用于后面的删除）
                    eventTemplate.setOrganizer(SimpleClassApplication.PRESET_ORGANIZER);

                    //事件的标题
                    eventTemplate.setTitle(aClass.getName());

                    //事件的发生地点，即上课地点
                    eventTemplate.setEventLocation(classinfo.getLocation() + classinfo.getRoom());

                    //事件的描述，这里附上授课教师
                    eventTemplate.setDescription(context.getString(indi.github.icear.simpleclass.R.string.teacher) + ": " + aClass.getTeachers());

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
                    } catch (DataNotProvidedException e) {
                        e.printStackTrace();
                        isImported = false;//导出失败
                        continue;
                    }

                    //获得开学的第一天日期
                    Date firstSemesterDay = timeDataProvider.getFirstSemesterDay();
                    Calendar gregorianCalendar = new GregorianCalendar();

                    //设定日期为开学第一日日期，然后再做日期加法
                    gregorianCalendar.set(firstSemesterDay.getYear() + 1900,
                            firstSemesterDay.getMonth(),//Calendar中month以0开头，和Date设定一样
                            firstSemesterDay.getDate(),
                            0,
                            0,
                            0
                    );//屏蔽未定义的时间对后面时间操作的影响

                    /*
                     * Calendar类使用set函数时并不会更新所有的数据，只有在调用getTime等函数时才会刷新
                     * 反而使用add时会立刻刷新所有数据
                     */

                    //设定时间模板
                    Calendar classStartTemplate = (Calendar) gregorianCalendar.clone();
                    classStartTemplate.set(Calendar.HOUR_OF_DAY, classTimeQuantum.getStartTime().getHours());
                    classStartTemplate.set(Calendar.MINUTE, classTimeQuantum.getStartTime().getMinutes());
                    classStartTemplate.set(Calendar.SECOND, classTimeQuantum.getStartTime().getSeconds());

//                    Log.d(TAG, "StartTime: " + classTimeQuantum.getStartTime().getHours() + ":" + classTimeQuantum.getStartTime().getMinutes() + ":" + classTimeQuantum.getStartTime().getSeconds());
//                    Log.d(TAG, "classStartTemplate: " + classStartTemplate.getTime());

                    Calendar classEndTemplate = (Calendar) gregorianCalendar.clone();
                    classEndTemplate.set(Calendar.HOUR_OF_DAY, classTimeQuantum.getEndTime().getHours());
                    classEndTemplate.set(Calendar.MINUTE, classTimeQuantum.getEndTime().getMinutes());
                    classStartTemplate.set(Calendar.SECOND, classTimeQuantum.getStartTime().getSeconds());

//                    Log.d(TAG, "EndTime: " + classTimeQuantum.getEndTime().getHours() + ":" + classTimeQuantum.getEndTime().getMinutes() + ":" + classTimeQuantum.getEndTime().getSeconds());
//                    Log.d(TAG, "classEndTemplate: " + classEndTemplate.getTime());

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

                            //添加周次备注
                            newEvent.setDescription(newEvent.getDescription() + " " +
                                    context.getString(R.string.weekCount, String.valueOf(week)));

                            calendarDataProvider.createNewEvent(newEvent);//创建
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
            isFinished = true;//流程结束
            mView.hideProgress();
            mView.showProgressSuccessfullyFinished();
        }

        /**
         * 通过MessageHandle要求主线程做出UI相关操作
         *
         * @param aClass     要变化的参数
         * @param isImported 要变化的参数，
         */
        private void showItemWorkResult(IClass aClass, boolean isImported) {
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
