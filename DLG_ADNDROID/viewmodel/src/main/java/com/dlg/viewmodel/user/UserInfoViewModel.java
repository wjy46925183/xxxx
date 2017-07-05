package com.dlg.viewmodel.user;

import android.content.Context;

import com.dlg.data.user.model.UserInfoDataBean;
import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.server.UserServer;
import com.dlg.viewmodel.user.presenter.UserInfoPresenter;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonResponse;
import rx.Subscriber;

/**
 * 作者：wangdakuan
 * 主要功能：用户基本信息(根据ID获取）
 * 创建时间：2017/6/30 14:47
 */
public class UserInfoViewModel extends BaseViewModel<JsonResponse<List<UserInfoDataBean>>> {

    private UserInfoPresenter userInfoPresenter;
    private final UserServer mServer;

    public UserInfoViewModel(Context context, UserInfoPresenter userInfoPresenter) {
        this.userInfoPresenter = userInfoPresenter;
        mServer = new UserServer(context);
    }

    /**
     * 查询用户信息（根据ID）
     *
     * @param userId
     */
    public void queryUserDetail(String userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        mSubscriber = queryUserDetail();
        mServer.queryUserDetail(mSubscriber, map);
    }

    private Subscriber<JsonResponse<List<UserInfoDataBean>>> queryUserDetail() {
        return new RXSubscriber<JsonResponse<List<UserInfoDataBean>>, List<UserInfoDataBean>>(null) {
            @Override
            public void requestNext(List<UserInfoDataBean> t) {
                if (null != userInfoPresenter && null != t && t.size() > 0) {
                    userInfoPresenter.userInfoData(t.get(0));
                }
            }
        };
    }
}
