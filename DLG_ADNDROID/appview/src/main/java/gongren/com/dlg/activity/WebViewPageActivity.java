package gongren.com.dlg.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;

import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.utils.ShareUtils;
import gongren.com.dlg.utils.ToastUtils;

/**
 * 作者：wangdakuan
 * 主要功能：
 * 创建时间：2017/6/29 13:49
 */
public class WebViewPageActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;//返回键
    @Bind(R.id.tv_title)
    TextView tvTitle;//标题栏
    @Bind(R.id.iv_right)
    ImageView ivRight;//右边按钮
    @Bind(R.id.web_content)
    WebView webContent;//WebView内容
    @Bind(R.id.layout_title)
    LinearLayout layoutTitle; //头部

    private WebSettings webset;

    private ShareAction mShareAction = null;
    private List<String> stringUrlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_utils);
        ButterKnife.bind(this);
        initView();
    }

    public void synCookies(String url) {
        // Cookie是通过我们Volley活着HttpClient获取的
        List<HttpCookie> _cookies = MyApplication.getCookieStore().getCookies();
        String _cookie = "";
        if (null != _cookies && _cookies.size() > 0) {
            for (HttpCookie cookie : _cookies) {
                _cookie = cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain() + "; path=" + cookie.getPath();
//                synCookies(this, GetDataConfing.BASEURL, _cookie);
                synCookies(this, url, _cookie);
            }
        }
    }

    /**
     * 设置Cookie
     *
     * @param context
     * @param url
     * @param cookie  格式：uid=21233 如需设置多个，需要多次调用
     */
    protected void synCookies(Context context, String url, String cookie) {
        try {
            URL url1 = new URL(url);
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie(url1.getHost(), cookie);//cookies是在HttpClient中获得的cookie
            CookieSyncManager.getInstance().sync();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        Intent intent = getIntent();

        String functionTitle = intent.getStringExtra("functionTitle");
        String contentUrl = intent.getStringExtra("contentUrl");
        String userId = intent.getStringExtra("userId");
        String type = "type=" + intent.getStringExtra("type");
        boolean isShow = intent.getBooleanExtra("isShow", true);
        layoutTitle.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tvTitle.setText(functionTitle);
        ivRight.setVisibility(View.INVISIBLE);
//        initWebView();
//        //加载需要显示的网页
//        WebSettings webSettings = webContent.getSettings();
//        //设置WebView属性，能够执行Javascript脚本
//        webSettings.setJavaScriptEnabled(true);
//        //设置可以访问文件
//        webSettings.setAllowFileAccess(true);
//        //设置支持缩放
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setUseWideViewPort(true);
//        //提高渲染的优先级
//        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        initWebView();
        if (!TextUtils.isEmpty(userId)) {
            Uri mUriStr = Uri.parse(contentUrl);
            Set<String> pathSegList = mUriStr.getQueryParameterNames();
            if (null != pathSegList && pathSegList.size() > 0) {
                contentUrl = contentUrl + "&" + type + "&userId=" + userId;
            } else {
                contentUrl = contentUrl + "?" + type + "&userId=" + userId;
            }
            synCookies(contentUrl);
            webContent.loadUrl(contentUrl);
        } else {
            webContent.postUrl(contentUrl, type.getBytes());
        }
//        webContent.postUrl(contentUrl,type.getBytes());
        //设置Web视图
//        String url = "file:///android_asset/testH5.html";
        webContent.setWebViewClient(webViewClient);
//        webContent.loadUrl("http://10.20.31.66/index.html");



        //设置Web视图
//        webContent.setWebViewClient(new webViewClient ());
    }

    private void initWebView() {
        webset = webContent.getSettings();
        webContent.setScrollbarFadingEnabled(true);
        webContent.setWebViewClient(webViewClient);
        webContent.requestFocus();
        webContent.setFocusable(true);
        webContent.setHorizontalScrollBarEnabled(false);
        webContent.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webset.setTextSize(WebSettings.TextSize.NORMAL);
        webset.setTextSize(WebSettings.TextSize.NORMAL);
        webset.setJavaScriptEnabled(true);
        //设置默认的显示编码
        webset.setDefaultTextEncodingName("UTF-8");
        webset.setAllowFileAccess(true);
        webset.setUseWideViewPort(true);
        webset.setLoadWithOverviewMode(true);
        webset.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webset.setUserAgentString(null);
        webset.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webset.setDomStorageEnabled(true);
        webset.setGeolocationEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webset.setAllowFileAccessFromFileURLs(false);
            webset.setAllowUniversalAccessFromFileURLs(false);
        }
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:      //返回
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (stringUrlList.size() > 1) {
            if (webContent.canGoBack()) {
                stringUrlList.remove(stringUrlList.size() - 1);
                webContent.goBack();   //后退
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    boolean isFinished = false;
    boolean isStart = false;
    protected WebViewClient webViewClient = new WebViewClient() {
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();  // 接受所有网站的证书
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            isFinished = false;
            if(!isStart){
                stringUrlList.add(url);
            }
            isStart = true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            synCookies(url);
            isStart = false;
            if (!isStart && isFinished) {
                if(stringUrlList.size()>0){
                    stringUrlList.remove(stringUrlList.size()-1);
                }
            }
        }

        //dlg://share?url=http://www.baidu.com&amp;
        // title=‘真我’比你更懂你-分享&amp;msg=我们都是好孩子&amp;
        // uid=123654&amp;img=http://zw.dalinggong.com/images/logo.png


        //dlg://Invite?url=http://www.baidu.com&amp;
        // title=‘真我’比你更懂你-邀请&amp;msg=这是邀请的协议&amp;
        // uid=123654987&amp;img=http://zw.dalinggong.com/images/logo.png
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String shareUrl = null;
            String title = null;
            String msg = null;
            String uid = null;
            String img = null;
            String cid = null;
            String path = null;

            if (url.toLowerCase().contains("/dlgpop")) {
                finish();
                return true;
            }
            if (url.toLowerCase().contains("dlg://share") || url.toLowerCase().contains("dlg://invite")
                    || url.toLowerCase().contains("dlg://inviteqq") || url.toLowerCase().contains("dlg://inviteweixin")) {
                if (!TextUtils.isEmpty(url)) {
                    Uri mUriStr = Uri.parse(url);
                    path = mUriStr.getPath();
                    shareUrl = mUriStr.getQueryParameter("url");
                    title = mUriStr.getQueryParameter("title");
                    msg = mUriStr.getQueryParameter("msg");
                    uid = mUriStr.getQueryParameter("uid");
                    img = mUriStr.getQueryParameter("img");
                    cid = mUriStr.getQueryParameter("cid");
                }
                if (TextUtils.isEmpty(shareUrl)) {
                    ToastUtils.showToastLong(WebViewPageActivity.this, "没有获取到分享参数。");
                }
                if (url.toLowerCase().contains("dlg://inviteqq") || url.toLowerCase().contains("dlg://inviteweixin")) {
                    mShareAction = ShareUtils.setUMShare(WebViewPageActivity.this, url,
                            title, msg, shareUrl, img);
                    mShareAction.share();
//                    mShareAction.open();
                    return true;
                }
                if (url.toLowerCase().contains("dlg://share") || url.toLowerCase().contains("dlg://invite")) {
                    if (url.toLowerCase().contains("dlg://share")) {
                        mShareAction = ShareUtils.setUMShareAction(WebViewPageActivity.this,
                                title, msg, shareUrl, img);
                        mShareAction.open();
                    } else {
                        mShareAction = ShareUtils.setUMShareInvitation(WebViewPageActivity.this,
                                title, msg, shareUrl, img);
                        mShareAction.open();
                    }
                }
                return true;
            }
            isFinished = true;
            view.loadUrl(url);
            return true;//返回true表明点击网页里面的连接还是在当前的webview里跳转,不跳到浏览器
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    };


}
