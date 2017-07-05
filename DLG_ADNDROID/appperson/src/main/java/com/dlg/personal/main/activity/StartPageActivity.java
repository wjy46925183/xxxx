package com.dlg.personal.main.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.common.map.MapManager;
import com.common.string.LogUtils;
import com.dlg.data.model.MyMapLocation;
import com.dlg.personal.R;
import com.dlg.personal.app.ActivityNavigator;
import com.dlg.personal.app.MApp;
import com.dlg.personal.base.BaseActivity;
import com.dlg.personal.base.ToolBarType;
import com.dlg.personal.home.activity.HomeActivity;
import com.dlg.personal.main.adapter.StartPageAdapter;
import com.dlg.viewmodel.key.AppKey;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

import static com.dlg.viewmodel.key.AppKey.CacheKey.MAP_LOCATION_LATLNG;


/**
 * Created by Wangjinya on 2017/6/15.
 * 开启页面
 */
public class StartPageActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager; //滑动数据
    private ImageView mImageView; //初始化页面
    //滑动显示的图片
    private int[] resIds =
            {R.mipmap.android1, R.mipmap.android2, R.mipmap.android3};
    private EdgeEffectCompat leftEdge, rightEdge;

    private String businessNumber; //订单ID

    private MapManager mMapManager; //定位需要的管理类
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            LogUtils.i("启动页定位onLocationChanged==" + aMapLocation.toString());
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
            mACache.put(MAP_LOCATION_LATLNG, mapLocation);
            MApp.getInstance().setMapLocation(mapLocation);
            mLocationClient.stopLocation();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_start_page, ToolBarType.NO);

        initView();
        initLocation();
        setBundle();
        boolean noFirstEnter = mACache.getAsBoolean(AppKey.CacheKey.NO_FIRST_ENTER);
        if (!noFirstEnter) {//第一次进入 引导页
            initPage();
        } else {//不是第一次进入 开场动画
            mViewPager.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(R.mipmap.start_page_image).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mImageView);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    jumpHome();//进入主页
                }
            }, 3000);
        }
    }

    /**
     * H5协议
     */
    public void setBundle() {
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if (null != appLinkAction && appLinkData != null) {
            businessNumber = appLinkData.getQueryParameter("businessNumber");
        }
    }


    /**
     * 初始化定位
     */
    private void initLocation() {
        RxPermissions permissions = new RxPermissions(this);
        // Must be done during an initialization phase like onCreate
        //申请定位权限和sd卡写入权限
        permissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        startLocation();
                        if(aBoolean){

                        }else{// At least one permission is denied

                        }
                    }
                });

    }

    private void startLocation(){
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    /**
     * 开场引导页
     */
    private void initPage() {
        try {
            Field leftEdgeField = mViewPager.getClass().getDeclaredField("mLeftEdge");
            Field rightEdgeField = mViewPager.getClass().getDeclaredField("mRightEdge");
            if (leftEdgeField != null && rightEdgeField != null) {
                leftEdgeField.setAccessible(true);
                rightEdgeField.setAccessible(true);
                leftEdge = (EdgeEffectCompat) leftEdgeField.get(mViewPager);
                rightEdge = (EdgeEffectCompat) rightEdgeField.get(mViewPager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<View> views = new ArrayList<>();
        StartPageAdapter startPageAdapter = new StartPageAdapter(this, views);
        mViewPager.setAdapter(startPageAdapter);

        for (int i = 0; i < resIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(resIds[i]);
            if (i == resIds.length - 1) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jumpHome();
                    }
                });
            }
            views.add(imageView);
        }
        startPageAdapter.notifyDataSetChanged();

        mViewPager.addOnPageChangeListener(this);
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.iv_start_page);
        mViewPager = (ViewPager) findViewById(R.id.start_page_viewpager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //到了最后一张并且还继续拖动，出现蓝色限制边条了
        if (leftEdge != null && !rightEdge.isFinished()) {
            mACache.put(AppKey.CacheKey.NO_FIRST_ENTER, true);
            jumpHome();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    /**
     * 跳入主页
     */
    private void jumpHome() {
        ActivityNavigator.navigator().navigateTo(HomeActivity.class);
        finish();
    }
}
