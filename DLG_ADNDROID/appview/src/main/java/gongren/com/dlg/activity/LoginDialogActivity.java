package gongren.com.dlg.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.common.view.gridpasswordview.GridPasswordView;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import gongren.com.dlg.R;
import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.javabean.LoginUserInfo;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.ExampleUtil;
import gongren.com.dlg.utils.GsonUtils;
import gongren.com.dlg.utils.IntegerUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.utils.WorkbenchManager;
import gongren.com.dlg.view.LoginLoadingView;
import gongren.com.dlg.view.VisibleEditText;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 登录界面
 *
 */
public class LoginDialogActivity extends BaseActivity {

    @Bind(R.id.login_authcode_back)
    ImageView ivBack;
    @Bind(R.id.login_authcode_title)
    TextView tvTitle;
    @Bind(R.id.login_authcode_save)
    TextView tvSave;
    @Bind(R.id.login_authcode_inputfalse2)
    TextView tvInputfalse2;
    @Bind(R.id.login_authcode_sendwho)
    TextView tvSendwho;
    @Bind(R.id.login_authcode_time)
    Button tvTime;
    @Bind(R.id.login_authcode_pswView)
    GridPasswordView pswView;
    @Bind(R.id.login_authcode_agreexy)
    TextView agreeXyBtn;
    @Bind(R.id.login_authcode_xybtn)
    TextView yhxyBtn;
    //验证码登录
    @Bind(R.id.login_authcode_layout)
    LinearLayout layoutYanzhengma;
    //用户协议
    @Bind(R.id.login_authcode_regiestxy)
    LinearLayout layoutRegiestxy;


    @Bind(R.id.login_setpassword_password)
    VisibleEditText etPassword2;
    @Bind(R.id.login_setpassword_showpsd)
    CheckBox cbPassword2;
    //二次密码
    @Bind(R.id.login_setpassword_pwd2)
    RelativeLayout relativiePwd2;
    @Bind(R.id.login_setpassword_sure)
    TextView tvSure;
    //设置密码
    @Bind(R.id.login_setpassword_layout)
    LinearLayout layoutSetpassword;

    @Bind(R.id.login_password_finish)
    ImageView ivFinish;
    @Bind(R.id.login_password_yanzhengma)
    TextView tvYanzhengma;
    @Bind(R.id.login_password_agreement)
    TextView tvAgreement;
    @Bind(R.id.login_password_account)
    VisibleEditText etAccount;
    @Bind(R.id.login_password_password)
    VisibleEditText etPassword;
    @Bind(R.id.login_password_pwdshow)
    CheckBox cbPassword;
    @Bind(R.id.login_password_pwdlayout)
    RelativeLayout relativiePwd;
    @Bind(R.id.login_password_forgetpwd)
    TextView tvForgetpassword;
    @Bind(R.id.login_password_inputfalse)
    TextView tvInputfalse;
    @Bind(R.id.login_password_login)
    TextView tvLogin;
    //密码登录界面
    @Bind(R.id.login_password_layout)
    LinearLayout layoutPassword;
    @Bind(R.id.check_loading)
    LoginLoadingView check_loading;

