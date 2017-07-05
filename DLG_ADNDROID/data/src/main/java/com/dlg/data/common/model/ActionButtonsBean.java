package com.dlg.data.common.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：wangdakuan
 * 主要功能：操作按钮对象类
 * 创建时间：2017/6/23 15:00
 */
public class ActionButtonsBean implements Serializable {
    private String businessNumber; ////订单业务编号
    private int roleType;  //角色类型(1.雇员,2.雇主,3.系统)
    private int statusCode; //状态代码
    private String statusText;  //状态文本
    private CountdownBean countdownVo;  //订单提示语对象
    private List<ButtonBean> buttonList; //操作按钮
    private List<AssistButtonBean> assistButtonList; //辅助按钮

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public CountdownBean getCountdownVo() {
        return countdownVo;
    }

    public void setCountdownVo(CountdownBean countdownVo) {
        this.countdownVo = countdownVo;
    }

    public List<ButtonBean> getButtonList() {
        return buttonList;
    }

    public void setButtonList(List<ButtonBean> buttonList) {
        this.buttonList = buttonList;
    }

    public List<AssistButtonBean> getAssistButtonList() {
        return assistButtonList;
    }

    public void setAssistButtonList(List<AssistButtonBean> assistButtonList) {
        this.assistButtonList = assistButtonList;
    }
}
