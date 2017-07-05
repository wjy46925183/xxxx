package gongren.com.dlg.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.alipayUtils.AlipayUtils;
import gongren.com.dlg.alipayUtils2_0.AuthResult;
import gongren.com.dlg.alipayUtils2_0.PayResult;
import gongren.com.dlg.javabean.QuickPaymentNumber;
import gongren.com.dlg.javabean.QuickPaymentSmsJson;
import gongren.com.dlg.javabean.ReceiveReturnIdJson;
import gongren.com.dlg.javabean.YiKaTongBean;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataCacheUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.GsonUtils;
import gongren.com.dlg.utils.LogUtils;
import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;
import gongren.com.dlg.wxapi.WechatPayUtils;

/**
 * 钱包充值
 */
public class RechargeActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.layout_type)
    LinearLayout layoutType;
//    @Bind(R.id.layout_alipay)
//    LinearLayout layoutAlipay;
//    @Bind(R.id.layout_weixin)
//    LinearLayout layoutWeixin;
//    @Bind(R.id.layout_card)
//    LinearLayout layoutCard;
    @Bind(R.id.et_num)
    EditText etNum;
    @Bind(R.id.btn_chongzhi)
    Button btnChongzhi;

    @Bind(R.id.tv_getcode)
    TextView tvGetcode;
    @Bind(R.id.edit_code)
    EditText editCode;
    @Bind(R.id.tv_back_proving)
    LinearLayout tvBackProving;
    @Bind(R.id.checkbox_yi)
    CheckBox checkbox_yi;  //一卡通

    @Bind(R.id.checkbox_zhi)
    CheckBox checkbox_zhi; //支付宝

    @Bind(R.id.checkbox_wei)
    CheckBox checkbox_wei; //微信

    @Bind(R.id.text_hit)
    TextView textHit; //银行卡绑定提示
    @Bind(R.id.checkbox_bank_card)
    CheckBox checkboxBankCard; //银行卡
    @Bind(R.id.box_bank_card)
    ImageView boxBankCard;

    private static final int SDK_PAY_FLAG = 1;      //支付宝支付的TAG
    private static final int SDK_AUTH_FLAG = 2;     //支付宝支付的TAG
    private static final int REQUEST_QUERY_PAYLIST = 1;      //查询支付方式
    private static final int TAG_ALIPAY = 2;         //支付宝支付
    private static final int TAG_WEICHAT = 3;        //微信支付
    private static final int TAG_YIKATONG = 4;     //一卡通支付
    private static final int TAG_COMIT = 5;          //提交订单
    private static final int TAG_BANK = 6;          //银行卡支付

    private static final int REQUEST_BandingWX = 7;          //绑定微信
    private Dialog dialog;
    private IWXAPI iwxapi;
    private String orderId;                      //预支付工资界面传过来的发单id
    private LocalBroadcastManager manager;
    private WeichatReceiver weichatReceiver;
    private List<JsonMap<String, Object>> data = new ArrayList<>();
    private int payType;       //支付方式，1支付宝，2银行卡，3微信 ,4一卡通

    private int i = 60;       //倒计时数字
    private String mobile;   //用户手机号
    private long codeTime;   //时间戳
    private final int what_CODECHECK = 12;     //验证码校验
    private static final int REQUEST_CODE = 11;      //获取验证码的请求
    private QuickPaymentNumber quickPaymentNumber;
    private String alipayBindId;
    private String weChatBindId;
    private String bankBindId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        orderId = getIntent().getStringExtra("orderId");
        tvTitle.setText("充值");
        tvSave.setText("编辑");
        mShareAPI = UMShareAPI.get(context);

        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, WechatPayUtils.APP_ID, true);
        // 将该app注册到微信
        msgApi.registerApp(WechatPayUtils.APP_ID);

        iwxapi = WXAPIFactory.createWXAPI(this, WechatPayUtils.APP_ID);
        registerWeichatPayReceiver();
        checkbox_zhi.setChecked(true);
        payType = 1;
        checkbox_yi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    payType = TAG_YIKATONG;
                    checkboxBankCard.setChecked(false);
                    checkbox_zhi.setChecked(false);
                    checkbox_wei.setChecked(false);
//                    checkbox_yi.setChecked(true);
                    tvBackProving.setVisibility(View.GONE);
                }
            }
        });

        checkbox_zhi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    payType = 1;
                    checkboxBankCard.setChecked(false);
                    checkbox_wei.setChecked(false);
                    checkbox_yi.setChecked(false);
