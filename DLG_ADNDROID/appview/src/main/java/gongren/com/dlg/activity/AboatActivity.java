package gongren.com.dlg.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;

/**
 * Created by wjy
 */

public class AboatActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_right)
    ImageView mIvRight;
    @Bind(R.id.iv_aboat)
    ImageView mIvAboat;
    @Bind(R.id.tv_dalinggong)
    TextView mTvDalinggong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboat);
        ButterKnife.bind(this);
        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mTvTitle.setText(R.string.aboat_our);
        Glide.with(this).load(R.mipmap.aboat_us)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mIvAboat);
        mTvDalinggong.setText("打零工（上海）互联网科技有限公司\n©2017 v"+getAppVersionName(this));
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
