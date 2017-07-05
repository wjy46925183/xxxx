package com.dlg.viewmodel.common;

import android.content.Context;

import com.dlg.data.common.model.ActionButtonsBean;
import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.common.presenter.OrderButtnPresenter;
import com.dlg.viewmodel.server.CommonServer;

import java.util.HashMap;

import rx.Subscriber;

/**
 * 作者：wangdakuan
 * 主要功能：
 * 创建时间：2017/7/4 15:48
 */
public class OrderButtnViewModel extends BaseViewModel<ActionButtonsBean> {
    private OrderButtnPresenter orderButtnPresenter;
    private CommonServer mCommonServer;

    public OrderButtnViewModel(Context context, OrderButtnPresenter presenter) {
        this.orderButtnPresenter = presenter;
        mCommonServer = new CommonServer(context);
        mContext = context;
    }

    public void getOrderButtnData(String businessNumber) {
        HashMap<String, String> map = new HashMap<>();
        map.put("businessNumber", businessNumber);
        mSubscriber = getOrderButtnDataSubscriber();
        mCommonServer.getActionButtonsList(mSubscriber, map);
    }

    private Subscriber<ActionButtonsBean> getOrderButtnDataSubscriber() {
        return new RXSubscriber<ActionButtonsBean, ActionButtonsBean>(null) {
            @Override
            public void requestNext(ActionButtonsBean buttonsBean) {
                orderButtnPresenter.onOrderButtnList(buttonsBean);
            }
        };
    }

}