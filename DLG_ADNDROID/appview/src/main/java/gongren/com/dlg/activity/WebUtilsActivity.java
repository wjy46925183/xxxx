package gongren.com.dlg.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
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
import gongren.com.dlg.volleyUtils.GetDataConfing;

public class WebUtilsActivity extends BaseActivity {

    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>
    private Uri imageUri;

    @Bind(R.id.iv_back)
    ImageView ivBack;//返回键
    @Bind(R.id.tv_title)
    TextView tvTitle;//标题栏
    @Bind(R.id.iv_right)
    ImageView ivRight;//右边按钮
    @Bind(R.id.web_content)
    WebView webContent;//WebView内容

    private WebSettings webset;

    private ShareAction mShareAction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_utils);
        ButterKnife.bind(this);
        synCookies();
        initView();
    }

    public void synCookies() {
        // Cookie是通过我们Volley活着HttpClient获取的
        List<HttpCookie> _cookies = MyApplication.getCookieStore().getCookies();
        String _cookie = "";
        if (null != _cookies && _cookies.size() > 0) {
            for (HttpCookie cookie : _cookies) {
                _cookie = cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain() + "; path=" + cookie.getPath();
                synCookies(this, GetDataConfing.BASEURL, _cookie);
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

        tvTitle.setText(functionTitle);
        ivRight.setVisibility(View.INVISIBLE);
//        initWebView();
        //加载需要显示的网页
        WebSettings webSettings = webContent.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        //提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

//        webContent.postUrl(contentUrl,type.getBytes());
        //设置Web视图
//        String url = "file:///android_asset/testH5.html";
        webContent.setWebViewClient(webViewClient);
//        webContent.loadUrl("http://10.20.31.66/index.html");
        if (!TextUtils.isEmpty(userId)) {
            Uri mUriStr = Uri.parse(contentUrl);
            Set<String> pathSegList = mUriStr.getQueryParameterNames();
            if (null != pathSegList && pathSegList.size() > 0) {
                contentUrl = contentUrl + "&" + type + "&userId=" + userId;
            } else {
                contentUrl = contentUrl + "?" + type + "&userId=" + userId;
            }
            webContent.loadUrl(contentUrl);
        } else {
            webContent.postUrl(contentUrl, type.getBytes());
        }

        webContent.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                take();
                return true;
            }


            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                take();
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                take();
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                take();
            }
        });

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
        webset.setAllowFileAccess(false);
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
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected WebViewClient webViewClient = new WebViewClient() {
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();  // 接受所有网站的证书
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
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
                if (url.toLowerCase().contains("dlg://inviteqq") || url.toLowerCase().contains("dlg://inviteweixin")) {
                    mShareAction = ShareUtils.setUMShare(WebUtilsActivity.this, path,
                            title, msg, shareUrl, img);
                    mShareAction.share();
                    return true;
                }
                if (url.toLowerCase().contains("dlg://share") || url.toLowerCase().contains("dlg://invite")) {
                    if (url.toLowerCase().contains("dlg://share")) {
                        mShareAction = ShareUtils.setUMShareAction(WebUtilsActivity.this,
                                title, msg, shareUrl, img);
                        mShareAction.open();
                    } else {
                        mShareAction = ShareUtils.setUMShareInvitation(WebUtilsActivity.this,
                                title, msg, shareUrl, img);
                        mShareAction.open();
                    }
                }
                return true;
            }
            view.loadUrl(url);
            return true;//返回true表明点击网页里面的连接还是在当前的webview里跳转,不跳到浏览器
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    };

//    //Web视图
//    private class webViewClient extends WebViewClient {
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {

                if (result != null) {
                    String path = getPath(getApplicationContext(),
                            result);
                    Uri uri = Uri.fromFile(new File(path));
                    mUploadMessage
                            .onReceiveValue(uri);
                } else {
                    mUploadMessage.onReceiveValue(imageUri);
                }
                mUploadMessage = null;


            }
        }
    }


    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.BASE)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;

        if (resultCode == RESULT_OK) {

            if (data == null) {

                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }

        return;
    }


    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        // Create the storage directory if it does not exist
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);

        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        this.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
