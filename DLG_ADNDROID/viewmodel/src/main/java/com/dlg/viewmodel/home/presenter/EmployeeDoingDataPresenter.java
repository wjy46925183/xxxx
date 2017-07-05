package com.dlg.viewmodel.home.presenter;

import com.dlg.data.home.model.DoingTaskOrderDetailBean;

import java.util.List;

/**
 * 作者：wangdakuan
 * 主要功能：雇员进行中的订单列表数据
 * 创建时间：2017/7/4 09:15
 */
public interface EmployeeDoingDataPresenter {
    void onDoingTaskList(List<DoingTaskOrderDetailBean> taskOrderDetailList);
}
