package gongren.com.dlg.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.fragment.odddetail.EmployFragment;
import gongren.com.dlg.utils.DrivingRouteOverlay;
import gongren.com.dlg.utils.ToastUtils;

/**
 * Created by Wangjinya on 2017/4/13.
 * 雇员的订单零工详情 地图展示 及弹框提示
 */

public class EmployDetailsActivity extends FragmentActivity implements LocationSource, AMap.OnMarkerClickListener, AMap.OnCameraChangeListener, AMapLocationListener, RouteSearch.OnRouteSearchListener {
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_right)
    ImageView mIvRight;
    @Bind(R.id.map_view)
    MapView mMapView;
    private EmployFragment mEmployFragment;
    private AMap mAmap;
    private Marker startNavigationmarker;
    private Marker endNavigationmarker;
    private RouteSearch mRouteSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employ);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);
        mEmployFragment = (EmployFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_odd);
        mEmployFragment.checkDialogType(EmployFragment.WORKING, mTvTitle);

        mMapView.onCreate(savedInstanceState);
        initMapView();
    }

    /**
     * 初始化地图参数
     */
    private void initMapView() {
        mAmap = mMapView.getMap();
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
        mAmap.setOnCameraChangeListener(this);       //对amap添加移动地图事件监听器

        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_title:
                break;
        }
    }

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
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    @Override
    public void deactivate() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        setImgMaker(marker);
        ToastUtils.showToastShort(this, "点击了雇员头像");
        return false;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

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
    }

    LatLng mLocalLatlng;
    private String locationAddress;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            //获取工作地理位置
            mLocalLatlng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());//雇员位置
            LatLng latLng = new LatLng(31.2338893501, 121.3820219756);//工作地址

            LatLonPoint mEndPoint = new LatLonPoint(31.2338893501, 121.3820219756);//工作地址为终点
            LatLonPoint mStartPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            //移动到工作的地理位置
            mAmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            mAmap.addCircle(new CircleOptions().
                    center(latLng).
                    radius(50).
                    fillColor(Color.parseColor("#995CA6B9")).
                    strokeColor(Color.parseColor("#9902add9")).
                    strokeWidth(1));
            //显示路线
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                    mStartPoint, mEndPoint);
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo,
                    RouteSearch.DrivingDefault, null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，
            // 第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
            if (mlocationClient != null) {
                //具体定位的地点
                locationAddress = aMapLocation.getAddress();
                mlocationClient.stopLocation();
                setImgMaker(null);
            }
        }
    }

    /**
     * 在地图上面设置工作位置
     */
    private void setImgMaker(Marker mar) {
        if (mar == null) {
            View view = View.inflate(this, R.layout.item_map_order_big, null);
            TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
//            civ.setImageResource(R.drawable.ic_launcher);
            tv_price.setText("200元/天");
            MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromView(view))
                    .position(new LatLng(31.2338893501, 121.3820219756))
                    .draggable(true);
            Marker marker = mAmap.addMarker(markerOptions);
            marker.setZIndex(0);
            marker.setToTop();
        } else {
            View newView = View.inflate(this, R.layout.item_map_order_big, null);
            TextView tv_price = (TextView) newView.findViewById(R.id.tv_price);
//            ivHead.setImageResource(R.drawable.ic_launcher);
            mar.setIcon(BitmapDescriptorFactory.fromView(newView));
            mar.setToTop();
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    DriveRouteResult mDriveRouteResult = result;
                    DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            this, mAmap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    //显示不同路径的缩放比例
                    drivingRouteOverlay.zoomToSpan(500);

                } else if (result != null && result.getPaths() == null) {
                    ToastUtils.showToastShort(this, "未找到路线");
                }
            } else {
                ToastUtils.showToastShort(this, "未找到路线");
            }
        } else {
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
