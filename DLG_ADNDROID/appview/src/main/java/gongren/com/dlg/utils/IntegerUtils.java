package gongren.com.dlg.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gongren.com.dlg.alibaba.cloudapi.Base64Encoder;

/**
 * 字符串工具类
 */
public class IntegerUtils {

    //验证手机号
    public static final int API_TAG_QUERY = 1001;
    //账号密码登录
    public static final int API_PSD_LOGIN = 1002;
    //获取验证码
    public static final int API_GET_VERCODE = 1003;
    //验证码校验
    public static final int API_CHECK_CODE = 1004;
    //注册
    public static final int API_USER_REGISTER = 1005;
    //修改密码
    public static final int API_MODIFY_PWD = 1006;
    //工种查询
    public static final int API_COMMON_CRAFT_LIST = 1007;
    //热门搜索
    public static final int API_HOT_CRAFT_LIST = 1008;
    //是否有进行中任务
    public  static final int API_TAG_WORK_TASKING = 1009;
    //雇主是否有进行中的任务
    public  static final int API_TAG_BOSS_TASKING = 1010;
    //首页-地图(雇员显示列表数据)
    public static final int API_WORK_MAP_LIST = 1011;
    //首页-地图(（RPC）查询任务详情)
    public static final int API_WORK_MAP_DETAIL = 1012;
    //首页-地图(雇主显示列表数据)
    public static final int API_BOSS_MAP_LIST = 1013;
    //首页-地图(（RPC）雇员详情)
    public static final int API_BOSS_MAP_DETAIL = 1014;
    //获取正在进行中任务（雇员）
    public static final int API_WORKER_TASKING_DETAIL = 1015;
    //更新用户资料信息
    public static final int API_UPDATE_INFORMATION = 1016;
    //未读信息总条数
    public static final int API_UNREAD_MESSAGE = 1017;
    //1系统消息
    public static final int API_SYSTEM_MESSAGE = 1018;
    //活动推广
    public static final int API_ACTIVITY_MESSAGE= 1019;
    //零工推荐
    public static final int API_RECOMMEND_MESSAGE = 1020;
    //消息列表详情
    public static final int API_MESSAGE_LISTDETAIL = 1021;
    //消息列表详情
    public static final int API_MESSAGE_DETAIL = 1022;
    //1.1.6（REST）首页-雇主-有人接单、等待验收-列表
    public  static final int API_TAG_BOSS_TASKING_LIST = 1023;
    //4.2.2(REST)查询订单操作配置
    public  static final int ORDER_FLOW_CONFIG_REST = 1024;
    //零工搜索
    public static final int SELECT_WORKER = 100001;
    //雇主选择订单信息
    public static final int BOSSORDER_SELECT = 100002;
    //雇员展开雇主任务
    public static final int WORKORDER_SHOW = 100003;
    //消息列表详情
    public static final int MESSAGE_LIST_DETAIL = 100004;

}
