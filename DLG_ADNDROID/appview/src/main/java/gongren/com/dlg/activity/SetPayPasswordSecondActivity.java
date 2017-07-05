package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.utils.ActivityManager;
import gongren.com.dlg.utils.Base64Utils;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.MD5Utils;
import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.view.PasswordInputView;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 确认支付密码
 * Created by lin.li on 2017/2/11.
 */
public class SetPayPasswordSecondActivity extends BaseActivity {

    @Bind(R.id.passwordInputView)
    PasswordInputView mPasswordInputView;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    //判断来源
    private int type = 0;
    private String identifyingCode;
    private String pwd;
    private Dialog dialog;
    private static final int TAG_RestPassWord = 1;
    private static final int TAG_AddPassWord = 2;
    private static final int TAG_YanZhengPassWord = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpaypassword);
        ButterKnife.bind(this);
        ActivityManager.getActivityManager().addActivity(this);
        mPasswordInputView.setFocusable(true);
        mPasswordInputView.setFocusableInTouchMode(true);
        mPasswordInputView.requestFocus();
        getIntentData();
        initView();
    }

    private void getIntentData() {
        pwd = getIntent().getStringExtra("pwd");
        type = getIntent().getIntExtra("type", 0);
        try {
            identifyingCode = getIntent().getStringExtra("identifyingCode");
        }catch (Exception e){
        }

    }

    private void initView() {
        tvTitle.setText("确认支付密码");
        mPasswordInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    if (TextUtils.isEmpty(pwd)) {
                        DialogUtils.closeInputMethod(context);

//                        Map<String, Object> map = BaseMapUtils.getMap(context);
//                        map.put("payPassword", Base64Utils.getBase64(MD5Utils.MD5Encode(s.toString()) + "/."));
//                        DataUtils.loadData(context, GetDataConfing.validate, map, TAG_YanZhengPassWord, responseDataCallback);

                        setResult(RESULT_OK, new Intent().putExtra("payPwd",
                                Base64Utils.getBase64(MD5Utils.MD5Encode(mPasswordInputView.getText().toString()) + "/.")));
                        finish();
                        return;
                    }
                    if (pwd.equals(s.toString())) {
                        DialogUtils.closeInputMethod(context);
                        dialog = DialogUtils.showLoadingDialog(context);
                        Map<String, Object> map = BaseMapUtils.getMap(context);
                        map.put("password",  Base64Utils.getBase64(MD5Utils.MD5Encode(pwd) + "/."));
                        //                        map.put("password", pwd);
                        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));

                        if (type == 1) {        //设置密码
                            DataUtils.loadData(context, GetDataConfing.addPayPassword, map, TAG_AddPassWord, responseDataCallback);
                        } else if (type == 2) {    //重置密码
                            map.put("code", identifyingCode);
                            DataUtils.loadData(context, GetDataConfing.updatePayPwd, map, TAG_RestPassWord, responseDataCallback);
                        }
                    } else {
                        mPasswordInputView.getText().clear();
                        mPasswordInputView.requestFocus();
                        Toast.makeText(context, "两次输入不一致!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (tvTitle != null) {
                if (dialog != null && dialog.isShowing())
                    dialog.cancel();
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                    Log.e("pay","request"+dataRequest.getWhat()+"code="+jsonMap.getInt("code"));
                    if (!TextUtils.isEmpty(json)) {
                        if (dataRequest.getWhat() == TAG_RestPassWord) {      //重置密码
                            //JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
//                            ToastUtils.showToastShort(context, "caowei:"+jsonMap.getStringNoNull("msg"));
                            if (jsonMap.getInt("code") == 0) {
                                ActivityManager.getActivityManager().finishAllActivity();
                            }
                        } else if (dataRequest.getWhat() == TAG_AddPassWord) {       //设置密码
                            //JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                            if (jsonMap.getInt("code") == 0) {
                                SharedPreferencesUtils.saveBoolean(context, SharedPreferencesUtils.havePayPwd, true);
                                startActivity(new Intent(context, BundPaymentModeActivity.class));
                                ActivityManager.getActivityManager().finishAllActivity();
                            } else {
                                ToastUtils.showToastShort(context, jsonMap.getStringNoNull("msg"));
                            }
                        } else if (dataRequest.getWhat() == TAG_YanZhengPassWord) {     //验证密码
                            //JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                            if (jsonMap.getInt("code") == 0) {
                                setResult(RESULT_OK, new Intent().putExtra("payPwd",
                                        Base64Utils.getBase64(MD5Utils.MD5Encode(mPasswordInputView.getText().toString()) + "/.")));
                                finish();
                            } else {
                                ToastUtils.showToastShort(context, jsonMap.getStringNoNull("msg"));
                            }
                        }
                    }
                }
            }
        }
    };

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getActivityManager().finishActivity(this);
    }
}
