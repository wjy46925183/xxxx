package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.string.StringUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.LogUtils;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.view.AlertView;
import gongren.com.dlg.view.OnItemClickListener;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 提现
 * Created by lin.li on 2017/2/12.
 */
public class GetCashActivity extends BaseActivity {

    //标题
    @Bind(R.id.tv_title)
    TextView tvTitle;
    //编辑
    @Bind(R.id.tv_save)
    TextView tvSave;
    //提现数目
    @Bind(R.id.edit_num)
    EditText editNum;
    //提现按钮
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    //支付宝的名字
    @Bind(R.id.card_alipay_name)
    TextView cardAlipayName;
    //支付宝账号
    @Bind(R.id.card_alipay_desc)
    TextView cardAlipayDesc;
    //支付宝选择
    @Bind(R.id.img_alipay_select)
    ImageView imgAlipaySelect;
    //支付宝的布局
    @Bind(R.id.alipay_layout)
    RelativeLayout zhifubaoLayout;
    //微信名字
    @Bind(R.id.card_wechat_name)
    TextView cardWechatName;
    //微信账号
    @Bind(R.id.card_wechat_desc)
    TextView cardWechatDesc;
    //选择微信图片
    @Bind(R.id.img_wechat_select)
    ImageView imgWechatSelect;
    //微信布局
    @Bind(R.id.wechat_layout)
    RelativeLayout wechatLayout;
    //银行卡名称
    @Bind(R.id.card_bank_name)
    TextView cardBankName;
    //银行卡账号
    @Bind(R.id.card_bank_desc)
    TextView cardBankDesc;
    //银行卡选择
    @Bind(R.id.img_bank_select)
    ImageView imgBankSelect;
    @Bind(R.id.img_logo3)
    ImageView img_logo3;
    //银行卡的布局
    @Bind(R.id.bank_layout)
    RelativeLayout bankLayout;
    //排序布局
    @Bind(R.id.sorting_layout)
    LinearLayout sortingLayout;
    //可提现金额
    @Bind(R.id.tv_canWithdraw)
    TextView tv_canWithdraw;
    //可提现次数
    @Bind(R.id.tv_freetime)
    TextView tv_freetime;
    //手续费
    @Bind(R.id.tv_poundage)
    TextView tv_poundage;
    @Bind(R.id.tv_getcode)
    TextView tvGetcode;
    @Bind(R.id.edit_code)
    EditText editCode;

    private int withDrawalCount = 2;     //剩余的免费提现次数

//    private Double canWithDrawCash;
    private Double withdrawCashAmount; //可提现余额
    private Double orangeCount;
    private Double noWithdrawCash;
    private Dialog dialog;
    private Dialog wddialog;
    private String money;

    private String alipayBindId;
    private String weChatBindId;
    private String bankBindId;

    private static final int REQUEST_CODE = 11;      //获取验证码的请求
    private int i = 60;       //倒计时数字
    private String mobile;   //用户手机号
    private long codeTime;   //时间戳
    private final int what_CODECHECK = 12;     //验证码校验

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cash);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("提现");
        tvSave.setText("编辑");
        mShareAPI = UMShareAPI.get(context);
        Intent intent = getIntent();
        orangeCount = intent.getDoubleExtra("orangeCount", 0);
        noWithdrawCash = intent.getDoubleExtra("noWithdrawCash", 0);
        withdrawCashAmount = intent.getDoubleExtra("withdrawCashAmount", 0);
        tv_canWithdraw.setText("可提现金额：" + withdrawCashAmount + "元");
//        canWithDrawCash = orangeCount - noWithdrawCash;
        editNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                money = s.toString();
