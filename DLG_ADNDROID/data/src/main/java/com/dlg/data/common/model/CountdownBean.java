package com.dlg.data.common.model;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：订单提示语对象
 * 创建时间：2017/6/23 15:02
 */
public class CountdownBean implements Serializable{
    /**
     * thisDate : 1494827265225
     * remainingTime : 4334775remainingTime
     * mapTipsText : 后开工
     * otherMapTipsText : 请到地图上蓝色范围内进行
     */

    private long thisDate; //系统当前时间
    private int remainingTime; //剩余时间
    private String mapTipsText; //提示文本
    private String otherMapTipsText; //地图提示文本

    public long getThisDate() {
        return thisDate;
    }

    public void setThisDate(long thisDate) {
        this.thisDate = thisDate;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getMapTipsText() {
        return mapTipsText;
    }

    public void setMapTipsText(String mapTipsText) {
        this.mapTipsText = mapTipsText;
    }

    public String getOtherMapTipsText() {
        return otherMapTipsText;
    }

    public void setOtherMapTipsText(String otherMapTipsText) {
        this.otherMapTipsText = otherMapTipsText;
    }
}
