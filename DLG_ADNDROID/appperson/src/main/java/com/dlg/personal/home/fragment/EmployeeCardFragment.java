package com.dlg.personal.home.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dlg.data.home.model.HomeMapListBean;
import com.dlg.data.home.model.WorkCardBean;
import com.dlg.personal.R;
import com.dlg.personal.base.BaseFragment;
import com.dlg.personal.home.view.HomeEmployeeCardView;
import com.dlg.viewmodel.home.WorkCardViewModel;
import com.dlg.viewmodel.home.presenter.WorkCardPresenter;

import java.util.List;

/**
 * 作者：王进亚
 * 主要功能：雇员主页 卡片
 * 创建时间：2017/6/30 13:20
 */

public class EmployeeCardFragment extends BaseFragment implements WorkCardPresenter, View.OnClickListener {

    private HomeEmployeeCardView mCardView;
    private TextView mTvActive;
    private TextView mTvShare;
    private HomeMapListBean mHomeMapListBean;
    private WorkCardViewModel workCardViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_employee_card, null);
        initView(inflate);
        initCard();
        return inflate;
    }

    /**
     * 卡片信息
     */
    private void initCard() {
        if (mHomeMapListBean != null) {
            workCardViewModel.getCardData(mHomeMapListBean.getId());
        }
    }

    /**
     * 初始化view
     *
     * @param inflate
     */
    private void initView(View inflate) {
        workCardViewModel = new WorkCardViewModel(getContext(), this);
        mCardView = (HomeEmployeeCardView) inflate.findViewById(R.id.card_view);
        mTvActive = (TextView) inflate.findViewById(R.id.tv_active);
        mTvShare = (TextView) inflate.findViewById(R.id.tv_share);
        mTvActive.setOnClickListener(this);
        mTvShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_active) { //抢单
        } else if (i == R.id.tv_share) { //分享

        }
    }

    /**
     * 获取每个marker所对应的唯一的订单对象
     *
     * @param bean
     */
    public void setHomeMapListBean(@NonNull HomeMapListBean bean) {
        if (bean == null) {
            throw new IllegalArgumentException("bean is null");
        }
        this.mHomeMapListBean = bean;
    }

    @Override
    public void getCard(List<WorkCardBean> cardBeans) {
        if (null != cardBeans && cardBeans.size() > 0) {
            mCardView.setDataCard(cardBeans.get(0));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != workCardViewModel) {
            workCardViewModel.onDestroyView();
        }
    }
}
