package gongren.com.dlg.utils;

import android.content.Context;

import com.sobot.chat.SobotApi;
import com.sobot.chat.api.model.Information;

import gongren.com.dlg.javabean.UserInfoSobot;

/**
 * 开启人工服务工具类
 */
public class SobotUtils {

    public static final String APPKEY = "cc934d7bb14e45ab80b0fb806cc0cf3f";

    public static void startSobot(Context context, UserInfoSobot userInfoSobot) {
        Information info = new Information();
        info.setAppKey(APPKEY);/* 必填 */
        /* 选填，默认为"#09aeb0". 可以设置头部背景，提交评价背景，相似问题字体颜色和富文本类型中“阅读全文”字体颜色*/
        info.setColor("#272526");
        //设置是否会话保持
        info.setBackOrClose(false);
        if (userInfoSobot != null) {
            /* 选填，设置用户唯一标识 */
            info.setUid(userInfoSobot.getUserId());
            //设置用户昵称
            info.setUname(userInfoSobot.getNickName());
            //设置头像
            info.setFace(userInfoSobot.getLogo());
            info.setPhone(userInfoSobot.getPhone());/* 用户电话，选填 */
            info.setEmail(userInfoSobot.getEmail());/* 用户邮箱，选填 */
        }
        SobotApi.startSobotChat(context, info);
    }
}
