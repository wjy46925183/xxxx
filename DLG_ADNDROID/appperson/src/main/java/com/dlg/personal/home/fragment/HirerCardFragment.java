package com.dlg.personal.home.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dlg.data.user.model.UserInfoDataBean;
import com.dlg.personal.R;
import com.dlg.personal.base.BaseFragment;
import com.dlg.personal.home.view.HirerContentCardView;
import com.dlg.viewmodel.user.UserInfoViewModel;
import com.dlg.viewmodel.user.presenter.UserInfoPresenter;


/**
 * 作者：王达宽
 * 主要功能：雇主主页 卡片
 * 创建时间：2017/6/30 13:20
 */

public class HirerCardFragment extends BaseFragment implements View.OnClickListener,UserInfoPresenter {


    private HirerContentCardView mCardView; //卡片内容布局
    private TextView mTvShare; //分享按钮
    private TextView mTvHireTa; //雇TA按钮

    private UserInfoViewModel mInfoViewModel; //用户请求ViewModel
    private String userId; //雇员ID

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_hirer_card, null);
        initView(inflate);
        initData();
        return inflate;
    }

    /**
     * 给整个view赋值
     */
    private void initData() {
        mInfoViewModel = new UserInfoViewModel(getActivity(),this);
        mInfoViewModel.queryUserDetail(userId);
    }

    /**
     * 初始化view
     * @param inflate
     */
    private void initView(View inflate) {
        mCardView = (HirerContentCardView) inflate.findViewById(R.id.card_view);
        mTvShare = (TextView) inflate.findViewById(R.id.tv_share);
        mTvHireTa = (TextView) inflate.findViewById(R.id.tv_hire_ta);
        mTvShare.setOnClickListener(this);
        mTvHireTa.setOnClickListener(this);
        // TODO: 2017/7/3  分享按钮暂时隐藏
        mTvShare.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_share) { //分享按钮
        } else if (i == R.id.tv_hire_ta) { //雇TA按钮

        }
    }

    /**
     * 获取每个marker所对应的唯一的雇员id
     * @param userId
     */
    public void setHomeMapDataId(@NonNull String userId) {
        this.userId = userId;
    }

    /**
     * 接口数据返回
     * @param userInfoDataBean
     */
    @Override
    public void userInfoData(UserInfoDataBean userInfoDataBean) {
        mCardView.setUserInfo(userInfoDataBean);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mInfoViewModel.onDestroyView();
    }
}
