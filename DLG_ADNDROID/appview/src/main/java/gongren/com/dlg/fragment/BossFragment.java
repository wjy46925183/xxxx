package gongren.com.dlg.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.AddressSearcherActivity;
import gongren.com.dlg.activity.LoginDialogActivity;
import gongren.com.dlg.activity.MainActivity;
import gongren.com.dlg.activity.RealNameAuthenticationActivity;
import gongren.com.dlg.activity.ReleaseXQActivity;
import gongren.com.dlg.adapter.MapWorkerPagerAdapter;
import gongren.com.dlg.adapter.MapWorkerPagerAdapterV;
import gongren.com.dlg.adapter.OrderPopListAdapter;
import gongren.com.dlg.adapter.OrderPopListAdapterV;
import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.javabean.MainEvent;
import gongren.com.dlg.javabean.MainToBossFragmentEvent;
import gongren.com.dlg.javabean.OrderCreateInfo;
import gongren.com.dlg.javabean.TaskOfOrderInfo;
import gongren.com.dlg.javabean.UserInfoSobot;
import gongren.com.dlg.javabean.master.BossMapMsgBean;
import gongren.com.dlg.utils.DataCacheUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.DimensUtils;
import gongren.com.dlg.utils.IntegerUtils;
import gongren.com.dlg.utils.OrderToast;
import gongren.com.dlg.utils.SobotUtils;
import gongren.com.dlg.utils.WorkMapManager;
import gongren.com.dlg.view.MyListView;
import gongren.com.dlg.view.MyViewPager;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

import static com.amap.api.maps.AMapUtils.calculateLineDistance;
import static com.umeng.socialize.Config.dialog;
import static gongren.com.dlg.utils.IntegerUtils.API_TAG_BOSS_TASKING_LIST;

/**
 * 雇主的首页
 * Created by zhangpei on 2017/3/22.
 */
public class BossFragment extends BaseFragment implements LocationSource, AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnMarkerClickListener, AMap.OnCameraChangeListener {
    @Bind(R.id.top_title)
    TextView top_title;
    @Bind(R.id.homeboss_mapView)
    TextureMapView homebossMapView;
    @Bind(R.id.homeboss_iv_sobot)
    ImageView homebossIvSobot;
    @Bind(R.id.homeboss_iv_location)
    ImageView homebossIvLocation;
    @Bind(R.id.homeboss_rb_workday)
    TextView homebossRbWorkday;
    @Bind(R.id.homeboss_rb_week)
    TextView homebossRbWeek;
    @Bind(R.id.homeboss_rb_project)
    TextView homebossRbProject;
    @Bind(R.id.homeboss_rg)
    LinearLayout homebossRg;
    @Bind(R.id.homeboss_tv_address)
    TextView homebossTvAddress;
    @Bind(R.id.homeboss_tv_xuqiu)
    TextView homebossTvXuqiu;
    @Bind(R.id.homeboss_layout_xuqiu)
    LinearLayout homebossLayoutXuqiu;
    @Bind(R.id.homeboss_btn_xuqiu)
    LinearLayout homebossBtnXuqiu;

    @Bind(R.id.homeboss_layout_address)
    LinearLayout homebossLayoutAddress;
    @Bind(R.id.homeboss_vp)
    MyViewPager homebossVp;
    //    @Bind(R.id.homeboss_vp_order)
    MyViewPager homebossVpOrder;

    @Bind(R.id.layout_vp_order)
    LinearLayout layoutVpOrder;
    @Bind(R.id.homeboss_layout_bottom)
    RelativeLayout homebossLayoutBottom;

    @Bind(R.id.bossmap_sobotandlocation)
    LinearLayout bossmap_sobotandlocation;
    //    @Bind(R.id.bossmap_layout_order)
//    LinearLayout bossmap_layout_order;
    @Bind(R.id.homeboss_layout_listandorder)
    LinearLayout homeboss_layout_listandorder;

    @Bind(R.id.boss_slid_layout)
    LinearLayout boss_slid_layout;
    @Bind(R.id.homeboss_iv_ongoingorder)
    CircleImageView homeboss_iv_ongoingorder;
    public static final int FROM_ADDRESS = 12;
    private AMap amap;
    private PopupWindow OrderPop;
    private OrderPopListAdapter orderPopListAdapter;
    private OrderPopListAdapterV orderPopListAdapterV;
    private String locationAddress;
    private String city;
    private String postType = "", demandType = "1";
    private View detailView = null;
    private final int TAG_REQUEST_TASK_LIST = 101;//查询根据任务和订单状态查询订单列表
    private LatLng mLocalLatlng;       //定位到本地的经纬度
    private GeocodeSearch geocoderSearch;
    private boolean isCardIsLook;//卡片是否可以见 默认不可见
    private boolean isShow = false;
    private boolean isOrderMode = false;//是否是订单模式
    boolean mHasDoingTask = false;
    private TaskOfOrderInfo dataOrder;
    private List<TaskOfOrderInfo.DataBean> listOrder = new ArrayList<>();
    private int viewPageIndex = 0;
    boolean bool = false;//是否有正在进行中的订单
    private static final int TAG_USERINFO = 2;      //请求用户信息
    private String isSwitchJoe = "";//是否左侧栏点击切换雇员雇主，默认不是
    MapWorkerPagerAdapterV adapter;
    private boolean isFirstGetAdr = true;
    private String jobIds;
    private int tag;
    private WorkMapManager mWorkMapManager;
    //    private Dialog mLoadingDialog;
    private MainActivity mMainActivity;

