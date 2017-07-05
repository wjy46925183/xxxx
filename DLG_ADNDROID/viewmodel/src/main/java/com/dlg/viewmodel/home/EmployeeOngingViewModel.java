package com.dlg.viewmodel.home;

import android.content.Context;

import com.common.cache.ACache;
import com.dlg.data.home.model.DoingTaskOrderDetailBean;
import com.dlg.viewmodel.BasePresenter;
import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.home.presenter.EmployeeDoingDataPresenter;
import com.dlg.viewmodel.key.AppKey;
import com.dlg.viewmodel.server.HomeServer;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonResponse;
import rx.Subscriber;

/**
 * 作者：王达宽
 * 主要功能：进行中订单
 * 创建时间：2017/6/27 10:47
 */
public class EmployeeOngingViewModel extends BaseViewModel<JsonResponse<List<DoingTaskOrderDetailBean>>> {
    private EmployeeDoingDataPresenter mEmployeeDoingDataPresenter;
    private BasePresenter mBasePresenter;
    private HomeServer mHomeServer;

    public EmployeeOngingViewModel(Context context, BasePresenter basePresenter, EmployeeDoingDataPresenter presenter){
        this.mEmployeeDoingDataPresenter = presenter;
        this.mBasePresenter = basePresenter;
        mHomeServer = new HomeServer(context);
        mContext = context;
    }

    public void getHasDoingTaskData(){
        HashMap<String,String> map = new HashMap<>();
        map.put("userId", ACache.get(mContext).getAsString(AppKey.CacheKey.MY_USER_ID));
        mSubscriber = getHasDoingTaskDataSubscriber();
        mHomeServer.getDoingTaskDetailList(mSubscriber,map);
    }

    private Subscriber<JsonResponse<List<DoingTaskOrderDetailBean>>> getHasDoingTaskDataSubscriber(){
        return new RXSubscriber<JsonResponse<List<DoingTaskOrderDetailBean>>, List<DoingTaskOrderDetailBean>>(mBasePresenter) {
            @Override
            public void requestNext(List<DoingTaskOrderDetailBean> beanList) {
                if(null != mEmployeeDoingDataPresenter){
                    mEmployeeDoingDataPresenter.onDoingTaskList(beanList);
                }
            }
        };
    }

}
