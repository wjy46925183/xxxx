package com.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 共享参数工具类
 */
public class SharedPreferencesUtils {
    /**
     * sp的名字
     */
    public static final String SP_NAME = "myInfo";

    /**
     * 登录的token值
     */
    public static final String TOKEN = "token";

    /******
     * 登录COOKIE值
     */
    public static final String COOKIE = "Cookie";

    /**
     * 登录的session值
     */
    public static final String SESSION = "session";

    /**
     * 用户的id
     */
    public static final String ID = "id";
    /**
     * 用户的id
     */
    public static final String USERID = "loginUserId";
    /**
     * 用户的头像
     */
    public static final String FILELOGO = "fileLogo";

    /******
     * 用户验证状态
     */
    public static final String STATUS = "auditStatus";
    /**
     * 用户的名字
     */
    public static final String UserName = "UserName";

    /**
     * 是否不是第一次打开APP的boolean值
     */
    public static final String NOFIRSTIN = "noFirstIn";

    /**
     * 用户手机号
     */
    public static final String MOBILE = "mobile";
    /**
     * 用户登录密码
     */
    public static final String PASSWORD = "password";

    /**
     * 是否设置过支付密码
     */
    public static final String havePayPwd = "havePayPwd";

    /**
     * 雇主端公司名称
     */
    public static final String COMPANYNAME = "companyName";

    /**
     * 雇主端联系人名字
     */
    public static final String EMPLOYERSNAME = "employersName";

    /**
     * 雇主端公司地址
     */
    public static final String COMPANYADDRESS = "companyAddress";

    /**
     * 认证状态值，1认证中，2认证成功，3认证失败，4未认证
     */
    public static final String RENZHENG_STATUS = "RENZHENG_STATUS";

    /**
     * 定位后的纬度latitude
     */
    public static final String latitude = "latitude";

    /**
     * 定位后的经度longitude
     */
    public static final String longitude = "longitude";

    /**
     * 定位后的省
     */
    public static final String locationProvince = "locationProvince";

    /**
     * 定位后的城市
     */
    public static final String locationCity = "locationCity";

    /**
     * 定位后的区
     */
    public static final String locationDistrict = "locationDistrict";

    /**
     * 定位后的地址
     */
    public static final String locationAddress = "locationAddress";

    /**
     * 定位后区的id
     */
    public static final String AdCode = "AdCode";

    /**
     * 历史搜索的地址
     */
    public static final String historyAddress = "historyAddress";

    /**
     * 保存支付方式
     */
    public static final String PayType = "PayType";

    /**
     * COOKIE保存
     */
    public static final String COOKIE_NAME = "Cookie";
    /*****
     * 角色
     */
    public static final String UserRole = "role";
    /**
     * 身份证
     */
    public static final String CERTIFICATENUMBER = "certificateNumber";
    /**
     * 用户的身份证 真实姓名
     */
    public static final String REAL_NAME = "name";

    /**
     * 保存String类型
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean saveString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * 清空所有的APP记录
     * @param context
     */
    public static void clearSharePreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear().commit();
    }
    /**
     * 保存int类型
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean saveInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * 保存boolean类型
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean saveBoolean(Context context, String key, Boolean value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * 获取String类型
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if(sp.contains(key))
            return sp.getString(key, "");
        else
            return "";
    }

    /**
     * 获取int类型
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    /**
     * 获取boolean类型
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * 清空
     * @param context
     */
    public static void clear(Context context){
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.FILELOGO, "");
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.USERID, "");
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.MOBILE, "");
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.TOKEN, "");
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.PASSWORD, "");
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.RENZHENG_STATUS, "");
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.STATUS, "");
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.EMPLOYERSNAME, "");
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.COMPANYADDRESS, "");
//        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.UserRole, "");
        SharedPreferencesUtils.saveString(context,SharedPreferencesUtils.CERTIFICATENUMBER,"");
        SharedPreferencesUtils.saveString(context,SharedPreferencesUtils.REAL_NAME,"");
        SharedPreferencesUtils.saveString(context,SharedPreferencesUtils.UserName,"");
        SharedPreferencesUtils.saveString(context,SharedPreferencesUtils.SESSION,"");
    }

}
