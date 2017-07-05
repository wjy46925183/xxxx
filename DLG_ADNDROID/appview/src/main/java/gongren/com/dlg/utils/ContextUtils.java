package gongren.com.dlg.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 *
 */
public class ContextUtils {

    private static LayoutInflater inflater;

    public static View inflate(Context context, int resId) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
        return inflater.inflate(resId, null);
    }

    /**
     * 获取屏幕宽
     *
     * @param context
     * @return
     */
    public static int getSreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高
     *
     * @param context
     * @return
     */
    public static int getSreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