    public void setPostType(String postType) {
        this.postType = postType;
    }

    //上级Activity发来的消息
    @Subscribe
    public void onMessageEvent(MainToBossFragmentEvent event) {
        tag = event.getTag();
        switch (event.getTag()) {
            case 0:
                postType = event.getMsg();
                isOrderMode = true;
                locationMarker1 = null;
                ActingOrderModel();
                break;
            case 1:
                postType = event.getMsg();
                isOrderMode = false;
                locationMarker1 = null;
                OutOrderModel();
                break;
            case 2:
                ////点击零工类型导航条
                postType = event.getMsg();
                if (!isOrderMode)
                    loadMapsDatas();
                break;
            case 3:
                //显示弹窗
                if (event.getMsg().equals("打开")) {
                    showOrderPop();
                } else {
                    closeOrderPop();
                }
                break;

            case 4:
                //登录成功
                isDoingOrder();
                if (latLonPoint == null)
                    break;
                if (!isOrderMode)
                    loadMapsDatas();
            case 10://当前没有进行中的订单
                //退出订单
                isOrderMode = false;
                EventBus.getDefault().post(new MainEvent("", 1));
                OutOrderModel();
                //点击地图关闭打开的零工信息卡片
                mHasDoingTask = false;
                isDoingOrder();
                if (detailView != null)
                    detailView.setVisibility(View.GONE);
                break;
            case FROM_ADDRESS://自己选择完地址以后返回首页 大头针跳到指定地址 并加载数据
                String msg = event.getMsg();
                double latitude = Double.parseDouble(msg.split(",")[0]);
                double longitude = Double.parseDouble(msg.split(",")[1]);
                amap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));
                loadMapsDatas();
                break;
            case 21:
                String msg1 = event.getMsg();
                if (msg1 != null) {
                    int i = Integer.parseInt(msg1);
                    datas.remove(i);
                }
                jobIds = null;
                break;
            case 200:
                loadData();
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_boss, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        homebossMapView.onCreate(savedInstanceState);// 此方法必须重写
        mMainActivity = (MainActivity) activity;
        initView();
        initListener();
        if (!StringUtils.isLogin(activity)) {
            startLocation();//如果是游客身份 直接开始定位 不进行是否有进行中的任务的验证
        } else {
            isDoingOrder();
        }
        return view;
    }

    /**
     * 初始化界面
     */
    private void initView() {
        initMap();
        isCardIsLook = false;
        layoutVpOrder.setVisibility(View.GONE);
        homebossVp.setVisibility(View.GONE);
        //通知主页面显示第一行数据
        if (getActivity() instanceof MainActivity) {
            Message mesg = new Message();
            mesg.what = IntegerUtils.BOSSORDER_SELECT;
            ((MainActivity) getActivity()).getHandler().sendMessage(mesg);
        }
    }

    private void initMap() {
        amap = homebossMapView.getMap();
        mWorkMapManager = new WorkMapManager(amap);
//        amap.setMapType(AMap.MAP_TYPE_NAVI);
        UiSettings uiSettings = amap.getUiSettings();
        uiSettings.setLogoBottomMargin(-60);
        uiSettings.setZoomControlsEnabled(false);     //隐藏缩放按钮
        uiSettings.setScaleControlsEnabled(true);      //显示比例尺
        uiSettings.setTiltGesturesEnabled(false);       //倾斜手势
        uiSettings.setRotateGesturesEnabled(false);     //旋转手势
    }


    private void initListener() {
        amap.setLocationSource(this);// 设置定位监听
        amap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        amap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
        amap.setOnMarkerClickListener(this);   //对地图标注点击监听
        amap.setOnCameraChangeListener(this);       //对amap添加移动地图事件监听器
        //地址反编码
        if (geocoderSearch == null) {
            geocoderSearch = new GeocodeSearch(activity);
        }
        amap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // if (isOrderMode && !isFromOrder) {

                if (!isShow) {//卡片模式进入有地址栏模式
                    if (isCardIsLook && !isOrderMode) {//卡片可见的时候 隐藏 并且不是订单模式
                        homebossVp.setVisibility(View.GONE);
                        layoutVpOrder.setVisibility(View.GONE);
                        isCardIsLook = false;//至为false 防止再次加载网络数据 出现地图卡顿的想象
                        // }
                        homebossRg.setVisibility(View.VISIBLE);//工作类型选择控件：1.工作日 2.双休日3.计件
                        bossmap_sobotandlocation.setVisibility(View.VISIBLE);//客服和定位控件
                        //if(!isOrderMode){
                        homebossLayoutAddress.setVisibility(View.VISIBLE);
                        locationMarker1 = null;
                        OutOrderModelCard();
                        setImgBigMarker(null);
                        if (lastMarker != null) {
                            lastMarker.remove();
                        }
                    }
                }

                if (isOrderMode) {//订单模式进入非订单模式
                    //退出订单
                    OutOrderModel();
                    isOrderMode = false;
//                    startLocation();
//                    startJumpAnimation(true);
                    EventBus.getDefault().post(new MainEvent("", 1));
                }
                //点击地图关闭打开的零工信息卡片
                if (detailView != null)
                    detailView.setVisibility(View.GONE);
            }
        });
        geocoderSearch.setOnGeocodeSearchListener(this);
        homebossVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (markerList != null && markerList.size() > 0) {
                    setImgBigMarker(markerList.get(position));
                    if (detailView != null)
                        detailView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //进入菜单
        homeboss_iv_ongoingorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        //超过40dp触发事件
                        if (DimensUtils.getScreenWith(activity) - event.getRawX() > 300) {
                            boss_slid_layout.setPadding(DimensUtils.dip2px(activity, 2), DimensUtils.dip2px(activity, 2),
                                    (int) 2.5 * DimensUtils.px2dip(activity, 60)
                                    , DimensUtils.dip2px(activity, 2));
                            //进入订单页面
                            //EventBus.getDefault().post(new MainEvent("", 4));
                            isShow = true;
                            //ActingOrderModel();
                        }
                        //滑动在10dp和40dp之间
                        if (DimensUtils.getScreenWith(activity) - event.getRawX() >= 50 &&
                                DimensUtils.getScreenWith(activity) - event.getRawX() <= 300) {
                            boss_slid_layout.setPadding(DimensUtils.dip2px(activity, 2), DimensUtils.dip2px(activity, 2),
                                    (int) (2.5 * DimensUtils.px2dip(activity, (float)
                                            (DimensUtils.getScreenWith(activity) - event.getRawX())))
                                    , DimensUtils.dip2px(activity, 2));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (DimensUtils.getScreenWith(activity) - event.getRawX() >= 50 &&
                                DimensUtils.getScreenWith(activity) - event.getRawX() <= 300) {
                            boss_slid_layout.setPadding(DimensUtils.dip2px(activity, 2), DimensUtils.dip2px(activity, 2),
                                    (int) 2.5 * DimensUtils.px2dip(activity, 60)
                                    , DimensUtils.dip2px(activity, 2));
                        }
                        ActingOrderModel();
                        break;
                }
                return true;
            }
        });
    }

    //进入订单模式 地图模式进入订单模式
    private void ActingOrderModel() {
//        mLoadingDialog = DialogUtils.showLoadingDialog(activity);

        amap.clear();//清空地图上面所有的图标
        if (markerList != null) {
            for (int i = 0; i < markerList.size(); i++) {
                markerList.get(i).remove();
            }
            markerList.clear();
        }
        if (lastMarker != null) {
            lastMarker.remove();
        }
        if (locationMarker != null) {
            locationMarker.remove();
        }

        homeboss_layout_listandorder.setVisibility(View.GONE);
        //bossmap_layout_order.setVisibility(View.VISIBLE);
        homebossRg.setVisibility(View.GONE);
        bossmap_sobotandlocation.setVisibility(View.GONE);
        homebossLayoutAddress.setVisibility(View.GONE);
        isCardIsLook = false;
        homebossVp.setVisibility(View.GONE);
        layoutVpOrder.setVisibility(View.VISIBLE);
        isOrderMode = true;
        EventBus.getDefault().post(new MainEvent("", 4));
        loadData();
    }

    private void loadData() {
        if (StringUtils.isLogin(activity)) {
            String userId = SharedPreferencesUtils.getString(getContext(), SharedPreferencesUtils.USERID);
            getJobTaskOfOrderList(userId);
        }
    }

    /**
     * 1.1.6（REST）首页-雇主-有人接单、等待验收-列表
     *
     * @param userId
     */
