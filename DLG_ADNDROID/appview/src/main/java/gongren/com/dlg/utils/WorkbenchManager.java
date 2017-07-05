package gongren.com.dlg.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.amap.api.services.core.LatLonPoint;
import com.common.sys.SystemUtil;
import com.common.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.javabean.DoingTaskOrderDetailModel;
import gongren.com.dlg.javabean.NearbyTask;
import gongren.com.dlg.javabean.TaskStartWorkTimeModel;
import gongren.com.dlg.javabean.base.ButtonListBean;
import gongren.com.dlg.javabean.worker.WorkerStatusBean;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.RequestCallback;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;

/**
 * 工作台数据管理类
 * Created by Administrator on 2017/4/18 0018.
 */
public class WorkbenchManager {
    private static WorkbenchManager instance = null;
    private static Context context = null;
    public final static int TAG_REQUST_TASK_BUTTONS = 0x60002;
    public final static int TAG_REQUST_TASK_TIME = 0x60004;
    public final static int TAG_REQUST_BUTTON_EVENT = 0x60006;
    public static final int TAG_GET_TASK_DETAIL = 0x600008;

    /******
     * 初始化实例
     * @param mContext
     * @return
     */
    public static WorkbenchManager getInstance(Context mContext) {
        if (instance == null) {
            instance = new WorkbenchManager();
        }
        context = mContext;

        return instance;
    }

