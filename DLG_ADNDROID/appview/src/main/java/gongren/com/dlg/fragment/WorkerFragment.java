package gongren.com.dlg.fragment;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.bumptech.glide.Glide;
import com.common.string.LogUtils;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
import gongren.com.dlg.activity.FindWorkSearchActivity;
import gongren.com.dlg.activity.LoginDialogActivity;
import gongren.com.dlg.activity.MainActivity;
import gongren.com.dlg.activity.WorkOrderDetailActivity;
import gongren.com.dlg.adapter.MapOrderPagerAdapter;
import gongren.com.dlg.adapter.WorkCardPageAdapter;
import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.javabean.DoingTaskOrderDetailModel;
import gongren.com.dlg.javabean.MainEvent;
import gongren.com.dlg.javabean.MainToWorkerFragmentEvent;
import gongren.com.dlg.javabean.MapLoadEvent;
import gongren.com.dlg.javabean.UserInfoSobot;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataCacheUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DimensUtils;
import gongren.com.dlg.utils.DrivingRouteOverlay;
import gongren.com.dlg.utils.IntegerUtils;
import gongren.com.dlg.utils.SobotUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.utils.WorkMapManager;
import gongren.com.dlg.utils.WorkbenchManager;
import gongren.com.dlg.view.MyViewPager;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.RequestCallback;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;


/**
 * Created by zhangpei on 2017/3/22.
 */
public class WorkerFragment extends BaseFragment implements LocationSource, AMapLocationListener, AMap.OnMarkerClickListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnCameraChangeListener, RouteSearch.OnRouteSearchListener {
    @Bind(R.id.homeworker_mapView)
    TextureMapView homeworkerMapView;
    @Bind(R.id.homeworker_iv_sobot)
    ImageView homeworkerIvSobot;
    @Bind(R.id.homeworker_iv_location)
    ImageView homeworkerIvLocation;
    @Bind(R.id.homeworker_rb_workday)
    TextView homeworkerRbWorkday;
    @Bind(R.id.homeworker_rb_week)
    TextView homeworkerRbWeek;
    @Bind(R.id.homeworker_rb_project)
    TextView homeworkerRbProject;
    @Bind(R.id.homeworker_rg)
    LinearLayout homeworkerRg;
    @Bind(R.id.homeworker_tv_address)
    TextView homeworkerTvAddress;
    @Bind(R.id.homeworker_tv_xuqiu)
    TextView homeworkerTvXuqiu;
    @Bind(R.id.homeworker_layout_xuqiu)
    LinearLayout homeworkerLayoutXuqiu;
    @Bind(R.id.homeboss_tv_xuqiulay)
    LinearLayout homebossLayoutXuqiulay;
    @Bind(R.id.homeworker_layout_address)
    LinearLayout homeworkerLayoutAddress;
    @Bind(R.id.homeworker_vp)
    MyViewPager homeworkerVp;

    @Bind(R.id.work_slid_layout)
    LinearLayout work_slid_layout;
    @Bind(R.id.homeworker_iv_ongoingorder)
    CircleImageView homeworkerIvOngoingorder;

    @Bind(R.id.workermap_sobotandlocation)
    LinearLayout workermapSobotandlocation;

    @Bind(R.id.homeworker_layout_bottom)
    RelativeLayout homeworkerLayoutBottom;
    //进入订单的布局
    @Bind(R.id.workerorder_layout)
    LinearLayout workerorder_layout;
    //列表和订单滚轮的图标
    @Bind(R.id.homeworker_layout_listandorder)
    LinearLayout homeworker_layout_listandorder;
    //    @Bind(R.id.work_card_page)
    MyViewPager mWorkCardPage; //雇员正在进行中的卡片
    private WorkCardPageAdapter cardPageAdapter;
    //    @Bind(R.id.doing_task_detail_card)
//    DoingTaskDetailCardView doingTaskDetailCardView;
//    @Bind(R.id.guyuan_banner)
//    ConvenientBanner guyuanBanner; //雇员正在进行中的订单viewpage
    TextView workerorder_title;
    ImageView workerorder_back;

    private RouteSearch mRouteSearch;
    private AMap map;
    private String locationAddress;
    private String city;
    private String postType = "job.type_0", demandType = "";
    private View detailView = null;
    private boolean isFromOrder;//是否来自订单
    private boolean isOrderMode;//是否是订单模式
    //是否正在请求当前进行中的任务
    boolean isRequestTaskDetail = false;
    WorkMapManager workMapManager;
    private LatLng myLatlng;
    private boolean mislocation = false;
    private boolean isJustGoBackFromOrder;//是否从订单中刚回来 回来的话 大头针的地理位置应为myLatlng
    private boolean isJustGoBackFromOrder1;//是否从订单中刚回来 回来的话 大头针的地理位置应为myLatlng
    private boolean isFirstLoadMapDada = true;//是否是第一次在地图上面加载数据 是的话，加载，不是的话，不加载，后期再优化
    private boolean isCardIsLook;//卡片是否可以见 默认不可见
    public static final int FROM_ADDRESS = 4;
    private View mView;
    private boolean isWithinArea = false;//是否在工作范围内 默认不在


    private static final int TAG_USERINFO = 2;      //请求用户信息
    private boolean isFirstGetAdr = true;

    private int tag;

    public void setPostType(String postType) {
        this.postType = postType;
    }

