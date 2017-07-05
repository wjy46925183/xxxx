package com.dlg.viewmodel.home;

import android.content.Context;

import com.dlg.data.home.model.KeyHotBean;
import com.dlg.viewmodel.BasePresenter;
import com.dlg.viewmodel.BaseViewModel;
import com.dlg.viewmodel.RXSubscriber;
import com.dlg.viewmodel.home.presenter.KeyHotSelectionPresenter;
import com.dlg.viewmodel.server.HomeServer;

import java.util.HashMap;
import java.util.List;

import okhttp.rx.JsonResponse;
import rx.Subscriber;

/**
 * 作者：小明
 * 主要功能：
 * 创建时间：2017/6/28 0028 16:04
 */
public class KeyHotSelectionViewModle extends BaseViewModel<JsonResponse<List<KeyHotBean>>> {
    private BasePresenter basePresenter;
    private final HomeServer mServer;
    private KeyHotSelectionPresenter keyHotSelectionPresenter;

    public KeyHotSelectionViewModle(Context context,BasePresenter basePresenter, KeyHotSelectionPresenter keyHotSelectionPresenter) {
        this.basePresenter = basePresenter;
        mServer = new HomeServer(context);
        this.keyHotSelectionPresenter=keyHotSelectionPresenter;
    }

    public void getKeyHotData(String type, String sign) {
        HashMap<String,String> map = new HashMap<>();
        map.put("groupCode",type);
        map.put("sign",sign);
        mSubscriber=getKeyHotText();
        mServer.getKeyHotData(mSubscriber,map);
    }
    private Subscriber<JsonResponse<List<KeyHotBean>>>getKeyHotText(){
        return new RXSubscriber<JsonResponse<List<KeyHotBean>>, List<KeyHotBean>>(basePresenter) {
            @Override
            public void requestNext(List<KeyHotBean> keyHotBeen) {
                keyHotSelectionPresenter.getKeyHot(keyHotBeen);
            }
        };
    }
}
