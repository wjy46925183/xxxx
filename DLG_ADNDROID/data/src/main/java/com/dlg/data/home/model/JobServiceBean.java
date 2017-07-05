package com.dlg.data.home.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：wangdakuan
 * 主要功能：零工服务数据类
 * 创建时间：2017/6/30 10:09
 */
public class JobServiceBean implements Serializable{
    /**
     * id : 27635454542777686907021470611
     * active : 1
     * version : 1
     * createTime : 1498120993000
     * createUserId : 27624578803640429833423460393
     * modifyTime : 1498120993000
     * modifyUserId : null
     * userId : 27624578803640429833423460393
     * demandType : 1
     * serviceName : 我来了图片
     * serviceType : job.type_5
     * serviceTypeName : 教育培训
     * serviceMeterUnit : 1
     * serviceMeterUnitName : null
     * price : 2220
     * serviceDescription : 发生发生的发山东发生发生发生
     * auditStatus : 2
     * auditUserId : null
     * auditUserName : null
     * auditRemark : null
     * auditDate : null
     * images : null
     * imagesUrl : null
     * name : null
     * logo : null
     * imagesUrlList : null
     * detailsUrl : http://dlg.dalinggong.com/api****?serviceId=27635454542777686907021470611
     */
    private String id; //ID
    private int active;
    private int version;
    private long createTime; //创建时间
    private String createUserId; //创建者ID
    private long modifyTime; //修改时间
    private String modifyUserId; //修改者ID
    private String userId; //用户ID
    private int demandType; //类型
    private String serviceName; //服务名称
    private String serviceType; //服务类型
    private String serviceTypeName; //服务类型名称
    private int serviceMeterUnit; //服务单位
    private String serviceMeterUnitName; //服务单位名称
    private double price; //价格
    private String serviceDescription; //服务信息
    private int auditStatus; //
    private String auditUserId;
    private String auditUserName;
    private String auditRemark;
    private String auditDate;
    private String images;
    private String imagesUrl; //图片
    private String name;
    private String logo; //Logo图片
    private List<String> imagesUrlList; //图片数据集合
    private String detailsUrl; //详情链接

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDemandType() {
        return demandType;
    }

    public void setDemandType(int demandType) {
        this.demandType = demandType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public int getServiceMeterUnit() {
        return serviceMeterUnit;
    }

    public void setServiceMeterUnit(int serviceMeterUnit) {
        this.serviceMeterUnit = serviceMeterUnit;
    }

    public String getServiceMeterUnitName() {
        return serviceMeterUnitName;
    }

    public void setServiceMeterUnitName(String serviceMeterUnitName) {
        this.serviceMeterUnitName = serviceMeterUnitName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(String auditUserId) {
        this.auditUserId = auditUserId;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<String> getImagesUrlList() {
        return imagesUrlList;
    }

    public void setImagesUrlList(List<String> imagesUrlList) {
        this.imagesUrlList = imagesUrlList;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }
}
