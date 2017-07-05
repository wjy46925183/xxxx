package gongren.com.dlg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;

/**
 * Created by Administrator on 2017/6/19 0019.
 * 选择企业性质页面
 */
public class SelectNatureActivity extends BaseActivity {
    @Bind(R.id.cb1)
    CheckBox cb1;
    @Bind(R.id.cb2)
    CheckBox cb2;
    @Bind(R.id.cb3)
    CheckBox cb3;
    @Bind(R.id.cb4)
    CheckBox cb4;
    @Bind(R.id.cb5)
    CheckBox cb5;
    @Bind(R.id.cb6)
    CheckBox cb6;
    @Bind(R.id.cb7)
    CheckBox cb7;
    @Bind(R.id.cb8)
    CheckBox cb8;
    @Bind(R.id.cb9)
    CheckBox cb9;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_nature);
        ButterKnife.bind(this);
        tvTitle.setText("企业性质");
    }
    @OnClick({R.id.rl1, R.id.rl2, R.id.rl3, R.id.rl4, R.id.rl5, R.id.rl6, R.id.rl7, R.id.rl8, R.id.rl9})
    public void onViewClicked(View view) {
        Intent i = new Intent();
        switch (view.getId()) {
            case R.id.rl1:
                cb1.setChecked(true);
                i.putExtra("nature_type", "国有企业");
                i.putExtra("nature_position","1");
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.rl2:
                cb1.setChecked(true);
                i.putExtra("nature_type", "集体企业");
                i.putExtra("nature_position","2");
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.rl3:
                cb1.setChecked(true);
                i.putExtra("nature_type", "联营企业");
                i.putExtra("nature_position","3");
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.rl4:
                cb1.setChecked(true);
                i.putExtra("nature_type", "股份合作制企业");
                i.putExtra("nature_position","4");
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.rl5:
                cb1.setChecked(true);
                i.putExtra("nature_type", "私营企业");
                i.putExtra("nature_position","5");
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.rl6:
                cb1.setChecked(true);
                i.putExtra("nature_type", "个体户");
                i.putExtra("nature_position","6");
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.rl7:
                cb1.setChecked(true);
                i.putExtra("nature_type", "合伙企业");
                i.putExtra("nature_position","7");
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.rl8:
                cb1.setChecked(true);
                i.putExtra("nature_type", "有限责任公司");
                i.putExtra("nature_position","8");
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.rl9:
                cb1.setChecked(true);
                i.putExtra("nature_type", "股份有限公司");
                i.putExtra("nature_position","9");
                setResult(RESULT_OK, i);
                finish();
                break;
        }
    }
}
