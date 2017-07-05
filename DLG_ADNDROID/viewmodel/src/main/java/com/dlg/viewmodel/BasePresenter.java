package com.dlg.viewmodel;

/**
 * 作者：wangdakuan
 * 主要功能：基础回调监听
 * 创建时间：2017/6/22 19:28
 */
public interface BasePresenter {
    /**
     * 开始请求
     */
    void requestStart();

    /**
     * 请求完成
     */
    void requestComplete();

    /**
     * 请求回调
     */
    void requestNext(String msg);

    /**
     * 请求异常
     */
    void requestError(String msg);

    /**
     * 请求登录异常
     */
    void logInError();
}
