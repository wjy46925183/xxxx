package com.dlg.personal.home.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.common.string.StringUtil;
import com.dlg.personal.R;

//Toast统一管理类
public class ToastUtils
{

    private ToastUtils()
    {
            /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;
    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, String message)
    {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message)
    {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message)
    {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message)
    {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message)
    {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration)
    {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration)
    {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 弹出框
     * @param context
     * @param message
     * @param title
     * @return
     */
    public static Toast getMessageToast(Context context,String message, String title){
        Toast toast = new Toast(context);
        View inflate = View.inflate(context, R.layout.toast_agree, null);
        TextView tvMessage = (TextView) inflate.findViewById(R.id.tv_message_toast);
        TextView tv_title = (TextView) inflate.findViewById(R.id.tv_title_toast);
        if(!TextUtils.isEmpty(message)){
            tvMessage.setText(message);
        }else{
            tvMessage.setText("");
        }
        if(!TextUtils.isEmpty(title)){
            tv_title.setText(title);
        }else{
            tv_title.setText("");
            tvMessage.setTextSize(15);
        }
        toast.setView(inflate);
        toast.setGravity(Gravity.CENTER, 0, StringUtil.dip2px(context, 80f));
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }
}