package com.dlg.personal.home.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.common.overlay.AMapUtil;
import com.dlg.data.home.model.HomeMapBossListBean;
import com.dlg.data.home.model.TabBean;
import com.dlg.personal.R;
import com.dlg.personal.base.BaseFragment;
import com.dlg.personal.base.DlgMapView;
import com.dlg.personal.home.activity.HomeActivity;
import com.dlg.personal.home.adapter.EmployeeCardAdapter;
import com.dlg.personal.home.view.HomePromptCardView;
import com.dlg.personal.home.view.HomeTypeView;
import com.dlg.viewmodel.home.HirerDoingViewModel;
import com.dlg.viewmodel.home.HirerMapsViewModel;
import com.dlg.viewmodel.home.TabViewModel;
import com.dlg.viewmodel.home.presenter.HirerDoingPresenter;
import com.dlg.viewmodel.home.presenter.HirerMapPresenter;
import com.dlg.viewmodel.home.presenter.TabPresenter;
import com.dlg.viewmodel.key.AppKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinya on 2017/6/19.
 * 雇员主页
 */

public class HirerHomeFragment extends BaseFragment implements View.OnClickListener, TabLayout.OnTabSelectedListener, TabPresenter, HirerMapPresenter, DlgMapView.OnCameraChangeFinish, ViewPager.OnPageChangeListener, HirerDoingPresenter {
    private TabLayout mTabLayout; //底部Tab标签
    private ImageView mButtonList; //雇主列表数据切换按钮
    private HomeTypeView mHomeType; //标签布局
    private ImageView ivKeFu, ivMyLocation; //客服与定位按钮
    private HomePromptCardView mPromptCardView; //底部卡片布局
    private LinearLayout mLinearLayoutTask; //进行中总布局
    private ImageView mImageViewTask; //进行中头像布局
    private ViewPager mHomeHirerPager; //雇主看雇员的卡片显示布局
    private LinearLayout llBtn;
    private boolean cardVisible;

    private EmployeeCardAdapter mCardAdapter; //卡片适配器
    private List<Fragment> mCardFragments = new ArrayList<>(); //卡片显示数据

    private TabViewModel mTabViewModel; //头部标签ViewModel
    private HirerMapsViewModel mMapsViewModel; //地图数据
    private HomeActivity mHomeActivity;
    private DlgMapView mMapView; //地图控件
    private Handler mHandler = new Handler();
    private Marker mPinMarker;//大头针Marker
    private String mDataCode;
    private LatLng lastLatLng;
    private String mDemandType; //任务类型 空为全部、1=工作日、2=双休日、3=计件

    private HirerListFragment mHirerListFragment; //雇主列表页
    private FragmentManager mManager;

