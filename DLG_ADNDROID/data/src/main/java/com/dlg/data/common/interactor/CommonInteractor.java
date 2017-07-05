package com.dlg.data.common.interactor;

import com.dlg.data.common.model.ActionButtonsBean;
import com.dlg.data.common.model.ShareDataBean;

import java.util.HashMap;

import rx.Observable;

/**
 * 作者：wangdakuan
 * 主要功能：公用模块接口
 * 创建时间：2017/6/23 09:57
 */
public interface CommonInteractor {
    /**
     * 订单操作按钮数据接口
     * @param hashMap
     * @return
     */
    Observable<ActionButtonsBean> getActionButtons(HashMap<String,String> hashMap);

    /**
     * 获取分享内容数据
     * @param hashMap
     * @return
     */
    Observable<ShareDataBean> getShareData(HashMap<String,String> hashMap);
}
