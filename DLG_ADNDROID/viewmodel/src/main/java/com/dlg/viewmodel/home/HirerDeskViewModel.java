package com.dlg.viewmodel.home;

import android.content.Context;

import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.home.presenter.HirerDeskPresenter;
import com.dlg.viewmodel.server.HomeServer;

import java.util.HashMap;

import rx.Subscriber;

/**
 * 作者：王进亚
 * 主要功能：雇主正在进行中订单
 * 创建时间：2017/7/5 09:34
 */

public class HirerDeskViewModel extends BaseViewModel{
    private HirerDeskPresenter mDeskPresenter;
    private final HomeServer mHomeServer;

    public HirerDeskViewModel(Context context, HirerDeskPresenter mDeskPresenter){
        this.mDeskPresenter = mDeskPresenter;
        mHomeServer = new HomeServer(context);
    }

    public void getHirerData(){
        HashMap<String,String> hashMap = new HashMap<>();

        mSubscriber = getSub();
        mHomeServer.getBossTaskingList(mSubscriber,hashMap);
    }

    private Subscriber<String> getSub(){
        return new RXSubscriber<String,String>(null) {
            @Override
            public void requestNext(String s) {
                mDeskPresenter.getHirerDesk(s);
            }
        };
    }
}
