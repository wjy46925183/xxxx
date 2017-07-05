package com.dlg.data.home.model;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：首页地图数据
 * 创建时间：2017/6/23 14:42
 */
public class HomeMapListBean implements Serializable {

    private String id; //任务id
    private String price;  //单价，橙子数量
    private int jobMeterUnit; //计量单位
    private String jobMeterUnitName;// 计量单位名称时,天,单
    private double xCoordinate;// x坐标
    private double yCoordinate;// y坐标

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getJobMeterUnit() {
        return jobMeterUnit;
    }

    public void setJobMeterUnit(int jobMeterUnit) {
        this.jobMeterUnit = jobMeterUnit;
    }

    public String getJobMeterUnitName() {
        return jobMeterUnitName;
    }

    public void setJobMeterUnitName(String jobMeterUnitName) {
        this.jobMeterUnitName = jobMeterUnitName;
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

    @Override
    public String toString() {
        return "HomeMapListBean{" +
                "id='" + id + '\'' +
                ", price='" + price + '\'' +
                ", jobMeterUnit=" + jobMeterUnit +
                ", jobMeterUnitName='" + jobMeterUnitName + '\'' +
                ", xCoordinate='" + xCoordinate + '\'' +
                ", yCoordinate='" + yCoordinate + '\'' +
                '}';
    }
}
