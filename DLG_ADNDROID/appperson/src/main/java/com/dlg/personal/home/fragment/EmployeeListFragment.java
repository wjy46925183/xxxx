package com.dlg.personal.home.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.common.view.loadmore.BaseLoadMoreHeaderAdapter;
import com.dlg.data.home.model.EmployeeListBean;
import com.dlg.data.model.MyMapLocation;
import com.dlg.personal.R;
import com.dlg.personal.app.MApp;
import com.dlg.personal.base.BaseFragment;
import com.dlg.personal.home.adapter.EmployeeListAdapter;
import com.dlg.viewmodel.home.EmployeeListViewModel;
import com.dlg.viewmodel.home.presenter.EmployeeListPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Wangjinya on 2017/6/20.
 * 雇员主页列表
 */

public class EmployeeListFragment extends BaseFragment implements EmployeeListPresenter, SwipeRefreshLayout.OnRefreshListener, BaseLoadMoreHeaderAdapter.OnLoadMoreListener {
    private RecyclerView mRecy_employeelist; //雇员列表显示布局
    private ImageView mIv_entermap; //地图首页切换按钮
    private SwipeRefreshLayout swipe_refresh; //刷新控件
    private String postType; //选择的tab标签
    private List<EmployeeListBean> beans = new ArrayList<>(); //数据集
    private List<EmployeeListBean> employeeJobListCopy = new ArrayList<>(); //数据集排序后的数据
    private EmployeeListAdapter mEmployeeListAdapter; //适配器
    private EmployeeListViewModel mViewModel; //列表数据ViewModel
    private int pageIndex = 0;
    private String minId = "" + 0;
    MyMapLocation mapLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_home_map_list, null);
        bindViews(inflate);
        bindData();
        return inflate;
    }

    /**
     * 绑定数据
     */
    private void bindData() {
        if (mViewModel == null) {
            mViewModel = new EmployeeListViewModel(getActivity(),this, this);
        } else {
            mViewModel.onDestroy();//取消上次的请求
        }
        mapLocation = MApp.getInstance().getMapLocation();
        if (null != mapLocation) {
            mViewModel.getList(mapLocation.getLongitude() + "", mapLocation.getLatitude() + "", postType, pageIndex + "", minId);
        }
    }

    /**
     * 绑定view
     *
     * @param inflate
     */
    private void bindViews(View inflate) {
        mRecy_employeelist = (RecyclerView) inflate.findViewById(R.id.recy_list);
        mIv_entermap = (ImageView) inflate.findViewById(R.id.iv_entermap);
        swipe_refresh = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);

        mIv_entermap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragment() instanceof EmployeeHomeFragment) {
                    ((EmployeeHomeFragment) getParentFragment()).removeListFragment();
                }
            }
        });
        mRecy_employeelist.setLayoutManager(new LinearLayoutManager(mContext));
        mEmployeeListAdapter = new EmployeeListAdapter(mContext,
                mRecy_employeelist, beans, R.layout.item_worker_list);
        mRecy_employeelist.setAdapter(mEmployeeListAdapter);

        swipe_refresh.setColorSchemeResources(R.color.orange_yellow);
        swipe_refresh.setOnRefreshListener(this);
        mEmployeeListAdapter.setOnLoadMoreListener(this);
        autoRefresh();
    }
    /**
     * 自动刷新
     */
    private void autoRefresh(){
        if(swipe_refresh != null){
            swipe_refresh.post(new Runnable() {
                @Override
                public void run() {
                    swipe_refresh.setRefreshing(true);
                }
            });
        }
    }
    /**
     * tab切换的时候
     * @param postType
     */
    public void changeTab(String postType) {
        this.postType = postType;
        autoRefresh();
        if (mContext != null) {//为空的时候 还没有添加该fragment
            pageIndex = 0;
            minId = "0";
            bindData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.onDestroy();
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        minId = "0";
        bindData();
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        if(null != employeeJobListCopy && employeeJobListCopy.size()>0){
            minId = employeeJobListCopy.get(0).getId();
        }
        bindData();
    }

    @Override
    public void requestError(String msg) {
        super.requestError(msg);
        if (swipe_refresh.isRefreshing()) {
            swipe_refresh.setRefreshing(false);
        }
    }

    @Override
    public void logInError() {
        super.logInError();
        if (swipe_refresh.isRefreshing()) {
            swipe_refresh.setRefreshing(false);
        }
    }

    @Override
    public void getListData(List<EmployeeListBean> dataBeans) {
        if (pageIndex == 0) {//下拉刷新的时候 清空列表数据
            beans.clear();
        }
        beans.addAll(dataBeans);
        mEmployeeListAdapter.notifyDataSetChanged();
        if (swipe_refresh.isRefreshing()) {
            swipe_refresh.setRefreshing(false);
        }
        employeeJobListCopy.addAll(dataBeans);
        Collections.sort(employeeJobListCopy, new Comparator<EmployeeListBean>() {
            @Override
            public int compare(EmployeeListBean lhs, EmployeeListBean rhs) {
                if(!TextUtils.isEmpty(lhs.getId()) && !TextUtils.isEmpty(rhs.getId())){
                    double a = Double.parseDouble(lhs.getId());
                    double b = Double.parseDouble(rhs.getId());
                    if(a<b){
                        return -1;
                    }else {
                        return 1;
                    }
                }
                return 0;
            }
        });

        mEmployeeListAdapter.completeLoadMore();
    }
}
