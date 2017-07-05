package gongren.com.dlg.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.canyinghao.canrefresh.classic.FooterRefreshView;
import com.common.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.BaseActivity;
import gongren.com.dlg.activity.DoingOrderActivity;
import gongren.com.dlg.activity.FinishOrderActivity;
import gongren.com.dlg.activity.PayOrderActivity;
import gongren.com.dlg.activity.PreOrderActivity;
import gongren.com.dlg.activity.ReleaseXQActivity;
import gongren.com.dlg.adapter.FindListAdapter;
import gongren.com.dlg.javabean.ShareBean;
import gongren.com.dlg.javabean.master.masterlist.BossBean;
import gongren.com.dlg.javabean.master.masterlist.DataBean;
import gongren.com.dlg.javabean.master.masterlist.OrderStatusListBean;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.ShareUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.utils.WorkbenchManager;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;

/******
 * Created by Administrator on 2016/12/26.
 */
public class Worker_GuZhuFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener,
        CanRefreshLayout.OnLoadMoreListener {

    @Bind(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @Bind(R.id.can_refresh_footer)
    FooterRefreshView canRefreshFooter;
    @Bind(R.id.can_content_view)
    SwipeMenuListView canContentView;
    @Bind(R.id.refresh)
    CanRefreshLayout refresh;
    @Bind(R.id.cb_all)
    CheckBox cbAll;
    @Bind(R.id.tv_delete)
    Button tvDelete;
    @Bind(R.id.tv_cancle)
    Button tvCancle;
    @Bind(R.id.tv_piliang_delete)
    Button tvPiliangDelete;
    @Bind(R.id.layout)
    LinearLayout layout;
    @Bind(R.id.tv_bg)
    ImageView tvBg;
    private int index = 0;//多个tab，显示哪一个。
    private FindListAdapter adapter;
    private BossBean bossBean;
    private BaseActivity activity = null;

    private int pageIndex = 0;
    private ShareAction mShareAction = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employers_yifabu, container, false);
        ButterKnife.bind(this, view);
        activity = (BaseActivity) getActivity();
//        EventBus.getDefault().register(this);
        initView();
        tvBg.setVisibility(View.GONE);
        return view;
    }