//    Dialog dialog;

    private void getJobTaskOfOrderList(String userId) {
        if (isShow) {
//            dialog = DialogUtils.showLoadingDialog(activity);
            isShow = false;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("format", "json");
        DataUtils.loadData(activity, GetDataConfing.BOSS_IS_TASKING_LIST, map, API_TAG_BOSS_TASKING_LIST, responseDataCallback);

    }


    //退出订单模式
    private void OutOrderModelCard() {
        if (null != locationMarker) {
            locationMarker.setToTop();
            locationMarker.setPositionByPixels(homebossMapView.getWidth() / 2, homebossMapView.getHeight() / 2);
        }
        if (mLocalLatlng != null)
            mWorkMapManager.addMineMark(activity, mLocalLatlng);
        isShow = false;
//        setLocationAddress();
        if (mLocalLatlng != null) {
        }
        //初始化数据
//        clearDatas();
        homeboss_layout_listandorder.setVisibility(View.VISIBLE);
        layoutVpOrder.setVisibility(View.GONE);
        homebossRg.setVisibility(View.VISIBLE);
        bossmap_sobotandlocation.setVisibility(View.VISIBLE);
        homebossLayoutAddress.setVisibility(View.VISIBLE);
        isOrderMode = false;

        if (mHasDoingTask) {
            boss_slid_layout.setVisibility(View.VISIBLE);
        } else {
            boss_slid_layout.setVisibility(View.GONE);
        }
    }

    //退出订单模式
    private void OutOrderModel() {
        amap.clear();
        if (mLocalLatlng != null)
            mWorkMapManager.addMineMark(activity, mLocalLatlng);
        isShow = false;
        setLocationAddress();
        if (mLocalLatlng != null && tag != FROM_ADDRESS) {
            amap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocalLatlng, 14));
        }
        tag = -1;
        //初始化数据
        clearDatas();
        homeboss_layout_listandorder.setVisibility(View.VISIBLE);
        layoutVpOrder.setVisibility(View.GONE);
        homebossRg.setVisibility(View.VISIBLE);
        bossmap_sobotandlocation.setVisibility(View.VISIBLE);
        homebossLayoutAddress.setVisibility(View.VISIBLE);
        isOrderMode = false;

        if (mHasDoingTask) {
            boss_slid_layout.setVisibility(View.VISIBLE);
        } else {
            boss_slid_layout.setVisibility(View.GONE);
        }
    }

    private void clearDatas() {

        for (int i = 0; i < markerList.size(); i++) {
            markerList.get(i).remove();
        }
        markerList.clear();
    }
    private boolean isLocationAddress = false;
    //初始化假数据
    private List<Marker> markerList = new ArrayList<>();
    private void setDatas(final BossMapMsgBean dataBean) {
        for (int i = 0; i < markerList.size(); i++) {
            Marker marker = markerList.get(i);
            marker.remove();//将地图上面所有的雇主信息移除
        }

        if (lastMarker != null) {
            lastMarker.remove();
        }
        lastMarker = null;
        markerList.clear();

        for (int i = 0; i < dataBean.data.size(); i++) {

            final View view = View.inflate(activity, R.layout.item_map_order_img, null);
            final CircleImageView headImg = (CircleImageView) view.findViewById(R.id.iv_head_mark);
            BossMapMsgBean.DataBean dataBean1 = dataBean.data.get(i);
            LatLng latLng = new LatLng(dataBean1.yCoordinate, dataBean1.xCoordinate);
            double distance = calculateLineDistance(latLng, mLocalLatlng) / 1000;

            dataBean1.distance = String.format("%.2f", distance);

            String userLogo = dataBean1.userLogo;
            Glide.with(activity)
                    .load(userLogo)
                    .asBitmap()
                    .thumbnail(0.1f)
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .error(R.mipmap.ic_launcher)
                    .into(headImg);
            MarkerOptions markerOptions = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromView(view))
                    .position(latLng)
                    .visible(true)
                    .draggable(false);
            Marker marker = amap.addMarker(markerOptions);
            marker.setZIndex(i);
            markerList.add(marker);
        }

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度
        for (int i = 0; i < markerList.size(); i++) {
            boundsBuilder.include(markerList.get(i).getPosition());//把所有点都include进去（LatLng类型）
        }
