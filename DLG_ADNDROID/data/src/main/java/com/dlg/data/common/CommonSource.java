package com.dlg.data.common;

import com.dlg.data.cache.ObjectCache;
import com.dlg.data.common.interactor.CommonInteractor;
import com.dlg.data.common.model.ActionButtonsBean;
import com.dlg.data.common.model.ShareDataBean;
import com.dlg.data.common.url.CommonUrl;
import com.http.okgo.OkGo;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonConvert;
import okhttp.rx.JsonResponse;
import okhttp.rx.RxAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 作者：wangdakuan
 * 主要功能：实现接口
 * 创建时间：2017/6/23 10:03
 */
public class CommonSource implements CommonInteractor{
    private final ObjectCache objectCache;

    /**
     */
    public CommonSource(ObjectCache objectCache) {
        this.objectCache = objectCache;
    }

    private Action1<Object> saveToCacheAction(final String key) {
        return new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o != null) {
                    CommonSource.this.objectCache.put(o, key);
                }
            }
        };
    }

    @Override
    public Observable<ActionButtonsBean> getActionButtons(HashMap<String, String> hashMap) {
        return OkGo.post(CommonUrl.GET_ACTION_BUTTONS)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<ActionButtonsBean>>>() {
                }, RxAdapter.<JsonResponse<List<ActionButtonsBean>>>create())
                .map(new Func1<JsonResponse<List<ActionButtonsBean>>, ActionButtonsBean>() {
                    @Override
                    public ActionButtonsBean call(JsonResponse<List<ActionButtonsBean>> responses) {
                        if(null != responses && null != responses.getData()
                                && responses.getData().size()>0){
                            return responses.getData().get(0);
                        }
                        return null;
                    }
                })
//                .doOnNext(saveToCacheAction(HomeUrl.TAB_HOME+ JSON.toJSONString(hashMap)))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ShareDataBean> getShareData(HashMap<String, String> hashMap) {
        return OkGo.post(CommonUrl.SHARE_DATA)//
                .params(hashMap)
                .getCall(new JsonConvert<JsonResponse<List<ShareDataBean>>>() {
                }, RxAdapter.<JsonResponse<List<ShareDataBean>>>create())
                .map(new Func1<JsonResponse<List<ShareDataBean>>, ShareDataBean>() {
                    @Override
                    public ShareDataBean call(JsonResponse<List<ShareDataBean>> responses) {
                        if(null != responses && null != responses.getData()
                                && responses.getData().size()>0){
                            return responses.getData().get(0);
                        }
                        return null;
                    }
                })
//                .doOnNext(saveToCacheAction(HomeUrl.TAB_HOME+ JSON.toJSONString(hashMap)))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
