package indi.github.icear.simpleclass.module.academicdata;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import indi.github.icear.network.BasicNameValuePair;
import indi.github.icear.network.NameValuePair;
import indi.github.icear.network.util.NetworkUtil;
import indi.github.icear.simpleclass.module.academicdata.entity.Class;
import indi.github.icear.simpleclass.module.academicdata.entity.ClassInfo;
import indi.github.icear.simpleclass.module.academicdata.entity.User;
import indi.github.icear.util.ConvertUtil;

/**
 * Created by icear on 2017/9/25.
 * 东北林业大学教务处类
 */
public class NEFUAcademicHelper implements AcademicDataHelper {
    /**
     * 读取周次模式：连续模式
     */
    private static final int READ_WEEK_MODE_CONTINUOUS = 915;
    /**
     * 读取周次模式：奇数周模式
     */
    private static final int READ_WEEK_MODE_ODD = 180;
    /**
     * 读取周次模式：偶数周模式
     */
    private static final int READ_WEEK_MODE_EVEN = 364;
    private static String TAG = NEFUAcademicHelper.class.getSimpleName();

    public NEFUAcademicHelper() {
        //用于保持Cookie的同步
        CookieHandler.setDefault(new CookieManager());
    }

    @Override
    public boolean init(String userName, String password) throws IOException {
        return login(userName, password);
    }

    @Override
    public User getUser() throws IOException {
        return initUser();
    }

    @Override
    public List<Class> getClasses() throws IOException {
        List<Class> classContainer = new ArrayList<>();

        String response;
        response = NetworkUtil.httpGetForString("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/xskb/xskb_list.do", null);
        Document document = Jsoup.parse(response);
        Element classTable = document.getElementsByAttributeValue("id", "kbtable").first();
        Element tBody = classTable.getAllElements().first();
        Elements trEls = tBody.getElementsByTag("tr");

        trEls.remove(0);//跳过第一行（星期标识）
        trEls.remove(trEls.size() - 1);//跳过最后一行（备注）

        int dayIndex = 0;
        int classIndex = 0;
        //行
        for (Element tr :
                trEls) {
            classIndex++;
            //列
            for (Element td :
                    tr.getElementsByTag("td")) {
                parseNode(classContainer, td.html(), classIndex, ++dayIndex);
            }
            dayIndex = 0;
        }

        Log.i(TAG, "finally get " + classContainer.size() + " classes");
        if (classContainer.size() == 0) {
            return null;
        } else {
            return classContainer;
        }
    }

    /**
     * 解析课程所在的html元素
     *
     * @param classContainer class容器
     * @param html           格子内html内容
     * @param classIndex     节数索引（第几节课）
     * @param dayIndex       天数索引（星期几）
     */
    private void parseNode(List<Class> classContainer, String html, int classIndex, int dayIndex) {
        Log.i(TAG, "tend to parseNode");
        Document document = Jsoup.parse(html);
        Element container = document.getElementsByAttributeValue("class", "kbcontent").first();
        Log.d(TAG, "container:");
        Log.d(TAG, container.html());
//        Pattern p = Pattern.compile("[-]*(<br>)?(.*?)<br><font title=\"周次\\(节次\\)\">(.*?)\\(周\\)</font><br><font title=\"教室\">(.*?)</font><br>");
        Pattern p = Pattern.compile("(<br>)?([^-<>]*?)\\s*?<br>\\s*?<font[\\s\\S]*?>(\\D*?)</font>\\s*?<br>\\s*?<font[\\s\\S]*?>(.*?)\\((.*?)\\)</font>\\s*?<br>\\s*?<font[\\s\\S]*?>(.*?)</font>\\s*?<br>");
        Matcher m = p.matcher(container.html());
        while (m.find()) {
            //用于兼容课程表格式,林大是每两节课合并在一起显示
            //所以这里对它添加两次，重新拆分成两节课以符合标准格式
            for (int i = classIndex * 2 - 1; i <= classIndex * 2; i++) {
                Log.d(TAG, "class find match");
                Log.d(TAG, "name: " + m.group(2));
                Log.d(TAG, "teacher: " + m.group(3));
                Log.d(TAG, "week: " + m.group(4));
                Log.d(TAG, "weekMode: " + m.group(5));
                Log.d(TAG, "location: " + m.group(6));

                ClassInfo classInfo = new ClassInfo();
                classInfo.setWeekDay(dayIndex);
                classInfo.setSection(i);

                if ("周".equals(m.group(5))) {
                    readWeek(classInfo, m.group(4), READ_WEEK_MODE_CONTINUOUS);//正常模式
                } else if ("单周".equals(m.group(5))) {
                    readWeek(classInfo, m.group(4), READ_WEEK_MODE_ODD);//单周模式
                } else if ("双周".equals(m.group(5))) {
                    readWeek(classInfo, m.group(4), READ_WEEK_MODE_EVEN);//双周模式
                }
                readLocation(classInfo, m.group(6));
                updateOrAddClass(classContainer, m.group(2), m.group(3), classInfo);
            }
        }
    }

    /**
     * 从mixLocation读取地点数据填充至classInfo中
     *
     * @param classInfo   要填充的classInfo
     * @param mixLocation 要读取的地点信息
     */
    private void readLocation(ClassInfo classInfo, String mixLocation) {
        Log.d(TAG, "parse Location");
        Log.d(TAG, mixLocation);
        /* 将上课地点的楼与教室分开，便于后面读取时间表 */
        Pattern p = Pattern.compile("(.{0,3})([a-zA-Z]?\\d{3})");
        Matcher m = p.matcher(mixLocation);
        if (m.find()) {
            Log.d(TAG, "location find match");
            Log.d(TAG, "location: " + m.group(1));
            Log.d(TAG, "room: " + m.group(2));
            classInfo.setLocation(m.group(1));
            classInfo.setRoom(m.group(2));
        } else {
            Log.e(TAG, "oh! We can't read this location!");
        }
    }

