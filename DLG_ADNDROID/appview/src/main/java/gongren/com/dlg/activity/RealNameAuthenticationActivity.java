package gongren.com.dlg.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.alibaba.cloudapi.HttpUtil;
import gongren.com.dlg.alibaba.cloudapi.constant.Constants;
import gongren.com.dlg.javabean.RefreshEvent;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 实名认证首页
 * Created by lin.li on 2017/2/11.
 */
public class RealNameAuthenticationActivity extends BaseActivity {

    int x;//宽
    int y;//长
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.img_status)
    ImageView imgStatus;
    @Bind(R.id.rel_img_bg)
    RelativeLayout relImgBg;
    @Bind(R.id.ed_name)
    EditText edName;
    @Bind(R.id.ed_cardNo)
    EditText edCardNo;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    @Bind(R.id.ll_card)
    LinearLayout ll_card;
    @Bind(R.id.ll_name)
    LinearLayout ll_name;
    @Bind(R.id.name)
    TextView tvname;
    @Bind(R.id.number)
    TextView number;

    // 要申请的权限
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};

    //认证状态 1认证中，2认证成功，3认证失败，4未认证
    private String authenticationState = "";
    private static final int tag_submit = 1;
    private static final int tag_query = 2;
    private static final String TAG = "RealNameAuthenticationActivity";
    private Dialog dialog;

    private static final String TIP = "身份证识别失败，请重试或者重拍！";
    private static final int SETIMAGE = 1;
    private static final int HANDLE_FAIL = 2;
    private static final int DISMISS_DIALOG = 3;
    private final int SHOW_INFO = 4;

    private final int IDCARD_DISCEM = 0x60001;
    private final int IDCARD_identification = 0x60002;
    Handler mUIHandler;
    String name = "";
    String num = "";
    int cardType=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realnameautho);
        ButterKnife.bind(this);
        mUIHandler = new Handler(new InnerCallBack());
        initView();
    }

    private void initView() {


        authenticationState = SharedPreferencesUtils.getString(context, SharedPreferencesUtils.RENZHENG_STATUS);
        if ("2".equals(authenticationState)) {//如果已经认证的话
            imgStatus.setImageResource(R.mipmap.chenggong);
            imgStatus.setClickable(false);//无法点击
            edName.setText(SharedPreferencesUtils.getString(this, SharedPreferencesUtils.REAL_NAME));
            edCardNo.setText(SharedPreferencesUtils.getString(this, SharedPreferencesUtils.CERTIFICATENUMBER));
            edName.setFocusable(false);//失去焦点
            edCardNo.setFocusable(false);
            btnConfirm.setVisibility(View.GONE);
            if (TextUtils.isEmpty(edName.getText()) || TextUtils.isEmpty(edCardNo.getText())) {
                edCardNo.setText("暂无");
                edName.setText("暂无");
            }
        } else {
            imgStatus.setImageResource(R.mipmap.shangchuan);

            cardType = getIntent().getIntExtra("authenticcode", 0);
            switch (cardType){
                case 1:
                    number.setText("身份证号");
                    break;
                case 13:
                    number.setText("护照编号");
                    break;
                case 14:
                    number.setText("港澳通行证");
                    break;
                case 10:
                    number.setText("台胞通行证");
            }
        }
        tvTitle.setText("实名认证");
        tvSave.setVisibility(View.GONE);
        tvTitle.setTextColor(getResources().getColor(R.color.black_textcolor));
        ((ImageView) findViewById(R.id.iv_back)).setImageResource(R.mipmap.back_black);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        x = metrics.widthPixels;//获取了屏幕的宽度
        y = metrics.heightPixels * 504 / 1334;
    }

    private void uploadIdCardInfo(String idName, String idNo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", idName);
        map.put("idCard", idNo);
        map.put("cardType",cardType);
        map.put("sex",3);
        DataUtils.loadData(this, GetDataConfing.ID_CARD_identification, map, IDCARD_identification, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    ToastUtils.showToastShort(getBaseContext(), "网络异常");
                } else {
                    String json = dataRequest.getResponseData();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            ToastUtils.showToastShort(getBaseContext(), "认证成功!");
                            SharedPreferencesUtils.saveString(RealNameAuthenticationActivity.this, SharedPreferencesUtils.RENZHENG_STATUS, "2");
                            SharedPreferencesUtils.saveString(RealNameAuthenticationActivity.this, SharedPreferencesUtils.STATUS, "2");
                            imgStatus.setImageResource(R.mipmap.chenggong);
                            imgStatus.setClickable(false);//无法点击
                            SharedPreferencesUtils.saveString(RealNameAuthenticationActivity.this, SharedPreferencesUtils.REAL_NAME, edName.getText().toString());
                            SharedPreferencesUtils.saveString(RealNameAuthenticationActivity.this, SharedPreferencesUtils.CERTIFICATENUMBER, edCardNo.getText().toString());
                            edName.setFocusable(false);//失去焦点
                            edCardNo.setFocusable(false);
                            btnConfirm.setVisibility(View.GONE);
                            EventBus.getDefault().post(new RefreshEvent("", RefreshEvent.USER_INFO_PAGE));
                        } else {
                            ToastUtils.showToastShort(getBaseContext(), "认证失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void discernIDCard(Bitmap bitmap) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            map.put("img", URLEncoder.encode(getImageStr(bitmap), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (null == dialog)
            dialog = DialogUtils.showLoadingDialog(this);
        else
            dialog.show();
        DataUtils.loadData(this, GetDataConfing.ID_CARD_DISCEM, map, IDCARD_DISCEM, responseDataCallback);
    }

    private String getImageStr(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (bitmap != null)
            imgStatus.setImageBitmap(bitmap);
        String s = Base64.encodeToString(bytes, Base64.NO_WRAP);
        return s;
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @OnClick({R.id.img_status})
    public void onPhotoClick() {
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
    }

    public void go2CustomCamera() {
        String manufature = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (manufature.toUpperCase().equals("VIVO") && model.toUpperCase().equals("VIVO X6A")) {
            showTip("暂不支持该型号手机智能识别身份证，请手动输入姓名和身份证进行验证!");
            return;
        }
        if (Build.VERSION.SDK_INT <= 19) {
            showTip("该版本手机系统暂不支持智能识别身份证，请手动输入姓名和身份证进行验证!");
            return;
        }
        startActivityForResult(new Intent(RealNameAuthenticationActivity.this, CustomCameraActivity.class), 1);
    }

    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {

        new AlertDialog.Builder(this)
                .setTitle("照片权限")
                .setMessage("是否允许用户使用相机")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 10);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 10) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
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

    @OnClick(R.id.btn_confirm)
    public void submit() {

        String userName = edName.getText().toString().trim();
        String number = edCardNo.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "身份证不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (number.length() < 15 || number.length() > 18) {
            Toast.makeText(this, "身份证填写不合法", Toast.LENGTH_LONG).show();
            return;
        }

        //提交服务器，根据接口返回的状态，显示不同的图片
        uploadIdCardInfo(userName, number);
    }

    @TargetApi(16)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {//获取图片显示，显示提交按钮，做提交操作.
                String filepath = data.getStringExtra("filepath");
                //从文件加载图片。
                Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                //显示提交按钮
                //TODO　换成本地的接口
                discernIDCard(bitmap);
            } else {
                String str = data.getStringExtra("pic");
                String name = data.getStringExtra("name");
                String num = data.getStringExtra("num");

                edCardNo.setText(num);
                edName.setText(name);
                edName.setSelection(name.length());
                imgStatus.setVisibility(View.GONE);
            }
        }
    }

    public void verifyID(final Bitmap bitmap) {
        dialog.show();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String getPath = "/rest/160601/ocr/ocr_idcard.json";

                String imgBase64 = StringUtils.bitmapToString(bitmap);
                // 拼装请求body的json字符串
                JSONObject requestObj = new JSONObject();
                try {
                    JSONObject configObj = new JSONObject();
                    JSONObject obj = new JSONObject();
                    JSONArray inputArray = new JSONArray();
                    configObj.put("side", "face");
                    obj.put("image", getParam(50, imgBase64));
                    obj.put("configure", getParam(50, configObj.toString()));
                    inputArray.put(obj);
                    requestObj.put("inputs", inputArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String content = requestObj.toString();

                Log.w("data", content);

                HttpUtil.getInstance().httpPostBytes(getPath, null, null, content.getBytes(Constants.CLOUDAPI_ENCODING), null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        LogUtils.logD("test", "errorInfo--->" + e.getMessage());
                        Message.obtain(mUIHandler, HANDLE_FAIL).sendToTarget();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        getResultString(response);
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    /*
      * 获取参数的json对象
      */
    public JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private void getResultString(Response response) throws IOException {
        StringBuilder result = new StringBuilder();
        if (response.code() != 200) {
            result.append("错误原因：").append(response.header("X-Ca-Error-Message")).append(Constants.CLOUDAPI_LF).append(Constants.CLOUDAPI_LF);
//            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            Message.obtain(mUIHandler, HANDLE_FAIL).sendToTarget();
            return;
        }

        result.append(Constants.CLOUDAPI_LF).append(new String(response.body().bytes(), Constants.CLOUDAPI_ENCODING));
        try {
            JSONObject outputObj = new JSONObject(result.toString());
            String output = outputObj.optString("outputs");

            JSONArray array = new JSONArray(output);
            JSONObject obj = array.getJSONObject(0);
            String outputValue = obj.optString("outputValue");

            JSONObject outputValueObj = new JSONObject(outputValue);
            String dataValue = outputValueObj.optString("dataValue");

            JSONObject dataValueObj = new JSONObject(dataValue);
            name = dataValueObj.optString("name");
            num = dataValueObj.optString("num");
        } catch (JSONException e) {
            e.printStackTrace();
            Message.obtain(mUIHandler, DISMISS_DIALOG).sendToTarget();
        }

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(num) || name.equals("") || num.equals("")) {
            Message.obtain(mUIHandler, HANDLE_FAIL).sendToTarget();
            return;
        }

        //显示识别到的姓名，身份证。
        Message.obtain(mUIHandler, SHOW_INFO).sendToTarget();
    }

    private class InnerCallBack implements Handler.Callback {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case SHOW_INFO://显示识别的身份证信息。
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();

                    edName.setText(name);
                    edCardNo.setText(num);
                    break;
                case SETIMAGE:
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();

                    break;
                case HANDLE_FAIL:
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(RealNameAuthenticationActivity.this, TIP, Toast.LENGTH_LONG).show();
                    break;
                case DISMISS_DIALOG:
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    break;
            }
            return false;
        }
    }

    String idName;
    String idNo;
    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(context);
            } else if (dataRequest.getWhat() == IDCARD_DISCEM) {
                String json = dataRequest.getResponseData();
                try {
                    JSONObject obj = new JSONObject(json);
                    if (obj.optInt("code") == 0) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        JSONObject subJson = jsonArray.getJSONObject(0);
                        idName = subJson.getString("name");
                        idNo = subJson.getString("num");
                        if (idName != null && idName.length() > 0) {
                            edName.setText(idName);
                        }
                        if (idNo != null && idNo.length() > 0) {
                            edCardNo.setText(idNo);
                        }
                    } else {
                        Toast.makeText(context, obj.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                String json = dataRequest.getResponseData();
                LogUtils.logD(TAG, "返回的信息---->" + json);
                if (!TextUtils.isEmpty(json)) {
                    List<JsonMap<String, Object>> data = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                    if (dataRequest.getWhat() == tag_submit) {      //提交信息

                        try {
                            JSONObject obj = new JSONObject(json);
                            int code = obj.optInt("code");
                            if (code != 0) {
                                showTip(obj.optString("msg"));

                            } else {
                                SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.RENZHENG_STATUS, "2");
//                                relativeLayout.setBackgroundColor(Color.parseColor("#2cc701"));
                                imgStatus.setVisibility(View.VISIBLE);
                                imgStatus.setBackgroundResource(R.mipmap.chenggong);
                                imgStatus.setClickable(false);
                                btnConfirm.setVisibility(View.GONE);
                                isEdit(false, edCardNo);
                                isEdit(false, edName);

                                dialog.show();
                                Map<String, Object> map = BaseMapUtils.getMap(context);
                                DataUtils.loadData(context, GetDataConfing.queryAuthenticateInfo, map, tag_query, responseDataCallback);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (dataRequest.getWhat() == tag_query) {

                        try {
                            JSONObject obj = new JSONObject(json);
                            int code = obj.optInt("code");
                            if (code != 0) {
                                showTip(obj.optString("msg"));

                            } else {

                                JsonMap<String, Object> map = data.get(0);
                                String idcardNumber = map.getString("idcardNumber", "1111");
                                //实名认证状态
                                String audit = map.getString("auditStatus", "0");


                                //实名认证成功
                                if (authenticationState.equals("2")) {
                                    String name = map.getString("name");
                                    SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.REAL_NAME, name);
                                    edCardNo.setText(idcardNumber);
                                    edName.setText(name);
//                                    relativeLayout.setBackgroundColor(Color.parseColor("#2cc701"));
                                    imgStatus.setBackgroundResource(R.mipmap.chenggong);
                                    btnConfirm.setVisibility(View.GONE);
                                    isEdit(false, edCardNo);
                                    isEdit(false, edName);
                                    SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.RENZHENG_STATUS, 2 + "");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

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

    private void isEdit(boolean value, EditText editText) {

        if (value) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setFilters(new InputFilter[]{new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                    return null;
                }
            }});
        } else {
            //设置不可获取焦点
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            //输入框无法输入新的内容
            editText.setFilters(new InputFilter[]{new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                    return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
                }

            }});
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
