/*
 * Copyright (C), 2013-2017, 工人网
 * FileName: Base64.java
 * Author:   jgYang
 * Date:     2017年2月22日 下午9:54:11
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package gongren.com.dlg.utils;

import java.io.UnsupportedEncodingException;

import gongren.com.dlg.alibaba.cloudapi.Base64Encoder;

/**
 * 功能描述:
 * <p/>
 * Author:   jgYang
 * Date:     2017年2月22日 下午9:54:11
 */
public class Base64Utils {

    // 加密
    public static String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new Base64Encoder().encode(b);
        }
        return s;
    }

}
