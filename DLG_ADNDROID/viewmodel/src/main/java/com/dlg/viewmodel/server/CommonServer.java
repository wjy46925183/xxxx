package com.dlg.viewmodel.server;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.dlg.data.common.factory.CommonFactory;
import com.dlg.data.common.interactor.CommonInteractor;
import com.dlg.data.common.url.CommonUrl;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：wangdakuan
 * 主要功能：公共接口类
 * 创建时间：2017/6/23 14:52
 */
public class CommonServer {


    CommonFactory commonFactory;

    public CommonServer(Context appContext) {
        this(new CommonFactory(appContext));
    }

    public CommonServer(CommonFactory commonFactory) {
        this.commonFactory = commonFactory;
    }

    /**
     * 订单操作按钮数据
     * @param subscriber
     * @param hashMap
     */
    public void getActionButtonsList(Subscriber subscriber, HashMap<String, String> hashMap) {
        CommonInteractor dataInteractor = commonFactory.createData(CommonUrl.GET_ACTION_BUTTONS + JSON.toJSONString(hashMap));
        dataInteractor.getActionButtons(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取分享内容数据
     * @param subscriber
     * @param hashMap
     */
    public void getShareData(Subscriber subscriber, HashMap<String, String> hashMap) {
        CommonInteractor dataInteractor = commonFactory.createData(CommonUrl.GET_ACTION_BUTTONS + JSON.toJSONString(hashMap));
        dataInteractor.getShareData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
