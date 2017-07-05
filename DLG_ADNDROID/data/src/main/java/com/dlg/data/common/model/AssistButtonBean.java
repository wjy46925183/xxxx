package com.dlg.data.common.model;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：辅助按钮对象
 * 创建时间：2017/6/23 15:06
 */
public class AssistButtonBean implements Serializable{

    /**
     * buttonText : 取消
     * buttonStatus : 101
     * dataWeight : 5
     * dataCode : assist_btn1
     * groupCode : assist_btn
     */
    private String buttonText; //按钮名称
    private String buttonStatus; //按钮状态
    private int dataWeight; //按钮权重
    private String dataCode; //按钮标识码
    private String groupCode; //按钮所属组

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonStatus() {
        return buttonStatus;
    }

    public void setButtonStatus(String buttonStatus) {
        this.buttonStatus = buttonStatus;
    }

    public int getDataWeight() {
        return dataWeight;
    }

    public void setDataWeight(int dataWeight) {
        this.dataWeight = dataWeight;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