    private List<TabBean> mTabBeans;  //Tab标签数据
    private boolean isEnterList;//是否进入了主页列表
    private boolean isClick; //是否是点击地图上的marker 如果是为true 否则为false
    private String mDataListCode; //数据列表点击后的Tab标签字段
    private HirerDoingViewModel mHirerDoingViewModel;
    private boolean isFromDesk;//是否从工作台过来的

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_hirerhome, null);
        initView(inflate);
        initData();
        return inflate;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mTabViewModel = new TabViewModel(getActivity(), this);
        mTabViewModel.getTab();
        mHirerDoingViewModel = new HirerDoingViewModel(mActivity, this);//雇主是否有进行中的订单

        mMapView.setOnCameraChangeFinish(this);
        mMapView.clearAllMarkers();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPinMarker == null) {
                    //添加大头针
                    mPinMarker = mMapView.addPin();
                    lastLatLng = mMapView.getMyLng();
                    mMapView.addMyMarker(mMapView.getMyLng());
                    loadMapData(mMapView.getCenterLatLng(), mDemandType);
                }
                //地址反编译 获取地址
                mMapView.regeocodeSearched(AMapUtil.convertToLatLonPoint(mMapView.getCenterLatLng()), mPromptCardView.getTvAddress());
            }
        }, 500);//延迟500豪秒 防止地图未绘制完成 添加大头针失败

        mHomeHirerPager.addOnPageChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHirerDoingViewModel.isHasDoingOrder(mACache.getAsString(AppKey.CacheKey.MY_USER_ID));
    }
    /**
     * 隐藏主页订单卡片
     */
    private void goneCardView(){
        mHomeHirerPager.setVisibility(View.GONE);
        llBtn.setVisibility(View.VISIBLE);
        mPromptCardView.setVisibility(View.VISIBLE);
        mHomeType.setVisibility(View.VISIBLE);
        cardVisible = false;
    }

    /**
     * 展现主页订单卡片
     */
    private void visibilityCardView(){
        mHomeHirerPager.setVisibility(View.VISIBLE);
        llBtn.setVisibility(View.GONE);
        mPromptCardView.setVisibility(View.GONE);
        mHomeType.setVisibility(View.GONE);
        cardVisible = true;
    }
    private void loadMapData(final LatLng latLng, final String type) {
        mHomeActivity.setLoading(true);
        mMapView.jumpPin(mPinMarker, latLng);//跳动大头针
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMapsViewModel != null) {
                    mMapsViewModel.getMapDatas(latLng.longitude + "", latLng.latitude + "",
                            mDataCode, type);
                }
            }
        }, 10);
    }

    /**
     * 初始化view
     *
     * @param inflate
     */
    public void initView(View inflate) {
        mTabLayout = (TabLayout) inflate.findViewById(R.id.tablayout_hirerhome);
        mButtonList = (ImageView) inflate.findViewById(R.id.btn_list);
        mLinearLayoutTask = (LinearLayout) inflate.findViewById(R.id.layout_task);
        mImageViewTask = (ImageView) inflate.findViewById(R.id.image_task);
        ivKeFu = (ImageView) inflate.findViewById(R.id.iv_kefu);
        ivMyLocation = (ImageView) inflate.findViewById(R.id.iv_my_location);
        mHomeType = (HomeTypeView) inflate.findViewById(R.id.home_type);
        mPromptCardView = (HomePromptCardView) inflate.findViewById(R.id.home_tv_card);
        mHomeHirerPager = (ViewPager) inflate.findViewById(R.id.home_hirer_pager);
        llBtn = (LinearLayout) inflate.findViewById(R.id.layout_btn);
        if (mActivity instanceof HomeActivity) {
            mHomeActivity = (HomeActivity) mActivity;
            mMapView = mHomeActivity.getMapView();
        }
        mManager = getChildFragmentManager();
        mMapsViewModel = new HirerMapsViewModel(getActivity(), this);
        //初始化ViewPager
        mCardAdapter = new EmployeeCardAdapter(mActivity.getSupportFragmentManager(), mCardFragments);
        mHomeHirerPager.setAdapter(mCardAdapter);
        initListener();
    }

    private void initListener() {
        mButtonList.setOnClickListener(this);
        ivKeFu.setOnClickListener(this);
        ivMyLocation.setOnClickListener(this);
        mLinearLayoutTask.setOnClickListener(this);
        mImageViewTask.setOnClickListener(this);
        mPromptCardView.setTvAddressClick(this);
        mPromptCardView.setBtnReleaseJobsClick(this);
        mTabLayout.setOnTabSelectedListener(this);
        mHomeType.setHomeTypeClick(new HomeTypeView.onHomeTypeClick() {
            @Override
            public void onClick(String demandType) {
                mDemandType = demandType;
                if (null != mMapView) {
                    loadMapData(mMapView.getCenterLatLng(), demandType);
                }
            }
        });
        /**
         * 1.不是雇员 2参数：点击marker监听 postion为下标和订单list集合对应
         */
        mMapView.setClickMarkerItem(false, new DlgMapView.ClickMarker() {
            @Override
            public void clickMa(int position) {
                mHomeHirerPager.setCurrentItem(position,false);
                visibilityCardView();
                isClick = true;
            }
        });
        mMapView.setMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                goneCardView();
                mMapView.setSmallLastMarker(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_list) {//列表
            if (mHirerListFragment == null) {
                mHirerListFragment = new HirerListFragment();
                mHirerListFragment.changeTab(mDataCode);
            }
            isEnterList = true;
            mManager.beginTransaction().replace(R.id.layout_fragment, mHirerListFragment).commit();
        } else if (i == R.id.iv_kefu) {//客服

        } else if (i == R.id.iv_my_location) {//我的位置
            mMapView.toMyLocation();//移动到我的位置
        } else if (i == R.id.layout_task || i == R.id.image_task) {//正在进行中的任务
            mHomeActivity.checkHireDesk(true);
        } else if (i == R.id.tv_address) {//地址

        } else if (i == R.id.btn_release_jobs) {//找到一个零工

        }
    }

    /**
     * 从列表返回地图主页
     */
    public void removeListFragment() {
        if (mHirerListFragment != null) {
            mManager.beginTransaction().remove(mHirerListFragment).commit();
            mHirerListFragment = null;
            isEnterList = false;
            if (!TextUtils.isEmpty(mDataListCode) && !TextUtils.equals(mDataListCode, mDemandType)) {
                loadMapData(mMapView.getCenterLatLng(), mDemandType);
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mDataCode = mTabBeans.get(tab.getPosition()).getDataCode();
        if (isEnterList) {
            mDataListCode = mDataCode;
            mHirerListFragment.changeTab(mDataCode);
        } else if (mPinMarker != null) {//不与上面第一次添加大头针冲突 做此判断防止全部...加载不出
            if(cardVisible){//卡片可见的话，隐藏卡片 将大marker变小 在进行网络请求
                goneCardView();
            }
            loadMapData(mMapView.getCenterLatLng(), mDemandType);//切换的时候 也要进行请求 参数2为暂时调试数据
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void tabJson(List<TabBean> tabBeen) {
        mTabBeans = tabBeen;
        for (int i = 0; i < tabBeen.size(); i++) {
            TabLayout.Tab tab = mTabLayout.newTab().setText(tabBeen.get(i).getDataName());
            mTabLayout.addTab(tab);
        }
    }

    @Override
    public void toMapList(List<HomeMapBossListBean> homeMapListBeans) {
        mHomeActivity.setLoading(false);
        if (homeMapListBeans != null) {
            mMapView.setHirerAddMarkers(homeMapListBeans);
            mCardFragments.clear();
            for (int i = 0; i < homeMapListBeans.size(); i++) {
                HirerCardFragment hirerCardFragment = new HirerCardFragment();
                hirerCardFragment.setHomeMapDataId(homeMapListBeans.get(i).getUserId());
                mCardFragments.add(hirerCardFragment);
            }
            mCardAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onMoveFinish(CameraPosition cameraPosition) {
        if(!cardVisible){
            LatLng target = cameraPosition.target;
            float distance = 0;
            if (lastLatLng != null) {
                distance = AMapUtils.calculateLineDistance(lastLatLng, target);
            }

            if (lastLatLng == null || distance > 3000.0f) {
                loadMapData(target, "1");
                lastLatLng = target;
            }
            //地址反编译 获取地址
            mMapView.regeocodeSearched(AMapUtil.convertToLatLonPoint(target), mPromptCardView.getTvAddress());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabViewModel.onDestroyView();
        mMapsViewModel.onDestroyView();
        mHomeActivity.setLoading(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTabViewModel.onDestroy();
        mMapsViewModel.onDestroy();
        mPinMarker = null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(!isClick){
            mMapView.setBigMarker(false,position);
        }
        isClick = false;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 是否有进行中订单
     * @param isHasDoing
     */
    @Override
    public void doingOrder(boolean isHasDoing) {
        if(!isHasDoing){
            mLinearLayoutTask.setVisibility(View.GONE);
        }else{
            mLinearLayoutTask.setVisibility(View.VISIBLE);
            if(isFromDesk){//如果是从工作台过来的 那么 不进行切换 但是上面的进行中的按钮要进行刷新
                return;
            }
            mMapsViewModel.onDestroy();//取消网络请求地图上的数据
            mHomeActivity.checkHireDesk(isHasDoing);
        }
    }

    /**
     * 是否是从工作台切换过来的
     * @param isFromDesk
     */
    public void setFromDesk(boolean isFromDesk){
        this.isFromDesk = isFromDesk;
    }
}
