package gongren.com.dlg.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GsonUtils {

    /**
     * @param @param  jsonResult
     * @param @param  clz
     * @param @return 设定文件
     * @return T    返回类型
     * @throws
     * @Title: jsonToBean
     * @Description: 解析JSON字符串
     */
    public static <T> T jsonToBean(String jsonResult, Class<T> clz) {
        Gson gson = new Gson();
        T t = gson.fromJson(jsonResult, clz);
        return t;
    }


    public static <T> T getResultJson(String jsonResut, Class<T> clazz) {
        if (jsonResut != null) {
            String json = beforeParser(jsonResut);
            if (!TextUtils.isEmpty(json))
                return GsonUtils.jsonToBean(json, clazz);
        }

        return null;
    }

    @SuppressWarnings("unused")
    private static String beforeParser(String jsonResut) {
        int retcode;
        try {
            JSONObject jsonObject = new JSONObject(jsonResut);
            retcode = jsonObject.getInt("Root");
            return jsonResut;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param <T>
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: stringToGson
     * @Description: 将List转换成JSON数据
     */
    public static String listToGson(List<String> lsit) {
        Gson gson = new Gson();
        List<String> persons = new ArrayList<String>();
        String str = gson.toJson(persons);
        return str;
    }

    /**
     * @param @param  str
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: stringToGson
     * @Description: 将数组转换成JSON数据
     */
    public static String arrayToGson(String[] str) {
        Gson gson = new Gson();
        String strJson = gson.toJson(str);
        return strJson;
    }

    // 将Json数据解析成相应的映射对象
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }

    // 将Json数组解析成相应的映射对象列表
    public static <T> List<T> parseJsonArrayWithGson(String jsonData,
                                                     Class<T> type) {
        Gson gson = new Gson();
        List<T> result = gson.fromJson(jsonData, new TypeToken<List<T>>() {
        }.getType());
        return result;
    }
}
