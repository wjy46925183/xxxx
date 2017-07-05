package com.dlg.personal.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dlg.personal.R;
import com.dlg.personal.base.BaseFragment;
import com.dlg.personal.home.view.HirerContentCardView;
import com.dlg.personal.home.view.OrderButtnView;

/**
 * 作者：王进亚
 * 主要功能：雇主工作台卡片
 * 创建时间：2017/7/4 11:45
 */

public class HirerDeskCardFragment extends BaseFragment {

    private LinearLayout mBossmapLayoutOrder;
    private HirerContentCardView mHirerDeskCard;
    private OrderButtnView mOrderBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.item_hire_doing_card, null);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        mBossmapLayoutOrder = (LinearLayout) inflate.findViewById(R.id.bossmap_layout_order);
        mHirerDeskCard = (HirerContentCardView) inflate.findViewById(R.id.hirer_desk_card);
        mOrderBtn = (OrderButtnView) inflate.findViewById(R.id.order_btn);

        //TODO 按钮初始化 根据接口返回数据来
    }
}
