package gongren.com.dlg.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.fragment.odddetail.LoCallBack;
import gongren.com.dlg.fragment.odddetail.MasterFragment;
import gongren.com.dlg.javabean.FinishEvent;
import gongren.com.dlg.utils.ToastUtils;

/**
 * Created by Wangjinya on 2017/4/13.
 * 雇主的订单零工详情 地图展示 及弹框提示
 */

public class MasterDetailsActivity extends FragmentActivity implements AMap.OnMarkerClickListener, AMap.OnCameraChangeListener, LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener, LoCallBack {
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_right)
    ImageView mIvRight;
    @Bind(R.id.map_view)
    MapView mMapView;
    private MasterFragment mMasterFragment;
    private AMap mAmap;
    private String businessNumber;//订单编号
    private Double tip; //小费
    private Double pay;    //金额
    private String orders; //支付订单
    private double bao;
//    private RouteSearch mRouteSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mMapView.onCreate(savedInstanceState);
        businessNumber = getIntent().getStringExtra("businessNumber");
        tip = getIntent().getDoubleExtra("tip", 0);
        pay = getIntent().getDoubleExtra("pay", 0);
        orders = getIntent().getStringExtra("orders");
        bao = getIntent().getDoubleExtra("bao", 0);
        int status = getIntent().getIntExtra("status", 0);
        if (status == 6) {
            mTvTitle.setText("等待雇员同意");
        } else if (status == 7) {
            mTvTitle.setText("拒绝邀请");
        }
//        mNot_punch = getIntent().getBooleanExtra("not_punch",false);
        mMasterFragment =
                (MasterFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_odd);
        if(status == 6 || status == 7){
            mMasterFragment.setId_Tital(tip, pay, businessNumber, orders, null, bao);
        }else {
            mMasterFragment.setId_Tital(tip, pay, businessNumber, orders, mTvTitle, bao);
        }
        Log.e("tip~~MasterDetail", "tip=" + tip);
        mMasterFragment.setLoCallBack(this);
        initMapView();
    }


    //下级Fragment发来的消息
    @Subscribe
    public void onMessageEvent(FinishEvent event) {
        finish();
    }

    /**
     * 初始化地图参数
     */
    private void initMapView() {
        mAmap = mMapView.getMap();
        if (null != mAmap) {
            mAmap.setMapType(AMap.MAP_TYPE_NAVI);
            UiSettings uiSettings = mAmap.getUiSettings();
            uiSettings.setLogoBottomMargin(-60);
            uiSettings.setZoomControlsEnabled(false);     //隐藏缩放按钮
            uiSettings.setScaleControlsEnabled(true);      //显示比例尺

            mAmap.setLocationSource(this);// 设置定位监听
            mAmap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
            mAmap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
            mAmap.setOnMarkerClickListener(this);
            mAmap.setOnCameraChangeListener(this);
        }
        //对amap添加移动地图事件监听器
//        mRouteSearch = new RouteSearch(this);
//        mRouteSearch.setRouteSearchListener(this);

    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
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
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!marker.equals(maplocationMarker)) {
            ToastUtils.showToastShort(this, "点击了雇员头像");
        }
        return true;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
    }

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
        }
    }

    @Override
    public void deactivate() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
    }

    private LatLng mLocalLatlng;
    private String locationAddress;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            mAmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//            if (mRouteSearch != null && latLonPoint != null) {
//                //显示路线
//                RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
//                        new LatLonPoint(mLocalLatlng.latitude, mLocalLatlng.longitude)
//                        , latLonPoint);
//                RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo,
//                        RouteSearch.DrivingDefault, null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，
//                // 第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
//                mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
//            }
            if (mlocationClient != null) {
                //具体定位的地点
                locationAddress = aMapLocation.getAddress();
                mlocationClient.stopLocation();
                mlocationClient = null;
                setImgMaker();
            }
        }
    }

    /**
     * 在地图上面设置雇员位置
     */
    private void setImgMaker() {
        View view = View.inflate(this, R.layout.item_map_order_img_big, null);
        CircleImageView civ = (CircleImageView) view.findViewById(R.id.iv_head);
        Glide.with(this).load(logo).placeholder(R.mipmap.morentouxiang).error(R.mipmap.morentouxiang).into(civ);
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromView(view))
                .position(latLng)
                .draggable(true);
        Marker marker = mAmap.addMarker(markerOptions);
        marker.setZIndex(0);
        marker.setToTop();
        setLocationAddress();
    }

    /**
     * 添加定位大头针的图标
     */
    private Marker maplocationMarker;        //定位的图标

    private void setLocationAddress() {
        MarkerOptions markerOptions1 = new MarkerOptions().icon(
                BitmapDescriptorFactory.fromResource(R.mipmap.map_location))
                .position(mLocalLatlng)
                .title("")
                .snippet("")
                .draggable(true);
        maplocationMarker = mAmap.addMarker(markerOptions1);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    /**
     * 定位回调
     *
     * @param driveRouteResult
     * @param errorCode
     */
    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {
//        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
//            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
//                if (driveRouteResult.getPaths().size() > 0) {
//                    DriveRouteResult mDriveRouteResult = driveRouteResult;
//                    DrivePath drivePath = mDriveRouteResult.getPaths()
//                            .get(0);
//                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
//                            this, mAmap, drivePath,
//                            mDriveRouteResult.getStartPos(),
//                            mDriveRouteResult.getTargetPos(), null);
//                    drivingRouteOverlay.setIsShowMaker(false);//不显示起点和终点
//                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
//                    drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
//                    drivingRouteOverlay.removeFromMap();
//                    drivingRouteOverlay.addToMap();
//                    //显示不同路径的缩放比例
//
//                } else if (driveRouteResult != null && driveRouteResult.getPaths() == null) {
//                    ToastUtils.showToastShort(this, "未找到路线");
//                }
//            } else {
//                ToastUtils.showToastShort(this, "未找到路线");
//            }
//        } else {
//        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    private LatLng latLng;
    private LatLonPoint latLonPoint;

    /**
     * 获取雇员的地理位置
     *
     * @param longitude
     * @param latitude
     */
    @Override
    public void employeeLoation(double longitude, double latitude) {
        latLng = new LatLng(latitude, longitude);
        latLonPoint = new LatLonPoint(latitude, longitude);
    }

    /**
     * 获取雇主的工作地理位置
     *
     * @param longitude
     * @param latitude
     */
    @Override
    public void masterLoation(double longitude, double latitude) {
        mLocalLatlng = new LatLng(latitude, longitude);
        mlocationClient.startLocation();
    }

    private String logo;

    @Override
    public void getLogo(String logo) {
        this.logo = logo;
    }
}
