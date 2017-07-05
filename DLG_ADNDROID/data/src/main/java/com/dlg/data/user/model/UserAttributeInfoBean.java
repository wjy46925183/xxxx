package com.dlg.data.user.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：用户属性(基本信息)
 * 创建时间：2017/6/30 14:28
 */
public class UserAttributeInfoBean implements Serializable {
    /**
     * "userId": 2.751236767730612e+28, // 用户Id
     * "name": null, //用户真实姓名
     * "logo": null,//logo
     * "releaseCount": null, //发单统计
     * "scoreCount": null,//评分统计
     * "creditCount": null,//诚信统计
     * "distance": "10公里",//'距离
     * "description": null,//描述
     * "certificateType": null,//证件类型(0.身份证)
     * "certificateNumber": null,//证件号码
     * "joinCount": null,//接单统计
     * "height": null,//身高
     * "weight": null,体重
     * "sex": null,//性别(1.男,2.女,3.保密)
     * "isHealthCertificate": null,//是否有健康证(0.否,1.是)
     * "degree": null,//学历
     * "identity": null,//用户身份(0.学生,1.自由工作者,2.兼职人员)
     * "role": null,//当前角色(1.顾主,2.雇员)
     * `auditStatus` :'审核状态(0.未审核,1.审核中,2.审核通过,3.审核失败)',
     */
    private String id;
    private String userId;   // 用户Id
    private String name; //用户真实姓名
    private String logo;//logo
    private String releaseCount; //发单统计
    private String scoreCount;//评分统计
    private String creditCount;//诚信统计
    private String distance;//'距离
    private String description;//描述
    private String certificateType;//证件类型(0.身份证)
    private String certificateNumber;//证件号码
    private String joinCount;//接单统计
    private String height;//身高
    private String weight;//体重
    private String sex;//性别(1.男,2.女,3.保密)
    private String isHealthCertificate;//是否有健康证(0.否,1.是)
    private String degree;//学历
    private String identity;//用户身份(0.学生,1.自由工作者,2.兼职人员)
    private String role;//当前角色(1.顾主,2.雇员)
    private String location; //地址
    private String personalizedSignature; // 个性化的签名
    private String cancelCount; // 取消数
    private String lateCount; // 年末数
    private String serviceCount; //服务数
    private String auditStatus; //'审核状态(0.未审核,1.审核中,2.审核通过,3.审核失败)',

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getReleaseCount() {
        return releaseCount;
    }

    public void setReleaseCount(String releaseCount) {
        this.releaseCount = releaseCount;
    }

    public String getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(String scoreCount) {
        this.scoreCount = scoreCount;
    }

    public String getCreditCount() {
        if (TextUtils.isEmpty(creditCount)) {
            return "36.5";
        }
        return creditCount;
    }

    public void setCreditCount(String creditCount) {
        this.creditCount = creditCount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(String joinCount) {
        this.joinCount = joinCount;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIsHealthCertificate() {
        return isHealthCertificate;
    }

    public void setIsHealthCertificate(String isHealthCertificate) {
        this.isHealthCertificate = isHealthCertificate;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getIdentity() {
        return identity;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getIdentityName() {
        String nameType = "自由工作者";
        if(TextUtils.isEmpty(identity) || "null".equals(identity)){
            return nameType;
        }
        int _identity = Integer.parseInt(identity);
        switch (_identity) {
            case 0:
                nameType = "在校学生";
                break;
            case 1:
                nameType = "自由工作者";
                break;
            case 2:
                nameType = "兼职人员";
                break;
        }
        return nameType;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPersonalizedSignature() {
        if (TextUtils.isEmpty(personalizedSignature)) {
            return "自由、灵活、赚钱";
        }
        return personalizedSignature;
    }

    public void setPersonalizedSignature(String personalizedSignature) {
        this.personalizedSignature = personalizedSignature;
    }

    public String getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(String cancelCount) {
        this.cancelCount = cancelCount;
    }

    public String getLateCount() {
        return lateCount;
    }

    public void setLateCount(String lateCount) {
        this.lateCount = lateCount;
    }

    public String getServiceCount() {
        return serviceCount;
    }

    public int getServiceCountNum() {
        if(TextUtils.isEmpty(serviceCount) || "null".equals(serviceCount)){
            return 0;
        }
        return Integer.parseInt(serviceCount);
    }

    public void setServiceCount(String serviceCount) {
        this.serviceCount = serviceCount;
    }
}
