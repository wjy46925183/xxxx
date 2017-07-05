package gongren.com.dlg.activity;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.fragment.BossFragment;
import gongren.com.dlg.fragment.BossListFragment;
import gongren.com.dlg.fragment.WorkerFragment;
import gongren.com.dlg.fragment.WorkerListFragment;
import gongren.com.dlg.javabean.LoginUserInfo;
import gongren.com.dlg.javabean.MainEvent;
import gongren.com.dlg.javabean.MainToBossFragmentEvent;
import gongren.com.dlg.javabean.MainToWorkerFragmentEvent;
import gongren.com.dlg.javabean.OrderCreateInfo;
import gongren.com.dlg.javabean.ReceiveReturnIdJson;
import gongren.com.dlg.javabean.RefreshEvent;
import gongren.com.dlg.javabean.TaskOfOrderInfo;
import gongren.com.dlg.javabean.UserInfomationData;
import gongren.com.dlg.javabean.UserMessageModel;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DlgUtils;
import gongren.com.dlg.utils.ExampleUtil;
import gongren.com.dlg.utils.GsonUtils;
import gongren.com.dlg.utils.IntegerUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.utils.WorkbenchManager;
import gongren.com.dlg.view.AlertView;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;
import spring.update.UpdateBuilder;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    //雇员进入订单的title
    @Bind(R.id.workerorder_hometitle)
    LinearLayout workerorder_title;
    @Bind(R.id.workerorder_mine)
    CircleImageView workerorderMine;
    @Bind(R.id.workerorder_title)
    TextView workerorderTitle;
    //雇员首页title
    @Bind(R.id.worker_title)
    LinearLayout worker_title;
    @Bind(R.id.worker_iv_mine)
    CircleImageView workerIvMine;
    @Bind(R.id.worker_tv_search)
    TextView workerTvSearch;
    @Bind(R.id.worker_iv_message)
    ImageView workerIvMessage;
    @Bind(R.id.worker_iv_show)
    ImageView workerIvShow;
    //雇主进入订单的title
    @Bind(R.id.boss_layout_ordertitle)
    LinearLayout boss_layout_ordertitle;
    @Bind(R.id.bossoder_layout)
    RelativeLayout bossoder_layout;
    @Bind(R.id.bossorder_mine)
    CircleImageView bossorderMine;
    @Bind(R.id.bossoder_titie)
    TextView bossoderTitie;
    @Bind(R.id.bossorder_ordername)
    TextView bossorderOrdername;
    @Bind(R.id.bossorder_img)
    ImageView bossorderImg;
    //雇主首页title
    @Bind(R.id.boss_layout_homepagetitle)
    RelativeLayout boss_layout_homepagetitle;
    @Bind(R.id.bossorder_batch)
    TextView bossorderBatch;
    @Bind(R.id.boss_iv_mine)
    CircleImageView bossIvMine;
    @Bind(R.id.boss_tv_search)
    ImageView bossTvSearch;
    @Bind(R.id.boss_iv_message)
    ImageView bossIvMessage;
    @Bind(R.id.iv_head)
    ImageView ivHead;
    @Bind(R.id.slid_name)
    TextView tvName;
    @Bind(R.id.slid_status)
    TextView tvStatus;

    @Bind(R.id.home_tablayout)
    TabLayout home_tablayout;
    @Bind(R.id.home_tablayout_boss)
    TabLayout home_tablayout_boss;
    @Bind(R.id.main_content_layout)
    FrameLayout mainContentLayout;
    //侧滑菜单的控件
    @Bind(R.id.slide_layout_head)
    RelativeLayout slide_layout_head;
    //抽屉布局
    @Bind(R.id.linearDrawer)
    LinearLayout linearDrawer;
    //抽屉控件
    @Bind(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.rb_01)
    RadioButton rb_01;
    @Bind(R.id.rb_02)
    RadioButton rb_02;

    @Bind(R.id.map_rg)
    RadioGroup mapRg;
    @Bind(R.id.left_draw)
    LinearLayout left_draw;
    @Bind(R.id.progress_bar_loading)
    ProgressBar boss_loading_bar;
    @Bind(R.id.worker_loading)
    ProgressBar worker_loading;

    @Bind(R.id.iamge_zhenwo)
    CircleImageView iamgeZhenwo; //真我
    @Bind(R.id.iamge_integral_mall)
    CircleImageView iamgeIntegralMall; //积分商城
    @Bind(R.id.iamge_recommended)
    CircleImageView iamgeRecommended; //推荐有奖

    private String title;
    private UserMessageModel userMessageModel;
    private AlertView alertView;

    //boss地图界面
    private BossFragment bossFragment;
    //worker地图界面
    private WorkerFragment workerFragment;
    //boss列表界面
    private BossListFragment bossListFragment;
    //worker列表界面
    private WorkerListFragment workerListFragment;
    //    private List<Fragment> fragments = new ArrayList<>();
    private FragmentManager manager;
    private FragmentTransaction transaction;
    //是否进入订单的标志
    private boolean isActingWorkerOrder = false;
    private boolean isActingBossOrder = false;
    //是否进入订单列表的标志
    private boolean isActingWorkerList = false;
    private boolean isActingBossList = false;

    //判断是雇主，雇员
    private boolean isGuYuan = true;//默认是雇员
    private String tempRole;

    //title的集合
    List<View> viewlist = new ArrayList<>();

    private String operatorTag = "worker";

    private String bossTag = "", workerTag = "";
    private final int TAG_REQUEST_SETT_PASS = 111;//查询是否设置支付密码
    private boolean isSettPassWord = false;
    private final String employeeType = "1", masterType = "2";
    private String businessNumber; //成欢迎页传过来的订单ID  订单ID 不为空时需要切换成雇员

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        UpdateBuilder.create().check(MainActivity.this);
        getUserInformationData();
        /**
         * 确保点击侧拉时 地图主页不受影响 不加此方法 会点击 真我 的时候 点击到地址栏
         */
        left_draw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        if (null != getIntent()) {
            boolean isLogout = getIntent().getBooleanExtra("isLogout", false);
            if (isLogout && !StringUtils.isLogin(this)) {//如果是退出以后进入主页
                startActivityForResult(new Intent(this, LoginDialogActivity.class), 0);
            }
            String againD = getIntent().getStringExtra("againD");
            if ("againD".equals(againD)) {
                EventBus.getDefault().post(new MainToWorkerFragmentEvent("", 13));
            }
        }
        businessNumber = getIntent().getStringExtra("businessNumber");
        if (!TextUtils.isEmpty(businessNumber)) {
            String UserRole = SharedPreferencesUtils.getString(this, SharedPreferencesUtils.UserRole);
            if (!TextUtils.isEmpty(UserRole) && UserRole.equals(masterType)) {
                SharedPreferencesUtils.saveString(this, SharedPreferencesUtils.UserRole, employeeType);
            }
            WorkOrderDetailActivity.open(this, businessNumber);
        }
        initView();
        initModel();
        loadDatas();
        registerMessageReceiver();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case IntegerUtils.BOSSORDER_SELECT:
                    //雇主，标题下拉框，选中之后，在标题显示数据,以及地图和底部卡片的变化。
                    //批量信息
                    if (msg.obj instanceof JsonMap) {
                        JsonMap<String, Object> map = (JsonMap<String, Object>) msg.obj;
                        bossorderBatch.setTag(map.getInt("id"));
                        //设置标题
                        setTitleData(map);
                    }
                    break;
            }
        }
    };

    private void initModel() {
        userMessageModel = new UserMessageModel();
    }


    private void setTitleData(JsonMap<String, Object> map) {
        if (map == null) {
            return;
        }
        String orderStatus = map.getStringNoNull("status");
        String num = map.getStringNoNull("num");
        bossoderTitie.setText(orderStatus + "(" + num + ")");

        String zhiwei = map.getStringNoNull("title");
        bossorderOrdername.setText(zhiwei);
    }

    public Handler getHandler() {
        return this.handler;
    }

    /**
     * 初始化布局
     */
    private void initView() {

        Glide.with(this).load(GetDataConfing.BASEURL + "/static/images/zw.jpg").fitCenter().override(200, 200).error(R.mipmap.ic_launcher)
                .into(iamgeZhenwo);
        Glide.with(this).load(GetDataConfing.BASEURL + "/static/images/jfsc.jpg").fitCenter().override(200, 200).error(R.mipmap.ic_launcher)
                .into(iamgeIntegralMall);
        Glide.with(this).load(GetDataConfing.BASEURL + "/static/images/tjyj.jpg").fitCenter().override(200, 200).error(R.mipmap.ic_launcher)
                .into(iamgeRecommended);

        //初始化Fragment
        initFragment();
        //初始化侧滑菜单
        initDrawer();
        //初始化title集合
        viewlist.add(workerorder_title);
        viewlist.add(worker_title);
        viewlist.add(boss_layout_homepagetitle);
        viewlist.add(boss_layout_ordertitle);
        //显示雇员首页title
        //        setHomeTitle(0);
        //初始化界面监听
        initListener();
        //验证用户是否登录，新增用户登录状态
        if (StringUtils.isLogin(context)) {
            WorkbenchManager.getInstance(context).addUserLogin();
        }
    }

    //下级Fragment发来的消息
    @Subscribe
    public void onMessageEvent(RefreshEvent event) {
        switch (event.getTag()) {
            case RefreshEvent.MINE_PAGE_DATA:
                isBoss = !isBoss;
                break;
        }
    }

    //下级Fragment发来的消息
    @Subscribe
    public void onMessageEvent(MainEvent event) {
        switch (event.getTag()) {
            case 0:
                operatorTag = "worker";
                //显示雇员地图
                setHomeTitle(worker_title);
                if (null != workerFragment) {
                    workerFragment.setPostType(workerTag);
                }
//                manager.beginTransaction().replace(R.id.main_content_layout, workerFragment).commit();
                IsVisitableTabLayout(1);
                isActingWorkerOrder = false;
                isActingWorkerList = false;
                break;
            case 1:
                operatorTag = "boss";
                //显示雇主地图
                setHomeTitle(boss_layout_homepagetitle);
                if (null != bossFragment) {
                    bossFragment.setPostType(bossTag);
                }
                manager.beginTransaction().replace(R.id.main_content_layout, bossFragment).commitAllowingStateLoss();
                IsVisitableTabLayout(2);
                isActingBossOrder = false;
                isActingBossList = false;
                String msg = event.getMsg();
                if (bossFragment != null && "isSwitchJoe".equals(msg)) {
                    bossFragment.sendT("isSwitchJoe");
                }
                break;
            case 2:
                //显示雇员列表
                setHomeTitle(worker_title);
                int selectedTabPosition = home_tablayout.getSelectedTabPosition();
                TabLayout.Tab tab = home_tablayout.getTabAt(selectedTabPosition);
                JsonMap<String, Object> jsonMap = tab.getTag() instanceof JsonMap ? (JsonMap<String, Object>) tab.getTag() : new JsonMap<String, Object>();
                String postType = jsonMap.getString("dataCode");
                if (null != workerListFragment) {
                    workerListFragment.setPostType(postType);
                }

                manager.beginTransaction().replace(R.id.main_content_layout, workerListFragment).commit();
                IsVisitableTabLayout(1);
                isActingWorkerOrder = false;
                isActingWorkerList = true;
                break;

            case 3:
                //显示雇主列表
                setHomeTitle(boss_layout_homepagetitle);
                if (null != bossListFragment) {
                    bossListFragment.setPostType(workerTag);
                }
                //                manager.beginTransaction().replace(R.id.main_content_layout, bossListFragment).commit();
                manager.beginTransaction().replace(R.id.main_content_layout, bossListFragment).commitAllowingStateLoss();
                IsVisitableTabLayout(2);
                isActingBossOrder = false;
                isActingBossList = true;

                break;

            case 4://雇主地图首页，滚轮进来的。
                //显示雇主进入订单
                isActingBossOrder = true;
                doTitle(event);//修改标题
                setHomeTitle(boss_layout_ordertitle);
                if (null != bossFragment) {
                    bossFragment.setPostType(bossTag);
                }
                manager.beginTransaction().replace(R.id.main_content_layout, bossFragment).commitAllowingStateLoss();
                IsVisitableTabLayout(0);
                break;

            case 5:
                //显示雇员进入订单
                if (null != workerFragment) {
                    workerFragment.setPostType(workerTag);
                }
                isActingWorkerOrder = true;
                setHomeTitle(workerorder_title);
                manager.beginTransaction().replace(R.id.main_content_layout, workerFragment).commitAllowingStateLoss();
                IsVisitableTabLayout(0);
                break;

            case 6:
                //订单列表消失的操作
                openInfoAnimation(OrderVisiable);
                OrderVisiable = true;
                break;

            case 7:
                //雇员零工详情
                //得到订单id。
                //从列表里找出要显示的fragment,显示出来。
                setHomeTitle(worker_title);
                if (null != workerFragment) {
                    workerFragment.setPostType(workerTag);
                }
                manager.beginTransaction().replace(R.id.main_content_layout, workerFragment).commitAllowingStateLoss();
                IsVisitableTabLayout(1);
                isActingWorkerOrder = false;
                isActingWorkerList = false;
                workerFragment.setWorkDetail(event.getMsg());
                break;
            case 14:
                String index = event.getMsg();
                if (index != null) {
                    //int i = Integer.parseInt(index);
                    if (title != null) {
                        TaskOfOrderInfo taskOfOrderInfo = new Gson().fromJson(title, TaskOfOrderInfo.class);
                        if (taskOfOrderInfo != null) {
                            if (taskOfOrderInfo.getData().size() > 0) {
                                for (int i = 0; i < taskOfOrderInfo.getData().size(); i++) {
                                    for (int j = 0; j < taskOfOrderInfo.getData().get(i).getList().size(); j++) {
                                        OrderCreateInfo orderCreateInfo = taskOfOrderInfo.getData().get(i).getList().get(j);
                                        if (orderCreateInfo.getJobId().equals(index)) {
                                            int status = taskOfOrderInfo.getData().get(i).getStatus();
                                            int usedNum = taskOfOrderInfo.getData().get(i).getUsedNum();

                                            if (status > 0) {
                                                if (10 == status) {
                                                    bossoderTitie.setText("有人接单(" + usedNum + ")");
                                                } else if (30 == status) {
                                                    bossoderTitie.setText("待付款(" + usedNum + ")");
                                                } else if (40 == status) {
                                                    bossoderTitie.setText("已完成(" + usedNum + ")");
                                                } else {
                                                    bossoderTitie.setText("等待验收(" + usedNum + ")");
                                                }
                                            }
                                            if ("0".equals(taskOfOrderInfo.getData().get(i).getPrice())) {
                                                bossorderOrdername.setText("志愿义工");
                                            } else {
                                                bossorderOrdername.setText(taskOfOrderInfo.getData().get(i).getPostName()
                                                        + " " + taskOfOrderInfo.getData().get(i).getPrice()
                                                        + "元/" + taskOfOrderInfo.getData().get(i).getJobMeterUnitName());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                break;
        }
    }

    private void doTitle(MainEvent event) {
        title = event.getMsg();
        int position = event.getPosition();
        setOrderTiltle(position);
    }

    private void setOrderTiltle(int position) {
        if (title != null) {
            TaskOfOrderInfo taskOfOrderInfo = new Gson().fromJson(title, TaskOfOrderInfo.class);
            if (taskOfOrderInfo != null) {
                if (taskOfOrderInfo.getData().size() > 0) {
                    TaskOfOrderInfo.DataBean dataBean = taskOfOrderInfo.getData().get(position);
                    int status = dataBean.getStatus();
                    int usedNum = dataBean.getUsedNum();
                    if (status > 0) {
                        if (10 == dataBean.getStatus()) {
                            bossoderTitie.setText("有人接单(" + usedNum + ")");
                        } else if (30 == status) {
                            bossoderTitie.setText("待付款(" + usedNum + ")");
                        } else {
                            bossoderTitie.setText("等待验收(" + usedNum + ")");
                        }
                    }
                    if ("0".equals(dataBean.getPrice())) {
                        bossorderOrdername.setText("志愿义工");
                    } else {
                        bossorderOrdername.setText(dataBean.getPostName() + " " + dataBean.getPrice() + "元/" + dataBean.getJobMeterUnitName());
                    }

                }
            }
        }
    }

    /**
     * 初始化Fragment数据
     */
    private void initFragment() {
        manager = getSupportFragmentManager();
        bossFragment = new BossFragment();//雇主的工作台
        bossListFragment = new BossListFragment();//雇主列表页
        workerFragment = new WorkerFragment();//雇员的工作台
        workerListFragment = new WorkerListFragment();//雇员列表页
        String userRole = SharedPreferencesUtils.getString(this, SharedPreferencesUtils.UserRole);//进入主页，验证登录身份
        if (employeeType.equals(userRole) || TextUtils.isEmpty(userRole))//是雇员身份或则游客身份
        {
            MyApplication.isBoss = false;
            SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.UserRole, employeeType + "");//存储当前角色状态值
            operatorTag = "worker";
            //            manager.beginTransaction().replace(R.id.main_content_layout, workerFragment).commit();//默认加载的是雇员
            manager.beginTransaction().replace(R.id.main_content_layout, workerFragment).commitAllowingStateLoss();//默认加载的是雇员
            rb_01.setChecked(true);//侧滑菜单中 雇员被选中
            worker_title.setVisibility(View.VISIBLE);//雇员tital隐藏
            boss_layout_homepagetitle.setVisibility(View.GONE);
            slide_layout_head.setBackgroundResource(R.mipmap.bg_findorder);
            isGuYuan = true;
        } else {
            MyApplication.isBoss = true;
            SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.UserRole, masterType + "");//存储当前角色状态值
            operatorTag = "boss";
            //            manager.beginTransaction().replace(R.id.main_content_layout, bossFragment).commit();//加载的是雇主身份
            manager.beginTransaction().replace(R.id.main_content_layout, bossFragment).commitAllowingStateLoss();//加载的是雇主身份
            rb_02.setChecked(true);//侧滑菜单中 雇主被选中
            worker_title.setVisibility(View.GONE);//雇员tital隐藏
            boss_layout_homepagetitle.setVisibility(View.VISIBLE);//雇主标题显示
            slide_layout_head.setBackgroundResource(R.mipmap.bg_gohring);
            isGuYuan = false;
        }
    }

    private boolean isBoss;

    private void initDrawer() {
        mDrawerLayout.setFocusableInTouchMode(true);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        linearDrawer.setLayoutParams(new DrawerLayout.LayoutParams((int) (metric.widthPixels * 0.67),
                metric.heightPixels, Gravity.START));
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                isBoss = !rb_01.isChecked();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                if (isBoss == !rb_01.isChecked()) {
                    return;
                }
                if (!rb_01.isChecked()) {
                    if (isActingBossList) {
                        fragment = bossListFragment;
                    } else {
                        fragment = bossFragment;
                    }
                    if (!fragment.isResumed()) {
                        manager.beginTransaction().replace(R.id.main_content_layout, fragment).commitAllowingStateLoss();
                    }
                    //是否进入订单 是和否
                    if (isActingBossOrder) {
                        setHomeTitle(boss_layout_ordertitle);
                        IsVisitableTabLayout(0);
                        EventBus.getDefault().post(new MainToBossFragmentEvent(bossTag, 0));
                    } else {
                        setHomeTitle(boss_layout_homepagetitle);
                        IsVisitableTabLayout(2);
                        EventBus.getDefault().post(new MainToBossFragmentEvent(bossTag, 1));
                    }
                    bossFragment.sendT("isSwitchJoe");
                } else {
                    if (isActingWorkerList) {
                        fragment = workerListFragment;
                    } else {
                        fragment = workerFragment;
                    }
                    if (!fragment.isResumed()) {
                        manager.beginTransaction().replace(R.id.main_content_layout, fragment).commitAllowingStateLoss();//雇员列表
                    }
                    //是否进入订单
                    if (isActingWorkerOrder) {
                        setHomeTitle(workerorder_title);
                        IsVisitableTabLayout(0);
                        EventBus.getDefault().post(new MainToWorkerFragmentEvent(workerTag, 0));
                    } else {
                        setHomeTitle(worker_title);
                        IsVisitableTabLayout(1);
                        EventBus.getDefault().post(new MainToWorkerFragmentEvent(workerTag, 1));
                    }
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    private Fragment fragment;

    private void initListener() {
        //地图的点击监听
        mapRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_01) {//雇员
                    if (isGuYuan) {
                        return;
                    }
                    if (null != fragment) {
                        manager.beginTransaction().remove(fragment);
                    }
                    tempRole = employeeType;
                    isGuYuan = true;
                    MyApplication.isBoss = false;
                    slide_layout_head.setBackgroundResource(R.mipmap.bg_findorder);
                } else { //雇主
                    if (!isGuYuan) {
                        return;
                    }
                    if (null != fragment) {
                        manager.beginTransaction().remove(fragment);
                    }
                    tempRole = masterType;
                    isGuYuan = false;
                    MyApplication.isBoss = true;
                    //判断加载列表还是地图
                    slide_layout_head.setBackgroundResource(R.mipmap.bg_gohring);
                }
                SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.UserRole, tempRole + "");//存储当前角色状态值
            }
        });
        //tab的监听 雇员，雇主
        home_tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (rb_01.isChecked()) {//雇员
                    if (tab.getTag() == null && workerTag.equals("")) {
                        workerTag = "";
                        EventBus.getDefault().post(new MainToWorkerFragmentEvent("", 2));
                    } else {
                        JsonMap<String, Object> jsonMap = tab.getTag() instanceof JsonMap ? (JsonMap<String, Object>) tab.getTag() : new JsonMap<String, Object>();
                        workerTag = jsonMap.getString("dataCode");
                        EventBus.getDefault().post(new MainToWorkerFragmentEvent(workerTag, 2));
                    }
                } else {
                    if (tab.getTag() == null) {
                        bossTag = "";
                        EventBus.getDefault().post(new MainToBossFragmentEvent("", 2));
                    } else {
                        JsonMap<String, Object> jsonMap = tab.getTag() instanceof JsonMap ? (JsonMap<String, Object>) tab.getTag() : new JsonMap<String, Object>();
                        bossTag = jsonMap.getString("dataCode");
                        EventBus.getDefault().post(new MainToBossFragmentEvent(bossTag, 2));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        home_tablayout_boss.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag() == null) {
                    bossTag = "";
                    EventBus.getDefault().post(new MainToBossFragmentEvent("", 2));
                } else {
                    JsonMap<String, Object> jsonMap = tab.getTag() instanceof JsonMap ? (JsonMap<String, Object>) tab.getTag() : new JsonMap<String, Object>();
                    bossTag = jsonMap.getString("dataCode");
                    EventBus.getDefault().post(new MainToBossFragmentEvent(bossTag, 2));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void onSwitchRoleCallBack() {
        //和登录页面和修改密码界面保持一致 保存形式为String形式

    }

    //显示雇主的tab还是雇员的tab
    private void IsVisitableTabLayout(int index) {
        switch (index) {
            case 0:
                home_tablayout.setVisibility(View.GONE);
                home_tablayout_boss.setVisibility(View.GONE);
                break;
            case 1:
                home_tablayout.setVisibility(View.VISIBLE);
                home_tablayout_boss.setVisibility(View.GONE);
                break;
            case 2:
                home_tablayout.setVisibility(View.GONE);
                home_tablayout_boss.setVisibility(View.VISIBLE);
                break;
        }
    }

    private boolean OrderVisiable = true;

    @OnClick({R.id.workerorder_mine, R.id.worker_iv_mine, R.id.worker_tv_search,
            R.id.worker_iv_message, R.id.bossorder_mine, R.id.bossoder_layout,
            R.id.bossorder_batch, R.id.boss_iv_mine, R.id.boss_tv_search,
            R.id.boss_iv_message, R.id.iv_head, R.id.slide_linggong, R.id.slide_qianbao,
            R.id.slide_shezhi, R.id.slid_name, R.id.ll_zhenwo, R.id.layout_integral_mall
            , R.id.layout_recommended})
    public void onClick(View view) {
        String authStatus = SharedPreferencesUtils.getString(context, SharedPreferencesUtils.STATUS);
        switch (view.getId()) {
            case R.id.layout_integral_mall:
                if (StringUtils.isLogin(this) && authStatus.equals("2")) {
                    Intent i = new Intent(context, WebViewPageActivity.class);
                    i.putExtra("functionTitle", "福利社");
                    i.putExtra("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
                    i.putExtra("contentUrl", GetDataConfing.INTEGRAL_MALL);
                    i.putExtra("type", "8");
                    startActivity(i);
                } else {
                    if (!StringUtils.isLogin(this)) {
                        startActivityForResult(new Intent(this, LoginDialogActivity.class), 0);
                    } else {
                        if (!authStatus.equals("2")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("请进行实名认证")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startActivity(new Intent(context, RealNameAuthenticationActivity.class));
                                        }
                                    })
                                    .create().show();
                        }
                    }

                }
                break;
            case R.id.layout_recommended:
                if (StringUtils.isLogin(this) && authStatus.equals("2")) {
                    Intent i = new Intent(context, WebViewPageActivity.class);
                    i.putExtra("functionTitle", "推荐有奖");
                    i.putExtra("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
                    i.putExtra("contentUrl", GetDataConfing.ZHEN_WO);
                    i.putExtra("type", "7");
                    startActivity(i);
                } else {
                    if (!StringUtils.isLogin(this)) {
                        startActivityForResult(new Intent(this, LoginDialogActivity.class), 0);
                    } else {
                        if (!authStatus.equals("2")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("请进行实名认证")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startActivity(new Intent(context, RealNameAuthenticationActivity.class));
                                        }
                                    })
                                    .create().show();
                        }
                    }

                }
                break;
            case R.id.worker_tv_search:
                //雇员首页的搜索(历史搜索和热门)
                startActivity(new Intent(context, WorkerSearchActivity.class));
                break;
            case R.id.worker_iv_message:
                if (StringUtils.isLogin(this)) {
                    if (SharedPreferencesUtils.getString(this, SharedPreferencesUtils.RENZHENG_STATUS).equals("2")) {
                        //雇员首页的消息
                        Intent employeeIntent = new Intent(context, NewsActivity.class);
                        employeeIntent.putExtra("userRole", isGuYuan);
                        startActivity(employeeIntent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("请进行实名认证")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startActivity(new Intent(context, RealNameAuthenticationActivity.class));
                                    }
                                })
                                .create().show();
                    }
                } else {
                    startActivityForResult(new Intent(this, LoginDialogActivity.class), 0);
                }
                break;

            case R.id.bossoder_layout:
                //点击出现箭头动画，显示弹窗
                if (OrderVisiable) {
                    openInfoAnimation(OrderVisiable);
                    OrderVisiable = false;
                    EventBus.getDefault().post(new MainToBossFragmentEvent("打开", 3));
                } else {
                    openInfoAnimation(OrderVisiable);
                    OrderVisiable = true;
                    EventBus.getDefault().post(new MainToBossFragmentEvent("关闭", 4));
                }
                break;

            case R.id.bossorder_batch:
                //雇主单子批量处理申请人员
                Intent intent = new Intent(context, OrderWorkerActivity.class);
                intent.putExtra("isGuYuan", false);
                startActivity(intent);
                break;

            case R.id.workerorder_mine:
            case R.id.worker_iv_mine:

            case R.id.bossorder_mine:
            case R.id.boss_iv_mine:
                if (mDrawerLayout != null)
                    mDrawerLayout.openDrawer(GravityCompat.START);
                if (StringUtils.isLogin(this)) {
                    rb_01.setText(getResources().getString(R.string.guyuan));
                    rb_02.setText(getResources().getString(R.string.guzhu));
                    tvStatus.setVisibility(View.VISIBLE);
                    if ("2".equals(SharedPreferencesUtils.getString(this, SharedPreferencesUtils.RENZHENG_STATUS))) {
                        tvStatus.setText(R.string.has_yanzheng);
                    } else {
                        tvStatus.setText(R.string.no_yanzheng);
                    }
                } else {
                    rb_01.setText(R.string.guyuan);
                    rb_02.setText(R.string.guzhu);
                }
                break;
            case R.id.boss_tv_search:
                //雇主首页的搜索
                break;
            case R.id.boss_iv_message:
                if (StringUtils.isLogin(this)) {
                    //雇主首页的消息
                    Intent employerIntent = new Intent(context, NewsActivity.class);
                    employerIntent.putExtra("userRole", "1");//0代表雇员 1代表雇主
                    startActivity(employerIntent);
                } else {
                    startActivityForResult(new Intent(this, LoginDialogActivity.class), 0);
                }
                break;
            case R.id.slid_name:
            case R.id.iv_head:        //个人资料
                if (StringUtils.isLogin(this)) {
                   /* if (SharedPreferencesUtils.getBoolean(context,"iscompany")){
                        startActivity(new Intent(this,CompanyInfoActivity.class));
                    }else{
                        go2UserInfo();
                    }*/
                    //startActivity(new Intent(this,CompanyInfoActivity.class));
                    go2UserInfo();

                } else {
                    startActivityForResult(new Intent(this, LoginDialogActivity.class), 0);
                }
                break;
            case R.id.slide_linggong:      //我的零工
                if (StringUtils.isLogin(this)) {
                    if (!SharedPreferencesUtils.getString(context, SharedPreferencesUtils.STATUS).equals("2")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("请进行实名认证")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startActivity(new Intent(context, RealNameAuthenticationActivity.class));
                                    }
                                })
                                .create().show();
                    } else {
                        go2LGList(isGuYuan);
                    }
                } else {
                    startActivityForResult(new Intent(this, LoginDialogActivity.class), 0);
                }
                break;
            case R.id.slide_qianbao:      //我的钱包
                //打包的时候，一定要加上对实名认证的判断
                if (StringUtils.isLogin(this)) {
                    if (authStatus.equals("2") && isSettPassWord) {
                        startActivity(new Intent(context, MyWalletActivity.class));
                    } else {
                        if (!authStatus.equals("2")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("请进行实名认证")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startActivity(new Intent(context, RealNameAuthenticationActivity.class));
                                        }
                                    })
                                    .create().show();
                        } else if (!isSettPassWord) {
                            startActivity(new Intent(context, SetPayPasswordActivity.class).putExtra("type", 1));

                        }
                    }
                } else {
                    startActivityForResult(new Intent(this, LoginDialogActivity.class), 0);
                }
                break;
            case R.id.slide_shezhi:      //设置
                if (StringUtils.isLogin(this)) {
                    startActivity(new Intent(context, SettingsActivity.class));
                } else {
                    startActivityForResult(new Intent(this, LoginDialogActivity.class), 0);
                }
                break;

            case R.id.ll_zhenwo:
                if (StringUtils.isLogin(this) && authStatus.equals("2")) {
                    Intent i = new Intent(context, WebUtilsActivity.class);
                    i.putExtra("functionTitle", "真我");
                    i.putExtra("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
                    i.putExtra("contentUrl", GetDataConfing.ZHEN_WO);
                    i.putExtra("type", "6");
                    startActivity(i);
                } else {
                    if (!StringUtils.isLogin(this)) {
                        startActivityForResult(new Intent(this, LoginDialogActivity.class), 0);
                    } else {
                        if (!authStatus.equals("2")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("请进行实名认证")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startActivity(new Intent(context, RealNameAuthenticationActivity.class));
                                        }
                                    })
                                    .create().show();
                        }
                    }

                }
                break;
        }
    }

    private void isSettPaymentPass() {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));

        DataUtils.loadData(context, GetDataConfing.CHECK_PAYMENT_PASS, map, TAG_REQUEST_SETT_PASS, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    if (!TextUtils.isEmpty(json)) {
                        if (dataRequest.getWhat() == TAG_REQUEST_SETT_PASS) {
                            JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                            if (jsonMap.getString("code").equals("0")) {
                                ReceiveReturnIdJson receiveReturnIdJson = GsonUtils.jsonToBean(json, ReceiveReturnIdJson.class);
                                if (receiveReturnIdJson.data.get(0).equals("true")) {
                                    isSettPassWord = true;
                                    SharedPreferencesUtils.saveBoolean(context, SharedPreferencesUtils.havePayPwd, true);
                                } else {
                                    isSettPassWord = false;
                                    SharedPreferencesUtils.saveBoolean(context, SharedPreferencesUtils.havePayPwd, false);
                                }
                            }
                        }

                    }
                }
            }
        });
    }

    /**
     * 请求用户信息
     */
    public void getUserInformationData() {
        Map<String, Object> map = new HashMap<>();
        map.put("format", "json");
        DataUtils.loadData(context, GetDataConfing.get_user_information, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataRequest.getResponseData());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    UserInfomationData userInfomationData = new Gson().fromJson(jsonArray.getString(0), UserInfomationData.class);
                    bindViewData(userInfomationData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 用户资料数据绑定到视图
     *
     * @param userInfomationData
     */
    //TODO
    public void bindViewData(UserInfomationData userInfomationData) {
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.MOBILE, userInfomationData.userRestVo.phone);
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.TOKEN, "");
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.CERTIFICATENUMBER,
                userInfomationData.userAttributeRestVo.certificateNumber);
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.REAL_NAME,
                userInfomationData.userAttributeRestVo.name);
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.STATUS,
                userInfomationData.userRestVo.auditStatus);
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.UserName,
                userInfomationData.userRestVo.username);
        Log.e("nameSP", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.UserName) + "");
        Log.e("nameSP", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.REAL_NAME) + "");

        if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.UserName))) {
            tvName.setText(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.UserName));
        } else if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.REAL_NAME))) {
            tvName.setText(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.REAL_NAME));
        } else {
            tvName.setText(DlgUtils.showMidHintPhone(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.MOBILE)));
        }
        if ("2".equals(userInfomationData.userRestVo.auditStatus)) {
            tvStatus.setText(R.string.has_yanzheng);
        } else {
            tvStatus.setText(R.string.no_yanzheng);
        }
    }

    public void go2UserInfo() {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("isguyuan", isGuYuan);
        startActivity(intent);
    }

    /**
     * 展开详细信息动画
     */
    private void openInfoAnimation(boolean flag) {
        ObjectAnimator.ofFloat(bossorderImg, "rotationX", 0.0F, 180.0F)//
                .setDuration(500)
                .start();
        if (!flag) {
            //显示
            bossorderImg.setImageResource(R.mipmap.icon_ordertitle_up);
        } else {
            bossorderImg.setImageResource(R.mipmap.icon_ordertitle_down);
        }
    }

    //从侧边栏进入我的零工列表，判断，是进入雇员的，还是雇主的。
    public void go2LGList(boolean isGuYuan) {
        Intent intent = new Intent(context, OrderWorkerActivity.class);
        intent.putExtra("isGuYuan", isGuYuan);
        startActivity(intent);
    }

    /*****
     * 预加载网络数据
     */
    private void loadDatas() {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("groupCode", "job.type");
        // map.put("sign", "1"); //不传表示查询所有
        DataUtils.loadData(context, GetDataConfing.COMMON_HOT_CRAFT, map, IntegerUtils.API_COMMON_CRAFT_LIST, responseDataCallback);
        String mobile = SharedPreferencesUtils.getString(context, SharedPreferencesUtils.MOBILE);
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.TOKEN, "");
        String pwd = SharedPreferencesUtils.getString(context, SharedPreferencesUtils.PASSWORD);


        if (mobile != null && !mobile.equals("") && pwd != null && !pwd.equals("")) {
            //重新获取用户基本信息
            map = BaseMapUtils.getMap(context);
            map.put("type", String.valueOf(1));
            map.put("principal", mobile);
            map.put("credentials", pwd);
            DataUtils.loadData(context, GetDataConfing.SYSTEM_LOGIN, map, IntegerUtils.API_PSD_LOGIN, responseDataCallback);
        }

        if (!StringUtils.isLogin(context))
            return;
        //获取未读信息总条数
        userMessageModel.queryUnReadMessageCounts(this, IntegerUtils.API_UNREAD_MESSAGE, responseDataCallback);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawers();
            } else {
                if (System.currentTimeMillis() - exitTime > 2000) {
                    ToastUtils.showToastShort(this, "再按一下退出程序");
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击返回键收回侧滑菜单
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int tag = intent.getIntExtra("tag", -1);
        if (tag == 0) {
            if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawers();
            }
        }
        businessNumber = intent.getStringExtra("businessNumber");
        if (!TextUtils.isEmpty(businessNumber)) {
            String UserRole = SharedPreferencesUtils.getString(this, SharedPreferencesUtils.UserRole);
            if (!TextUtils.isEmpty(UserRole) && UserRole.equals(masterType)) {
                SharedPreferencesUtils.saveString(this, SharedPreferencesUtils.UserRole, employeeType);
                initFragment();
            }
            WorkOrderDetailActivity.open(this, businessNumber);
        }
    }

    /***********
     * 设置需要显示的title
     * 0.worker_hometitle，1.worker_ordertitle，
     * 2.boss_layout_homepagetitle，3.boss_layout_ordertitle
     */
    private void setHomeTitle(View layout) {
        for (int i = 0; i < viewlist.size(); i++) {
            if (viewlist.get(i) == layout) {
                viewlist.get(i).setVisibility(View.VISIBLE);
            } else {
                viewlist.get(i).setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!StringUtils.isLogin(context)) {
            tvName.setText("登录");
            workerIvMine.setImageResource(R.mipmap.ic_wode);
            bossIvMine.setImageResource(R.mipmap.ic_wode);
            return;
        }
        String logo = SharedPreferencesUtils.getString(context, SharedPreferencesUtils.FILELOGO);
        Glide.with(this).load(logo).fitCenter().override(200, 200).error(R.mipmap.ic_launcher)
                .into(ivHead);
        Glide.with(this).load(logo).fitCenter().override(200, 200).error(R.mipmap.ic_launcher)
                .into(workerorderMine);
        Glide.with(this).load(logo).fitCenter().override(200, 200).error(R.mipmap.ic_launcher)
                .into(workerIvMine);
        Glide.with(this).load(logo).fitCenter().override(200, 200).error(R.mipmap.ic_launcher)
                .into(bossorderMine);
        Glide.with(this).load(logo).fitCenter().override(200, 200).error(R.mipmap.ic_launcher)
                .into(bossIvMine);
        if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(this, SharedPreferencesUtils.UserName))) {
            tvName.setText(SharedPreferencesUtils.getString(this, SharedPreferencesUtils.UserName));
            Log.e("dothis", "~~~~~~~6");
        } else if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(this, SharedPreferencesUtils.REAL_NAME))) {
            tvName.setText(SharedPreferencesUtils.getString(this, SharedPreferencesUtils.REAL_NAME));
            Log.e("dothis", "~~~~~~~7");
        } else {
            tvName.setText(DlgUtils.showMidHintPhone(SharedPreferencesUtils.getString(this, SharedPreferencesUtils.MOBILE)));
            Log.e("dothis", "~~~~~~~8");
        }

        if ("2".equals(SharedPreferencesUtils.getString(this, SharedPreferencesUtils.RENZHENG_STATUS))) {
            tvStatus.setText(R.string.has_yanzheng);
            Log.e("dothis", "~~~~~~~9");
        } else {
            tvStatus.setText(R.string.no_yanzheng);
            Log.e("dothis", "~~~~~~~10");
        }
        isSettPaymentPass();
        //setName();
    }

    /*private void setName() {
        if (StringUtils.isLogin(this)) {
            if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.UserName))) {
                tvName.setText(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.UserName));
                return;
            }
            if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.REAL_NAME))) {
                tvName.setText(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.REAL_NAME));
                return;
            }
            if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.MOBILE))) {
                tvName.setText(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.MOBILE));
            }
        }
    }
*/
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(context);
            } else {
                String json = dataRequest.getResponseData();
                if (!TextUtils.isEmpty(json)) {
                    switch (dataRequest.getWhat()) {

                        case IntegerUtils.API_COMMON_CRAFT_LIST:
                            List<JsonMap<String, Object>> jsonMap = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                            upLoadTableHead(jsonMap);
                            break;

                        case IntegerUtils.API_PSD_LOGIN:
                            //账号密码登录
                            resultLogin(json);
                            break;

                        case IntegerUtils.API_UPDATE_INFORMATION:
                            try {
                                JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());
                                int code = jsonObject.getInt("code");
                                if (code == 0) {
                                    onSwitchRoleCallBack();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;

                        case IntegerUtils.API_UNREAD_MESSAGE:
                            //获取未读信息总条数
                            JsonMap<String, Object> map = JsonParseHelper.getJsonMap(json);
                            if ((map.get("data") instanceof JSONArray ? ((JSONArray) map.get("data")).optInt(0) : map.getInt("data")) > 0)
                                workerIvShow.setVisibility(View.VISIBLE);
                            else
                                workerIvShow.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        }
    };

    private void upLoadTableHead(List<JsonMap<String, Object>> jsonMap) {
        //初始化tabhost
        if (jsonMap != null && jsonMap.size() > 0) {
            for (int i = 0; i < jsonMap.size(); i++) {
                JsonMap<String, Object> map = jsonMap.get(i);

                TabLayout.Tab tabWork = home_tablayout.newTab().setText(map.getString("dataName"));
                tabWork.setTag(map);
                home_tablayout.addTab(tabWork);

                TabLayout.Tab tabBoos = home_tablayout_boss.newTab().setText(map.getString("dataName"));
                tabBoos.setTag(map);
                home_tablayout_boss.addTab(tabBoos);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //友盟分享，若果在fragment中实现分享，需要在activity中进行调用这个
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (operatorTag.equals("worker"))
                        EventBus.getDefault().post(new MainToWorkerFragmentEvent("reLoad", 3));
                    else if (operatorTag.equals("boss"))
                        EventBus.getDefault().post(new MainToBossFragmentEvent("reLoad", 4));
                    setUserMesg();
                    if (!StringUtils.isLogin(context))
                        return;
                    //获取未读信息总条数
                    userMessageModel.queryUnReadMessageCounts(this, IntegerUtils.API_UNREAD_MESSAGE, responseDataCallback);
                    break;
            }
        }
    }

    private void setUserMesg() {
        if (!StringUtils.isLogin(this)) {
            return;
        }
        Glide.with(this).load(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.FILELOGO)).fitCenter().override(200, 200).error(R.mipmap.ic_launcher)
                .into(ivHead);
        Glide.with(this).load(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.FILELOGO)).fitCenter().override(200, 200).error(R.mipmap.ic_launcher)
                .into(workerorderMine);
        Glide.with(this).load(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.FILELOGO)).fitCenter().override(200, 200).error(R.mipmap.ic_launcher)
                .into(workerIvMine);
        Glide.with(this).load(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.FILELOGO)).fitCenter().override(200, 200).error(R.mipmap.ic_launcher)
                .into(bossorderMine);
        Glide.with(this).load(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.FILELOGO)).fitCenter().override(200, 200).error(R.mipmap.ic_launcher)
                .into(bossIvMine);
        String ststus = SharedPreferencesUtils.getString(context, SharedPreferencesUtils.STATUS);
        switch (ststus) {

            case "0":
                tvStatus.setText("未审核");
                break;

            case "1":
                tvStatus.setText("审核中");
                break;

            case "2":
                tvStatus.setText("审核通过");
                break;

            case "3":
                tvStatus.setText("审核失败");
                break;
        }
    }

    private void resultLogin(String json) {
        JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);

        String data = jsonMap.getString("data");
        LoginUserInfo loginUserInfo = GsonUtils.jsonToBean(data, LoginUserInfo.class);
        if (loginUserInfo != null) {
            String id = String.valueOf(loginUserInfo.userId);
            //设置别名
            SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.UserRole, "1");
            SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.FILELOGO, loginUserInfo.logo);
            SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.STATUS, loginUserInfo.auditStatus);
            SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.EMPLOYERSNAME, loginUserInfo.name);
            SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.COMPANYADDRESS, loginUserInfo.location);
            setUserMesg();
        }
    }


    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 开启雇主地图加载动画
     */
    public void startLoadingBossMapAnimation() {
        boss_loading_bar.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭雇主加载动画
     */
    public void stopLoadingBossMapAnimation() {
        boss_loading_bar.setVisibility(View.GONE);
    }

    /**
     * 开启雇员订单加载
     */
    public void startLoadingWorker() {
        worker_loading.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭雇员订单加载
     */
    public void stopLoadingWorker() {
        worker_loading.setVisibility(View.GONE);
    }
}
