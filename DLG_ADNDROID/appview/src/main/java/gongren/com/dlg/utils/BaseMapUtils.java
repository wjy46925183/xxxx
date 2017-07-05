package gongren.com.dlg.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取map集合的工具类
 */
public class BaseMapUtils {

    /**
     * 获取请求参数的map集合
     *
     * @return
     */
    public static Map<String, Object> getMap(Context context) {
        Map<String, Object> map = new HashMap<>();
//        map.put("_version", StringUtils.getVersionName(context));
//        map.put("_appid", "1001");
        map.put("format", "json");
//        map.put("_token", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.TOKEN));
//        map.put("_ts", System.currentTimeMillis() + "");
        return map;
    }

}
