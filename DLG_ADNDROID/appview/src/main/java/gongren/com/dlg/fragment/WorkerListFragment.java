package gongren.com.dlg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.canyinghao.canrefresh.classic.FooterRefreshView;
import com.common.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.WorkerFindOrderActivity;
import gongren.com.dlg.adapter.WorkerListAdapter;
import gongren.com.dlg.javabean.EmployeeJob;
import gongren.com.dlg.javabean.EmployeeJobJson;
import gongren.com.dlg.javabean.MainEvent;
import gongren.com.dlg.javabean.MainToWorkerFragmentEvent;
import gongren.com.dlg.javabean.MapLoadEvent;
import gongren.com.dlg.utils.DataCacheUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.GsonUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 雇员零工列表
 */
public class WorkerListFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {

    @Bind(R.id.iv_nodata)
    ImageView ivNodata;
    @Bind(R.id.tv_nodata)
    TextView tvNodata;
    @Bind(R.id.layout_nodata)
    LinearLayout layoutNodata;
    @Bind(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @Bind(R.id.can_refresh_footer)
    FooterRefreshView canRefreshFooter;
    @Bind(R.id.can_content_view)
    SwipeMenuListView canContentView;
    @Bind(R.id.refresh)
    CanRefreshLayout refresh;
    @Bind(R.id.workerlist_map)
    ImageView workerlistMap;

    private String postType = "job.type_0";//默认是全部
    private String minId = "0";
    private int currentPage = 0;
    private static final int TAG_REQUEST = 1;
    private WorkerListAdapter workerListAdapter;
    private ArrayList<EmployeeJob> employeeJobList;
    private ArrayList<EmployeeJob> employeeJobListCopy ;



    //上级Activity发来的消息
    @Subscribe
    public void onMessageEvent(MainToWorkerFragmentEvent event) {
        switch (event.getTag()) {
            case 2:
                if (employeeJobList != null){
                    employeeJobListCopy.clear();
                    employeeJobList.clear();
                }
                String string = event.getMsg();
                String[] split = string.split(",");
                postType = split[0];
                refresh.setLoadMoreEnabled(true);
                refresh.autoRefresh();//自动刷新
                break;
            case 100:
                if (employeeJobList != null){
                    employeeJobListCopy.clear();
                    employeeJobList.clear();
                }
                refresh.autoRefresh();//自动刷新
                break;
        }
    }

    //地图加载成功，刷新数据
    @Subscribe
    public void onMessageEvent(MapLoadEvent event) {
        switch (event.getLoadStatus()) {
            case MapLoadEvent.LOAD_SUCCESS:
                initData();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_workerlist, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        employeeJobList = new ArrayList<>();
        employeeJobListCopy = new ArrayList<>();
        initView();
        return view;
    }

    /**
     * 初始化界面
     */
    private void initView() {
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        workerListAdapter = new WorkerListAdapter(activity, employeeJobListCopy);
        canContentView.setAdapter(workerListAdapter);
        layoutNodata.setVisibility(View.GONE);
        canContentView.setEmptyView(layoutNodata);
//        refresh.autoRefresh();
        canContentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmployeeJob employeeJob = employeeJobListCopy.get(position);
                Intent intent = new Intent(activity, WorkerFindOrderActivity.class);
                intent.putExtra("id",employeeJob.id);
                intent.putExtra("yCoordinate",employeeJob.yCoordinate);
                intent.putExtra("xCoordinate",employeeJob.xCoordinate);
                intent.putExtra("postTypeName",employeeJob.postTypeName);
                intent.putExtra("postName",employeeJob.postName);
                intent.putExtra("price",employeeJob.price);
                intent.putExtra("jobMeterUnitName",employeeJob.jobMeterUnitName);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        refresh.autoRefresh();
        super.onResume();
    }

    /**
     * 加载网络数据
     */
    private void initData() {
        Map<String, Object> map = new HashMap<>();
        if (DataCacheUtils.getIstance().getCurrentLongitudeX() == null) {
            return;
        }
        map.put("xCoordinate", DataCacheUtils.getIstance().getCurrentLongitudeX());
        map.put("yCoordinate", DataCacheUtils.getIstance().getCurrentLatitudeY());
        map.put("postType", postType);
        map.put("demandType", "");
        map.put("postName", "");
        map.put("pageSize", "10");
        map.put("userId", SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.USERID));
        map.put("pageIndex", currentPage + "");
        map.put("format","json");
        map.put("minId",minId);
        DataUtils.loadData(activity, GetDataConfing.jobTaskRestJobTask, map, TAG_REQUEST, responseDataCallback);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            refresh.loadMoreComplete();
            //刷新完自动收起
            refresh.refreshComplete();
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(activity);
            } else {
                String json = dataRequest.getResponseData();
                Log.i("====s==",json);
                if (!TextUtils.isEmpty(json)) {
                    if (dataRequest.getWhat() == TAG_REQUEST) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if ("0".equals(jsonObject.optString("code"))) {
                                EmployeeJobJson employeeJobJson = GsonUtils.jsonToBean(json, EmployeeJobJson.class);
                                if (employeeJobJson != null && employeeJobJson.data != null && employeeJobJson.data.size() > 0) {
//                                    if(employeeJobJson.data.size() < 10){
//                                        refresh.setLoadMoreEnabled(false);
//                                        ToastUtils.showToastShort(getActivity(),"已加载全部");
//                                    }
                                    employeeJobList.addAll(employeeJobJson.data);
                                    employeeJobListCopy.addAll(employeeJobJson.data);
                                    Collections.sort(employeeJobList, new Comparator<EmployeeJob>() {
                                        @Override
                                        public int compare(EmployeeJob lhs, EmployeeJob rhs) {
                                            if(!TextUtils.isEmpty(lhs.id) && !TextUtils.isEmpty(rhs.id)){
                                                double a = Double.parseDouble(lhs.id);
                                                double b = Double.parseDouble(rhs.id);
                                                if(a<b){
                                                    return -1;
                                                }else {
                                                    return 1;
                                                }
                                            }
                                            return 0;
                                        }
                                    });
                                    if(employeeJobListCopy.size() == 0)
                                        layoutNodata.setVisibility(View.VISIBLE);
                                }
                                workerListAdapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(activity, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.workerlist_map)
    public void onClick() {
        EventBus.getDefault().post(new MainEvent("", 0));
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        minId = employeeJobList.get(0).id;
        initData();
    }

    @Override
    public void onRefresh() {
        if (employeeJobList != null){
            employeeJobListCopy.clear();
            employeeJobList.clear();
        }
        currentPage = 0;
        minId = "0" ;
        refresh.setLoadMoreEnabled(true);
        initData();
    }

    public void setPostType(String postType){
        this.postType = postType;
    }
}
