package com.dlg.data.user.model;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：用户其它基本信息
 * 创建时间：2017/6/30 14:23
 */
public class UserRestInfoBean implements Serializable {

    private String id; //用户id
    private String username;   //用户昵称
    private String phone;  //手机号
    private String imei;  //imei手机设备识别码
    private String email;   //邮箱
    private String weChat;  //微信号
    private String oicq;  //qq号
    private String userType; ////用户类型PERSONAL(个人)  ENTERPRISE(企业)
    private String status; //用户状态(0.新建,1.正常,2.冻结)
    private String auditStatus; ////审核状态(0.未审核,1.审核中,2.审核通过,3.审核失败)
    private String origin; //注册来源
    private String messageSendIdentify; ////消息推送标识
    private String personalizedSignature; //个性签名
    private String location; //所在地
    private String isShowCreditArchive; //是否显示诚信档案

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getOicq() {
        return oicq;
    }

    public void setOicq(String oicq) {
        this.oicq = oicq;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getMessageSendIdentify() {
        return messageSendIdentify;
    }

    public void setMessageSendIdentify(String messageSendIdentify) {
        this.messageSendIdentify = messageSendIdentify;
    }

    public String getPersonalizedSignature() {
        return personalizedSignature;
    }

    public void setPersonalizedSignature(String personalizedSignature) {
        this.personalizedSignature = personalizedSignature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIsShowCreditArchive() {
        return isShowCreditArchive;
    }

    public void setIsShowCreditArchive(String isShowCreditArchive) {
        this.isShowCreditArchive = isShowCreditArchive;
    }
}
