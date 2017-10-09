package com.github.Icear.NEFU.SimpleClass.Data;

import com.github.Icear.NEFU.SimpleClass.Data.Class.Class;
import com.github.Icear.NEFU.SimpleClass.Data.Class.ClassInfo;
import com.github.Icear.Network.BasicNameValuePair;
import com.github.Icear.Network.HttpClient;
import com.github.Icear.Network.HttpCookieStore;
import com.github.Icear.Network.NameValuePair;
import com.github.Icear.Network.Util.NetworkUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by icear on 2017/9/25.
 * 教务处类
 */
public class AcademicAdmin {
//    private static Logger logger = LogManager.getLogger(AcademicAdmin.class.getName());

    private User user;
    private com.github.Icear.Network.HttpCookieStore cookieToken;//登陆令牌


    private AcademicAdmin(){}

    /**
     * 初始化工具，注册到对应的用户上
     *
     * @param userName 用户名
     * @param password 密码
     * @return 成功返回初始化的对象，失败返回null
     * @throws IOException 网络IO或数据处理错误
     */
    public static AcademicAdmin getInstance(String userName, String password) throws IOException {
        AcademicAdmin academicAdmin = new AcademicAdmin();
        if (academicAdmin.login(userName, password)) {
            academicAdmin.initUser();
            return academicAdmin;
        } else {
            return null;
        }
    }

    public User getUser() {
        return user;
    }

    public List<Class> getClasses() throws IOException {
        List<Class> classContainer = new ArrayList<>();

        String response = new HttpClient(cookieToken).httpGetForString("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/xskb/xskb_list.do",null);
//        String response = NetworkUtil.httpGetForString(closeableHttpClient,
//                "http://jwcnew.nefu.edu.cn/dblydx_jsxsd/xskb/xskb_list.do", null);
        Document document = Jsoup.parse(response);
        Element classTable = document.getElementsByAttributeValue("id", "kbtable").first();
        Element tbody = classTable.getAllElements().first();
        Elements trEls = tbody.getElementsByTag("tr");

        trEls.remove(0);//跳过第一行（星期标识）
        trEls.remove(trEls.size() - 1);//跳过最后一行（备注）

        int dayIndex = 0;
        int classIndex = 0;
        //行
        for (Element tr :
                trEls) {
            dayIndex++;
            //列
            for (Element td :
                    tr.getElementsByTag("td")) {
                parseNode(classContainer, td.html(), ++classIndex, dayIndex);
            }
            classIndex = 0;
        }

//        logger.info("finally get " + classContainer.size() + " classes");
        if (classContainer.size() == 0) {
            return null;
        } else {
            return classContainer;
        }
    }

//    public boolean hasLogin(){
//        return cookieToken != null;
//    }

//    public List<Class> getClasses() throws IOException {
//        List<Class> classContainer = new ArrayList<>();
//
//
//        try (CloseableHttpClient closeableHttpClient = HttpClients.custom().setDefaultCookieStore(cookieToken).build()) {
//            String response = NetworkUtil.httpGetForString(closeableHttpClient,
//                    "http://jwcnew.nefu.edu.cn/dblydx_jsxsd/xskb/xskb_list.do", null);
//            Document document = Jsoup.parse(response);
//            Element classTable = document.getElementsByAttributeValue("id", "kbtable").first();
//            Element tbody = classTable.getAllElements().first();
//            Elements trEls = tbody.getElementsByTag("tr");
//
//            trEls.remove(0);//跳过第一行（星期标识）
//            trEls.remove(trEls.size() - 1);//跳过最后一行（备注）
//
//            int dayIndex = 0;
//            int classIndex = 0;
//            //行
//            for (Element tr :
//                    trEls) {
//                dayIndex++;
//                //列
//                for (Element td :
//                        tr.getElementsByTag("td")) {
//                    parseNode(classContainer, td.html(), ++classIndex, dayIndex);
//                }
//                classIndex = 0;
//            }
//        }
////        logger.info("finally get " + classContainer.size() + " classes");
//        if (classContainer.size() == 0) {
//            return null;
//        } else {
//            return classContainer;
//        }
//    }

    private void parseNode(List<Class> classContainer, String html, int classIndex, int dayIndex) {
//        logger.info("tend to parseNode");
        Document document = Jsoup.parse(html);
        Element container = document.getElementsByAttributeValue("class", "kbcontent").first();
//        logger.debug("container:");
//        logger.debug(container.html());
//        Pattern p = Pattern.compile("[-]*(<br>)?(.*?)<br><font title=\"周次\\(节次\\)\">(.*?)\\(周\\)</font><br><font title=\"教室\">(.*?)</font><br>");
        Pattern p = Pattern.compile("(<br>)?([^-<>]*?)\\s*?<br>\\s*?<font[\\s\\S]*?>(\\D*?)</font>\\s*?<br>\\s*?<font[\\s\\S]*?>(.*?)\\(周\\)</font>\\s*?<br>\\s*?<font[\\s\\S]*?>(.*?)</font>\\s*?<br>");
        Matcher m = p.matcher(container.html());
        for (int i = classIndex * 2 - 1; i <= classIndex * 2; i++) {
            if (m.find()) {
//                logger.debug("class find match");
//                logger.debug("name: " + m.group(2));
//                logger.debug("teacher: " + m.group(3));
//                logger.debug("week: " + m.group(4));
//                logger.debug("location: " + m.group(5));

                ClassInfo classInfo = new ClassInfo();
                classInfo.setWeekDay(dayIndex);
                classInfo.setSection(i);

                readWeek(classInfo, m.group(4));
                readLocation(classInfo, m.group(5));
                updateClass(classContainer, m.group(2), m.group(3), classInfo);
            }
//            else {
//                logger.debug("no found, pass");
//            }
        }
    }

