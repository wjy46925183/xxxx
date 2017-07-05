package com.dlg.viewmodel.home;

import android.content.Context;

import com.common.cache.ACache;
import com.dlg.data.home.model.HomeMapListBean;
import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.home.presenter.EmployeeMapPresenter;
import com.dlg.viewmodel.key.AppKey;
import com.dlg.viewmodel.server.HomeServer;

import java.util.HashMap;
import java.util.List;

import rx.Subscriber;

/**
 * 作者：王进亚
 * 主要功能：
 * 创建时间：2017/6/27 10:47
 */

public class EmployeeMapsViewModel extends BaseViewModel<List<HomeMapListBean>> {
    private EmployeeMapPresenter mEmployeeMapPresenter;
    private HomeServer mHomeServer;

    public EmployeeMapsViewModel(Context context,EmployeeMapPresenter presenter){
        this.mEmployeeMapPresenter = presenter;
        mHomeServer = new HomeServer(context);
        mContext = context;
    }

    public void getMapDatas(String xCoordinate,String yCoordinate,
                            String postType,String demandType){
        HashMap<String,String> map = new HashMap<>();
        map.put("userId", ACache.get(mContext).getAsString(AppKey.CacheKey.MY_USER_ID));
        map.put("xCoordinate",xCoordinate);
        map.put("yCoordinate",yCoordinate);
        map.put("postType",postType);
        map.put("demandType",demandType);
        mSubscriber = getMapSubscriber();
        mHomeServer.getHomeWorkMapList(mSubscriber,map);
    }

    private Subscriber<List<HomeMapListBean>> getMapSubscriber(){
        return new RXSubscriber<List<HomeMapListBean>, List<HomeMapListBean>>(null) {
            @Override
            public void requestNext(List<HomeMapListBean> beanList) {
                mEmployeeMapPresenter.toMapList(beanList);
            }
        };
    }

}