    /*******
     * 获取地图数据
     * @param latLonMapCenterPoint
     * @param postType
     * @param demandType //需求类型 //1.工作日,2.双休日,3.计件
     * @param requestCallback
     * liukui 2017/04/25
     */
    public static void getMapDatas(LatLonPoint latLonMapCenterPoint, String postType, final String demandType, final RequestCallback<DataRequest> requestCallback) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        if(null == latLonMapCenterPoint){
            map.put("xCoordinate", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.longitude));
            //y坐标(维度)
            map.put("yCoordinate", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.latitude));
        }else {
            map.put("xCoordinate", latLonMapCenterPoint.getLongitude());
            //y坐标(维度)
            map.put("yCoordinate", latLonMapCenterPoint.getLatitude());
        }

        //岗位类型
        map.put("postType", postType);
        //需求类型 //1.工作日,2.双休日,3.计件
        map.put("demandType", demandType);
        map.put("format", "json");
        DataUtils.loadData(context, GetDataConfing.WORK_MAP_LIST, map, IntegerUtils.API_WORK_MAP_LIST, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                try {
                    if (dataRequest.getResponseData() != null) {
                        JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());
                        if ("0".equals(jsonObject.optString("code"))) {
                            requestCallback.onSuccess(dataRequest);
                        } else if ("20000010".equals(jsonObject.optString("code"))) {
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取雇员工作台 按钮文本及状态码 剩余时间
     * @param businessNumber
     * @param requestCallback
     */
    public static void getTaskButtons(String businessNumber, final RequestCallback<WorkerStatusBean> requestCallback) {
        Map<String, Object> map = new HashMap<>();
        map.put("format", "json");
        map.put("businessNumber", businessNumber);
        DataUtils.loadData(context, GetDataConfing.get_task_buttons, map, TAG_REQUST_TASK_BUTTONS, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    requestCallback.onError("网络异常");
                } else {
                    try {
                        String responseData = dataRequest.getResponseData();
                        JSONObject jsonObject = new JSONObject(responseData);
                        if("0".equals(jsonObject.optString("code"))){
                            WorkerStatusBean workerStatusBean = new Gson().fromJson(responseData, WorkerStatusBean.class);
                            requestCallback.onSuccess(workerStatusBean);
                        }else {
                            requestCallback.onError("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //获取开工/完工/收工时间
    public static void getTaskTime(String businessNumber, final RequestCallback<TaskStartWorkTimeModel> requestCallback) {
        Map<String, Object> map = new HashMap<>();
        map.put("businessNumber", businessNumber);
        map.put("format", "json");
        DataUtils.loadData(context, GetDataConfing.get_trask_time, map, TAG_REQUST_TASK_TIME, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    requestCallback.onError("网络异常");
                } else {
                    JSONObject jsonObject = null;
                    try {
                        Gson gson = new Gson();
                        jsonObject = new JSONObject(dataRequest.getResponseData());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        TaskStartWorkTimeModel taskStartWorkTimeModel = gson.fromJson(jsonArray.getString(0), TaskStartWorkTimeModel.class);
                        requestCallback.onSuccess(taskStartWorkTimeModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 发送操作按钮点击请求 包括拒绝邀请
     */
    public static void postBtnEvent(String businessNumber, WorkerStatusBean.DataBean dataBean, final RequestCallback<Boolean> requestCallback) {
        postBtnEvent(businessNumber,dataBean.buttonList.get(0),requestCallback);
    }

    /**
     * 发送操作 只有同意
     */
    public static void postBtnEvent(String businessNumber,ButtonListBean nextStatusCode, final RequestCallback<Boolean> requestCallback) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("operaStatus", nextStatusCode.operateStatusCode);
        map.put("nextStatusCode", nextStatusCode.nextStatusCode);
        map.put("businessNumber", businessNumber);
        map.put("userVo", SharedPreferencesUtils.getString(MyApplication.instance, SharedPreferencesUtils.USERID));
        map.put("format", "json");
        DataUtils.loadData(context, GetDataConfing.post_task_button_event, map, TAG_REQUST_BUTTON_EVENT, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    requestCallback.onError("网络异常");
                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(dataRequest.getResponseData());
                        if (jsonObject.getString("code").equals("0")) {
                            requestCallback.onSuccess(true);
                        } else {
                            requestCallback.onError(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取正在进行中的任务
     */
    public static void getDoingTaskDetail(final RequestCallback<List<DoingTaskOrderDetailModel>> requestCallback) {
        Map<String, Object> map = new HashMap<>();
        map.put("format", "json");
        DataUtils.loadData(context, GetDataConfing.get_doing_task_detail, map, TAG_GET_TASK_DETAIL, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    requestCallback.onError("网络异常");
                } else {
                    JSONObject jsonObject = null;
                    try {
                        Gson gson = new Gson();
                        jsonObject = new JSONObject(dataRequest.getResponseData());
                        if(jsonObject.getInt("code") != 0){
                            requestCallback.onError(jsonObject.getString("msg"));
                        }else {
                            String jsonArray = jsonObject.getString("data");
                            List<DoingTaskOrderDetailModel> doingTaskOrderDetailModel = gson.fromJson(jsonArray, new TypeToken<ArrayList<DoingTaskOrderDetailModel>>() {}.getType());
                            requestCallback.onSuccess(doingTaskOrderDetailModel);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
//                        requestCallback.onError("数据解析异常");
                    }
                }
            }
        });
    }

    /**
     * 根据订单id获取任务订单详情
     */
    public static void getDoingTaskDetailById(String businessNumber, final RequestCallback<DoingTaskOrderDetailModel> requestCallback) {
        Map<String, Object> map = new HashMap<>();
        map.put("format", "json");
        map.put("businessNumber", businessNumber);
        DataUtils.loadData(context, GetDataConfing.get_task_detail_by_id, map, TAG_GET_TASK_DETAIL, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    requestCallback.onError("网络异常");
                } else {
                    JSONObject jsonObject = null;
                    try {
                        Gson gson = new Gson();
                        jsonObject = new JSONObject(dataRequest.getResponseData());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        DoingTaskOrderDetailModel doingTaskOrderDetailModel = gson.fromJson(jsonArray.getString(0), DoingTaskOrderDetailModel.class);
                        requestCallback.onSuccess(doingTaskOrderDetailModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /*****
     * 查看是否有进行中的订单
     */
    public static void loadHasDoingTask(final RequestCallback<Boolean> requestCallback) {
        Map<String, Object> map = new HashMap<>();
        map.put("format", "json");
        DataUtils.loadData(context, GetDataConfing.WORKER_IS_TASKING, map, TAG_GET_TASK_DETAIL, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    requestCallback.onError("网络异常");
                } else {
                    JSONObject jsonObject = null;
                    try {
                        Gson gson = new Gson();
                        jsonObject = new JSONObject(dataRequest.getResponseData());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Boolean hasDoingTask = gson.fromJson(jsonArray.getString(0), Boolean.class);
                        requestCallback.onSuccess(hasDoingTask);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /*****
     * 获取附近的任务列表
     */
    public static void getNearTaskList(LatLonPoint latLonPoint, String postType, String demandType, final RequestCallback<List<NearbyTask>> requestCallback) {
        Map<String, Object> map = new HashMap<>();
        // x坐标(经度)
        map.put("xCoordinate", String.valueOf(latLonPoint.getLongitude()));
        //y坐标(维度)
        map.put("yCoordinate", String.valueOf(latLonPoint.getLatitude()));
        //岗位类型
        map.put("postType", postType);
        //需求类型 //1.工作日,2.双休日,3.计件
        map.put("demandType", demandType);
        map.put("format", "json");
        DataUtils.loadData(context, GetDataConfing.WORK_MAP_LIST, map, IntegerUtils.API_WORK_MAP_LIST, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    requestCallback.onError("网络异常");
                } else {
                    JSONObject jsonObject = null;
                    try {
                        Gson gson = new Gson();
                        List<NearbyTask> list = new ArrayList<>();
                        jsonObject = new JSONObject(dataRequest.getResponseData());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            list.add(gson.fromJson(jsonArray.getString(i), NearbyTask.class));
                        }
                        requestCallback.onSuccess(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void striveForOrder(String jobId, final RequestCallback<Boolean> requestCallback) {
        Map<String, Object> map = new HashMap<>();
        map.put("jobId", jobId);
        map.put("employeeId", SharedPreferencesUtils.getString(MyApplication.instance, SharedPreferencesUtils.USERID));
        map.put("format", "json");
        DataUtils.loadData(context, GetDataConfing.post_strive_for_order, map, 102223666, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    requestCallback.onError("网络异常");
                } else {
                    JSONObject jsonObject = null;
                    try {
                        Gson gson = new Gson();
                        List<NearbyTask> list = new ArrayList<>();
                        jsonObject = new JSONObject(dataRequest.getResponseData());
                        String code = jsonObject.getString("code");
                        if (code.equals("0")) {
                            requestCallback.onSuccess(true);
                            ToastUtils.showToastShort(context, jsonObject.optString("msg"));
                        } else {
                            requestCallback.onError(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void addUserLogin() {
        Map<String, Object> map = new HashMap<>();

        String x = SharedPreferencesUtils.getString(MyApplication.getInstance(), SharedPreferencesUtils.longitude);
        String y = SharedPreferencesUtils.getString(MyApplication.getInstance(), SharedPreferencesUtils.latitude);
        map.put("xCoordinate", x);
        map.put("yCoordinate", y);
        map.put("appId", "1");
        map.put("format","json");
        map.put("appVersion", SystemUtil.getVersionName(context));
        map.put("loginIp", getIPAddress(context));
        DataUtils.loadData(context, GetDataConfing.SYSTEM_ADDUSERMESG, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
            }
        });
    }

    /**
     * 获取ip
     *
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }
    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public interface StringCallBack{
        void onFinish(String json);
    }
    //获取分享成功接口 回调数据
    public static void getShareData(Context context, String url,String jobId, final StringCallBack callback){

        Map<String,Object> map = new HashMap<>();
        map.put("taskId",jobId);
        map.put("format","json");
        DataUtils.loadData(context, url, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                String responseData = dataRequest.getResponseData();
                callback.onFinish(responseData);
            }
        });
    }

    public static void getBossReleaseOrders(){
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("status", "0");
        map.put("pageSize", "30");
        map.put("minId", "0");
        DataUtils.loadData(context, GetDataConfing.getJobTaskOfNotUsedPage, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {

            }
        });
    }
}
