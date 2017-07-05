package com.dlg.personal.home.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.common.map.MapManager;
import com.dlg.data.model.MyMapLocation;
import com.dlg.data.user.model.UserAttributeInfoBean;
import com.dlg.personal.R;
import com.dlg.personal.app.MApp;
import com.dlg.personal.base.BaseActivity;
import com.dlg.personal.base.DlgMapView;
import com.dlg.personal.base.HomeToolBarHelper;
import com.dlg.personal.base.ToolBarType;
import com.dlg.personal.home.fragment.EmployeeHomeFragment;
import com.dlg.personal.home.fragment.HierDeskFragment;
import com.dlg.personal.home.fragment.HirerHomeFragment;
import com.dlg.personal.home.menu.UserMenuFragment;
import com.dlg.viewmodel.key.AppKey;
import com.dlg.viewmodel.user.LogInViewModel;
import com.dlg.viewmodel.user.presenter.LogInPresenter;

import static com.dlg.viewmodel.key.AppKey.CacheKey.MAP_LOCATION_LATLNG;

/**
 * Created by Wangjinya on 2017/6/19.
 * 主页
 */

public class HomeActivity extends BaseActivity implements LogInPresenter {
    private LinearLayout ll_content;
    private DlgMapView mMapView; //地图控件
    private UserMenuFragment mUserMenuFragment; //侧滑菜单页
    private DrawerLayout mDrawerLayout; //侧滑控件
    private TextView editSearch; //搜索栏
    private boolean isGuyuan = true, currentStatus = true;
    private ImageView mIv_search;
    private EmployeeHomeFragment mEmployeeHomeFragment;
    private HirerHomeFragment mHirerHomeFragment;
    private String centerPlaceName;
    private LatLng mLatLng;
    private MyMapLocation mMapLocation; //当前定位的数据
    private ProgressBar mPbLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_home, ToolBarType.Home);
        mMapLocation = (MyMapLocation) mACache.getAsObject(AppKey.CacheKey.MAP_LOCATION_LATLNG);
        initView(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    private void initView(Bundle savedInstanceState) {
        ll_content = (LinearLayout) findViewById(R.id.layout_main);
        mMapView = (DlgMapView) findViewById(R.id.map_view);
        mUserMenuFragment = (UserMenuFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        mMapView.onCreate(savedInstanceState);
        initMap();
        initToolbar();//初始化标题
        initContent();
        initDraw();
        // TODO: 2017/7/4 暂时用于测试登录
        LogInViewModel viewModel = new LogInViewModel(this, this, this);
        viewModel.logIn("15801955439", "123456");
    }

    /**
     * 初始化抽屉
     */
    private void initDraw() {
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (isGuyuan == currentStatus) {//当前状态和本次状态一致
                    return;
                }
                if (isGuyuan) {
                    checkEmployee();//切换雇员
                } else {
                    checkHirer();//切换雇主
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }


    /**
     * 初始化地图
     */
    private void initMap() {
        /**
         * 如果在引导页定位成功了就不需要在进行定位
         */
        if (null != mMapLocation) {
            MApp.getInstance().setMapLocation(mMapLocation);
            moveToLocation(mMapLocation);
        } else {
            MapManager.startLocation(this, new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    MapManager.stopLocation();
                    MyMapLocation mapLocation = new MyMapLocation();
                    mapLocation.setAdCode(aMapLocation.getAdCode());
                    mapLocation.setLatitude(aMapLocation.getLatitude());
                    mapLocation.setLongitude(aMapLocation.getLongitude());
                    mapLocation.setProvince(aMapLocation.getProvince());
                    mapLocation.setCity(aMapLocation.getCity());
                    mapLocation.setDistrict(aMapLocation.getDistrict());
                    mapLocation.setCityCode(aMapLocation.getCityCode());
                    mapLocation.setAdCode(aMapLocation.getAdCode());
                    mapLocation.setAddress(aMapLocation.getAddress());
                    mapLocation.setCountry(aMapLocation.getCountry());
                    mapLocation.setRoad(aMapLocation.getRoad());
                    mapLocation.setPoiName(aMapLocation.getPoiName());
                    mMapLocation = mapLocation;
                    MApp.getInstance().setMapLocation(mMapLocation);
                    moveToLocation(mapLocation);
                    mACache.put(MAP_LOCATION_LATLNG, mapLocation);
                }
            });
        }
    }

    /**
     * 移动到定位位置
     */
    private void moveToLocation(MyMapLocation mMapLocation) {
        mLatLng = new LatLng(mMapLocation.getLatitude(), mMapLocation.getLongitude());
        mMapView.moveToLocation(mLatLng);
        mMapView.setMyLng(mLatLng);
    }

    /**
     * 初始化主页面
     */
    private void initContent() {
        checkEmployee();
    }

    /**
     * 切换到雇员
     */
    private void checkEmployee() {
        setToolBarVisibility(mDrawerLayout);
        if (mEmployeeHomeFragment == null)
            mEmployeeHomeFragment = new EmployeeHomeFragment();
        mMapView.toMyLocation();//切到我的位置
        addFragment(R.id.content_frame, mEmployeeHomeFragment, "employeeHome");
        editSearch.setVisibility(View.VISIBLE);
        mIv_search.setVisibility(View.INVISIBLE);
        currentStatus = true;
    }

    /**
     * 切换雇主
     */
    private void checkHirer() {
        setToolBarVisibility(mDrawerLayout);
        if (mHirerHomeFragment == null)
            mHirerHomeFragment = new HirerHomeFragment();
        mMapView.toMyLocation();//切到我的位置
        mHirerHomeFragment.setFromDesk(false);
        addFragment(R.id.content_frame, mHirerHomeFragment, "hirerHomeFragment");
        mIv_search.setVisibility(View.VISIBLE);
        editSearch.setVisibility(View.INVISIBLE);
        currentStatus = false;
    }

    /**
     * 标题初始化
     */
    private void initToolbar() {
        setToolBarVisibility(mDrawerLayout);
        editSearch = ((HomeToolBarHelper) mToolBarHelper).getEditSearch();
        mIv_search = ((HomeToolBarHelper) mToolBarHelper).getIv_search();
        mPbLoading = ((HomeToolBarHelper) mToolBarHelper).getPb();
        ((HomeToolBarHelper) mToolBarHelper).setToolbarRightIamgOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/6/10 消息点击事件
            }
        });
        ((HomeToolBarHelper) mToolBarHelper).setToolbarLeftIamgeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mDrawerLayout) {
                    openDrawer();
                }
            }
        });
    }

    /**
     * 打开抽屉
     */
    public void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);//打开抽屉
    }

    /**
     * 侧拉切换状态
     */
    public void checkRole(boolean isGuyuan) {
        this.isGuyuan = isGuyuan;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(isGuyuan){

        }else{
            mHirerHomeFragment.setFromDesk(false);//从其他的界面返回的时候 重新初始化 boolean值
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public DlgMapView getMapView() {
        return mMapView;
    }

    /**
     * 是否加载
     *
     * @param isLoading
     */
    public void setLoading(boolean isLoading) {
        if (isLoading) {
            mPbLoading.setVisibility(View.VISIBLE);
        } else {
            mPbLoading.setVisibility(View.GONE);
        }
    }

    /**
     * 是否切换雇主工作台
     *
     * @param isCheck
     */
    public void checkHireDesk(boolean isCheck) {
        if (isCheck) {
            setToolBarGone(mDrawerLayout);
            mMapView.clearAllMarkers();
            addFragment(R.id.content_frame, new HierDeskFragment(), "hierDesk");
        } else {
            setToolBarVisibility(mDrawerLayout);
            if (mHirerHomeFragment == null)
                mHirerHomeFragment = new HirerHomeFragment();
            mMapView.toMyLocation();
            mHirerHomeFragment.setFromDesk(true);
            addFragment(R.id.content_frame, mHirerHomeFragment, "hirerHomeFragment");
        }
    }

    @Override
    public void logInUserInfo(UserAttributeInfoBean infoBean) {
        mACache.put(AppKey.CacheKey.MY_USER_ID, infoBean.getUserId());
        mACache.put(AppKey.CacheKey.MY_USER_INFO, infoBean);
    }
}
