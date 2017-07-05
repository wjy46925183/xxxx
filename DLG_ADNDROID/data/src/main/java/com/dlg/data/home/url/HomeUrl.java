package com.dlg.data.home.url;

import com.dlg.data.UrlNet;

/**
 * 作者：wangdakuan
 * 主要功能：首页模块请求接口连接
 * 创建时间：2017/6/23 10:58
 */
public class HomeUrl {
    /**
     * 首页TAB标签
     */
    public final static String TAB_HOME = UrlNet.getName() + "/api/dictionaryRest/queryGroup";
    /**
     * 雇员列表数据
     */
    public static final String EMPPLOYEELIST_URL = UrlNet.getName() + "/api/jobTaskRest/getJobTaskMapPage";

    /**
     * 雇员是否有进行中的订单
     */
    public static final String WORKER_IS_TASKING = UrlNet.getName() + "/api/jobTaskRest/isOrderByEmployeeId";
    /**
     * 雇主是否有进行中的订单
     */
    public static final String BOSS_IS_TASKING = UrlNet.getName() + "/api/jobTaskRest/isOrderByEmployerId";

    /**
     * 雇员进行中的订单数据
     */
    public static final String DOING_TASK_DETAIL_LIST = UrlNet.getName() + "/api/orderRest/findListByEmployeeIdOrBusinessNumber";
    /**
     * 首页雇员地图数据
     */
    public static final String HOME_WORK_MAP_LIST = UrlNet.getName() + "/api/jobTaskRest/getJobTaskMapList";
    /**
     * 首页雇主地图数据
     */
    public static final String HOME_BOSS_MAP_LIST = UrlNet.getName() + "/api/jobTaskRest/getJobTaskMapListByEmployeeId";
    /**
     * 雇主进行中的订单
     */
    public static final String BOSS_IS_TASKING_LIST = UrlNet.getName() + "/api/jobTaskRest/getJobTaskOfOrderList";
    /**
     * 雇主列表数据
     */
    public static final String BOSS_DATA_LIST = UrlNet.getName() + "/api/jobTaskRest/getJobTaskMapPageByEmployeeId";
    /**
     * 关键字和热门搜索
     */
    public static final String KEY_HOT_SELECT = UrlNet.getName() + "/api/dictionaryRest/queryGroup";
    /**
     * 数据字典列表
     */
    public static final String WORK_TYPE = UrlNet.getName() + "/api/dictionaryRest/queryGroup";
    /**
     * 雇员主页卡片
     */
    public static final String EMPLOYEE_CARD = UrlNet.getName()+"/api/jobTaskRest/getJobTaskById";
}