    private Activity context;
    private int countDown = 60;       //倒计时数字
    private String mobile;
    private String password;
    private Map<String, Integer> booleanMap = new HashMap<>();    //1老2新，给手机号一个标示，用于后面的跳转判断
    private long codeTime;   //时间戳
    private String ZQCode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置activiy点击空白区域，不会消失的属性
        MyApplication.isLogInPage = true;
        setFinishOnTouchOutside(true);
        setContentView(R.layout.activity_login_dialog);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        context = this;
        //第三方的输入框由密码模式变成可以看到的
        pswView.togglePasswordVisibility();
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        tvLogin.setText("下一步");
        tvLogin.setEnabled(false);
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString()))
                    return;
                //输入不为空
                if (StringUtils.isPhoneNumber(s.toString())) {
                    tvLogin.setEnabled(true);
                } else {
                    if (tvInputfalse != null && tvInputfalse.isShown())
                        tvInputfalse.setVisibility(View.GONE);
                    tvLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //如果密码错误进行改变时候错误提示的信息消失
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(etPassword.getText().toString()) && !TextUtils.isEmpty(password))
                    if (etPassword.getText().length() != password.length()) {
                        tvInputfalse.setVisibility(View.GONE);
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //如果密码错误进行改变时候错误提示的信息消失
        etPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(etPassword2.getText().toString())) {
                    tvSure.setEnabled(true);
                } else {
                    tvSure.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * 密码显示与隐藏
         *
         */
        cbPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //设置EditText文本为隐藏的
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    //设置EditText文本为可见的
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        cbPassword2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //设置EditText文本为隐藏的
                    etPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    //设置EditText文本为可见的
                    etPassword2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        //账号的焦点在点击下一步之后出现在密码上。当账号再次获取焦点时，密码输入框消失
        etAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    relativiePwd.setVisibility(View.GONE);
                    tvForgetpassword.setVisibility(View.GONE);
                    tvYanzhengma.setVisibility(View.GONE);
                    tvAgreement.setVisibility(View.VISIBLE);
                    tvLogin.setText("下一步");
                } else {
                    relativiePwd.setVisibility(View.VISIBLE);
                    tvForgetpassword.setVisibility(View.VISIBLE);
                    tvYanzhengma.setVisibility(View.VISIBLE);
                    tvAgreement.setVisibility(View.GONE);
                }
            }
        });


        //输入框输入完成后，如果输入完成之后进行网络请求，成功后进行密码设置
        pswView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (!TextUtils.isEmpty(psw)) {
                    if (psw.length() == 4) {
                        //校验验证码网络请求
                        if (!StringUtils.isVerifyCode(psw)) {
                            ToastUtils.showToastShort(context, "请输入正确的验证码格式");
                        } else {
                            authCode(psw);
                        }
                    }
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
    }


    /*****
     * 验证码
     * @param psw
     */
    private String registerCode = "";
    private void authCode(String psw){
        //进入设置密码。
        if (booleanMap.get(mobile) == 1) {
            if (forgetAndRegisterFlag) {
                //忘记密码
                ZQCode = psw;
               /* layoutYanzhengma.setVisibility(View.GONE);
                layoutPassword.setVisibility(View.GONE);
                layoutSetpassword.setVisibility(View.VISIBLE);*/
                Map<String, Object> map = BaseMapUtils.getMap(context);
                map.put("phone", mobile);
                map.put("verifyCode", psw);
                DataUtils.loadData(context, GetDataConfing.REGISTER_CHECKVERIFYCODE, map, IntegerUtils.API_CHECK_CODE, responseDataCallback);
            } else {
                //如果是老用户直接验证码登录
                Map<String, Object> map = BaseMapUtils.getMap(context);
                map.put("type", String.valueOf(1));
                map.put("principal", mobile);
                map.put("sms", "code");
                map.put("credentials", psw);
                map.put("rememberMe",true);
                DataUtils.loadData(context, GetDataConfing.SYSTEM_LOGIN, map, IntegerUtils.API_PSD_LOGIN, responseDataCallback);
            }
        } else {
            registerCode = psw;
            showLayoutSetPasswd();
        }
    }


    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            tvLogin.setEnabled(true);
            if (tvTitle != null) {
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    JsonMap<String, Object> jsonMap = null;
                    if (!TextUtils.isEmpty(json)) {
                        switch (dataRequest.getWhat()){
                            case IntegerUtils.API_TAG_QUERY:
                                check_loading.setVisibility(View.GONE);
                                //进行网络请求,判断是否是新用户登录
                                jsonMap = JsonParseHelper.getJsonMap(json);
                                switch (jsonMap.getString("code")){

                                    case "0":
                                        //code为0，账号存在，直接密码账号登陆
                                        tvSave.setVisibility(View.VISIBLE);
                                        layoutRegiestxy.setVisibility(View.INVISIBLE);
                                        relativiePwd.setVisibility(View.VISIBLE);
                                        tvYanzhengma.setVisibility(View.VISIBLE);
                                        tvAgreement.setVisibility(View.GONE);
                                        tvForgetpassword.setVisibility(View.VISIBLE);
                                        etPassword.setText("");
                                        etPassword.setHint("请输入密码");
                                        etPassword.requestFocus();
                                        tvLogin.setText("登录");
                                        booleanMap.put(mobile, 1);
                                        break;

                                    default:
                                        tvSave.setVisibility(View.GONE);
                                        layoutRegiestxy.setVisibility(View.VISIBLE);
                                        //账号不存在，验证码登陆界面
                                        showLayoutYZM();
                                        booleanMap.put(mobile, 2);
                                        break;
                                }
                                break;

                            case IntegerUtils.API_PSD_LOGIN:
                                check_loading.setVisibility(View.GONE);
                                //账号密码登录
                                resultLogin(json);
                                break;

                            case IntegerUtils.API_GET_VERCODE:
                                //获取验证码
                                jsonMap = JsonParseHelper.getJsonMap(json);
                                if (jsonMap.getInt("code") != 0) {
                                    tvTime.setEnabled(true);
                                    ToastUtils.showToastLong(context, jsonMap.getStringNoNull("msg"));
                                }else {
                                    codeTime = System.currentTimeMillis();
                                    countDown = 59;
                                    handler.postDelayed(runnable, 1000);
                                }
                                break;

                            case IntegerUtils.API_CHECK_CODE:
                                //获取手机号验证码
                                jsonMap  = JsonParseHelper.getJsonMap(json);
                                if (jsonMap.getInt("code") == 0) {
                                    layoutYanzhengma.setVisibility(View.GONE);
                                    layoutPassword.setVisibility(View.GONE);
                                    layoutSetpassword.setVisibility(View.VISIBLE);
                                    //设置密码确认
                                    tvSure.setEnabled(false);
                                } else {
                                    ToastUtils.showToastLong(context, "验证码错误请重新输入");
                                }
                                break;

                            case IntegerUtils.API_USER_REGISTER:
                                try {
                                    JSONObject object = new JSONObject(json);
                                    if (object.optInt("code") == 0) {
                                        ToastUtils.showToastShort(LoginDialogActivity.this,"注册成功");
                                        //注册成功后进行登录
                                        Map<String, Object> map = BaseMapUtils.getMap(context);
                                        map.put("type", String.valueOf(1));
                                        map.put("principal", mobile);
                                        map.put("rememberMe","true");
                                        map.put("credentials", etPassword2.getText().toString());
                                        map.put("rememberMe",true);
                                        DataUtils.loadData(context, GetDataConfing.SYSTEM_LOGIN, map, IntegerUtils.API_PSD_LOGIN, responseDataCallback);
                                    } else {
                                        ToastUtils.showToastLong(context, object.optString("msg"));
                                        layoutYanzhengma.setVisibility(View.VISIBLE);
                                        pswView.forceInputViewGetFocus();
                                        layoutPassword.setVisibility(View.GONE);
                                        layoutSetpassword.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case IntegerUtils.API_MODIFY_PWD:
                                //忘记密码
                                jsonMap = JsonParseHelper.getJsonMap(json);
                                if (jsonMap.getInt("code") == 0) {
                                    //注册成功后进行登录
                                    Map<String, Object> map = BaseMapUtils.getMap(context);
                                    map.put("type", String.valueOf(1));
                                    map.put("principal", mobile);
                                    map.put("credentials", etPassword2.getText().toString());
                                    map.put("rememberMe",true);
                                    DataUtils.loadData(context, GetDataConfing.SYSTEM_LOGIN, map, IntegerUtils.API_PSD_LOGIN, responseDataCallback);
                                } else {
                                    layoutYanzhengma.setVisibility(View.VISIBLE);
                                    pswView.forceInputViewGetFocus();
                                    layoutPassword.setVisibility(View.GONE);
                                    layoutSetpassword.setVisibility(View.GONE);
                                }
                                break;
                        }

                    }
                }
            }
        }
    };

    /**
     * 账号密码登录--成功
     *
     * @param json
     */
    private void resultLogin(String json) {
        Log.e("DLG", "cookieStore==" + MyApplication.getCookieStore());
        CookieManager cookieManager = new CookieManager(MyApplication.getCookieStore(), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        SharedPreferencesUtils.clear(this);
//        LoginUserInfoJson loginUserInfoJson = GsonUtils.jsonToBean(json,LoginUserInfoJson.class);
        JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);

        if(jsonMap.getStringNoNull("code").equals("0")){
            String data = jsonMap.getString("data");
            LoginUserInfo loginUserInfo = GsonUtils.jsonToBean(data, LoginUserInfo.class);
            if(loginUserInfo!=null) {

                String id = String.valueOf(loginUserInfo.userId);
                MobclickAgent.onProfileSignIn(id);
                //设置别名
                setAlias(id);
                boolean saveIDOk = SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.USERID, id);
                SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.FILELOGO, loginUserInfo.logo);
                SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.MOBILE, mobile);
                SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.TOKEN, "");
