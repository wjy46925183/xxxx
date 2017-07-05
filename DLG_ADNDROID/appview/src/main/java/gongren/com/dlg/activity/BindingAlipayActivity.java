package gongren.com.dlg.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 绑定支付宝
 */
public class BindingAlipayActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;

    @Bind(R.id.alipay_name)
    TextView alipayName;
    @Bind(R.id.alipay_accout)
    EditText alipayAccout;

    Button btnBanding;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_alipay);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        tvTitle.setText("绑定支付宝");
        tvSave.setVisibility(View.GONE);
        alipayName.setText(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.REAL_NAME));
    }

    @OnClick({R.id.iv_back, R.id.btn_Banding})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_Banding:
                Request_BindingAlipay();
                break;
        }
    }

    /**
     * 绑定支付宝接口
     */
    private void Request_BindingAlipay() {
        String username = alipayName.getText().toString();
        String account = alipayAccout.getText().toString();
        String acctType = "1";
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showToastShort(context, "请填写支付宝姓名持有人名称");
            return;
        }
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showToastShort(context, "请填写支付宝账号");
            return;
        }
        dialog = DialogUtils.showLoadingDialog(context);
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("payAccount", account);
        map.put("payAccountHouseholder", username);
        map.put("payType", "1");
        map.put("reservedPhone", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.MOBILE));
//        map.put("reservedPhone", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.MOBILE));

        DataUtils.loadData(context, GetDataConfing.payBindRest,
                map, TAG_REQUEST, responseDataCallback);
    }


    private final int TAG_REQUEST = 10;

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
                                ToastUtils.showToastLong(context, "支付宝绑定成功");
                                setResult(RESULT_OK);
                                finish();
                            }else {
                                ToastUtils.showToastLong(context, "支付宝绑定失败");
                            }
                        }
                    }
                }
            }
        }
    };
}
