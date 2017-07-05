package com.dlg.personal.home.fragment;


import android.content.Intent;
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
import com.dlg.data.home.model.HomeMapListBean;
import com.dlg.data.home.model.TabBean;
import com.dlg.personal.R;
import com.dlg.personal.base.BaseFragment;
import com.dlg.personal.base.DlgMapView;
import com.dlg.personal.home.activity.ConditionsSearchActivity;
import com.dlg.personal.home.activity.HomeActivity;
import com.dlg.personal.home.adapter.EmployeeCardAdapter;
import com.dlg.personal.home.view.HomePromptCardView;
import com.dlg.personal.home.view.HomeTypeView;
import com.dlg.viewmodel.home.EmployeeIsDoingViewModel;
import com.dlg.viewmodel.home.EmployeeMapsViewModel;
import com.dlg.viewmodel.home.TabViewModel;
import com.dlg.viewmodel.home.presenter.EmployeeIsDoingPresenter;
import com.dlg.viewmodel.home.presenter.EmployeeMapPresenter;
import com.dlg.viewmodel.home.presenter.TabPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinya on 2017/6/19.
 * 雇员主页
 */

public class EmployeeHomeFragment extends BaseFragment implements View.OnClickListener, TabLayout.OnTabSelectedListener, TabPresenter, EmployeeMapPresenter,
        DlgMapView.OnCameraChangeFinish, ViewPager.OnPageChangeListener, EmployeeIsDoingPresenter {

    private TabLayout mTabLayout; //头部标签布局
    private ImageView mButtonList; //列表数据切换布局按钮
    private ImageView ivKeFu, ivMyLocation; //客服与定位按钮
    private LinearLayout mLinearLayoutTask; //进行中按钮控件
    private ImageView mImageViewTask; //进行中的头像布局
    private DlgMapView mMapView;//HomeActivity中的地图对象
    private HomeTypeView mHomeType; //标签布局
    private HomePromptCardView mPromptCardView; //底部卡片布局
    private ViewPager mCardViewPager;//底部订单卡片
    private LinearLayout layout_btn;//定位，客服

    private EmployeeMapsViewModel mEmployeeMapsViewModel;  //地图数据ViewModel
    private TabViewModel mTabViewModel; //tab标签数据ViewModel
    private EmployeeIsDoingViewModel mIsDoingViewModel; //雇员是否有进行中的订单

    private FragmentManager mManager;
    private EmployeeListFragment mEmployeeListFragment;  //雇员列表数据页面
    private List<TabBean> mTabBeans;  //Tab标签数据
    private boolean isEnterList;//是否进入了主页列表
    private String mDataCode;
    private HomeActivity mHomeActivity;
    private Handler mHandler = new Handler();
    private Marker mPinMarker;//大头针Marker
    private LatLng lastLatLng;
    private String mDemandType; //任务类型 空为全部、1=工作日、2=双休日、3=计件

    private String mDataListCode; //数据列表点击后的Tab标签字段
    private List<HomeMapListBean> homeBeans = new ArrayList<>();
    private List<Fragment> mCardFragments = new ArrayList<>();
    private EmployeeCardAdapter mCardAdapter;
    private boolean isClick; //是否是点击地图上的marker 如果是为true 否则为false
    private boolean cardVisible; //是否卡片可见

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_employeehome, null);
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
        mEmployeeMapsViewModel = new EmployeeMapsViewModel(mActivity, this);
        mIsDoingViewModel = new EmployeeIsDoingViewModel(mActivity, this);
        mMapView.setOnCameraChangeFinish(this);
        mMapView.clearAllMarkers();//清空marker
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPinMarker == null) {
                    //添加大头针
                    mPinMarker = mMapView.addPin();
                    mMapView.addMyMarker(mMapView.getMyLng());
                    loadMapData(mMapView.getCenterLatLng(), mDemandType);
                    //加载网络数据
                    loadMapData(mMapView.getCenterLatLng(), mDemandType);
                    //设置地图marker点击
                    mMapView.setClickMarkerItem(true, new DlgMapView.ClickMarker() {
                        @Override
                        public void clickMa(int position) {
                            visibilityCardView();//展示订单卡片
                            isClick = true;
                            mCardViewPager.setCurrentItem(position, false);//保持 卡片和切换一致
                        }
                    });
                }
                //地址反编译 获取地址
                mMapView.regeocodeSearched(AMapUtil.convertToLatLonPoint(mMapView.getCenterLatLng()), mPromptCardView.getTvAddress());
            }
        }, 500);//延迟500豪秒 防止地图未绘制完成 添加大头针失败
        mMapView.setMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                goneCardView();//隐藏订单卡片
                mMapView.setSmallLastMarker(true);
            }
        });
        mCardViewPager.addOnPageChangeListener(this);
    }

    /**
     * 隐藏主页订单卡片
     */
    private void goneCardView() {
        mCardViewPager.setVisibility(View.GONE);
        layout_btn.setVisibility(View.VISIBLE);
        mPromptCardView.setVisibility(View.VISIBLE);
        mHomeType.setVisibility(View.VISIBLE);
        cardVisible = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mIsDoingViewModel) {
            mIsDoingViewModel.isHasDoingTask();
        }
    }

    /**
     * 展现主页订单卡片
     */
    private void visibilityCardView() {
        mCardViewPager.setVisibility(View.VISIBLE);
        layout_btn.setVisibility(View.GONE);
        mPromptCardView.setVisibility(View.GONE);
        mHomeType.setVisibility(View.GONE);
        cardVisible = true;
    }

    /**
     * 加载地图上面的网络数据
     *
     * @param latLng 参数为经纬度实体类
     * @param type   双休日 工作日 计件
     */
    private void loadMapData(final LatLng latLng, final String type) {

        mMapView.jumpPin(mPinMarker, latLng);//跳动大头针
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mEmployeeMapsViewModel != null) {
                    mEmployeeMapsViewModel.onDestroy();//如果上次的网络加载还未结束 直接结束上次出网络请求
                    mEmployeeMapsViewModel.getMapDatas(latLng.longitude + "", latLng.latitude + "",
                            mDataCode, type);
                }
            }
        }, 100);
    }

    /**
     * 初始化view
     *
     * @param inflate
     */
    public void initView(View inflate) {
        mTabLayout = (TabLayout) inflate.findViewById(R.id.tablayout_employee);
        mButtonList = (ImageView) inflate.findViewById(R.id.btn_list);
        ivKeFu = (ImageView) inflate.findViewById(R.id.iv_kefu);
        ivMyLocation = (ImageView) inflate.findViewById(R.id.iv_my_location);
        mLinearLayoutTask = (LinearLayout) inflate.findViewById(R.id.layout_task);
        mImageViewTask = (ImageView) inflate.findViewById(R.id.image_task);
        mHomeType = (HomeTypeView) inflate.findViewById(R.id.home_type);
        mPromptCardView = (HomePromptCardView) inflate.findViewById(R.id.home_tv_card);
        mCardViewPager = (ViewPager) inflate.findViewById(R.id.home_employee_pager);
        layout_btn = (LinearLayout) inflate.findViewById(R.id.layout_btn);
        mManager = getChildFragmentManager();
        if (mActivity instanceof HomeActivity) {
            mHomeActivity = (HomeActivity) mActivity;
            mMapView = mHomeActivity.getMapView();
        }
        //初始化ViewPager
        mCardAdapter = new EmployeeCardAdapter(mActivity.getSupportFragmentManager(), mCardFragments);
        mCardViewPager.setAdapter(mCardAdapter);
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
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_list) {//列表
            if (mEmployeeListFragment == null) {
                mEmployeeListFragment = new EmployeeListFragment();
                mEmployeeListFragment.changeTab(mDataCode);
            }
            isEnterList = true;
            mManager.beginTransaction().replace(R.id.layout_fragment, mEmployeeListFragment).commit();
        } else if (i == R.id.iv_kefu) {//客服

        } else if (i == R.id.iv_my_location) {//我的位置
            mMapView.toMyLocation();//移动到我的位置
        } else if (i == R.id.layout_task) {//正在进行中的任务

        } else if (i == R.id.tv_address) {//地址

        } else if (i == R.id.btn_release_jobs) {//找到一个零工
            Intent intent = new Intent(getActivity(), ConditionsSearchActivity.class);
            intent.putExtra("xCoordinate", lastLatLng.longitude);
            intent.putExtra("yCoordinate", lastLatLng.latitude);
            //intent.putExtra("",)
            startActivity(intent);

        }
    }

    /**
     * tab切换
     *
     * @param tab
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mDataCode = mTabBeans.get(tab.getPosition()).getDataCode();
        if (isEnterList) {
            mDataListCode = mDataCode;
            mEmployeeListFragment.changeTab(mDataCode);
        } else if (mPinMarker != null) {//不与上面第一次添加大头针冲突 做此判断防止全部...加载不出
            if (cardVisible) {//卡片可见的话，隐藏卡片 将大marker变小 在进行网络请求
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

    /**
     * Tab中的数据
     *
     * @param tabBeen
     */
    @Override
    public void tabJson(List<TabBean> tabBeen) {
        mTabBeans = tabBeen;
        for (int i = 0; i < tabBeen.size(); i++) {
            TabLayout.Tab tab = mTabLayout.newTab().setText(tabBeen.get(i).getDataName());
            mTabLayout.addTab(tab);
        }
    }

    /**
     * 从列表返回地图主页
     */
    public void removeListFragment() {
        if (mEmployeeListFragment != null) {
            mManager.beginTransaction().remove(mEmployeeListFragment).commit();
            mEmployeeListFragment = null;
            isEnterList = false;
            if (!TextUtils.isEmpty(mDataListCode) && !TextUtils.equals(mDataListCode, mDemandType)) {
                loadMapData(mMapView.getCenterLatLng(), mDemandType);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabViewModel.onDestroyView();
        mEmployeeMapsViewModel.onDestroyView();
        mIsDoingViewModel.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTabViewModel.onDestroy();
        mEmployeeMapsViewModel.onDestroy();
        mIsDoingViewModel.onDestroy();
        mPinMarker = null;
    }

    /**
     * 地图上面的网络数据
     *
     * @param homeMapListBeans
     */
    @Override
    public void toMapList(List<HomeMapListBean> homeMapListBeans) {
        if (homeMapListBeans != null) {
            homeBeans.clear();
            homeBeans.addAll(homeMapListBeans);
            mMapView.addEmployeeMarkers(homeBeans);

            mCardFragments.clear();
            for (int i = 0; i < homeBeans.size(); i++) {
                EmployeeCardFragment employeeCardFragment = new EmployeeCardFragment();
                employeeCardFragment.setHomeMapListBean(homeMapListBeans.get(i));
                mCardFragments.add(employeeCardFragment);
            }
            mCardAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 是否有进行中订单
     *
     * @param isHasDoing
     */
    @Override
    public void isHasDoingTask(boolean isHasDoing) {
        mLinearLayoutTask.setVisibility(isHasDoing ? View.VISIBLE : View.GONE);
    }

    /**
     * 移动地图停下来的监听事件 停止下来 加载地图上面的网络数据
     *
     * @param cameraPosition
     */
    @Override
    public void onMoveFinish(CameraPosition cameraPosition) {
        if (cardVisible) {//如果卡片可见的话 那么 我们不能去加载网络数据 刷新地图
            return;
        }
        LatLng target = cameraPosition.target;
        float distance = 0;
        if (lastLatLng != null) {
            distance = AMapUtils.calculateLineDistance(lastLatLng, target);
        }

        if (lastLatLng == null || distance > 3000.0f) {
            loadMapData(target, mDemandType);
            lastLatLng = target;
        }
        //地址反编译 获取地址
        mMapView.regeocodeSearched(AMapUtil.convertToLatLonPoint(target), mPromptCardView.getTvAddress());
    }

    /**
     * 移动ViewPager
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 地图点击marker和切换viewpager是同步的关系
     * 点击marker的时候 setbigmarker方法应该被合理调用
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        if (!isClick) {
            mMapView.setBigMarker(true, position);//ViewPager和地图上面的用法一致
        }
        isClick = false;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
