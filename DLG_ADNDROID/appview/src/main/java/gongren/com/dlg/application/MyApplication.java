package gongren.com.dlg.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.common.sys.SystemUtil;
import com.dlg.personal.app.MApp;
import com.iflytek.cloud.SpeechUtility;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import gongren.com.dlg.utils.ExampleUtil;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.PersistentCookieStore;
import spring.update.UpdateConfig;
import spring.update.callback.UpdateCheckCB;
import spring.update.callback.UpdateDownloadCB;
import spring.update.creator.DefaultNeedDownloadCreator;
import spring.update.creator.DefaultNeedInstallCreator;
import spring.update.creator.DefaultNeedUpdateCreator;
import spring.update.model.CheckEntity;
import spring.update.model.HttpMethod;
import spring.update.model.Update;
import spring.update.model.UpdateParser;
import spring.update.strategy.WifiFirstStrategy;

//import android.support.multidex.MultiDexApplication;

//import android.support.multidex.MultiDexApplication;

public class MyApplication extends MApp {
    //application的对象
    public static MyApplication instance;
    private static PersistentCookieStore cookieStore;
    public static LatLng mLocalLatlng;
    public static int  dataValueDistance = 1000; //打开距离
    public static boolean isLogInPage = false;
    public static boolean isBoss = false; //是否是雇主

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        //科大讯飞
        SpeechUtility utility = SpeechUtility.createUtility(MyApplication.this, "appid=" + "59367c54");
        instance = this;
        /**
         * 开启debug模式，方便定位错误，具体错误检查方式可以查看
         * http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
         */
        Config.DEBUG = true;
        //初始化友盟分享
        UMShareAPI.get(this);
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        registerMessageReceiver();  // used for receive msg
        cookieStore = new PersistentCookieStore(this);
        Log.e("DLG", "cookieStore==" + cookieStore.getCookies());
        CookieManager cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        UpdateConfig();//检查APP版本更新
        super.onCreate();
    }

    public static PersistentCookieStore getCookieStore() {
        return cookieStore;
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //微信
        PlatformConfig.setWeixin("wx68a8f66601847550", "e6c3894850eb395a571b095baebaafc6");
        //QQ
        PlatformConfig.setQQZone("1105693051", "XMrgV1M09LUDYbs5");
    }

    /**
     * application的单利
     *
     * @return
     */
    public static synchronized MyApplication getInstance() {
        return instance;
    }

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "gongren.com.dlg.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }

        /**
         * 如果接收到的激光需要做出的操作
         *
         * @param msg
         */
        private void setCostomMsg(String msg) {
            if (null != msg) {
            }
        }
    }

    /**
     * app更新初始化
     */
    private void UpdateConfig() {
        CheckEntity checkEntity = new CheckEntity();
        checkEntity.setMethod(HttpMethod.POST);
        checkEntity.setUrl(GetDataConfing.CHECK_VERSION);
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("appVersionCode", getVersion());
        stringMap.put("channel", AnalyticsConfig.getChannel(this));//渠道号
        stringMap.put("format", "json");
        stringMap.put("sdkVersionCode", SystemUtil.getDeviceSDK());
        checkEntity.setParams(stringMap);
        UpdateConfig.getConfig()
                .checkEntity(checkEntity)
                .jsonParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) {
                        Update update = new Update("");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if ("0".equals(jsonObject.optString("code"))) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                JSONObject jobt = (JSONObject) data.get(0);
                                boolean isDownload = jobt.getBoolean("isDownload");
                                String downloadUrl = jobt.getString("downloadUrl");
                                int newAppVersionCode = jobt.getInt("newAppVersionCode");
                                boolean newest = jobt.getBoolean("newest");
                                String newVersionRemark = jobt.getString("newVersionRemark");
                                String newVersion = jobt.getString("newVersion ");
                                update.setUpdateTime(System.currentTimeMillis());
                                // 此apk包的下载地址
                                update.setUpdateUrl(downloadUrl);
                                // 此apk包的版本号
                                update.setVersionCode(newAppVersionCode);
                                // 此apk包的版本名称
                                update.setVersionName(newVersion);
                                // 此apk包的更新内容
                                update.setUpdateContent(newVersionRemark);
                                // 此apk包是否为强制更新
                                update.setForced(isDownload);
                                // 是否显示忽略此次版本更新按钮
                                update.setIgnore(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return update;
                    }
                })
                .updateDialogCreator(new DefaultNeedUpdateCreator()) //提示更新框
                .downloadDialogCreator(new DefaultNeedDownloadCreator()) //下载框
                .installDialogCreator(new DefaultNeedInstallCreator()) //安装框
                .strategy(new WifiFirstStrategy())  //框逻辑显示
                .checkCB(new UpdateCheckCB() {

                    @Override
                    public void onCheckError(int code, String errorMsg) {
                    }

                    @Override
                    public void onUserCancel() {
                        Toast.makeText(getApplicationContext(), "用户取消更新", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCheckIgnore(Update update) {
                        Toast.makeText(getApplicationContext(), "用户忽略此版本更新", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void hasUpdate(Update update) {
                        Toast.makeText(getApplicationContext(), "检查到有更新", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void noUpdate() {
//                        Toast.makeText(getApplicationContext(), "无更新", Toast.LENGTH_SHORT).show();
                    }
                })
                // apk下载的回调
                .downloadCB(new UpdateDownloadCB() {
                    @Override
                    public void onUpdateStart() {
                        Toast.makeText(getApplicationContext(), "下载开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUpdateComplete(File file) {
                        Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUpdateProgress(long current, long total) {
//                        ToastUtils.showToastShort(getApplicationContext(),current+"::::"+total);
                    }

                    @Override
                    public void onUpdateError(int code, String errorMsg) {
//                        Toast.makeText(getApplicationContext(), "下载失败：code:" + code + ",errorMsg:" + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            int version = info.versionCode;
            return version + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
