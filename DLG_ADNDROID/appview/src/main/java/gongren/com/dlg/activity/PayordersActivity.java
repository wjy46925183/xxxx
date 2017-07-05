package gongren.com.dlg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.FinishEvent;
import gongren.com.dlg.utils.Base64Utils;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.utils.MD5Utils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.view.PasswordInputView;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 支付订单的页
 * Created by Administrator on 2017/4/5.
 */
public class PayordersActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.li)
    View li;
    @Bind(R.id.income_text)
    TextView incomeText;
    @Bind(R.id.income_money_text)
    TextView incomeMoneyText;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.xiaofei_text)
    TextView xiaofeiText;
    @Bind(R.id.xiaofei_money_text)
    TextView xiaofeiMoneyText;
    @Bind(R.id.xiaofei_linear)
    LinearLayout xiaofeiLinear;
    @Bind(R.id.line3)
    View line3;
    @Bind(R.id.pingtai_text)
    TextView pingtaiText;
    @Bind(R.id.pingtai_money_text)
    TextView pingtaiMoneyText;
    @Bind(R.id.pingtai_linear)
    LinearLayout pingtaiLinear;
    @Bind(R.id.line4)
    View line4;
    @Bind(R.id.total_text)
    TextView totalText;
    @Bind(R.id.total_money_text)
    TextView totalMoneyText;
    //    @Bind(R.id.tota)
