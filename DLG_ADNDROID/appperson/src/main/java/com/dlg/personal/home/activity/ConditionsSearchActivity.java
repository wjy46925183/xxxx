package com.dlg.personal.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlg.personal.R;
import com.dlg.personal.base.BaseActivity;

/**
 * 作者：小明
 * 主要功能：
 * 创建时间：2017/6/29 0029 16:34
 */
public class ConditionsSearchActivity extends BaseActivity implements View.OnClickListener {
    private TabLayout tablayout;
    private EditText workName;
    private LinearLayout layoutWorkerType;
    private TextView workType;
    private LinearLayout llDownAcceptPay;
    private EditText lowPay;
    private LinearLayout layoutZhengshu;
    private TextView unitDay;
    private TextView unitTime;
    private TextView unitNum;
    private Button btnComit;
    private RecyclerView recycler;
    private String[] tabArray = {"工作日", "双休日", "计件"};
    private int demandType = 0;//工种类型

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_condition_search);
        initView();
    }

    private void initView() {
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        workName = (EditText) findViewById(R.id.work_name);
        layoutWorkerType = (LinearLayout) findViewById(R.id.layout_worker_type);
        workType = (TextView) findViewById(R.id.work_type);
        llDownAcceptPay = (LinearLayout) findViewById(R.id.ll_down_accept_pay);
        lowPay = (EditText) findViewById(R.id.low_pay);
        layoutZhengshu = (LinearLayout) findViewById(R.id.layout_zhengshu);
        unitDay = (TextView) findViewById(R.id.unit_day);
        unitTime = (TextView) findViewById(R.id.unit_time);
        unitNum = (TextView) findViewById(R.id.unit_num);
        btnComit = (Button) findViewById(R.id.btn_comit);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        layoutWorkerType.setOnClickListener(this);
        initTab();
    }

    private void initTab() {
        for (int i = 0; i < tabArray.length; i++) {
            TabLayout.Tab tab = tablayout.newTab().setText(tabArray[i]);
            tab.setTag(i);
            tablayout.addTab(tab);
        }
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ((int) (tab.getTag()) == 2) {
                    unitDay.setVisibility(View.GONE);
                    unitTime.setVisibility(View.GONE);
                    unitNum.setVisibility(View.VISIBLE);
                } else {
                    unitDay.setVisibility(View.VISIBLE);
                    unitNum.setVisibility(View.VISIBLE);
                    unitTime.setVisibility(View.GONE);
                }
                demandType = (int) (tab.getTag());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.layout_worker_type) {
            startActivityForResult(new Intent(),RESULT_OK);
        }
        if (i == R.id.btn_comit){
            go2workPage();
        }

    }

    private void go2workPage() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            if (resultCode==RESULT_OK){
                workType.setText(data.getStringExtra("worktype"));
            }

        }

    }
}
