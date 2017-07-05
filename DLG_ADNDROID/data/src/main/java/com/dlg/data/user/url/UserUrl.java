package com.dlg.data.user.url;

import com.dlg.data.UrlNet;

/**
 * 作者：wangdakuan
 * 主要功能：用户模块
 * 创建时间：2017/6/23 19:14
 */
public class UserUrl {
    /**
     * 查询用户信息（根据ID获取）
     */
    public static final String QUERY_USER_DETAIL = UrlNet.getName() + "/api/userRest/queryUserDetail";
    /**
     * 用户登录接口
     */
    public static final String USER_LOGIN = UrlNet.getName() + "/login";
}
