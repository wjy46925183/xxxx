package com.dlg.data.user;

import com.alibaba.fastjson.JSON;
import com.dlg.data.cache.ObjectCache;
import com.dlg.data.user.interactor.UserInteractor;
import com.dlg.data.user.model.UserAttributeInfoBean;
import com.dlg.data.user.model.UserInfoDataBean;
import com.dlg.data.user.url.UserUrl;
import com.http.okgo.OkGo;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonConvert;
import okhttp.rx.JsonResponse;
import okhttp.rx.RxAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 作者：wangdakuan
 * 主要功能：用户接口实现
 * 创建时间：2017/6/23 19:17
 */
public class UserSource implements UserInteractor{

    private final ObjectCache objectCache;

    public UserSource(ObjectCache objectCache) {
        this.objectCache = objectCache;
    }


    private Action1<Object> saveToCacheAction(final String key) {
        return new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o != null) {
                    UserSource.this.objectCache.put(o, key);
                }
            }
        };
    }

    @Override
    public Observable<JsonResponse<List<UserInfoDataBean>>> queryUserDetail(HashMap<String, String> hashMap) {
        return OkGo.post(UserUrl.QUERY_USER_DETAIL)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<UserInfoDataBean>>>() {
                }, RxAdapter.<JsonResponse<List<UserInfoDataBean>>>create())
                .doOnNext(saveToCacheAction(UserUrl.QUERY_USER_DETAIL + JSON.toJSONString(hashMap)))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<JsonResponse<UserAttributeInfoBean>> userLogIn(HashMap<String, String> hashMap) {
        return OkGo.post(UserUrl.USER_LOGIN)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<UserAttributeInfoBean>>() {
                }, RxAdapter.<JsonResponse<UserAttributeInfoBean>>create())
                .doOnNext(saveToCacheAction(UserUrl.USER_LOGIN + JSON.toJSONString(hashMap)))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