    //EventBus指令消息
    @Subscribe
    public void onMessageEvent(MainToWorkerFragmentEvent event) {
        if (!(activity instanceof MainActivity)) {//不是主页 不接受通知内容 在oncreate方法也只有是主页才注册
            return;
        }
        tag = event.getTag();
        switch (event.getTag()) {
            case 0:
                postType = event.getMsg();
                isOrderMode = true;
                centerLocationMarker1 = null;
                ActingOrderModel();
                break;
            case 1:
                postType = event.getMsg();
                isOrderMode = false;
                centerLocationMarker1 = null;
                OutOrderModel();
                break;
            case 2:
                //点击零工类型导航条
                postType = event.getMsg();
                if (latLonMapCenterPoint == null)
                    break;
                homeworkerVp.setVisibility(View.GONE);
                isCardIsLook = false;
                homeworkerLayoutAddress.setVisibility(View.VISIBLE);
                loadMapsDatas();
                break;
            case 3:
                loadHasDoingTask();
                break;
            case FROM_ADDRESS://自己选择完地址以后返回首页 大头针跳到指定地址 并加载数据
                String msg = event.getMsg();
                double latitude = Double.parseDouble(msg.split(",")[0]);
                double longitude = Double.parseDouble(msg.split(",")[1]);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));
                loadMapsDatas();
                break;
            case 100:  //推送的更新消息
                loadData();
                break;
            case 13:
                OutOrderModel();
                EventBus.getDefault().post(new MainEvent("", 0));
                break;

        }
    }

    String businessNumber;

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
        this.isFromOrder = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mView) {
            ViewGroup viewGroup = (ViewGroup) mView.getParent();
            if (null != viewGroup) {
                viewGroup.removeView(mView);
            }
            mView = null;
        }
        mView = inflater.inflate(R.layout.fragment_home_worker, container, false);
        ButterKnife.bind(this, mView);
        homeworkerMapView.onCreate(savedInstanceState);// 此方法必须重写
