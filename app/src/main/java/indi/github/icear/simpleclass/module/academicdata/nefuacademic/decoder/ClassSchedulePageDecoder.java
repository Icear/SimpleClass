package indi.github.icear.simpleclass.module.academicdata.nefuacademic.decoder;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.module.academicdata.nefuacademic.entity.NEFUClass;
import indi.github.icear.simpleclass.module.academicdata.nefuacademic.entity.NEFUClassInfo;
import indi.github.icear.simpleclass.module.academicdata.nefuacademic.util.InterfaceConvertUtil;

/**
 * Created by icear on 2018/2/28.
 * 课程表页面解析器
 */

public class ClassSchedulePageDecoder {
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
    private static String TAG = ClassSchedulePageDecoder.class.getName();
    private Document document;

    public ClassSchedulePageDecoder(String data) {
        document = Jsoup.parse(data);
    }

    /**
     * 获得页面中的课程
     *
     * @return 课程信息
     */
    public List<IClass> getClasses() {
        List<IClass> classContainer = new ArrayList<>();
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
        return classContainer;
    }

    /**
     * 获得可读取数据的学期列表
     * @return 学期列表
     */
    public List<String> getAvailableSections() {
        List<String> sectionList = new ArrayList<>();
        Element sectionContainer = document.getElementById("xnxq01id");
        Elements sections = sectionContainer.children();
        for (Element section :
                sections) {
            sectionList.add(section.attr("value"));
        }
        return sectionList;
    }

    /**
     * 解析课程所在的html元素
     *
     * @param classContainer class容器
     * @param html           格子内html内容
     * @param classIndex     节数索引（第几节课）
     * @param dayIndex       天数索引（星期几）
     */
    private void parseNode(List<IClass> classContainer, String html, int classIndex, int dayIndex) {
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

                NEFUClassInfo NEFUClassInfo = new NEFUClassInfo();
                NEFUClassInfo.setWeekDay(dayIndex);
                NEFUClassInfo.setSection(i);

                if ("周".equals(m.group(5))) {
                    readWeek(NEFUClassInfo, m.group(4), READ_WEEK_MODE_CONTINUOUS);//正常模式
                } else if ("单周".equals(m.group(5))) {
                    readWeek(NEFUClassInfo, m.group(4), READ_WEEK_MODE_ODD);//单周模式
                } else if ("双周".equals(m.group(5))) {
                    readWeek(NEFUClassInfo, m.group(4), READ_WEEK_MODE_EVEN);//双周模式
                }
                readLocation(NEFUClassInfo, m.group(6));
                updateOrAddClass(classContainer, m.group(2), m.group(3), NEFUClassInfo);
            }
        }
    }

    /**
     * 从mixLocation读取地点数据填充至classInfo中
     *
     * @param NEFUClassInfo   要填充的classInfo
     * @param mixLocation 要读取的地点信息
     */
    private void readLocation(NEFUClassInfo NEFUClassInfo, String mixLocation) {
        Log.d(TAG, "parse Location");
        Log.d(TAG, mixLocation);
        /* 将上课地点的楼与教室分开，便于后面读取时间表 */
        Pattern p = Pattern.compile("(.{0,3})([a-zA-Z]?\\d{3})");
        Matcher m = p.matcher(mixLocation);
        if (m.find()) {
            Log.d(TAG, "location find match");
            Log.d(TAG, "location: " + m.group(1));
            Log.d(TAG, "room: " + m.group(2));
            NEFUClassInfo.setLocation(m.group(1));
            NEFUClassInfo.setRoom(m.group(2));
        } else {
            Log.e(TAG, "oh! We can't read this location!");
        }
    }

    /**
     * 从mixWeek读取周次信息填充至classInfo中
     *
     * @param NEFUClassInfo 要填充的classInfo
     * @param mixWeek   要读取的周次信息
     * @param mode      周次模式，传入定义好的常数
     */
    private void readWeek(NEFUClassInfo NEFUClassInfo, String mixWeek, int mode) {
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
        NEFUClassInfo.setWeek(weekList);
    }

    /**
     * 更新Class信息，当class信息与现有class相同时自动进行合并
     *
     * @param classContainer 容器
     * @param name           课程名称
     * @param teacher        上课教室
     * @param NEFUClassInfo      上课信息
     */
    private void updateOrAddClass(List<IClass> classContainer, String name, String teacher, NEFUClassInfo NEFUClassInfo) {
        //查找现有class，有相同者进行合并
        for (IClass aClass :
                classContainer) {
            if (aClass.getName().equals(name) && aClass.getTeachers().equals(teacher)) {
                aClass.getClassInfo().add(NEFUClassInfo);
                return;
            }
        }
        //未找到，创建新的
        NEFUClass newClass = new NEFUClass();
        newClass.setName(name);
        newClass.setTeachers(teacher);
        List<NEFUClassInfo> NEFUClassInfos = new ArrayList<>();
        NEFUClassInfos.add(NEFUClassInfo);
        newClass.setClassInfo(InterfaceConvertUtil.convertListForIClassInfo(NEFUClassInfos));
        classContainer.add(newClass);
    }
}
