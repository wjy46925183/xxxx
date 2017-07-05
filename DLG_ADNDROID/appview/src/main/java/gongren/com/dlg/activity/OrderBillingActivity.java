package gongren.com.dlg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.common.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.InvoiceMoreInfoEvent;
import gongren.com.dlg.javabean.InvoiceTemplate;
import gongren.com.dlg.javabean.InvoiceTemplateJson;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.GsonUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 订单开票
 */
public class OrderBillingActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;

    @Bind(R.id.et_company_rise)
    EditText etCompanyRise;
    @Bind(R.id.tv_invoice_sum)
    TextView tvInvoiceSum;
    @Bind(R.id.et_addressee)
    EditText etAddressee;
    @Bind(R.id.et_relation_telephone)
    EditText etRelationTelephone;
    @Bind(R.id.et_location_area)
    EditText etLocationArea;
    @Bind(R.id.et_bankaccount_address)
    EditText etBankaccountAddress;
    @Bind(R.id.cb_alipay_check)
    CheckBox cbAlipayCheck;
    @Bind(R.id.cb_wechat_check)
    CheckBox cbWechatCheck;
    @Bind(R.id.cb_bankcard_check)
    CheckBox cbBankcardCheck;
    @Bind(R.id.tv_select_receipt_price)
    TextView tvSelectReceiptPrice;

    private double countMoney;
    private static final int TAG_REQUEST = 1;
    private static final int TAG_REQUEST_QUERY = 2;
    private InvoiceMoreInfoEvent invoiceMoreInfoEvent;
    private ArrayList<String> billIdList;
    private ArrayList<String> subBillBusinessList;
    private ArrayList<InvoiceTemplate> dataList;
    private StringBuilder arrStr;
    private StringBuilder subBillId;
    private Map<String, Object> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_billing);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("按订单开票");
        tvSave.setVisibility(View.GONE);
        Intent intent = getIntent();
        countMoney = intent.getDoubleExtra("countMoney", 0);
        billIdList = intent.getStringArrayListExtra("billIdList");
        subBillBusinessList = intent.getStringArrayListExtra("subBillBusinessList");
        arrStr = new StringBuilder("{");
        subBillId = new StringBuilder("");
        for (int i = 0; i<billIdList.size(); i++){
            arrStr.append(billIdList.get(i)+",");
            subBillId.append(subBillBusinessList.get(i)+",");
        }
        arrStr.replace(arrStr.length()-1, arrStr.length(), "}");
        subBillId.replace(subBillId.length()-1, subBillId.length(), " ");
        tvInvoiceSum.setText(countMoney+"元");
        double v = countMoney * 0.06;
        String result = String .format("%.2f",v);
        tvSelectReceiptPrice.setText(result);
        
        initData();
    }

    private void initData() {

        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        DataUtils.loadData(context, GetDataConfing.GETINVOICETEMPLATE, map, TAG_REQUEST_QUERY, responseDataCallback);
    }
    @Subscribe
    public void onEvent(InvoiceMoreInfoEvent event) {
        invoiceMoreInfoEvent = event;
    }

    @OnClick({R.id.iv_back, R.id.rl_more_info, R.id.tv_btn_payment, R.id.cb_alipay_check, R.id.cb_wechat_check, R.id.cb_bankcard_check})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_more_info:
                Intent moreInfoActivityIntent = new Intent(context, OrderBillMoreInfoActivity.class);
                if(dataList!=null && dataList.size()>0){
                    moreInfoActivityIntent.putExtra("InvoiceTemplate",dataList.get(0));
                }else if(invoiceMoreInfoEvent != null){
                    InvoiceTemplate invoiceTemplate = new InvoiceTemplate() ;
                    invoiceTemplate.taxpayerIdentificationCode = invoiceMoreInfoEvent.getTaxpayerDistinguishNumber();
                    invoiceTemplate.registerAddress = invoiceMoreInfoEvent.getCompanyAddress() ;
                    invoiceTemplate.checkTakerPhone = invoiceMoreInfoEvent.getCompanyPhone();
                    invoiceTemplate.bankName = invoiceMoreInfoEvent.getBankAccount();
                    invoiceTemplate.bankAccount = invoiceMoreInfoEvent.getBankAccountNumber();
                    moreInfoActivityIntent.putExtra("InvoiceTemplate",invoiceTemplate);
                }
                startActivity(moreInfoActivityIntent);
                break;
            case R.id.tv_btn_payment:

                String companyRise = etCompanyRise.getText().toString().trim();
                String addressee = etAddressee.getText().toString().trim();
                String relationTelephone = this.etRelationTelephone.getText().toString().trim();
                String locationArea = etLocationArea.getText().toString().trim();
                String bankaccountAddress = etBankaccountAddress.getText().toString().trim();

                if(invoiceMoreInfoEvent==null){
                    if(dataList!=null && dataList.size()>0){
                        InvoiceTemplate invoiceTemplate1 = dataList.get(0);
                        invoiceMoreInfoEvent = new InvoiceMoreInfoEvent();
                        invoiceMoreInfoEvent.setTaxpayerDistinguishNumber(invoiceTemplate1.taxpayerIdentificationCode);
                        invoiceMoreInfoEvent.setCompanyAddress(invoiceTemplate1.registerAddress);
                        invoiceMoreInfoEvent.setCompanyPhone(invoiceTemplate1.registerPhone);
                        invoiceMoreInfoEvent.setBankAccount(invoiceTemplate1.bankName);
                        invoiceMoreInfoEvent.setBankAccountNumber(invoiceTemplate1.bankAccount);
                    }
                }
                if(TextUtils.isEmpty(companyRise)){
                    ToastUtils.showToastShort(context, "公司抬头不能为空");
                    return;
                }
                if(countMoney<=0){
                    ToastUtils.showToastShort(context, "发票金额不能少于0");
                    return;
                }
                if(TextUtils.isEmpty(addressee)){
                    ToastUtils.showToastShort(context, "收件人不能为空");
                    return;
                }
                if(TextUtils.isEmpty(locationArea)){
                    ToastUtils.showToastShort(context, "所在地区不能为空");
                    return;
                }
                if(TextUtils.isEmpty(relationTelephone)){
                    ToastUtils.showToastShort(context, "联系电话不能为空");
                    return;
                }
                if(TextUtils.isEmpty(bankaccountAddress)){
                    ToastUtils.showToastShort(context, "开户行地址不能为空");
                    return;
                }
