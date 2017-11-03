package com.github.Icear.NEFU.SimpleClass;

import com.github.Icear.NEFU.SimpleClass.Data.Entity.TimeQuantum;

import org.junit.Test;

import java.util.List;
import java.util.Map;


/**
 * Created by icear on 2017/9/22.
 * 测试
 *
 * @deprecated 依赖android SDK环境，无法正常执行
 */
public class TimeManagerTest {
    private TimeManager timeManager;

    @Test
    public void init() throws Exception {
        timeManager = new TimeManager();
        timeManager.init();
        System.out.println("TimeManager:" + timeManager);

    }

    @org.junit.Test
    public void getTimeList() throws Exception {
        init();
        System.out.println("TimeList:");
        Map<String, List<TimeQuantum>> timeList = timeManager.getTimeList();

        //输出
        for (String key :
                timeList.keySet()) {
            List<TimeQuantum> list = timeList.get(key);
            System.out.println("\t" + key + ":");

            for (TimeQuantum t :
                    list) {
                System.out.println("\t\t" + t.toString());
            }
        }


    }

}