package com.dlg.personal.home.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlg.personal.R;

/**
 * 作者：wangdakuan
 * 主要功能：首页标签数据空间
 * 创建时间：2017/6/28 13:30
 */
public class HomeTypeView extends LinearLayout implements View.OnClickListener {


    private TextView mHomeWorkday; //工作日
    private TextView mHomeWeek;  //双休日
    private TextView mHomeProject; //计价

    public HomeTypeView(Context context) {
        super(context);
        init();
    }

    public HomeTypeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomeTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HomeTypeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.view_home_type, this);
        initView(contentView);
    }

    private void initView(View contentView) {
        mHomeWorkday = (TextView) contentView.findViewById(R.id.home_workday);
        mHomeWeek = (TextView) contentView.findViewById(R.id.home_week);
        mHomeProject = (TextView) contentView.findViewById(R.id.home_project);
        mHomeWorkday.setOnClickListener(this);
        mHomeWeek.setOnClickListener(this);
        mHomeProject.setOnClickListener(this);
    }
    private String demandType;
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.home_workday) {
            //工作日
            if (!mHomeWorkday.isSelected()) {
                demandType = "1";
                mHomeWorkday.setSelected(true);
                mHomeWeek.setSelected(false);
                mHomeProject.setSelected(false);
            } else {
                demandType = "";
                mHomeWorkday.setSelected(false);
            }

        } else if (i == R.id.home_week) {
            //双休日
            if (!mHomeWeek.isSelected()) {
                demandType = "2";
                mHomeWorkday.setSelected(false);
                mHomeWeek.setSelected(true);
                mHomeProject.setSelected(false);
            } else {
                demandType = "";
                mHomeWeek.setSelected(false);
            }
        } else if (i == R.id.home_project) {
            //计件
            if (!mHomeProject.isSelected()) {
                demandType = "3";
                mHomeWorkday.setSelected(false);
                mHomeWeek.setSelected(false);
                mHomeProject.setSelected(true);
            } else {
                demandType = "";
                mHomeProject.setSelected(false);
            }
        }
        if(null != homeTypeClick){
            homeTypeClick.onClick(demandType);
        }
    }

    private onHomeTypeClick homeTypeClick;

    public void setHomeTypeClick(onHomeTypeClick homeTypeClick) {
        this.homeTypeClick = homeTypeClick;
    }

   public interface onHomeTypeClick{
        void onClick(String demandType);
    }
}
