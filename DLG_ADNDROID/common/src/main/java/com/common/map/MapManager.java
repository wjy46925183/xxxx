package com.common.map;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;

import java.util.List;

/**
 * Created by Wangjinya on 2017/6/9.
 * 高德地图 工具类
 */

public class MapManager {

    public static AMapLocationClient aMapLocationClient;
    /**
     * 开启定位 静态方法
     *
     * @param listener
     */
    public static void startLocation(Context context, AMapLocationListener listener) {
        aMapLocationClient = new AMapLocationClient(context);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位参数
        aMapLocationClient.setLocationOption(mLocationOption);
        aMapLocationClient.setLocationListener(listener);
        aMapLocationClient.startLocation();
    }
    /**
     * 关闭定位
     */
    public static void stopLocation(){
        if(aMapLocationClient != null){
            aMapLocationClient.stopLocation();
        }
    }

//    /**
//     * 画一个圆形
//     */
//    public void addCircleMarker(LatLng centerLatLng,
//                                int radius, String fillColor, String strokeColor, int strokeWidth) {
//        Circle circle = mAMap.addCircle(new CircleOptions().
//                center(centerLatLng).
//                radius(radius).    //半径一公里/单位米
//                fillColor(Color.parseColor(fillColor)).
//                strokeColor(Color.parseColor(strokeColor)).
//                strokeWidth(strokeWidth));
//    }
//
//    /**
//     * 地图移动到指定经纬度
//     */
//    public void moveTo(LatLng latLng, int zoomLevel) {
//        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
//    }
//
//    /**
//     * 地图自适应缩放
//     */
//    public void selfZoom(LatLng... latLngs) {
//        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
//        for (int i = 0; i < latLngs.length; i++) {
//            boundsBuilder.include(latLngs[i]);
//        }
//
//        mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50));
//    }
//
//    /**
//     * 地图自适应缩放
//     */
//    public void selfZoom(@NonNull List<LatLng> latLngs, int paddingScreen) {
//        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
//        for (int i = 0; i < latLngs.size(); i++) {
//            boundsBuilder.include(latLngs.get(i));
//        }
//        mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), paddingScreen));
//    }
//


    /**
     * 搜索周边热点
     *
     * @param searchText
     */
    public static void searchNearPoi(Context context, String searchText, String city, final NearPoiCallBack callBack) {
        if (searchText != null && city != null) {
            InputtipsQuery inputquery = new InputtipsQuery(searchText, city);
            Inputtips inputTips = new Inputtips(context, inputquery);
            inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
                @Override
                public void onGetInputtips(List<Tip> list, int i) {
                    if (callBack != null) {
                        callBack.nearPoi(list);
                    }
                }
            });
            inputTips.requestInputtipsAsyn();
        }
    }

    /**
     * 周边热点回调接口
     */
    public interface NearPoiCallBack {
        void nearPoi(List<Tip> tips);
    }
}
