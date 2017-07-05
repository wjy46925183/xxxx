package gongren.com.dlg.volleyUtils;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.CookieManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lenovo on 2017/4/17.
 */
public class JsonObjectPostRequest extends Request<JSONObject> {
    /**
     * 请求的标记
     */
    public int what;
    /**
     * 服务器地址
     */
    private String ip;
    /**
     * 方法名字
     */
    private String actionName;
    /**
     * 请求参数
     */
    private Map<String, Object> params;

    private Map<String, String> mMap;
    private Response.Listener<JSONObject> mListener;
    public String cookieFromResponse;
    private String mHeader;
    private Map<String, String> sendHeader=new HashMap<String, String>(1);

    public JsonObjectPostRequest(String url, Response.Listener<JSONObject> listener,
                           Response.ErrorListener errorListener, Map map) {

        super(Method.POST, url, errorListener);
        mListener = listener;
        mMap = map;
    }

    //当http请求是post时，则需要该使用该函数设置往里面添加的键值对
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }
    private ResponseDataCallback mRequestCallback;
    public void setResponseDataCallback(ResponseDataCallback responseDataCallback){
        this.mRequestCallback = responseDataCallback;
    }
    public ResponseDataCallback getResponseDataCallback() {
        return mRequestCallback;
    }
    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            // 参数的转换
            byte[] bodyBytes = JsonObjectPostRequest.encryptParams(mMap);
            return bodyBytes ;
        } catch (Throwable e){
        }
        return super.getBody();
    }

    /**
     * 对post请求数据进行加密
     * @param params
     * @return
     * @throws Throwable
     */
    public static byte[] encryptParams(Map<String, String> params) throws Throwable{
        if (params == null){
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (stringBuilder.length() > 0){
                stringBuilder.append("&");
            }
            stringBuilder.append(key).append("=").append(Uri.encode(value));
        }

        byte[] buff = stringBuilder.toString().getBytes("utf-8");

        return buff;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            mHeader = response.headers.toString();
            JSONObject jsonObject = new JSONObject(jsonString);

            for(Map.Entry<String,String> entry : response.headers.entrySet()) {
                if("Set-Cookie".equalsIgnoreCase(entry.getKey())) {
                    //将cookie保存到CookieManager
                    CookieManager.getInstance().setCookie(GetDataConfing.BASEURL, entry.getValue());
                    break;
                }
            }
            return Response.success(jsonObject,
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return sendHeader;
    }

    public void setSendCookie(String cookie){
        sendHeader.put("Cookie",cookie);
    }
    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    private String responseData;
    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
    /**
     * 标记网络错误
     */
    private boolean isNetError = false;

    public boolean isNetError() {
        return isNetError;
    }

    public void setNetError(boolean isNetError) {
        this.isNetError = isNetError;
    }
    /**
     * 重写父类的获取信息的方法 当含有&lt;string> &lt;/string>时去掉前后的&lt;string> &lt;/string>
     */
    public String getInfo() {
        if (TextUtils.isEmpty(responseData)) {
            int startIndex = responseData.indexOf("{");
            int endIndex = responseData.lastIndexOf("}");
            responseData = responseData.substring(startIndex, endIndex + 1);
        }
        return responseData;
    }
}