//                    checkbox_zhi.setChecked(true);
                    tvBackProving.setVisibility(View.GONE);
                }
            }
        });

        checkbox_wei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    payType = 2;
                    checkboxBankCard.setChecked(false);
                    checkbox_yi.setChecked(false);
                    checkbox_zhi.setChecked(false);
//                    checkbox_wei.setChecked(true);
                    tvBackProving.setVisibility(View.GONE);
                }
            }
        });
        checkboxBankCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    payType = 3;
                    checkbox_wei.setChecked(false);
                    checkbox_yi.setChecked(false);
                    checkbox_zhi.setChecked(false);
//                    checkboxBankCard.setChecked(true);
                    tvBackProving.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 查询支付方式
     */
    private void queryPayList() {
        dialog = DialogUtils.showLoadingDialog(context);
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("minId", "0");
        map.put("size", "10");
        map.put("payType", "");
        DataUtils.loadData(context, GetDataConfing.queryBindInfoList, map, REQUEST_QUERY_PAYLIST, responseDataCallback);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (tvTitle != null) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    if (!TextUtils.isEmpty(json)) {
                        if (dataRequest.getWhat() == REQUEST_QUERY_PAYLIST) {      //查询支付方式
                            LogUtils.logD("zq", json);
                            data = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                            setPayType();
                        } else if (dataRequest.getWhat() == TAG_ALIPAY) {      //支付宝支付
                            LogUtils.logD("zq", json);
                            List<JsonMap<String, Object>> data = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                            if (data != null && data.size() > 0) {
                                String tradeId = data.get(0).getStringNoNull("billBusinessNumber");
                                String returnRul = data.get(0).getStringNoNull("returnRul");
                                if (!TextUtils.isEmpty(tradeId) && !TextUtils.isEmpty(returnRul)) {
                                    AlipayUtils alipayUtils = new AlipayUtils(context, tradeId, returnRul, mHandler);
                                    alipayUtils.aliPay("金橙", "金橙信息", etNum.getText().toString());
                                }
                            }
                        } else if (dataRequest.getWhat() == TAG_WEICHAT) {      //微信支付
                            LogUtils.logD("zq", json);
                            List<JsonMap<String, Object>> data = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                            if (data != null && data.size() > 0) {
                                JsonMap<String, Object> jsonMap = data.get(0);
                                PayReq req = new PayReq();
                                req.appId = WechatPayUtils.APP_ID;//
                                req.partnerId = jsonMap.getStringNoNull("mchId");
                                req.prepayId = jsonMap.getStringNoNull("prepayId");
                                req.nonceStr = jsonMap.getStringNoNull("nonceStr");
                                req.timeStamp = jsonMap.getStringNoNull("time");
                                req.packageValue = jsonMap.getStringNoNull("oddPackage");
                                req.sign = jsonMap.getStringNoNull("sign");
                                req.extData = "app Data";
                                DataCacheUtils.getIstance().setPayReqCache(req);
                                iwxapi.sendReq(req);
//                                req.appId = "wx68a8f66601847550";//
//                                req.partnerId = "1401108902";
//                                req.prepayId = "wx20170420192912af084a6cbb0698161052";
//                                req.nonceStr = "4WEE5cRWTerWFW62";
//                                req.timeStamp = "1492687778";
//                                req.packageValue = "Sign=WXPay";
//                                req.sign = "854EBB25AE91DB8E61A36F6F30BC8A15";
//                                req.openId = weChatBindId;
                                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信

                            } else {
                                JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                                ToastUtils.showToastShort(context, jsonMap.getStringNoNull("msg"));
                            }
                        } else if (dataRequest.getWhat() == TAG_COMIT) {      //提交发布的订单
                            LogUtils.logD("zq", json);
                            JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                            ToastUtils.showToastShort(context, jsonMap.getStringNoNull("msg"));
                            int code = jsonMap.getInt("code");
                            if (code == 0) {
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        } else if (dataRequest.getWhat() == REQUEST_BandingWX) {      //绑定微信
//                            queryPayList();
//                            mShareAPI.deleteOauth(context, SHARE_MEDIA.WEIXIN, delectauthListener);
                            ReceiveReturnIdJson receiveReturnIdJson = GsonUtils.jsonToBean(json, ReceiveReturnIdJson.class);
                            if (receiveReturnIdJson.code.equals("0")) {
                                ToastUtils.showToastShort(context, "微信绑定成功");
                                queryPayList();
                            } else {
                                ToastUtils.showToastShort(context, receiveReturnIdJson.msg);
                            }

                        } else if (dataRequest.getWhat() == REQUEST_CODE) {
                            QuickPaymentSmsJson smsJson = GsonUtils.jsonToBean(json, QuickPaymentSmsJson.class);
                            if (smsJson.data != null && smsJson.data.size() > 0) {
                                quickPaymentNumber = smsJson.data.get(0);
                            }
                        } else if (dataRequest.getWhat() == TAG_BANK) {
                            //银行卡支付
                        } else if (dataRequest.getWhat() == TAG_YIKATONG) {
                            YiKaTongBean bean = GsonUtils.jsonToBean(json, YiKaTongBean.class);
                            if (bean != null) {
                                String url = bean.getData().get(0).getPayUrl();
                                String code = bean.getData().get(0).getJsonRquestData();
                                Intent intent = new Intent(RechargeActivity.this, CMBWebViewActivity.class);
                                intent.putExtra("url", url);
                                intent.putExtra("code", code);
                                startActivity(intent);
                            }
                        }
                    }
                }
            }
        }
    };

    private void setPayType() {
        if (data != null && data.size() > 0) {
            if (payType != 0) {
                for (int i = 0; i < data.size(); i++) {
                    JsonMap<String, Object> jsonMap = data.get(i);
                    final int acctType = jsonMap.getInt("payType");
                    if (acctType != 3) {
                        return;
                    }
                    String bankAcct = jsonMap.getStringNoNull("payAccount");
                    String bankType = jsonMap.getStringNoNull("depositBank");
                    textHit.setText(bankAcct.substring(0, 4) + "****" +
                            bankAcct.substring(bankAcct.length() - 4, bankAcct.length())
                            + "  " + bankType);
                    checkboxBankCard.setVisibility(View.VISIBLE);
                    boxBankCard.setVisibility(View.GONE);
                }
            }
        }
    }

    @OnClick({R.id.iv_back,  R.id.layout_card, R.id.btn_chongzhi, R.id.tv_save, R.id.tv_getcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:    //返回
                finish();
                break;
            case R.id.tv_save:    //编辑
                startActivityForResult(new Intent(context, EditWalletCardActivity.class), 1);
                break;
            case R.id.layout_card:      //绑定银行卡
                if (boxBankCard.isShown()) {
                    startActivityForResult(new Intent(context, BindingBankCardActivity.class), 1);
                }
                break;
            case R.id.btn_chongzhi:     //立即充值
                String editCodeStr = editCode.getText().toString().trim();
                if (payType == 0) {
                    ToastUtils.showToastShort(context, "请先绑定一种支付方式");
                    return;
                }
                String money = etNum.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    ToastUtils.showToastShort(context, "请输入充值金额");
                    return;
                }
                if (Long.parseLong(money) <= 0) {
                    ToastUtils.showToastShort(context, "充值金额不能为0");
                    return;
                }
                if (payType == 3) {
                    if (TextUtils.isEmpty(editCodeStr)) {
                        ToastUtils.showToastShort(context, "验证码不能为空");
                        return;
                    }
                }
                dialog = DialogUtils.showLoadingDialog(context);
                Map<String, Object> map = BaseMapUtils.getMap(context);
                map.put("amount", money);
                map.put("ptCashAva", Long.parseLong(money) * 10 + "");//橙子数
                map.put("ptCashAva", Long.parseLong(money) * 10 + "");
                map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));

