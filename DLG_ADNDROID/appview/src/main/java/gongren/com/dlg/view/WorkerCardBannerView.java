package gongren.com.dlg.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.common.view.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.MainActivity;
import gongren.com.dlg.activity.WorkOrderDetailActivity;
import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.javabean.DoingTaskOrderDetailModel;
import gongren.com.dlg.javabean.ShareBean;
import gongren.com.dlg.javabean.base.ButtonListBean;
import gongren.com.dlg.javabean.worker.WorkerStatusBean;
import gongren.com.dlg.utils.ShareUtils;
import gongren.com.dlg.utils.WorkMapManager;
import gongren.com.dlg.utils.WorkbenchManager;
import gongren.com.dlg.volleyUtils.GetDataConfing;

/**
 * 作者：wangdakuan
 * 主要功能：
 * 创建时间：2017/6/6 17:07
 */
public abstract class WorkerCardBannerView implements Holder<Object> {
    private View mView;
    private ViewHolder viewHolder;
    private ShareAction mShareAction = null;
    private LatLng myLatlng;
    private LatLonPoint mEndPoint;
    private boolean isWithinArea = false;
    private WorkMapManager workMapManager;
    private AMap map;
    private TextView titleView;
    private Marker startNavigationmarker, endNavigationmarker;
    private RouteSearch mRouteSearch;

    public abstract void deleteItem(int position);

    @Override
    public View createView(Context context) {
        //布局
        mView = LayoutInflater.from(context).inflate(R.layout.view_worker_card_banner, null);
        viewHolder = new ViewHolder(mView);
        return mView;
    }

    @Override
    public void UpdateUI(final Context context, final int position, Object data) {

        DoingTaskOrderDetailModel orderDetailModel = null;
        if (data instanceof DoingTaskOrderDetailModel) {
            orderDetailModel = (DoingTaskOrderDetailModel) data;
            getShareData((Activity) context, orderDetailModel.jobId);
        }
        setEndPoint(orderDetailModel);
        viewHolder.doingTaskDetailCardView.initViewData(orderDetailModel);
        isWithinArea = false;//初始化
        viewHolder.doingTaskDetailCardView.setIsWithIn(false);//初始化为false 防止二次冲突
        double y = Double.parseDouble(orderDetailModel.xyCoordinateVo.jobYCoordinate);
        double x = Double.parseDouble(orderDetailModel.xyCoordinateVo.jobXCoordinate);
        final LatLng workPlace = new LatLng(y, x);
        if (workMapManager.getCalculateLineDistance(myLatlng, workPlace) < MyApplication.dataValueDistance) {//测量雇员位置和工作地址距离 在工作范围内
            isWithinArea = true;//在工作范围内
            viewHolder.doingTaskDetailCardView.setIsWithIn(isWithinArea);
        }
        viewHolder.doingTaskDetailCardView.setWorkerorder_title(titleView);
        final DoingTaskOrderDetailModel finalOrderDetailModel = orderDetailModel;
        viewHolder.doingTaskDetailCardView.setRefreshRequest(new DoingTaskDetailCardView.OperateRequestCallBack() {
            @Override
            public void OnRefresh(ButtonListBean dataBean) {//从DoingTaskDetailCardView中的回调 倒计时结束所执行的操作
                if (null != dataBean) {
                    if (dataBean.nextStatusCode == 0 && context instanceof MainActivity) {//知道了的时候
                        //回到工作台
                        deleteItem(position);
//                        MainToWorkerFragmentEvent event = new MainToWorkerFragmentEvent("", 1);
//                        EventBus.getDefault().post(event);
                    } else if (dataBean.nextStatusCode == 0 && context instanceof WorkOrderDetailActivity) {
                        ((WorkOrderDetailActivity) context).finish();
                    }

                    if (context instanceof MainActivity) {
                        if ((dataBean.operateStatusCode == 24 && dataBean.nextStatusCode == 22)
                                || dataBean.operateStatusCode == 22) {
                            viewHolder.doingTaskDetailCardView.getTaskButtons();
                        } else { //operateStatusCode 3、23、24、2
                            deleteItem(position);
                        }
                    }

                } else {
                    viewHolder.doingTaskDetailCardView.getTaskButtons();
                }

            }

            @Override
            public void OnStateUpdateCallBack(int state, WorkerStatusBean.DataBean dataBean) {
                int operateStatusCode = dataBean.buttonList.get(0).operateStatusCode;
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                switch (operateStatusCode) {
                    case 10:  //等待雇主同意
                    case 11://雇主已拒绝
                    case 20:  //同意邀请
                    case 0:
                    case 1:
                        Marker marker = workMapManager.addTaskMark(context, finalOrderDetailModel);
                        if(marker!=null){
                            builder.include(marker.getPosition());
                            marker.setClickable(false);//展示工作地址展示的Marker 带有价格的图像
                        }
                        Marker marker1 = workMapManager.addMineMark(context, myLatlng);
                        if(marker1!=null){
                            builder.include(marker1.getPosition());
                            marker1.setClickable(false);//展示我的位置
                        }
                        break;
                    case 21:  //开工打开或开工打卡迟到倒计时
                        if (!isWithinArea) {
                            setNavigation(context,builder);//显示导航路线
                        }
                    case 22:  //正在干活，未到完成时间
                    case 23:  //正在干活，已到完成时间
                    case 24://计件正在干活中 已到完成时间
                    case 2://再来一单
                        LatLng latLng = showTaskNearRange();
                        if(latLng!=null){
                            builder.include(latLng);
                        }
                        Marker marker2 = workMapManager.addMineMark(context, myLatlng);
                        if(marker2!=null){
                            marker2.setClickable(false);//
                            builder.include(marker2.getPosition());
                        }

                        Marker marker3 = workMapManager.addWorkPlaceMarker(finalOrderDetailModel);//展示我的工作地址位置
                        if(marker3!=null){
                            builder.include(marker3.getPosition());
                        }
                        break;

                }
                titleView.setText(dataBean.statusText);
                map.setMinZoomLevel(12);
                map.setMaxZoomLevel(14);
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
            }
        });
    }

