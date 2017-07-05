package com.dlg.viewmodel.home;

import android.content.Context;

import com.common.cache.ACache;
import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.home.presenter.EmployeeIsDoingPresenter;
import com.dlg.viewmodel.key.AppKey;
import com.dlg.viewmodel.server.HomeServer;

import java.util.HashMap;

import rx.Subscriber;

/**
 * 作者：wangdakuan
 * 主要功能：是否有进行中的订单
 * 创建时间：2017/7/4 09:14
 */
public class EmployeeIsDoingViewModel extends BaseViewModel<Boolean> {
    private EmployeeIsDoingPresenter employeeIsDoingPresenter;
    private HomeServer mHomeServer;

    public EmployeeIsDoingViewModel(Context context, EmployeeIsDoingPresenter presenter) {
        this.employeeIsDoingPresenter = presenter;
        mHomeServer = new HomeServer(context);
        mContext = context;
    }

    public void isHasDoingTask() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", ACache.get(mContext).getAsString(AppKey.CacheKey.MY_USER_ID));
        mSubscriber = getMapSubscriber();
        mHomeServer.isHasDoingTask(mSubscriber, map);
    }

    private Subscriber<Boolean> getMapSubscriber() {
        return new RXSubscriber<Boolean, Boolean>(null) {
            @Override
            public void requestNext(Boolean isHas) {
                employeeIsDoingPresenter.isHasDoingTask(isHas);
            }
        };
    }

}