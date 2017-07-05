package gongren.com.dlg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.utils.ActivityManager;
import gongren.com.dlg.view.PasswordInputView;

/**
 * 设置支付密码
 * Created by lin.li on 2017/2/11.
 */
public class SetPayPasswordActivity extends BaseActivity {

    @Bind(R.id.passwordInputView)
    PasswordInputView mPasswordInputView;

    @Bind(R.id.tv_title)
    TextView tvTitle;
    InputMethodManager inputManager;

    private int type = 0;   //我的钱包进来是1   重置密码是2
    private String identifyingCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpaypassword);
        ButterKnife.bind(this);
        ActivityManager.getActivityManager().addActivity(this);
        initView();
        /**
         * 弹出键盘
         */
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                inputManager = (InputMethodManager) mPasswordInputView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mPasswordInputView, 0);
            }

        }, 500);
    }

    private void initView() {
        type = getIntent().getIntExtra("type", 0);
        try {
            identifyingCode = getIntent().getStringExtra("identifyingCode");
        } catch (Exception e) {
        }

        if (type == 1)
            tvTitle.setText("设置支付密码");
        else if (type == 2)
            tvTitle.setText("重置支付密码");
        else if (type == 3)
            tvTitle.setText("输入支付密码");
        mPasswordInputView.setFocusable(true);
        mPasswordInputView.setFocusableInTouchMode(true);
        mPasswordInputView.requestFocus();

        mPasswordInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6 && type == 3) {
                    setResult(13,new Intent().putExtra("payPwd", mPasswordInputView.getText().toString()));
                }else if(s.length() == 6){
                    Intent intent = new Intent(context, SetPayPasswordSecondActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("identifyingCode", identifyingCode);
                    intent.putExtra("pwd", mPasswordInputView.getText().toString());
                    startActivity(intent);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getActivityManager().finishActivity(this);
        if (inputManager != null)
            inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
