package gongren.com.dlg.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.fragment.FindWorkerFragment;
import gongren.com.dlg.view.CustumMapView;

/**
 * Created by Wangjinya on 2017/5/10.
 * 雇员点击雇主招聘列表 进入的地图详情
 */

public class WorkerFindOrderActivity extends BaseActivity implements LocationSource, AMap.OnCameraChangeListener, AMapLocationListener {
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_right)
    ImageView mIvRight;
    @Bind(R.id.map_view)
    CustumMapView mMapView;
    @Bind(R.id.fl_card)
    FrameLayout fl_card;
    @Bind(R.id.ll)
    LinearLayout ll;
    private AMapLocationClient mlocationClient;
    private AMap mMap;
    private FindWorkerFragment mMapOrderFragment;
    private final int MapOrderTag = 1;
    private LatLng myLatLng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_find_order);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);
        initMapView();
    }

    private LatLng latLng;
    //加载数据
    private void initData() {
        mMapOrderFragment = new FindWorkerFragment();//卡片信息

        mMapOrderFragment.setWorkerFindTag(MapOrderTag);
        if (getIntent() != null) {
            String id = getIntent().getStringExtra("id");
            double yCoordinate = getIntent().getDoubleExtra("yCoordinate",0);
            double xCoordinate = getIntent().getDoubleExtra("xCoordinate",0);
            latLng = new LatLng(yCoordinate,xCoordinate);
            String postTypeName = getIntent().getStringExtra("postTypeName");
            String postName = getIntent().getStringExtra("postName");
            String price = getIntent().getStringExtra("price");
            String jobMeterUnitName = getIntent().getStringExtra("jobMeterUnitName");

            final View oldView = View.inflate(this, R.layout.item_map_small, null);
            TextView oldPrice = (TextView) oldView.findViewById(R.id.tv_price);
            if(!"志愿义工".equals(postTypeName)){
                mTvTitle.setText(postName +" "+ price
                        + "元/" + jobMeterUnitName);
                oldPrice.setText(price+"\n元/"
                        +jobMeterUnitName);
            }else{
                mTvTitle.setText(postName);
                oldPrice.setText("0\n元/单");
            }

            if(yCoordinate <= 0||xCoordinate<=0){
                /**
                 * 取不到经纬度，回调经纬度
                 */
                mMapOrderFragment.setLocationCallBack(new FindWorkerFragment.LocationCallBack() {
                    @Override
                    public void location(double yCoordinate, double xCoordinate) {
                        latLng = new LatLng(yCoordinate, xCoordinate);
                        MarkerOptions options = new MarkerOptions()
                                .draggable(true)
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromView(oldView));
                        Marker marker = mMap.addMarker(options);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
                    }
                });
            }else{
                /**
                 * 能取到经纬度
                 */
                MarkerOptions options = new MarkerOptions()
                        .draggable(true)
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromView(oldView));
                Marker marker = mMap.addMarker(options);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
            }
            mMapOrderFragment.setTaskId(id);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_card, mMapOrderFragment).commit();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    //初始化地图
    private void initMapView() {
        mMap = mMapView.getMap();
//        mMap.setZ
        mMap.setMapType(AMap.MAP_TYPE_NAVI);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setLogoBottomMargin(-60);
        uiSettings.setZoomControlsEnabled(false);     //隐藏缩放按钮
        uiSettings.setScaleControlsEnabled(true);      //显示比例尺
        uiSettings.setGestureScaleByMapCenter(false);
        mMap.setLocationSource(this);// 设置定位监听
        mMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        mMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
        mMap.setOnCameraChangeListener(this);       //对amap添加移动地图事件监听器
        startLocation();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onCreate(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mlocationClient != null)
            mlocationClient.stopLocation();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(4000);
            mLocationOption.setNeedAddress(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
        }
        mlocationClient.startLocation();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
    }

    @Override
    public void deactivate() {
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    //移动地图 完成
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    //定位完成
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            startLocation();
            return;
        }
        myLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        MarkerOptions options = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_mark_mine_72_72))
                .draggable(true)
                .position(myLatLng);
        mMap.addMarker(options);
        if (mlocationClient != null)
            mlocationClient.stopLocation();
        initData();
    }
}
