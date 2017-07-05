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
import com.http.okgo.OkGo;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonConvert;
import okhttp.rx.JsonResponse;
import okhttp.rx.RxAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 作者：wangdakuan
 * 主要功能：实现接口
 * 创建时间：2017/6/23 10:03
 */
public class HomeSource implements HomeInteractor {
    private final ObjectCache objectCache;

    /**
     */
    public HomeSource(ObjectCache objectCache) {
        this.objectCache = objectCache;
    }

    private Action1<Object> saveToCacheAction(final String key) {
        return new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o != null) {
                    HomeSource.this.objectCache.put(o, key);
                }
            }
        };
    }

    @Override
    public Observable<JsonResponse<List<EmployeeListBean>>> getListData(HashMap<String, String> hashMap) {
        return OkGo.post(HomeUrl.EMPPLOYEELIST_URL)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<EmployeeListBean>>>() {
                }, RxAdapter.<JsonResponse<List<EmployeeListBean>>>create())
                .doOnNext(saveToCacheAction(HomeUrl.EMPPLOYEELIST_URL + JSON.toJSONString(hashMap)))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<TabBean>> getTab(HashMap<String, String> hashMap) {
        return OkGo.post(HomeUrl.TAB_HOME)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<TabBean>>>() {
                }, RxAdapter.<JsonResponse<List<TabBean>>>create())
                .map(new Func1<JsonResponse<List<TabBean>>, List<TabBean>>() {
                    @Override
                    public List<TabBean> call(JsonResponse<List<TabBean>> responses) {
                        return responses.getData();
                    }
                })
                .doOnNext(saveToCacheAction(HomeUrl.TAB_HOME + JSON.toJSONString(hashMap)))
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public Observable<Boolean> isHasDoingTask(HashMap<String, String> hashMap) {
        return OkGo.post(HomeUrl.WORKER_IS_TASKING)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<Boolean>>>() {
                }, RxAdapter.<JsonResponse<List<Boolean>>>create())
                .map(new Func1<JsonResponse<List<Boolean>>, Boolean>() {
                    @Override
                    public Boolean call(JsonResponse<List<Boolean>> responses) {
                        if (null != responses && null != responses.getData()
                                && responses.getData().size() > 0) {
                            return responses.getData().get(0);
                        }
                        return false;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<JsonResponse<List<DoingTaskOrderDetailBean>>> getDoingTaskDetailList(HashMap<String, String> hashMap) {
        return OkGo.post(HomeUrl.DOING_TASK_DETAIL_LIST)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<DoingTaskOrderDetailBean>>>() {
                }, RxAdapter.<JsonResponse<List<DoingTaskOrderDetailBean>>>create())
//                .doOnNext(saveToCacheAction(HomeUrl.DOING_TASK_DETAIL_LIST+ JSON.toJSONString(hashMap)))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<JsonResponse<List<HomeMapListBean>>> getHomeWorkMapList(HashMap<String, String> hashMap) {
        return OkGo.post(HomeUrl.HOME_WORK_MAP_LIST)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<HomeMapListBean>>>() {
                }, RxAdapter.<JsonResponse<List<HomeMapListBean>>>create())
                .doOnNext(saveToCacheAction(HomeUrl.HOME_WORK_MAP_LIST + JSON.toJSONString(hashMap)))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<JsonResponse<List<HomeMapBossListBean>>> getHomeBossMapList(HashMap<String, String> hashMap) {
        return OkGo.post(HomeUrl.HOME_BOSS_MAP_LIST)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<HomeMapBossListBean>>>() {
                }, RxAdapter.<JsonResponse<List<HomeMapBossListBean>>>create())
                .doOnNext(saveToCacheAction(HomeUrl.HOME_BOSS_MAP_LIST + JSON.toJSONString(hashMap)))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> isHasDoingTaskBoss(HashMap<String, String> hashMap) {
        return OkGo.post(HomeUrl.BOSS_IS_TASKING)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<Boolean>>>() {
                }, RxAdapter.<JsonResponse<List<Boolean>>>create())
                .map(new Func1<JsonResponse<List<Boolean>>, Boolean>() {
                    @Override
                    public Boolean call(JsonResponse<List<Boolean>> responses) {
                        if (null != responses && null != responses.getData()
                                && responses.getData().size() > 0) {
                            return responses.getData().get(0);
                        }
                        return false;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<JsonResponse<List<DoingTaskOrderBossBean>>> getBossTaskingList(HashMap<String, String> hashMap) {
        return OkGo.post(HomeUrl.BOSS_IS_TASKING_LIST)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<DoingTaskOrderBossBean>>>() {
                }, RxAdapter.<JsonResponse<List<DoingTaskOrderBossBean>>>create())
//                .doOnNext(saveToCacheAction(HomeUrl.BOSS_IS_TASKING_LIST+ JSON.toJSONString(hashMap)))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<JsonResponse<List<BossListBean>>> getBossDtaList(HashMap<String, String> hashMap) {
        return OkGo.post(HomeUrl.BOSS_DATA_LIST)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<BossListBean>>>() {
                }, RxAdapter.<JsonResponse<List<BossListBean>>>create())
                .doOnNext(saveToCacheAction(HomeUrl.BOSS_DATA_LIST + JSON.toJSONString(hashMap)))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<JsonResponse<List<KeyHotBean>>> getKeyHotDtaList(HashMap<String, String> hashMap) {
        return OkGo.post(HomeUrl.KEY_HOT_SELECT)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<KeyHotBean>>>() {
                }, RxAdapter.<JsonResponse<List<KeyHotBean>>>create())
                .doOnNext(saveToCacheAction(HomeUrl.KEY_HOT_SELECT + JSON.toJSONString(hashMap)))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<JsonResponse<List<WorkTypeBean>>> getWorkTypelist(HashMap<String, String> hashMap) {
        return null;
    }

    /**
     * 雇员首页 卡片
     * @param hashMap
     * @return
     */
    @Override
    public Observable<JsonResponse<List<WorkCardBean>>> getWorkCard(HashMap<String, String> hashMap) {
        return OkGo.post(HomeUrl.EMPLOYEE_CARD)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<WorkCardBean>>>() {
                }, RxAdapter.<JsonResponse<List<WorkCardBean>>>create())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
