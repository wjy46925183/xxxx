package gongren.com.dlg.javabean;

import android.content.Context;

import java.util.Map;

import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.IntegerUtils;
import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;

/**
 * Created by 施杰 on 2017/4/11.
 * 获取未读消息总数，获取消息列表
 */

public class UserMessageModel {



    /**
     * 获取未读消息总数
     *
     * @param context
     * @param responseDataCallback
     */
    public void queryUnReadMessageCounts(Context context,int type, ResponseDataCallback responseDataCallback) {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context,SharedPreferencesUtils.USERID));
        DataUtils.loadData(context, GetDataConfing.unReadMessageCounts, map, type, responseDataCallback);
    }

    /**
     * 获取消息列表
     *
     * messageType:”1”//消息类型(1.系统消息,2.活动推广,3.零工推荐)
     isRead ： //是否阅读(0未读，1已读)
     pageSize:”10”//条数
     minId:”0”// 起始页码
     *
     ****/
    public void getMessageListByType(Context context, int type,String isRead,int startNum,int pageSize, ResponseDataCallback responseDataCallback) {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(context,SharedPreferencesUtils.USERID));
        switch (type){
            case IntegerUtils.API_SYSTEM_MESSAGE:
                map.put("messageType", String.valueOf(1));
            break;

            case IntegerUtils.API_ACTIVITY_MESSAGE:
                map.put("messageType", String.valueOf(2));
            break;
            case IntegerUtils.API_RECOMMEND_MESSAGE:
                map.put("messageType", String.valueOf(3));
            break;
        }
        if(isRead!=null&&!isRead.equals(""))
            map.put("isRead", String.valueOf(isRead));
        map.put("pageSize", 1000);
        map.put("minId", String.valueOf(startNum));
        DataUtils.loadData(context, GetDataConfing.userSystemMessage, map, type, responseDataCallback);
    }

    /******
     *消息列表详情
     * @param context
     * @param id
     * @param responseDataCallback
     */
    public void getMessageListDeatil(Context context, String id,int type, ResponseDataCallback responseDataCallback) {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("id", id);
        map.put("userId", SharedPreferencesUtils.getString(context,SharedPreferencesUtils.USERID));
        DataUtils.loadData(context, GetDataConfing.messageListDetail, map, type, responseDataCallback);
    }

    /*******
     * 用户消息详情
     * @param context
     * @param id
     * @param type
     * @param responseDataCallback
     */
    public void getMessageDeatil(Context context, String id,int type, ResponseDataCallback responseDataCallback) {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("messageId", id);
        map.put("userId", SharedPreferencesUtils.getString(context,SharedPreferencesUtils.USERID));
        DataUtils.loadData(context, GetDataConfing.messageDetail, map, type, responseDataCallback);
    }

    /**
     * 更新消息 isRead 1 已阅读
     *
     * @param context
     * @param responseDataCallback
     */
    public void updateMessage(Context context, String id, ResponseDataCallback responseDataCallback) {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("id", id);
        map.put("isRead", "1");
        DataUtils.loadData(context, GetDataConfing.messageUpdate, map, 0x50, responseDataCallback);
    }

}
