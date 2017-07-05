package com.dlg.personal.home.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.common.utils.StringUtils;
import com.dlg.personal.R;
import com.dlg.personal.base.BaseFragment;
import com.dlg.personal.base.DlgMapView;
import com.dlg.personal.home.activity.HomeActivity;
import com.dlg.personal.home.adapter.EmployeeCardAdapter;
import com.dlg.personal.home.adapter.HirerDeskAdapter;
import com.dlg.personal.home.view.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者：王进亚
 * 主要功能：雇员工作台
 * 创建时间：2017/7/4 09:18
 */

public class HierDeskFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private DlgMapView mMapView;
    private RelativeLayout mBossLayoutOrdertitle;
    private LinearLayout mLlToolbar;
    private CircleImageView mBossorderMine;
    private RelativeLayout mBossoderLayout;
    private TextView mBossoderTitie;
    private TextView mTvPrice;
    private ImageView mIvDownUp;
    private TextView mTvLots;
    private RecyclerView mRecyHireDesk;
    private ViewPager mViewPagerDesk;
    private List<String> datas = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();
    private boolean isVisibility = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_hier_desk, null);
        initView(view);

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.TRANSPARENT);

        TextView textView = new TextView(mContext);
        textView.setHeight(StringUtils.getStatusHeight(mActivity));
        linearLayout.addView(textView);
        linearLayout.addView(view);
        return linearLayout;
    }

    /**
     * 初始化View
     *
     * @param view
     */
    private void initView(View view) {
        mBossLayoutOrdertitle = (RelativeLayout) view.findViewById(R.id.boss_layout_ordertitle);
        mLlToolbar = (LinearLayout) view.findViewById(R.id.ll_toolbar);
        mBossorderMine = (CircleImageView) view.findViewById(R.id.bossorder_mine);
        mBossoderLayout = (RelativeLayout) view.findViewById(R.id.bossoder_layout);
        mBossoderTitie = (TextView) view.findViewById(R.id.bossoder_titie);
        mTvPrice = (TextView) view.findViewById(R.id.tv_price);
        mIvDownUp = (ImageView) view.findViewById(R.id.iv_down_up);
        mTvLots = (TextView) view.findViewById(R.id.tv_lots);
        mRecyHireDesk = (RecyclerView) view.findViewById(R.id.recy_hire_desk);
        mViewPagerDesk = (ViewPager) view.findViewById(R.id.home_hire_desk_pager);
        mBossoderLayout.setOnClickListener(this);
        mBossorderMine.setOnClickListener(this);
        if (mActivity instanceof HomeActivity) {
            mMapView = ((HomeActivity) mActivity).getMapView();
            mMapView.setMapClickListener(new AMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if(isVisibility){
                        mRecyHireDesk.setVisibility(View.GONE);
                        isVisibility = false;
                        mIvDownUp.setImageResource(R.mipmap.down);
                    }else{
                        ((HomeActivity) mActivity).checkHireDesk(false);
                    }
                }
            });
        }
        mLlToolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//触摸标题 防止地图移动
            }
        });
        mBossoderTitie.setText("有人接单(3)");
        mTvPrice.setText("服务员 120元/天");

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyHireDesk.setLayoutManager(manager);

        for (int i = 0; i < 4; i++) {
            datas.add("服务员 120元/天"+i);
        }
        HirerDeskAdapter hirerDeskAdapter =
                new HirerDeskAdapter(mContext, mRecyHireDesk, datas, R.layout.item_hire_order);
        mRecyHireDesk.setAdapter(hirerDeskAdapter);

        //viewpager中的内容
        for (int i = 0; i < 5; i++) {
            mFragments.add(new HirerDeskCardFragment());
        }
        mViewPagerDesk.setVisibility(View.VISIBLE);
        mViewPagerDesk.setAdapter(new EmployeeCardAdapter(getChildFragmentManager(),mFragments));
        mViewPagerDesk.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.bossoder_layout){
            if(isVisibility){
                mRecyHireDesk.setVisibility(View.GONE);
                isVisibility = false;
                mIvDownUp.setImageResource(R.mipmap.down);
            }else{
                mRecyHireDesk.setVisibility(View.VISIBLE);
                mIvDownUp.setImageResource(R.mipmap.up);
                isVisibility = true;
            }
        }else if(id == R.id.bossorder_mine){
            if(mActivity instanceof HomeActivity){
                ((HomeActivity) mActivity).openDrawer();//打开抽屉
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ToastUtils.getMessageToast(mContext,"进入下一个订单","已同意").show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
