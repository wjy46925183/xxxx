package gongren.com.dlg.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * Created by zjf on 2017/2/12.
 * 代金券的界面
 */
public class VoucherActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_show)
    EditText etShow;

    private Dialog dialog;
    private int TAG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("兑换码");
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
            if (ivBack != null) {
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    if (!TextUtils.isEmpty(json)) {
                        if (dataRequest.getWhat() == TAG_REQUEST) {
                            JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                            ToastUtils.showToastLong(context, jsonMap.getStringNoNull("msg"));
//                            if (jsonMap.getInt("code") == 0) {
//                                //不做操作
//                            }
                        }
                    }
                }
            }
        }
    };

    @OnClick({R.id.iv_back, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                int num = etShow.getText().length();
                if (num < 8) {
                    ToastUtils.showToastLong(context, "请输入有效的兑换码");
                } else {
                    Map<String, Object> map = BaseMapUtils.getMap(context);
                    map.put("userRpcvo", "788909895532548096");
                    map.put("exchangeCode", etShow.getText().toString());
                    dialog = DialogUtils.showLoadingDialog(context);
                    DataUtils.loadData(context, GetDataConfing.redeem, map, TAG_REQUEST, responseDataCallback);
                }
                break;
        }
    }


}