    public void setLatlng(LatLng myLatlng, TextView titleView) {
        this.myLatlng = myLatlng;
        this.titleView = titleView;
    }

    public void setWorkMapManager(WorkMapManager workMapManager, AMap map, RouteSearch mRouteSearch) {
        this.workMapManager = workMapManager;
        this.map = map;
        this.mRouteSearch = mRouteSearch;
    }

    private void getShareData(final Activity activity, String jobId) {
        //获取分享的内容
        WorkbenchManager.getShareData(activity, GetDataConfing.SHARE_DATA,
                jobId, new WorkbenchManager.StringCallBack() {
                    @Override
                    public void onFinish(String json) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if ("0".equals(jsonObject.optString("code"))) {
                                ShareBean shareBean = new Gson().fromJson(json, ShareBean.class);
                                if (shareBean != null && shareBean.getData() != null && shareBean.getData().size() > 0) {
                                    ShareBean.DataBean dataBean = shareBean.getData().get(0);
                                    String tital = dataBean.getTaskTitle();
                                    String url = dataBean.getDetailsUrl();
                                    String taskDescription = dataBean.getTaskDescription();
                                    String userLogo = dataBean.getUserLogo();
                                    mShareAction = ShareUtils.setUMShareAction(activity,
                                            tital, taskDescription, url, userLogo);
                                    viewHolder.doingTaskDetailCardView.setShareAction(mShareAction);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 展示打卡半径
     */
    private LatLng showTaskNearRange() {
        if (mEndPoint == null) {
            return null;
        }
        map.animateCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(mEndPoint.getLatitude(),
                        mEndPoint.getLongitude()), 15));
        Circle circle = map.addCircle(new CircleOptions().
                center(new LatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude())).
                radius(1000).    //半径一公里/单位米
                fillColor(Color.parseColor("#995CA6B9")).
                strokeColor(Color.parseColor("#9902add9")).
                strokeWidth(8));
        return circle.getCenter();
    }

    /**
     * 显示定位路线 起始路线
     */
    private void setNavigation(final Context context, LatLngBounds.Builder b) {
        if (myLatlng == null || mEndPoint == null)
            return;
        LatLonPoint startPoint = new LatLonPoint(myLatlng.latitude, myLatlng.longitude);
        map.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_map_infowindow_jijian, null);
                TextView infoWindow = (TextView) view.findViewById(R.id.tv_count);
                infoWindow.setText("导航");
                infoWindow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        workMapManager.startNavigate(context, "", mEndPoint.getLatitude(), mEndPoint.getLongitude());
                    }
                });
                return view;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        MarkerOptions startmarkerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_start))
                .position(new LatLng(startPoint.getLatitude(), startPoint.getLongitude()))
                .title("")
                .snippet("")
                .draggable(true);
        startNavigationmarker = map.addMarker(startmarkerOptions);
        b.include(startNavigationmarker.getPosition());
        MarkerOptions endmarkerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_end))
                .position(new LatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude()))
                .title("")
                .snippet("")
                .draggable(true);

        endNavigationmarker = map.addMarker(endmarkerOptions);
        b.include(endNavigationmarker.getPosition());
        startNavigationmarker.showInfoWindow();

        //显示路线
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                startPoint, mEndPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo,
                RouteSearch.DrivingDefault, null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，
        // 第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询

    }

    static class ViewHolder {
        @Bind(R.id.doing_task_detail_card)
        DoingTaskDetailCardView doingTaskDetailCardView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private void setEndPoint(DoingTaskOrderDetailModel doingTaskOrderDetailModel) {
        if (doingTaskOrderDetailModel.xyCoordinateVo == null) {
            return;
        }
        map.clear();
        double x = Double.valueOf(doingTaskOrderDetailModel.xyCoordinateVo.jobXCoordinate);
        double y = Double.valueOf(doingTaskOrderDetailModel.xyCoordinateVo.jobYCoordinate);
        mEndPoint = new LatLonPoint(y, x);
    }
}