//                if (!TextUtils.isEmpty(money)) {
//                    if (StringUtils.checkMoney(money)) {
//                        if (Double.parseDouble(money) > Double.parseDouble(canWithDrawCash + "") / 10) {
//                            editNum.setText((Double.parseDouble(canWithDrawCash + "") / 10) + "");
//                        }
//                    }
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 查询银行卡列表,查询支付宝,查询微信
     */
    private void query() {
        dialog = DialogUtils.showLoadingDialog(context);
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("minId", "0");
        map.put("size", "10");
        map.put("payType", "");

        DataUtils.loadData(context, GetDataConfing.queryBindInfoList,
                map, REQUEST_QUERY, responseDataCallback);
        //查询可提现次数
        DataUtils.loadData(context, GetDataConfing.withdrawNum,
                map, REQUEST_WithDrawal_COUNT, responseDataCallback);
    }

    /**
     * 微信登录监听
     */
    private UMShareAPI mShareAPI;
    private String openId = "";


    private void BandingWeichat(Map<String, String> map) {
        /*"payAccount": "",//支付账号
    “payAccountHouseholder”:””,//支付户名
    "payType": null, //支付类型(0.橙子,1.支付宝,2.微信,3.银行卡)
"reservedPhone": "", //预留手机号
“vaildCode”:””//验证码
“bindId”://快捷支付绑定id
*/
        String name = map.get("screen_name");
        name  = StringUtil.filterEmoji(name,"");
        com.common.string.LogUtils.e("==========|="+name);
        Map<String, Object> map1 = BaseMapUtils.getMap(context);
        map1.put("payAccount", map.get("openid"));
        map1.put("payType", 2 + "");
        map1.put("payAccounthouseholder", name);
        map1.put("reservedPhone", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.MOBILE));
        DataUtils.loadData(context, GetDataConfing.BandingWX,
                map1, REQUEST_BandingWX, responseDataCallback);
    }

    /**
     * 提现
     * SharedPreferencesUtils.getString(context, "WXopenid");
     */
    String payName, payUid, payPwd;

    private void withDrawal() {
        String orange = editNum.getText().toString();
        String editCodeStr = editCode.getText().toString().trim();
        String tel = SharedPreferencesUtils.getString(context, SharedPreferencesUtils.MOBILE);
        if (WITHDRAWTAG == 0) {
            ToastUtils.showToastShort(context, "请选择支付方式");
            return;
        }
        if (TextUtils.isEmpty(orange)) {
            ToastUtils.showToastShort(context, "请输入提现的金额");
            return;
        }
        if (Float.parseFloat(orange) == 0) {
            ToastUtils.showToastShort(context, "提现的金额不能为0");
            return;
        }
        if (Float.parseFloat(orange) > withdrawCashAmount) {
            ToastUtils.showToastShort(context, "最多可提现" + withdrawCashAmount + "元");
            return;
        }
        if (WITHDRAWTAG == 1) {

        } else if (WITHDRAWTAG == 6) {
            if (bankMap == null || bankMap.size() <= 0) {
                ToastUtils.showToastShort(context, "请先绑定银行卡");
                return;
            }
        } else {
            if (TextUtils.isEmpty(openId)) {
                ToastUtils.showToastShort(context, "请先绑定微信账号");
                return;
            }
        }
        if (TextUtils.isEmpty(tel)) {
            ToastUtils.showToastShort(context, "请输入手机号码");
            return;
        }
        if (!StringUtils.isPhoneNumber(tel)) {
            ToastUtils.showToastShort(context, "请输入格式正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(editCodeStr)) {
            ToastUtils.showToastShort(context, "验证码不能为空");
            return;
        }

        wddialog = DialogUtils.showLoadingDialog(context);
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("amount", (Double.parseDouble(orange)) + "");
        map.put("mobile", tel);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("vaildCode", editCodeStr);
        map.put("payPassword", payPwd);
        if (WITHDRAWTAG == 1) {
            //支付宝提现
            map.put("payType", "1");
//            map.put("payName", payName);
            map.put("bindId", alipayBindId);
        } else if (WITHDRAWTAG == 2) {
            //微信提现
            map.put("payType", "2");
//            map.put("bindId", "201704241826235251638973176");
            map.put("bindId", weChatBindId);
        } else {
            //银行卡提现
            payName = bankMap.getStringNoNull("openName");
            map.put("payName", payName);
            map.put("payType", "3");
//            map.put("bindId", bankMap.getStringNoNull("id"));
            map.put("bindId", bankBindId);
        }
        DataUtils.loadData(context, GetDataConfing.withdraw, map, REQUEST_WITHDRAWAL, responseDataCallback);
    }

    private static final int REQUEST_WITHDRAWAL = 1;         //提现的请求

    private static final int REQUEST_QUERY = 4;            //查询
    private static final int REQUEST_BandingWX = 5;          //绑定微信

    private static final int REQUEST_WithDrawal_COUNT = 6;   //查询提现次数

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (tvTitle != null) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (wddialog != null) {
                    wddialog.dismiss();
                }
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    if (!TextUtils.isEmpty(json)) {
                        if (dataRequest.getWhat() == REQUEST_WITHDRAWAL) {      //提现
                            LogUtils.logD("zp", json);
                            JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                            if (jsonMap.getInt("code") == 0) {
                                setResult(RESULT_OK);
                                ToastUtils.showToastShort(context, "提现成功");
                                finish();
                            } else {
                                ToastUtils.showToastShort(context, "提现失败");
                            }
                        } else if (dataRequest.getWhat() == REQUEST_BandingWX) {      //绑定微信
                            query();
                            selectWitDrawType(1);
                            //绑定成功后解除回调
//                            mShareAPI.deleteOauth(context, SHARE_MEDIA.WEIXIN, delectauthListener);
                        } else if (dataRequest.getWhat() == REQUEST_QUERY) {        //查询微信
                            LogUtils.logD("zp", json);
                            List<JsonMap<String, Object>> data = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                            if (data != null) {
                                setData(data);
                            }
                        } else if (dataRequest.getWhat() == REQUEST_WithDrawal_COUNT) {        //查询提现次数
                            LogUtils.logD("zp", json);
                            JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                            String data = jsonMap.getStringNoNull("data", "0");
                            data = StringUtils.replaceJson(data);
                            if (!TextUtils.isEmpty(data)) {
                                withDrawalCount = 2 - Integer.parseInt(data);
                            }
                            withDrawalCount = withDrawalCount < 0 ? 0 : withDrawalCount;
                            tv_freetime.setText("本月剩余免费次数：" + withDrawalCount);

//                            tv_canWithdraw.setText("可提现金额：" + Double.parseDouble((canWithDrawCash) + "") / 10 + "元");

                            if (withDrawalCount < 1) {
                                tv_poundage.setText("手续费：2元");
                                //判断是否不可提现
//                                if (canWithDrawCash == 0 || orangeCount < 20) {
//                                    tv_canWithdraw.setHint("金橙不足无法提现");
//                                } else if (noWithdrawCash < 20) {
//                                    canWithDrawCash = orangeCount - 20;
//                                    if(canWithDrawCash>0){
//                                        tv_canWithdraw.setHint("可提现金额：" + Double.parseDouble(canWithDrawCash + "") / 10 + "元");
//                                    }else{
//                                        tv_canWithdraw.setHint("可提现金额：0元");
//                                    }
//                                }
                            } else {
                                tv_poundage.setText("手续费：0元");
                            }
                        }
                    }
                }
            }
        }
    };

    /**
     * 适配数据
     *
     * @param data
     * @param alipayType
     * @param wechatType
     * @param bankType
     * @param alipayTypeindex
     * @param wechatTypeindex
     * @param bankTypeindex
     */
    private JsonMap alipayMap, wxMap, bankMap;

    private void setData(List<JsonMap<String, Object>> data) {
        JsonMap bankMap1 = null;
        for (int i = 0; i < data.size(); i++) {
            switch (data.get(i).getInt("payType")) {
                case 1:
                    if (alipayMap == null) {
                        alipayMap = data.get(i);
                    }

                    break;
                case 2:
                    wxMap = data.get(i);
                    break;
                case 3:
                    if (bankMap1 == null) {
                        bankMap = data.get(i);
                        bankMap1 = data.get(i);
                    }

                    break;
            }
        }
        if (alipayMap != null) {
            alipayBindId = alipayMap.getStringNoNull("bindId");
            payUid = alipayMap.getStringNoNull("payAccount");
            payName = alipayMap.getStringNoNull("payAccounthouseholder");
            cardAlipayDesc.setText(alipayMap.getStringNoNull("payAccount"));
            imgAlipaySelect.setImageResource(R.mipmap.radiobutton_nochecked);
        } else {
            imgAlipaySelect.setImageResource(R.mipmap.next);
            cardAlipayDesc.setText("未绑定");
        }
        if (wxMap != null) {
            openId = wxMap.getStringNoNull("payAccount");
            String wxname = wxMap.getStringNoNull("payAccounthouseholder");
            cardWechatDesc.setText(wxname);
            imgWechatSelect.setImageResource(R.mipmap.radiobutton_nochecked);
            weChatBindId = wxMap.getStringNoNull("bindId");
        } else {
            imgWechatSelect.setImageResource(R.mipmap.next);
            cardWechatDesc.setText("未绑定");
        }
        if (bankMap != null) {
            String bankAcct = bankMap1.getStringNoNull("payAccount");
            String bankType1 = bankMap1.getStringNoNull("depositBank");
//            银行icon
            String iamgIcon = bankMap.getStringNoNull("bankIconUrl");
            Glide.with(this).load(iamgIcon).fitCenter().error(R.mipmap.zhifu_yiwangtong)
                    .into(img_logo3);
//            img_logo3.setImageResource(getResources().
//                    getIdentifier(bankMap.getStringNoNull("bankIconUrl"), "mipmap", getPackageName()));
            if (!TextUtils.isEmpty(bankType1)) {
                cardBankName.setText(bankType1);
            }
            cardBankDesc.setText(bankAcct.substring(0, 4) + "****" +
                    bankAcct.substring(bankAcct.length() - 4, bankAcct.length()));
            imgBankSelect.setImageResource(R.mipmap.radiobutton_nochecked);
            bankBindId = bankMap1.getStringNoNull("bindId");
        } else {
            imgBankSelect.setImageResource(R.mipmap.next);
            cardBankDesc.setText("未绑定");
            cardBankName.setText("银行卡");
        }
        getSaveData();
    }

    /**
     * 获取保存的数据
     */
    private void getSaveData() {
        JsonMap<String, Object> map = JsonParseHelper.getJsonMap(SharedPreferencesUtils.
                getString(context, SharedPreferencesUtils.PayType));
        if (map != null) {
            int tag = map.getInt(SharedPreferencesUtils.
                    getString(context, SharedPreferencesUtils.MOBILE), 0);
            selectWitDrawType(tag);
        }
    }

    private UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            //上传微信信息
            BandingWeichat(map);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            if (throwable.getMessage().contains("没有安装应用")) {
                ToastUtils.showToastLong(context, "你尚未安装微信，请安装后再进行操作");
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.showToastLong(context, "您取消了微信授权");
        }
    };

    /**
     * 返回
     */
    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    /**
     * 编辑
     */
    @OnClick(R.id.tv_save)
    public void edit() {
        startActivity(new Intent(this, EditWalletCardActivity.class));
    }

    /**
     * 提现按钮
     */
    @OnClick({R.id.btn_confirm, R.id.tv_getcode})
    public void confirm(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                if (WITHDRAWTAG == 0) {
                    ToastUtils.showToastShort(context, "请选择提现方式");
                    return;
                }
                if (TextUtils.isEmpty(editNum.getText().toString().trim())) {
                    ToastUtils.showToastShort(context, "请输入提现金额");
                    return;
                }
                if (!StringUtils.checkMoney(editNum.getText().toString())) {
                    ToastUtils.showToastShort(context, "请输入正确的金额");
                    return;
                }
                if (!StringUtils.isVerifyCode(editCode.getText().toString().trim())) {
                    ToastUtils.showToastShort(context, "请输入验证码");
                    return;
                }
                checkCode();
//                startActivityForResult(new Intent(context, SetPayPasswordSecondActivity.class).putExtra("type", 3), 13);
                break;
            case R.id.tv_getcode:
                getCode();
                break;
        }

    }

    /**
     * 获取验证码的请求
     */
    private void getCode() {
        dialog = DialogUtils.showLoadingDialog(context);
        //获取发送验证码的时间戳
        codeTime = System.currentTimeMillis();
        i = 60;
        tvGetcode.setEnabled(false);
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("phone", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.MOBILE));
        DataUtils.loadData(context, GetDataConfing.SYSTEM_NOTECODE, map, REQUEST_CODE, responseDataCallbackVerification);
    }

    /**
     * 校验验证码的网络请求
     */
    private void checkCode() {

        if (!StringUtils.isVerifyCode(editCode.getText().toString())) {
            ToastUtils.showToastShort(context, "请输入正确的验证码格式");
        } else {
            //验证码校验
            Map<String, Object> map = BaseMapUtils.getMap(context);
            map.put("phone", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.MOBILE));
            map.put("verifyCode", editCode.getText().toString());
            map.put("format", "format");
            DataUtils.loadData(context, GetDataConfing.REGISTER_CHECKVERIFYCODE, map, what_CODECHECK, responseDataCallbackVerification);
        }