//                SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.UserName, loginUserInfo.);
                SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.PASSWORD, etPassword.getText().toString().trim());
                SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.RENZHENG_STATUS, loginUserInfo.auditStatus);
                SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.STATUS, loginUserInfo.auditStatus);
                SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.EMPLOYERSNAME, loginUserInfo.name);
                SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.COMPANYADDRESS, loginUserInfo.location);
                SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.UserRole, loginUserInfo.role);
                SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.REAL_NAME, loginUserInfo.name);
                SharedPreferencesUtils.saveString(context,SharedPreferencesUtils.CERTIFICATENUMBER,loginUserInfo.certificateNumber);
                WorkbenchManager.getInstance(context).addUserLogin();
                if (saveIDOk) {
                    setResult(RESULT_OK);
                    finish();
                }
            } else {
                //密码错误
                tvInputfalse.setVisibility(View.VISIBLE);
            }
        }else {
            ToastUtils.showToastShort(context, jsonMap.getStringNoNull("msg"));
        }

        DialogUtils.closeInputMethod(context);
    }

    /**
     * 切换到验证码界面
     */
    private void showLayoutYZM() {
        //密码登录
        layoutPassword.setVisibility(View.GONE);
        //验证码信息
        layoutYanzhengma.setVisibility(View.VISIBLE);
        pswView.forceInputViewGetFocus();
        //设置密码
        layoutSetpassword.setVisibility(View.GONE);
        //发送验证码按钮
        if(StringUtils.isPhoneNumber(mobile)){
            StringBuffer sb=new StringBuffer(mobile);
            sb.insert(3, " ");
            sb.insert(8, " ");
            tvSendwho.setText("验证码发送  " + sb.toString());
        }else
            tvSendwho.setText("验证码发送  " + mobile);
        sendYZMCode();
    }

    /**
     * 切换到设置密码界面
     */
    private void showLayoutSetPasswd() {

        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("phone", mobile);
        map.put("verifyCode", registerCode);
        DataUtils.loadData(context, GetDataConfing.REGISTER_CHECKVERIFYCODE, map, IntegerUtils.API_CHECK_CODE, responseDataCallback);
    }

    /**
     * 发送验证码
     * liukui  2017/04/18
     */
    private void sendYZMCode() {
        if (!TextUtils.isEmpty(mobile)) {
            if (StringUtils.isPhoneNumber(mobile)) {
                //请求验证码
                tvTime.setEnabled(false);
                Map<String, Object> map = BaseMapUtils.getMap(context);
                map.put("phone", etAccount.getText().toString().trim());
                DataUtils.loadData(context, GetDataConfing.SYSTEM_NOTECODE,map, IntegerUtils.API_GET_VERCODE, responseDataCallback);
            }
        }
    }

    /**
     * 忘记密码还是注册的标志
     */
    private boolean forgetAndRegisterFlag = false;

    @OnClick({R.id.login_password_agreement, R.id.login_password_yanzhengma, R.id.login_password_forgetpwd,
            R.id.login_authcode_back, R.id.login_authcode_save, R.id.login_authcode_time, R.id.login_password_finish,
            R.id.login_setpassword_sure,R.id.login_authcode_xybtn,R.id.login_password_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_password_finish:
                //返回
                finish();
                DialogUtils.closeInputMethod(context);
                break;

            case R.id.login_authcode_xybtn:
            case R.id.login_password_agreement:
                //用户协议
                DialogUtils.closeInputMethod(context);
                Intent agreementIntent = new Intent(context, WebUtilsActivity.class);
                agreementIntent.putExtra("functionTitle","用户协议");
                agreementIntent.putExtra("contentUrl",GetDataConfing.BASEURL_H5);
                agreementIntent.putExtra("type","3");
                startActivity(agreementIntent);
                break;

            case R.id.login_password_yanzhengma:
                //验证码登录按钮
                DialogUtils.closeInputMethod(context);
                showLayoutYZM();
                break;

            case R.id.login_password_forgetpwd:
                //忘记密码按钮
                DialogUtils.closeInputMethod(context);
                showLayoutYZM();        //忘记密码
                forgetAndRegisterFlag = true;
                break;

            case R.id.login_authcode_back:
                //注册界面返回
                layoutPassword.setVisibility(View.VISIBLE);
                tvInputfalse.setVisibility(View.GONE);
                layoutYanzhengma.setVisibility(View.GONE);
                layoutSetpassword.setVisibility(View.GONE);

                relativiePwd.setVisibility(View.GONE);
                tvForgetpassword.setVisibility(View.GONE);
                tvYanzhengma.setVisibility(View.GONE);
                tvLogin.setText("下一步");
                countDown = 0;
                DialogUtils.closeInputMethod(context);
                break;

            case R.id.login_authcode_save:
                //密码登录按钮
                layoutYanzhengma.setVisibility(View.GONE);
                layoutPassword.setVisibility(View.VISIBLE);
                layoutSetpassword.setVisibility(View.GONE);

                tvSave.setVisibility(View.VISIBLE);
                layoutRegiestxy.setVisibility(View.INVISIBLE);
                relativiePwd.setVisibility(View.VISIBLE);
                tvYanzhengma.setVisibility(View.VISIBLE);
                tvAgreement.setVisibility(View.GONE);
                tvForgetpassword.setVisibility(View.VISIBLE);
                etPassword.requestFocus();
                tvLogin.setText("登录");
                DialogUtils.closeInputMethod(context);
                break;

            case R.id.login_authcode_time:
                //点击发送验证码，进行时间的倒计时
                sendYZMCode();
                DialogUtils.closeInputMethod(context);
                break;

            case R.id.login_setpassword_sure:
                //点击确认之后，设置密码
                registerOrForgetPSW();
                DialogUtils.closeInputMethod(context);
                break;

            case R.id.login_password_login:
                //当点击是下一步的时候
                tvLogin.setEnabled(false);
                if (tvLogin.getText().toString().equals("下一步")) {
                    //进行网络请求,判断是否是新用户登录（下面的内容写在回调中）
                    mobile = etAccount.getText().toString().trim();
                    if (!TextUtils.isEmpty(mobile)) {
                        if (StringUtils.isPhoneNumber(mobile)) {
                            Map<String, Object> map = BaseMapUtils.getMap(context);
                            map.put("phone", mobile);
                            map.put("type", "1");
                            check_loading.setVisibility(View.VISIBLE);
                            DataUtils.loadData(context, GetDataConfing.SYSTEM_CHECKMOBILE, map, IntegerUtils.API_TAG_QUERY, responseDataCallback);
                        } else {
                            ToastUtils.showToastShort(context, "请输入正确的手机号");
                        }
                    }
                } else {
                    //当按钮是登录状态的时候
                    if (TextUtils.isEmpty(etPassword.getText().toString())) {
                        ToastUtils.showToastLong(context, "请输入您的密码");
                        tvLogin.setEnabled(true);
                    } else {
                        //网络请求（如果正确，跳转，如果错误，显示红色的错误提醒）
                        password = etPassword.getText().toString();
                        check_loading.setVisibility(View.VISIBLE);
                        Map<String, Object> map = BaseMapUtils.getMap(context);
                        map.put("type", String.valueOf(1));
                        map.put("principal", mobile);
                        map.put("credentials", password);
                        map.put("format", "json");
                        map.put("rememberMe",true);
                        DataUtils.loadData(context, GetDataConfing.SYSTEM_LOGIN, map, IntegerUtils.API_PSD_LOGIN, responseDataCallback);
                    }
                }

                DialogUtils.closeInputMethod(context);
                break;
        }
    }

    /**
     * 注册和忘记密码
     */
    private void registerOrForgetPSW() {
        String string = etPassword2.getText().toString();
        if (TextUtils.isEmpty(string)) {
            ToastUtils.showToastLong(context, "请输入密码");
            return;
        }
        if (!StringUtils.isPassWord(string)) {
            ToastUtils.showToastLong(context, "请输入6~14位密码");
            return;
        }
        if (forgetAndRegisterFlag) {
            //忘记密码
            String psw = etPassword2.getText().toString();
            if (TextUtils.isEmpty(mobile)) {
                ToastUtils.showToastShort(context, "请输入手机号码");
                return;
            }
            if (!StringUtils.isPhoneNumber(mobile)) {
                ToastUtils.showToastShort(context, "请输入格式正确的手机号码");
                return;
            }
            if (TextUtils.isEmpty(psw)) {
                ToastUtils.showToastShort(context, "请输入密码");
                return;
            }
            if (!StringUtils.isPassWord(psw)) {
                ToastUtils.showToastShort(context, "请输入6-14位的密码");
                return;
            }
            if (codeTime == 0) {
                ToastUtils.showToastShort(context, "请获取验证码");
                return;
            }
            if (TextUtils.isEmpty(ZQCode)) {
                ToastUtils.showToastShort(context, "请输入验证码");
                return;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("phone", mobile);
            map.put("password", psw.trim());
            map.put("code", ZQCode);
            map.put("type", String.valueOf(1));
            map.put("isLogin","1");
            map.put("format","json");
            DataUtils.loadData(context, GetDataConfing.SYSTEM_FORGETPWD, map, IntegerUtils.API_MODIFY_PWD , responseDataCallback);
        } else {
            //注册
            getActivePermissions();
        }
    }


    /******
     * 用户注册
     * @param DEVICE_ID
     */
    private void userRegister(String DEVICE_ID){

        String channel = AnalyticsConfig.getChannel(this);//获取渠道名

        Map<String, Object> map = new HashMap<>();
        map.put("phone", mobile);//调试
        map.put("password",  etPassword2.getText().toString());
        map.put("userType", String.valueOf(1));//type:用户类型，1、个人，2、企业
        map.put("vaildCode",registerCode);//验证码
        map.put("username","");//用户名
        map.put("imei",DEVICE_ID);//手机设备识别码
        map.put("email","");//邮箱
        map.put("weChat","");//微信
        map.put("oicq","");//QQ
        map.put("personalizedSignature","");//个性签名
        map.put("location","");//所在地
        map.put("parentUserId","");
        map.put("isLogin",0); //0(默认为否)注册成功后是否默认登录0:false, 1:true
        map.put("source","register");// 来源  register(自己注册)/generalize(活动推广)/recommend(用户推荐)  (必填)
        map.put("activity","");//活动
        map.put("channel",channel);//渠道
        map.put("ws","");//关键字
        map.put("client","ANDROID");//客户端类型 如：ANDROID、IOS、H5、PC  (必填)
        map.put("","");
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            map.put("appVersion",packageInfo.versionName+"");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        DataUtils.loadData(context, GetDataConfing.SYSTEM_REGISTER, map, IntegerUtils.API_USER_REGISTER, responseDataCallback);

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
                if (countDown < 1) {
                    tvTime.setText("再次发送");
                    tvTime.setEnabled(true);
                    countDown = 0;
                } else {
                    handler.postDelayed(this, 1000);
                    tvTime.setText(countDown-- + "s后重发");
                    tvTime.setEnabled(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 设置别名
     */
    private static final String TAG = "JPush";

    private void setAlias(String alias) {

        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(context, "alias不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(context, "格式不对", Toast.LENGTH_SHORT).show();
            return;
        }
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.e("wangdakuan", "Set alias in handler."+msg.obj);
                    //(String) msg.obj
                    Set<String> strings = new HashSet<>();
//                    strings.add(msg.obj.toString());
//                    JPushInterface.setAlias(getApplicationContext(),msg.obj.toString(),new TagAliasCallback(){
//                        @Override
//                        public void gotResult(int i, String s, Set<String> set) {
//                            Log.e("wangdakuan", i+" |="+s);
//                        }
//                    });
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private static final int MSG_SET_ALIAS = 1001;
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.isLogInPage = false;
    }


    /******
     * 动态获取权限
     * liukui 2017/04/24
     */
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    public void getActivePermissions(){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_CALL_PHONE);
                return;
            }else{
                //上面已经写好的拨号方法
                TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                String DEVICE_ID = tm.getDeviceId()==null?"":tm.getDeviceId();
                userRegister(DEVICE_ID);
            }
        } else {
            //上面已经写好的拨号方法
            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String DEVICE_ID = tm.getDeviceId()==null?"":tm.getDeviceId();
            userRegister(DEVICE_ID);
        }
    }

    /******
     * 处理动态获取权限结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    String DEVICE_ID = tm.getDeviceId()==null?"":tm.getDeviceId();
                    userRegister(DEVICE_ID);
                } else {
                    // Permission Denied
                    ToastUtils.showToastShort(context,"权限获取失败");
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
