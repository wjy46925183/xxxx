package com.dlg.data.common.model;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：分享数据对象
 * 创建时间：2017/6/23 15:29
 */
public class ShareDataBean implements Serializable{

    private String taskId; //任务ID
    private String taskTitle; //分享标题
    private String taskDescription; //任务介绍
    private String detailsUrl; //分享地址
    private double xCoordinate; // x坐标
    private double yCoordinate;  //y坐标
    private String userLogo; //用户头像

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

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
}