    /**
     * 从mixWeek读取周次信息填充至classInfo中
     *
     * @param classInfo 要填充的classInfo
     * @param mixWeek   要读取的周次信息
     * @param mode      周次模式，传入定义好的常数
     */
    private void readWeek(ClassInfo classInfo, String mixWeek, int mode) {
        Log.d(TAG, "tend to parse Week");
        Log.d(TAG, mixWeek);
        List<Integer> weekList = new ArrayList<>();

        switch (mode) {
            case READ_WEEK_MODE_ODD:
                for (String weekPart :
                        mixWeek.split(",")) {
                    //处理多段时间
                    String[] weeks = weekPart.split("-");//分割周起始与结束(只有一个时则只执行一次
                    for (int i = Integer.parseInt(weeks[0]); i <= Integer.parseInt(weeks[weeks.length - 1]); i++) {
                        if (i % 2 == 1) {
                            weekList.add(i);
                        }
                    }
                }
                break;
            case READ_WEEK_MODE_EVEN:
                for (String weekPart :
                        mixWeek.split(",")) {
                    //处理多段时间
                    String[] weeks = weekPart.split("-");//分割周起始与结束(只有一个时则只执行一次
                    for (int i = Integer.parseInt(weeks[0]); i <= Integer.parseInt(weeks[weeks.length - 1]); i++) {
                        if (i % 2 == 0) {
                            weekList.add(i);
                        }
                    }
                }
                break;
            case READ_WEEK_MODE_CONTINUOUS:
            default:
                for (String weekPart :
                        mixWeek.split(",")) {
                    //处理多段时间
                    String[] weeks = weekPart.split("-");//分割周起始与结束(只有一个时则只执行一次
                    for (int i = Integer.parseInt(weeks[0]); i <= Integer.parseInt(weeks[weeks.length - 1]); i++) {
                        weekList.add(i);
                    }
                }
                break;
        }
        Log.d(TAG, "final parse week number: " + weekList.size());
        classInfo.setWeek(weekList);
    }

    /**
     * 更新Class信息，当class信息与现有class相同时自动进行合并
     *
     * @param classContainer 容器
     * @param name           课程名称
     * @param teacher        上课教室
     * @param classInfo      上课信息
     */
    private void updateOrAddClass(List<Class> classContainer, String name, String teacher, ClassInfo classInfo) {
        //查找现有class，有相同者进行合并
        for (Class aClass :
                classContainer) {
            if (aClass.getName().equals(name) && aClass.getTeachers().equals(teacher)) {
                aClass.getClassInfo().add(classInfo);
                return;
            }
        }
        //未找到，创建新的
        Class newClass = new Class();
        newClass.setName(name);
        newClass.setTeachers(teacher);
        List<ClassInfo> classInfos = new ArrayList<>();
        classInfos.add(classInfo);
        newClass.setClassInfo(classInfos);
        classContainer.add(newClass);
    }


    /**
     * 执行登陆，登陆成功则初始化有效的cookieToken
     *
     * @param userName 用户名
     * @param password 密码
     * @return 成功返回true，失败返回false
     * @throws IOException 网络IO或数据处理错误
     */
    private boolean login(String userName, String password) throws IOException {
        Log.i(TAG, "Start login");
        List<NameValuePair> parameter = new ArrayList<>();
        parameter.add(new BasicNameValuePair("USERNAME", userName));
        parameter.add(new BasicNameValuePair("PASSWORD", password));

        URL url = new URL("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/xk/LoginToXk");

        Log.d(TAG, "execute request to " + url.toString());
        Log.d(TAG, "method: Post");

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");

        String postData = NetworkUtil.generateString(parameter);
        httpURLConnection.setRequestProperty("Accept-Encoding", "");
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);


        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(postData.getBytes("UTF-8"));
//        outputStream.write(Charset.forName("UTF-8").encode(postData));
        outputStream.flush();
        outputStream.close();

        InputStream inputStream = httpURLConnection.getInputStream();//开启输入流，但不需要接收数据
        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
            //登陆成功
            inputStream.close();
            Log.i(TAG, "Login status: successful");
            return true;
        } else {
            //登陆失败
            Log.i(TAG, "Login status: failed");
            Log.d(TAG, "response:");
            Log.d(TAG, "\tstatus code: " + httpURLConnection.getResponseCode());
            Log.d(TAG, "\tcontext:" + ConvertUtil.toString(inputStream));
            inputStream.close();
            return false;
        }
    }

    /**
     * 访问用户主页，读取相关信息并储存到User类中
     * @return user对象
     * @throws IOException 网络IO或读取信息失败
     */
    private User initUser() throws IOException {
        Log.i(TAG, "Start to init User");
        String response;
        response = NetworkUtil.httpGetForString("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/framework/main.jsp", null);
        Document document = Jsoup.parse(response);
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
            Log.i(TAG, "read info succeed, method finish");
            return user;
        } else {
            Log.e(TAG, "parse user info failed");
            Log.d(TAG, "container info:\n" + container);
            Log.d(TAG, "targetElement: \n" + info);
            throw new IOException("read user info failed, unknown format");
        }
    }
}
