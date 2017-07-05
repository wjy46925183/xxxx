package com.dlg.data.home.model;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：雇员进行中订单数据对象
 * 创建时间：2017/6/23 13:46
 */
public class DoingTaskOrderDetailBean implements Serializable{
    private String id;                 //订单Id
    private String employeeId;       //雇员Id
    private String employerId;       //雇主Id
    private String status;            //订单状态
    private String businessNumber;   //订单编号
    private String logo;               //用户头像
    private String isInProgress;     //是否进行中(0.否,1.是)"
    private Integer currentOperateStatus;        //需求类型(1.工作日,2.双休日,3.计件)"
    private String jobId;             //任务id
    private String postName;          //任务名
    private String postType;          //任务类型
    private String name;               //任务对接人
    private String scoreCount;        //评分统计
    private String creditCount;       //诚信统计
    private String price;              //订单单价
    private String totalPrice;        //订单总价
    private String postTypeName;
    private String meterUnitName;         //1, 零工计量单位(1.天,2.时,3.单)
    private XYCoordinateBean xyCoordinateVo;   //任务坐标信息
    private String startDate;                //订单开始时间
    private String endDate;                  //订单结束时间
    private String provinceName;            //任务地址(省)
    private String tipAmount; //小费
    private String modifyUserId; //小费
    private String workAddress;             //任务地址信息(市)
    private String villageName;             //任务地址详情(镇)
    private String areaName;               //任务地址详情(区)
    private int demandType;
    private int isFarmersInsurance;        //保险字段

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIsInProgress() {
        return isInProgress;
    }

    public void setIsInProgress(String isInProgress) {
        this.isInProgress = isInProgress;
    }

    public Integer getCurrentOperateStatus() {
        return currentOperateStatus;
    }

    public void setCurrentOperateStatus(Integer currentOperateStatus) {
        this.currentOperateStatus = currentOperateStatus;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(String scoreCount) {
        this.scoreCount = scoreCount;
    }

    public String getCreditCount() {
        return creditCount;
    }

    public void setCreditCount(String creditCount) {
        this.creditCount = creditCount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPostTypeName() {
        return postTypeName;
    }

    public void setPostTypeName(String postTypeName) {
        this.postTypeName = postTypeName;
    }

    public String getMeterUnitName() {
        return meterUnitName;
    }

    public void setMeterUnitName(String meterUnitName) {
        this.meterUnitName = meterUnitName;
    }

    public XYCoordinateBean getXyCoordinateVo() {
        return xyCoordinateVo;
    }

    public void setXyCoordinateVo(XYCoordinateBean xyCoordinateVo) {
        this.xyCoordinateVo = xyCoordinateVo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getDemandType() {
        return demandType;
    }

    public void setDemandType(int demandType) {
        this.demandType = demandType;
    }

    public int getIsFarmersInsurance() {
        return isFarmersInsurance;
    }

    public void setIsFarmersInsurance(int isFarmersInsurance) {
        this.isFarmersInsurance = isFarmersInsurance;
    }
}
