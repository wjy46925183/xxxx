package gongren.com.dlg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.ReceiptHistoryAdapter;
import gongren.com.dlg.javabean.ReceiptHistoryDataJson;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.LogUtils;
import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 开票历史
 */
public class ReceiptHistoryActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;

    @Bind(R.id.refresh)
    CanRefreshLayout refresh;
    @Bind(R.id.can_content_view)
    ListView canContentView;

    private int currentPage = 0;
    private List<ReceiptHistoryDataJson.DataBean> dataList = new ArrayList<>();   //数据源
    private List<ReceiptHistoryDataJson> dataJsonList;
    private static final int TAG_REQUEST = 1;
    private ReceiptHistoryAdapter billingHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_history);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addData();
    }

    private void initView() {
        tvTitle.setText("开票历史");
        tvSave.setVisibility(View.GONE);
        billingHistoryAdapter = new ReceiptHistoryAdapter(context, dataList);
        canContentView.setAdapter(billingHistoryAdapter);

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



        canContentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent receiptInfoActivityIntent = new Intent(context, ReceiptInfoActivity.class);
                String receiptId =  String.valueOf(dataList.get(position).getId());
//                BigDecimal bd = new BigDecimal(iddi);
//                String str = bd.toPlainString();
//                Long ss = Long.parseLong(str);
                receiptInfoActivityIntent.putExtra("receiptId",receiptId);
//                receiptInfoActivityIntent.putExtra("receiptId","27515846659451444359074745900");
                startActivity(receiptInfoActivityIntent);
            }
        });
    }

    private void addData() {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("size", "10");
        map.put("minId", "0");
        map.put("pageNumber", currentPage + "");
        DataUtils.loadData(context, GetDataConfing.queryInvoiceList, map, TAG_REQUEST, responseDataCallback);

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
                        LogUtils.logD("zq", json);
                        try {
                            Gson gson = new Gson();
                            ReceiptHistoryDataJson receiptHistoryDataJson = gson.fromJson(json, ReceiptHistoryDataJson.class);
                            if(receiptHistoryDataJson!=null){
                                dataList.clear();
                                List<ReceiptHistoryDataJson.DataBean> data = receiptHistoryDataJson.getData();
                                dataList.addAll(data);
                                billingHistoryAdapter.notifyDataSetChanged();
                            }
                        }catch (Exception e){
                            com.common.string.LogUtils.e("====="+e.getMessage());
                        }

                           /* JSONObject root = new JSONObject(json.toString());
                            JSONArray array = root.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                //                                JSONObject stud = array.getJSONObject(i);
                                String dataJson = array.getString(i);
                                dataList = JsonParseHelper.getList_JsonMap(dataJson);
                               *//* dataJsonList = GsonUtils.parseJsonArrayWithGson(dataJson,
                                        ReceiptHistoryDataJson.class);*//*
                            }
                            if (dataList == null || dataList.size() < 1) {
                                dataList = new ArrayList<>();
                                refresh.setLoadMoreEnabled(false);
                            }
                            //                        dataList = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                            //                        if (dataList == null || dataList.size() < 1) {
                            //                            dataList = new ArrayList<>();
                            //                            refresh.setLoadMoreEnabled(false);
                            //                        }
                            //刷新数据
                            billingHistoryAdapter.refreshDatas(dataList, currentPage);*/

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
