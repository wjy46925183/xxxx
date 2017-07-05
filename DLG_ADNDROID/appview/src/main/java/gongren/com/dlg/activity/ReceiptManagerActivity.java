package gongren.com.dlg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.common.utils.SharedPreferencesUtils;

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
import gongren.com.dlg.adapter.ReceiptManagerAdapter;
import gongren.com.dlg.javabean.ReceiptBillData;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

public class ReceiptManagerActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;

    @Bind(R.id.can_content_view)
    SwipeMenuListView canContentView;
    @Bind(R.id.refresh)
    CanRefreshLayout refresh;

    @Bind(R.id.tv_select_receipt_tote)
    TextView tvSelectReceiptTote;
    @Bind(R.id.tv_select_receipt_price)
    TextView tvSelectReceiptPrice;
    @Bind(R.id.cb_receipt_all)
    CheckBox cbReceiptAll;

    private int currentPage = 0;
    private List<JsonMap<String, Object>> dataList = new ArrayList<>();   //数据源
    private Map<String, JsonMap<String, Object>> tempDataMap = new HashMap();
    private ReceiptManagerAdapter receiptManagerAdapter;
    private static final int TAG_REQUEST = 1;
    private ArrayList<ReceiptBillData> selectBillList = new ArrayList<>();
    private int selectCount = 0;
    private Double selectAllPrice = 0.0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvSelectReceiptTote.setText(selectCount + "");
            tvSelectReceiptPrice.setText(String.format("%.2f", selectAllPrice)  + "");
            if (dataList.size() == tempDataMap.size()) {
                cbReceiptAll.setChecked(true);
            }else {
                cbReceiptAll.setChecked(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_manager);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("发票");
        tvSave.setText("开票历史");
        receiptManagerAdapter = new ReceiptManagerAdapter(context, dataList);
        canContentView.setAdapter(receiptManagerAdapter);

        refresh.setOnRefreshListener(new CanRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                refresh.setLoadMoreEnabled(true);
                initDatas();
                refresh.refreshComplete();
            }
        });
        refresh.setOnLoadMoreListener(new CanRefreshLayout.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                currentPage++;
                initDatas();
                refresh.loadMoreComplete();
                //刷新完自动收起
                refresh.refreshComplete();
            }
        });
        refresh.autoRefresh();

        canContentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cbBillCheck = (CheckBox) view.findViewById(R.id.cb_bill_check);
                cbBillCheck.setChecked(!cbBillCheck.isChecked());
                JsonMap<String, Object> jsonMap = dataList.get(position);
                Double price = Math.abs(jsonMap.getDouble("amount"));
                if (cbBillCheck.isChecked()) {
                    tempDataMap.put(jsonMap.getString("billId"), jsonMap);
                    selectAllPrice += price;
                    selectCount++;
                } else {
                    tempDataMap.remove(jsonMap.getString("billId"));
                    selectAllPrice = selectAllPrice - price;
                    selectCount--;
                }

                mHandler.sendEmptyMessage(0);
            }
        });

    }

    private void initDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("status", "0");
        map.put("minId", "0");
        map.put("pageSize", "10");
        map.put("isPossessInvoice", "0");
        map.put("pageNumber", currentPage + "");
        map.put("format", "json");
        DataUtils.loadData(context, GetDataConfing.billDetailRestList, map, TAG_REQUEST, responseDataCallback);
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
                        List<JsonMap<String, Object>> _dataList = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                        if (dataList == null || dataList.size() < 1) {
                            dataList = new ArrayList<>();
                            refresh.setLoadMoreEnabled(false);
                        }
                        if(currentPage==0){
                            dataList.clear();
                        }
                        if(null != _dataList && _dataList.size()>0){
                            dataList.addAll(_dataList);
                        }
                        if (dataList.size()  ==  tempDataMap.size()) {
                            cbReceiptAll.setChecked(true);
                        }else {
                            cbReceiptAll.setChecked(false);
                        }
                        //刷新数据
                        receiptManagerAdapter.refreshDatas(_dataList, currentPage);
                    }
                }
            }
        }
        //        }
    };

    @OnClick({R.id.iv_back, R.id.tv_save, R.id.cb_receipt_all, R.id.tv_btn_next_step})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save://开票历史记录
                startActivity(new Intent(context, ReceiptHistoryActivity.class));
                break;
            case R.id.cb_receipt_all:
                receiptManagerAdapter.setAllSelect(cbReceiptAll.isChecked());
                if (cbReceiptAll.isChecked()) {
                    selectAllPrice = 0.0;
                    if (dataList.size() > 0) {
                        for (int i = 0; i < dataList.size(); i++) {
                            JsonMap<String, Object> jsonMap = dataList.get(i);
                            tempDataMap.put(jsonMap.getString("billId"), jsonMap);
                            Double price = Math.abs(jsonMap.getDouble("amount"));
                            selectAllPrice += price;
                        }
                        selectCount = dataList.size();
                    }

                } else {
                    if (dataList.size() > 0) {
                        for (int i = 0; i < dataList.size(); i++) {
                            JsonMap<String, Object> jsonMap = dataList.get(i);
                            tempDataMap.remove(jsonMap.getStr("billId"));
                        }
                    }
                    selectAllPrice = 0.0;
                    selectCount = 0;
                }
                mHandler.sendEmptyMessage(0);
                break;
            case R.id.tv_btn_next_step:
                Intent orderBillingActivityIntent = new Intent(context, OrderBillingActivity.class);
                Double countMoney = 0.0;
                ArrayList<String> billIdList = new ArrayList<>();
                ArrayList<String> subBillBusinessList = new ArrayList<>();
                for (String key : tempDataMap.keySet()) {
                    countMoney += Math.abs(tempDataMap.get(key).getDouble("amount", 0));
                    billIdList.add(key);
                    JsonMap<String, Object> jsonMap = tempDataMap.get(key);
                    subBillBusinessList.add(jsonMap.getString("subBillBusinessNumber"));
                }
                orderBillingActivityIntent.putExtra("countMoney", countMoney);
                orderBillingActivityIntent.putStringArrayListExtra("billIdList", billIdList);
                orderBillingActivityIntent.putStringArrayListExtra("subBillBusinessList", subBillBusinessList);
                if (subBillBusinessList != null && subBillBusinessList.size() > 0) {
//                    startActivity(orderBillingActivityIntent);
                    startActivityForResult(orderBillingActivityIntent, 200);
                } else {
                    ToastUtils.showToastShort(context, "请选择开发票账单");
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200) {
            tempDataMap.clear();
            tvSelectReceiptTote.setText(0 + "");
            tvSelectReceiptPrice.setText(0 + "");
            currentPage = 0;
            refresh.setLoadMoreEnabled(true);
            if (null != receiptManagerAdapter) {
                receiptManagerAdapter.setAllSelect(false);
            }
            initDatas();
            refresh.refreshComplete();
        }
    }
}
