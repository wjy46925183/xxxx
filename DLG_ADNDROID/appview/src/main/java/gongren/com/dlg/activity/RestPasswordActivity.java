package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.utils.ActivityManager;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 重置密码
 */
public class RestPasswordActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_phoneNum)
    TextView tvPhoneNum;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.btn_code)
    TextView btnCode;

    private static final int REQUEST_CODE = 0;      //获取验证码的请求
    private int i = 60;       //倒计时数字
    private Dialog dialog;
    private String mobile;   //用户手机号
    private long codeTime;   //时间戳
    private final int what_CODECHECK = 4;     //验证码校验
    private String identifyingCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pay_password);
        ButterKnife.bind(this);
        ActivityManager.getActivityManager().addActivity(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("重置密码");
        mobile = SharedPreferencesUtils.getString(context, SharedPreferencesUtils.MOBILE);
        if (!TextUtils.isEmpty(mobile) && mobile.length() == 11)
            tvPhoneNum.setText(mobile.substring(0, 3) + "****" + mobile.substring(7));
        getCode();
    }

    /**
     * 获取验证码的请求
     */
    private void getCode() {
        dialog = DialogUtils.showLoadingDialog(context);
        //获取发送验证码的时间戳
        codeTime = System.currentTimeMillis();
        i = 60;
        btnCode.setEnabled(false);
        Map<String, Object> map = BaseMapUtils.getMap(context);

        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("phone",SharedPreferencesUtils.getString(context, SharedPreferencesUtils.MOBILE));
        DataUtils.loadData(context, GetDataConfing.SYSTEM_NOTECODE , map, REQUEST_CODE, responseDataCallback);
    }

    @OnClick({R.id.iv_back, R.id.btn_code, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_code:     //获取验证码
                getCode();
                break;
            case R.id.btn_next:     //下一步
                checkCode();
                break;
        }
    }

    /**
     * 校验验证码的网络请求
     */
    private void checkCode() {
        identifyingCode = etCode.getText().toString().trim();
        if (!StringUtils.isVerifyCode(identifyingCode)) {
            ToastUtils.showToastShort(context, "请输入正确的验证码格式");
        } else {
//            startActivity(new Intent(context, SetPayPasswordActivity.class).putExtra("type", 2).putExtra("identifyingCode", identifyingCode));
            //验证码校验
            Map<String, Object> map = BaseMapUtils.getMap(context);
            map.put("phone", mobile);
            map.put("verifyCode", etCode.getText().toString());
            map.put("format","format");
            DataUtils.loadData(context, GetDataConfing.REGISTER_CHECKVERIFYCODE, map, what_CODECHECK, responseDataCallback);

        }

//        startActivity(new Intent(context, SetPayPasswordActivity.class).putExtra("type", 2));
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
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
//                            ToastUtils.showToastShort(context, jsonMap.getStringNoNull("msg"));
                            if (jsonMap.getInt("code") == 0) {
                                handler.postDelayed(runnable, 1000);
                                ToastUtils.showToastLong(context,"验证码发送成功");
                            } else {
                                btnCode.setEnabled(true);
                            }
                        } else if (dataRequest.getWhat() == what_CODECHECK) {     //校验验证码
                            JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                            if (jsonMap.getInt("code") == 0) {
                                startActivity(new Intent(context, SetPayPasswordActivity.class).putExtra("type", 2).putExtra("identifyingCode", identifyingCode));
                            } else {
                                ToastUtils.showToastLong(context, jsonMap.getStringNoNull("msg"));
                            }
                        }
                    }
                }
            }
        }
    };

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
                    btnCode.setText("再次发送");
                    btnCode.setEnabled(true);
                    i = 0;
                } else {
                    handler.postDelayed(this, 1000);
                    btnCode.setText(i-- + "s后重发");
                    btnCode.setEnabled(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
