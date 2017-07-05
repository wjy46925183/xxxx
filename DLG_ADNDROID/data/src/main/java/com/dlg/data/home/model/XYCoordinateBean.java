package com.dlg.data.home.model;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：经纬度与距离对象
 * 创建时间：2017/6/23 13:47
 */
public class XYCoordinateBean implements Serializable{

    private String jobXCoordinate;  //任务坐标x
    private String jobYCoordinate;  //任务坐标y
    private String userXCoordinate;  //x坐标
    private String userYCoordinate;  //y坐标
    private String distanceToJob;   //坐标距离（公里）

    public String getJobXCoordinate() {
        return jobXCoordinate;
    }

    public void setJobXCoordinate(String jobXCoordinate) {
        this.jobXCoordinate = jobXCoordinate;
    }

    public String getJobYCoordinate() {
        return jobYCoordinate;
    }

    public void setJobYCoordinate(String jobYCoordinate) {
        this.jobYCoordinate = jobYCoordinate;
    }

    public String getUserXCoordinate() {
        return userXCoordinate;
    }

    public void setUserXCoordinate(String userXCoordinate) {
        this.userXCoordinate = userXCoordinate;
    }

    public String getUserYCoordinate() {
        return userYCoordinate;
    }

    public void setUserYCoordinate(String userYCoordinate) {
        this.userYCoordinate = userYCoordinate;
    }

    public String getDistanceToJob() {
        return distanceToJob;
    }

    public void setDistanceToJob(String distanceToJob) {
        this.distanceToJob = distanceToJob;
    }
}
