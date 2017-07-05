package gongren.com.dlg.javabean.master;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wangjinya on 2017/5/4.
 * 雇主工作台 卡片存储网络数据
 */

public class MasterCard implements Serializable{
    public int code;
    public String msg;
    public List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        public String id;
        public String isShowCreditArchive; //是否有诚信档案 1没有 0显示
        public UserRestVoBean userRestVo;
        public UserAttributeRestVoBean userAttributeRestVo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public UserRestVoBean getUserRestVo() {
            return userRestVo;
        }

        public void setUserRestVo(UserRestVoBean userRestVo) {
            this.userRestVo = userRestVo;
        }

        public UserAttributeRestVoBean getUserAttributeRestVo() {
            return userAttributeRestVo;
        }

        public void setUserAttributeRestVo(UserAttributeRestVoBean userAttributeRestVo) {
            this.userAttributeRestVo = userAttributeRestVo;
        }

        public static class UserRestVoBean implements Serializable{

            public String id;
            public String username;
            public String phone;
            public String imei;
            public String email;
            public String weChat;
            public String oicq;
            public String userType;
            public int status;
            public int auditStatus;
            public String origin;
            public String messageSendIdentify;

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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getAuditStatus() {
                return auditStatus;
            }

            public void setAuditStatus(int auditStatus) {
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
        }

        public static class UserAttributeRestVoBean implements Serializable{
            public String id;
            public String userId;
            public String name;
            public String logo;
            public String releaseCount;
            public String scoreCount;
            public String creditCount;
            public String distance;
            public String description;
            public String certificateType;
            public String certificateNumber;
            public String joinCount;
            public String height;
            public String weight;
            public String sex;
            public String isHealthCertificate;
            public String degree;
            public String identity;
            public String role;
            public String location;
            public String personalizedSignature;
            public String cancelCount;
            public String lateCount;
            public int serviceCount;

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
        }
    }
}
