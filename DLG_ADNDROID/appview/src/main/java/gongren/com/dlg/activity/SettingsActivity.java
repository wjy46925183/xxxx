package gongren.com.dlg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import gongren.com.dlg.R;
import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.utils.ActivityController;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.CacheUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.ExampleUtil;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.view.AlertView;
import gongren.com.dlg.view.OnItemClickListener;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 设置界面（雇主、雇员共用一个）
 */
public class SettingsActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;//返回键
    @Bind(R.id.tv_title)
    TextView tvTitle;//标题栏
    @Bind(R.id.iv_right)
    ImageView ivRight;//右边按钮
    @Bind(R.id.tv_version_name)
    TextView tvVersionName;//版本号
    @Bind(R.id.changepwd_linear)
    LinearLayout changepwdLinear;//修改密码
    @Bind(R.id.renzheng_linear)
    LinearLayout renzhengLinear;//实名认证
    @Bind(R.id.cache_text)
    TextView cacheText;//缓存大小
    @Bind(R.id.clear_cache_linear)
    LinearLayout clearCacheLinear;//清理缓存
    @Bind(R.id.userxy_linear)
    LinearLayout userxyLinear;//用户协议
    @Bind(R.id.about_linear)
    LinearLayout aboutLinear;//关于我们
    @Bind(R.id.tv_exit)
    Button tvExit;//退出
    /*@Bind(R.id.company_linear)
    LinearLayout company_linear;*/
    private AlertView alertView;
    private static final int TAG_EXIT = 0;      //退出登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("设置");
        ivRight.setVisibility(View.INVISIBLE);
        tvVersionName.setText("V" + StringUtils.getVersionName(context));
        if (CacheUtils.getFileSize().equals(".00B"))
            cacheText.setText("0.00B");
        else
            cacheText.setText(CacheUtils.getFileSize());
    }

    @OnClick({R.id.iv_back, R.id.changepwd_linear, R.id.renzheng_linear, R.id.clear_cache_linear, R.id.tv_exit, R.id.userxy_linear, R.id.about_linear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:      //返回
                finish();
                break;

            case R.id.changepwd_linear:   //修改密码
                startActivity(new Intent(context, RevisePassWordActivity.class));
                break;
            case R.id.renzheng_linear:   //实名认证
                startActivity(new Intent(context, RealNameAuthenticationActivity.class));
                break;

            case R.id.clear_cache_linear:     //清除缓存
                CacheUtils.deleteFolderFile(CacheUtils.productPath, false);
                cacheText.setText("0.00B");
                ToastUtils.showToastShort(context, "缓存已经清除");
                break;

            case R.id.tv_exit:         //退出登录
                alertView = new AlertView("提示", "确定退出登录？", "取消", null,
                        new String[]{"确定"}, context, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position != -1) {
                            DataUtils.loadData(context, GetDataConfing.LOGOUT, BaseMapUtils.getMap(context), TAG_EXIT, responseDataCallback);
                        }
                    }
                });
                alertView.setView(false, false, "").setCancelable(true);
                alertView.show();
                break;

            case R.id.userxy_linear:    //用户协议，跳h5页面
                Intent agreementIntent = new Intent(context, WebUtilsActivity.class);
                agreementIntent.putExtra("functionTitle", "用户协议");
                agreementIntent.putExtra("contentUrl", GetDataConfing.BASEURL_H5);
                agreementIntent.putExtra("type", "3");
                startActivity(agreementIntent);
                break;
            case R.id.about_linear:    //关于我们，跳h5页面
                Intent aboutUsIntent = new Intent(context, AboatActivity.class);
                startActivity(aboutUsIntent);
                break;
            /*case R.id.company_linear:
                SharedPreferencesUtils.saveBoolean(context,"iscompany",true);
                DialogUtils.showSimpleDialog(context, "切换企业版本？", "", new DialogUtils.ConfirmCallback() {
                    @Override
                    public void confirm(DialogInterface dialog, int which) {
                        startActivity(new Intent(SettingsActivity.this,MainActivity.class));
                        dialog.dismiss();
                    }
                });*/

        }
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (tvTitle != null) {
                if (dataRequest.isNetError()) {
                    if (dataRequest.getWhat() != TAG_EXIT)
                        ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    if (!TextUtils.isEmpty(json)) {
                        if (dataRequest.getWhat() == TAG_EXIT) {                //退出登录完成
                            //置空别名
                            SharedPreferencesUtils.saveString(context,
                                    SharedPreferencesUtils.USERID, "");

                            String userId = SharedPreferencesUtils.getString(context,
                                    SharedPreferencesUtils.USERID);
//                            setAlias(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.ID));
                            JPushInterface.setAliasAndTags(getApplicationContext(), "", null, mAliasCallback);
                            MyApplication.isLogInPage = true;
//                            SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.TOKEN, "");
                            SharedPreferencesUtils.clear(context);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtra("isLogout", true);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    ActivityController.removeAll();//关闭所有的页面
                                }
                            },500);

                        }
                    }
                }
            }
        }
    };

    private void setAlias(String alias) {
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(context, "格式不对", Toast.LENGTH_SHORT).show();
            return;
        }
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    //(String) msg.obj
                    JPushInterface.setAliasAndTags(getApplicationContext(), "", null, mAliasCallback);
                    break;
                default:
            }
        }
    };
    private static final String TAG = "JPush";
    private static final int MSG_SET_ALIAS = 1001;
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (alertView != null && alertView.isShowing()) {
                alertView.dismiss();
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
