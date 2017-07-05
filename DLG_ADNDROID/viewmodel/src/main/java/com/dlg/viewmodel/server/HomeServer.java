package com.dlg.viewmodel.server;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.dlg.data.home.factory.HomeFactory;
import com.dlg.data.home.interactor.HomeInteractor;
import com.dlg.data.home.url.HomeUrl;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：wangdakuan
 * 主要功能：首页模块请求服务
 * 创建时间：2017/6/1 14:31
 */
public class HomeServer {

    HomeFactory homeFactory;

    public HomeServer(Context appContext) {
        this(new HomeFactory(appContext));
    }

    public HomeServer(HomeFactory homeFactory) {
        this.homeFactory = homeFactory;
    }

    /**
     * 雇员列表数据
     *
     * @param subscriber
     * @param hashMap
     */
    public void getList(Subscriber subscriber, HashMap<String, String> hashMap) {
        HomeInteractor dataInteractor = homeFactory.createHomeData(HomeUrl.EMPPLOYEELIST_URL + JSON.toJSONString(hashMap), true);
        dataInteractor.getListData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 首页TAB标签数据
     *
     * @param subscriber
     * @param hashMap
     */
    public void getTabText(Subscriber subscriber, HashMap<String, String> hashMap) {
        HomeInteractor dataInteractor = homeFactory.createHomeData(HomeUrl.TAB_HOME + JSON.toJSONString(hashMap));
        dataInteractor.getTab(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 雇员是否有进行中的订单
     *
     * @param subscriber
     * @param hashMap
     */
    public void isHasDoingTask(Subscriber subscriber, HashMap<String, String> hashMap) {
        HomeInteractor dataInteractor = homeFactory.createHomeData(HomeUrl.WORKER_IS_TASKING + JSON.toJSONString(hashMap));
        dataInteractor.isHasDoingTask(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 雇员进行中的订单
     *
     * @param subscriber
     * @param hashMap
     */
    public void getDoingTaskDetailList(Subscriber subscriber, HashMap<String, String> hashMap) {
        HomeInteractor dataInteractor = homeFactory.createHomeData(HomeUrl.DOING_TASK_DETAIL_LIST + JSON.toJSONString(hashMap), true);
        dataInteractor.getDoingTaskDetailList(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 雇员首页地图数据
     *
     * @param subscriber
     * @param hashMap
     */
    public void getHomeWorkMapList(Subscriber subscriber, HashMap<String, String> hashMap) {
        HomeInteractor dataInteractor = homeFactory.createHomeData(HomeUrl.HOME_WORK_MAP_LIST + JSON.toJSONString(hashMap), true);
        dataInteractor.getHomeWorkMapList(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 雇主首页地图数据
     *
     * @param subscriber
     * @param hashMap
     */
    public void getHomeBossMapList(Subscriber subscriber, HashMap<String, String> hashMap) {
        HomeInteractor dataInteractor = homeFactory.createHomeData(HomeUrl.HOME_BOSS_MAP_LIST + JSON.toJSONString(hashMap), true);
        dataInteractor.getHomeBossMapList(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 雇员是否有进行中的订单
     *
     * @param subscriber
     * @param hashMap
     */
    public void isHasDoingTaskBoss(Subscriber subscriber, HashMap<String, String> hashMap) {
        HomeInteractor dataInteractor = homeFactory.createHomeData(HomeUrl.BOSS_IS_TASKING + JSON.toJSONString(hashMap));
        dataInteractor.isHasDoingTaskBoss(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 雇主进行中订单
     * @param subscriber
     * @param hashMap
     */
    public void getBossTaskingList(Subscriber subscriber, HashMap<String, String> hashMap) {
        HomeInteractor dataInteractor = homeFactory.createHomeData(HomeUrl.BOSS_IS_TASKING_LIST + JSON.toJSONString(hashMap));
        dataInteractor.getBossTaskingList(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 雇主列表数据
     * @param subscriber
     * @param hashMap
     */
    public void getBossDtaList(Subscriber subscriber, HashMap<String, String> hashMap) {
        HomeInteractor dataInteractor = homeFactory.createHomeData(HomeUrl.BOSS_DATA_LIST + JSON.toJSONString(hashMap),true);
        dataInteractor.getBossDtaList(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    /**
     * 关键字和热门搜索
     * @param subscriber
     * @param hashMap
     * */
    public void getKeyHotData(Subscriber subscriber, HashMap<String, String> hashMap) {
        HomeInteractor dataInteractor = homeFactory.createHomeData(HomeUrl.KEY_HOT_SELECT + JSON.toJSONString(hashMap));
        dataInteractor.getKeyHotDtaList(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    /**
     * 零工类型
     * @param subscriber
     * @param hashMap
     * */
    public void getWorkTypeData(Subscriber subscriber, HashMap<String, String> hashMap) {
        HomeInteractor dataInteractor = homeFactory.createHomeData(HomeUrl.WORK_TYPE + JSON.toJSONString(hashMap));
        dataInteractor.getWorkTypelist(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 零工类型
     * @param subscriber
     * @param hashMap
     * */
    public void getWorkCard(Subscriber subscriber, HashMap<String, String> hashMap) {
        HomeInteractor dataInteractor = homeFactory.createHomeData(HomeUrl.EMPLOYEE_CARD,true);
        dataInteractor.getWorkCard(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
