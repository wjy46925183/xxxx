package com.dlg.viewmodel.server;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.dlg.data.user.factory.UserFactory;
import com.dlg.data.user.interactor.UserInteractor;
import com.dlg.data.user.url.UserUrl;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：wangdakuan
 * 主要功能：用户接口服务类
 * 创建时间：2017/6/23 19:19
 */
public class UserServer {

    UserFactory userFactory;

    public UserServer(Context appContext) {
        this(new UserFactory(appContext));
    }

    public UserServer(UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    /**
     * 根据ID获取用户信息
     * @param subscriber
     * @param hashMap
     */
    public void queryUserDetail(Subscriber subscriber, HashMap<String, String> hashMap) {
        UserInteractor dataInteractor = userFactory.createUserData(UserUrl.QUERY_USER_DETAIL + JSON.toJSONString(hashMap), true);
        dataInteractor.queryUserDetail(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 用户登录
     * @param subscriber
     * @param hashMap
     */
    public void userLogIn(Subscriber subscriber, HashMap<String, String> hashMap) {
        UserInteractor dataInteractor = userFactory.createUserData(UserUrl.USER_LOGIN + JSON.toJSONString(hashMap), true);
        dataInteractor.userLogIn(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


}