//        amap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50));//第二个参数为四周留空宽度
//        amap.setMaxZoomLevel(12);//最大缩放标准设置为14
        MapWorkerPagerAdapter adapter = new MapWorkerPagerAdapter(this.getFragmentManager(), dataBean, new Handler());
        homebossVp.setAdapter(adapter);
        homebossVp.setOffscreenPageLimit(0);
        isLocationAddress = true;
        setLocationAddress();
    }

    private UserInfoSobot userInfoSobot;     //用户信息实体类

    @OnClick({R.id.homeboss_iv_sobot, R.id.homeboss_iv_location, R.id.homeboss_layout_xuqiu,
            R.id.homeboss_layout_address, R.id.homeboss_iv_list, R.id.homeboss_rb_workday, R.id.homeboss_rb_week, R.id.homeboss_rb_project,
            R.id.homeboss_iv_ongoingorder, R.id.homeboss_tv_address, R.id.homeboss_btn_xuqiu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeboss_iv_sobot:
                if (StringUtils.isLogin(activity)) {//客服
                    //启动客服
                    SobotUtils.startSobot(activity, userInfoSobot);
                } else {
                    startActivity(new Intent(activity, LoginDialogActivity.class));
                }
                break;
            case R.id.homeboss_iv_location:
                amap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocalLatlng, 14));
                break;
            case R.id.homeboss_layout_xuqiu:
                break;
            case R.id.homeboss_layout_address:

                break;
            case R.id.homeboss_iv_list:
                EventBus.getDefault().post(new MainEvent("", 3));
                break;
            case R.id.homeboss_iv_ongoingorder:
                //是否进入了订单

                break;

            case R.id.homeboss_tv_address:
                Intent intent2 = new Intent(activity, AddressSearcherActivity.class);
                if (latLonPoint != null) {
                    intent2.putExtra("lat", latLonPoint.getLatitude());
                    intent2.putExtra("lon", latLonPoint.getLongitude());
                    intent2.putExtra("address", locationAddress);
                    intent2.putExtra("city", city);
                }
                startActivity(intent2);
                break;

            case R.id.homeboss_btn_xuqiu:
                if (StringUtils.isLogin(activity)) {
                    if (SharedPreferencesUtils.getString(activity,
                            SharedPreferencesUtils.RENZHENG_STATUS).equals("2")) {
                        //发布零工
                        Intent intent = new Intent(activity, ReleaseXQActivity.class);
                        startActivity(intent);
                    } else {
                        DialogUtils.showSimpleDialog(activity, "提示", "未认证身份证信息,去认证", new DialogUtils.ConfirmCallback() {
                            @Override
                            public void confirm(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                activity.startActivity(new Intent(activity, RealNameAuthenticationActivity.class));
                            }
                        });
                    }

                } else {
                    startActivity(new Intent(activity, LoginDialogActivity.class));
                }

                break;

            case R.id.homeboss_rb_workday:
                if (!homebossRbWorkday.isSelected()) {
                    homebossRbWorkday.setSelected(true);
                    homebossRbWeek.setSelected(false);
                    homebossRbProject.setSelected(false);
                    demandType = "1";
                } else {
                    demandType = "";
                    homebossRbWorkday.setSelected(false);
                }
                loadMapsDatas();
                break;
            case R.id.homeboss_rb_week:
                if (!homebossRbWeek.isSelected()) {
                    homebossRbWorkday.setSelected(false);
                    homebossRbWeek.setSelected(true);
                    homebossRbProject.setSelected(false);
                    demandType = "2";
                } else {
                    demandType = "";
                    homebossRbWeek.setSelected(false);
                }
                loadMapsDatas();
                break;
            case R.id.homeboss_rb_project:
                if (!homebossRbProject.isSelected()) {
                    homebossRbWorkday.setSelected(false);
                    homebossRbWeek.setSelected(false);
                    homebossRbProject.setSelected(true);
                    demandType = "3";
                } else {
                    demandType = "";
                    homebossRbProject.setSelected(false);
                }
                loadMapsDatas();
                break;
        }
    }

    /**
     * 附近的雇员设置成点击状态的Marker
     *
     * @param marker
     */
    private Marker lastMarker;

    private void setImgBigMarker(Marker marker) {
        //清除全部
        if (lastMarker != null) {
            int zIndex = (int) lastMarker.getZIndex();
            BossMapMsgBean.DataBean dataBean = bossMapMsgBean.data.get(zIndex);
            View view = View.inflate(activity, R.layout.item_map_order_img, null);
            CircleImageView headImg = (CircleImageView) view.findViewById(R.id.iv_head_mark);
            Glide.with(activity)
                    .load(dataBean.userLogo)
                    .asBitmap()
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .error(R.mipmap.ic_launcher)
                    .into(headImg);
            lastMarker.setIcon(BitmapDescriptorFactory.fromView(view));
        }
        if (null != marker) {
            int zIndex = (int) marker.getZIndex();
            BossMapMsgBean.DataBean dataBean = bossMapMsgBean.data.get(zIndex);
            final View bigView = View.inflate(activity, R.layout.item_map_order_img_big, null);
            final ImageView ivHead = (ImageView) bigView.findViewById(R.id.iv_head);
            Glide.with(activity)
                    .load(dataBean.userLogo)
                    .asBitmap()
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .error(R.mipmap.ic_launcher)
                    .into(ivHead);
            marker.setIcon(BitmapDescriptorFactory.fromView(bigView));
            marker.setToTop();
            startScaleBigAnimation(marker);

            lastMarker = marker;
            lastMarker.setToTop();
        }
    }

    /**
     * 显示订单数量的列表
     */
    private void showOrderPop() {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.pop_title_orderlist, null);
        //显示条目
        MyListView lv = (MyListView) contentView.findViewById(R.id.pop_orderlv);
