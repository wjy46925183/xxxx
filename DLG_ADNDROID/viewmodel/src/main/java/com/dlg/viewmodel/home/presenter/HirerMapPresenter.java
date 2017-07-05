package com.dlg.viewmodel.home.presenter;

import com.dlg.data.home.model.HomeMapBossListBean;

import java.util.List;

/**
 * 作者：王进亚
 * 主要功能：地图数据回调接口
 * 创建时间：2017/6/27 10:44
 */

public interface HirerMapPresenter {
    void toMapList(List<HomeMapBossListBean> homeMapListBeans);
}
