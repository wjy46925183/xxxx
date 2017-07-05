package gongren.com.dlg.utils;

import android.content.Context;

import com.common.utils.SharedPreferencesUtils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataQueue;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;

/**
 * 数据请求工具类
 */
public class DataUtils {

    /**
     * 数据请求封装工具类
     * @param context
     * @param url
     * @param map
     * @param responseDataCallback
     */
    public static GetDataQueue loadData(Context context, String url, Map<String, Object> map, int tag,
                                        ResponseDataCallback responseDataCallback) {

        map.put("format","json");
        map.put("userId", SharedPreferencesUtils.getString(context,SharedPreferencesUtils.USERID)+"");
        DataRequest dataRequest = new DataRequest();
        dataRequest.setResponseDataCallback(responseDataCallback);
        dataRequest.setWhat(tag);
        dataRequest.setParams(map);//有时候，需要在回调的地方取到参数里的某个值。
        GetDataQueue getDataQueue = GetDataQueue.getInstance();
        getDataQueue.addDataRequest(dataRequest, context, url, map);
        return getDataQueue;
    }

    /**
     * 数据请求封装工具类
     * @param context
     * @param url
     * @param map
     * @param responseDataCallback
     */
    public static GetDataQueue loadData2(Context context, String url, Map<String, Object> map, int tag,
                                        ResponseDataCallback responseDataCallback) {

        map.put("format","json");
        DataRequest dataRequest = new DataRequest();
        dataRequest.setResponseDataCallback(responseDataCallback);
        dataRequest.setWhat(tag);
        dataRequest.setParams(map);//有时候，需要在回调的地方取到参数里的某个值。
        GetDataQueue getDataQueue = GetDataQueue.getInstance();
        getDataQueue.addDataRequest(dataRequest, context, url, map);
        return getDataQueue;
    }

    /**
     * 数据请求封装工具类
     * @param context
     * @param url
     * @param map
     * @param responseDataCallback
     */
    public static GetDataQueue loadData(Context context, String url, Map<String, Object> map,
                                        ResponseDataCallback responseDataCallback) {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setResponseDataCallback(responseDataCallback);
        dataRequest.setParams(map);//有时候，需要在回调的地方取到参数里的某个值。
        GetDataQueue getDataQueue = GetDataQueue.getInstance();
        getDataQueue.addDataRequest(dataRequest, context, url, map);
        return getDataQueue;
    }
    public static GetDataQueue uploadImage(String url,File file, int tag,
                                           ResponseDataCallback responseDataCallback){
        HashMap<String,Object> map=new HashMap<>();
        map.put("base64Str",getImageStr(file));
        map.put("img",getImageStr(file));
        return loadData(null,url,map,tag,responseDataCallback);
    }

    private static String getImageStr(File file){
        byte[] data =null;
        try {
            FileInputStream fileInputStream=new FileInputStream(file);
            data=new byte[fileInputStream.available()];
            fileInputStream.read(data);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(data!=null){
            return android.util.Base64.encodeToString(data, android.util.Base64.DEFAULT);
        }
        return "";
    }

    /**
     * NumberFormat is the abstract base class for all number formats.
     * This class provides the interface for formatting and parsing numbers.
     * @param d
     * @return
     */
    public static String formatDouble(double d) {
        NumberFormat nf = NumberFormat.getNumberInstance();


        // 保留两位小数
        nf.setMaximumFractionDigits(2);


        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.UP);


        return nf.format(d);
    }

    //判断是否是数字
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static boolean isNullStr(String str){
        if(str==null||str.equals("")){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 字符串转int
     * @param str
     * @return
     */
    public static int string2Int(String str){
        if(isNullStr(str)){
            return 0;
        }
        int result = 0;
        try {
            result = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 按照指定的符号，去切分字符串。
     * @param oldstr 要切分的字符串
     * @param split 分隔符
     * @return
     */
    public static String[] splitSpecialString(String oldstr,String split){
        if(isNullStr(oldstr)||isNullStr(split)){
            return null;
        }

        if(oldstr.indexOf(split)!=-1){
            return oldstr.split(split);
        }else{
            return null;
        }
    }

    /**
     * 从double中四舍五入取整
     * @param old
     * @return
     */
    public static int getIntFromDouble(double old){
        int result = 0;
        BigDecimal bigde = new BigDecimal(String.valueOf(old)).setScale(0, BigDecimal.ROUND_HALF_UP);
        result = bigde.intValue();
        return result;
    }

//    public static void upload(File file, String url){
//        RequestParams params = new RequestParams(url);
//        params.setMultipart(true);
//        params.addBodyParameter("base64Str",file," multipart/form-data");
//        params.addParameter("userId","806020061826519040");
//        x.http().post(params, new Callback.CacheCallback<Object>() {
//            @Override
//            public void onSuccess(Object result) {
//
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//
//            @Override
//            public boolean onCache(Object result) {
//                return false;
//            }
//        });
//    }
}
