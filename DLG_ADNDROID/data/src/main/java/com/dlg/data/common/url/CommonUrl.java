package com.dlg.data.common.url;

import com.dlg.data.UrlNet;

/**
 * 作者：wangdakuan
 * 主要功能：公用模块接口连接
 * 创建时间：2017/6/23 14:55
 */
public class CommonUrl {
    /**
     * 订单操作按钮接口连接
     */
    public static final String GET_ACTION_BUTTONS = UrlNet.getName() + "/api/orderFlowConfigRest/findByBusinessNumber";

    /**
     * 分享内容获取
     */
    public static final String SHARE_DATA = UrlNet.getName() + "/api/jobTaskRest/findJobTaskShareToWeiXin";
}
