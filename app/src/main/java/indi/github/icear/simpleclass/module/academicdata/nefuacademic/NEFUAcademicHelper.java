package indi.github.icear.simpleclass.module.academicdata.nefuacademic;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import indi.github.icear.network.BasicNameValuePair;
import indi.github.icear.network.NameValuePair;
import indi.github.icear.network.util.NetworkUtil;
import indi.github.icear.simpleclass.module.academicdata.AcademicDataHelper;
import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.module.academicdata.entity.IUser;
import indi.github.icear.simpleclass.module.academicdata.nefuacademic.decoder.ClassSchedulePageDecoder;
import indi.github.icear.simpleclass.module.academicdata.nefuacademic.decoder.IndexPageDecoder;
import indi.github.icear.util.ConvertUtil;

/**
 * Created by icear on 2017/9/25.
 * 东北林业大学教务处类
 */
public class NEFUAcademicHelper implements AcademicDataHelper {

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
    public IUser getUser() throws IOException {
        return initUser();
    }

    @Override
    public List<IClass> getClasses(String section) throws IOException {
        Log.i(TAG, "getting classes for section: " + section);

        List<IClass> classContainer = new ArrayList<>();
        List<NameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair("xnxq01id", section));

        String response = NetworkUtil.httpPostForString("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/xskb/xskb_list.do", null, postData);
        ClassSchedulePageDecoder classSchedulePageDecoder = new ClassSchedulePageDecoder(response);
        classContainer.addAll(classSchedulePageDecoder.getClasses());

        Log.i(TAG, "decode the IClass Schedule, and get " + classContainer.size() + " classes");
        if (classContainer.size() == 0) {
            return null;
        } else {
            return classContainer;
        }
    }

    @Override
    public List<String> getSectionList() throws IOException {
        String response = NetworkUtil.httpGetForString("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/xskb/xskb_list.do", null);
        ClassSchedulePageDecoder classSchedulePageDecoder = new ClassSchedulePageDecoder(response);
        return classSchedulePageDecoder.getAvailableSections();
    }

    @Override
    public String getSchool() {
        return "Northeast-Forestry-University";
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
    private IUser initUser() throws IOException {
        Log.i(TAG, "Start to init IUser");
        String response = NetworkUtil.httpGetForString("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/framework/main.jsp", null);
        IndexPageDecoder indexPageDecoder = new IndexPageDecoder(response);
        IUser user = indexPageDecoder.getUser();
        Log.i(TAG, "read info succeed, method finish");
        return user;
    }


}
