package gongren.com.dlg.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.BaseActivity;
import gongren.com.dlg.activity.PingFenActivity;
import gongren.com.dlg.activity.WorkOrderDetailActivity;
import gongren.com.dlg.adapter.GuYuanOrderListAdapter;
import gongren.com.dlg.javabean.GuYuanOrderItem;
import gongren.com.dlg.javabean.TaskStartWorkTimeModel;
import gongren.com.dlg.javabean.worker.WorkerStatusBean;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.utils.WorkbenchManager;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.RequestCallback;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * Created by Administrator on 2016/12/26.
 */
public class Worker_GuYuanFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener,
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
    private String status = "";
    private int currentpage = 0;

    private final int TAG_OrderList = 1;
    private GuYuanOrderListAdapter adapter;
    //    private List<JsonMap<String, Object>> list = new ArrayList<>();   //数据源
    private List<GuYuanOrderItem> tmplist = new ArrayList<>();   //数据源
    private List<GuYuanOrderItem> tmpSortingList = new ArrayList<>();   //数据源(排序获取最新的mid进行分页)
    private BaseActivity activity = null;

    private final int PING_JIA = 10;//评价
    private final int CANCLE_ORDER = 11;//取消订单
    private int current_select = 0;//当前操作的是哪一项。
    private String minId = "0";//用来分页的。
    private Dialog mLoadingDialog;
    private int pageIndex;//当前显示的是那一页.
    private boolean isStart = true;//判断定时器，是否开启了。
    private List<TaskStartWorkTimeModel> mWorkTimeModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employers_yifabu, container, false);
        ButterKnife.bind(this, view);
        activity = (BaseActivity) getActivity();
        initView();
        return view;
    }

    private void initView() {
        mWorkTimeModels = new ArrayList<>();
        canContentView.setEmptyView(tvBg);
        initSwipeMenuListView();
        initListener();
        initClick();
        refresh.setRefreshEnabled(true);
        refresh.setLoadMoreEnabled(true);
    }

    public void initSwipeMenuListView() {
        adapter = new GuYuanOrderListAdapter(activity, tmplist, status, adpterListener);
        canContentView.setAdapter(adapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                switch (menu.getViewType()) {
                    case 0://有侧滑删除
                        addCHMenu(menu);
                        break;
                    case 1://无侧滑删除
                        break;
                }
            }
        };
        canContentView.setMenuCreator(creator);
        canContentView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                GuYuanOrderItem jsonMap = tmplist.get(position);
                deleteOrder(jsonMap.getBusinessNumber(), jsonMap.getEmployeeId());
                return false;
            }
        });
        canContentView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });
        canContentView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        canContentView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                        startTime(1000);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动状态
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public void addCHMenu(SwipeMenu menu) {
        SwipeMenuItem deleteItem = new SwipeMenuItem(
                activity.getApplicationContext());
        deleteItem.setBackground(R.color.bg_color);
        deleteItem.setWidth((int) activity.getResources().getDimensionPixelSize(R.dimen.delete_width));
        deleteItem.setIcon(R.mipmap.delete_item);
        menu.addMenuItem(deleteItem);

        // 右边多余的部分
        SwipeMenuItem rightItem = new SwipeMenuItem(
                activity.getApplicationContext());
        rightItem.setBackground(R.color.bg_color);
        rightItem.setWidth((int) activity.getResources().getDimensionPixelSize(R.dimen.bold_line_height));
        menu.addMenuItem(rightItem);
    }

    private View.OnClickListener adpterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pingfen_btn://评价
                    current_select = (int) v.getTag();
                    go2PingJia();
                    break;
            }
        }
    };

    public void go2PingJia() {
        Intent intent = new Intent(getActivity().getApplication(), PingFenActivity.class);
        GuYuanOrderItem orderItem = tmplist.get(current_select);
        Bundle bundle = new Bundle();
        //评价需要的参数
        bundle.putSerializable("orderInfo", orderItem);
        //判断是雇员的评价，还是雇主的评价
        bundle.putBoolean("isGuYuan", true);
        intent.putExtras(bundle);
        intent.putExtra("data", bundle);
        //intent.putExtra("isGuYuan", true);
        startActivityForResult(intent, PING_JIA);

    }

    private void initClick() {
        canContentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkOrderDetailActivity.open(activity, tmplist.get(position).getBusinessNumber());
            }
        });
    }

    /**
     * 删除可以删除的订单（取消的订单都可以被删除，其他的不可以被删除 不可以有侧拉按钮）
     *
     * @param businessNumber
     * @param userId
     */
    private void deleteOrder(String businessNumber, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("businessNumber", businessNumber);
        map.put("userVo", userId);
        map.put("format", "json");
        DataUtils.loadData(activity, GetDataConfing.DELETE_ORDER, map, new ResponseDataCallback() {
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

    /**
     * 初始化监听
     */
    private void initListener() {
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
    }

    /**
     * 界面加载
     */
    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.USERID));
        String status = getStatus();
        map.put("status", status);
        map.put("format", "json");
        map.put("pageIndex", currentpage + "");
        map.put("pagesize", 8 + "");
        DataUtils.loadData(activity, GetDataConfing.ordOrderList, map, TAG_OrderList, responseDataCallback);
    }

    public String getStatus() {
        String result = "";
        switch (pageIndex) {
            case 0:
                result = "";
                break;
            case 1:
                result = "20,21";
                break;
            case 2:
                result = "30";
                break;
            case 3:
                result = "40";
                break;
        }
        return result;
    }

    /**
     * 数据请求回调接口（在回调接口中进行操作）
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(activity);
            } else {
                if (canContentView != null) {
                    String json = dataRequest.getResponseData();
                    String dataJson = "";
                    if (!TextUtils.isEmpty(json)) {
                        try {
                            JSONObject object = new JSONObject(json);
                            dataJson = object.getString("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonMap<String, Object> jsonMap = null;
                        List<GuYuanOrderItem> data = null;
                        String error = null;
                        switch (dataRequest.getWhat()) {
                            case TAG_OrderList://获取订单列表
                                data = new Gson().fromJson(dataJson,
                                        new TypeToken<List<GuYuanOrderItem>>() {
                                        }.getType());
                                ;
                                if (data == null || data.size() < 1) {
//                                    ToastUtils.showToastShort(activity, "无更多内容");
                                    if (refresh != null) {
                                        refresh.refreshComplete();
                                        refresh.loadMoreComplete();
                                    }
                                    if (null != tmplist && currentpage == 0) {//是下拉刷新
                                        tmplist.clear();
                                    }
                                    if(null != adapter){
                                        adapter.setDatas(tmplist);
                                    }
                                } else {
                                    getDataButton(data);
                                }
                                break;
                        }
                        if (!DataUtils.isNullStr(error)) {
                            ToastUtils.showToastShort(activity, error);
                        }
                    }
                }
            }
        }
    };
    int num = 0;
    List<GuYuanOrderItem> data;
    final List<GuYuanOrderItem> orderItems = new ArrayList<>();

    /**
     * 添加封装按钮、状态、时间数据
     *
     * @param data
     * @return
     */
    private void getDataButton(List<GuYuanOrderItem> data) {
        this.data = data;
        num = 0;
        orderItems.clear();
        getTaskButton(data.get(0));
    }

    private void getTaskButton(final GuYuanOrderItem orderItem) {

        WorkbenchManager.getTaskButtons(orderItem.getBusinessNumber(), new RequestCallback<WorkerStatusBean>() {
            @Override
            public void onSuccess(final WorkerStatusBean taskStartWorkTimeModel) {
                num++;
                orderItem.setButtonData(taskStartWorkTimeModel.data);
                orderItems.add(orderItem);
                if (data.size() > num) {
                    getTaskButton(data.get(num));
                } else {
                    if (currentpage == 0) {//是下拉刷新
                        tmplist.clear();
                    }
                    tmplist.addAll(orderItems);
                    adapter.setDatas(tmplist);
                    if (refresh != null) {
                        refresh.refreshComplete();
                        refresh.loadMoreComplete();
                    }
                    if (null != tmpSortingList) {
                        tmpSortingList.clear();
                    }
                    tmpSortingList.addAll(tmplist);
                    Collections.sort(tmpSortingList, new Comparator<GuYuanOrderItem>() {
                        @Override
                        public int compare(GuYuanOrderItem lhs, GuYuanOrderItem rhs) {
                            if (!TextUtils.isEmpty(lhs.getJobId()) && !TextUtils.isEmpty(rhs.getJobId())) {
                                double a = Double.parseDouble(lhs.getJobId());
                                double b = Double.parseDouble(rhs.getJobId());
                                if (a < b) {
                                    return -1;
                                } else {
                                    return 1;
                                }
                            }
                            return 0;
                        }
                    });
                }
            }

            @Override
            public void onError(String mag) {
                num++;
                orderItem.setButtonData(null);
                orderItems.add(orderItem);
                if (data.size() > num) {
                    getTaskButton(data.get(num));
                } else {
                    if (currentpage == 0) {//是下拉刷新
                        tmplist.clear();
                    }
                    tmplist.addAll(orderItems);
                    adapter.setDatas(tmplist);
                    minId = tmplist.get(tmplist.size() - 1).getJobId();//获取最后一个的id,用来分页的。
                    if (refresh != null) {
                        refresh.refreshComplete();
                        refresh.loadMoreComplete();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        startTime(1000);
        refresh.autoRefresh();
    }


    /**
     * 加载更多的回调
     */
    @Override
    public void onLoadMore() {
        currentpage++;
        if (tmpSortingList.size() > 0) {
            minId = tmpSortingList.get(0).getJobId();//获取最后一个的id,用来分页的。
        }
        loadData();
    }

    /**
     * 刷新的回调
     */
    @Override
    public void onRefresh() {
        currentpage = 0;
        minId = "0";
        loadData();
    }

    //开启定时器
    public void startTime(long times) {
        if (isStart || pageIndex != 0) {//如果不是全部的页，就不启动定时器
            return;
        }
        isStart = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PING_JIA://评价的回调
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setIndex(int index) {
        this.pageIndex = index;
    }
}
