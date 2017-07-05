package gongren.com.dlg.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.AudioAttributes;
import android.media.Image;
import android.media.ImageReader;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.alibaba.cloudapi.HttpUtil;
import gongren.com.dlg.alibaba.cloudapi.constant.Constants;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.DimensUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.view.AutoFitTextureView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 身份验证 自定义相机
 * Created by lin.li on 2017/2/11.
 * modify by luoxiaohui on 2017/2/22.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CustomCameraActivity extends BaseActivity {

    private static final String TAG = "CustomCameraActivity";
    private static final String TIP = "身份证识别失败，请重试或者重拍！";
    private static final int SETIMAGE = 1;
    private static final int HANDLE_FAIL = 2;
    private static final int DISMISS_DIALOG = 3;

    @Bind(R.id.textureview)
    AutoFitTextureView textureview;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.bg_img)
    ImageView bgImg;
    @Bind(R.id.get_img)
    ImageView getImg;

    @Bind(R.id.img_re)
    RelativeLayout imgRe;
    @Bind(R.id.tips_text)
    TextView tipsText;
    @Bind(R.id.takepicture_btn)
    Button takepictureBtn;
    @Bind(R.id.cancle_btn)
    Button cancleBtn;
    @Bind(R.id.ok_btn)
    Button okBtn;
    @Bind(R.id.bottom_btn_re)
    RelativeLayout bottomBtnRe;
    @Bind(R.id.customcamera_layout)
    RelativeLayout customcameraLayout;

    // 要申请的权限
    private String[] permissions = {Manifest.permission.CAMERA};
    private Dialog dialog;

    private int x;//宽
    private int y;// 长

    private String fileName = "";
    private DisplayMetrics metrics = null;

    private Bitmap finalBitmap = null;
    Handler mHandler;
    Handler mUIHandler;
    ImageReader mImageReader;
    // 创建作为预览的CaptureRequest.Builder
    CaptureRequest.Builder mPreViewBuidler;
    CameraCaptureSession mCameraSession;
    CameraCharacteristics mCameraCharacteristics;
    Ringtone ringtone;
    //相机会话的监听器，通过他得到mCameraSession对象，这个对象可以用来发送预览和拍照请求
    private CameraCaptureSession.StateCallback mSessionStateCallBack;
    //打开相机时候的监听器，通过他可以得到相机实例，这个实例可以创建请求建造者
    private CameraDevice.StateCallback cameraOpenCallBack;
    private ImageReader.OnImageAvailableListener onImageAvaiableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader imageReader) {
            mHandler.post(new ImageSaver(imageReader.acquireNextImage()));
        }
    };
    private Size mPreViewSize;
    //预览图显示控件的监听器，可以监听这个surface的状态
    private TextureView.SurfaceTextureListener mSurfacetextlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        ButterKnife.bind(this);
        mUIHandler = new Handler(new InnerCallBack());
        //初始化拍照的声音
        ringtone = RingtoneManager.getRingtone(this, Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
        AudioAttributes.Builder attr = new AudioAttributes.Builder();
        attr.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
        ringtone.setAudioAttributes(attr.build());

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        x = metrics.widthPixels;//获取了屏幕的宽度
        y = metrics.heightPixels;
//        y = DimensUtils.dip2px(context,374f);//mTextureView的高度
        LogUtils.logE("测试", "相机的高度:" + y);
        //初始化自定义长宽后的view
//        initBaseViews();

        //注册点击事件
        initOnClickListener();
        //初始化数据
        initViews();

        initmSurfacetextlistener();
    }

    private void initmSurfacetextlistener() {
        mSurfacetextlistener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                // 当TextureView可用时，打开摄像头
                HandlerThread thread = new HandlerThread("Ceamera3");
                thread.start();
                mHandler = new Handler(thread.getLooper());
                //获取能打开的摄像头
                CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                String cameraid = CameraCharacteristics.LENS_FACING_FRONT + "";
                try {
                    mCameraCharacteristics = manager.getCameraCharacteristics(cameraid);
                    StreamConfigurationMap map = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                    mPreViewSize = getPictureSize(map.getOutputSizes
                            (SurfaceTexture.class), x);
                    if (mPreViewSize == null) {
                        mPreViewSize = new Size(x, y);
                    }
                    LogUtils.logE("测试图片大小", "Width=" + mPreViewSize.getWidth() + ",Height=" + mPreViewSize.getHeight());
                    //创建读图片的对象
                    mImageReader = ImageReader.newInstance(mPreViewSize.getWidth(), mPreViewSize.getHeight(), ImageFormat.JPEG, 5);
                    mImageReader.setOnImageAvailableListener(onImageAvaiableListener, mHandler);

                    //初始化预览的图片比例
                    initTextureViewSize(width, height);

                    //打开摄像头,编译器强制加的。
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            manager.openCamera(cameraid, cameraOpenCallBack, mHandler);
                        } else {
                            manager.openCamera(cameraid, cameraOpenCallBack, mHandler);
                        }
                    } else {
                        manager.openCamera(cameraid, cameraOpenCallBack, mHandler);
                    }

                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        };
        //初始化相机布局
        textureview.setSurfaceTextureListener(mSurfacetextlistener);
    }

    private void initViews() {
        bottomBtnRe.setVisibility(View.GONE);
        ivBack.setImageResource(R.mipmap.back_black);
        tvTitle.setTextColor(getResources().getColor(R.color.black_textcolor));
        tvTitle.setText("实名认证");
        ivRight.setVisibility(View.INVISIBLE);

        mSessionStateCallBack = new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                try {
                    mCameraSession = cameraCaptureSession;
                    mPreViewBuidler.set(CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                    cameraCaptureSession.setRepeatingRequest(mPreViewBuidler.build(), null, mHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

            }
        };

        cameraOpenCallBack = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice cameraDevice) {
                Log.d(TAG, "相机已经打开");
                try {
                    mPreViewBuidler = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

                    SurfaceTexture texture = textureview.getSurfaceTexture();
//                    texture.setDefaultBufferSize(x, y);
                    Surface surface = new Surface(texture);
                    mPreViewBuidler.addTarget(surface);
                    cameraDevice.createCaptureSession(Arrays.asList(surface,
                            mImageReader.getSurface()), mSessionStateCallBack, mHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDisconnected(CameraDevice cameraDevice) {
                Log.d(TAG, "相机连接断开");
            }

            @Override
            public void onError(CameraDevice cameraDevice, int i) {
                Log.d(TAG, "相机打开失败");
            }
        };

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(x, y);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        textureview.setLayoutParams(lp);

        //设置背景的高宽
        RelativeLayout.LayoutParams bgLp = (RelativeLayout.LayoutParams) imgRe.getLayoutParams();
        bgLp.width = (int) DimensUtils.getScreenWith(this);
        bgLp.height = bgLp.width * 776 / 750;
        imgRe.setLayoutParams(bgLp);
//        mImageView.setLayoutParams(new RelativeLayout.LayoutParams(x,y));
    }

    public void initTextureViewSize(int width, int height) {
        //设置预览大小.
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        String cameraid = CameraCharacteristics.LENS_FACING_FRONT + "";
        // 获取指定摄像头的特性
        CameraCharacteristics characteristics = null;
        try {
            characteristics = manager.getCameraCharacteristics(cameraid);
            // 获取摄像头支持的配置属性
            StreamConfigurationMap map = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            // 获取最佳的预览尺寸
            Size previewSize = chooseOptimalSize(map.getOutputSizes(
                    SurfaceTexture.class), width, height, mPreViewSize);

            // 根据选中的预览尺寸来调整预览组件（TextureView）的长宽比
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                textureview.setAspectRatio(previewSize.getWidth(), previewSize.
                        getHeight());
            } else {
                textureview.setAspectRatio(previewSize.getHeight(),
                        previewSize.getWidth());
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private static Size chooseOptimalSize(Size[] choices
            , int width, int height, Size aspectRatio) {
        // 收集摄像头支持的大过预览Surface的分辨率
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }
        // 如果找到多个预览尺寸，获取其中面积最小的
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            System.out.println("找不到合适的预览尺寸！！！");
            return choices[0];
        }
    }

    // 为Size定义一个比较器Comparator
    static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            // 强转为long保证不会发生溢出
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    //    private RelativeLayout cancelLayout;
//    private RelativeLayout topCoverLayout;
    private RelativeLayout bottomCoverLayout;
    private RelativeLayout confirmLayout;
    private RelativeLayout chooseRootLayout;
    private LinearLayout useViewLayout;
    private LinearLayout resetViewLayout;//重置按钮
    //    private float scaleWidth;
//    private float scaleHeight;
    private ImageView imageView;//截取的bitmap放置在其中
    private RelativeLayout imageViewLayout;

    private void initBaseViews() {
//        scaleWidth = 750f / x;
//        scaleHeight = 1334f / y;

//        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.customcamera_layout);
//
//        //===============================================上面遮挡部分=============================================
////        topCoverLayout = new RelativeLayout(this);
////        topCoverLayout.setBackgroundColor(Color.parseColor("#505050"));
////        int topHeight = y * 340 / 1334;
////        RelativeLayout.LayoutParams topCoverParams = new RelativeLayout.LayoutParams(x, topHeight);
//
////        ImageView cancelView = new ImageView(this);
////        int cancelWidth = y * 60 / 1334;
////        int cancelHeight = cancelWidth;
////        cancelView.setBackgroundResource(R.mipmap.ic_wallet_cha);
////        RelativeLayout.LayoutParams cancelViewParams = new RelativeLayout.LayoutParams(cancelWidth, cancelHeight);
////        cancelViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT & RelativeLayout.ALIGN_PARENT_BOTTOM);
////        cancelView.setLayoutParams(cancelViewParams);
//
//
////        cancelLayout = new RelativeLayout(this);
////        RelativeLayout.LayoutParams cancelLayoutParams = new RelativeLayout.LayoutParams((int) (cancelWidth * 1.3),
////                (int) (cancelHeight * 1.3));
////        cancelLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
////        cancelLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
////        cancelLayoutParams.bottomMargin = (int) (scaleWidth * 60);
////        cancelLayoutParams.rightMargin = (int) (scaleWidth * 60);
////        cancelLayout.setLayoutParams(cancelLayoutParams);
////        cancelLayout.addView(cancelView);
////
////        topCoverLayout.addView(cancelLayout);
//
////        rootLayout.addView(topCoverLayout, topCoverParams);
//
//        //=================================================中间ImageView部分============================================
//        imageView = new ImageView(this);
//        imageViewLayout = new RelativeLayout(this);
//        int middleHeight = y * 504 / 1334;
//        RelativeLayout.LayoutParams imageViewParams = new RelativeLayout.LayoutParams(-1, middleHeight);
//        imageViewParams.topMargin = y * 340 / 1334;
//        imageViewLayout.setLayoutParams(imageViewParams);
//        imageViewLayout.addView(imageView);
//
//        rootLayout.addView(imageViewLayout);
//        //=================================================下面遮挡部分==================================================
//        bottomCoverLayout = new RelativeLayout(this);
//        bottomCoverLayout.setBackgroundColor(Color.parseColor("#505050"));
//        int bottomHeight = y * 490 / 1334;
//        RelativeLayout.LayoutParams bottomCoverParams = new RelativeLayout.LayoutParams(x, bottomHeight);
//        bottomCoverParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        bottomCoverParams.topMargin = (int) (y * 844 / 1334);
//        bottomCoverLayout.setLayoutParams(bottomCoverParams);
//
//        ImageView confirmView = new ImageView(this);
//        confirmView.setBackgroundResource(R.mipmap.ic_wallet_paizhao);
//        confirmLayout = new RelativeLayout(this);
//        RelativeLayout.LayoutParams confirmLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
//        confirmLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        confirmLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        confirmLayoutParams.bottomMargin = (int) (scaleHeight * 50);
//        confirmLayout.setLayoutParams(confirmLayoutParams);
//
//        confirmLayout.addView(confirmView);
//        bottomCoverLayout.addView(confirmLayout);
//
//        //-------------------------------------------使用和重置按钮布局-------------------------------------------------------------
//        chooseRootLayout = new RelativeLayout(this);
//        RelativeLayout.LayoutParams chooseRootParams = new RelativeLayout.LayoutParams(-2, -2);
//        chooseRootParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        chooseRootParams.bottomMargin = (int) (scaleHeight * 100);
//        chooseRootParams.leftMargin = (int) (scaleHeight * 100);
//        chooseRootParams.rightMargin = (int) (scaleHeight * 100);
//        chooseRootLayout.setLayoutParams(chooseRootParams);
//
//        LinearLayout chooseLayout = new LinearLayout(this);
//        chooseLayout.setOrientation(LinearLayout.HORIZONTAL);
//        LinearLayout.LayoutParams chooseParams = new LinearLayout.LayoutParams(x, -2);
//        chooseLayout.setLayoutParams(chooseParams);
//
//        useViewLayout = new LinearLayout(this);
//        useViewLayout.setGravity(Gravity.CENTER);
//        useViewLayout.setBackgroundColor(Color.parseColor("#02add9"));
//        TextView useView = new TextView(this);
//        useView.setPadding((int) (scaleHeight * 30), (int) (scaleHeight * 30), (int) (scaleHeight * 30),
//                (int) (scaleHeight * 30));
//        useView.setText("使用");
//        useView.setTextColor(Color.parseColor("#ffffff"));
//        useView.setTextSize(20);
//        LinearLayout.LayoutParams useViewParams = new LinearLayout.LayoutParams(-1, -2);
//        useViewParams.weight = 1;
//        useViewLayout.setLayoutParams(useViewParams);
//        useViewLayout.addView(useView);
//
//        LinearLayout nullLayout = new LinearLayout(this);
//        LinearLayout.LayoutParams nullParams = new LinearLayout.LayoutParams((int) (scaleHeight * 100), 10);
//        nullLayout.setLayoutParams(nullParams);
//
//        resetViewLayout = new LinearLayout(this);
//        resetViewLayout.setGravity(Gravity.CENTER);
//        resetViewLayout.setBackgroundColor(Color.parseColor("#999999"));
//        TextView resetView = new TextView(this);
//        resetView.setPadding((int) (scaleHeight * 30), (int) (scaleHeight * 30), (int) (scaleHeight * 30),
//                (int) (scaleHeight * 30));
//        LinearLayout.LayoutParams resetViewParams = new LinearLayout.LayoutParams(-1, -2);
//        resetView.setTextColor(Color.parseColor("#ffffff"));
//        resetView.setText("重拍");
//        resetView.setTextSize(20);
//        resetViewParams.weight = 1;
//        resetViewLayout.setLayoutParams(resetViewParams);
//        resetViewLayout.addView(resetView);
//
//        chooseLayout.addView(useViewLayout);
//        chooseLayout.addView(nullLayout);
//        chooseLayout.addView(resetViewLayout);
//        chooseRootLayout.addView(chooseLayout);
//        bottomCoverLayout.addView(chooseRootLayout);
//
//        rootLayout.addView(bottomCoverLayout);
//        chooseRootLayout.setVisibility(View.GONE);
//        imageViewLayout.setVisibility(View.GONE);
//
//        initOnClickListener();
    }

    private void initOnClickListener() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        cancelLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomCameraActivity.this.finish();
//            }
//        });

        takepictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = DialogUtils.showLoadingDialog(context);
                try {
                    shootSound();
                    Log.d(TAG, "正在拍照");
                    CaptureRequest.Builder builder = mCameraSession.getDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                    builder.addTarget(mImageReader.getSurface());
                    builder.set(CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_AUTO);
                    builder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                            CameraMetadata.CONTROL_AF_TRIGGER_START);
                    builder.set(CaptureRequest.JPEG_ORIENTATION, 90);//
                    mCameraSession.capture(builder.build(), null, mHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //裁剪拍照得到的图片。
                Bitmap bitmap = CropID(finalBitmap);
                if (bitmap != null) {
                    //保存图片，传送路径
                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsoluteFile();
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, System.currentTimeMillis() + ".png");
                    if(!file.exists()){
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    String filepath = file.getAbsolutePath();
                    saveBitmap(bitmap,file);
                    //把图片带回到上一个页面
                    Intent intent = new Intent();
                    intent.putExtra("filepath",filepath);
                    CustomCameraActivity.this.setResult(Activity.RESULT_OK, intent);
                    CustomCameraActivity.this.finish();
//                    verifyID(finalBitmap);//调用支付宝的接口验证图片.
                }
            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImg.setVisibility(View.GONE);
                textureview.setVisibility(View.VISIBLE);
                tipsText.setVisibility(View.VISIBLE);
                takepictureBtn.setVisibility(View.VISIBLE);
                bottomBtnRe.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (mCameraSession != null) {
            mCameraSession.getDevice().close();
            mCameraSession.close();
        }
        if (finalBitmap != null && !finalBitmap.isRecycled()) {
            finalBitmap.recycle();
            finalBitmap = null;
        }
    }


    /**
     * 播放系统的拍照的声音
     */
    public void shootSound() {
        ringtone.stop();
        ringtone.play();
    }


    private class ImageSaver implements Runnable {
        Image reader;

        public ImageSaver(Image reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            Log.d(TAG, "正在保存图片");
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsoluteFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, System.currentTimeMillis() + ".png");
            fileName = file.getPath();
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                ByteBuffer buffer = reader.getPlanes()[0].getBuffer();
                byte[] buff = new byte[buffer.remaining()];
                buffer.get(buff);
                BitmapFactory.Options ontain = new BitmapFactory.Options();
                ontain.inSampleSize = 1;
                Bitmap bm = BitmapFactory.decodeByteArray(buff, 0, buff.length, ontain);
//                int wd = bm.getWidth();
//                int hd = bm.getHeight();
                //因为拍出来的图片很小，所以要对图片进行缩放
                bm = Bitmap.createScaledBitmap(bm, x, y, true);
//                int wd1 = bm.getWidth();
//                int hd1 = bm.getHeight();
                finalBitmap = ImageCropWithRect(bm);
//                int wd2 = finalBitmap.getWidth();
//                int hd2 = finalBitmap.getHeight();
                saveBitmap(finalBitmap, file);

                Message.obtain(mUIHandler, SETIMAGE).sendToTarget();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    reader.close();
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 保存方法
     */
    public void saveBitmap(Bitmap bm, File f) {
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class InnerCallBack implements Handler.Callback {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case SETIMAGE:
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();

                    textureview.setVisibility(View.INVISIBLE);

                    getImg.setVisibility(View.VISIBLE);
                    if (finalBitmap != null) getImg.setImageBitmap(finalBitmap);
                    tipsText.setVisibility(View.GONE);
                    takepictureBtn.setVisibility(View.GONE);
                    bottomBtnRe.setVisibility(View.VISIBLE);
                    break;
                case HANDLE_FAIL:
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(CustomCameraActivity.this, TIP, Toast.LENGTH_LONG).show();
                    break;
                case DISMISS_DIALOG:
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    break;
            }
            return false;
        }
    }

    public Size getPictureSize(Size[] list, int th) {

        for (Size s : list) {
            if ((s.getWidth() > th) && equalRate(s, 34 / 75f)) {
                Log.i("TAG", "最终设置图片尺寸:w = " + s.getWidth() + "h = " + s.getHeight());
                return s;
            }

        }
        return null;


    }

    private ArrayList<Size> getSizeList(Size[] list) {
        ArrayList<Size> sizeList = new ArrayList<>();
        if (list != null) {
            for (Size size : list) {
                sizeList.add(size);
            }
        }

        return sizeList;
    }

    private Size getOptimalPreviewSize(ArrayList<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.getWidth() / size.getHeight();
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.getHeight() - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.getHeight() - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public boolean equalRate(Size s, float rate) {
        float r = (float) (s.getWidth()) / (float) (s.getHeight());
        if (Math.abs(r - rate) <= 0.2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 按长方形裁切图片
     *
     * @param bitmap
     * @return
     */
    public Bitmap ImageCropWithRect(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int nw, nh;     //新的bitmap的长宽
        int retX, retY; //新的bitmap的位置
        if (w > h) {
            nw = h / 2;
            nh = h;
            retX = (w - nw) / 2;
            retY = 0;
        } else {
            //由于我们的app强制竖屏，所以走这里的逻辑
            retX = 0;
            retY = DimensUtils.dip2px(context, 44) + DimensUtils.getStatusHeight(context);//标题栏+状态栏的高度
//            retY = y * 340 / 1334;
            nw = w;
            nh = w * 776 / 750;
//            nh = y * 504 / 1334;
        }

        // 下面这句是关键
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
                false);

        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
        // false);
    }

    /**
     * 裁剪出身份证的大小的图片
     *
     * @param bitmap
     * @return
     */
    public Bitmap CropID(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int nw, nh;     //新的bitmap的长宽
        int retX, retY; //新的bitmap的位置
        if (w > h) {
            nw = h / 2;
            nh = h;
            retX = (w - nw) / 2;
            retY = 0;
        } else {
            //由于我们的app强制竖屏，所以走这里的逻辑
            retX = w * 2 / 750;
            retY = h * 185 / 776;
//            retY = y * 340 / 1334;
            nw = w * 707 / 750;
            nh = h * 408 / 750;
//            nh = y * 504 / 1334;
        }

        // 下面这句是关键
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
                false);

        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
        // false);
    }

    public void verifyID(final Bitmap bitmap) {
        dialog.show();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String getPath = "/rest/160601/ocr/ocr_idcard.json";

                String imgBase64 = StringUtils.bitmapToString(bitmap);
                // 拼装请求body的json字符串
                JSONObject requestObj = new JSONObject();
                try {
                    JSONObject configObj = new JSONObject();
                    JSONObject obj = new JSONObject();
                    JSONArray inputArray = new JSONArray();
                    configObj.put("side", "face");
                    obj.put("image", getParam(50, imgBase64));
                    obj.put("configure", getParam(50, configObj.toString()));
                    inputArray.put(obj);
                    requestObj.put("inputs", inputArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String content = requestObj.toString();

                Log.w("data", content);

                HttpUtil.getInstance().httpPostBytes(getPath, null, null, content.getBytes(Constants.CLOUDAPI_ENCODING), null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        LogUtils.logD("test", "errorInfo--->" + e.getMessage());
                        Message.obtain(mUIHandler, HANDLE_FAIL).sendToTarget();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        getResultString(response);
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    /*
      * 获取参数的json对象
      */
    public JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private void getResultString(Response response) throws IOException {
        StringBuilder result = new StringBuilder();
        if (response.code() != 200) {
            result.append("错误原因：").append(response.header("X-Ca-Error-Message")).append(Constants.CLOUDAPI_LF).append(Constants.CLOUDAPI_LF);
//            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            Message.obtain(mUIHandler, HANDLE_FAIL).sendToTarget();
            return;
        }

        Intent intent = new Intent();
        result.append(Constants.CLOUDAPI_LF).append(new String(response.body().bytes(), Constants.CLOUDAPI_ENCODING));
        try {
            JSONObject outputObj = new JSONObject(result.toString());
            String output = outputObj.optString("outputs");

            JSONArray array = new JSONArray(output);
            JSONObject obj = array.getJSONObject(0);
            String outputValue = obj.optString("outputValue");

            JSONObject outputValueObj = new JSONObject(outputValue);
            String dataValue = outputValueObj.optString("dataValue");

            JSONObject dataValueObj = new JSONObject(dataValue);
            String name = dataValueObj.optString("name");
            String num = dataValueObj.optString("num");

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(num) || name.equals("") || num.equals("")) {
                Message.obtain(mUIHandler, HANDLE_FAIL).sendToTarget();
                return;
            }

            intent.putExtra("name", name);
            intent.putExtra("num", num);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            Message.obtain(mUIHandler, DISMISS_DIALOG).sendToTarget();
        }

        intent.putExtra("pic", fileName);
        CustomCameraActivity.this.setResult(Activity.RESULT_OK, intent);
        CustomCameraActivity.this.finish();
    }
}

















