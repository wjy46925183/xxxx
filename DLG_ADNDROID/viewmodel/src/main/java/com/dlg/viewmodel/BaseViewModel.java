package com.dlg.viewmodel;

import android.content.Context;

import rx.Subscriber;

/**
 * 作者：wangdakuan
 * 主要功能：
 * 创建时间：2017/6/1 15:49
 */
public class BaseViewModel<T> implements BaseListening{
    protected Subscriber<T> mSubscriber;
    protected Context mContext;
    @Override
    public void onDestroyView() {
        if(null != mSubscriber){
            mSubscriber.unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        if(null != mSubscriber){
            mSubscriber.unsubscribe();
        }
    }

    @Override
    public void onResume() {

    }
}
