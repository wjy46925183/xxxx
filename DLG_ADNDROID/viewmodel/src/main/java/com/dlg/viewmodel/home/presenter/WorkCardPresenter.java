package com.dlg.viewmodel.home.presenter;

import com.dlg.data.home.model.WorkCardBean;

import java.util.List;

/**
 * 作者：王进亚
 * 主要功能：
 * 创建时间：2017/6/30 16:22
 */

public interface WorkCardPresenter {
    void getCard(List<WorkCardBean> cardBeans);
}
