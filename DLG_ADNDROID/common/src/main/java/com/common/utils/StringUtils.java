package com.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串工具类
 */
public class StringUtils {
    private Context context;
    public static String imgurl1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492012568762&di=d7b71b4a6e59d9528b52d6b73152d878&imgtype=0&src=http%3A%2F%2Fwww.pp3.cn%2Fuploads%2F201412%2F2014122528.jpg";
    public static String imgurl2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492012615191&di=2abaa8ef8ac8231cec47c6dcd6dccbb1&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F15%2F36%2F01%2F11y58PICHjw_1024.jpg";

    public StringUtils(Context context) {
        this.context = context;
    }

    /**
     * 手机号码的格式验证
     *
     * @param
     * @return
     */
    public static boolean isPhoneNumber(String mobiles) {
        Pattern p = Pattern.compile("((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 邮箱的格式验证
     *
     * @param
     * @return
     */
    public static boolean isEmail(String line) {
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(line);
        return m.matches();
    }


    /**
     * 判断是否是密码
     *
     * @param password
     * @return
     */
    public static boolean isPassWord(String password) {
        if (password.length() > 5 && password.length() < 15)
            return true;
        else
            return false;
    }

    /**
     * 判断验证码
     *
     * @param verifyCode
     * @return
     */
    public static boolean isVerifyCode(String verifyCode) {
        Pattern p = Pattern
                .compile("[0-9]{4}");
        Matcher m = p.matcher(verifyCode);
        return m.matches();
    }

    /**
     * 账号的格式验证
     *
     * @param
     * @return不能用中文的汉字和符号!
     */
    public static boolean isHaveChineseChar(String str) {
        if (str.length() == str.getBytes().length) {
            return true;
        }
        return false;
    }

    /**
     * 验证银卡卡号
     *
     * @param cardNo
     * @return
     */
    public static boolean isBankCard(String cardNo) {
        Pattern p = Pattern.compile("^\\d{16,19}$|^\\d{6}[- ]\\d{10,13}$|^\\d{4}[- ]\\d{4}[- ]\\d{4}[- ]\\d{4,7}$");
        Matcher m = p.matcher(cardNo);
        return m.matches();
    }

    /**
     * 验证身份证号是否符合规则
     *
     * @param text 身份证号
     * @return
     */
    public static boolean personIdValidation(String text) {
        return Pattern.matches("^(\\d{14}|\\d{17})(\\d|[xX])$", text);
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    /**
     * 身份证的格式
     *
     * @param
     * @return
     */
    public static String DealIDcard(String idcard) {
        if (idcard != null) {
            if (idcard.length() == 15) {
                String six = idcard.substring(0, 6);
                idcard = six + "**********";
            } else if (idcard.length() == 18) {
                String six = idcard.substring(0, 6);
                idcard = six + "*************";
            }
        }
        return idcard;
    }

    /**
     * 替换Json解析中的如下字段（data）
     * {"code":0,"extra":"groupId","total":0,"totalpage":0,"data":["1470278048134"]}
     *
     * @param data
     * @return
     */
    public static String replaceJson(String data) {
        if (TextUtils.isEmpty(data))
            return "";
        else {
            data = data.replace("[", "");
            data = data.replace("]", "");
            data = data.replace("\"", "");
            data = data.replace(" ", "").trim();
            return data;
        }
    }

    /**
     * 把6:00格式化成int值600
     *
     * @param time
     * @return
     */
    public static int replaceTime(String time) {
        time = time.replace(":", "");
        return Integer.parseInt(time);
    }

    /**
     * 把带"-"的时间字符串剪切成两个时间字符串并转化成int值，如00:00-03:00
     *
     * @param time
     * @return
     */
    public static int[] splitTime(String time) {
        String[] strArr = time.split("-");
        if (strArr != null && strArr.length == 2) {
            int[] intArr = {replaceTime(strArr[0]), replaceTime(strArr[1])};
            return intArr;
        }
        return null;
    }

    /**
     * 把2016.11.11格式化成int值20161111
     *
     * @param date
     * @return
     */
    public static int replaceDate(String date) {
        date = date.replace(".", "");
        return Integer.parseInt(date);
    }

    /**
     * 判断用户是否登录
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {

        String userId = SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID);
        if (TextUtils.isEmpty(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID))) {
            return false;
        }

        return !TextUtils.isEmpty(userId);
    }

    /**
     * 固话的正则表达式
     * 如果是符合格式的字符串,返回 true ,否则为 false
     */
    public static boolean isPhone(String str) {
        String regex = "^(\\d{3,4}-)?\\d{6,8}$";
        return match(regex, str);
    }

    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /*
     * bitmap通过base64转码成字符串
     */
    public static String bitmapToString(Bitmap bitmap) {
        String string = null;

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bStream);

        byte[] bytes = bStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 钱的格式
     *
     * @param money
     * @return
     */
    public static boolean checkMoney(String money) {
        boolean tag = true;
        if (money.substring(0, 1).equals(".")) {
            return false;
        }
        if (money.substring(money.length() - 1, money.length()).equals(".")) {
            return false;
        }
        int x = 0;
        for (int i = 0; i <= money.length() - 1; i++) {
            String getstr = money.substring(i, i + 1);
            if (getstr.equals(".")) {
                x = x + 1;
            }
        }
        if (x > 1) {
            return false;
        }
        return tag;
    }

    public static String get2Str(int str) {
        String temp = "";
        if (str < 10) {
            temp = "0" + str;
        } else {
            temp = String.valueOf(str);
        }
        return temp;
    }

    public static String str2UTF8(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }

        try {
            String result = URLEncoder.encode(str, "utf-8");
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    /******
     * 验证字符串
     *
     * @param content
     * @return
     */
    public static String verifyString(String content) {
        return content.equals("null") || content == null ? "" : content;
    }
    /**
     * 获得状态栏的高度
     *
     * @param
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
