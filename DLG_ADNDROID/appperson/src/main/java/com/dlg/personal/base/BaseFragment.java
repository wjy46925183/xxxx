package com.dlg.personal.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.cache.ACache;
import com.common.string.LogUtils;
import com.dlg.viewmodel.BasePresenter;
import com.dlg.viewmodel.BaseViewModel;


/**
 * 作者：wangdakuan
 * 主要功能：Fragment 基类页面
 * 创建时间：2017/6/1 12:00
 */
public class BaseFragment extends Fragment implements BasePresenter {

    protected Context mContext;
    protected BaseActivity mActivity;
    protected BaseViewModel mViewModel;
    protected ACache mACache;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if(mContext instanceof BaseActivity){
            mActivity = (BaseActivity) mContext;
        }
        mACache = ACache.get(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * activity 添加fragment
     *
     * @param containerViewId 需要添加的fragment位置ID
     * @param fragment        fragment 对象实例
     * @param tag             fragment 标记
     */
    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
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
        FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }


    /**
     * 获取Fragment
     *
     * @param tag
     * @param <T>
     * @return
     */
    public <T extends Fragment> T getFragment(String tag) {
        return (T)mActivity.getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null != mViewModel){
            mViewModel.onDestroyView();
        }
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
        LogUtils.e("=========请求成功============"+msg);
    }

    @Override
    public void requestError(String msg) {
        LogUtils.e("=========请求异常============"+msg);
    }

    @Override
    public void logInError() {
        LogUtils.e("=========请求登录异常============");
    }
}
