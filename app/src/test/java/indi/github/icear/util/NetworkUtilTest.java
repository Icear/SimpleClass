package indi.github.icear.util;

import org.junit.Test;

import indi.github.icear.network.util.NetworkUtil;

/**
 * Created by icear on 2017/10/8.
 * NetworkUtilTest
 */
public class NetworkUtilTest {
    @Test
    public void httpGetForString() throws Exception {
        System.out.println(NetworkUtil.httpGetForString("http://www.baidu.com", null));
    }

}