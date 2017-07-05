package com.dlg.viewmodel.home;

import android.content.Context;

import com.common.cache.ACache;
import com.dlg.data.home.model.HomeMapBossListBean;
import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.home.presenter.HirerMapPresenter;
import com.dlg.viewmodel.key.AppKey;
import com.dlg.viewmodel.server.HomeServer;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonResponse;
import rx.Subscriber;

/**
 * 作者：王进亚
 * 主要功能：雇主地图数据
 * 创建时间：2017/6/27 10:47
 */
public class HirerMapsViewModel extends BaseViewModel<JsonResponse<List<HomeMapBossListBean>>> {
    private HirerMapPresenter mhirerMapPresenter;
    private HomeServer mHomeServer;

    public HirerMapsViewModel(Context context, HirerMapPresenter presenter){
        this.mhirerMapPresenter = presenter;
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
        mHomeServer.getHomeBossMapList(mSubscriber,map);
    }

    private Subscriber<JsonResponse<List<HomeMapBossListBean>>> getMapSubscriber(){
        return new RXSubscriber<JsonResponse<List<HomeMapBossListBean>>, List<HomeMapBossListBean>>(null) {
            @Override
            public void requestNext(List<HomeMapBossListBean> beanList) {
                mhirerMapPresenter.toMapList(beanList);
            }
        };
    }

}
