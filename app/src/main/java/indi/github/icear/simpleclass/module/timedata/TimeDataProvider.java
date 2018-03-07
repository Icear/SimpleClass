package indi.github.icear.simpleclass.module.timedata;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.security.InvalidParameterException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

import indi.github.icear.simpleclass.module.timedata.entity.TimeData;
import indi.github.icear.simpleclass.module.timedata.entity.TimeQuantum;
import indi.github.icear.simpleclass.module.timedata.exception.DataInitializeFailException;
import indi.github.icear.simpleclass.module.timedata.exception.DataNotProvidedException;
import indi.github.icear.simpleclass.module.timedata.util.DateDeserializer;
import indi.github.icear.simpleclass.module.timedata.util.TimeDeserializer;
import indi.github.icear.simpleclass.module.timescheduledata.contract.ITimeSchedule;
import indi.github.icear.simpleclass.module.timescheduledata.contract.ITimeScheduleProvider;
import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.ServerNotAvailableException;
import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.TargetNotFoundException;
import indi.github.icear.simpleclass.module.timescheduledata.implement.TimeScheduleProvider;


/**
 * Created by icear on 2017/9/13.
 * 提供各个教室的上课时间表以及相关信息
 */
public class TimeDataProvider {
    /*
     * 由TimeManager管理器来管理时间表
     * 设定提供一个初始化函数用于提前从数据表中读取时间表
     * 再提供一个函数来读取指定的时间
     */
    private static String TAG = TimeDataProvider.class.getSimpleName();
    private Context context;
    private TimeData timeData;
    private String school;
    private String section;
    private boolean initialized = false;

    public TimeDataProvider(Context context) {
        this.context = context;
    }

    /**
     * 获得时间数据
     *
     * @return 时间数据
     */
    public Map<String, List<TimeQuantum>> getTimeList() {
        return timeData.getTimeSchedule();
    }

    /**
     * 用于初始化预置数据
     *
     * @param school 目标学校
     * @param section 目标学期
     * @throws TargetNotFoundException 目标数据未找到
     * @throws ServerNotAvailableException 服务不可用
     * @throws DataInitializeFailException 数据初始化失败
     */
    public void init(String school, String section)
            throws TargetNotFoundException, ServerNotAvailableException, DataInitializeFailException {

        if ("".equals(school.trim()) || "".equals(section.trim())) {
            throw new InvalidParameterException("'school' or 'section' should not be null");
        }
        Log.i(TAG, "TimeDataProvider onStart init...");

        ITimeScheduleProvider timeScheduleProvider = new TimeScheduleProvider(context);
        ITimeSchedule timeSchedule = timeScheduleProvider.getTimeSchedule(school, section);
        String timeDataString = timeSchedule.getTimeScheduleFileData();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Time.class, new TimeDeserializer())//注册TimeDeserializer
                .registerTypeAdapter(Date.class, new DateDeserializer())//注册DateDeserializer
                .create();//创建Gson解析器
        timeData = gson.fromJson(timeDataString, new TypeToken<TimeData>() {
        }.getType());

        if (timeData != null) {
            Log.i(TAG, "successful loaded data.");
            Log.d(TAG, timeData.toString());
            initialized = true;
            this.school = school;
            this.section = section;
        } else {
            Log.e(TAG, "Oh no! We loaded nothing!");
            //如果读取到的数据为空
            throw new DataInitializeFailException("load nothing from json data file");
        }
    }

    /**
     * 检查是否已初始化
     * @return 初始化状态
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * 获得当前数据状态-学校
     *
     * @return 学校
     */
    public String getSchool() {
        return school;
    }

    /**
     * 获得当前数据状态-学期
     *
     * @return 学期
     */
    public String getSection() {
        return section;
    }

    /**
     * 获取指定的课程时间段
     *
     * @param classLocation 教室地点
     * @param index         课程位置
     * @return 仅在按照参数索引到目标时返回课程时间段，未查找到目标时返回Null
     * @exception DataNotProvidedException 当请求了没有被设定的time数据时抛出此异常
     */
    public TimeQuantum getClassTime(String classLocation, int index) throws DataNotProvidedException {
        if (index < 1) {
            throw new IllegalArgumentException("index: " + index);
        }

        if (timeData.getTimeSchedule().containsKey(classLocation)) {
            return timeData.getTimeSchedule().get(classLocation).get(index - 1);
        } else {
            throw new DataNotProvidedException("time data about " + classLocation + " had not been provided");
        }
    }

    /**
     * 获得学期第一天的日期
     *
     * @return 日期,
     */
    public Date getFirstSemesterDay() {
        return timeData.getSemesterStartDay();
    }

    /**
     * 获得时间数据的时区
     *
     * @return 时区
     */
    public String getTimeZone() {
        return timeData.getTimeZone();
    }

}
