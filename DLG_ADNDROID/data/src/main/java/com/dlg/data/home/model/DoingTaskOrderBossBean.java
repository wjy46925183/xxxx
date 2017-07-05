package com.dlg.data.home.model;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：雇主订单数据
 * 创建时间：2017/6/23 16:53
 */
public class DoingTaskOrderBossBean extends DoingTaskOrderDetailBean implements Serializable{
    private String GroupId;
    private String phone;
    private String location;
    private String cityId;
    private String cityName;
    private String serviceAmount;
    private String amount;
    private String height;
    private String description;
    private long modifyTime;
    private int isCancel;
    private String parentJobId;
    private String sex;
    private String weight;
    private String isEvaluate;
    private String areaId;
    private String identity;
    private String distance;
    private Double releaseCount;
    private String jobTitle;
    private int meterUnit;
    private String evaluateLevel;
    private String auditPermission;
    private int joinCount;
    private String cancelAmount;

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(String serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(int isCancel) {
        this.isCancel = isCancel;
    }

    public String getParentJobId() {
        return parentJobId;
    }

    public void setParentJobId(String parentJobId) {
        this.parentJobId = parentJobId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getIsEvaluate() {
        return isEvaluate;
    }

    public void setIsEvaluate(String isEvaluate) {
        this.isEvaluate = isEvaluate;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Double getReleaseCount() {
        return releaseCount;
    }

    public void setReleaseCount(Double releaseCount) {
        this.releaseCount = releaseCount;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getMeterUnit() {
        return meterUnit;
    }

    public void setMeterUnit(int meterUnit) {
        this.meterUnit = meterUnit;
    }

    public String getEvaluateLevel() {
        return evaluateLevel;
    }

    public void setEvaluateLevel(String evaluateLevel) {
        this.evaluateLevel = evaluateLevel;
    }

    public String getAuditPermission() {
        return auditPermission;
    }

    public void setAuditPermission(String auditPermission) {
        this.auditPermission = auditPermission;
    }

    public int getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(int joinCount) {
        this.joinCount = joinCount;
    }

    public String getCancelAmount() {
        return cancelAmount;
    }

    public void setCancelAmount(String cancelAmount) {
        this.cancelAmount = cancelAmount;
    }
}
