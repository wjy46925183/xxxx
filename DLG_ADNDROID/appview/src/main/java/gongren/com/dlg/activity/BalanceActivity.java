package gongren.com.dlg.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.canyinghao.canrefresh.CanRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.BalanceAdapter;
import gongren.com.dlg.utils.DataUtils;

import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 收支明细界面
 */
public class BalanceActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.can_content_view)
    SwipeMenuListView canContentView;
    @Bind(R.id.refresh)
    CanRefreshLayout refresh;

    @Bind(R.id.rb_01)
    RadioButton rb01;
    @Bind(R.id.rb_02)
    RadioButton rb02;
    @Bind(R.id.rb_03)
    RadioButton rb03;

    private BalanceAdapter balanceAdapter;
    private int currentPage = 0;
    private List<JsonMap<String, Object>> dataList = new ArrayList<>();   //数据源
    private static final int TAG_REQUEST = 1;
    private String billStatus = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("收支明细");
        balanceAdapter = new BalanceAdapter(context, dataList);
        canContentView.setAdapter(balanceAdapter);

        //下拉刷新监听
        refresh.setOnRefreshListener(new CanRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                refresh.setLoadMoreEnabled(true);
                addData();
                refresh.refreshComplete();   //刷新完成
            }
        });

        //上拉加载监听
        refresh.setOnLoadMoreListener(new CanRefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                addData();
                refresh.loadMoreComplete();   //加载完成
            }
        });

        refresh.autoRefresh();
    }

    /**
     * 网络请求数据
     */
    private void addData() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("pageSize", "10");
        map.put("minId", "0");
        map.put("status", billStatus);
        map.put("isPossessInvoice", "");
        map.put("format","format");
        map.put("pageNumber", currentPage + "");

        DataUtils.loadData(context, GetDataConfing.findBillDetailList, map, TAG_REQUEST, responseDataCallback);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
//            if (canContentView != null) {
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    if (!TextUtils.isEmpty(json)) {
                        if (dataRequest.getWhat() == TAG_REQUEST) {
                            dataList = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                            if (dataList == null || dataList.size() < 1) {
                                dataList = new ArrayList<>();
                                refresh.setLoadMoreEnabled(false);
                            }
                            //刷新数据
                            balanceAdapter.refreshDatas(dataList, currentPage);
                        }
                    }
                }
            }
//        }
    };

    @OnClick({R.id.iv_back, R.id.rb_01, R.id.rb_02, R.id.rb_03})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rb_01:
                billStatus = "0";
                dataList.clear();
                refresh.autoRefresh();
//                addData();
                break;
            case R.id.rb_02:
                billStatus = "2";
                dataList.clear();
                refresh.autoRefresh();
//                addData();
                break;
            case R.id.rb_03:
                billStatus = "1";
                dataList.clear();
                refresh.autoRefresh();
//                addData();
                break;


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
