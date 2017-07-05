package com.dlg.viewmodel.home;

import android.content.Context;

import com.dlg.data.home.model.EmployeeListBean;
import com.dlg.viewmodel.BasePresenter;
import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.home.presenter.EmployeeListPresenter;
import com.dlg.viewmodel.server.HomeServer;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonResponse;
import rx.Subscriber;

/**
 * Created by Wangjinya on 2017/6/20.
 */

public class EmployeeListViewModel extends BaseViewModel<JsonResponse<List<EmployeeListBean>>> {
    private BasePresenter basePresenter;
    private EmployeeListPresenter employeeListPresenter;
    private final HomeServer mServer;

    public EmployeeListViewModel(Context context,BasePresenter presenter, EmployeeListPresenter employeeListPresenter) {
        this.basePresenter = presenter;
        this.employeeListPresenter = employeeListPresenter;
        mServer = new HomeServer(context);
    }

    public void getList(String xCoordinate,String yCoordinate,String postType,
                        String pageIndex,String minId) {
        HashMap<String, String> map = new HashMap<>();

        map.put("xCoordinate", xCoordinate);
        map.put("yCoordinate", yCoordinate);
        map.put("postType", postType);
        map.put("demandType", "");
        map.put("postName", "");
        map.put("pageSize", "10");
        map.put("userId", "0");
        map.put("pageIndex", pageIndex);
        map.put("minId", minId);
        mSubscriber = getListtext();
        mServer.getList(mSubscriber, map);
    }

    private Subscriber<JsonResponse<List<EmployeeListBean>>> getListtext() {
        return new RXSubscriber<JsonResponse<List<EmployeeListBean>>, List<EmployeeListBean>>(basePresenter) {
            @Override
            public void requestNext(List<EmployeeListBean> t) {
                employeeListPresenter.getListData(t);
            }
        };
    }
}
