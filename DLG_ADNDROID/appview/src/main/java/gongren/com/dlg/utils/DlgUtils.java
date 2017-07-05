package gongren.com.dlg.utils;

import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class DlgUtils {

    //电话数字中间遮蔽
    public static String showMidHintPhone(String p) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (i >= 3 && i <= 6) {
                sb.append('*');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
