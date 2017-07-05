package gongren.com.dlg.javabean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by luoxiaohui .
 * on 2017/5/18
 * 文件描述：
 */

    public  class OrderCreateInfo implements Serializable{
        /**
         * employeeId : 27579322318046497824857254115
         * phone : 13776054690
         * meterUnitName : 天
         * location : null
         * cityId : 310100
         * cityName : 上海市
         * serviceAmount : null
         * provinceName : 上海市
         * endDate : null
         * amount : null
         * xyCoordinateVo : {"jobYCoordinate":31.256879,"distanceToJob":null,"jobXCoordinate":121.401068,"userYCoordinate":null,"userXCoordinate":null}
         * employerId : 27579323277277189657753938151
         * height : null
         * description : null
         * modifyTime : 1495095703000
         * status : 10
         * isCancel : 0
         * price : 6699
         * businessNumber : 27579647792398933994104096013-R-946df9eecaca44a5bb71e82c289ec0f6-1495095702543
         * parentJobId : null
         * demandType : 1
         * villageName : 真如镇
         * postTypeName : 其他
         * provinceId : 310000
         * startDate : null
         * sex : null
         * weight : null
         * jobId : 27579635008805290913384826063
         * creditCount : null
         * isInProgress : 1
         * isEvaluate : null
         * areaId : 510000
         * identity : null
         * distance : 10公里
         * postName : 搬砖
         * name : 曹伟
         * totalPrice : 147378
         * releaseCount : null
         * jobTitle :
         * logo : null
         * cancelCause : null
         * postType : job.type_17
         * meterUnit : 1
         * evaluateLevel : null
         * auditPermission : null
         * workAddress : 36
         * villageId : 310107101
         * scoreCount : null
         * areaName : 普陀区
         * modifyUserId : null
         * joinCount : 1
         * currentOperateStatus : 10
         * cancelAmount : null
         */
        private String GroupId;
        private String employeeId;
        private String phone;
        private String meterUnitName;
        private Object location;
        private String cityId;
        private String cityName;
        private Object serviceAmount;
        private String provinceName;
        private Object endDate;
        private Object amount;
        private XyCoordinateVoBean xyCoordinateVo;
        private String employerId;
        private Object height;
        private String description;
        private long modifyTime;
        private int status;
        private int isCancel;
        private String price;
        private String businessNumber;
        private Object parentJobId;
        private int demandType;
        private String villageName;
        private String postTypeName;
        private String provinceId;
        private Object startDate;
        private Object sex;
        private Object weight;
        private String jobId;
        private String creditCount;
        private int isInProgress;
        private Object isEvaluate;
        private String areaId;
        private String identity;
        private String distance;
        private String postName;
        private String name;
        private String totalPrice;
        private Double releaseCount;
        private String jobTitle;
        private String logo;
        private Object cancelCause;
        private String postType;
        private int meterUnit;
        private Object evaluateLevel;
        private Object auditPermission;
        private String workAddress;
        private String villageId;
        private String scoreCount;
        private String areaName;
        private Object modifyUserId;
        private int joinCount;
        private int currentOperateStatus;
        private String insuranceAmount; //保险费用
        private Object cancelAmount;

    public String getInsuranceAmount() {
        if(TextUtils.isEmpty(insuranceAmount) || "null".equals(insuranceAmount)){
            return "0";
        }
        return insuranceAmount;
    }

    public void setInsuranceAmount(String insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    private OrderButtonInfo orderButtonInfo;

    public OrderButtonInfo getOrderButtonInfo() {
        return orderButtonInfo;
    }

    public void setOrderButtonInfo(OrderButtonInfo orderButtonInfo) {
        this.orderButtonInfo = orderButtonInfo;
    }

    public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMeterUnitName() {
            return meterUnitName;
        }

        public void setMeterUnitName(String meterUnitName) {
            this.meterUnitName = meterUnitName;
        }

        public Object getLocation() {
            return location;
        }

        public void setLocation(Object location) {
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

        public Object getServiceAmount() {
            return serviceAmount;
        }

        public void setServiceAmount(Object serviceAmount) {
            this.serviceAmount = serviceAmount;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public Object getEndDate() {
            return endDate;
        }

        public void setEndDate(Object endDate) {
            this.endDate = endDate;
        }

        public Object getAmount() {
            return amount;
        }

        public void setAmount(Object amount) {
            this.amount = amount;
        }

        public XyCoordinateVoBean getXyCoordinateVo() {
            return xyCoordinateVo;
        }

        public void setXyCoordinateVo(XyCoordinateVoBean xyCoordinateVo) {
            this.xyCoordinateVo = xyCoordinateVo;
        }

        public String getEmployerId() {
            return employerId;
        }

        public void setEmployerId(String employerId) {
            this.employerId = employerId;
        }

        public Object getHeight() {
            return height;
        }

        public void setHeight(Object height) {
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIsCancel() {
            return isCancel;
        }

        public void setIsCancel(int isCancel) {
            this.isCancel = isCancel;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getBusinessNumber() {
            return businessNumber;
        }

        public void setBusinessNumber(String businessNumber) {
            this.businessNumber = businessNumber;
        }

        public Object getParentJobId() {
            return parentJobId;
        }

        public void setParentJobId(Object parentJobId) {
            this.parentJobId = parentJobId;
        }

        public int getDemandType() {
            return demandType;
        }

        public void setDemandType(int demandType) {
            this.demandType = demandType;
        }

        public String getVillageName() {
            return villageName;
        }

        public void setVillageName(String villageName) {
            this.villageName = villageName;
        }

        public String getPostTypeName() {
            return postTypeName;
        }

        public void setPostTypeName(String postTypeName) {
            this.postTypeName = postTypeName;
        }

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public Object getStartDate() {
            return startDate;
        }

        public void setStartDate(Object startDate) {
            this.startDate = startDate;
        }

        public Object getSex() {
            return sex;
        }

        public void setSex(Object sex) {
            this.sex = sex;
        }

        public Object getWeight() {
            return weight;
        }

        public void setWeight(Object weight) {
            this.weight = weight;
        }

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public String getCreditCount() {
            return creditCount;
        }

        public void setCreditCount(String creditCount) {
            this.creditCount = creditCount;
        }

        public int getIsInProgress() {
            return isInProgress;
        }

        public void setIsInProgress(int isInProgress) {
            this.isInProgress = isInProgress;
        }

        public Object getIsEvaluate() {
            return isEvaluate;
        }

        public void setIsEvaluate(Object isEvaluate) {
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

        public String getPostName() {
            return postName;
        }

        public void setPostName(String postName) {
            this.postName = postName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
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

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public Object getCancelCause() {
            return cancelCause;
        }

        public void setCancelCause(Object cancelCause) {
            this.cancelCause = cancelCause;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public int getMeterUnit() {
            return meterUnit;
        }

        public void setMeterUnit(int meterUnit) {
            this.meterUnit = meterUnit;
        }

        public Object getEvaluateLevel() {
            return evaluateLevel;
        }

        public void setEvaluateLevel(Object evaluateLevel) {
            this.evaluateLevel = evaluateLevel;
        }

        public Object getAuditPermission() {
            return auditPermission;
        }

        public void setAuditPermission(Object auditPermission) {
            this.auditPermission = auditPermission;
        }

        public String getWorkAddress() {
            return workAddress;
        }

        public void setWorkAddress(String workAddress) {
            this.workAddress = workAddress;
        }

        public String getVillageId() {
            return villageId;
        }

        public void setVillageId(String villageId) {
            this.villageId = villageId;
        }

        public String getScoreCount() {
            return scoreCount;
        }

        public void setScoreCount(String scoreCount) {
            this.scoreCount = scoreCount;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public Object getModifyUserId() {
            return modifyUserId;
        }

        public void setModifyUserId(Object modifyUserId) {
            this.modifyUserId = modifyUserId;
        }

        public int getJoinCount() {
            return joinCount;
        }

        public void setJoinCount(int joinCount) {
            this.joinCount = joinCount;
        }

        public int getCurrentOperateStatus() {
            return currentOperateStatus;
        }

        public void setCurrentOperateStatus(int currentOperateStatus) {
            this.currentOperateStatus = currentOperateStatus;
        }

        public Object getCancelAmount() {
            return cancelAmount;
        }

        public void setCancelAmount(Object cancelAmount) {
            this.cancelAmount = cancelAmount;
        }

        public static class XyCoordinateVoBean implements Serializable{
            /**
             * jobYCoordinate : 31.256879
             * distanceToJob : null
             * jobXCoordinate : 121.401068
             * userYCoordinate : null
             * userXCoordinate : null
             */

            private double jobYCoordinate;
            private String distanceToJob;
            private double jobXCoordinate;
            private double userYCoordinate;
            private double userXCoordinate;

            public double getJobYCoordinate() {
                return jobYCoordinate;
            }

            public void setJobYCoordinate(double jobYCoordinate) {
                this.jobYCoordinate = jobYCoordinate;
            }

            public String getDistanceToJob() {
                return distanceToJob;
            }

            public void setDistanceToJob(String distanceToJob) {
                this.distanceToJob = distanceToJob;
            }

            public double getJobXCoordinate() {
                return jobXCoordinate;
            }

            public void setJobXCoordinate(double jobXCoordinate) {
                this.jobXCoordinate = jobXCoordinate;
            }

            public double getUserYCoordinate() {
                return userYCoordinate;
            }

            public void setUserYCoordinate(double userYCoordinate) {
                this.userYCoordinate = userYCoordinate;
            }

            public double getUserXCoordinate() {
                return userXCoordinate;
            }

            public void setUserXCoordinate(double userXCoordinate) {
                this.userXCoordinate = userXCoordinate;
            }
        }
    }
