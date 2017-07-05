package com.dlg.personal.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.bumptech.glide.Glide;
import com.common.overlay.AMapUtil;
import com.common.overlay.BusRouteOverlay;
import com.common.overlay.DrivingRouteOverlay;
import com.common.overlay.RideRouteOverlay;
import com.common.overlay.WalkRouteOverlay;
import com.common.string.StringUtil;
import com.dlg.data.home.model.HomeMapBossListBean;
import com.dlg.data.home.model.HomeMapListBean;
import com.dlg.personal.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者：王进亚
 * 主要功能：打零工地图基类 无需在Activity中配置各种参数 生命周期还需要自己配置
 * 创建时间：2017/6/27 09:29
 */

public class DlgMapView extends TextureMapView {
    private AMap mAMap;
    private List<Marker> mMarkers = new ArrayList<>();
    private Marker myMarker;
    private GeocodeSearch geocoderSearch; //地址反编译类
    private RouteSearch routeSearch;
    private List<HomeMapListBean> homeMapListBeans;
    private int clickedPosition = -1;//marker最后一次被点击的位置 默认是-1
    private LatLng mLatLng;

    public DlgMapView(Context context) {
        super(context);
        initDlgMap();
    }

    public DlgMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initDlgMap();
    }

    public DlgMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initDlgMap();
    }

    public DlgMapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
        initDlgMap();
    }

    /**
     * 初始化地图
     */
    private void initDlgMap() {
        mAMap = getMap();
        //设置地图相关参数
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setLogoBottomMargin(-60);
        uiSettings.setZoomControlsEnabled(false);     //隐藏缩放按钮
        uiSettings.setScaleControlsEnabled(true);      //显示比例尺
        uiSettings.setTiltGesturesEnabled(false);       //倾斜手势
        uiSettings.setRotateGesturesEnabled(false);     //旋转手势
        mAMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

    }

    /**
     * 设置我的位置 也就是地图定位成功以后 将我的位置LatLng给初始化
     *
     * @param latLng
     */
    public void setMyLng(LatLng latLng) {
        mLatLng = latLng;
    }

    /**
     * 返回我的定位的信息
     *
     * @return
     */
    public LatLng getMyLng() {
        return mLatLng;
    }

    /**
     * 初始化clickPosition
     */
    public void setClickedPosition() {
        clickedPosition = -1;
    }

    /**
     * 绘制海量点图层 待定
     *
     * @param latlngs
     */
    public void setItemsMulti(List<LatLng> latlngs) {

        View view = View.inflate(getContext(), R.layout.item_map_small, null);

        MultiPointOverlayOptions overlayOptions = new MultiPointOverlayOptions();
        overlayOptions.icon(BitmapDescriptorFactory.fromView(view));//设置图标
        overlayOptions.anchor(0.5f, 0.5f); //设置锚点

        MultiPointOverlay multiPointOverlay = mAMap.addMultiPointOverlay(overlayOptions);

        List<MultiPointItem> list = new ArrayList<MultiPointItem>();
        for (int i = 0; i < latlngs.size(); i++) {
            //创建MultiPointItem存放，海量点中某单个点的位置及其他信息
            MultiPointItem multiPointItem = new MultiPointItem(latlngs.get(i));
            list.add(multiPointItem);
        }
        multiPointOverlay.setItems(list);//将规范化的点集交给海量点管理对象设置，待加载完毕即可看到海量点信息
    }

    /**
     * 添加雇员marker 不包括 大头针和定位图标
     * 添加前先删除之前的marker 防止重叠
     *
     * @param homeMapListBeans
     */
    public void addEmployeeMarkers(List<HomeMapListBean> homeMapListBeans) {
        this.homeMapListBeans = homeMapListBeans;
        clearEmployeeMarkers();

        for (int i = 0; i < homeMapListBeans.size(); i++) {
            HomeMapListBean homeMapListBean = homeMapListBeans.get(i);
            LatLng latLng = new LatLng(homeMapListBean.getyCoordinate(), homeMapListBean.getxCoordinate());

            View view = View.inflate(getContext(), R.layout.item_map_small, null);
            TextView tv_price = (TextView) view.findViewById(R.id.tv_price);

            tv_price.setText(homeMapListBean.getPrice() + "\n元/" + homeMapListBean.getJobMeterUnitName());
            MarkerOptions markerOptions = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromView(view))
                    .title("")
                    .position(latLng).draggable(false);

            Marker marker = mAMap.addMarker(markerOptions);
            marker.setZIndex(i);
            mMarkers.add(marker);//将地图上面的marker添加到集合里面 二次加载的时候 用于remove
        }
    }

    /**
     * 地图Marker点击事件监听
     */
    public void setClickMarkerItem(final boolean isGuYuan, final ClickMarker clickMarker) {
        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int zIndex = (int) marker.getZIndex();
                if (zIndex == clickedPosition) {
                    clickedPosition = -1;
                }
                setBigMarker(isGuYuan, zIndex);
                if (clickMarker != null)
                    clickMarker.clickMa(zIndex);
                return true;
            }
        });
    }

    /**
     * 雇员将最后一个大的marker变小
     */
    public void setSmallLastMarker(boolean isGuYuan) {
        if (isGuYuan) {
            if (bigLastMarker != null) {
                View view = View.inflate(getContext(), R.layout.item_map_small, null);
                TextView tv_price = (TextView) view.findViewById(R.id.tv_price);

                HomeMapListBean bean = homeMapListBeans.get(clickedPosition);
                tv_price.setText(bean.getPrice() + "\n元/" + bean.getJobMeterUnitName());
                bigLastMarker.setIcon(BitmapDescriptorFactory.fromView(view));
            }
        } else {
            if (bigLastMarker != null) {
                HomeMapBossListBean bean = homeMapBossListBeans.get(clickedPosition);
                final View view = View.inflate(getContext(), R.layout.item_map_hire_img, null);
                final CircleImageView headImg = (CircleImageView) view.findViewById(R.id.iv_head_mark);
                Glide.with(getContext()).load(bean.getUserLogo())
                        .thumbnail(0.1f)
                        .placeholder(R.mipmap.icon_default)
                        .error(R.mipmap.icon_default).into(headImg);
                bigLastMarker.setIcon(BitmapDescriptorFactory.fromView(view));
            }
        }

    }

    //最后一个变大的Marker
    private Marker bigLastMarker;

    /**
     * 设置被选中 变大的marker
     *
     * @param isGuYuan
     * @param zIndex
     */
    public void setBigMarker(boolean isGuYuan, int zIndex) {
        if(mMarkers.size()-1 < zIndex){
            return;
        }
        final Marker bigMarker = mMarkers.get(zIndex);
        if (clickedPosition == zIndex) {//下标位置相同的时候 必须要初始化 clickedPosition -1
            clickedPosition = -1;
        }
        if (isGuYuan) {
            final View newView = View.inflate(getContext(), R.layout.item_map_big, null);
            final TextView newPrice = (TextView) newView.findViewById(R.id.tv_price);
            final HomeMapListBean homeMapListBean = homeMapListBeans.get(zIndex);
            final LatLng latLng = new LatLng(homeMapListBean.getyCoordinate(), homeMapListBean.getxCoordinate());

            newPrice.setText(homeMapListBean.getPrice() + "\n元/"
                    + homeMapListBean.getJobMeterUnitName());

            bigMarker.setIcon(BitmapDescriptorFactory.fromView(newView));
            if (clickedPosition != -1) {
                final View view = View.inflate(getContext(), R.layout.item_map_small, null);
                final TextView tv_price = (TextView) view.findViewById(R.id.tv_price);

                final HomeMapListBean bean = homeMapListBeans.get(clickedPosition);
                tv_price.setText(bean.getPrice() + "\n元/" + bean.getJobMeterUnitName());
                mMarkers.get(clickedPosition).setIcon(BitmapDescriptorFactory.fromView(view));
            }
            bigLastMarker = bigMarker;
            clickedPosition = zIndex;
            bigMarker.setToTop();
            mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
        } else {
            final View bossView = View.inflate(getContext(), R.layout.item_map_hire_big, null);
            final CircleImageView circleImageView = (CircleImageView) bossView.findViewById(R.id.iv_head);
            final HomeMapBossListBean homeMapBossListBean = homeMapBossListBeans.get(zIndex);
            final LatLng latLng = new LatLng(homeMapBossListBean.getyCoordinate(), homeMapBossListBean.getxCoordinate());
            Glide.with(getContext()).load(homeMapBossListBean.getUserLogo())
                    .placeholder(R.mipmap.icon_default)
                    .error(R.mipmap.icon_default)
                    .thumbnail(0.5f)
                    .into(circleImageView);
            bigMarker.setIcon(BitmapDescriptorFactory.fromView(bossView));
            if (clickedPosition != -1) {
                final  HomeMapBossListBean bean = homeMapBossListBeans.get(clickedPosition);
                final View view = View.inflate(getContext(), R.layout.item_map_hire_img, null);
                final CircleImageView headImg = (CircleImageView) view.findViewById(R.id.iv_head_mark);
                Glide.with(getContext()).load(bean.getUserLogo())
                        .thumbnail(0.5f)
                        .placeholder(R.mipmap.icon_default)
                        .error(R.mipmap.icon_default).into(headImg);
                mMarkers.get(clickedPosition).setIcon(BitmapDescriptorFactory.fromView(view));
            }
            clickedPosition = zIndex;
            bigLastMarker = bigMarker;
            bigMarker.setToTop();
            mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
        }

    }


    public interface ClickMarker {
        void clickMa(int position);
    }


    private List<HomeMapBossListBean> homeMapBossListBeans;

    /**
     * 雇主添加markers
     *
     * @param homeMapBossListBeans
     */
    public void setHirerAddMarkers(List<HomeMapBossListBean> homeMapBossListBeans) {
        this.homeMapBossListBeans = homeMapBossListBeans;
        clearEmployeeMarkers();//清空之前的数据
        for (int i = 0; i < homeMapBossListBeans.size(); i++) {
            HomeMapBossListBean bean = homeMapBossListBeans.get(i);
            LatLng latLng = new LatLng(bean.getyCoordinate(), bean.getxCoordinate());

            final View view = View.inflate(getContext(), R.layout.item_map_hire_img, null);
            final CircleImageView headImg = (CircleImageView) view.findViewById(R.id.iv_head_mark);
            Glide.with(getContext()).load(bean.getUserLogo())
                    .thumbnail(0.5f)
                    .placeholder(R.mipmap.icon_default)
                    .error(R.mipmap.icon_default).into(headImg);
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromView(view));
            Marker marker = mAMap.addMarker(options);
            marker.setZIndex(i);
            marker.setToTop();
            mMarkers.add(marker);
        }
    }

    /**
     * 清除所有订单信息Marker 不包括大头针和定位图标
     */
    public void clearEmployeeMarkers() {
        clickedPosition = -1;
        for (int i = 0; i < mMarkers.size(); i++) {
            mMarkers.get(i).remove();
            mMarkers.get(i).setAlpha(0);
        }
        mMarkers.clear();
    }

    /**
     * 移动到指定位置 缩放比例默认为15
     *
     * @param latLng
     */
    public void moveToLocation(LatLng latLng) {
        mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    /**
     * 添加我的位置
     *
     * @param myLatLng 我的位置经纬度
     */
    public Marker addMyMarker(LatLng myLatLng) {
        if (myLatLng != null) {
            MarkerOptions options = new MarkerOptions()
                    .position(myLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_head))
                    .visible(true);
            Marker marker = mAMap.addMarker(options);
            marker.setClickable(false);
            myMarker = marker;
            return marker;
        }
        return null;
    }

    /**
     * 移动到我的位置 地图点击按钮就移动到我的定位地点
     */
    public void toMyLocation() {
        if (myMarker != null) {
            moveToLocation(myMarker.getPosition());
        } else {
            Toast.makeText(getContext(), "未定位成功", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 移除我的位置Marker
     */
    public void removeMyMarker() {
        if (myMarker != null) {
            myMarker.remove();
        }
    }

    /**
     * 移动地图 停下来的监听 传递参数为null的时候 不能回调到停下来的相关数据
     *
     * @param onCameraChangeFinish
     */
    public void setOnCameraChangeFinish(final OnCameraChangeFinish onCameraChangeFinish) {
        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (onCameraChangeFinish != null)
                    onCameraChangeFinish.onMoveFinish(cameraPosition);
            }
        });
    }

    /**
     * 移动地图停下来的接口 该接口和上述一起使用
     */
    public interface OnCameraChangeFinish {
        void onMoveFinish(CameraPosition cameraPosition);
    }

    /**
     * 添加大头针
     */
    public Marker addPin() {
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_pin))
                .draggable(true);
        Marker marker = mAMap.addMarker(markerOptions);
        marker.setToTop();
        marker.setPositionByPixels(getWidth() / 2, getHeight() / 2);
        marker.setClickable(false);
        return marker;
    }

    /**
     * 大头针Marker 跳动动画
     */
    public void jumpPin(Marker pinMarker, LatLng centerLatlng) {
        clearEmployeeMarkers();//清空之前的数据
        Point point = mAMap.getProjection().toScreenLocation(centerLatlng);
        point.y -= StringUtil.dip2px(getContext(), 30);
        LatLng target = mAMap.getProjection().fromScreenLocation(point);

        //使用TranslateAnimation,填写一个需要移动的目标点
        Animation animation = new TranslateAnimation(target);
        animation.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                // 模拟重加速度的interpolator
                if (input <= 0.5) {
                    return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                } else {
                    return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                }
            }
        });
        //整个移动所需要的时间
        animation.setDuration(150);
        //设置动画
        pinMarker.setAnimation(animation);
        //开始动画
        pinMarker.startAnimation();
    }

    /**
     * 得到地图中心点位置 经纬度Latlng
     *
     * @return
     */
    public LatLng getCenterLatLng() {
        CameraPosition cameraPosition = mAMap.getCameraPosition();
        return cameraPosition.target;
    }

    /**
     * 清空地图上所有的marker 包括大头针和定位图标
     */
    public void clearAllMarkers() {
        mAMap.clear();
        myMarker = null;//初始化为空
    }

    /**
     * 地址反编译 得到地址
     *
     * @param centerPoint
     * @param
     */
    public void regeocodeSearched(LatLonPoint centerPoint, final TextView tvAddress) {
        if (geocoderSearch == null)
            geocoderSearch = new GeocodeSearch(getContext());
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(centerPoint, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
                        if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
                            List<PoiItem> pois = result.getRegeocodeAddress().getPois();
                            if (tvAddress != null) {
                                tvAddress.setText(pois.get(0).getTitle());
                            }
                        }
                    }
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
    }

    /**
     * 画一个圆形
     *
     * @param centerLatLng 圆心点Latlng经纬度
     * @param radius       圆半径
     */
    public void addCircleMarker(LatLng centerLatLng, int radius) {
        Circle circle = mAMap.addCircle(new CircleOptions().
                center(centerLatLng).
                radius(radius).    //半径一公里/单位米
                fillColor(Color.parseColor("#995CA6B9")).
                strokeColor(Color.parseColor("#9902add9")).
                strokeWidth(4));
    }

    /**
     * 获取起点和终点 线路
     * 起点到终点距离 >3000 驾车模式
     * 起点到终点距离 >1000 <3000 骑车模式
     * 起点到终点距离 小于1000米 步行模式
     *
     * @param startPoint
     * @param endPoint
     */
    public void setPath(final LatLonPoint startPoint, final LatLonPoint endPoint) {
        if (routeSearch == null) {
            routeSearch = new RouteSearch(getContext());
            routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
                @Override
                public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
                    List<BusPath> paths = busRouteResult.getPaths();
                    BusPath busPath = paths.get(0);
                    BusRouteOverlay busRouteOverlay = new BusRouteOverlay(getContext(), mAMap,
                            busPath, startPoint, endPoint);
                    busRouteOverlay.setNodeIconVisibility(false);
                    busRouteOverlay.removeFromMap();
                    busRouteOverlay.addToMap();
                    busRouteOverlay.zoomToSpan();
                }

                @Override
                public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                    DrivePath drivePath = driveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(getContext(),
                            mAMap, drivePath, startPoint, endPoint, null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    //显示不同路径的缩放比例
                    drivingRouteOverlay.zoomToSpan();
                }

                @Override
                public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
                    WalkPath walkPath = walkRouteResult.getPaths().get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(getContext(),
                            mAMap, walkPath, startPoint, endPoint);
                    walkRouteOverlay.setNodeIconVisibility(false);
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                }

                @Override
                public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
                    RidePath ridePath = rideRouteResult.getPaths().get(0);
                    RideRouteOverlay rideRouteOverlay = new RideRouteOverlay(getContext(),
                            mAMap, ridePath, startPoint, endPoint);
                    rideRouteOverlay.setNodeIconVisibility(false);
                    rideRouteOverlay.removeFromMap();
                    rideRouteOverlay.addToMap();
                    rideRouteOverlay.zoomToSpan();
                }
            });
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
            RouteSearch.DriveRouteQuery driveRouteQuery =
                    new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null, null, "");
            RouteSearch.RideRouteQuery rideRouteQuery = new RouteSearch.RideRouteQuery(fromAndTo);
            RouteSearch.WalkRouteQuery walkRouteQuery = new RouteSearch.WalkRouteQuery(fromAndTo);
            RouteSearch.BusRouteQuery busRouteQuery = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BusDefault
                    , null, 0);

            float distance = AMapUtils.calculateLineDistance(AMapUtil.convertToLatLng(startPoint),
                    AMapUtil.convertToLatLng(endPoint));
            if (distance >= 3000) {//驾车
                routeSearch.calculateDriveRouteAsyn(driveRouteQuery);
            } else if (distance < 3000 && distance > 1000) {//骑车
                routeSearch.calculateRideRouteAsyn(rideRouteQuery);
            } else if (distance <= 1000) {//步行
                routeSearch.calculateWalkRouteAsyn(walkRouteQuery);
            }
        }
    }

    /**
     * 地图点击事件
     */
    public void setMapClickListener(AMap.OnMapClickListener listener) {
        if (mAMap != null)
            mAMap.setOnMapClickListener(listener);
    }
}
