package com.dlg.viewmodel.home;

import android.content.Context;

import com.dlg.data.home.model.TabBean;
import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.home.presenter.TabPresenter;
import com.dlg.viewmodel.server.HomeServer;

import java.util.HashMap;
import java.util.List;

import rx.Subscriber;

/**
 * Created by wangjinya on 2017/6/19.
 */

public class TabViewModel extends BaseViewModel{
    private TabPresenter mTabPresenter;
    private final HomeServer mServer;
    private Subscriber<List<TabBean>> mSubscriber;

    public TabViewModel(Context context , TabPresenter tabPresenter){
        this.mTabPresenter = tabPresenter;
        mServer = new HomeServer(context);
    }
    public void getTab(){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("groupCode", "job.type");
        mSubscriber = getTabtext();
        mServer.getTabText(getTabtext(),hashMap);
    }

    private Subscriber<List<TabBean>> getTabtext(){
        return new RXSubscriber<List<TabBean>, List<TabBean>>(null) {
            @Override
            public void requestNext(List<TabBean> tabBeen) {
                mTabPresenter.tabJson(tabBeen);
            }
        };
    }
    @Override
    public void onDestroyView() {
        if(null != mSubscriber){
            mSubscriber.unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        if(null != mSubscriber){
            mSubscriber.unsubscribe();
        }
    }

    @Override
    public void onResume() {

    }
}
