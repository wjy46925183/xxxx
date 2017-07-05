package com.dlg.data.user.interactor;

import com.dlg.data.user.model.UserAttributeInfoBean;
import com.dlg.data.user.model.UserInfoDataBean;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonResponse;
import rx.Observable;

/**
 * 作者：wangdakuan
 * 主要功能：用户接口
 * 创建时间：2017/6/23 19:15
 */
public interface UserInteractor {
    /**
     * 根据ID 获取用户信息
     * @param hashMap
     * @return
     */
    Observable<JsonResponse<List<UserInfoDataBean>>> queryUserDetail(HashMap<String,String> hashMap);

    /**
     * 用户登录
     * @param hashMap
     * @return
     */
    Observable<JsonResponse<UserAttributeInfoBean>> userLogIn(HashMap<String,String> hashMap);
}
