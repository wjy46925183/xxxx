package gongren.com.dlg.javabean.master;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2017/4/15.
 */

public class MasterDetailBean implements Serializable{


    /**
     * code : 0
     * msg : 成功
     * total : 0
     * totalpage : 0
     * data : [{"status":40,"list":[{"id":2.7530200896850536E28,"active":1,"version":1,"createTime":1492415182000,"createUserId":null,"modifyTime":1492415182000,"modifyUserId":null,"employeeId":829899302276042752,"employerId":788909895532548096,"status":40,"demandType":3,"businessNumber":"27530200896850536396922206264/R/dfdf7bfbcf2443a29fefeda4ebcad64b/1492415181309","isInProgress":1,"currentOperateStatus":40,"jobId":834019187730026496,"postName":"物流员","postType":"job.type_16","postTypeName":"其他","name":"孙立坤","phone":"13361928379","logo":null,"releaseCount":15,"joinCount":0,"scoreCount":null,"creditCount":null,"distance":"jd.scope_2","description":null,"xyCoordinateVo":null,"startDate":null,"endDate":null,"price":10,"totalPrice":10,"meterUnit":null}]}]
     */

    private String code;
    private String msg;
    private String total;
    private String totalpage;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(String totalpage) {
        this.totalpage = totalpage;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * status : 40
         * list : [{"id":2.7530200896850536E28,"active":1,"version":1,"createTime":1492415182000,"createUserId":null,"modifyTime":1492415182000,"modifyUserId":null,"employeeId":829899302276042752,"employerId":788909895532548096,"status":40,"demandType":3,"businessNumber":"27530200896850536396922206264/R/dfdf7bfbcf2443a29fefeda4ebcad64b/1492415181309","isInProgress":1,"currentOperateStatus":40,"jobId":834019187730026496,"postName":"物流员","postType":"job.type_16","postTypeName":"其他","name":"孙立坤","phone":"13361928379","logo":null,"releaseCount":15,"joinCount":0,"scoreCount":null,"creditCount":null,"distance":"jd.scope_2","description":null,"xyCoordinateVo":null,"startDate":null,"endDate":null,"price":10,"totalPrice":10,"meterUnit":null}]
         */

        private int status;
        private List<ListBean> list;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Serializable {
            /**
             * id : 2.7530200896850536E28
             * active : 1
             * version : 1
             * createTime : 1492415182000
             * createUserId : null
             * modifyTime : 1492415182000
             * modifyUserId : null
             * employeeId : 829899302276042752
             * employerId : 788909895532548096
             * status : 40
             * demandType : 3
             * businessNumber : 27530200896850536396922206264/R/dfdf7bfbcf2443a29fefeda4ebcad64b/1492415181309
             * isInProgress : 1
             * currentOperateStatus : 40
             * jobId : 834019187730026496
             * postName : 物流员
             * postType : job.type_16
             * postTypeName : 其他
             * name : 孙立坤
             * phone : 13361928379
             * logo : null
             * releaseCount : 15
             * joinCount : 0
             * scoreCount : null
             * creditCount : null
             * distance : jd.scope_2
             * description : null
             * xyCoordinateVo : null
             * startDate : null
             * endDate : null
             * price : 10
             * totalPrice : 10
             * meterUnit : null
             * isEvaluate 是否已评价0否1是
             */

            private String id;
            private String active;
            private String version;
            private String createTime;
            private String createUserId;
            private String modifyTime;
            private String modifyUserId;
            private String employeeId;
            private String employerId;
            private int status;
            private int demandType;
            private String businessNumber;
            private String isInProgress;
            private String currentOperateStatus;
            private String jobId;
            private String postName;
            private String postType;
            private String postTypeName;
            private String name;
            private String phone;
            private String logo;
            private String releaseCount;
            private String joinCount;
            private String scoreCount;
            private String creditCount;
            private String distance;
            private String description;
            private String xyCoordinateVo;
            private String startDate;
            private String endDate;
            private String price;
            private String totalPrice;
            private String meterUnit;
            private String isEvaluate;
            private String evaluateLevel;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getActive() {
                return active;
            }

            public void setActive(String active) {
                this.active = active;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getCreateUserId() {
                return createUserId;
            }

            public void setCreateUserId(String createUserId) {
                this.createUserId = createUserId;
            }

            public String getModifyTime() {
                return modifyTime;
            }

            public void setModifyTime(String modifyTime) {
                this.modifyTime = modifyTime;
            }

            public String getModifyUserId() {
                return modifyUserId;
            }

            public void setModifyUserId(String modifyUserId) {
                this.modifyUserId = modifyUserId;
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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getDemandType() {
                return demandType;
            }

            public void setDemandType(int demandType) {
                this.demandType = demandType;
            }

            public String getBusinessNumber() {
                return businessNumber;
            }

            public void setBusinessNumber(String businessNumber) {
                this.businessNumber = businessNumber;
            }

            public String getIsInProgress() {
                return isInProgress;
            }

            public void setIsInProgress(String isInProgress) {
                this.isInProgress = isInProgress;
            }

            public String getCurrentOperateStatus() {
                return currentOperateStatus;
            }

            public void setCurrentOperateStatus(String currentOperateStatus) {
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

            public String getPostTypeName() {
                return postTypeName;
            }

            public void setPostTypeName(String postTypeName) {
                this.postTypeName = postTypeName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
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

            public String getJoinCount() {
                return joinCount;
            }

            public void setJoinCount(String joinCount) {
                this.joinCount = joinCount;
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

            public String getXyCoordinateVo() {
                return xyCoordinateVo;
            }

            public void setXyCoordinateVo(String xyCoordinateVo) {
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

            public String getMeterUnit() {
                return meterUnit;
            }

            public void setMeterUnit(String meterUnit) {
                this.meterUnit = meterUnit;
            }

            public String getIsEvaluate() {
                return isEvaluate;
            }

            public void setIsEvaluate(String isEvaluate) {
                this.isEvaluate = isEvaluate;
            }

            public String getEvaluateLevel() {
                return evaluateLevel;
            }

            public void setEvaluateLevel(String evaluateLevel) {
                this.evaluateLevel = evaluateLevel;
            }
        }
    }
}