//                map.put("ptCashAva", Long.parseLong(money) * 10 + "");
                if (payType == 1) {                //支付宝支付
                    map.put("payType", "1");
                    map.put("bindId", "");
                    DataUtils.loadData(context, GetDataConfing.accountRecharge, map, TAG_ALIPAY, responseDataCallback);
                } else if (payType == 2) {         //微信支付
                    map.put("payType", "2");
                    map.put("bindId", "");
                    DataUtils.loadData(context, GetDataConfing.accountRecharge, map, TAG_WEICHAT, responseDataCallback);
                } else if (payType == 3) {          //招商银行支付
                    dialog.dismiss();
//                    ToastUtils.showToastShort(context, getString(R.string.toast_jijian_cancle));
                    map.put("payType", "3");
                    map.put("bindId", bankBindId);
                    map.put("businessNumber", quickPaymentNumber.billBusinessNumber);
                    map.put("paymentNo", quickPaymentNumber.paymentNo);
                    map.put("smsValidationcode", editCodeStr);
                    DataUtils.loadData(context, GetDataConfing.accountRecharge, map, TAG_BANK, responseDataCallback);
                } else if (payType == 4) {
                    dialog.dismiss();
//                    ToastUtils.showToastShort(context, getString(R.string.toast_jijian_cancle));
                    map.put("payType", "4");
                    map.put("amount", money);
                    DataUtils.loadData(context, GetDataConfing.accountRecharge, map, TAG_YIKATONG, responseDataCallback);
                }
                break;
            case R.id.tv_getcode:
                String moneyTemp = etNum.getText().toString();
                if (TextUtils.isEmpty(moneyTemp)) {
                    ToastUtils.showToastShort(context, "请输入充值金额");
                    return;
                }
                getCode(moneyTemp);

                break;
        }
    }

    /**
     */
    private long timestamp;

    /**
     * 获取验证码的请求
     */
    private void getCode(String moneyTemp) {
        dialog = DialogUtils.showLoadingDialog(context);
        //获取发送验证码的时间戳
        codeTime = System.currentTimeMillis();
        i = 60;
        tvGetcode.setEnabled(false);
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("bindId", bankBindId);
        map.put("amount", moneyTemp);
        startTimer();
        DataUtils.loadData(context, GetDataConfing.sendQuickPayMessage, map, REQUEST_CODE, responseDataCallback);
    }

    private MyCount myCount = null;

    /**
     * 倒计时方法
     */
    private void startTimer() {
        timestamp = System.currentTimeMillis();
        myCount = new MyCount(60000, 1000);
        myCount.start();
    }

    /**
     * 计时
     */
    class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetcode.setText(millisUntilFinished / 1000 + "秒后重发");
            tvGetcode.setEnabled(false);
        }

        @Override
        public void onFinish() {
            // 在这里进行设置解决时间停留的问题
            tvGetcode.setText("获取验证码");
            tvGetcode.setEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryPayList();
    }

    /**
     * 微信登录监听
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private UMShareAPI mShareAPI;
    private UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            //上传微信信息
            Map<String, Object> map1 = BaseMapUtils.getMap(context);
            map1.put("payAccount", map.get("openid"));
            map1.put("payType", 2 + "");
            map1.put("payAccounthouseholder", map.get("screen_name"));
            map1.put("reservedPhone", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.MOBILE));
            DataUtils.loadData(context, GetDataConfing.BandingWX, map1, REQUEST_BandingWX, responseDataCallback);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            if (throwable.getMessage().contains("没有安装应用")) {
                ToastUtils.showToastLong(context, "你尚未安装微信，请安装后再进行操作");
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.showToastLong(context, "您取消了微信授权");
            if (dialog != null) {
                dialog.dismiss();
            }

        }
    };

    /**
     * 解除微信绑定
     */
    private UMAuthListener delectauthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//            ToastUtils.showToastLong(context, throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