//        startActivity(new Intent(context, SetPayPasswordActivity.class).putExtra("type", 2));
    }

    private Handler handler = new Handler();
    /**
     * 读秒倒计时
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                if (i < 1) {
                    tvGetcode.setText("再次发送");
                    tvGetcode.setEnabled(true);
                    i = 0;
                } else {
                    handler.postDelayed(this, 1000);
                    tvGetcode.setText(i-- + "s后重发");
                    tvGetcode.setEnabled(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallbackVerification = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (dialog != null && dialog.isShowing())
                dialog.cancel();
            if (tvTitle != null) {
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    if (!TextUtils.isEmpty(json)) {
                        if (dataRequest.getWhat() == REQUEST_CODE) {      //获取验证码
                            JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                            ToastUtils.showToastShort(context, "获取验证码成功");
                            if (jsonMap.getInt("code") == 0) {
                                handler.postDelayed(runnable, 1000);
                            } else {
                                tvGetcode.setEnabled(true);
                            }
                        } else if (dataRequest.getWhat() == what_CODECHECK) {     //校验验证码
                            JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                            if (jsonMap.getInt("code") == 0) {
//                                startActivity(new Intent(context, SetPayPasswordActivity.class).putExtra("type", 2));
                                startActivityForResult(new Intent(context, SetPayPasswordSecondActivity.class).putExtra("type", 3), 13);
                            } else {
                                ToastUtils.showToastLong(context, jsonMap.getStringNoNull("msg"));
                            }
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onResume() {
        alipayMap = null;
        wxMap = null;
        bankMap = null;
        //查询
        query();
        super.onResume();
    }

    /**
     * 支付宝
     */
    @OnClick(R.id.alipay_layout)
    public void alipay() {
        //点击绑定微信账户
        if (cardAlipayDesc.getText().toString().equals("未绑定")) {
            startActivityForResult(new Intent(context, BindingAlipayActivity.class), 11);
        } else {
            //选择支付宝支付
            selectWitDrawType(0);
        }
    }

    /**
     * 绑定微信
     */
    @OnClick(R.id.wechat_layout)
    public void wechat() {
        //点击绑定微信账户
        if (cardWechatDesc.getText().toString().equals("未绑定")) {
            mShareAPI.getPlatformInfo(context, SHARE_MEDIA.WEIXIN, authListener);
        } else {
            //选择微信支付
            selectWitDrawType(1);
        }
    }

    /**
     * 绑定银行卡布局
     */
    @OnClick(R.id.bank_layout)
    public void bank() {
        //点击银行卡微信账户
        if (cardBankDesc.getText().toString().equals("未绑定")) {
            startActivityForResult(new Intent(context, BindingBankCardActivity.class), 12);
        } else {
            //选择银行卡支付
            selectWitDrawType(2);
        }
    }

    /**
     * 提现声明
     */
    private AlertView alertView;

    @OnClick(R.id.tv_explain)
    public void explain() {

        alertView = new AlertView("提示", "自2017年1月1日起，充值金橙\n不能提现，只能用于支付劳务报酬\n或用于平台服务消费",
                null, null, new String[]{"确定"}, context, AlertView.Style.Alert,
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        alertView.dismiss();
                    }
                });
        alertView.setView(false, false, "").setCancelable(true);
        alertView.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 11:
                    selectWitDrawType(0);
                    break;
                case 12:
                    selectWitDrawType(2);
                    break;
                case 13:
                    payPwd = data.getStringExtra("payPwd");
                    withDrawal();
                    break;
            }
        }
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 选择支付方式并置顶，支付宝为0，微信为1，银行卡为2
     */
    //
    private int WITHDRAWTAG = 0;

    private void selectWitDrawType(int type) {
        sortingLayout.removeAllViews();
        switch (type) {
            case 0:
                sortingLayout.addView(zhifubaoLayout);
                sortingLayout.addView(wechatLayout);
                sortingLayout.addView(bankLayout);
                if (cardAlipayDesc.getText().toString().equals("未绑定")) {
                    imgAlipaySelect.setImageResource(R.mipmap.next);
                    WITHDRAWTAG = 0;
                } else {
                    WITHDRAWTAG = 1;
                    imgAlipaySelect.setImageResource(R.mipmap.radiobutton_checked);
                }
                if (cardWechatDesc.getText().toString().equals("未绑定")) {
                    imgWechatSelect.setImageResource(R.mipmap.next);
                } else {
                    imgWechatSelect.setImageResource(R.mipmap.radiobutton_nochecked);
                }
                if (cardBankDesc.getText().toString().equals("未绑定")) {
                    imgBankSelect.setImageResource(R.mipmap.next);
                } else {
                    imgBankSelect.setImageResource(R.mipmap.radiobutton_nochecked);
                }
                break;
            case 1:
                sortingLayout.addView(wechatLayout);
                sortingLayout.addView(zhifubaoLayout);
                sortingLayout.addView(bankLayout);
                if (cardWechatDesc.getText().toString().equals("未绑定")) {
                    imgWechatSelect.setImageResource(R.mipmap.next);
                    WITHDRAWTAG = 0;
                } else {
                    imgWechatSelect.setImageResource(R.mipmap.radiobutton_checked);
                    WITHDRAWTAG = 2;
                }
                if (cardAlipayDesc.getText().toString().equals("未绑定")) {
                    imgAlipaySelect.setImageResource(R.mipmap.next);
                } else {
                    imgAlipaySelect.setImageResource(R.mipmap.radiobutton_nochecked);
                }
                if (cardBankDesc.getText().toString().equals("未绑定")) {
                    imgBankSelect.setImageResource(R.mipmap.next);
                } else {
                    imgBankSelect.setImageResource(R.mipmap.radiobutton_nochecked);
                }
                break;
            case 2:
                sortingLayout.addView(bankLayout);
                sortingLayout.addView(zhifubaoLayout);
                sortingLayout.addView(wechatLayout);
                if (cardBankDesc.getText().toString().equals("未绑定")) {
                    imgBankSelect.setImageResource(R.mipmap.next);
                    WITHDRAWTAG = 0;
                } else {
                    WITHDRAWTAG = 6;
                    imgBankSelect.setImageResource(R.mipmap.radiobutton_checked);
                }
                if (cardAlipayDesc.getText().toString().equals("未绑定")) {
                    imgAlipaySelect.setImageResource(R.mipmap.next);
                } else {
                    imgAlipaySelect.setImageResource(R.mipmap.radiobutton_nochecked);
                }
                if (cardWechatDesc.getText().toString().equals("未绑定")) {
                    imgWechatSelect.setImageResource(R.mipmap.next);
                } else {
                    imgWechatSelect.setImageResource(R.mipmap.radiobutton_nochecked);
                }
                break;
        }
        JsonMap<String, Object> map = new JsonMap<>();
        map.put(SharedPreferencesUtils.
                getString(context, SharedPreferencesUtils.MOBILE), type);
        SharedPreferencesUtils.
                saveString(context, SharedPreferencesUtils.PayType, map.toJson());
    }

    /**
     * 解除微信绑定
     */
    private UMAuthListener delectauthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            ToastUtils.showToastLong(context, throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.showToastLong(context, "您取消微信授权失败");
        }
    };
}