package com.dlg.viewmodel.home;

import android.content.Context;

import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.home.presenter.HirerDoingPresenter;
import com.dlg.viewmodel.server.HomeServer;

import java.util.HashMap;

import rx.Subscriber;

/**
 * 作者：王进亚
 * 主要功能：
 * 创建时间：2017/7/4 17:09
 */

public class HirerDoingViewModel extends BaseViewModel {
    private HirerDoingPresenter mHirerDoingPresenter;
    private final HomeServer mHomeServer;

    public HirerDoingViewModel(Context context, HirerDoingPresenter presenter){
        this.mHirerDoingPresenter = presenter;
        mHomeServer = new HomeServer(context);
    }

    public void isHasDoingOrder(String userId){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("userId",userId);
        hashMap.put("format","json");
        mSubscriber = getDoingOrder();
        mHomeServer.isHasDoingTaskBoss(mSubscriber,hashMap);
    }

    private Subscriber<Boolean> getDoingOrder(){
        return new RXSubscriber<Boolean, Boolean>(null) {
            @Override
            public void requestNext(Boolean aBoolean) {
                mHirerDoingPresenter.doingOrder(aBoolean);
            }
        };
    }
}
