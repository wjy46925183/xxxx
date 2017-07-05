package gongren.com.dlg.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.ReceiveReturnIdJson;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.GsonUtils;
import gongren.com.dlg.utils.LogUtils;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

public class BindingBankCardActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.bank_username)
    TextView bankUsername;
    @Bind(R.id.bank_account)
    EditText bankAccount;
    @Bind(R.id.bank_mobile)
    EditText bankMobile;
    @Bind(R.id.bank_qrcode)
    EditText bankQrcode;
    @Bind(R.id.btn_getCode)
    TextView btnGetCode;
    @Bind(R.id.btn_Banding)
    Button btnBanding;

    private MyCount myCount = null;
    private Dialog dialog;
    private String bindId;
    // 要申请的权限
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_bank_card);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        bankUsername.setText(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.REAL_NAME));
        tvTitle.setText("绑定银行卡");
    }

    @OnClick({R.id.iv_back, R.id.btn_getCode, R.id.btn_Banding,R.id.recoginze_card})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_getCode:
                Request_GetQrCode();
                break;
            case R.id.btn_Banding:
                Request_BindingBankCards();
                break;
            case R.id.recoginze_card:
                //打开摄像头
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        go2CustomCamera();
                    } else {
                        showDialogTipUserRequestPermission();
                    }
                } else {
                    go2CustomCamera();
                }
                break;
        }
    }

    private void go2CustomCamera() {
        String manufature = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (manufature.toUpperCase().equals("VIVO") && model.toUpperCase().equals("VIVO X6A")) {
            showTip("暂不支持该型号手机智能识别银行卡，请手动输入银行卡号");
            return;
        }
        if (Build.VERSION.SDK_INT <= 19) {
            showTip("该版本手机系统暂不支持智能识别银行卡，请手动输入银行卡号");
            return;
        }
        startActivityForResult(new Intent(BindingBankCardActivity.this, BindingBankCardCameraActivity.class), 1);
    }
    private void showTip(String msg) {

        new AlertDialog.Builder(this)
                .setTitle("提 示")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {

        new AlertDialog.Builder(this)
                .setTitle("照片权限")
                .setMessage("是否允许用户使用相机")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(BindingBankCardActivity.this, permissions, 10);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }
    // 用户权限申请的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 10) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        Toast.makeText(this, "请至设置页设置权限", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    go2CustomCamera();
                }
            }
        }
    }

    /**
     * 绑定银行卡接口
     */
    private long timestamp;

    private void Request_BindingBankCards() {
        String username = bankUsername.getText().toString();
        String account = bankAccount.getText().toString();
        String mobile = bankMobile.getText().toString();
        String qrcode = bankQrcode.getText().toString();
        //TODO 打开请填写持卡人姓名验证
//        if (TextUtils.isEmpty(username)) {
//            ToastUtils.showToastShort(context, "请填写持卡人姓名");
//            return;
//        }
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showToastShort(context, "请填写银行账号");
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showToastShort(context, "请填写手机号");
            return;
        }
        if (!StringUtils.isPhoneNumber(mobile)) {
            ToastUtils.showToastShort(context, "请填写正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(qrcode)) {
            ToastUtils.showToastShort(context, "请填写验证码");
            return;
        }
        if (timestamp == 0) {
            ToastUtils.showToastShort(context, "请获取验证码");
            return;
        }
        dialog = DialogUtils.showLoadingDialog(context);
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("payAccount", account);
        map.put("reservedPhone", mobile);
        map.put("payType", "3");
        map.put("vaildCode", qrcode);
        map.put("bindId", bindId);
        DataUtils.loadData(context, GetDataConfing.payBindRest,
                map, TAG_REQUEST, responseDataCallback);
    }

    /**
     * 请求验证码接口
     */
    private void Request_GetQrCode() {
        String account = bankAccount.getText().toString();
        String mobile = bankMobile.getText().toString();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showToastShort(context, "请填写银行账号");
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showToastShort(context, "请填写支行预留手机号");
            btnGetCode.setEnabled(true);
            return;
        }
        if (!StringUtils.isPhoneNumber(mobile)) {
            ToastUtils.showToastShort(context, "请填写正确的手机号");
            btnGetCode.setEnabled(true);
            return;
        }

        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("phone", mobile);
        map.put("account", account);
        startTimer();
        DataUtils.loadData(context, GetDataConfing.CODEURL,
                map, TAG_REQUESTCODE, responseDataCallback);

    }

    //校验手机号
    private void checkMoble() {
        String mobile = bankMobile.getText().toString();
        String qrcode = bankQrcode.getText().toString();
        if (TextUtils.isEmpty(qrcode)) {
            ToastUtils.showToastShort(context, "请填写验证码");
            return;
        }
        if (timestamp == 0) {
            ToastUtils.showToastShort(context, "请获取验证码");
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showToastShort(context, "请填写手机号");
            return;
        }
        if (!StringUtils.isPhoneNumber(mobile)) {
            ToastUtils.showToastShort(context, "请填写正确的手机号");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("mobileCode", qrcode);
        map.put("timestamp", timestamp + "");
        DataUtils.loadData(context, GetDataConfing.validateMobileAndCode, map,
                what_CODECHECK, responseDataCallback);

    }

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
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    if (!TextUtils.isEmpty(json)) {
                        if (dataRequest.getWhat() == TAG_REQUEST) {
                            LogUtils.logD("zp", json);
                            JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                            if (jsonMap.getInt("code") == 0) {
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                ToastUtils.showToastLong(context, jsonMap.getStringNoNull("msg"));
                            }
                        } else if (dataRequest.getWhat() == TAG_REQUESTCODE) {
                            ReceiveReturnIdJson json1 = GsonUtils.jsonToBean(json, ReceiveReturnIdJson.class);
                            if (json1.data != null && json1.data.size() > 0) {
                                bindId = json1.data.get(0);
                            }
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null!=data){
            if (resultCode==RESULT_OK){
                String cardnumber=data.getStringExtra("number");
                bankAccount.setText(cardnumber);

            }
        }

    }

    private final int TAG_REQUEST = 10;
    private final int TAG_REQUESTCODE = 11;
    private final int what_CODECHECK = 4;     //验证码校验

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
            btnGetCode.setText(millisUntilFinished / 1000 + "秒后重发");
            btnGetCode.setEnabled(false);
        }

        @Override
        public void onFinish() {
            // 在这里进行设置解决时间停留的问题
            btnGetCode.setText("重新获取");
            btnGetCode.setEnabled(true);
        }
    }
}
