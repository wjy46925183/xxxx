package com.dlg.viewmodel.home.presenter;

import com.dlg.data.home.model.BossListBean;

import java.util.List;

/**
 * 作者：wangdakuan
 * 主要功能：雇主列表数据接口
 * 创建时间：2017/6/27 09:05
 */
public interface HirerListPresenter {
    void getListData(List<BossListBean> beans);
}
