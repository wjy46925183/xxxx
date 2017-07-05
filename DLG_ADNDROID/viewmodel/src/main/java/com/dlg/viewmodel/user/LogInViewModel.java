package com.dlg.viewmodel.user;

import android.content.Context;

import com.dlg.data.user.model.UserAttributeInfoBean;
import com.dlg.viewmodel.BasePresenter;
import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.server.UserServer;
import com.dlg.viewmodel.user.presenter.LogInPresenter;

import java.util.HashMap;

import okhttp.rx.JsonResponse;
import rx.Subscriber;

/**
 * 作者：wangdakuan
 * 主要功能：登录ViewModel
 * 创建时间：2017/7/4 16:16
 */
public class LogInViewModel extends BaseViewModel<JsonResponse<UserAttributeInfoBean>> {

    private LogInPresenter mLogInPresenter;
    private BasePresenter mBasePresenter;
    private final UserServer mServer;

    public LogInViewModel(Context context, BasePresenter basePresenter, LogInPresenter presenter) {
        this.mLogInPresenter = presenter;
        mBasePresenter = basePresenter;
        mServer = new UserServer(context);
    }

    /**
     * 登录
     * @param mobile  手机号码,（必填）
     * @param password 密码或验证码, （必填）
     */
    public void logIn(String mobile,String password) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(1)); //用户类型, （必填1个人 2企业）
        map.put("principal", mobile); //手机号码,（必填）
        map.put("credentials", password); // 密码或验证码, （必填）
        map.put("rememberMe", "true");
        mSubscriber = logInSubscriber();
        mServer.userLogIn(mSubscriber, map);
    }

    private Subscriber<JsonResponse<UserAttributeInfoBean>> logInSubscriber() {
        return new RXSubscriber<JsonResponse<UserAttributeInfoBean>, UserAttributeInfoBean>(mBasePresenter) {
            @Override
            public void requestNext(UserAttributeInfoBean t) {
                if (null != mLogInPresenter) {
                    mLogInPresenter.logInUserInfo(t);
                }
            }
        };
    }
}