//        String orderinfo = SharedPreferencesUtils.getString(activity, "orderinfo");
//        taskOfOrderInfo = new Gson().fromJson(orderinfo, TaskOfOrderInfo.class);
        listOrder.clear();
        listOrder.addAll(taskOfOrderInfo.getData());
        //获取屏幕的宽高
        OrderPop = new PopupWindow(contentView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        //设置气泡的背景
        OrderPop.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        OrderPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().post(new MainEvent("", 6));
            }
        });
        //设置气泡挂载的位置
        OrderPop.showAsDropDown(top_title);
        orderPopListAdapterV = new OrderPopListAdapterV(activity, listOrder, OrderPop, jobIds);
        lv.setAdapter(orderPopListAdapterV);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskOfOrderInfo.DataBean dataBean = listOrder.get(position);
                if (dataBean != null) {
                    if (dataBean.getList() != null) {
                        jobIds = dataBean.getId();
                        String jobId = dataBean.getList().get(0).getJobId();
                        for (int i = 0; i < datas.size(); i++) {
                            if (datas.get(i).getJobId().equals(jobIds)) {
                                if(null != homebossVpOrder){
                                    homebossVpOrder.setCurrentItem(i);
                                }
                                OrderPop.dismiss();
                                return;
                            }
                        }
                    }
                }
            }
        });

    }

    private void closeOrderPop() {
        if (OrderPop != null) {
            OrderPop.dismiss();
        }
    }

    /**
     * 雇主是否有进行中的订单
     */
    private void isDoingOrder() {
        startLocation();
        if (!StringUtils.isLogin(getActivity())) {
            boss_slid_layout.setVisibility(View.GONE);
            homebossVp.setVisibility(View.GONE);
        }
        String userId = SharedPreferencesUtils.getString(getContext(), SharedPreferencesUtils.USERID);
        if (!TextUtils.isEmpty(userId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("format", "json");
            DataUtils.loadData(activity, GetDataConfing.BOSS_IS_TASKING, map, IntegerUtils.API_TAG_BOSS_TASKING, responseDataCallback);
        }
    }

    /*****
     * 雇主地图上面的招聘信息
     */
    private void loadMapsDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("format", "json");
        map.put("userId", SharedPreferencesUtils.getString(getContext(), SharedPreferencesUtils.USERID));
        if (latLonPoint != null) {
            // x坐标(经度)
            map.put("xCoordinate", String.valueOf(latLonPoint.getLongitude()));
            //y坐标(维度)
            map.put("yCoordinate", String.valueOf(latLonPoint.getLatitude()));
        } else {
            // x坐标(经度)
            map.put("xCoordinate", SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.longitude));
            //y坐标(维度)
            map.put("yCoordinate", SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.latitude));
        }
        //岗位类型
        map.put("postType", postType);
        //需求类型 //1.工作日,2.双休日,3.计件
        map.put("demandType", demandType);
        mMainActivity.startLoadingBossMapAnimation();//开启加载动画
        DataUtils.loadData(activity, GetDataConfing.BOSS_MAP_LIST, map, IntegerUtils.API_BOSS_MAP_LIST, responseDataCallback);
    }

    /**
     * 定位
     *
     * @param onLocationChangedListener
     */
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            mLocalLatlng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            MyApplication.mLocalLatlng = mLocalLatlng;
//            amap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocalLatlng, 15));
            if (mlocationClient != null) {
                mlocationClient.stopLocation();
            }
            latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            String address = aMapLocation.getPoiName();
            SharedPreferencesUtils.saveString(activity, "pro", aMapLocation.getProvince());
            SharedPreferencesUtils.saveString(activity, "city", aMapLocation.getCity());
            SharedPreferencesUtils.saveString(activity, "area", aMapLocation.getDistrict());
            SharedPreferencesUtils.saveString(activity, "street", aMapLocation.getStreet() + aMapLocation.getStreetNum());
            SharedPreferencesUtils.saveString(activity, "adr", address);
            SharedPreferencesUtils.saveString(activity, "proId", aMapLocation.getAdCode().toString().substring(0, 3) + "000");
            SharedPreferencesUtils.saveString(activity, "cityId", aMapLocation.getCityCode());
            SharedPreferencesUtils.saveString(activity, "areaId", aMapLocation.getAdCode());
            SharedPreferencesUtils.saveString(activity, "streetId", "");

            //具体定位的地点
            locationAddress = aMapLocation.getAddress();
            homebossTvAddress.setText(locationAddress);
            DataCacheUtils.getIstance().setCurrentLongitudeX(aMapLocation.getLongitude());
            DataCacheUtils.getIstance().setCurrentLatitudeY(aMapLocation.getLatitude());