//            ToastUtils.showToastLong(context, "您取消微信授权失败");
        }
    };

    /**
     * 充值成功后执行的方法，若是普通充值则直接finish，若是发单界面跳转的充值则再次请求发单接口
     */
    private void paySuccess() {
        if (TextUtils.isEmpty(orderId)) {
            setResult(RESULT_OK);
            finish();
        } else
            submitOrder();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                        paySuccess();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(context,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(context,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
            }
        }
    };


    public class WeichatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.gongren.weichatPay")) {
                int errCode = intent.getIntExtra("errCode", -1);
                if (errCode == 0) {
                    ToastUtils.showToastShort(context, "充值成功");
                    paySuccess();
                } else
                    ToastUtils.showToastShort(context, "充值失败");
                LogUtils.logD("zq", "ssssssssssssssssssss" + errCode);
            }
        }
    }

    /**
     * 提交发布的订单
     */
    private void submitOrder() {
        dialog = DialogUtils.showLoadingDialog(context);
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("id", orderId);
        DataUtils.loadData(context, GetDataConfing.payMission, map, TAG_COMIT, responseDataCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        manager.unregisterReceiver(weichatReceiver);
    }

    /**
     * 注册广播，接收微信支付的errCode
     */
    private void registerWeichatPayReceiver() {
        manager = LocalBroadcastManager.getInstance(context);
        weichatReceiver = new WeichatReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.gongren.weichatPay");
        manager.registerReceiver(weichatReceiver, intentFilter);
    }
}
