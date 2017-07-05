package com.dlg.data.user;

import com.alibaba.fastjson.JSON;
import com.dlg.data.cache.ObjectCache;
import com.dlg.data.user.interactor.UserInteractor;
import com.dlg.data.user.model.UserAttributeInfoBean;
import com.dlg.data.user.model.UserInfoDataBean;
import com.dlg.data.user.url.UserUrl;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonResponse;
import rx.Observable;

/**
 * 作者：wangdakuan
 * 主要功能：用户缓存接口实现
 * 创建时间：2017/6/23 19:17
 */
public class UserDiaskSource implements UserInteractor{
    private final ObjectCache objectCache;

    public UserDiaskSource(ObjectCache objectCache) {
        this.objectCache = objectCache;
    }

    @Override
    public Observable<JsonResponse<List<UserInfoDataBean>>> queryUserDetail(HashMap<String, String> hashMap) {
        return this.objectCache.getList(UserUrl.QUERY_USER_DETAIL + JSON.toJSONString(hashMap), JsonResponse.class, UserInfoDataBean.class);
    }

    @Override
    public Observable<JsonResponse<UserAttributeInfoBean>> userLogIn(HashMap<String, String> hashMap) {
        return this.objectCache.get(UserUrl.USER_LOGIN + JSON.toJSONString(hashMap), JsonResponse.class, UserAttributeInfoBean.class);
    }
}
