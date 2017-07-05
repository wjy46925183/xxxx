package gongren.com.dlg.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cmb.pb.util.CMBKeyboardFunc;
import gongren.com.dlg.R;

/**
 * 招商银行网页端界面
 */
public class CMBWebViewActivity extends BaseActivity {

    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmbwebview);
        ButterKnife.bind(this);

        tvTitle.setText("一网通支付");
//
//        YiKaTongBean bean = (YiKaTongBean) getIntent().getExtras().getSerializable("bean");
//        String payUrl = bean.getData().get(0).getPayUrl();
            String payUrl = getIntent().getStringExtra("url");
            String code = getIntent().getStringExtra("code");
        if (!TextUtils.isEmpty(payUrl)) {
            webView.setWebViewClient(new WebViewClient());
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setSaveFormData(false);
            webSettings.setSavePassword(false);
            webSettings.setSupportZoom(false);

            String s = "jsonRequestData=" + code/*.getBranchID() + "&CoNo=" + cmb.getCoNo() + "&BillNo=" + cmb.getBillNo()
                    + "&Amount=" + new DecimalFormat("0.00").format(Double.parseDouble(cmb.getAmount())) + "&Date="
                    + DateUtils.dateFormat("yyyyMMdd", System.currentTimeMillis() + "") + "&MerchantUrl=" + cmb.getMerchantUrl()
                    + "&MerchantCode=" + cmb.getMerchantCode() + "&MerchantPara=" + cmb.getMerchantPara()
                    + "&MerchantRetUrl=" + GetDataConfing.webURL + "?type=4"*/;

            webView.postUrl(payUrl, s.getBytes());

            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    CMBKeyboardFunc kbFunc = new CMBKeyboardFunc(CMBWebViewActivity.this);
                    if (kbFunc.HandleUrlCall(webView, url) == false) {
//                        return super.shouldOverrideUrlLoading(view, url);
                        return kbFunc.HandleUrlCall(webView, url) || super.shouldOverrideUrlLoading(view, url);
                    } else {
                        return true;
                    }
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            } else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