//        if (MyApplication.mLocalLatlng != null) {
//            map.animateCamera(CameraUpdateFactory.newLatLngZoom(MyApplication.mLocalLatlng, 14));
//        }
        if (activity instanceof MainActivity) {
            initMain();
        } else if (activity instanceof WorkOrderDetailActivity) {
            initWork();
        }

        return mView;
    }

    /**
     * 雇员的零工
     */
    private void initWork() {
        initMap();//初始化地图
        initWorkView();
        startLocation();
        getTaskDetailById(businessNumber);
    }

    /**
     * 主页
     */
    private void initMain() {
        EventBus.getDefault().register(this);
        workerorder_title = (TextView) getActivity().findViewById(R.id.workerorder_title);

        initView();
        initDatas();
        initListener();
        if (!StringUtils.isLogin(activity)) {
            startLocation();//如果是游客身份 直接开始定位 不进行是否有进行中的任务的验证
        } else {
            loadHasDoingTask();
        }
        initEvent();
        if (!StringUtils.isLogin(getActivity())) {
            work_slid_layout.setVisibility(View.GONE);
        }
    }

    //零工view初始化
    private void initWorkView() {
        homeworkerLayoutBottom.setVisibility(View.GONE);
        homeworkerVp.setVisibility(View.GONE);
//        doingTaskDetailCardView.setVisibility(View.VISIBLE);
        workerorder_layout.setVisibility(View.VISIBLE);
        homeworker_layout_listandorder.setVisibility(View.GONE);
        workermapSobotandlocation.setVisibility(View.GONE);
        homeworkerRg.setVisibility(View.GONE);
        homeworkerLayoutAddress.setVisibility(View.GONE);
    }

    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    private static final int REQUEST_QUERY = 100;     //请求分享的标题与内容文字

    private void initEvent() {
        if (workerorder_back != null) {
            workerorder_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case IntegerUtils.WORKORDER_SHOW:
                    detailView = msg.obj instanceof View ? (View) msg.obj : null;
                    break;
            }
        }
    };

    /**
     * 初始化界面
     */
    private void initView() {

        /**
         * 初始化分享
         * url   请求的接口  如果是固定的，可以写死；不是的话，传参形式    eg：GetDataConfing.shareContent
         * param_key 请求接口的形参   eg：id   userId  ...
         * param_value 参数的接口的实参
         */
//        initUMShare();
        //初始化
        initMap();
        isCardIsLook = false;
        homeworkerVp.setVisibility(View.GONE);
        //请求用户详情，用以开启人工服务
        DataUtils.loadData(activity, GetDataConfing.queryUserInfoToService, BaseMapUtils.getMap(activity), TAG_USERINFO, responseDataCallback);

    }

    private void initMap() {
        map = homeworkerMapView.getMap();
//        map.setMapType(AMap.MAP_TYPE_NAVI);
        workMapManager = new WorkMapManager(map);
        mRouteSearch = new RouteSearch(activity);
        mRouteSearch.setRouteSearchListener(this);
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setLogoBottomMargin(-60);
        uiSettings.setZoomControlsEnabled(false);     //隐藏缩放按钮
        uiSettings.setScaleControlsEnabled(true);      //显示比例尺
        uiSettings.setTiltGesturesEnabled(false);       //倾斜手势
        uiSettings.setRotateGesturesEnabled(false);     //旋转手势
    }

    //初始化假数据
    private List<Marker> markerList = new ArrayList<>();
    private List<JsonMap<String, Object>> datasList = new ArrayList<>();

    private void clearDatas() {
        markerList.clear();
        datasList.clear();
        if (homeworkerVp.getAdapter() != null) {
            homeworkerVp.getAdapter().notifyDataSetChanged();
        }
        initDatas();
    }

    private void initDatas() {
        if (!isFromOrder) {
            isCardIsLook = false;
            homeworkerVp.setVisibility(View.GONE);
            //地址反编码
            if (geocoderSearch == null) {
                geocoderSearch = new GeocodeSearch(activity);
            }
            geocoderSearch.setOnGeocodeSearchListener(this);
        }
    }

    /**
     * 判断是否进入了订单模式
     */
    private void initListener() {
        map.setLocationSource(this);// 设置定位监听
        map.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        map.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
        map.setOnMarkerClickListener(this);
        map.setOnCameraChangeListener(this);       //对amap添加移动地图事件监听器
        map.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (isOrderMode) {
                    isFirstLoadMapDada = true;
                    isJustGoBackFromOrder = true;
                    //退出订单
                    isOrderMode = false;
                    EventBus.getDefault().post(new MainEvent("", 0));
                    isCardIsLook = false;//至为false 防止再次加载网络数据 出现地图卡顿的想象
                    setBigMarker(null);
                    OutOrderModel();
                } else if (isCardIsLook) {//卡片可见的时候 隐藏
                    homeworkerVp.setVisibility(View.GONE);
                    isFirstLoadMapDada = true;//目的是 刷新地图中的网络数据
                    isCardIsLook = false;//至为false 防止再次加载网络数据 出现地图卡顿的想象
                    homeworkerLayoutAddress.setVisibility(View.VISIBLE);
                    homeworkerLayoutBottom.setVisibility(View.VISIBLE);
                    OutOrderModel(isCardIsLook);
                }
                //点击地图关闭打开的零工信息卡片
                if (detailView != null)
                    detailView.setVisibility(View.GONE);

            }
        });

        homeworkerVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setBigMarker(markerList.get(position));
                if (detailView != null)
                    detailView.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /**
         * 滚轮监听事件
         */
        homeworkerIvOngoingorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        //超过40dp触发时间
                        if (DimensUtils.getScreenWith(activity) - event.getRawX() > 300) {
                            work_slid_layout.setPadding(DimensUtils.dip2px(activity, 2), DimensUtils.dip2px(activity, 2),
                                    (int) 2.5 * DimensUtils.px2dip(activity, 60)
                                    , DimensUtils.dip2px(activity, 2));
                            //进入订单页面
                        }
                        //滑动在10dp和40dp之间
                        if (DimensUtils.getScreenWith(activity)
                                - event.getRawX() >= 50 &&
                                DimensUtils.getScreenWith(activity) - event.getRawX() <= 300) {
                            work_slid_layout.setPadding(DimensUtils.dip2px(activity, 2), DimensUtils.dip2px(activity, 2),
                                    (int) (2.5 * DimensUtils.px2dip(activity, (float)
                                            (DimensUtils.getScreenWith(activity) - event.getRawX())))
                                    , DimensUtils.dip2px(activity, 2));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (DimensUtils.getScreenWith(activity) - event.getRawX() >= 50 &&
                                DimensUtils.getScreenWith(activity) - event.getRawX() < 300) {
                            work_slid_layout.setPadding(DimensUtils.dip2px(activity, 2), DimensUtils.dip2px(activity, 2),
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

//    private Dialog loadingDialog;

    //进入订单模式
    private void ActingOrderModel() {
        map.clear();
        homeworker_layout_listandorder.setVisibility(View.GONE);
        workerorder_layout.setVisibility(View.GONE);
//        doingTaskDetailCardView.setVisibility(View.GONE);
        homeworkerRg.setVisibility(View.GONE);
        workermapSobotandlocation.setVisibility(View.GONE);
        homeworkerLayoutAddress.setVisibility(View.GONE);
        homeworkerLayoutBottom.setVisibility(View.GONE);
        isCardIsLook = false;
        homeworkerVp.setVisibility(View.GONE);

        if (activity instanceof MainActivity)
            EventBus.getDefault().post(new MainEvent("", 5));
        isOrderMode = true;
//        loadingDialog = DialogUtils.showLoadingDialog(activity);
        loadData();
    }

    private void loadData() {
        if (isFromOrder && StringUtils.isLogin(activity)) {
            getTaskDetailById(businessNumber);
        } else if (StringUtils.isLogin(activity)) {
            getDoingTaskDetail();
        }
    }

    /**
     * 点击地图头像显示卡片后在点击地图隐藏卡片走的方法
     *
     * @param isCardIsLook
     */
    private void OutOrderModel(boolean isCardIsLook) {
//        map.clear();
        if (null != centerLocationMarker) {
            centerLocationMarker.setToTop();
            centerLocationMarker.setPositionByPixels(homeworkerMapView.getWidth() / 2, homeworkerMapView.getHeight() / 2);
        }
        setBigMarker(null);
        //初始化数据
//        clearDatas();

        if (myLatlng != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 14));
        }
        homeworker_layout_listandorder.setVisibility(View.VISIBLE);
        workerorder_layout.setVisibility(View.GONE);

        homeworkerRg.setVisibility(View.VISIBLE);
        workermapSobotandlocation.setVisibility(View.VISIBLE);
        homeworkerLayoutAddress.setVisibility(View.VISIBLE);
        isOrderMode = false;
        isGuidanceIng = false;


        if (mHasDoingTask) {
            work_slid_layout.setVisibility(View.VISIBLE);
        } else {
            work_slid_layout.setVisibility(View.GONE);
        }
        isFromOrder = false;
    }

    //退出订单模式
    private void OutOrderModel() {
        map.clear();
        if (!isFromOrder)
            setLocationAddress();
        //初始化数据
        clearDatas();

        if (myLatlng != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 14));
        }
        startJumpAnimation();
        homeworker_layout_listandorder.setVisibility(View.VISIBLE);
        workerorder_layout.setVisibility(View.GONE);

        homeworkerRg.setVisibility(View.VISIBLE);
        workermapSobotandlocation.setVisibility(View.VISIBLE);
        homeworkerLayoutAddress.setVisibility(View.VISIBLE);
        isOrderMode = false;
        isGuidanceIng = false;


        if (mHasDoingTask) {
            work_slid_layout.setVisibility(View.VISIBLE);
        } else {
            work_slid_layout.setVisibility(View.GONE);
        }
        isFromOrder = false;
    }

    private Marker startNavigationmarker, endNavigationmarker;
    //定位地址（我的地址）
    private LatLonPoint mStartPoint = null;// = new LatLonPoint(31.2338893501, 121.3820219756);
    //目标地址
    private LatLonPoint mEndPoint = null;//= new LatLonPoint(31.2318483501, 121.3833379756);

    private UserInfoSobot userInfoSobot;     //用户信息实体类

    @OnClick({R.id.homeworker_iv_sobot, R.id.homeworker_iv_location, R.id.homeworker_layout_xuqiu, R.id.homeworker_layout_address, R.id.homeworker_iv_list,
            R.id.homeworker_iv_ongoingorder, R.id.homeworker_rb_workday, R.id.homeworker_rb_week, R.id.homeworker_rb_project,
            R.id.homeworker_tv_address, R.id.homeboss_tv_xuqiulay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeworker_iv_sobot:   //开启人工客服
                if (StringUtils.isLogin(activity)) {

                    SobotUtils.startSobot(activity, userInfoSobot);
                } else {
                    activity.startActivity(new Intent(activity, LoginDialogActivity.class));
                }

                break;
            case R.id.homeworker_iv_location:
                //定位
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocalLatlng, 14));
                homeworkerTvAddress.setText(locationAddress);
                break;

            case R.id.homeworker_layout_xuqiu:
                ToastUtils.showToastShort(activity, "需求");
                break;
            case R.id.homeworker_layout_address:
                break;
            case R.id.homeworker_iv_list:
                EventBus.getDefault().post(new MainEvent("", 2));
                break;
            case R.id.homeworker_iv_ongoingorder:
                break;

            case R.id.homeworker_tv_address:
                Intent intent = new Intent(activity, AddressSearcherActivity.class);
                if (latLonMapCenterPoint != null) {
                    intent.putExtra("lat", latLonMapCenterPoint.getLatitude());
                    intent.putExtra("lon", latLonMapCenterPoint.getLongitude());
                    intent.putExtra("address", locationAddress);
                    intent.putExtra("city", city);
                }
                startActivity(intent);
                break;

            case R.id.homeboss_tv_xuqiulay:
                if (StringUtils.isLogin(getContext())) {
                    //你想找什么样的零工
                    Intent findWorkSearchActivity = new Intent(activity, FindWorkSearchActivity.class);
                    if (latLonMapCenterPoint != null) {
                        findWorkSearchActivity.putExtra("x_longitude", latLonMapCenterPoint.getLongitude());
                        findWorkSearchActivity.putExtra("y_latitude", latLonMapCenterPoint.getLatitude());
                    }
                    startActivity(findWorkSearchActivity);
                } else {
                    startActivity(new Intent(activity, LoginDialogActivity.class));
                }

                break;
            case R.id.homeworker_rb_workday:
                //工作日
                if (!homeworkerRbWorkday.isSelected()) {
                    demandType = "1";
                    homeworkerRbWorkday.setSelected(true);
                    homeworkerRbWeek.setSelected(false);
                    homeworkerRbProject.setSelected(false);
                } else {
                    demandType = "";
                    homeworkerRbWorkday.setSelected(false);
                }
                loadMapsDatas();
                break;

            case R.id.homeworker_rb_week:
                //双休日
                if (!homeworkerRbWeek.isSelected()) {
                    demandType = "2";
                    homeworkerRbWorkday.setSelected(false);
                    homeworkerRbWeek.setSelected(true);
                    homeworkerRbProject.setSelected(false);
                } else {
                    demandType = "";
                    homeworkerRbWeek.setSelected(false);
                }
                loadMapsDatas();
                break;

            case R.id.homeworker_rb_project:
                //计件
                if (!homeworkerRbProject.isSelected()) {
                    demandType = "3";
                    homeworkerRbWorkday.setSelected(false);
                    homeworkerRbWeek.setSelected(false);
                    homeworkerRbProject.setSelected(true);
                } else {
                    demandType = "";
                    homeworkerRbProject.setSelected(false);
                }
                loadMapsDatas();
                break;
        }
    }

    /**
     * 雇主发布的订单设置成点击状态的Marker
     *
     * @param marker
     */
    private void setBigMarker(Marker marker) {
        //清除全部
        if (lastMarker != null) {
            View oldView = View.inflate(activity, R.layout.item_map_order, null);
            TextView oldPrice = (TextView) oldView.findViewById(R.id.tv_price);
            JsonMap<String, Object> oldJsonMap = datasList.get((int) lastMarker.getZIndex());
            if ("志愿义工".equals(oldJsonMap.getStringNoNull("postTypeName")) || oldJsonMap.getDouble("price", 0) == 0) {
                oldPrice.setText(oldJsonMap.getDouble("price", 0) + "\n元/单");
            } else
                oldPrice.setText(oldJsonMap.getDouble("price", 0) + "\n元/" + oldJsonMap.getStringNoNull("jobMeterUnitName"));
            lastMarker.setIcon(BitmapDescriptorFactory.fromView(oldView));
        }
        if (null != marker) {
            View newView = View.inflate(activity, R.layout.item_map_order_big, null);
            TextView newPrice = (TextView) newView.findViewById(R.id.tv_price);
            JsonMap<String, Object> newJsonMap = datasList.get((int) marker.getZIndex());

            if ("志愿义工".equals(newJsonMap.getStringNoNull("postTypeName")) || newJsonMap.getDouble("price", 0) == 0) {
                newPrice.setText(newJsonMap.getDouble("price", 0) + "\n元/单");
            } else
                newPrice.setText(newJsonMap.getDouble("price", 0) + "\n元/" + newJsonMap.getStringNoNull("jobMeterUnitName"));
            marker.setIcon(BitmapDescriptorFactory.fromView(newView));

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));

            startScaleBigAnimation(marker);
            lastMarker = marker;
            lastMarker.setToTop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        homeworkerMapView.onResume();
        if (!StringUtils.isLogin(activity)) {
            work_slid_layout.setVisibility(View.GONE);
        } else {
            if (activity instanceof MainActivity) {
                if (isOnStop) {
                    loadHasDoingTask();
                    isOnStop = false;
                }

                if (map != null && tag != FROM_ADDRESS && isCardIsLook) {
                    String string = SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.latitude);
                    String string1 = SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.longitude);
                    LogUtils.e("onResume=" + string + "  string1=" + string1);
                    if (TextUtils.isEmpty(string) || TextUtils.isEmpty(string1)) return;
                    double latitude = Double.parseDouble(string);
                    Double longitude = Double.parseDouble(string1);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom
                            (new LatLng(latitude, longitude), 14));
                }
            }
        }
    }

    private boolean isOnStop;

    @Override
    public void onStop() {
        super.onStop();
        isOnStop = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isOnStop = false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onPause() {
        super.onPause();
        homeworkerMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (homeworkerMapView != null) {
            homeworkerMapView.onDestroy();
        }
        ButterKnife.unbind(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
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

    //定位改变
    private LatLng mLocalLatlng;       //手机定位的经纬度坐标

    //地图移动
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    private GeocodeSearch geocoderSearch;

    //地图移动完成
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (activity instanceof MainActivity) {
            if (!isOrderMode) {
                //屏幕中心的Marker跳动
                startJumpAnimation();
            }

            if (latLonMapCenterPoint != null) {
                DataCacheUtils.getIstance().setCurrentLongitudeX(latLonMapCenterPoint.getLongitude());
                DataCacheUtils.getIstance().setCurrentLatitudeY(latLonMapCenterPoint.getLatitude());
                EventBus.getDefault().post(new MapLoadEvent(MapLoadEvent.LOAD_SUCCESS));
            }
            if (!isOrderMode && !isCardIsLook) {
                homeworkerLayoutAddress.setVisibility(View.VISIBLE);
                homeworkerLayoutBottom.setVisibility(View.VISIBLE);
            }
        }
    }


    //地图中心经纬度
    private LatLonPoint latLonMapCenterPoint;
    private LatLng lastMapLoadLatLng;

    /******
     * 拖动地图PIO点获取经纬度和地址反编码
     * 拖拽结束，播放跳动动画，
     */
    private void startJumpAnimation() {
        if (centerLocationMarker != null) {
            LatLng latLng;
            //根据屏幕距离计算需要移动的目标点
            if (isJustGoBackFromOrder && myLatlng != null) {
                latLng = myLatlng;
                isJustGoBackFromOrder = false;
                centerLocationMarker.setToTop();//将大头针 放在定位图标的上面
            } else {
                latLng = centerLocationMarker.getPosition();
            }


            latLonMapCenterPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
            float distance = 1000;
            if (null != lastMapLoadLatLng) {
                distance = AMapUtils.calculateLineDistance(latLng, lastMapLoadLatLng);
            }
            if (!isCardIsLook) {
                if ((latLonMapCenterPoint != null && distance >= 3000) ||
                        (markerList == null || markerList.size() == 0)) {
                    Point point = map.getProjection().toScreenLocation(latLng);
                    point.y -= DimensUtils.dip2px(activity, 60);
                    LatLng target = map.getProjection().fromScreenLocation(point);
                    startJumpAnimation(target);
                    lastMapLoadLatLng = latLng;
                    loadMapsDatas();
                }
            }
            mStartPoint = latLonMapCenterPoint;
            getAddress();
        }
    }

    private void startJumpAnimation(LatLng target) {
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
        animation.setDuration(400);
        //设置动画
        centerLocationMarker.setAnimation(animation);
        //开始动画
        centerLocationMarker.startAnimation();
    }


    private Marker centerLocationMarker;        //可拖动mark的图标（一直位于地图中心）
    private Marker centerLocationMarker1;        //可拖动mark的图标（一直位于地图中心）
    private Marker maplocationMarker;           //手机定位mark图标

    //---------------------------------------------------------------------------------------------------------
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    /**
     * 驾车路线搜索规划回调
     *
     * @param result
     * @param code
     */
    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int code) {
        if (code == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    DriveRouteResult mDriveRouteResult = result;
                    DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            activity, map, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    //显示不同路径的缩放比例
//                    drivingRouteOverlay.setThroughPointIconVisibility(true);

                } else if (result != null && result.getPaths() == null) {
                    ToastUtils.showToastShort(activity, "未找到路线");
                }
            } else {
                ToastUtils.showToastShort(activity, "未找到路线");
            }
        }
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress() {
        RegeocodeQuery query = new RegeocodeQuery(latLonMapCenterPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    /**
     * 经纬度反编码地址信息
     *
     * @param result
     * @param rCode
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
                if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
//                    locationAddress = result.getRegeocodeAddress().getFormatAddress();
                    city = result.getRegeocodeAddress().getCity();
                    List<PoiItem> pois = result.getRegeocodeAddress().getPois();
                    if (pois != null && pois.size() > 0) {
                        locationAddress = (pois.get(0).getSnippet() + pois.get(0).getTitle()).trim().length() > 15 ? pois.get(0).getSnippet() : pois.get(0).getSnippet() + pois.get(0).getTitle();
                        if (isFirstGetAdr) {
                            SharedPreferencesUtils.saveString(activity, "adr", locationAddress);
                            isFirstGetAdr = false;
                        }
                    }
                    homeworkerTvAddress.setText(locationAddress);
                }
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    boolean isGuidanceIng = false;  //导航中


    /**
     * 开始定位
     */
    private void startLocation() {
        if (tag == FROM_ADDRESS) {
            tag = -1;
            return;
        }
//        if(null != MyApplication.mLocalLatlng ){
//            return;
//        }
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(activity);
            mLocationOption = new AMapLocationClientOption();
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

    /**
     * 定位回调
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            myLatlng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());//我的位置 copy一份作为备用
            MyApplication.mLocalLatlng = myLatlng;
            if (activity instanceof MainActivity) {
                LogUtils.e("========onLocationChanged=========");
                //保存地位数据
                workMapManager.saveLoaction(getContext(), aMapLocation);
                mLocalLatlng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                latLonMapCenterPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                SharedPreferencesUtils.saveString(activity, SharedPreferencesUtils.latitude, "" + aMapLocation.getLatitude());
                SharedPreferencesUtils.saveString(activity, SharedPreferencesUtils.longitude, "" + aMapLocation.getLongitude());
                mislocation = true;
                if (isOrderMode && StringUtils.isLogin(activity)) {
                    //订单模式定位回调
                    orderModeLoactionCallback();
                } else if (isFirstLoadMapDada) {
                    //工作台模式定位回调
                    workBenchModeLoactionCallBack(aMapLocation);
                    isFirstLoadMapDada = false;
                }
                if (!mHasDoingTask) {
                    //没有附近任务则开启定位服务，根据定位查找附近的任务
                    isOrderMode = false;
                    work_slid_layout.setVisibility(View.GONE);
                    isFirstLoadMapDada = true;
                    isJustGoBackFromOrder = true;
                    //退出订单
                    OutOrderModel();
                }
                //具体定位的地点
                city = aMapLocation.getCity();
                locationAddress = aMapLocation.getAddress();
                homeworkerTvAddress.setText(locationAddress);
                if (mlocationClient != null)
                    mlocationClient.stopLocation();//停止定位
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
            }
        } else {
//            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
//            LogUtils.logD("zq", errText);
//            ToastUtils.showToastShort(activity, "定位失败，请确认是否已打开定位权限");
        }
    }

    private void orderModeLoactionCallback() {
        if (isGuidanceIng) {
            //导航中，就不断的请求定位，切标出自己的位置
            LatLng endLatLng = new LatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude());
            float distance = AMapUtils.calculateLineDistance(mLocalLatlng, endLatLng);
            workMapManager.addMineMark(activity, mLocalLatlng);
        } else {
            workMapManager.addMineMark(activity, mLocalLatlng);
        }
    }

    private void workBenchModeLoactionCallBack(AMapLocation aMapLocation) {
        DataCacheUtils.getIstance().setCurrentLongitudeX(latLonMapCenterPoint.getLongitude());
        DataCacheUtils.getIstance().setCurrentLatitudeY(latLonMapCenterPoint.getLatitude());
        if (!isFromOrder)
            setLocationAddress();
        //移动中心点
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocalLatlng, 14));
        latLonMapCenterPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        startJumpAnimation();

        //发送消息，通知列表模式加载数据
        MainToWorkerFragmentEvent event = new MainToWorkerFragmentEvent("", 100);
        EventBus.getDefault().post(event);

//        WorkbenchManager.addUserLogin();
    }

    /**
     * 工作台模式的定位大头针的图标
     */
    private void setLocationAddress() {
        MarkerOptions markerOptions = new MarkerOptions().icon(
                BitmapDescriptorFactory.fromResource(R.mipmap.map_location))
                .title("")
                .snippet("")
                .draggable(true);
        centerLocationMarker = map.addMarker(markerOptions);
        centerLocationMarker.setToTop();
        centerLocationMarker.setPositionByPixels(homeworkerMapView.getWidth() / 2, homeworkerMapView.getHeight() / 2);
    }

    private Marker lastMarker;

    //地图上的Marker点击
    @Override
    public boolean onMarkerClick(Marker marker) {

        if (!marker.equals(centerLocationMarker) && !marker
                .equals(startNavigationmarker) && !marker.equals(maplocationMarker)
                && !marker.equals(endNavigationmarker)) {
            isCardIsLook = true;
            homeworkerLayoutAddress.setVisibility(View.GONE);
            homeworkerLayoutBottom.setVisibility(View.GONE);
            homeworkerVp.setVisibility(View.VISIBLE);
            homeworkerVp.setCurrentItem((int) marker.getZIndex());
            LatLng latLng = marker.getPosition();
            mEndPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
            setBigMarker(marker);
        }
        return true;
    }


    boolean mHasDoingTask = false;


    /**
     * ===================================================================   以下都是网络请求   ====================================================================
     * <p>
     * 客服系统需要的用户数据请求
     * <p>
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(activity);
            } else {
                String json = dataRequest.getResponseData();
                if (!TextUtils.isEmpty(json)) {
                    switch (dataRequest.getWhat()) {
                        case TAG_USERINFO:
                            List<JsonMap<String, Object>> data = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                            if (data != null && data.size() > 0) {
                                JsonMap<String, Object> jsonMap = data.get(0);
                                userInfoSobot = new UserInfoSobot();
                                userInfoSobot.setUserId(jsonMap.getStringNoNull("userId"));
                                userInfoSobot.setPhone(jsonMap.getStringNoNull("phone"));
                                userInfoSobot.setNickName(jsonMap.getStringNoNull("nickName"));
                                userInfoSobot.setEmail(jsonMap.getStringNoNull("email"));
                                userInfoSobot.setLogo(jsonMap.getStringNoNull("logo"));
                            }
                            break;
                    }
                }
            }
        }
    };


    /*****
     * 查看是否有进行中的订单
     */
    public void loadHasDoingTask() {
        if (!StringUtils.isLogin(activity)) {
            work_slid_layout.setVisibility(View.GONE);
        }
        if (!isFromOrder) {
            WorkbenchManager.getInstance(activity).loadHasDoingTask(new RequestCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean hasDoingTask) {
                    mHasDoingTask = hasDoingTask;
                    if (hasDoingTask) {
                        startLocation();
                        //有进行中的任务，直接到订单模式
                        ActingOrderModel();
                        isJustGoBackFromOrder = true;
                    } else {
                        startLocation();
                    }
                }

                @Override
                public void onError(String mag) {
                    ToastUtils.showToastShort(getContext(), mag);
                }
            });
        } else {
            startLocation();
            //有进行中的任务，直接到订单模式
            ActingOrderModel();
            isJustGoBackFromOrder = true;
        }
    }

    /**
     * 请求正在进行中的任务
     */
    private void getDoingTaskDetail() {
        if (isRequestTaskDetail) {
            return;
        }
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).startLoadingWorker();
            workerorder_title.setText("加载中");
        } else if (activity instanceof WorkOrderDetailActivity) {
            ((WorkOrderDetailActivity) activity).startLoadingWorker();
        }
        map.clear();
        isRequestTaskDetail = true;
        WorkbenchManager.getInstance(activity).getDoingTaskDetail(new RequestCallback<List<DoingTaskOrderDetailModel>>() {
            @Override
            public void onSuccess(List<DoingTaskOrderDetailModel> doingTaskOrderDetailModel) {
                isRequestTaskDetail = false;
                getTaskDetailCallBack(doingTaskOrderDetailModel);
            }

            @Override
            public void onError(String mag) {
                isRequestTaskDetail = false;
                ToastUtils.showToastShort(getContext(), mag);
            }
        });
    }

    /**
     * 正在进行中的任务请求返回
     *
     * @param doingTaskOrderDetailModel
     */
    private void getTaskDetailCallBack(final List<DoingTaskOrderDetailModel> doingTaskOrderDetailModel) {

        if (!isOrderMode && activity instanceof MainActivity) {
            return;
        }
        final List<DoingTaskOrderDetailModel> orderDetailModels = doingTaskOrderDetailModel;
        if (null == orderDetailModels || orderDetailModels.size() == 0) {
            return;
        }
//        guyuanBanner.setCanLoop(false);
        DoingTaskOrderDetailModel orderDetailModel = doingTaskOrderDetailModel.get(0);
        Glide.with(activity).load(orderDetailModel.logo).error(R.mipmap.icon_default_user).into(homeworkerIvOngoingorder);
        workerorder_layout.setVisibility(View.VISIBLE);
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).stopLoadingWorker();
        } else if (activity instanceof WorkOrderDetailActivity) {
            ((WorkOrderDetailActivity) activity).stopLoadingWorker();
        }
        TextView textView;
        if (null != tv_tital) {
            textView = tv_tital;
        } else {
            textView = workerorder_title;
        }
        cardPageAdapter = null;
        mWorkCardPage = null;
        mWorkCardPage = new MyViewPager(getActivity());
        mWorkCardPage.setId(R.id.worker_order_card);
        workerorder_layout.removeAllViews();
        workerorder_layout.addView(mWorkCardPage);

