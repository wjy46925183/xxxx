package com.dlg.viewmodel;

import okhttp.rx.JsonResponse;
import rx.Subscriber;

/**
 * 作者：wangdakuan
 * 主要功能：订阅者监听
 * 创建时间：2016/12/26 17:02
 */
public abstract class RXSubscriber<T,M> extends Subscriber<T>{

    private BasePresenter presenter;

    public RXSubscriber(BasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {
        if(null != presenter){
            presenter.requestComplete();
        }
    }

    @Override
    public void onError(Throwable e) {
        if(e.getMessage().contains("登录失败")){
            if(null != presenter){
                presenter.logInError();
            }
        }
        if(null != presenter){
            presenter.requestError(e.getMessage());
        }
    }

    @Override
    public void onNext(T t) {
        if(t instanceof JsonResponse){
            JsonResponse jsonResponse = (JsonResponse) t;
            requestNext((M) jsonResponse.getData());
            if(null != presenter){
                presenter.requestNext(jsonResponse.getMsg());
            }
        }else {
            requestNext((M) t);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(null != presenter){
            presenter.requestStart();
        }
    }

    public abstract void requestNext(M m);
}