//    //下级Fragment发来的消息
//    @Subscribe
//    public void onMessageEvent(RefreshEvent event) {
//        switch (event.getTag()) {
//            case RefreshEvent.ORDER_LIST_DATA:
//                refresh.smoothScrollTo(0, 0);
//                refresh.autoRefresh();
//                break;
//        }
//    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * 根据上一页选择的tab,判断出应该获取哪个状态的数据。
     *
     * @param index
     * @return
     */
    public String getStatus(int index) {
        String result = "";
        switch (index) {
            case 0://全部
                result = "";
                break;
            case 1://进行中
                result = "20";
                break;
            case 2://待付款
                result = "30";
                break;
            case 3://已完成
                result = "40";
                break;
        }
        return result;
    }

    private void initView() {
        canContentView.setEmptyView(tvBg);
        initSwipeMenuListView();
        initListener();
        initClick();
    }

    public SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            switch (menu.getViewType()) {
                case 0://有侧滑删除
                    addCHMenu(menu);
                    break;
                case 1://侧滑分享
                    addShareAndBianji(menu);
                    break;
            }
        }
    };

    public void initSwipeMenuListView() {
        adapter = new FindListAdapter(activity);
        canContentView.setAdapter(adapter);
        canContentView.smoothCloseMenu();
        canContentView.setMenuCreator(swipeMenuCreator);
        canContentView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                DataBean dataBean = adapter.getBossBean().get(position);
                SwipeMenuItem menuItem = menu.getMenuItem(index);
                if (menuItem.getId() == 0) {
                    deleteOrder(dataBean.id);
                } else if (menuItem.getId() == 1) {
//                    /**
//                     * 华为 6.0以上手机需要申请该权限 否则 崩溃
//                     */
//                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (!Settings.System.canWrite(getContext())) {
//                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
//                                    Uri.parse("package:" + getContext().getPackageName()));
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivityForResult(intent, 1);
//                            Toast.makeText(getContext(), "未申请该权限", Toast.LENGTH_SHORT).show();
//                        } else {
                            Intent intent = new Intent(activity, ReleaseXQActivity.class);
                            intent.putExtra(ReleaseXQActivity.EDIT_DATA, dataBean);
                            startActivity(intent);
//                        }
//                    }else{
//                        Intent intent = new Intent(activity, ReleaseXQActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable(ReleaseXQActivity.EDIT_DATA, dataBean);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    }

                } else if (menuItem.getId() == 2) {
                    getShareData(activity, dataBean.id);
                }

                return false;
            }
        });
    }


    private void getShareData(final Activity activity, String jobId) {
        final Dialog dialog = DialogUtils.loadingProgressDialog(activity);
        //获取分享的内容
        WorkbenchManager.getShareData(activity, GetDataConfing.SHARE_DATA,
                jobId, new WorkbenchManager.StringCallBack() {
                    @Override
                    public void onFinish(String json) {
                        dialog.dismiss();
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
                                    mShareAction.open();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 删除可以删除的订单 没有人接单的订单
     *
     * @param taskId
     * @param
     */
    private void deleteOrder(String taskId) {
        Map<String, Object> map = new HashMap<>();
        map.put("taskId", taskId);
        map.put("userId", SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.USERID));
        map.put("format", "json");
        DataUtils.loadData(activity, GetDataConfing.DELETE_BOSS_ORDER, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                String json = dataRequest.getResponseData();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    ToastUtils.showToastShort(activity, jsonObject.optString("msg"));
                    if ("0".equals(jsonObject.optString("code"))) {
                        refresh.autoRefresh();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //侧拉
    public void addCHMenu(SwipeMenu menu) {
        addShareAndBianji(menu);
        SwipeMenuItem editItem = new SwipeMenuItem(
                getActivity().getApplicationContext());
        editItem.setIcon(R.mipmap.edit);
        editItem.setWidth(getResources().getDimensionPixelSize(R.dimen.delete_width));
        editItem.setId(1);
        editItem.setTitleColor(Color.WHITE);
        // 将创建的菜单项添加进菜单中
        menu.addMenuItem(editItem);

        SwipeMenuItem deleteItem = new SwipeMenuItem(
                getActivity().getApplicationContext());
        deleteItem.setIcon(R.mipmap.delete);
        deleteItem.setTitleColor(Color.WHITE);
        deleteItem.setWidth(getResources().getDimensionPixelSize(R.dimen.delete_width));
        deleteItem.setId(0);
        // 将创建的菜单项添加进菜单中
        menu.addMenuItem(deleteItem);


    }

    //侧拉
    public void addShareAndBianji(SwipeMenu menu) {
        SwipeMenuItem shareItem = new SwipeMenuItem(
                getActivity().getApplicationContext());
        shareItem.setIcon(R.mipmap.share);
        shareItem.setWidth(getResources().getDimensionPixelSize(R.dimen.delete_width));
        shareItem.setId(2);
        // 将创建的菜单项添加进菜单中
        menu.addMenuItem(shareItem);
    }

    private void initClick() {
        canContentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != adapter.getBossBean()
                        && adapter.getBossBean().size() > 0
                        && adapter.getBossBean().get(position).orderStatusList.size() > 0) {
                    int status = adapter.getBossBean().get(position).orderStatusList.get(0).status;//后台已经将顺序排好
                    Log.e("worker", status + "=status");
                    Class<?> cls = null;
                    switch (status) {
                        case 10://有人接单
                        case 6://有人接单
                        case 7://有人接单
                            cls = PreOrderActivity.class;
                            break;
                        case 20://进行中
                        case 22:
                        case 21://进行中
                            cls = DoingOrderActivity.class;
                            break;
                        case 30://待付款
                            cls = PayOrderActivity.class;
                            break;
                        case 40://已完成
                            cls = FinishOrderActivity.class;
                            break;
                        case 50://已取消
                            cls = FinishOrderActivity.class;
                            break;
                    }

                    DataBean dataBean = adapter.getBossBean().get(position);
                    OrderStatusListBean statusListBean = dataBean.orderStatusList.get(0);


                    Intent intent = new Intent(activity, cls);
                    intent.putExtra("bean", statusListBean);
                    intent.putExtra("postName", dataBean.postName);
                    if (!"志愿义工".equals(dataBean.postTypeName)) {
                        intent.putExtra("salary", dataBean.price + "元/" + dataBean.jobMeterUnitName);
                    } else {
                        intent.putExtra("salary", dataBean.postTypeName);
                    }
                    intent.putExtra("pay_everyday", dataBean.price);
                    intent.putExtra("logo", dataBean.userLogo);
                    if (dataBean.isFarmersInsurance==1){
                        intent.putExtra("baoxian_money",2);
                    }else {
                        intent.putExtra("baoxian_money",0);
                    }
                    //intent.putExtra("isFarmersInsurance",statusListBean.list.get(0).isFarmersInsurance );
                    //LogUtils.logE("sfdsfassdf",statusListBean.list.get(0).isFarmersInsurance+"");


                    if (cls != null) {
                        activity.startActivity(intent);
                    }
                }
            }
        });
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        refresh.setRefreshEnabled(true);
        refresh.setLoadMoreEnabled(true);
    }

    /**
     * 界面加载 全部 or 进行中 or 代付款 or 待验收
     */
    private void loadData() {
        Map<String, Object> map = BaseMapUtils.getMap(activity);
        map.put("userId", SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.USERID));
        map.put("status", getStatus(index));//全部
//        map.put("minId", minId);//分页的时候 放的是最后一个查询jobid
        map.put("pageSize", 8 + "");
        map.put("pageIndex", pageIndex + "");

        DataUtils.loadData(activity, GetDataConfing.GUZHU_LINGONG_LIST, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                String json = dataRequest.getResponseData();
                if (refresh != null) {
                    refresh.refreshComplete();
                    refresh.loadMoreComplete();
                }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.optInt("code") == 0) {
                        bossBean = new Gson().fromJson(json, BossBean.class);
                        if (null != adapter && null != adapter.getBossBean()) {
                            if (pageIndex == 0) {
                                adapter.getBossBean().clear();
                            }
                            adapter.getBossBean().addAll(bossBean.data);
                            canContentView.setAdapter(adapter);
                        }
//                        adapter.setDataBean(bossBean.data, pageIndex);
                        if (bossBean != null && bossBean.data != null && bossBean.data.size() > 0) {
                            pageIndex++;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh.smoothScrollTo(0, 0);
        refresh.autoRefresh();
    }

    /**
     * 加载更多的回调
     */
    @Override
    public void onLoadMore() {
        loadData();
    }

    /**
     * 刷新的回调
     */
    @Override
    public void onRefresh() {
        pageIndex = 0;
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static Worker_GuZhuFragment newInstance() {
        Worker_GuZhuFragment fragment = new Worker_GuZhuFragment();
        return fragment;
    }
}
