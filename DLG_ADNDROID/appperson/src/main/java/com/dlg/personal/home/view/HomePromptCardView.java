package com.dlg.personal.home.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlg.personal.R;

/**
 * 作者：wangdakuan
 * 主要功能：雇主、雇员首页底部框布局
 * 创建时间：2017/6/28 14:21
 */
public class HomePromptCardView extends LinearLayout {

    private TextView mTvAddress; //地址显示数据
    private TextView mBtnReleaseJobs; //提示与布局

    public HomePromptCardView(Context context) {
        super(context);
        init();
    }

    public HomePromptCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomePromptCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HomePromptCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.view_home_prompt_card, this);
        initView(contentView);
    }

    private void initView(View contentView) {
        mTvAddress = (TextView) contentView.findViewById(R.id.tv_address);
        mBtnReleaseJobs = (TextView) contentView.findViewById(R.id.btn_release_jobs);
    }

    public void setTvAddress(String address) {
        if(null != mTvAddress){
            if(TextUtils.isEmpty(address)){
                mTvAddress.setText("正在获取我的位置...");
            }else {
                mTvAddress.setText(address);
            }

        }
    }

    /**
     * 显示地图地址控件
     * @return
     */
    public TextView getTvAddress() {
        return mTvAddress;
    }

    public void setmTvAddress(TextView mTvAddress) {
        this.mTvAddress = mTvAddress;
    }

    /**
     * 设置监听
     * @param addressClick
     */
    public void setTvAddressClick(OnClickListener addressClick) {
        if(null != mTvAddress && null != addressClick){
            mTvAddress.setOnClickListener(addressClick);
        }
    }
    /**
     * 设置监听
     * @param jobsClick
     */
    public void setBtnReleaseJobsClick(OnClickListener jobsClick) {
        if(null != mBtnReleaseJobs && null != jobsClick){
            mBtnReleaseJobs.setOnClickListener(jobsClick);
        }
    }
}
