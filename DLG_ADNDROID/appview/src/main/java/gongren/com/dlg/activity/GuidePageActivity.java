package gongren.com.dlg.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.common.utils.SharedPreferencesUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.GuidePageAdapter;
import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 启动页
 */
public class GuidePageActivity extends BaseActivity {

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.iv_start_gif)
    ImageView ivStartGif;
    private  String businessNumber; //获取H5传递的参数
    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String toke = SharedPreferencesUtils.getString(context, SharedPreferencesUtils.TOKEN);
                    if (!TextUtils.isEmpty(toke)) {
                        startActivity(new Intent(context, MainActivity.class).putExtra("isFirstIn", true).putExtra("businessNumber",businessNumber));
                    } else {
                        startActivity(new Intent(context, MainActivity.class).putExtra("businessNumber",businessNumber));
                    }
                    context.finish();
                    break;
                case 1:
                    setAdapter();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private EdgeEffectCompat leftEdge, rightEdge;
    private ArrayList<ImageView> views = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        ButterKnife.bind(this);
        initDatas();
        onCall();
        setBundle();
        initView();
        // ATTENTION: This was auto-generated to handle app links.
    }

    /**
     * H5协议
     */
    public void setBundle(){
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if(null != appLinkAction && appLinkData != null){
            businessNumber = appLinkData.getQueryParameter("businessNumber");
        }
        Log.e("DLG", "==========setBundle====H5进入==========");
//        String mydata = uridata.getQueryParameter("data");
//        WorkOrderDetailActivity.open(activity, tmplist.get(position).getBusinessNumber());
    }


    private void initView() {
        Glide.with(this).load(R.drawable.start_page_image).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivStartGif);
//        ivStartGif.setMovieResource(R.mipmap.start_page_image);
        boolean noFirstIn = SharedPreferencesUtils.getBoolean(context, SharedPreferencesUtils.NOFIRSTIN);
        if (noFirstIn) {
            //            ivStart.setVisibility(View.VISIBLE);
            ivStartGif.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            mHandler.sendEmptyMessageDelayed(0, 3000);
        } else {
            ivStartGif.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            mHandler.sendEmptyMessage(1);
        }
    }
    /**
     * 给viewPager设置适配器
     */
    private void setAdapter() {
        try {
            Field leftEdgeField = viewPager.getClass().getDeclaredField("mLeftEdge");
            Field rightEdgeField = viewPager.getClass().getDeclaredField("mRightEdge");
            if (leftEdgeField != null && rightEdgeField != null) {
                leftEdgeField.setAccessible(true);
                rightEdgeField.setAccessible(true);
                leftEdge = (EdgeEffectCompat) leftEdgeField.get(viewPager);
                rightEdge = (EdgeEffectCompat) rightEdgeField.get(viewPager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            switch (i) {
                case 0:
                    imageView.setImageResource(R.mipmap.android1);
                    break;
                case 1:
                    imageView.setImageResource(R.mipmap.android2);
                    break;
                case 2:
                    imageView.setImageResource(R.mipmap.android3);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpLoginActivity();
                        }
                    });
                    break;
                default:
                    break;
            }
            views.add(imageView);
        }
        GuidePageAdapter adapter = new GuidePageAdapter(views);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //到了最后一张并且还继续拖动，出现蓝色限制边条了
                if (leftEdge != null && !rightEdge.isFinished()) {
                    jumpLoginActivity();
                }
            }
        });
    }

    private void jumpLoginActivity() {
        SharedPreferencesUtils.saveBoolean(context, SharedPreferencesUtils.NOFIRSTIN, true);
        startActivity(new Intent(context, MainActivity.class));
        context.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(context);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(context);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    //定位权限
    final public static int REQUEST_ACCESS_COARSE_LOCATION = 123;

    public void onCall() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
                return;
            } else {
                //开启6.0一下的定位方法
                openGPS();
            }
        } else {
            //开启6.0一下的定位方法
            openGPS();
        }
    }

    public final void openGPS() {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取打开距离
     */
    private void initDatas() {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("groupCode", "clock_scope");
        DataUtils.loadData(context, GetDataConfing.dictionaryRest, map, 100, responseDataCallback);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            //            if (canContentView != null) {
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(context);
            } else {
                String json = dataRequest.getResponseData();
                if (!TextUtils.isEmpty(json)) {
                    LogUtils.logD("zq", json);
//
                    List<JsonMap<String, Object>> dataList = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                    if (null != dataList && dataList.size() > 0) {
                        JsonMap<String, Object> jsonMap = dataList.get(0);
                        if (!TextUtils.isEmpty(jsonMap.getString("dataValue"))) {
                            MyApplication.dataValueDistance = Integer.parseInt(jsonMap.getString("dataValue"));
                        }

                    }
                }
            }
        }
        //        }
    };

}
