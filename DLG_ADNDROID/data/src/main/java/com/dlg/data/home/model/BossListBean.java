package com.dlg.data.home.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：雇主列表数据
 * 创建时间：2017/6/23 16:53
 */
public class BossListBean implements Serializable {



    /**
     * {
     * "identity":0,
     * "distance":0.0,
     * "userLogo":"http://s.chengxinhutong.com/dds/img/gongren/dlg/logo/559561/s839450436430860288.png",
     * "userId":"27642953365604578607219837174",
     * "name":"郑兴趣",
     * "serviceFlag":0
     * }
     */
    /**
     * {
     "userLogo":"http://s.chengxinhutong.com/dds/img/gongren/dlg/logo/559561/s839450436430860288.png",
     "distance":0.0,
     "name":"段倩文",
     "identity":null,
     "joinCount":3,
     "scoreCount":null,
     "creditCount":null,
     "userId":"857074853910548480"
     }
     */
    private String identity; //身份  0 = 在校学生； 1= 自由工作者； 2 = 兼职人员 ；其它 = 兼职人员
    private String userLogo;  //用户头像
    private String distance; //距离
    private String name;  //用户真实姓名
    private String joinCount; //接单统计
    private String serviceNum; //多少项服务
    private String scoreCount; //评分统计
    private String creditCount; //诚信统计
    private String userId; //用户ID
    private JobServiceBean jobServiceRpcVo; //服务数据对象

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setJoinCount(String joinCount) {
        this.joinCount = joinCount;
    }

    public String getJoinCount() {
        return joinCount;
    }

    public String getServiceNum() {
        return serviceNum;
    }

    public void setServiceNum(String serviceNum) {
        this.serviceNum = serviceNum;
    }

    public JobServiceBean getJobServiceRpcVo() {
        return jobServiceRpcVo;
    }

    public void setJobServiceRpcVo(JobServiceBean jobServiceRpcVo) {
        this.jobServiceRpcVo = jobServiceRpcVo;
    }

    public String getIdentityName() {
        String identityStr="";
        if (TextUtils.isEmpty(identity)) {
            identityStr = "自由工作者";
        } else {
            if ("0".equals(identity)) {
                identityStr = "在校学生";
            } else if ("1".equals(identity)) {
                identityStr = "自由工作者";
            } else {
                identityStr = "兼职人员";
            }
        }
        return identityStr;
    }

    public String getServerName() {
        if(null == jobServiceRpcVo){
            return "";
        }
        int serviceMeterUnit = jobServiceRpcVo.getServiceMeterUnit();
        String unitName = null;
        if (serviceMeterUnit == 1) {
            unitName = "/天";
        } else if (serviceMeterUnit == 2) {
            unitName = "/时";
        } else if (serviceMeterUnit == 3) {
            unitName = "/单";
        }
        return "提供的服务:" + jobServiceRpcVo.getServiceName() + " " + jobServiceRpcVo.getPrice() + "元" + unitName + "...等" + serviceNum + "项";

    }

}
