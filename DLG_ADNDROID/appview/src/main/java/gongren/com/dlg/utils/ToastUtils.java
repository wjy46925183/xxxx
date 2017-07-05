package gongren.com.dlg.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast工具类
 */
public class ToastUtils {

    public static Toast toast;

    public static void showToastShort(Context context, String msg) {
        CToast cToast = CToast.makeText(context,msg,1000);
        cToast.setGravity(Gravity.CENTER,0,200);
        cToast.show();
    }

    public static void showToastLong(Context context, String msg) {
        CToast cToast = CToast.makeText(context,msg,1000);
        cToast.show();
    }

    public static void showToastShortCenter(final Context context, String msg) {
        CToast cToast = CToast.makeText(context,msg,1000);
        cToast.show();
    }
}
