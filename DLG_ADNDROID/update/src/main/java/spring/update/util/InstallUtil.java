package spring.update.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

import spring.update.BuildConfig;

/**
 * @author Administrator
 */
public class InstallUtil {

    public static final int REQUEST_INSTALL = 0x010101;

    /**
     * install apk
     *
     * @param context  the context is used to send install apk broadcast;
     * @param filename the file name to be installed;
     */
    public static void installApk(Context context, String filename) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File pluginfile = new File(filename);
//判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", pluginfile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(pluginfile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, REQUEST_INSTALL);
        }
        context.startActivity(intent);


//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        String type = "application/vnd.android.package-archive";
//        File pluginfile = new File(filename);
//        intent.setDataAndType(Uri.fromFile(pluginfile), type);
//        if (context instanceof Activity) {
//            ((Activity) context).startActivityForResult(intent,REQUEST_INSTALL);
//        }
//        context.startActivity(intent);
    }

    public static int getApkVersion(Context context) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
        return packageInfo.versionCode;
    }
}
