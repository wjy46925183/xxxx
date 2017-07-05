package gongren.com.dlg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.utils.ActivityManager;

/**
 * 钱包设置
 */
public class WalletSettingActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_reset_pwd)
    TextView tvResetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_setting);
        ButterKnife.bind(this);
        ActivityManager.getActivityManager().addActivity(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("钱包设置");

    }

    @OnClick({R.id.iv_back, R.id.tv_reset_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:    //返回
                finish();
                break;
            case R.id.tv_reset_pwd:   //重置支付密码
                startActivity(new Intent(context, RestPasswordActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