//            setMyMapMarker(mLocalLatlng);//定位我的位置
//            setLocationAddress();//将大头针展示到地图上面
            if (!isOrderMode && !StringUtils.isLogin(activity))//游客
                startJumpAnimation(true);//定位完成 大头针跳动 加载地图雇主的订单图标
        } else {
            startLocation();//如果定位失败 继续定位
        }
    }

    //地图移动
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    //地图移动完成
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (isShow) {

        } else {
            if (isCardIsLook) {
                layoutVpOrder.setVisibility(View.GONE);
            }
        }
        if (!isOrderMode) {
            //屏幕中心的Marker跳动
            startJumpAnimation(true);
        }
        if ("isSwitchJoe".equals(isSwitchJoe)) {
            isSwitchJoe = "";
            isDoingOrder();
        }
        isSwitchJoe = "back";
    }


    //地图上的Marker点击
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!marker.equals(locationMarker) && !marker.equals(locationMarker1)) {
            homebossVp.setVisibility(View.VISIBLE);
            homebossLayoutAddress.setVisibility(View.GONE);//地址栏
            homebossRg.setVisibility(View.GONE);//工作类型选择控件：1.工作日 2.双休日3.计件
            bossmap_sobotandlocation.setVisibility(View.GONE);//客服和定位控件
            isCardIsLook = true;
            //订单详情viewPage
            homebossVp.setCurrentItem((int) marker.getZIndex());
            setImgBigMarker(marker);
        }
        return true;
    }

    /**
     * 添加定位大头针的图标
     * locationMarker 新的
     * locationMarker1 旧的位置
     */
    private Marker locationMarker, locationMarker1;        //定位的图标

    private void setLocationAddress() {
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_location))
                .title("")
                .snippet("")
                .draggable(true);
        if (locationMarker != null) {
            locationMarker.remove();
        }
        locationMarker = amap.addMarker(markerOptions);
        locationMarker.setToTop();
        locationMarker.setPositionByPixels(homebossMapView.getWidth() / 2, homebossMapView.getHeight() / 2);
    }

    private BossMapMsgBean bossMapMsgBean;
    private TaskOfOrderInfo taskOfOrderInfo;
    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
