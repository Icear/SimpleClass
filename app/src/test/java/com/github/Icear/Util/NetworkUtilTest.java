package com.github.Icear.Util;

import com.github.Icear.Network.Util.NetworkUtil;

import org.junit.Test;

/**
 * Created by icear on 2017/10/8.
 */
public class NetworkUtilTest {
    @Test
    public void httpGet() throws Exception {
        System.out.println(NetworkUtil.httpGetForString("http://www.baidu.com", null));
    }

}