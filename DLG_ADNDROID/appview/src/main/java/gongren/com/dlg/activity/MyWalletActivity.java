package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 我的钱包 首页
 * Created by lin.li on 2017/2/11.
 */
public class MyWalletActivity extends BaseActivity {
    private static final String HELP = "2";
    private static final String HELP2 = "2";
    private static final String HELP_TITAL = "帮助";
    private static final String HELP_TITAL2 = "钱包协议";
    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Bind(R.id.tv_save)
    TextView tvSave;

    @Bind(R.id.tv_money)
    TextView tvMoney;  // 金额
    @Bind(R.id.tv_orange)
    TextView tvOrange;  // 换算成金橙

    private Double orangeCount;      //橙子数
    private int status;           //认证状态
    private Double noWithdrawCash;   //不可提现的橙子数
    private Double withdrawCashAmount;   //可提现的余额
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("我的钱包");
        tvSave.setText("明细");
    }

    @Override
    protected void onResume() {
        super.onResume();
        addData();
    }

    private void addData() {
        dialog = DialogUtils.showLoadingDialog(context);
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        DataUtils.loadData(context, GetDataConfing.queryBalance, map, REQUEST, responseDataCallback);
    }

    private static final int REQUEST = 0;

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (tvOrange != null) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    if (!TextUtils.isEmpty(json)) {
                        if (dataRequest.getWhat() == REQUEST) {      //钱包余额
                            LogUtils.logD("zq", json);
                            List<JsonMap<String, Object>> data = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                            if (data != null && data.size() > 0) {
                                JsonMap<String, Object> jsonMap = data.get(0);
                                orangeCount = jsonMap.getDouble("amount", 0);
                                tvOrange.setText("折合" + ((int)(orangeCount * 10)) + "金橙");
                                tvMoney.setText(((double) orangeCount)+ "元");
                                noWithdrawCash = jsonMap.getDouble("noWithdrawCashAmount", 0);
                                withdrawCashAmount = jsonMap.getDouble("withdrawCashAmount", 0);
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

    @OnClick(R.id.tv_save)
    public void onEditorClick() {
        startActivity(new Intent(context, BalanceActivity.class));
    }

    //解释、充值、提现、代金券、设置
    @OnClick({R.id.iv_explain, R.id.tv_recharge, R.id.tv_withdrawal,
            R.id.tv_invoicemanager,R.id.tv_vouchers, R.id.tv_setting})
    public void startActivityto(View v) {
        switch (v.getId()) {
            case R.id.iv_explain:
//                startActivity(new Intent(context, WebViewActivity.class).putExtra("type", 2));
                Intent agreementIntent = new Intent(context, WebUtilsActivity.class);
                agreementIntent.putExtra("type",HELP2);
                agreementIntent.putExtra("contentUrl", GetDataConfing.ZHEN_WO);
                agreementIntent.putExtra("functionTitle",HELP_TITAL2);
                startActivity(agreementIntent);
                break;
            case R.id.tv_recharge:
                startActivity(new Intent(context, RechargeActivity.class));
//                startActivity(new Intent(context, EmployeeDetailsActivity.class));

                break;
            case R.id.tv_withdrawal:
                startActivityForResult(new Intent(context, GetCashActivity.class)
                        .putExtra("orangeCount", orangeCount)
                        .putExtra("noWithdrawCash", noWithdrawCash)
                        .putExtra("withdrawCashAmount", withdrawCashAmount),
                        0);
                break;
            case R.id.tv_vouchers:  //兑换码
                startActivity(new Intent(context, VoucherActivity.class));
                break;
            case R.id.tv_invoicemanager://发票管理
                startActivity(new Intent(context, ReceiptManagerActivity.class));
                break;
            case R.id.tv_setting:
                startActivity(new Intent(context, WalletSettingActivity.class));
                break;
            default:
                break;
        }
    }

}
