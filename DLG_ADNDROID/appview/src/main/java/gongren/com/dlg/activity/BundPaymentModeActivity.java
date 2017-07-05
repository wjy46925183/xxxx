package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

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
import gongren.com.dlg.wxapi.WechatPayUtils;

/**
 * 绑定支付方式
 * Created by lin.li on 2017/2/11.
 */
public class BundPaymentModeActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.layout_alipay)
    LinearLayout layoutAlipay;
    @Bind(R.id.layout_weixin)
    LinearLayout layoutWeixin;
    @Bind(R.id.layout_card)
    LinearLayout layoutCard;

    private static final int REQUEST_BandingWX = 1;          //绑定微信
    private Dialog dialog;
    private IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bund_paymentmode);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ivBack.setVisibility(View.GONE);
        tvTitle.setText("绑定支付方式");
        tvSave.setText("跳过");
        mShareAPI = UMShareAPI.get(context);
        iwxapi = WXAPIFactory.createWXAPI(this, WechatPayUtils.APP_ID);
        iwxapi.registerApp(WechatPayUtils.APP_ID);
    }


    @OnClick({R.id.iv_back, R.id.tv_save, R.id.layout_alipay, R.id.layout_weixin, R.id.layout_card})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:    //跳过
                startActivity(new Intent(this, MyWalletActivity.class));
                finish();
                break;
            case R.id.layout_alipay:    //绑定支付宝
                startActivityForResult(new Intent(this, BindingAlipayActivity.class), 1);
                break;
            case R.id.layout_weixin:   //绑定微信
                mShareAPI.getPlatformInfo(context, SHARE_MEDIA.WEIXIN, authListener);
                break;
            case R.id.layout_card:     //绑定银行卡
                startActivityForResult(new Intent(this, BindingBankCardActivity.class), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            startActivity(new Intent(context, MyWalletActivity.class));
            finish();
        }
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (tvTitle != null) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    if (!TextUtils.isEmpty(json)) {
                        if (dataRequest.getWhat() == REQUEST_BandingWX) {      //绑定微信
                            mShareAPI.deleteOauth(context, SHARE_MEDIA.WEIXIN, delectauthListener);
                            startActivity(new Intent(context, MyWalletActivity.class));
                            finish();
                        }
                    }
                }
            }
        }
    };

    /**
     * 微信登录监听
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private UMShareAPI mShareAPI;
    private UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            //上传微信信息
            dialog = DialogUtils.showLoadingDialog(context);
            Map<String, Object> map1 = BaseMapUtils.getMap(context);
            map1.put("openId", map.get("openid"));
            map1.put("payType", 3 + "");
            map1.put("openName", map.get("screen_name"));
            DataUtils.loadData(context, GetDataConfing.BandingWX, map1, REQUEST_BandingWX, responseDataCallback);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            if (throwable.getMessage().contains("没有安装应用")) {
                ToastUtils.showToastLong(context, "你尚未安装微信，请安装后再进行操作");
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.showToastLong(context, "您取消了微信授权");
        }
    };

    /**
     * 解除微信绑定
     */
    private UMAuthListener delectauthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            ToastUtils.showToastLong(context, throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.showToastLong(context, "您取消微信授权失败");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
