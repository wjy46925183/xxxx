package gongren.com.dlg.volleyUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import gongren.com.dlg.activity.LoginDialogActivity;
import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.javabean.DlgJsonRequest;
import gongren.com.dlg.utils.ExampleUtil;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.utils.ToastUtils;

/**
 * Created by Jaelyn on 2015/12/3 0003.
 * <p/>
 * 网络请求队列
 */
public class GetDataQueue {

    private static GetDataQueue mInstance = null;
    private RequestQueue requestQueue;

    private GetDataQueue(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized GetDataQueue getInstance() {
        if (mInstance == null) {
            mInstance = new GetDataQueue(MyApplication.getInstance());
        }
        return mInstance;
    }

    private static Context context;

    public void addDataRequest(final DataRequest dataRequest, final Context context, final String httpurl, final Map<String, Object> mMap) {
        this.context = context;
        try {
            Log.e("DLG", "请求URL=" + httpurl);
            Log.e("DLG", "请求数据=" + new Gson().toJson(mMap));
            String requestBody = appendParameter(httpurl, mMap);
            DlgJsonRequest jsonObjectRequest = new DlgJsonRequest(Request.Method.POST, httpurl, requestBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        Log.e("DLG", "返回数据=" + new Gson().toJson(response));
                        dataRequest.setNetError(false);
                        dataRequest.setResponseData(response.toString());
                        dataRequest.setParams(mMap);
                        JsonMap jsonMap = JsonParseHelper.getJsonMap(response.toString());
                        if (!httpurl.equals(GetDataConfing.LOGOUT)) {
                            if (jsonMap.getStringNoNull("extra").equals("be_kicked_out") || jsonMap.getStringNoNull("code").equals("20000010")) {
                                //置空别名
                                String userId = SharedPreferencesUtils.getString(context,
                                        SharedPreferencesUtils.USERID);
                                if (StringUtils.isLogin(context)) {
                                    setAlias(userId);
                                    SharedPreferencesUtils.saveString(context,
                                            SharedPreferencesUtils.TOKEN, "");
                                    SharedPreferencesUtils.saveString(context,
                                            SharedPreferencesUtils.USERID, "");
                                    SharedPreferencesUtils.saveString(context,
                                            SharedPreferencesUtils.SESSION, "");
                                    SharedPreferencesUtils.clear(context);
                                    //取消踢出机制
                                    if (MyApplication.isLogInPage)
                                        //说明当前界面就停留在登录界面
                                        return;
                                    Intent intent = new Intent(context, LoginDialogActivity.class);
                                    context.startActivity(intent);
                                    ToastUtils.showToastLong(context, "账号被踢出登录");
                                }
                            }
                        }
                        dataRequest.getResponseDataCallback().onFinish(dataRequest);
                    } catch (Exception e) {
                        if (e != null) {
                            Log.e("===", "e---" + e.getMessage());
                            Log.e("===", e.toString());
                        }

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try {
                        dataRequest.setNetError(true);
                        dataRequest.setResponseData(error.getMessage());
                        dataRequest.getResponseDataCallback().onFinish(
                                dataRequest);
                    } catch (Exception e) {

                    }
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.logD("req data err", e.getMessage());
            dataRequest.setNetError(true);
            dataRequest.getResponseDataCallback().onFinish(dataRequest);
        }
    }

    private String appendParameter(String url, Map<String, Object> params) {
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return builder.build().getQuery();
    }

    //设置Jpush的别名
    private void setAlias(String alias) {
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            return;
        }
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    //(String) msg.obj
                    JPushInterface.setAliasAndTags(MyApplication.getInstance(), "", null, mAliasCallback);
                    break;
                default:
            }
        }
    };

    private static final String TAG = "JPush";
    private static final int MSG_SET_ALIAS = 1001;
    private static final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(context)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };

    public void cancle(Object tag) {
        requestQueue.cancelAll(tag);
    }

}
