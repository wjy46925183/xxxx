package com.dlg.data.user.model;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：雇员，雇主用户信息
 * 创建时间：2017/6/30 14:22
 */
public class UserInfoDataBean implements Serializable{
    private String isShowCreditArchive; //是否有诚信档案  0=有；1=没有
    private String id; //ID
    private UserRestInfoBean userRestVo; //用户其它基本信息
    private UserAttributeInfoBean userAttributeRestVo; //用户基本信息

    public String getIsShowCreditArchive() {
        return isShowCreditArchive;
    }

    public void setIsShowCreditArchive(String isShowCreditArchive) {
        this.isShowCreditArchive = isShowCreditArchive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserRestInfoBean getUserRestVo() {
        return userRestVo;
    }

    public void setUserRestVo(UserRestInfoBean userRestVo) {
        this.userRestVo = userRestVo;
    }

    public UserAttributeInfoBean getUserAttributeRestVo() {
        return userAttributeRestVo;
    }

    public void setUserAttributeRestVo(UserAttributeInfoBean userAttributeRestVo) {
        this.userAttributeRestVo = userAttributeRestVo;
    }
}
