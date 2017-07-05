package com.dlg.data.home;

import com.alibaba.fastjson.JSON;
import com.dlg.data.cache.ObjectCache;
import com.dlg.data.home.interactor.HomeInteractor;
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
import com.dlg.data.home.url.HomeUrl;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonResponse;
import rx.Observable;

/**
 * 作者：wangdakuan
 * 主要功能：缓存实现接口
 * 创建时间：2017/6/23 10:34
 */
public class HomeDiaskSource implements HomeInteractor {

    private final ObjectCache objectCache;

    public HomeDiaskSource(ObjectCache objectCache) {
        this.objectCache = objectCache;
    }


    @Override
    public Observable<JsonResponse<List<EmployeeListBean>>> getListData(HashMap<String, String> hashMap) {
        return this.objectCache.getList(HomeUrl.EMPPLOYEELIST_URL + JSON.toJSONString(hashMap), JsonResponse.class, EmployeeListBean.class);
    }

    @Override
    public Observable<List<TabBean>> getTab(HashMap<String, String> hashMap) {
        return this.objectCache.getList(HomeUrl.TAB_HOME + JSON.toJSONString(hashMap), TabBean.class);
    }

    @Override
    public Observable<Boolean> isHasDoingTask(HashMap<String, String> hashMap) {
        return null;
    }

    @Override
    public Observable<JsonResponse<List<DoingTaskOrderDetailBean>>> getDoingTaskDetailList(HashMap<String, String> hashMap) {
        return this.objectCache.getList(HomeUrl.DOING_TASK_DETAIL_LIST + JSON.toJSONString(hashMap), JsonResponse.class, DoingTaskOrderDetailBean.class);
    }

    @Override
    public Observable<JsonResponse<List<HomeMapListBean>>> getHomeWorkMapList(HashMap<String, String> hashMap) {
        return this.objectCache.getList(HomeUrl.HOME_WORK_MAP_LIST + JSON.toJSONString(hashMap), JsonResponse.class, HomeMapListBean.class);
    }

    @Override
    public Observable<JsonResponse<List<HomeMapBossListBean>>> getHomeBossMapList(HashMap<String, String> hashMap) {
        return this.objectCache.getList(HomeUrl.HOME_BOSS_MAP_LIST + JSON.toJSONString(hashMap), JsonResponse.class, HomeMapBossListBean.class);
    }

    @Override
    public Observable<Boolean> isHasDoingTaskBoss(HashMap<String, String> hashMap) {
        return null;
    }

    @Override
    public Observable<JsonResponse<List<DoingTaskOrderBossBean>>> getBossTaskingList(HashMap<String, String> hashMap) {
        return this.objectCache.getList(HomeUrl.BOSS_IS_TASKING_LIST + JSON.toJSONString(hashMap), JsonResponse.class, DoingTaskOrderBossBean.class);
    }

    @Override
    public Observable<JsonResponse<List<BossListBean>>> getBossDtaList(HashMap<String, String> hashMap) {
        return this.objectCache.getList(HomeUrl.BOSS_DATA_LIST + JSON.toJSONString(hashMap), JsonResponse.class, BossListBean.class);
    }

    @Override
    public Observable<JsonResponse<List<KeyHotBean>>> getKeyHotDtaList(HashMap<String, String> hashMap) {
        return this.objectCache.getList(HomeUrl.KEY_HOT_SELECT + JSON.toJSONString(hashMap), JsonResponse.class, KeyHotBean.class);
    }

    @Override
    public Observable<JsonResponse<List<WorkTypeBean>>> getWorkTypelist(HashMap<String, String> hashMap) {
        return this.objectCache.getList(HomeUrl.WORK_TYPE + JSON.toJSONString(hashMap), JsonResponse.class, WorkTypeBean.class);
    }

    @Override
    public Observable<JsonResponse<List<WorkCardBean>>> getWorkCard(HashMap<String, String> hashMap) {
        return null;
    }
}
