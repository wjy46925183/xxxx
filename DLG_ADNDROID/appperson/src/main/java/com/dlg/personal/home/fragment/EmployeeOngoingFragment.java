package com.dlg.personal.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dlg.data.home.model.DoingTaskOrderDetailBean;
import com.dlg.personal.R;
import com.dlg.personal.base.BaseFragment;
import com.dlg.personal.home.adapter.EmployeeCardAdapter;
import com.dlg.viewmodel.home.EmployeeOngingViewModel;
import com.dlg.viewmodel.home.presenter.EmployeeDoingDataPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：wangdakuan
 * 主要功能：雇员进行中页面显示Fragment
 * 创建时间：2017/7/3 13:37
 */
public class EmployeeOngoingFragment extends BaseFragment implements EmployeeDoingDataPresenter{

    private RelativeLayout mToolbar; //头部空间
    private ImageView mToolbarBack; //返回按钮
    private TextView mToolbarTitle; //标题
    private TextView mDividerLine; //分割线
    private ViewPager mEmployeeOngoingPager; //进行中订单数据显示viewpager

    private EmployeeOngingViewModel mEmployeeOngingViewModel; //进行中ViewModel
    private EmployeeCardAdapter mCardAdapter;

    private FragmentManager mManager;

    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_employee_ongoing, null);
        initView(inflate);
        initData();
        return inflate;
    }

    /**
     * 数据初始化
     */
    private void initData() {
        mEmployeeOngingViewModel = new EmployeeOngingViewModel(mContext,this,this);
        //初始化ViewPager
        mManager = getActivity().getSupportFragmentManager();
        mCardAdapter = new EmployeeCardAdapter(mManager, mFragmentList);
        mEmployeeOngoingPager.setAdapter(mCardAdapter);
    }

    /**
     * 控件初始化
     * @param inflate
     */
    private void initView(View inflate) {
        mToolbar = (RelativeLayout) inflate.findViewById(R.id.toolbar);
        mToolbarBack = (ImageView) inflate.findViewById(R.id.toolbar_back);
        mToolbarTitle = (TextView) inflate.findViewById(R.id.toolbar_title);
        mDividerLine = (TextView) inflate.findViewById(R.id.divider_line);
        mEmployeeOngoingPager = (ViewPager) inflate.findViewById(R.id.employee_ongoing_pager);
        mEmployeeOngoingPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 获取的进行中数据列表
     * @param taskOrderDetailList
     */
    @Override
    public void onDoingTaskList(List<DoingTaskOrderDetailBean> taskOrderDetailList) {
        mFragmentList.clear();
        if(null != taskOrderDetailList && taskOrderDetailList.size()>0){
            for (DoingTaskOrderDetailBean orderDetailBean : taskOrderDetailList) {
                EmployeeOngingCardFragment ongingCardFragment = new EmployeeOngingCardFragment();
                ongingCardFragment.setOrderDetailBean(orderDetailBean);
                mFragmentList.add(ongingCardFragment);
            }
        }
        mCardAdapter.notifyDataSetChanged();
    }
}
