package com.dlg.data.home.interactor;

import com.dlg.data.home.model.BossListBean;
import com.dlg.data.home.model.DoingTaskOrderBossBean;
import com.dlg.data.home.model.DoingTaskOrderDetailBean;
import com.dlg.data.home.model.EmployeeListBean;
import com.dlg.data.home.model.HomeMapBossListBean;
import com.dlg.data.home.model.HomeMapListBean;
import com.dlg.data.home.model.KeyHotBean;
import com.dlg.data.home.model.TabBean;
import com.dlg.data.home.model.WorkCardBean;
import com.dlg.data.home.model.WorkTypeBean;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonResponse;
import rx.Observable;

/**
 * 作者：wangdakuan
 * 主要功能：首页模块接口
 * 创建时间：2017/6/23 09:57
 */
public interface HomeInteractor {
    /**
     * 雇员列表接口
     * @param hashMap
     * @return
     */
    Observable<JsonResponse<List<EmployeeListBean>>> getListData(HashMap<String,String> hashMap);

    /**
     * 首页TAB数据接口
     * @param hashMap
     * @return
     */
    Observable<List<TabBean>> getTab(HashMap<String,String> hashMap);

    /**
     * 雇员是否有进行中的订单
     * @param hashMap
     * @return
     */
    Observable<Boolean> isHasDoingTask(HashMap<String,String> hashMap);

    /**
     * 雇主是否有进行中的订单
     * @param hashMap
     * @return
     */
    Observable<Boolean> isHasDoingTaskBoss(HashMap<String,String> hashMap);

    /**
     * 雇员进行中的订单数据
     * @param hashMap
     * @return
     */
    Observable<JsonResponse<List<DoingTaskOrderDetailBean>>> getDoingTaskDetailList(HashMap<String,String> hashMap);

    /**
     * 雇员首页地图数据
     * @param hashMap
     * @return
     */
    Observable<JsonResponse<List<HomeMapListBean>>> getHomeWorkMapList(HashMap<String,String> hashMap);

    /**
     * 雇主首页地图数据
     * @param hashMap
     * @return
     */
    Observable<JsonResponse<List<HomeMapBossListBean>>> getHomeBossMapList(HashMap<String,String> hashMap);

    /**
     * 雇主进行中订单
     * @param hashMap
     * @return
     */
    Observable<JsonResponse<List<DoingTaskOrderBossBean>>> getBossTaskingList(HashMap<String,String> hashMap);

    /**
     * 雇主列表数据
     * @param hashMap
     * @return
     */
    Observable<JsonResponse<List<BossListBean>>> getBossDtaList(HashMap<String,String> hashMap);

    /**
     * 搜索热门关键字
     * @param hashMap
     * @return
     */
    Observable<JsonResponse<List<KeyHotBean>>> getKeyHotDtaList(HashMap<String,String> hashMap);
    /**
     * 零工类型
     * @param hashMap
     * @return
     */
    Observable<JsonResponse<List<WorkTypeBean>>> getWorkTypelist(HashMap<String,String> hashMap);

    /**
     *地图首页 卡片
     */
    Observable<JsonResponse<List<WorkCardBean>>> getWorkCard(HashMap<String,String> hashMap);
}
