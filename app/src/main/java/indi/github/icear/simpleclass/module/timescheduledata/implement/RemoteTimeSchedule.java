package indi.github.icear.simpleclass.module.timescheduledata.implement;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;

import indi.github.icear.network.util.NetworkUtil;
import indi.github.icear.simpleclass.SimpleClassApplication;
import indi.github.icear.simpleclass.module.timescheduledata.contract.ITimeSchedule;

/**
 * Created by icear on 2018/3/4.
 * 读取网络提供TimeSchedule数据
 */

public class RemoteTimeSchedule implements ITimeSchedule {

    private String timeScheduleData;

    public RemoteTimeSchedule(String school, String section) throws IOException {
        if ("".equals(school.trim()) || "".equals(section.trim())) {
            throw new InvalidParameterException("'school' or 'section' should not be null");
        }

        String dirTemplate = "/TimeScheduleCache/\\s/\\s.cache";
        String finalUrl = SimpleClassApplication.remoteFileServerPathAddress
                + String.format(dirTemplate, school, section);
        timeScheduleData = NetworkUtil.httpGetForString(finalUrl, null);
    }

    @Override
    public InputStream getTimeScheduleInputStream() {
        try {
            return new ByteArrayInputStream(timeScheduleData.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new Error(e.getClass().getSimpleName() + ":" + e.getMessage());
            //此异常会是逻辑性错误
        }
    }

    @Override
    public String getTimeScheduleFileData() {
        return timeScheduleData;
    }
}
