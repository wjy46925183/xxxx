package com.common.sys;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;

/**
 * 作者：wangdakuan
 * 主要功能：手机信息或基本的版本信息
 * 创建时间：2016/12/26 15:24
 */
public class SystemUtil {
    /**
     * 使用的SDK版本
     *
     * @return
     */
    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备的型号
     */
    public static String getDeviceModel() {
        String deviceModel = Build.MODEL;
        //Log.v(TAG, "Device model: " + deviceModel);
        return deviceModel;
    }

    /**
     * 获取设备的厂商
     */
    public static String getDeviceBrand() {
        String deviceBrand = Build.BRAND;
        return deviceBrand;
    }

    /**
     * 获取系统的软件版本号
     */
    public static String getDeviceSDK() {
        String sdk_verison = Build.VERSION.SDK;
        return sdk_verison;
    }

    /**
     * 获取工程版本号
     */
    public static String getVersionCode(Context context) {

        String versionCode = "";
        try {
            versionCode = "" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取工程版本名称
     *
     * @return
     * @author carlos carlosk@163.com
     * @version 创建时间：2013-3-17 下午2:58:57
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 判断是否有SD卡
     *
     * @return
     */
    public static boolean getSDState() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDpath() {
        if (getSDState()) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return null;
    }

    /**
     * 是否有可用的网络
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        return networkInfo.isAvailable();
    }

    /**
     * wifi 网络是否可用
     *
     * @return
     */
    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * dip转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    /**
     * px转dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5F);
    }

    /**
     * sp转px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5F);
    }


    /**
     * 获取手机状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
