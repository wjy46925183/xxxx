package com.dlg.viewmodel.home;

import android.content.Context;

import com.common.cache.ACache;
import com.dlg.data.home.model.BossListBean;
import com.dlg.viewmodel.BasePresenter;
import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.home.presenter.HirerListPresenter;
import com.dlg.viewmodel.key.AppKey;
import com.dlg.viewmodel.server.HomeServer;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonResponse;
import rx.Subscriber;

/**
 * 作者：wangdakuan
 * 主要功能：雇主列表数据
 * 创建时间：2017/6/27 09:05
 */
public class HirerListViewModel extends BaseViewModel<JsonResponse<List<BossListBean>>> {
    private BasePresenter basePresenter;
    private HirerListPresenter hirerListPresenter;
    private final HomeServer mServer;

    public HirerListViewModel(Context context, BasePresenter presenter, HirerListPresenter hirerListPresenter) {
        this.basePresenter = presenter;
        this.hirerListPresenter = hirerListPresenter;
        mServer = new HomeServer(context);
        mContext = context;
    }

    public void getList(String xCoordinate,String yCoordinate,String postType,
                        String pageIndex) {
        HashMap<String, String> map = new HashMap<>();

        map.put("xCoordinate", xCoordinate);
        map.put("yCoordinate",yCoordinate);
        map.put("postType", postType);
        map.put("demandType", "");
        map.put("pageSize", "10");
        map.put("userId", ACache.get(mContext).getAsString(AppKey.CacheKey.MY_USER_ID)); //用户ID
        map.put("pageIndex", pageIndex + "");
        mSubscriber = getListtext();
        mServer.getBossDtaList(mSubscriber, map);
    }

    private Subscriber<JsonResponse<List<BossListBean>>> getListtext() {
        return new RXSubscriber<JsonResponse<List<BossListBean>>, List<BossListBean>>(basePresenter) {
            @Override
            public void requestNext(List<BossListBean> t) {
                hirerListPresenter.getListData(t);
            }
        };
    }
}
