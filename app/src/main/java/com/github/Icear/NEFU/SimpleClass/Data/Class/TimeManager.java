package com.github.Icear.NEFU.SimpleClass.Data.Class;

import android.util.Log;

import com.github.Icear.Util.ConvertUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.List;
import java.util.Map;


/**
 * Created by icear on 2017/9/13.
 * 储存各个教室的上课时间表
 */
public class TimeManager {
    /*
     * 由TimeManager管理器来管理时间表
     * 设定提供一个初始化函数用于提前从数据表中读取时间表
     * 再提供一个函数来读取指定的时间
     */
    private static String TAG = TimeManager.class.getSimpleName();
    private static TimeManager instance;
    private Map<String, List<TimeQuantum>> timeList;

    private TimeManager() {

    }

    /**
     * TimeManager
     * 单例模式
     *
     * @return 初始化成功返回TimeManager，失败返回Null
     */
    public static TimeManager getInstance() {
        if (instance == null) {
            try {
                instance = new TimeManager();
                instance.init();
            } catch (IOException e) {
                Log.d(TAG, "Exception occur during init");
                e.printStackTrace();
                instance = null;
            }
        }
        return instance;
    }

    /**
     * 获得时间数据
     *
     * @return 时间数据
     */
    public Map<String, List<TimeQuantum>> getTimeList() {
        return timeList;
    }

    /**
     * 设置时间数据
     *
     * @param timeList 时间数据
     */
    public void setTimeList(Map<String, List<TimeQuantum>> timeList) {
        this.timeList = timeList;
    }

    /**
     * 初始化函数，用于从本地读取预置数据
     *
     * @throws IOException 预置数据读写异常
     */
    public void init() throws IOException {
        Log.i(TAG, "TimeManager start init...");

        //TODO 检查获取input流为空的情况
        InputStream inputStream = Object.class.getResourceAsStream("/raw/timeschedule.json");//取得文件
        Log.d(TAG, "read \"timeschedule.json\" with inputStream: " + inputStream);

        String timeSchedule = ConvertUtil.toString(inputStream);//这里会抛出IOException，和文件读写有关
        Log.d(TAG, "convert data to String:");
        Log.d(TAG, timeSchedule);

        inputStream.close();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Time.class, new TimeDeserializer())
                .create();//注册LocalTimeDeserializer并创建Gson解析器
        timeList = gson.fromJson(timeSchedule, new TypeToken<Map<String, List<TimeQuantum>>>() {
        }.getType());
        if (timeList != null) {
            Log.i(TAG, "successful loaded data.");
            //输出
            for (String key :
                    timeList.keySet()) {
                List<TimeQuantum> list = timeList.get(key);
                Log.d(TAG, "\t" + key + ":");

                for (TimeQuantum t :
                        list) {
                    Log.d(TAG, "\t\t" + t.toString());
                }
            }
        } else {
            Log.w(TAG, "TimeManager load nothing!");
        }
    }

    /**
     * 获取指定的课程时间段
     *
     * @param classLocation 教室地点
     * @param index         课程位置
     * @return 仅在按照参数索引到目标时返回课程时间段，其余情况返回Null
     */
    public TimeQuantum getClassTime(String classLocation, int index) {
        if (timeList != null && timeList.containsKey(classLocation) && index >= 1) {
            return timeList.get(classLocation).get(index - 1);
        } else {
            return null;
        }
    }
}