//                int status = -1;
//                if(cbAlipayCheck.isChecked() || cbWechatCheck.isChecked() || cbBankcardCheck.isChecked()){
//                    if(cbAlipayCheck.isChecked()) {
//                        status = 0;
//                    }else if(cbWechatCheck.isChecked()){
//                        status = 1;
//                    }else if(cbBankcardCheck.isChecked()){
//                        status = 2;
//                    }
//
//                }else {
//                    ToastUtils.showToastShort(context, "需要选中支付方式");
//                    return;
//                }
                if(invoiceMoreInfoEvent==null){
                    ToastUtils.showToastShort(context, "请填写更多信息");
                    return;
                }

                map = BaseMapUtils.getMap(context);
                map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
                map.put("businessNumbers", arrStr.toString());
                map.put("subBillBusinessNumbers", subBillId.toString().trim());
                map.put("invoiceAmount", countMoney);
                map.put("bankAccount",invoiceMoreInfoEvent.getBankAccountNumber());
                map.put("companyName", companyRise);
                map.put("taxpayerIdentificationCode", invoiceMoreInfoEvent.getTaxpayerDistinguishNumber());
                map.put("registerAddress", invoiceMoreInfoEvent.getCompanyAddress());
                map.put("registerPhone", invoiceMoreInfoEvent.getCompanyPhone());
                map.put("bankName", invoiceMoreInfoEvent.getBankAccount());
                map.put("checkTakerName", addressee);
                map.put("checkTakerPhone", relationTelephone);
                map.put("checkTakerAddress", locationArea);
                map.put("remark", invoiceMoreInfoEvent.getRemarksExplain());
                if(dataList!=null && dataList.size()>0){
                    map.put("invoiceInformationId", dataList.get(0).id+"");

                }
                //跳转支付界面
                startActivityForResult(new Intent(context, SetPayPasswordSecondActivity.class).putExtra("type", 3), 13);
                break;
            case  R.id.cb_alipay_check:
                cbAlipayCheck.setChecked(true);

                cbWechatCheck.setChecked(false);
                cbBankcardCheck.setChecked(false);
                break;
            case  R.id.cb_wechat_check:
                cbWechatCheck.setChecked(true);

                cbAlipayCheck.setChecked(false);
                cbBankcardCheck.setChecked(false);
                break;
            case  R.id.cb_bankcard_check:
                cbBankcardCheck.setChecked(true);

                cbWechatCheck.setChecked(false);
                cbAlipayCheck.setChecked(false);
                break;
            default:
                break;
        }
    }

    private void submitDatas( Map<String, Object> map) {

        DataUtils.loadData(context, GetDataConfing.invoiceRest_addInvoice, map, TAG_REQUEST, responseDataCallback);
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
                        try {
                            JSONObject jsonObject = new JSONObject(json);

                            if ("0".equals(jsonObject.getString("code"))) {
                                ToastUtils.showToastShort(context, "开票成功");
                                setResult(200);
                                finish();
                            } else {
                                ToastUtils.showToastShort(context, jsonObject.getString("msg"));
                            }
                            LogUtils.logD("zq", json);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(dataRequest.getWhat() == TAG_REQUEST_QUERY){
                        //查询发票模版
                        InvoiceTemplateJson invoiceTemplateJson = GsonUtils.jsonToBean(json, InvoiceTemplateJson.class);
                        dataList = invoiceTemplateJson.data;
                        if(dataList!=null && dataList.size()>0){
                            etCompanyRise.setText(dataList.get(0).companyName);
                            etAddressee.setText(dataList.get(0).checkTakerName);
                            etRelationTelephone.setText(dataList.get(0).checkTakerPhone);
                            etLocationArea.setText(dataList.get(0).checkTakerAddress);
//                                    etBankaccountAddress.setText("");
                        }
                    }
                }
            }
        }
        //        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode==13) {

                   String payPwd = data.getStringExtra("payPwd");
                if(map!=null && map.size()>0){
                    map.put("payPassword", payPwd);
//                    map.put("payPassword", "/.96e79218965eb72c92a549dd5a330112");
                }
                submitDatas(map);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtils.saveString(this,"remarksExplain","");
        EventBus.getDefault().unregister(this);
    }
}