//    LinearLayout totalLinear;
    @Bind(R.id.remain_text)
    TextView remainText;
    @Bind(R.id.remain_money_text)
    TextView remainMoneyText;
    @Bind(R.id.remain_linear)
    LinearLayout remainLinear;
    @Bind(R.id.li2)
    View li2;
    @Bind(R.id.line5)
    View line5;
    @Bind(R.id.total_linear)
    LinearLayout totalLinear;
    @Bind(R.id.password_view)
    PasswordInputView passwordView;
    @Bind(R.id.scrollview)
    ScrollView scrollview;
    @Bind(R.id.tv_forgetPWD)
    TextView tv_forgetPWD;
    @Bind(R.id.line6)
    View line6;
    @Bind(R.id.baoxian_text)
    TextView baoxianText;
    @Bind(R.id.baoxian_money_text)
    TextView baoxianMoneyText;
    @Bind(R.id.baoxian_linear)
    LinearLayout baoxianLinear;
    private Context context;
    private int isfrom = 1;//默认1-是正常支付订单,2-取消订单扣钱.
    double tip;
    double pay;
    double bao;

    private double weiyue_money;//违约金
    private double income_money;//报酬

    private String orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_orders_activity);
        ButterKnife.bind(this);
        context = this;
        getParams();
        initDatas();
        initViews();
        submitPay();
        orders = getIntent().getStringExtra("orders");

        if (TextUtils.isEmpty(orders)) {
            totalLinear.setVisibility(View.GONE);
            line5.setVisibility(View.GONE);
        }
    }

    /**
     * 获取平台费率
     */
    private void initDatas() {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("groupCode", "server.rate");
        DataUtils.loadData(context, GetDataConfing.dictionaryRest, map, 100, responseDataCallback);
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
                    LogUtils.logD("zq", json);
//
                    List<JsonMap<String, Object>> dataList = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                    if (null != dataList && dataList.size() > 0) {
                        JsonMap<String, Object> jsonMap = dataList.get(0);
                        if (!TextUtils.isEmpty(jsonMap.getString("dataValue"))) {
                            Double aDouble = jsonMap.getDouble("dataValue", 0);

                            if (aDouble > 0) {
                                NumberFormat nf = new DecimalFormat("0.0 ");
                                aDouble = Double.parseDouble(nf.format((tip + pay) * aDouble));
                                if (isfrom == 1 && aDouble > 0) {
                                    pingtaiMoneyText.setText(aDouble + "元");//平台费
                                    totalMoneyText.setText((tip + pay + aDouble + bao) + "元");
                                    pingtaiLinear.setVisibility(View.VISIBLE);
                                } else {
                                    pingtaiLinear.setVisibility(View.GONE);
                                }
                            } else {
                                pingtaiLinear.setVisibility(View.GONE);
                            }
//                            MyApplication.dataValueDistance = Integer.parseInt(jsonMap.getString("dataValue"));
                        }

                    }
                }
            }
        }
        //        }
    };


    /**
     * 支付金额
     */
    private void submitPay() {
        passwordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    String psw = s.toString();
                    if (orders == null) {//其他的页面
                        //测试，假装输入密码之后，就支付成功了，回调支付密码给上一个页面.
                        String passwd = Base64Utils.getBase64(MD5Utils.MD5Encode(psw) + "/.");
                        Intent intent = new Intent();
                        intent.putExtra("payPassword", passwd);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Map<String, Object> map = BaseMapUtils.getMap(PayordersActivity.this);
                        map.put("orders", orders);
                        map.put("payPassword", Base64Utils.getBase64(MD5Utils.MD5Encode(psw) + "/."));
                        DataUtils.loadData(PayordersActivity.this, GetDataConfing.ORDER_SETTLEMENT, map, 1, new ResponseDataCallback() {
                            @Override
                            public void onFinish(DataRequest dataRequest) {
                                String responseData = dataRequest.getResponseData();
                                try {
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    if ("0".equals(jsonObject.optString("code"))) {
                                        ToastUtils.showToastShort(PayordersActivity.this, "支付成功");
                                        EventBus.getDefault().post(new FinishEvent());
                                        finish();
                                    } else {
                                        ToastUtils.showToastShort(PayordersActivity.this, jsonObject.optString("msg") + ",请重新支付");
                                        passwordView.setText("");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void getParams() {
        Intent intent = getIntent();
        if (intent != null) {
            isfrom = intent.getIntExtra("isfrom", 1);
            tip = getIntent().getDoubleExtra("tip", 0);
            pay = getIntent().getDoubleExtra("pay", 0);
            bao = getIntent().getDoubleExtra("bao", 0);
            if (isfrom != 1) {
                weiyue_money = intent.getDoubleExtra("pay", 0);
                income_money = intent.getDoubleExtra("tip", 0);
            }
        }
    }

    private void initViews() {
        if (bao == 0) {
            baoxianLinear.setVisibility(View.GONE);
            line6.setVisibility(View.GONE);
        }
        tvTitle.setText("取消订单");
//        tvSave.setVisibility(View.GONE);
        tvSave.setText("充值");

        if (isfrom == 1) {//正常支付订单
            tvTitle.setText("支付");
//            payPeopleText.setText("向 王翠平 支付工资");
            incomeText.setText("报酬");


            double total = (tip + pay + bao) * 1;
            String format = String.format("%.2f", total);

            xiaofeiMoneyText.setText(tip + "元");//小费
            incomeMoneyText.setText(pay + "元");//报酬
            baoxianMoneyText.setText(bao + "元");
            totalMoneyText.setText(format + "元");

            line2.setVisibility(View.VISIBLE);
            xiaofeiLinear.setVisibility(View.VISIBLE);
            line3.setVisibility(View.VISIBLE);
        } else {//取消订单扣钱
//            payPeopleText.setText("向 平台 支付违约金");
            incomeText.setText("违约金");
            incomeMoneyText.setText(weiyue_money + "元");

            if (income_money > 0) {
                incomeText.setText("报酬");
                incomeMoneyText.setText(income_money + "元");

                xiaofeiText.setText("违约金");
                xiaofeiMoneyText.setText(weiyue_money + "元");

                totalMoneyText.setText((income_money + weiyue_money) + "元");
            } else {
                incomeText.setText("违约金");
                incomeMoneyText.setText(weiyue_money + "元");

                line2.setVisibility(View.GONE);
                xiaofeiLinear.setVisibility(View.GONE);
                line4.setVisibility(View.GONE);
            }
            line3.setVisibility(View.GONE);
            pingtaiLinear.setVisibility(View.GONE);

        }
        remainLinear.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_save, R.id.tv_forgetPWD})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save://充值
                startActivity(new Intent(this, RechargeActivity.class));
                break;

            case R.id.tv_forgetPWD://忘记密码
                startActivity(new Intent(this, RestPasswordActivity.class));
                break;
        }
    }
}
