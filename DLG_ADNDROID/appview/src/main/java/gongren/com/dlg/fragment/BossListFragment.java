package gongren.com.dlg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.EmployeeDetailsActivity;
import gongren.com.dlg.adapter.BossListAdapter;
import gongren.com.dlg.javabean.EmployerEmployeeListJson;
import gongren.com.dlg.javabean.MainEvent;
import gongren.com.dlg.javabean.MainToBossFragmentEvent;
import gongren.com.dlg.utils.DataCacheUtils;
import gongren.com.dlg.utils.DataUtils;
import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 雇主列表页
 * Created by Administrator on 2017/3/22.
 */
public class BossListFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {

    @Bind(R.id.iv_nodata)
    ImageView ivNodata;
    @Bind(R.id.tv_nodata)
    TextView tvNodata;
    @Bind(R.id.layout_nodata)
    LinearLayout layoutNodata;
    @Bind(R.id.can_content_view)
    SwipeMenuListView canContentView;
    @Bind(R.id.refresh)
    CanRefreshLayout refresh;
    @Bind(R.id.bosslist_map)
    ImageView bosslistMap;
    private List<EmployerEmployeeListJson.DataBean> datas;
    private boolean isRefreshing;

    private static final int TAG_REQUEST = 1;
    private int currentPage = 0;
    private BossListAdapter bossListAdapter;
    private String postType = "";

    //上级Activity发来的消息
    @Subscribe
    public void onMessageEvent(MainToBossFragmentEvent event) {
        switch (event.getTag()) {
            case 2:
                if(datas!=null){
                    datas.clear();
                    bossListAdapter.notifyDataSetChanged();
                }
                postType = event.getMsg();
                initData();
                break;
        }
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_bosslist, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        return view;
    }

    /**
     * 初始化界面
     */
    private void initView() {
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        datas = new ArrayList<>();
        bossListAdapter = new BossListAdapter(activity, datas);
        canContentView.setAdapter(bossListAdapter);
        canContentView.setEmptyView(layoutNodata);

        canContentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent employeeDetailsActivityIntent = new Intent(activity, EmployeeDetailsActivity.class);
                employeeDetailsActivityIntent.putExtra("userId", datas.get(position).userId + "");
                employeeDetailsActivityIntent.putExtra("employeeParamId",datas.get(position).userId+"");
                startActivity(employeeDetailsActivityIntent);

            }
        });
        refresh.autoRefresh();
    }

    private void initData() {
        Map<String, Object> map = new HashMap<>();
        map.put("xCoordinate", DataCacheUtils.getIstance().getCurrentLongitudeX());
        map.put("yCoordinate", DataCacheUtils.getIstance().getCurrentLatitudeY());
        map.put("postType", postType);
        map.put("demandType", "");
        map.put("pageSize", "10");
        map.put("userId", SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.USERID));
        map.put("pageIndex", currentPage + "");
        map.put("format", "json");
        DataUtils.loadData(activity, GetDataConfing.jobTaskRestEmployee, map, TAG_REQUEST, responseDataCallback);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            refresh.loadMoreComplete();
            refresh.refreshComplete();
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(activity);
            } else {
                String json = dataRequest.getResponseData();
                Log.i("====s",json);
                if (!TextUtils.isEmpty(json)) {
                    if (dataRequest.getWhat() == TAG_REQUEST) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if ("0".equals(jsonObject.optString("code"))) {
                                EmployerEmployeeListJson employeeListJson = new Gson().fromJson(json, EmployerEmployeeListJson.class);
                                if(employeeListJson.data!=null&&employeeListJson.data.size()>0){
                                    if(isRefreshing){
                                        datas.clear();
                                        isRefreshing = false;
                                    }
                                    datas.addAll(employeeListJson.data);
                                    bossListAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        //        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.bosslist_map)
    public void onClick() {
        EventBus.getDefault().post(new MainEvent("isSwitchJoe", 1));
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        initData();
    }

    @Override
    public void onRefresh() {
        currentPage = 0;
        isRefreshing = true;
        initData();
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
