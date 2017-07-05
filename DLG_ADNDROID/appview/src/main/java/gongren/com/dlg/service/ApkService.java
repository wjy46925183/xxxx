package gongren.com.dlg.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * 检测到更新版本后台下载APK
 */
public class ApkService extends IntentService {

    public ApkService(String name) {
        super(name);
    }

    public ApkService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        String url = intent.getStringExtra("url");
//        if (!TextUtils.isEmpty(url)) {
//            RequestParams requestParams = new RequestParams(url);
//            requestParams.setAutoRename(true);
//            requestParams.setSaveFilePath(CacheUtils.productPath + "打零工.apk");
//            x.http().post(requestParams, new Callback.CommonCallback<File>() {
//                @Override
//                public void onSuccess(File result) {
//                    //安装
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    Uri uri = Uri.parse("file://" + result.getAbsolutePath());
//                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//
//                }
//
//                @Override
//                public void onCancelled(CancelledException cex) {
//
//                }
//
//                @Override
//                public void onFinished() {
//
//                }
//            });
//        }
    }
}
