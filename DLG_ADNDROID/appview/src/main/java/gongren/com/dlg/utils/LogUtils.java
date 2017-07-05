package gongren.com.dlg.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Log工具类
 */
public class LogUtils {

    public static boolean isDebug = true;

    public static void logD(String tag, String msg) {
        if (LogUtils.isDebug) {
            if (!TextUtils.isEmpty(msg)) {
                Log.d(tag, msg);
            } else {
                Log.d(tag, "打印内容为空");
            }
        }
    }

    public static void logE(String tag, String msg) {
        if (LogUtils.isDebug) {
            if (!TextUtils.isEmpty(msg)) {
                Log.e(tag, msg);
            } else {
                Log.e(tag, "打印内容为空");
            }
        }
    }
}
