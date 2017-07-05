package com.dlg.viewmodel.home;

import android.content.Context;

import com.dlg.data.home.model.WorkTypeBean;
import com.dlg.viewmodel.BasePresenter;
import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.home.presenter.WorkTypePresenter;
import com.dlg.viewmodel.server.HomeServer;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonResponse;
import rx.Subscriber;

/**
 * 作者：小明
 * 主要功能：选择工作类型
 * 创建时间：2017/6/29 0029 20:06
 */
public class WorkTypeViewModel extends BaseViewModel<JsonResponse<List<WorkTypeBean>>> {
    private BasePresenter basePresenter;
    private HomeServer mServer;
    public  WorkTypePresenter workTypePresenter;

    public WorkTypeViewModel(Context context, BasePresenter basePresenter, WorkTypePresenter workTypePresenter){
        this.workTypePresenter=workTypePresenter;
        this.basePresenter=basePresenter;
        mServer = new HomeServer(context);
    }

    public void getWorType(String type){
        HashMap <String ,String> map=new HashMap<>();
        map.put("groupCode",type);
        mSubscriber=getWorType();
        mServer.getWorkTypeData(mSubscriber,map);
    }
    private Subscriber<JsonResponse<List<WorkTypeBean>>> getWorType(){
        return new RXSubscriber<JsonResponse<List<WorkTypeBean>>, List<WorkTypeBean>>(basePresenter) {
            @Override
            public void requestNext(List<WorkTypeBean> workTypeBean) {
                workTypePresenter.getWorkType(workTypeBean);
            }
        };
    }


}
