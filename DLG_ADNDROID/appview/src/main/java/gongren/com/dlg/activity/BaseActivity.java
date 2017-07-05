package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.umeng.analytics.MobclickAgent;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import gongren.com.dlg.R;
import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.utils.ActivityController;
import gongren.com.dlg.view.AlertView;
import gongren.com.dlg.volleyUtils.GetDataConfing;

public class BaseActivity extends  AppCompatActivity{

    protected BaseActivity context;
    private static final String TAG = "BaseActivity";
    private AlertView alertView;
    private static final int TAG_ACTIVITY = 12121212;    //请求活动接口
    private static final int TAG_UPDATE = 13131313;      //请求检查更新接口
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
        setContentView(R.layout.activity_base);
        Log.e("DLG", "cookieStore==" + MyApplication.getCookieStore());
        CookieManager cookieManager = new CookieManager(MyApplication.getCookieStore(), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        Log.e(TAG,this.getClass().getSimpleName());
        context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removerActivity(this);
    }

    /**
     * 弹出活动的Dialog
     */
    protected void showDialog(String imageUrl, final String activityUrl) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.item_activityinformation, null);
        ImageView close = (ImageView) layout.findViewById(R.id.close);
        final ImageView img = (ImageView) layout.findViewById(R.id.img);
        final Dialog dialog = new Dialog(context, R.style.Mydialog);

        //加载活动图片
        Glide.with(MyApplication.getInstance()).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                //设置大小
                float bili = Float.parseFloat("" + resource.getHeight()) / resource.getWidth();
                DisplayMetrics metric = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metric);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        (int) (metric.widthPixels * 0.8),
                        (int) (metric.widthPixels * 0.8 * bili));
                img.setLayoutParams(layoutParams);
                img.setImageBitmap(resource);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开活动地址
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                //活动地址
                Uri content_url = Uri.parse(GetDataConfing.BASEURL + activityUrl);
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setContentView(layout);
    }
}
