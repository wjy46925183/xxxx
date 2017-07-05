package com.dlg.data.common.model;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：操作按钮对象
 * 创建时间：2017/6/23 15:04
 */
public class ButtonBean implements Serializable{
    /**
     * operateStatusCode : 21
     * operateStatusText : 开工打卡
     * nextStatusCode : 21
     * isGray : true
     */
    private int operateStatusCode;  //(按钮)操作状态值
    private String operateStatusText; //(按钮)操作状态文本
    private int nextStatusCode; //下一个订单状态
    private boolean isGray; //是否置灰 false否  true是

    public int getOperateStatusCode() {
        return operateStatusCode;
    }

    public void setOperateStatusCode(int operateStatusCode) {
        this.operateStatusCode = operateStatusCode;
    }

    public String getOperateStatusText() {
        return operateStatusText;
    }

    public void setOperateStatusText(String operateStatusText) {
        this.operateStatusText = operateStatusText;
    }

    public int getNextStatusCode() {
        return nextStatusCode;
    }

    public void setNextStatusCode(int nextStatusCode) {
        this.nextStatusCode = nextStatusCode;
    }

    public boolean isGray() {
        return isGray;
    }

    public void setGray(boolean gray) {
        isGray = gray;
    }
}