//        mWorkCardPage.destroyDrawingCache();
//        mWorkCardPage.removeAllViews();
        LatLng latLng = myLatlng == null ? MyApplication.mLocalLatlng : myLatlng;
        cardPageAdapter = new WorkCardPageAdapter(this.getChildFragmentManager(), orderDetailModels,
                latLng, textView, workMapManager, map, mRouteSearch);
        mWorkCardPage.setAdapter(cardPageAdapter);
        mWorkCardPage.setOffscreenPageLimit(1);
//        guyuanBanner.setPages(
//                new CBViewHolderCreator<WorkerCardBannerView>() {
//                    @Override
//                    public WorkerCardBannerView createHolder() {
//                        WorkerCardBannerView cardBannerView = new WorkerCardBannerView() {
//                            @Override
//                            public void deleteItem(int position) {
//                                if(orderDetailModels.size() == 1){
//                                    MainToWorkerFragmentEvent event = new MainToWorkerFragmentEvent("", 1);
//                                    EventBus.getDefault().post(event);
//                                }else {
//                                    orderDetailModels.remove(position);
//                                    guyuanBanner.notifyDataSetChanged();
//                                }
//                            }
//                        };
//                        LatLng latLng = myLatlng == null ? MyApplication.mLocalLatlng : myLatlng;
//
//                        if(null != tv_tital){
//                            cardBannerView.setLatlng(latLng,tv_tital);
//                        }else {
//                            cardBannerView.setLatlng(latLng,workerorder_title);
//                        }
//                        cardBannerView.setWorkMapManager(workMapManager, map, mRouteSearch);
//                        return cardBannerView;
//                    }
//                }, orderDetailModels);
    }

    /**
     * 从订单列表，推送，进来的。根据任务的id去查询任务详情
     *
     * @param businessNumber
     */
    private void getTaskDetailById(String businessNumber) {
        workerorder_layout.setVisibility(View.GONE);
//        if (loadingDialog == null)
//            loadingDialog = DialogUtils.showLoadingDialog(activity);
//        else {
//            loadingDialog.show();
//        }
        if (activity instanceof WorkOrderDetailActivity) {
            ((WorkOrderDetailActivity) activity).startLoadingWorker();
        }
        WorkbenchManager.getInstance(activity).getDoingTaskDetailById(businessNumber, new RequestCallback<DoingTaskOrderDetailModel>() {
            @Override
            public void onSuccess(DoingTaskOrderDetailModel doingTaskOrderDetailModel) {
                List<DoingTaskOrderDetailModel> orderDetailModels = new ArrayList<>();
                orderDetailModels.add(doingTaskOrderDetailModel);
                getTaskDetailCallBack(orderDetailModels);
            }

            @Override
            public void onError(String mag) {
                ToastUtils.showToastShort(getContext(), mag);
            }
        });
    }


    private void clearTaskMarks() {
        datasList.clear();
        for (int i = 0; i < markerList.size(); i++) {
            markerList.get(i).remove();
        }
        markerList.clear();
        if (myLatlng != null)
            workMapManager.addMineMark(activity, myLatlng);
    }

    /*******
     * 零工列表显示零工详情
     *
     * @param employeeJobId liukui 2014/04/25
     */
    public void setWorkDetail(String employeeJobId) {
        if (homeworkerVp != null && homeworkerVp.getVisibility() == View.GONE) {
            isCardIsLook = true;
            homeworkerVp.setVisibility(View.VISIBLE);
            homeworkerLayoutAddress.setVisibility(View.GONE);
            homeworkerLayoutBottom.setVisibility(View.GONE);
        }
        if (jobIdMap == null || !jobIdMap.containsKey(employeeJobId))
            return;

        Marker marker = jobIdMap.get(employeeJobId);
        int jobIndex = jobIndexMap.get(employeeJobId);
        homeworkerVp.setCurrentItem(jobIndex);
        LatLng latLng = marker.getPosition();
        mEndPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        setBigMarker(marker);
    }

    /*****
     * 获取附近的任务列表
     */
    private void loadMapsDatas() {

        // x坐标(经度)
        if (latLonMapCenterPoint == null) {
            return;
        }
//        if (com.ta.utdid2.android.utils.StringUtils.isEmpty(demandType)) {
//            demandType = "1";
//
//        }
        WorkbenchManager.getInstance(activity).getMapDatas(latLonMapCenterPoint, postType, demandType, new RequestCallback<DataRequest>() {
            @Override
            public void onSuccess(DataRequest o) {
                if (o instanceof DataRequest) {
                    DataRequest dataRequest = (DataRequest) o;
                    String json = dataRequest.getResponseData();
                    if (!TextUtils.isEmpty(json)) {
                        switch (dataRequest.getWhat()) {
                            case IntegerUtils.API_WORK_MAP_LIST:
                                if (isOrderMode) {
                                    return;
                                }
                                clearTaskMarks();
                                datasList.addAll(JsonParseHelper.getJsonMap_List_JsonMap(json, "data"));
                                if (datasList.size() == 0 && myLatlng != null && mislocation) {
//                                    ToastUtils.showToastShort(activity, "附近没有零工");
                                } else {
                                    centerLocationMarker1 = centerLocationMarker;
                                    setDatas();
                                }
                                isFirstLoadMapDada = false;
                                break;
                        }
                    }
                }
            }

            @Override
            public void onError(String mag) {
                ToastUtils.showToastShort(activity, mag);
            }
        });
    }

    private Map<String, Marker> jobIdMap = new HashMap<>();
    private Map<String, Integer> jobIndexMap = new HashMap<>();

    private void setDatas() {
        lastMarker = null;
        jobIdMap.clear();
        jobIndexMap.clear();

        for (int i = 0; i < datasList.size(); i++) {
            JsonMap<String, Object> jsonMap = datasList.get(i);
            double xOrdinates = jsonMap.getDouble("xCoordinate", 0);
            double yOrdinates = jsonMap.getDouble("yCoordinate", 0);
            String discribe = null;
            String postTypeName = jsonMap.getStringNoNull("postTypeName");
            if (!"志愿义工".equals(postTypeName)) {
                String jobMeterUnitName = jsonMap.getStr("jobMeterUnitName");
                if (!TextUtils.isEmpty(jobMeterUnitName)) {
                    discribe = jsonMap.getDouble("price", 0) + "\n元/" + jsonMap.getStr("jobMeterUnitName");
                } else {
                    discribe = "0\n元/单";
                }

            } else {
                discribe = "0\n元/单";
            }

            Marker marker = workMapManager.addNotInServiceTaskMark(getContext(), xOrdinates, yOrdinates, discribe);
            marker.setZIndex(i);
            jobIdMap.put(jsonMap.getString("id"), marker);
            jobIndexMap.put(jsonMap.getString("id"), i);
//            startScaleAnimation(marker);
            markerList.add(marker);
        }
        if (datasList.size() == 0) {
            return;
        }
        MapOrderPagerAdapter adapter = new MapOrderPagerAdapter(getFragmentManager(), datasList, handler);
        homeworkerVp.setAdapter(adapter);
        //不预加载页面不管用
        homeworkerVp.setOffscreenPageLimit(0);
    }

    private TextView tv_tital;

    /**
     * 雇员零工地图详情的标题
     */
    public void setTital(TextView tv_tital) {
        this.tv_tital = tv_tital;
    }

    private void startScaleBigAnimation(Marker marker) {
        Animation animation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f);
        animation.setDuration(400);
        marker.setAnimation(animation);
        marker.startAnimation();
    }
}

