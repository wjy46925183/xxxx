package com.dlg.viewmodel.home;

import android.content.Context;

import com.dlg.data.home.model.WorkCardBean;
import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.home.presenter.WorkCardPresenter;
import com.dlg.viewmodel.server.HomeServer;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonResponse;
import rx.Subscriber;

/**
 * 作者：王进亚
 * 主要功能：
 * 创建时间：2017/6/30 16:21
 */

public class WorkCardViewModel extends BaseViewModel<JsonResponse<List<WorkCardBean>>> {
    private WorkCardPresenter mWorkCardPresenter;
    private final HomeServer mHomeServer;

    public WorkCardViewModel(Context context, WorkCardPresenter presenter){
        mHomeServer = new HomeServer(context);
        this.mWorkCardPresenter = presenter;
    }
    public void getCardData(String taskId){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("taskId",taskId);
        mSubscriber = getWorkSub();
        mHomeServer.getWorkCard(mSubscriber,hashMap);
    }

    private Subscriber<JsonResponse<List<WorkCardBean>>> getWorkSub(){
        return new RXSubscriber<JsonResponse<List<WorkCardBean>>, List<WorkCardBean>>(null) {
            @Override
            public void requestNext(List<WorkCardBean> workCardBeen) {
               mWorkCardPresenter.getCard(workCardBeen);
            }
        };
    }
}
