package com.dlg.viewmodel.home.presenter;

import com.dlg.data.home.model.EmployeeListBean;

import java.util.List;

/**
 * Created by wangjinya on 2017/6/20.
 */

public interface EmployeeListPresenter{
    void getListData(List<EmployeeListBean> beans);
}
