package gongren.com.dlg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.utils.ActivityController;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.IntegerUtils;
import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 修改密码
 */
public class RevisePassWordActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;

    @Bind(R.id.tv_original_pass)
    TextView tvOriginalPass;
    @Bind(R.id.tv_new_pass)
    TextView tvNewPass;
    @Bind(R.id.tv_affirm_pass)
    TextView tvAffirmPass;
//    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_pass_word);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("修改密码");
        tvSave.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_back, R.id.but_revise_pass})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.but_revise_pass:
                String pastPass = tvOriginalPass.getText().toString().trim();
                String newPass = tvNewPass.getText().toString().trim();
                String affirmPass = tvAffirmPass.getText().toString().trim();
                if (pastPass == null || pastPass.length() <= 0) {
                    ToastUtils.showToastShort(context, "原密码不能为空");
                    return;
                }
                if (newPass == null || newPass.length() <= 0) {
                    ToastUtils.showToastShort(context, "新密码不能为空");
                    return;
                }
                if (affirmPass == null || affirmPass.length() <= 0) {
                    ToastUtils.showToastShort(context, "确认密码不能为空");
                    return;
                }
                if (!newPass.equals(affirmPass)) {
                    ToastUtils.showToastShort(context, "新密码和确认密码不一致，请重新输入");
                    return;
                }

                revisePassMethod(pastPass, newPass);
                break;
            default:
                break;
        }
    }

    /**
     * 修改密码
     *
     * @param pastPass
     * @param newPass
     */
    private void revisePassMethod(String pastPass, String newPass) {
//        mLoadingDialog = DialogUtils.showLoadingDialog(context);
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("oldPassword", pastPass);
        map.put("newPassword", newPass);
        DataUtils.loadData(context, GetDataConfing.SYSTEM_MODIFYPWD, map, IntegerUtils.API_MODIFY_PWD, responseDataCallback);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            //ToastUtils.showToastShort(LoginDialogActivity.this,dataRequest.getResponseData());
            if (tvTitle != null) {
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    Log.e("ResetPWD", "json=" + json);
                    JsonMap<String, Object> jsonMap = null;
                    if (!TextUtils.isEmpty(json)) {
                        switch (dataRequest.getWhat()) {
                            case IntegerUtils.API_MODIFY_PWD:
                                //忘记密码
                                jsonMap = JsonParseHelper.getJsonMap(json);
                                if (jsonMap.getInt("code") == 0) {
                                    /**
                                     * 退出登录
                                     */
                                    DataUtils.loadData(context, GetDataConfing.LOGOUT, BaseMapUtils.getMap(context), new ResponseDataCallback() {
                                        @Override
                                        public void onFinish(DataRequest dataRequest) {
                                            String responseData = dataRequest.getResponseData();
//                                            try {
//                                                JSONObject jsonObject = new JSONObject(responseData);
////                                                ToastUtils.showToastShort(context, jsonObject.optString("msg"));
//                                                if ("0".equals(jsonObject.optString("code"))) {
                                            //置空别名
                                            MyApplication.isLogInPage = true;
                                            SharedPreferencesUtils.clear(context);
                                            Intent intent = new Intent(context, MainActivity.class);
                                            intent.putExtra("isLogout", true);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            ActivityController.removeAll();//关闭所有的页面
//                                                }
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            mLoadingDialog.dismiss();
                                        }
                                    });
                                }
                                ToastUtils.showToastShort(context, jsonMap.getStringNoNull("msg"));
//                                    mLoadingDialog.dismiss();
                                break;
                        }

                    }
                }
            }
        }
    };


}
