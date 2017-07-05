package gongren.com.dlg.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.common.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.ReceiptInfoAdapter;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

import static gongren.com.dlg.R.id.tv_sendoff_time;

/**
 * 发票详情
 */
public class ReceiptInfoActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.tv_apply_time)
    TextView tvApplyTime;
    @Bind(tv_sendoff_time)
    TextView tvSendoffTime;
    @Bind(R.id.tv_receipt_type)
    TextView tvReceiptType;
    @Bind(R.id.tv_receipt_receive_name)
    TextView tvReceiptReceiveName;
    @Bind(R.id.tv_receipt_receive_phone)
    TextView tvReceiptReceivePhone;
    @Bind(R.id.tv_receipt_receive_address)
    TextView tvReceiptReceiveAddress;
    @Bind(R.id.tv_receipt_company_name)
    TextView tvReceiptCompanyName;
    @Bind(R.id.tv_receipt_service_charge)
    TextView tvReceiptServiceCharge;
    @Bind(R.id.tv_receipt_number)
    TextView tvReceiptNumber;
    @Bind(R.id.tv_receipt_company_address)
    TextView tvReceiptCompanyAddress;
    @Bind(R.id.tv_receipt_company_phone)
    TextView tvReceiptCompanyPhone;
    @Bind(R.id.tv_receipt_bank)
    TextView tvReceiptBank;
    @Bind(R.id.tv_receipt_bank_number)
    TextView tvReceiptBankNumber;

    @Bind(R.id.can_content_view)
    SwipeMenuListView canontentView;

    private List<JsonMap<String, Object>> dataList = new ArrayList<>();   //数据源
    private List<JsonMap<String, Object>> billDataList = new ArrayList<>();   //数据源
    private ReceiptInfoAdapter receiptInfoAdapter;
    private static final int TAG_REQUEST = 1;
    private static final int TAG_REQUEST_List = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("开票历史");
        tvSave.setVisibility(View.GONE);
        String receiptId = getIntent().getStringExtra("receiptId");

        receiptInfoAdapter = new ReceiptInfoAdapter(context, billDataList);
        canontentView.setAdapter(receiptInfoAdapter);
        setListViewHeightBasedOnChildren(canontentView);

        addData(receiptId);
    }


    private void addData(String receiptId) {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("invoiceId", receiptId);
        DataUtils.loadData(context, GetDataConfing.getInvoiceRest, map, TAG_REQUEST, responseDataCallback);
        map.put("size", "30");
        map.put("minId", "0");
        DataUtils.loadData2(context, GetDataConfing.queryByInvociceId, map, TAG_REQUEST_List, responseDataCallback);

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

                        dataList = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                        fillData();
                    }else if (dataRequest.getWhat() == TAG_REQUEST_List) {
                        LogUtils.logD("zq", json);

                        JSONObject root = null;
                        try {
                            root = new JSONObject(json.toString());
                            JSONArray array = root.getJSONArray("data");
                            if (null != array) {
                                //                                JSONObject stud = array.getJSONObject(i);
//                                String dataJson = array.getString(i);
                                billDataList = JsonParseHelper.getList_JsonMap(array.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        receiptInfoAdapter.refreshDatas(billDataList, 1);

                    }
                }
            }
        }
    };

    /**
     * 填充数据
     */
    private void fillData(){
        JsonMap<String, Object> jsonMap = dataList.get(0);
        tvApplyTime.setText(DateUtils.dateTimeFormat("yyyy.MM.dd HH:mm", jsonMap.getString("createTime")));
        tvSendoffTime.setText(DateUtils.dateTimeFormat("yyyy.MM.dd HH:mm", jsonMap.getString("modifyTime")));
        if("1".equals(jsonMap.getString("status"))){
            tvReceiptType.setText("申请中");
        }else if("3".equals(jsonMap.getString("status"))){
            tvReceiptType.setText("已寄出");
        }
        JsonMap<String, Object> invoiceInformationRestVo = jsonMap.getJsonMap("invoiceInformationRestVo");
        tvReceiptReceiveName.setText(invoiceInformationRestVo.getString("checkTakerName"));
        tvReceiptReceivePhone.setText(invoiceInformationRestVo.getString("checkTakerPhone"));
        tvReceiptReceiveAddress.setText(invoiceInformationRestVo.getString("checkTakerAddress"));

        tvReceiptCompanyName.setText(invoiceInformationRestVo.getString("companyName"));
        tvReceiptServiceCharge.setText(jsonMap.getString("invoiceAmount")+"元");
        tvReceiptNumber.setText(invoiceInformationRestVo.getString("taxpayerIdentificationCode"));
        tvReceiptCompanyAddress.setText(invoiceInformationRestVo.getString("registerAddress"));
        tvReceiptCompanyPhone.setText(invoiceInformationRestVo.getString("registerPhone"));
        tvReceiptBank.setText(invoiceInformationRestVo.getString("bankName"));
        tvReceiptBankNumber.setText(invoiceInformationRestVo.getString("bankAccount"));

    }


    @OnClick(R.id.iv_back)
    public void onClick(View v) {
        finish();

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
