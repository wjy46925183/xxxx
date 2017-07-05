package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.utils.SharedPreferencesUtils;
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
import gongren.com.dlg.javabean.PaymentBindingInfo;
import gongren.com.dlg.javabean.PaymentBindingJson;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.GsonUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 编辑
 * Created by lin.li on 2017/2/12.
 */
public class EditWalletCardActivity extends BaseActivity {


    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.card_alipay_desc)
    TextView cardAlipayDesc;
    @Bind(R.id.alipay_layout)
    RelativeLayout alipayLayout;
    @Bind(R.id.card_wechat_name)
    TextView cardWechatName;
    @Bind(R.id.card_wechat_desc)
    TextView cardWechatDesc;
    @Bind(R.id.wechat_layout)
    RelativeLayout wechatLayout;
    @Bind(R.id.card_bank_name)
    TextView cardBankName;
    @Bind(R.id.card_bank_desc)
    TextView cardBankDesc;
    @Bind(R.id.bank_layout)
    RelativeLayout bankLayout;
    @Bind(R.id.img_logo3)
    ImageView img_logo3;
    private final static  int REQUEST_UNBandingWX = 0 ;
    private Dialog dialog;
    private String id;
    private UMShareAPI mShareAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallet_card);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        tvTitle.setText("编辑");
        mShareAPI = UMShareAPI.get(context);
        query();
    }

    /**
     * 查询银行卡列表,查询支付宝,查询微信
     */
    private void query() {
        dialog = DialogUtils.showLoadingDialog(context);
        dialog.show();
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("minId", "0");
        map.put("size", "10");
        map.put("payType", "");
        DataUtils.loadData(context, GetDataConfing.queryBindInfoList,
                map, REQUEST_QUERY, responseDataCallback);
    }

    /**
     * 解绑
     */
    private void Unbinding(String bindId) {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("id", bindId);
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        DataUtils.loadData(context, GetDataConfing.unbind,
                map, REQUEST_UNBIND, responseDataCallback);
    }

    private static final int REQUEST_QUERY = 4;            //查询
    private static final int REQUEST_UNBIND = 5;             //解绑银行卡
    /**
     * 数据请求回调接口  银行卡，支付宝，微信
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
                    dialog.dismiss();
                    if (!TextUtils.isEmpty(json)) {
                        if (dataRequest.getWhat() == REQUEST_QUERY) {
                            LogUtils.logD("zp", json);
//                            List<JsonMap<String, Object>> data = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                            PaymentBindingJson bindingJson = GsonUtils.jsonToBean(json, PaymentBindingJson.class);
                            if (bindingJson.data != null) {
                                setData(bindingJson.data);
                            }
                        } else if (dataRequest.getWhat() == REQUEST_UNBIND) {

                            JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
//                            ToastUtils.showToastShort(context, jsonMap.getStringNoNull("msg"));
                            if (jsonMap.getInt("code") == 0) {
                                query();
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
     */
    private String alipayId, bankId, wxId;

    private void setData(List<PaymentBindingInfo> data) {
        PaymentBindingInfo alipayInfo = null, wxInfo =null, bankInfo = null;
        for (int i = 0; i < data.size(); i++) {
            switch (data.get(i).payType) {
                case 1:
                    alipayInfo = data.get(i);
                    alipayId = String.valueOf(alipayInfo.id);
                    break;
                case 2:
                    wxInfo = data.get(i);
                    wxId = String.valueOf(wxInfo.id);
                    break;
                case 3:
                    bankInfo = data.get(i);
                    bankId = String.valueOf(bankInfo.id);
                    break;
            }
        }
        //支付宝
        if (alipayInfo != null) {
            cardAlipayDesc.setText(alipayInfo.payAccount);
            alipayLayout.setVisibility(View.VISIBLE);
        } else {
            alipayLayout.setVisibility(View.GONE);
        }
        //银行卡
        if (bankInfo != null) {
            String bankAcct = bankInfo.payAccount;
            bankLayout.setVisibility(View.VISIBLE);
            cardBankDesc.setText(bankAcct.substring(0, 4) + "****" +
                    bankAcct.substring(bankAcct.length() - 4, bankAcct.length()));
            String iamgIcon =bankInfo.bankIconUrl;
            cardBankName.setText(bankInfo.depositBank);
            Glide.with(this).load(iamgIcon).fitCenter().error(R.mipmap.zhifu_yiwangtong)
                    .into(img_logo3);
            //            img_logo3.setImageResource(getResources().
            //                    getIdentifier(bankMap.getStringNoNull("icont"), "mipmap", getPackageName()));
        } else {
            bankLayout.setVisibility(View.GONE);
        }
        //微信
        if (wxInfo != null) {
            String wxname = wxInfo.payAccounthouseholder;
            cardWechatDesc.setText(wxname);
            wechatLayout.setVisibility(View.VISIBLE);
        } else {
            wechatLayout.setVisibility(View.GONE);
        }

    }

    private void setData1(List<JsonMap<String, Object>> data) {
        JsonMap alipayMap = null, wxMap = null, bankMap = null;
        for (int i = 0; i < data.size(); i++) {
            switch (data.get(i).getInt("payType")) {
                case 1:
                    alipayMap = data.get(i);
                    alipayId = alipayMap.getStringNoNull("id");
                    break;
                case 2:
                    bankMap = data.get(i);
                    bankId = bankMap.getStringNoNull("id");
                    break;
                case 3:
                    wxMap = data.get(i);
                    wxId = wxMap.getStringNoNull("id");
                    break;
            }
        }
        //支付宝
        if (alipayMap != null) {
            cardAlipayDesc.setText(alipayMap.getStringNoNull("payAccount"));
            alipayLayout.setVisibility(View.VISIBLE);
        } else {
            alipayLayout.setVisibility(View.GONE);
        }
        //银行卡
        if (bankMap != null) {
            String bankAcct = bankMap.getStringNoNull("payAccount");
            bankLayout.setVisibility(View.VISIBLE);
            cardBankDesc.setText(bankAcct.substring(0, 4) + "****" +
                    bankAcct.substring(bankAcct.length() - 4, bankAcct.length()));
            String iamgIcon = bankMap.getStringNoNull("bankIconUrl");
            Glide.with(this).load(iamgIcon).fitCenter().error(R.mipmap.zhifu_yiwangtong)
                    .into(img_logo3);
//            img_logo3.setImageResource(getResources().
//                    getIdentifier(bankMap.getStringNoNull("bankIconUrl"), "mipmap", getPackageName()));
        } else {
            bankLayout.setVisibility(View.GONE);
        }
        //微信
        if (wxMap != null) {
            String wxname = wxMap.getStringNoNull("openName");
            cardWechatDesc.setText(wxname);
            wechatLayout.setVisibility(View.VISIBLE);
        } else {
            wechatLayout.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @OnClick({R.id.tv_alipay_unbinding, R.id.tv_wechat_unbinding, R.id.tv_bank_unbinding})
    public void onClick(View view) {
        dialog.show();
        switch (view.getId()) {
            case R.id.tv_alipay_unbinding:
//                解绑支付宝
                Unbinding(alipayId);
//                Unbinding(alipayId);
//                startActivityForResult(new Intent(context, SetPayPasswordSecondActivity.class).putExtra("type", 3), 14);
                break;
            case R.id.tv_wechat_unbinding:
//                解绑微信
                mShareAPI.deleteOauth(context, SHARE_MEDIA.WEIXIN, authListener);
//                Unbinding(wxId);
//                startActivityForResult(new Intent(context, SetPayPasswordSecondActivity.class).putExtra("type", 3), 15);
                break;
            case R.id.tv_bank_unbinding:
//                解绑银行卡
                Unbinding(bankId);
//                startActivityForResult(new Intent(context, SetPayPasswordSecondActivity.class).putExtra("type", 3), 16);
                break;
        }
    }

    private UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Unbinding(wxId);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            if (throwable.getMessage().contains("没有安装应用")) {
                ToastUtils.showToastLong(context, "你尚未安装微信，请安装后再进行操作");
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.showToastLong(context, "您取消了微信授权");
            if(dialog!=null){
                dialog.dismiss();
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case 14:
                    Unbinding(alipayId);
                    break;
                case 15:
                    Unbinding(wxId);
                    break;
                case 16:
                    Unbinding(bankId);
                    break;
            }
        }
    }
}
