package gongren.com.dlg.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.common.utils.SharedPreferencesUtils;

import java.net.URLEncoder;

import gongren.com.dlg.R;
import gongren.com.dlg.activity.WorkOrderDetailActivity;
import gongren.com.dlg.javabean.DoingTaskOrderDetailModel;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class WorkMapManager {
    private AMap map;

    public WorkMapManager(AMap map) {
        this.map = map;
    }

    public void showTaskNearRange(LatLonPoint latLonPoint) {
        if (latLonPoint == null) {
            return;
        }
        map.addCircle(new CircleOptions().
                center(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude())).
                radius(1000).    //半径一公里/单位米
                fillColor(Color.parseColor("#995CA6B9")).
                strokeColor(Color.parseColor("#9902add9")).
                strokeWidth(8));
    }

    public Marker addTaskMark(Context context, DoingTaskOrderDetailModel doingTaskOrderDetailModel) {

        double x = Double.valueOf(doingTaskOrderDetailModel.xyCoordinateVo.jobXCoordinate);
        double y = Double.valueOf(doingTaskOrderDetailModel.xyCoordinateVo.jobYCoordinate);
        LatLng latLonPoint = new LatLng(y, x);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLonPoint, 13));
        return addNotInServiceTaskMark(context, doingTaskOrderDetailModel);
    }

    public Marker addNotInServiceTaskMark(Context context, DoingTaskOrderDetailModel doingTaskOrderDetailModel) {

        int demandType = doingTaskOrderDetailModel.demandType;
        String postTypeName = doingTaskOrderDetailModel.postTypeName;
        String price;
        if ("志愿义工".equals(postTypeName)) {
            price = "0\n元/单";
        } else {
            price = doingTaskOrderDetailModel.price + "\n元/" + doingTaskOrderDetailModel.meterUnitName;
        }


        return addNotInServiceTaskMark(context, Double.valueOf(doingTaskOrderDetailModel.xyCoordinateVo.jobXCoordinate),
                Double.valueOf(doingTaskOrderDetailModel.xyCoordinateVo.jobYCoordinate),
                price);
    }

    public Marker addNotInServiceTaskMark(Context context, double xOrdinates, double yOrdinates, String discribe) {
        View view = View.inflate(context, R.layout.item_map_order, null);
        TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
        tv_price.setText(discribe);
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromView(view)).position(new LatLng(yOrdinates, xOrdinates)).draggable(true);
        Marker marker = map.addMarker(markerOptions);
        if (context instanceof WorkOrderDetailActivity)
            marker.setClickable(false);
        return marker;
    }

    public Marker addDoingTaskMark(DoingTaskOrderDetailModel doingTaskOrderDetailModel) {
        double x = Double.valueOf(doingTaskOrderDetailModel.xyCoordinateVo.jobXCoordinate);
        double y = Double.valueOf(doingTaskOrderDetailModel.xyCoordinateVo.jobYCoordinate);
        LatLng latLonPoint = new LatLng(y, x);
        MarkerOptions markerOptions = new MarkerOptions().icon(
                BitmapDescriptorFactory.fromResource(R.mipmap.star))
                .position(latLonPoint)
                .title("")
                .snippet("")
                .draggable(true);
        return map.addMarker(markerOptions);
    }


    Marker minMarker;

    public Marker addMineMark(Context context,LatLng latLng) {
        if (minMarker != null) {
            minMarker.remove();
        }
        View inflate = LayoutInflater.from(context).inflate(R.layout.mark_mine, null);
        MarkerOptions markerOptions = new MarkerOptions().icon(
                BitmapDescriptorFactory.fromView(inflate))
                .position(latLng)
                .title("")
                .snippet("")
                .draggable(true);
        minMarker = map.addMarker(markerOptions);
        return minMarker;
    }

    public void moveMapTo(LatLng latLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    public void saveLoaction(Context context, AMapLocation aMapLocation) {
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.latitude, aMapLocation.getLatitude() + "");
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.longitude, aMapLocation.getLongitude() + "");
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.locationCity, aMapLocation.getCity());
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.locationProvince, aMapLocation.getProvince());
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.locationDistrict, aMapLocation.getDistrict());
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.locationAddress, aMapLocation.getAddress());
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.AdCode, aMapLocation.getAdCode());
    }

    public void startNavigate(Context context, String destination, double latitude, double longitude) {
        //1.判断用户手机是否安装高德地图APP
        boolean isInstalled = AMapUtil.isPkgInstalled("com.autonavi.minimap", context);
        //2.首选使用高德地图APP完成导航
        if (isInstalled) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("androidamap://navi?");
            try {
                //填写应用名称
                stringBuilder.append("sourceApplication=" + URLEncoder.encode("打零工", "utf-8"));
                //导航目的地
                stringBuilder.append("&poiname=" + URLEncoder.encode(destination, "utf-8"));
                //目的地经纬度
                stringBuilder.append("&lat=" + latitude);
                stringBuilder.append("&lon=" + longitude);
                stringBuilder.append("&dev=1&style=2");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //调用高德地图APP
            Intent intent = new Intent();
            intent.setPackage("com.autonavi.minimap");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setAction(Intent.ACTION_VIEW);
            //传递组装的数据
            intent.setData(Uri.parse(stringBuilder.toString()));
            context.startActivity(intent);
        } else {
            ToastUtils.showToastShort(context, "您没有装高德地图app,暂时不能导航");
        }
    }

    /**
     * 添加工作地址标记
     *
     * @param doingTaskOrderDetailModel
     */
    public Marker addWorkPlaceMarker(DoingTaskOrderDetailModel doingTaskOrderDetailModel) {
        if (doingTaskOrderDetailModel != null && doingTaskOrderDetailModel.xyCoordinateVo != null) {
            DoingTaskOrderDetailModel.XYCoordinateVo xyCoordinateVo = doingTaskOrderDetailModel.xyCoordinateVo;
            double y = Double.parseDouble(xyCoordinateVo.jobYCoordinate);//纬度
            double x = Double.parseDouble(xyCoordinateVo.jobXCoordinate);//经度

            MarkerOptions options = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_end))
                    .draggable(true)
                    .position(new LatLng(y, x));
            Marker marker = map.addMarker(options);
            marker.setToTop();//置顶
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(y, x), 13));
            return marker;
        }

        return null;

    }

    /**
     * 测量两个经纬度之间的距离
     *
     * @param myLatLng
     * @param workLatLng
     * @return
     */
    public float getCalculateLineDistance(LatLng myLatLng, LatLng workLatLng) {
        float distance = AMapUtils.calculateLineDistance(myLatLng, workLatLng);
        return distance;
    }
}