    private void readLocation(ClassInfo classInfo, String mixLocation) {
//        logger.debug("parse Location");
//        logger.debug(mixLocation);
        /* 将上课地点的楼与教室分开，便于后面读取时间表 */
        Pattern p = Pattern.compile("(.{0,3})([a-zA-Z]?\\d{3})");
        Matcher m = p.matcher(mixLocation);
        if (m.find()) {
//            logger.debug("location find match");
//            logger.debug("location: " + m.group(1));
//            logger.debug("room: " + m.group(2));
            classInfo.setLocation(m.group(1));
            classInfo.setRoom(m.group(2));
        }
//        else {
//            logger.error("oh! We can't read this location!");
//        }
    }

    private void readWeek(ClassInfo classInfo, String mixWeek) {
//        logger.debug("tend to parse Week");
//        logger.debug(mixWeek);
        List<Integer> weekList = new ArrayList<>();
        for (String weekPart :
                mixWeek.split(",")) {
            //处理多段时间
            String[] weeks = weekPart.split("-");//分割周起始与结束(只有一个时则只执行一次
            for (int i = Integer.parseInt(weeks[0]); i <= Integer.parseInt(weeks[weeks.length - 1]); i++) {
                weekList.add(i);
            }
        }
//        logger.debug("final parse week number: " + weekList.size());
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
    private void updateClass(List<Class> classContainer, String name, String teacher, ClassInfo classInfo) {
        //查找现有class，有相同者进行合并
        for (Class cclass :
                classContainer) {
            if (cclass.getName().equals(name) && cclass.getTeachers().equals(teacher)) {
                cclass.getClassInfo().add(classInfo);
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
//        logger.info("Start login");
        //执行登陆，获得有效的令牌Token
//        try (CloseableHttpClient closeableHttpClient = HttpClients.custom().setDefaultCookieStore(cookieToken).build()) {
//            List<NameValuePair> parameter = new ArrayList<>();
//            parameter.add(new BasicNameValuePair("USERNAME", userName));
//            parameter.add(new BasicNameValuePair("PASSWORD", password));
//
//            HttpPost httpPost = new HttpPost("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/xk/LoginToXk");
//            UrlEncodedFormEntity postBody = new UrlEncodedFormEntity(parameter, Consts.UTF_8);
//            httpPost.setEntity(postBody);
//
//            try (CloseableHttpResponse response = closeableHttpClient.execute(httpPost)) {
//                //检查是否登陆成功
//                if (response.getStatusLine().getStatusCode() == 302) {
//                    //登陆成功
////                    logger.info("Login status: succeed");
//                    return true;
//                } else {
//                    //登陆失败
//                    cookieToken = null;
////                    logger.info("Login status: failed");
////                    logger.debug("response:");
////                    logger.debug("\tstatus code: " + response.getStatusLine().getStatusCode());
////                    logger.debug("\tcontext:" + response.getEntity().toString());
//                    return false;
//                }
//            }
//        }

        cookieToken = new HttpCookieStore();//准备CookieStore

//        CookieHandler.setDefault(new CookieManager(cookieToken, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));


        List<NameValuePair> parameter = new ArrayList<>();
        parameter.add(new BasicNameValuePair("USERNAME", userName));
        parameter.add(new BasicNameValuePair("PASSWORD", password));

        URL url = new URL("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/xk/LoginToXk");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");

        String postData = NetworkUtil.generateString(parameter,"=","&");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);

        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(postData.getBytes());
        outputStream.flush();
        outputStream.close();

        InputStream inputStream = httpURLConnection.getInputStream();//开启输入流，但不需要接收数据
        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
            //登陆成功
            inputStream.close();
            CookieHandler.setDefault(null);
            return true;
        }else{
            //登陆失败
            inputStream.close();
            CookieHandler.setDefault(null);
            cookieToken = null;
            return false;
        }
    }

    /**
     * 访问用户主页，读取相关信息并初始化到User类中
     *
     * @throws IOException 网络IO或读取信息失败
     */
    private void initUser() throws IOException {
//        logger.info("Start to init User");
//        try (CloseableHttpClient closeableHttpClient = HttpClients.custom().setDefaultCookieStore(cookieToken).build()) {
//            String response = NetworkUtil.httpGetForString(closeableHttpClient,
//                    "http://jwcnew.nefu.edu.cn/dblydx_jsxsd/framework/main.jsp", null);

        String response = new HttpClient(cookieToken).httpGetForString( "http://jwcnew.nefu.edu.cn/dblydx_jsxsd/framework/main.jsp",null);
        Document document = Jsoup.parse(response);
        Element container = document.getElementsByAttributeValue("class", "wap").first();
        Element targetElement = container.getElementsByAttributeValue("class", "block1text").first();
        String info = targetElement.html();

        String patten = "姓名：(.*?)\\n<br>[\\s\\S]*?学号：(\\d*?)\\n<br>";
        Pattern r = Pattern.compile(patten);
        Matcher m = r.matcher(info);
        if (m.find()) {
            user = new User();
            user.setName(m.group(1));
            user.setId(m.group(2));
//                logger.info("read info succeed, method finish");
        } else {
//                logger.error("parse user info failed");
//                logger.debug("container info:\n" + container);
//                logger.debug("targetElement: \n" + info);
            throw new IOException("read user info failed, unknown format");
        }

    }
}
