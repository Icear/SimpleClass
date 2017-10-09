package com.github.Icear.NEFU.SimpleClass.Data.Class;

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
    private static TimeManager instanse;
//    private Logger logger = LogManager.getLogger(TimeManager.class.getName());
    private Map<String, List<TimeQuantum>> timeList;

    public static TimeManager getInstanse(){
        if(instanse == null){
            instanse = new TimeManager();
        }
        return instanse;
    }

    private TimeManager() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<TimeQuantum>> getTimeList() {
        return timeList;
    }

    public void setTimeList(Map<String, List<TimeQuantum>> timeList) {
        this.timeList = timeList;
    }

    /**
     * 初始化函数，用于从本地读取预置数据
     *
     * @throws IOException 预置数据读写异常
     */
    public void init() throws IOException {
//        logger.info("TimeManager start init...");

        InputStream inputStream = Object.class.getResourceAsStream("/raw/timeschedule.json");//取得文件
//        logger.debug("read \"timeschedule.json\" with inputStream: " + inputStream);

        String timeSchedule = ConvertUtil.toString(inputStream);//这里会抛出IOException，和文件读写有关
//        logger.debug("convert data to String:");
//        logger.debug(timeSchedule);

        inputStream.close();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Time.class, new TimeDeserializer())
                .create();//注册LocalTimeDeserializer并创建Gson解析器
        timeList = gson.fromJson(timeSchedule, new TypeToken<Map<String, List<TimeQuantum>>>() {
        }.getType());
//        if (timeList != null) {
//            logger.info("successful loaded data.");
//            //输出
//            for (String key :
//                    timeList.keySet()) {
//                List<TimeQuantum> list = timeList.get(key);
//                logger.debug("\t" + key + ":");
//
//                for (TimeQuantum t :
//                        list) {
//                    logger.debug("\t\t" + t.toString());
//                }
//            }
//        } else {
//            logger.warn("TimeManager load nothing!");
//        }
    }

    /**
     * 获取指定的课程时间段
     *
     * @param classLocation 教室地点
     * @param index         课程位置
     * @return 课程时间段
     */
    public TimeQuantum getClassTime(String classLocation, int index) {
        if(timeList != null){
            return timeList.get(classLocation).get(index);
        }else{
            return null;
        }
    }
}
