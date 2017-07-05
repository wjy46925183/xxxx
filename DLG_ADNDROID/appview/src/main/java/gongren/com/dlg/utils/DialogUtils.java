package gongren.com.dlg.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.ProgressHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gongren.com.dlg.R;
import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.view.GifView;

/**
 * AlertDialog工具类
 */
public class DialogUtils {

    //自定义接口，点击确定按钮时实现该接口
    public interface ConfirmCallback {
        void confirm(DialogInterface dialog, int which);
    }

    //回调确认按钮
    public interface CallBackConfrim {
        void confirm(DialogInterface dialog, TextView title);
    }

    /**
     * 弹出简单对话框
     *
     * @param context
     * @param title
     * @param message
     * @param callback
     */
    public static void showSimpleDialog(Context context, String title, String message, final ConfirmCallback callback) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.confirm(dialog, which);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 弹出自定义布局的对话框
     *
     * @param context
     */
    public static AlertDialog showViewDialog(Context context, View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        return alertDialog;
    }

    /**
     * 网络等待确认的Dialog
     * @param context
     * @return
     */
    public static Dialog showLoadingDialog(Context context) {
        return loadingProgressDialog(context);
    }

    public static SweetAlertDialog loadingProgressDialog(Context context){
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        ProgressHelper progressHelper = pDialog.getProgressHelper();
        progressHelper.setBarColor(Color.parseColor("#ff9752"));
        pDialog.setTitleText("loading");
        pDialog.setCancelable(true);
        pDialog.show();
        return pDialog;
    }

    /**
     * 网络等待确认的Toast
     * @return
     */
    public static Toast showLoadingToast() {
        Toast toast = Toast.makeText(MyApplication.getInstance(),
                "带图片的Toast", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();

        GifView gifView = new GifView(MyApplication.getInstance());
        gifView.setMovieResource(R.mipmap.loading_picture);
        toastView.addView(gifView);

        toast.show();
        return toast;
    }

    /**
     * 设置pop背景色
     *
     * @param bgcolor
     */
    public static void darkenBackgroud(Activity context, Float bgcolor) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgcolor;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    /**
     * 关闭软键盘
     */
    public static void closeInputMethod(Activity context) {
        try {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))

                    .hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
    }

}
