package gongren.com.dlg.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.BillingHistoryAdapter;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

public class BillingHistoryActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;

    @Bind(R.id.refresh)
    CanRefreshLayout refresh;
    @Bind(R.id.can_content_view)
    ListView canContentView;

    private int currentPage = 0;
    private List<JsonMap<String, Object>> dataList = new ArrayList<>();   //数据源
    private static final int TAG_REQUEST = 1;
    private BillingHistoryAdapter billingHistoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_history);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("开票历史");
        tvSave.setVisibility(View.GONE);
        billingHistoryAdapter = new BillingHistoryAdapter(context, dataList);
        canContentView.setAdapter(billingHistoryAdapter);

        //下拉刷新监听
        refresh.setOnRefreshListener(new CanRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                refresh.setLoadMoreEnabled(true);
                //                addData();
                refresh.refreshComplete();   //刷新完成
            }
        });

        //上拉加载监听
        refresh.setOnLoadMoreListener(new CanRefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                //                addData();
                refresh.loadMoreComplete();   //加载完成
            }
        });

        refresh.autoRefresh();
    }

    private void addData() {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("pagesize", "10");
        map.put("currentPage", currentPage + "");
        //TODO 更换成查询系统消息接口
        DataUtils.loadData(context, GetDataConfing.queryBalanceRecode, map, TAG_REQUEST, responseDataCallback);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (canContentView != null) {
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
                            billingHistoryAdapter.refreshDatas(dataList, currentPage);
                        }
                    }
                }
            }
        }
    };
    @OnClick(R.id.iv_back)
    public void onClick(View v) {
        finish();
    }

}
