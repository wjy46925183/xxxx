package com.dlg.personal.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.DateUtils;
import com.dlg.data.common.model.ActionButtonsBean;
import com.dlg.data.common.model.ButtonBean;
import com.dlg.data.home.model.DoingTaskOrderDetailBean;
import com.dlg.data.home.model.WorkCardBean;
import com.dlg.personal.R;
import com.dlg.personal.base.BaseFragment;
import com.dlg.personal.home.view.HomeEmployeeCardView;
import com.dlg.personal.home.view.OrderButtnView;
import com.dlg.viewmodel.common.OrderButtnViewModel;
import com.dlg.viewmodel.common.presenter.OrderButtnPresenter;

/**
 * 作者：wangdakuan
 * 主要功能：雇员进行中订单卡片显示fragment
 * 创建时间：2017/7/3 17:47
 */
public class EmployeeOngingCardFragment extends BaseFragment implements OrderButtnPresenter {

    private HomeEmployeeCardView mCardView; //卡片信息数据
    private OrderButtnView mBntOrderView; //订单按钮控件
    private RelativeLayout mRelaReward; //显示报酬价格
    private TextView mTvReward; //报酬
    private TextView mTvPayStatus; //状态

    private WorkCardBean cardBean = new WorkCardBean();
    private DoingTaskOrderDetailBean mOrderDetailBean;
    private OrderButtnViewModel mOrderButtnViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_employee_onging_card, null);
        initView(inflate);
        initData();
        return inflate;
    }

    /**
     * 控件初始化
     *
     * @param inflate
     */
    private void initView(View inflate) {
        mOrderButtnViewModel = new OrderButtnViewModel(mContext, this);
        mCardView = (HomeEmployeeCardView) inflate.findViewById(R.id.card_view);
        mBntOrderView = (OrderButtnView) inflate.findViewById(R.id.bnt_order_view);
        mBntOrderView.setButtonClickListener(new OrderButtnView.onButtonClickListener() {
            @Override
            public void buttonOnClick(ButtonBean buttonBean) {

            }
        });
        mRelaReward = (RelativeLayout) inflate.findViewById(R.id.rela_reward);
        mTvReward = (TextView) inflate.findViewById(R.id.tv_reward);
        mTvPayStatus = (TextView) inflate.findViewById(R.id.tv_payStatus);
    }

    /**
     * 从页面传递过来的数据对象
     *
     * @param orderDetailBean
     */
    public void setOrderDetailBean(DoingTaskOrderDetailBean orderDetailBean) {
        mOrderDetailBean = orderDetailBean;
        setCardBean();
    }

    /**
     * 封装成卡片对象
     */
    private void setCardBean() {
        cardBean.setProvinceName(mOrderDetailBean.getProvinceName());
        cardBean.setCityName(mOrderDetailBean.getAreaName());
        cardBean.setAreaName(mOrderDetailBean.getAreaName());
        cardBean.setWorkAddress(mOrderDetailBean.getWorkAddress());
        cardBean.setPostName(mOrderDetailBean.getPostName());
        cardBean.setPostTypeName(mOrderDetailBean.getPostTypeName());
        cardBean.setUserCreditCount(mOrderDetailBean.getCreditCount());
        cardBean.setUserLogo(mOrderDetailBean.getLogo());
        cardBean.setPrice(mOrderDetailBean.getPrice());
        cardBean.setJobMeterUnitName(mOrderDetailBean.getMeterUnitName());
        cardBean.setIsFarmersInsurance(mOrderDetailBean.getIsFarmersInsurance());

        int startYear = DateUtils.getDate_yyyy(mOrderDetailBean.getStartDate());
        int endYear = DateUtils.getDate_yyyy(mOrderDetailBean.getEndDate());
        int startMonth = DateUtils.getDate_MM(mOrderDetailBean.getStartDate());
        int endMonth = DateUtils.getDate_MM(mOrderDetailBean.getEndDate());
        int startDay = DateUtils.getDate_dd(mOrderDetailBean.getStartDate());
        int endDay = DateUtils.getDate_dd(mOrderDetailBean.getEndDate());
        int startHour = DateUtils.getDate_HH(mOrderDetailBean.getStartDate());
        int endHour = DateUtils.getDate_HH(mOrderDetailBean.getEndDate());
        int startMinute = DateUtils.getDate_mm(mOrderDetailBean.getStartDate());
        int endMinute = DateUtils.getDate_mm(mOrderDetailBean.getEndDate());
        cardBean.setStartYear(startYear);
        cardBean.setStartMonth(startMonth);
        cardBean.setStartDay(startDay);
        cardBean.setStartHour(startHour);
        cardBean.setStartMinute(startMinute);
        cardBean.setEndYear(endYear);
        cardBean.setEndMonth(endMonth);
        cardBean.setEndDay(endDay);
        cardBean.setEndHour(endHour);
        cardBean.setEndMinute(endMinute);
        cardBean.setDemandType(mOrderDetailBean.getDemandType());
    }

    @Override
    public void onOrderButtnList(ActionButtonsBean buttonsBean) {
        mBntOrderView.setButtonData(buttonsBean);
    }

    /**
     * 数据绑定
     */
    private void initData() {
        mOrderButtnViewModel.getOrderButtnData(mOrderDetailBean.getBusinessNumber());
        mCardView.setDataCard(cardBean);
        if (mOrderDetailBean.getStatus().equals("30")) {
            setRemuneration();
            mTvPayStatus.setText("待雇主支付");
        } else if (mOrderDetailBean.getStatus().equals("40")) {
            setRemuneration();
            mTvPayStatus.setText("雇主已支付");
        } else {
            mRelaReward.setVisibility(View.GONE);
        }

    }

    /**
     * 设置报酬显示控件的值
     */
    private void setRemuneration(){
        mRelaReward.setVisibility(View.VISIBLE);
        float totalPrice = TextUtils.isEmpty(mOrderDetailBean.getTotalPrice()) ? 0 : Float.parseFloat(mOrderDetailBean.getTotalPrice());
        float tipAmount = TextUtils.isEmpty(mOrderDetailBean.getTipAmount()) ? 0 : Float.parseFloat(mOrderDetailBean.getTipAmount());
        float price = totalPrice + tipAmount;
        if (tipAmount > 0) {
            mTvReward.setText("报酬：" + price + "元 (含小费" + tipAmount + "元)");
        } else {
            mTvReward.setText("报酬：" + mOrderDetailBean.getTotalPrice() + "元");
        }
    }
}
