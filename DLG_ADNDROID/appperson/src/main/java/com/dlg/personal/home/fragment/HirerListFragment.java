package com.dlg.personal.home.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.common.view.loadmore.BaseLoadMoreHeaderAdapter;
import com.dlg.data.home.model.BossListBean;
import com.dlg.data.model.MyMapLocation;
import com.dlg.personal.R;
import com.dlg.personal.app.MApp;
import com.dlg.personal.base.BaseFragment;
import com.dlg.personal.home.adapter.HirerListAdapter;
import com.dlg.viewmodel.home.HirerListViewModel;
import com.dlg.viewmodel.home.presenter.HirerListPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.dlg.personal.R.id.swipe_refresh;

/**
 * 作者：wangdakuan
 * 主要功能：雇主数据列表页
 * 创建时间：2017/6/27 09:05
 */
public class HirerListFragment extends BaseFragment implements HirerListPresenter, SwipeRefreshLayout.OnRefreshListener, BaseLoadMoreHeaderAdapter.OnLoadMoreListener {
    private RecyclerView mRecyList; //雇主列表显示布局
    private ImageView mIvEntermap; //地图首页切换按钮
    private SwipeRefreshLayout swipeRefresh; //刷新控件
    private String postType; //选择的tab标签
    private List<BossListBean> beans = new ArrayList<>(); //数据集
    private HirerListAdapter mHirerListAdapter; //适配器
    private HirerListViewModel mViewModel; //列表数据ViewModel
    private int pageIndex = 0;

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
            mViewModel = new HirerListViewModel(getActivity(), this, this);
        } else {
            mViewModel.onDestroy();//取消上次的请求
        }
        MyMapLocation mapLocation = MApp.getInstance().getMapLocation();
        if (null != mapLocation) {
            mViewModel.getList(mapLocation.getLongitude() + "", mapLocation.getLatitude() + "", postType, pageIndex + "");
        }
    }

    /**
     * 绑定view
     *
     * @param inflate
     */
    private void bindViews(View inflate) {
        mRecyList = (RecyclerView) inflate.findViewById(R.id.recy_list);
        mIvEntermap = (ImageView) inflate.findViewById(R.id.iv_entermap);
        swipeRefresh = (SwipeRefreshLayout) inflate.findViewById(swipe_refresh);

        mIvEntermap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragment() instanceof HirerHomeFragment) {
                    ((HirerHomeFragment) getParentFragment()).removeListFragment();
                }
            }
        });
        mRecyList.setLayoutManager(new LinearLayoutManager(mContext));
        mHirerListAdapter = new HirerListAdapter(mContext,
                mRecyList, beans, R.layout.item_hirer_list);
        mRecyList.setAdapter(mHirerListAdapter);

        swipeRefresh.setColorSchemeResources(R.color.orange_yellow);
        swipeRefresh.setOnRefreshListener(this);
        mHirerListAdapter.setOnLoadMoreListener(this);
        autoRefresh();
    }

    /**
     * 自动刷新
     */
    private void autoRefresh(){
        if(swipeRefresh != null){
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(true);
                }
            });
        }
    }
    /**
     * tab切换的时候
     *
     * @param postType
     */
    public void changeTab(String postType) {
        this.postType = postType;
        autoRefresh();
        if (mContext != null) {//为空的时候 还没有添加该fragment
            pageIndex = 0;
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
        bindData();
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        bindData();
    }

    @Override
    public void requestError(String msg) {
        super.requestError(msg);
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void logInError() {
        super.logInError();
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void getListData(List<BossListBean> _beans) {
        if (pageIndex == 0) {//下拉刷新的时候 清空列表数据
            beans.clear();
        }
        beans.addAll(_beans);
        mHirerListAdapter.notifyDataSetChanged();
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }

        mHirerListAdapter.completeLoadMore();
    }
}
