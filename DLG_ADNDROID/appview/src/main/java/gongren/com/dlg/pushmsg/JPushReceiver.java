package gongren.com.dlg.pushmsg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.common.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import gongren.com.dlg.activity.MainActivity;
import gongren.com.dlg.activity.WorkOrderDetailActivity;
import gongren.com.dlg.pushmsg.model.PushExtraData;
import gongren.com.dlg.utils.ActivityController;
import gongren.com.dlg.utils.IflyTekUtils;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        Bundle bundle = intent.getExtras();

        Log.d(TAG, "[JPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);
            for (String key : bundle.keySet()) {
                if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                    String extraData=bundle.getString(JPushInterface.EXTRA_EXTRA);
                    Log.i(TAG, "extraData："+extraData);
                    if (TextUtils.isEmpty(extraData)) {
                        Log.i(TAG, "This message has no Extra data");
                        break;
                    }
                    try {
                        Gson gson = new Gson();
                        PushExtraData pushExtraData = gson.fromJson(extraData, PushExtraData.class);
                        if(pushExtraData.orderStatus == 10 || pushExtraData.orderStatus == 40 ){
                            String string = bundle.getString(JPushInterface.EXTRA_ALERT);//有人接单的文字
                            IflyTekUtils.playIfly(string,ActivityController.getLastActivity());//语音播报
                        }
                    } catch (Exception e) {
                        Log.i(TAG, "通知消息事件分发出现异常:"+e.getMessage());
                    }
                    break;
                }
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            parseExtraData(bundle);
        }
    }

    /**
     * 解析协议数据，协议数据放在EXTRA_EXTRA里面
     *
     * @param bundle
     */
    private void parseExtraData(Bundle bundle) {
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                String extraData=bundle.getString(JPushInterface.EXTRA_EXTRA);
                Log.i(TAG, "extraData："+extraData);
                if (TextUtils.isEmpty(extraData)) {
                    Log.i(TAG, "This message has no Extra data");
                    break;
                }
                try {
                    Gson gson = new Gson();
                    PushExtraData pushExtraData = gson.fromJson(extraData, PushExtraData.class);
                    dispenseEvent(pushExtraData);
                } catch (Exception e) {
                    Log.i(TAG, "通知消息事件分发出现异常:"+e.getMessage());
                }
                break;
            }
        }
    }

    /**
     * 分发通知栏操作事件
     *
     * @param pushExtraData
     */
    private void dispenseEvent(PushExtraData pushExtraData) {
        switch (pushExtraData.toRoleType) {
            case 1://跳转订单详情
                WorkOrderDetailActivity.open(ActivityController.getLastActivity(),pushExtraData.businessNumber);
                break;
            case 2:
                SharedPreferencesUtils.saveString(context.getApplicationContext(), SharedPreferencesUtils.UserRole,"2");
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("isLogout",true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                break;
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    //自定义的key取法
                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

//	//send msg to MainActivity
//	private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
//	}
}