//            if (mLoadingDialog != null && mLoadingDialog.isShowing())
//                mLoadingDialog.dismiss();
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(activity);
            } else {
                String json = dataRequest.getResponseData();
                if (!TextUtils.isEmpty(json)) {
                    switch (dataRequest.getWhat()) {
                        case IntegerUtils.API_TAG_BOSS_TASKING:
                            JsonMap<String, Object> map = JsonParseHelper.getJsonMap(json);
                            bool = map.get("data") instanceof JSONArray ? ((JSONArray) map.get("data")).optBoolean(0) : map.getBoolean("data");
                            if (bool)
                                boss_slid_layout.setVisibility(View.VISIBLE);
                            else
                                boss_slid_layout.setVisibility(View.GONE);
                            mHasDoingTask = bool;
                            if (bool) {
//                               homeboss_vp
                                //有进行中的任务，直接到订单模式
                                ActingOrderModel();
                            } else {
                                if (!isCardIsLook) {
                                    if (null != lastMarker) {
                                        lastMarker.remove();
                                    }
                                    OutOrderModel();
                                }
                            }
                            break;

                        case IntegerUtils.API_BOSS_MAP_LIST:
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                mMainActivity.stopLoadingBossMapAnimation();
                                if ("0".equals(jsonObject.optString("code"))) {
                                    bossMapMsgBean = new Gson().fromJson(json, BossMapMsgBean.class);
                                    if (!isOrderMode)
                                        setDatas(bossMapMsgBean);
                                    locationMarker1 = locationMarker;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case TAG_REQUEST_TASK_LIST:


                            break;
                        case API_TAG_BOSS_TASKING_LIST://订单模式 展示地图 及订单处理
                            try {
                                if (isOrderMode) {
                                    JSONObject jsonObject = new JSONObject(json);
                                    if ("0".equals(jsonObject.optString("code"))) {
                                        SharedPreferencesUtils.saveString(activity, "orderinfo", json);
                                        taskOfOrderInfo = new Gson().fromJson(json, TaskOfOrderInfo.class);
                                        if (viewPageIndex > datas.size() - 1) {
                                            viewPageIndex = viewPageIndex - 1;
                                        }
                                        if(viewPageIndex<0){
                                            viewPageIndex = 0;
                                        }
                                        if(null != taskOfOrderInfo && null != taskOfOrderInfo.getData() && taskOfOrderInfo.getData().size()>0){
                                            EventBus.getDefault().post(new MainEvent(json,viewPageIndex, 4));
                                            setDataOrder(taskOfOrderInfo);
                                        }else {
                                            //退出订单
                                            if (null != lastMarker) {
                                                lastMarker.remove();
                                            }
                                            OutOrderModel();
                                            isOrderMode = false;
                                            boss_slid_layout.setVisibility(View.GONE);
                                            mHasDoingTask = false;
                                            EventBus.getDefault().post(new MainEvent("", 1));
//                                            isDoingOrder();
                                        }
                                    }
                                }
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                            break;
                        case TAG_USERINFO:
                            List<JsonMap<String, Object>> data = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                            if (data != null && data.size() > 0) {
                                JsonMap<String, Object> jsonMap = data.get(0);
                                userInfoSobot = new UserInfoSobot();
                                userInfoSobot.setUserId(jsonMap.getStringNoNull("userId"));
                                userInfoSobot.setPhone(jsonMap.getStringNoNull("phone"));
                                userInfoSobot.setNickName(jsonMap.getStringNoNull("nickName"));
                                SharedPreferencesUtils.saveString(activity, "nickName", jsonMap.getStringNoNull("nickName"));
                                userInfoSobot.setEmail(jsonMap.getStringNoNull("email"));
                                userInfoSobot.setLogo(jsonMap.getStringNoNull("logo"));
                            }
                            break;
                    }
                }
            }
        }
    };

    //可移动PIO点
    private LatLonPoint latLonPoint;
    private LatLng lastMapLoadLatLng;

    /******
     * 拖动地图PIO点获取经纬度和地址反编码
     */
    private void startJumpAnimation(boolean flag) {
        if (locationMarker != null) {

            //            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = locationMarker.getPosition();
            float distance = 1200;
            if (null != lastMapLoadLatLng) {
                distance = AMapUtils.calculateLineDistance(latLng, lastMapLoadLatLng);
            }

            latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
            if ((latLonPoint != null && distance >= 1200 && !isCardIsLook) ||
                    (markerList == null || markerList.size() == 0)) {
                Point point = amap.getProjection().toScreenLocation(latLng);
                point.y -= DimensUtils.dip2px(activity, 60);

                LatLng target = amap.getProjection().fromScreenLocation(point);
                //使用TranslateAnimation,填写一个需要移动的目标点
                //使用TranslateAnimation,填写一个需要移动的目标点
                startJumpLocation(target);
                lastMapLoadLatLng = latLng;
                if(!isLocationAddress){
                    loadMapsDatas();
                }else {
                    isLocationAddress = false;
                }

            }

            getAddress();

        }
    }

    private void startJumpLocation(LatLng target) {
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
        animation.setDuration(400);
        //设置动画
        locationMarker.setAnimation(animation);
        //开始动画
        locationMarker.startAnimation();
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress() {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
//                locationAddress = result.getRegeocodeAddress().getFormatAddress();
                city = result.getRegeocodeAddress().getCity();
                List<PoiItem> pois = result.getRegeocodeAddress().getPois();
                if (pois != null && pois.size() > 0) {
                    locationAddress = (pois.get(0).getSnippet() + pois.get(0).getTitle()).trim().length() > 15 ? pois.get(0).getSnippet() : pois.get(0).getSnippet() + pois.get(0).getTitle();
                    if (isFirstGetAdr) {
                        SharedPreferencesUtils.saveString(activity, "adr", locationAddress);
                        isFirstGetAdr = false;
                    }
                }
                homebossTvAddress.setText(locationAddress);
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 开始定位
     */
    private void startLocation() {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(activity);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
        }
        mlocationClient.startLocation();
    }

    private List<OrderCreateInfo> datas = new ArrayList<>();

    /**
     * 订单模式 展示到viewpager里面
     *
     * @param dataOrder
     */
    public void setDataOrder(TaskOfOrderInfo dataOrder) {

        adapter = null;
        homebossVpOrder = null;
        layoutVpOrder.removeAllViews();
        homebossVpOrder = new MyViewPager(getActivity());
        homebossVpOrder.setId(R.id.worker_order_card);
        layoutVpOrder.addView(homebossVpOrder);
        adapter = new MapWorkerPagerAdapterV(this.getChildFragmentManager()
                , activity);
        adapter.setList(dataOrder.getData(), homebossVpOrder);
        homebossVpOrder.setAdapter(adapter);
        homebossVpOrder.setOffscreenPageLimit(1);
        //adapter.notifyDataSetChanged();
        List<TaskOfOrderInfo.DataBean> list = dataOrder.getData();
        datas.clear();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                List<OrderCreateInfo> list1 = list.get(i).getList();
                if (list1 != null) {
                    for (int j = 0; j < list1.size(); j++) {
                        if (list1 != null && list1.size() > 0) {
                            datas.add(list1.get(j));
                            datas.get(j).setGroupId(list.get(i).getId());
                        }
                    }
                }
            }
            if (datas != null && datas.size() > 0) {
                amap.clear();
                amap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(datas.get(0).getXyCoordinateVo().getUserYCoordinate(),
                        datas.get(0).getXyCoordinateVo().getUserXCoordinate()), 14));
                setImageOnMap(datas.get(0).getLogo(), datas.get(0).getXyCoordinateVo().getUserYCoordinate(), datas.get(0).getXyCoordinateVo().getUserXCoordinate());
            }
        }
        if(viewPageIndex>0){
            homebossVpOrder.setCurrentItem(viewPageIndex);
        }

        homebossVpOrder.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // List<TaskOfOrderInfo.DataBean> data = taskOfOrderInfo.getData();
                if (viewPageIndex > datas.size() - 1) {
                    viewPageIndex = viewPageIndex - 1;
                }
                if(viewPageIndex<0){
                    viewPageIndex = 0;
                }
                for (int i = 0; i < datas.size(); i++) {
                    if (!datas.get(viewPageIndex).getJobId().equals(datas.get(position).getJobId())) {
                        if(position >viewPageIndex){
                            OrderToast.OrderToastShow(activity,true);
                        }else {
                            OrderToast.OrderToastShow(activity,false);
                        }

                        jobIds = datas.get(position).getJobId();
                        EventBus.getDefault().post(new MainEvent(datas.get(position).getJobId(), 14));
                    }
                }
                if (datas != null && datas.size() > 0) {
                    OrderCreateInfo orderCreateInfo = datas.get(position);
                    Log.e("DLG", "选择数据==" + new Gson().toJson(orderCreateInfo));
                    if (orderCreateInfo != null && null != orderCreateInfo.getXyCoordinateVo()) {
                        amap.clear();
                        amap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                orderCreateInfo.getXyCoordinateVo().getUserYCoordinate(),
                                orderCreateInfo.getXyCoordinateVo().getUserXCoordinate()), 14));
                        setImageOnMap(orderCreateInfo.getLogo(), orderCreateInfo.getXyCoordinateVo().getUserYCoordinate()
                                , orderCreateInfo.getXyCoordinateVo().getUserXCoordinate());
                    }
                }


                viewPageIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setImageOnMap(String logo, double jobYCoordinate, double jobXCoordinate) {
        Log.e("===", "setImageOnMap");
        View view = View.inflate(activity, R.layout.item_map_order_img, null);
        view.setBackground(null);
        CircleImageView headImg = (CircleImageView) view.findViewById(R.id.iv_head_mark);
        Glide.with(activity)
                .load(logo)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.mipmap.morentouxiang)
                .placeholder(R.mipmap.morentouxiang)
                .into(headImg);
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromView(view))
                .position(new LatLng(jobYCoordinate, jobXCoordinate))
                .draggable(true);

        Marker marker = amap.addMarker(markerOptions);
        marker.setClickable(false);
        marker.setToTop();
        /**
         * 地图正在进行中的头像
         */
        Glide.with(activity).load(logo).placeholder(R.mipmap.icon_default_user).error(R.mipmap.icon_default_user)
                .into(homeboss_iv_ongoingorder);
    }

    public void sendT(String s) {
        isSwitchJoe = s;
        if ("back".equals(isSwitchJoe)) {
            isDoingOrder();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        homebossMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        homebossMapView.onResume();
        if (amap != null && tag != FROM_ADDRESS) {
            String string = SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.latitude);
            String string1 = SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.longitude);
            if(TextUtils.isEmpty(string)||TextUtils.isEmpty(string1)){
                return;
            }
            double latitude = Double.parseDouble(string);
            Double longitude = Double.parseDouble(string1);
            amap.moveCamera(CameraUpdateFactory.newLatLngZoom
                    (new LatLng(latitude, longitude), 14));
        }
        if (!StringUtils.isLogin(activity) && !homebossVp.isShown()) {
            boss_slid_layout.setVisibility(View.GONE);
            loadMapsDatas();
        }
        if (!isOrderMode) {
            isDoingOrder();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        homebossMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homebossMapView.onDestroy();
        ButterKnife.unbind(this);
        deactivate();
    }

    private void startScaleAnimation(Marker marker) {
        Animation animation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f);
        animation.setDuration(400);
        marker.setAnimation(animation);
        marker.startAnimation();
    }

    private void startScaleBigAnimation(Marker marker) {
        Animation animation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f);
        animation.setDuration(400);
        marker.setAnimation(animation);
        marker.startAnimation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 10) {
                double y = data.getDoubleExtra("y", 0);
                double x = data.getDoubleExtra("x", 0);
                amap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(y, x), 14));
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
