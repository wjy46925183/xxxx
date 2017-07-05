package com.dlg.personal.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.common.cache.ACache;
import com.common.string.LogUtils;
import com.common.sys.RuleUtils;
import com.common.sys.SystemBarTintManager;
import com.dlg.personal.app.ActivityNavigator;
import com.dlg.viewmodel.BasePresenter;
import com.dlg.viewmodel.BaseViewModel;

/**
 * 作者：wangdakuan
 * 主要功能：Activity 基类
 * 创建时间：2017/6/1 11:59
 */
public class BaseActivity extends AppCompatActivity implements BasePresenter {

    protected BaseViewModel mViewModel;
    protected Context mContext;
    protected BaseBarHelper mToolBarHelper;
    private SystemBarTintManager tintManager;
    protected ACache mACache;
    protected BaseActivity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 取消显示标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //判断当前系统版本是否>=Andoird4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //设置状态栏背景状态
            //true：表明当前Android系统版本>=4.4
            setTranslucentStatus(true);
            //实例化SystemBarTintManager
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // 通知标题栏所需颜色
            tintManager.setStatusBarTintResource(this.getResources().getColor(android.R.color.transparent));
        }
        mACache = ACache.get(this);
        ActivityNavigator.navigator().addActivity(this);
        mContext = this;
        LogUtils.e(this.getClass().getSimpleName());
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 初始化布局
     * @param layoutResID 布局ID
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = this.getLayoutInflater().inflate(layoutResID, null);
        mToolBarHelper = new BaseBarHelper(this);
        setContentView(view, ToolBarType.Default);
    }

    /**
     * 设置头部为隐藏
     * @param view
     */
    public void setToolBarGone(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //设置状态栏背景状态
            //true：表明当前Android系统版本>=4.4
            view.setPadding(0, RuleUtils.getStatusBarHeight(mContext), 0, 0);
        }
        if (null != mToolBarHelper) {
            mToolBarHelper.setVisibility(View.GONE);
        }
    }

    /**
     * 设置头部为显示
     * @param view
     */
    public void setToolBarVisibility(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //设置状态栏背景状态
            //true：表明当前Android系统版本>=4.4
            view.setPadding(0, RuleUtils.getStatusBarHeight(mContext), 0, 0);
        }
        if (null != mToolBarHelper) {
            mToolBarHelper.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 动态添加头部
     * @param layoutResID
     * @param type
     */
    public void setContentView(@LayoutRes int layoutResID, ToolBarType type) {
        View view = this.getLayoutInflater().inflate(layoutResID, null);
        if (type == ToolBarType.NO) {
            super.setContentView(view);
        } else {
            if (type == ToolBarType.Home) {
                mToolBarHelper = new HomeToolBarHelper(this);
            } else {
                mToolBarHelper = new BaseBarHelper(this);
            }
            setContentView(view, type);
        }
    }

    /**
     * 动态添加头部过滤判断
     * @param view
     * @param type
     */
    private void setContentView(View view, ToolBarType type) {
        if (view instanceof DrawerLayout) {
            addTitleView(view, ((DrawerLayout) view).getChildAt(0), type);
        } else {
            addTitleView(view, view, type);
        }
        ImageView back = mToolBarHelper.getToolbarBack();
        if (null != back) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityNavigator.navigator().onBackPressed();
                }
            });
        }
    }

    /**
     * 动态添加头布局
     * @param parentView
     * @param view
     * @param type
     */
    private void addTitleView(View parentView, View view, ToolBarType type) {
        if (view instanceof LinearLayout) {
            ((LinearLayout) view).addView(mToolBarHelper, 0);
            super.setContentView(parentView);
        }
        if (type == ToolBarType.Default && (view instanceof RelativeLayout || view instanceof FrameLayout)) {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            //设置LinearLayout属性(宽和高)
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layout.setLayoutParams(layoutParams);
            layout.addView(mToolBarHelper, 0);
            layout.addView(view);
            super.setContentView(layout);
        }
        if (type == ToolBarType.Suspension && (view instanceof RelativeLayout || view instanceof FrameLayout)) {
            ((ViewGroup) view).addView(mToolBarHelper);
            super.setContentView(view);
        } else {
            if (type == ToolBarType.Suspension) {
                RelativeLayout layout = new RelativeLayout(this);
                //设置LinearLayout属性(宽和高)
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                layout.setLayoutParams(layoutParams);
                layout.addView(view);
                layout.addView(mToolBarHelper);
                super.setContentView(layout);
            }
        }
    }

    /**
     * activity 添加fragment
     *
     * @param containerViewId 需要添加的fragment位置ID
     * @param fragment        fragment 对象实例
     * @param tag             fragment 标记
     */
    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * activity 添加fragment
     *
     * @param containerViewId 需要添加的fragment位置ID
     * @param fragment        fragment 对象实例
     * @param tag             fragment 标记
     */
    protected void addFragmentToBackStack(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * fragment 返回
     */
    protected void onBackFragment() {
        getSupportFragmentManager().popBackStack();
    }

    /**
     * 获取Fragment
     *
     * @param tag
     * @param <T>
     * @return
     */
    public <T extends Fragment> T getFragment(String tag) {
        return (T) getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mViewModel) {
            mViewModel.onDestroy();
        }
        ActivityNavigator.navigator().removerActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mViewModel) {
            mViewModel.onResume();
        }
    }

    public BaseBarHelper getToolBarHelper() {
        return mToolBarHelper;
    }

    @Override
    public void requestStart() {
        LogUtils.e("=========开始请求============");
    }

    @Override
    public void requestComplete() {
        LogUtils.e("=========请求完成============");
    }

    @Override
    public void requestNext(String msg) {
        LogUtils.e("=========请求成功============");
    }

    @Override
    public void requestError(String msg) {
        LogUtils.e("=========请求异常============");
    }

    @Override
    public void logInError() {
        LogUtils.e("=========请求登录异常============");
    }
}
