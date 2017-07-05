package com.dlg.personal.home.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.utils.DateUtils;
import com.dlg.data.home.model.HomeMapListBean;
import com.dlg.data.home.model.WorkCardBean;
import com.dlg.personal.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者：王进亚
 * 主要功能：
 * 创建时间：2017/6/30 11:50
 */

public class HomeEmployeeCardView extends FrameLayout {
    private LinearLayout mWorkerorderLayoutItem;
    private TextView mWorkerorderTvName;//零工名字
    private TextView mWorkerorderTvChengdu;//分
    private TextView mBaoxian;//保险
    private TextView mWorkerorderTvPosition;//职位
    private TextView mWorkerorderTvType;//类型
    private TextView mWorkerorderTvNum;//接单数量
    private TextView mWorkerorderTvTime;//时间
    private TextView mWorkerorderTvAddress;//地址
    private CircleImageView mWorkerorderIvHead;//头部
    private TextView mWorkerorderIvCreditcount;//信用分
    private LinearLayout mWorkerorderLayoutDetail;//详情
    private TextView mTvDescription;//描述
    private TextView mWorkerorderTvdetail;//详情text

    private HomeMapListBean mHomeMapListBean;

    public HomeEmployeeCardView(@NonNull Context context) {
        super(context);
        initView();
    }

    public HomeEmployeeCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HomeEmployeeCardView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        View convertView =
                LayoutInflater.from(getContext()).inflate(R.layout.view_employee_card, null);
        addView(convertView);
        mWorkerorderLayoutItem = (LinearLayout) findViewById(R.id.workerorder_layout_item);
        mWorkerorderTvName = (TextView) findViewById(R.id.workerorder_tv_name);
        mWorkerorderTvChengdu = (TextView) findViewById(R.id.workerorder_tv_chengdu);
        mBaoxian = (TextView) findViewById(R.id.baoxian);
        mWorkerorderTvPosition = (TextView) findViewById(R.id.workerorder_tv_position);
        mWorkerorderTvType = (TextView) findViewById(R.id.workerorder_tv_type);
        mWorkerorderTvNum = (TextView) findViewById(R.id.workerorder_tv_num);
        mWorkerorderTvTime = (TextView) findViewById(R.id.workerorder_tv_time);
        mWorkerorderTvAddress = (TextView) findViewById(R.id.workerorder_tv_address);
        mWorkerorderIvHead = (CircleImageView) findViewById(R.id.workerorder_iv_head);
        mWorkerorderIvCreditcount = (TextView) findViewById(R.id.workerorder_iv_creditcount);
        mWorkerorderLayoutDetail = (LinearLayout) findViewById(R.id.workerorder_layout_detail);
        mTvDescription = (TextView) findViewById(R.id.tv_description);
        mWorkerorderTvdetail = (TextView) findViewById(R.id.workerorder_tvdetail);
    }

    public void setDataCard(WorkCardBean cardBean,boolean isDoingTask) {
        WorkCardBean workCardBean = cardBean;
        mWorkerorderTvAddress.setText(workCardBean.getProvinceName()
                + workCardBean.getCityName()
                + workCardBean.getAreaName() + workCardBean.getWorkAddress());
        mWorkerorderTvName.setText(workCardBean.getPostName());
        mWorkerorderTvType.setText(workCardBean.getPostTypeName());

        if(workCardBean.getRecruitNumber() != 0){
            mWorkerorderTvNum.setText("人数\n\n\n\n\n\n" + workCardBean.getRecruitNumber());
            mWorkerorderTvNum.setVisibility(View.VISIBLE);
        }else {
            mWorkerorderTvNum.setVisibility(View.GONE);
        }

        mWorkerorderIvCreditcount.setText(workCardBean.getUserCreditCount() + "");
        Glide.with(getContext()).load(workCardBean.getUserLogo()).placeholder(R.mipmap.icon_default_user)
                .into(mWorkerorderIvHead);
        if(isDoingTask){
            if("志愿义工".equals(cardBean.getPostTypeName())){
                mWorkerorderTvPosition.setText(cardBean.getPostTypeName());
            }else {
                mWorkerorderTvPosition.setText(workCardBean.getPrice() +
                        "元/" + workCardBean.getJobMeterUnitName());
            }
        }else {
            mWorkerorderTvPosition.setText(workCardBean.getPrice() +
                    "元/" + workCardBean.getJobMeterUnitName());
        }

        String taskDescription = workCardBean.getWorkAddress();
        mWorkerorderTvdetail.setText(taskDescription);
        if (workCardBean.getIsFarmersInsurance() == 1) {//保险
            mBaoxian.setVisibility(View.VISIBLE);
        } else {
            mBaoxian.setVisibility(View.INVISIBLE);
        }


        int demandType = workCardBean.getDemandType();
        int startYear = workCardBean.getStartYear();
        int startMonth = workCardBean.getStartMonth();
        int startDay = workCardBean.getStartDay();
        int startHour = workCardBean.getStartHour();
        int startMinute = workCardBean.getStartMinute();
        int endYear = workCardBean.getEndYear();
        int endMonth = workCardBean.getEndMonth();
        int endDay = workCardBean.getEndDay();
        int endHour = workCardBean.getEndHour();
        int endMinute = workCardBean.getEndMinute();
        String time = DateUtils.getTimeShow(demandType, startYear, startMonth, startDay, startHour, startMinute,
                endYear, endMonth, endDay, endHour, endMinute);

        mWorkerorderTvTime.setText(time);
        String demandString = "";
        switch (workCardBean.getDemandType()) {
            case 1:
                demandString = "工作日";
                break;
            case 2:
                demandString = "双休日";
                break;
            case 3:
                demandString = "计件";
                break;
        }
        mWorkerorderTvChengdu.setText(demandString);

        if(isDoingTask){
            mWorkerorderTvNum.setVisibility(View.GONE);
            mWorkerorderTvType.setVisibility(View.GONE);
        }
    }

    public void setDataCard(WorkCardBean cardBean) {
        setDataCard(cardBean,false);
    }
}
