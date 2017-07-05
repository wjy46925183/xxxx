package com.dlg.data;

import demo.java.com.data.BuildConfig;

/**
 * 作者：wangdakuan
 * 主要功能:URL 请求连接
 * 创建时间：2016/6/1 13:58
 * ---------------------------------
 */
public class UrlNet {
    public static String getName() {
        if (IS_RELEASE) {
            //线上
            return "http://dlg.dalinggong.com/v_2_4";
        } else {
            //预发布版
//            return "http://dlgprev.dalinggong.com";
            //测试
            return "http://10.20.31.201:8080";
        }
    }
    private static final Boolean IS_RELEASE = BuildConfig.IS_RELEASE;
}