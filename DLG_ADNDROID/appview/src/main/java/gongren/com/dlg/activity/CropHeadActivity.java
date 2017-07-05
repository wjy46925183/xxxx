package gongren.com.dlg.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.common.utils.headcrop.ClipImageLayout;
import com.common.utils.headcrop.ImageTools;

import gongren.com.dlg.R;
import gongren.com.dlg.utils.LogUtils;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class CropHeadActivity extends Activity {
    private ClipImageLayout mClipImageLayout;
    private String path;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_head);
        //这步必须要加
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("请稍后...");
        path = getIntent().getStringExtra("path");
        if (TextUtils.isEmpty(path) || !(new File(path).exists())) {
            Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmap = ImageTools.convertToBitmap(path, 600, 600);
        if (bitmap == null) {
            Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
            return;
        }
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        mClipImageLayout.setBitmap(bitmap);
        ((Button) findViewById(R.id.id_action_clip)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadingDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = mClipImageLayout.clip();
                        File file = getCacheDir(CropHeadActivity.this, System.currentTimeMillis() + ".png");
                        ImageTools.savePhotoToSDCard(bitmap, file);
                        Luban.with(CropHeadActivity.this).load(file).setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {
                                loadingDialog.dismiss();
                                Intent intent = new Intent();
                                intent.setData(Uri.fromFile(file));
                                LogUtils.logE("PATH1",  file.getPath());
                                setResult(RESULT_OK, intent);
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                loadingDialog.dismiss();
                            }
                        }).launch();
                    }
                }).start();
            }
        });
    }


    private File getCacheDir(Context context, String cacheName) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        cacheDir = new File(cacheDir, cacheName);
        return cacheDir;
    }

}
