package indi.github.icear.simpleclass.module.academicdata.nefuacademic.decoder;


import android.support.annotation.NonNull;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import indi.github.icear.simpleclass.module.academicdata.entity.User;

/**
 * Created by icear on 2018/2/28.
 * 用于解析主页
 */

public class IndexPageDecoder {
    private static String TAG = IndexPageDecoder.class.getName();
    private Document document;

    public IndexPageDecoder(String data) {
        document = Jsoup.parse(data);
    }

    @NonNull
    public User getUser() throws IOException {
        Element container = document.getElementsByAttributeValue("class", "wap").first();
        Element targetElement = container.getElementsByAttributeValue("class", "block1text").first();
        String info = targetElement.html();

        String patten = "姓名：(.*?)\\n<br>[\\s\\S]*?学号：(\\d*?)\\n<br>";
        Pattern r = Pattern.compile(patten);
        Matcher m = r.matcher(info);
        if (m.find()) {
            User user = new User();
            user.setName(m.group(1));
            user.setId(m.group(2));
            return user;
        } else {
            Log.e(TAG, "parse user info failed");
            Log.d(TAG, "container info:\n" + container);
            Log.d(TAG, "targetElement: \n" + info);
            throw new IOException("read user info failed, unknown format");
        }
    }

}
