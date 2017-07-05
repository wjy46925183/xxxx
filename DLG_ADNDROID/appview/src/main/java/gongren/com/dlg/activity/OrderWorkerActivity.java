package gongren.com.dlg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.fragment.Worker_GuYuanFragment;
import gongren.com.dlg.fragment.Worker_GuZhuFragment;

/**
 * 雇员订单管理Fragment
 */
public class OrderWorkerActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.rb_01)
    RadioButton rb01;
    @Bind(R.id.rb_02)
    RadioButton rb02;
    @Bind(R.id.rb_03)
    RadioButton rb03;
    @Bind(R.id.rb_04)
    RadioButton rb04;
    @Bind(R.id.rg_orderact)
    RadioGroup rgOrderact;
    @Bind(R.id.fragment_orderact)
    FrameLayout fragmentOrderact;

    private boolean isGuYuan = true;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    //雇主的我的零工四个列表
    //全部（0）
    private Worker_GuZhuFragment fragment01 = null;
    //进行中（1）
    private Worker_GuZhuFragment fragment02 = null;
    //待付款（2）
    private Worker_GuZhuFragment fragment03 = null;
    //已完成（3）
    private Worker_GuZhuFragment fragment04 = null;
//3662  8530 3914    16106
    //雇员的我的零工四个列表
    //全部（0）
    private Worker_GuYuanFragment fragment05 = null;
    //进行中（1）
    private Worker_GuYuanFragment fragment06 = null;
    //待付款（2）
    private Worker_GuYuanFragment fragment07 = null;
    //已完成（3）
    private Worker_GuYuanFragment fragment08 = null;

    private Handler handler = null;
    private int currentItem = 0;//当前显示的是哪一个。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        handler = new Handler();

        getParams();
        initView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//        UmengShareUtil.initShare();
    }

    public void getParams() {
        Intent intent = getIntent();
        if (intent != null) {
            isGuYuan = intent.getBooleanExtra("isGuYuan", true);
            if (isGuYuan) {
                tvSave.setVisibility(View.GONE);
                rb03.setText("待收款");
            } else {
                tvSave.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initView() {
        tvTitle.setText("我的零工");
        if(isGuYuan){
            tvSave.setText("卖自己");
        }else{
            tvSave.setText("发布");
        }


        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        if (isGuYuan) {//是雇员
            tvSave.setVisibility(View.VISIBLE);

            //全部（0）
            fragment05 = new Worker_GuYuanFragment();
            fragment05.setIndex(0);

            //进行中（1）
            fragment06 = new Worker_GuYuanFragment();
            fragment06.setIndex(1);

            //待付款（2）
            fragment07 = new Worker_GuYuanFragment();
            fragment07.setIndex(2);

            //已完成（3）
            fragment08 = new Worker_GuYuanFragment();
            fragment08.setIndex(3);
        } else {//是雇主
            tvSave.setVisibility(View.VISIBLE);

            //全部（0）
            fragment01 = Worker_GuZhuFragment.newInstance();
            fragment01.setIndex(0);

            //进行中（1）
            fragment02 = Worker_GuZhuFragment.newInstance();
            fragment02.setIndex(1);

            //待付款（2）
            fragment03 = Worker_GuZhuFragment.newInstance();
            fragment03.setIndex(2);

            //已完成（3）
            fragment04 = Worker_GuZhuFragment.newInstance();
            fragment04.setIndex(3);
        }
        showFragment(1);
    }

    public Handler getHandler() {
        return handler;
    }

    private void showFragment(int index) {
        transaction = manager.beginTransaction();
        switch (index) {
            case 1:
                if (isGuYuan) {//雇员
                    if (fragment05 != null) {
                        transaction.replace(R.id.fragment_orderact, fragment05);
                    }
                } else {//雇主
                    if (fragment01 != null) {
                        transaction.replace(R.id.fragment_orderact, fragment01);
                    }
                }
                break;
            case 2:
                if (isGuYuan) {//雇员
                    if (fragment06 != null) {
                        transaction.replace(R.id.fragment_orderact, fragment06);
                    }
                } else {//雇主
                    if (fragment02 != null) {
                        transaction.replace(R.id.fragment_orderact, fragment02);
                    }
                }
                break;
            case 3:
                if (isGuYuan) {//雇员
                    if (fragment07 != null) {
                        transaction.replace(R.id.fragment_orderact, fragment07);
                    }
                } else {//雇主
                    if (fragment03 != null) {
                        transaction.replace(R.id.fragment_orderact, fragment03);
                    }
                }
                break;
            case 4:
                if (isGuYuan) {//雇员
                    if (fragment08 != null) {
                        transaction.replace(R.id.fragment_orderact, fragment08);
                    }
                } else {//雇主
                    if (fragment04 != null) {
                        transaction.replace(R.id.fragment_orderact, fragment04);
                    }
                }
                break;
        }
        transaction.commit();

    }

    @OnClick({R.id.iv_back, R.id.rb_01, R.id.rb_02, R.id.rb_03, R.id.rb_04, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rb_01:
                showFragment(1);
                break;
            case R.id.rb_02:
                showFragment(2);
                break;
            case R.id.rb_03:
                showFragment(3);
                break;
            case R.id.rb_04:
                showFragment(4);
                break;
            case R.id.tv_save://发布
                if (isGuYuan) {
                    startActivity(new Intent(context, MySericeActivity.class));
                } else {
                    startActivity(new Intent(context, ReleaseXQActivity.class));
                }

                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
