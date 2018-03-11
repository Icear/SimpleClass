package indi.github.icear.simpleclass.module.timescheduledata.implement.remoteresources;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;

import indi.github.icear.network.util.NetworkUtil;
import indi.github.icear.simpleclass.SimpleClassApplication;
import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.ServerNotAvailableException;
import indi.github.icear.simpleclass.module.timescheduledata.contract.exception.TargetNotFoundException;

/**
 * Created by icear on 2018/3/7.
 * 用于提供RemoteTimeSchedule相关操作API
 */

public class RemoteTimeScheduleHelper {
    private static final String TAG = RemoteTimeScheduleHelper.class.getSimpleName();

    public RemoteTimeSchedule getTimeSchedule(String school, String section)
            throws TargetNotFoundException, ServerNotAvailableException {
        if ("".equals(school.trim()) || "".equals(section.trim())) {
            throw new InvalidParameterException("'school' or 'section' should not be null");
        }

        String dirTemplate = "/TimeSchedule/%s/%s.json";
        String finalUrl = SimpleClassApplication.REMOTE_FILE_SERVER_PATH_ADDRESS
                + String.format(dirTemplate, school, section);
        Log.d(TAG, "the final resource URL is: " + finalUrl);
        try {
            return new RemoteTimeSchedule(NetworkUtil.httpGetForString(finalUrl, null));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //目标文件不存在
            throw new TargetNotFoundException
                    ("Target for school: " + school + " ,section: " + section + " not found"
                            + "\n"
                            + e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            //其它网络异常
            throw new ServerNotAvailableException
                    (e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
