package gongren.com.dlg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;

/**
 * Created by Administrator on 2017/6/21 0021.
 * 选择实名认证类型
 */

public class RealNameAuthenticTypeActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    private int authenticcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_authentic_type);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("选择证件类型");
    }

    @OnClick({R.id.layout_card, R.id.layout_huzhao, R.id.layout_taiwan, R.id.layout_gangao,R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_card:
                authenticcode = 1;
                break;
            case R.id.layout_huzhao:
                authenticcode = 13;
                break;
            case R.id.layout_taiwan:
                authenticcode = 10;
                break;
            case R.id.layout_gangao:
                authenticcode = 14;
                break;
            case R.id.iv_back:
                finish();
        }
        Intent intent = new Intent(this, RealNameAuthenticationActivity.class);
        intent.putExtra("authenticcode", authenticcode);
        startActivity(intent);
    }

}
