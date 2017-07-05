package com.dlg.data.home.model;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：雇主地图数据
 * 创建时间：2017/6/23 15:38
 */
public class HomeMapBossListBean implements Serializable {
    private double xCoordinate; // x坐标
    private double yCoordinate; //y坐标
    private String userLogo; //头像
    private String userId; //用户id
    private String distance; //距离

    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
