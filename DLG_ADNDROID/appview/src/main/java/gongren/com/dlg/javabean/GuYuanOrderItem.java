package gongren.com.dlg.javabean;

import java.io.Serializable;
import java.util.List;

import gongren.com.dlg.javabean.worker.WorkerStatusBean;

/**
 * 作者：wangdakuan
 * 主要功能：雇员订单列表数据
 * 创建时间：2017/5/27 11:50
 */
public class GuYuanOrderItem implements Serializable{

    /**
     * employeeId : 27592692315236937272442573965
     * compensatoryPayment : null
     * meterUnitName : 单
     * jobId : 27592786227611016527769851209
     * isInProgress : 1
     * isEvaluate : 0
     * employeeVo : null
     * xyCoordinateVo : null
     * employerId : 27592688496760914014565389440
     * postName : 测试计件2222
     * orderTimeResultRpcVo : {"startMinute":18,"endYear":null,"createTime":1495807983000,"endDay":null,"endMinute":null,"endHour":null,"version":1,"id":"27592787039267755770990122319","isCrossDay":0,"modifyUserId":null,"startYear":2017,"startDay":26,"active":1,"startSecond":0,"startHour":22,"startMonth":5,"modifyTime":1495807983000,"endSecond":null,"endMonth":null,"orderBusinessNumber":"27592787039267755770990122317-R-9a2934870064448d87ae93232748528b-1495807982550","orderId":"27592787039267755770990122318","createUserId":"27592679900578175665914336372"}
     * modifyTime : 1495808464000
     * orderId : 27592787039267755770990122318
     * totalPrice : 10
     * cancelCause : null
     * postType : job.type_3
     * status : 9
     * isCancel : 0
     * evaluateLevel : null
     * meterUnit : 3
     * price : 10
     * businessNumber : 27592787039267755770990122317-R-9a2934870064448d87ae93232748528b-1495807982550
     * modifyUserId : 27592692315236937272442573965
     * currentOperateStatus : 10
     * demandType : 3
     * postTypeName : 生活服务
     */

    private String employeeId;
    private String compensatoryPayment;
    private String meterUnitName;
    private String jobId;
    private String isInProgress;
    private String isEvaluate;
    private String employeeVo;
    private String xyCoordinateVo;
    private String employerId;
    private String postName;
    private OrderTimeResultRpcVoBean orderTimeResultRpcVo;
    private long modifyTime;
    private String orderId;
    private String totalPrice;
    private String cancelCause;
    private String postType;
    private String status;
    private String isCancel;
    private String evaluateLevel;
    private String meterUnit;
    private String price;
    private String businessNumber;
    private String modifyUserId;
    private String currentOperateStatus;
    private String demandType;
    private String postTypeName;

    private List<WorkerStatusBean.DataBean> buttonData;

    public List<WorkerStatusBean.DataBean>  getButtonData() {
        return buttonData;
    }

    public void setButtonData(List<WorkerStatusBean.DataBean>  buttonData) {
        this.buttonData = buttonData;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCompensatoryPayment() {
        return compensatoryPayment;
    }

    public void setCompensatoryPayment(String compensatoryPayment) {
        this.compensatoryPayment = compensatoryPayment;
    }

    public String getMeterUnitName() {
        return meterUnitName;
    }

    public void setMeterUnitName(String meterUnitName) {
        this.meterUnitName = meterUnitName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getIsInProgress() {
        return isInProgress;
    }

    public void setIsInProgress(String isInProgress) {
        this.isInProgress = isInProgress;
    }

    public String getIsEvaluate() {
        return isEvaluate;
    }

    public void setIsEvaluate(String isEvaluate) {
        this.isEvaluate = isEvaluate;
    }

    public String getEmployeeVo() {
        return employeeVo;
    }

    public void setEmployeeVo(String employeeVo) {
        this.employeeVo = employeeVo;
    }

    public String getXyCoordinateVo() {
        return xyCoordinateVo;
    }

    public void setXyCoordinateVo(String xyCoordinateVo) {
        this.xyCoordinateVo = xyCoordinateVo;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public OrderTimeResultRpcVoBean getOrderTimeResultRpcVo() {
        return orderTimeResultRpcVo;
    }

    public void setOrderTimeResultRpcVo(OrderTimeResultRpcVoBean orderTimeResultRpcVo) {
        this.orderTimeResultRpcVo = orderTimeResultRpcVo;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCancelCause() {
        return cancelCause;
    }

    public void setCancelCause(String cancelCause) {
        this.cancelCause = cancelCause;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }

    public String getEvaluateLevel() {
        return evaluateLevel;
    }

    public void setEvaluateLevel(String evaluateLevel) {
        this.evaluateLevel = evaluateLevel;
    }

    public String getMeterUnit() {
        return meterUnit;
    }

    public void setMeterUnit(String meterUnit) {
        this.meterUnit = meterUnit;
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

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public String getCurrentOperateStatus() {
        return currentOperateStatus;
    }

    public void setCurrentOperateStatus(String currentOperateStatus) {
        this.currentOperateStatus = currentOperateStatus;
    }

    public String getDemandType() {
        return demandType;
    }

    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }

    public String getPostTypeName() {
        return postTypeName;
    }

    public void setPostTypeName(String postTypeName) {
        this.postTypeName = postTypeName;
    }

    public static class OrderTimeResultRpcVoBean implements Serializable{
        /**
         * startMinute : 18
         * endYear : null
         * createTime : 1495807983000
         * endDay : null
         * endMinute : null
         * endHour : null
         * version : 1
         * id : 27592787039267755770990122319
         * isCrossDay : 0
         * modifyUserId : null
         * startYear : 2017
         * startDay : 26
         * active : 1
         * startSecond : 0
         * startHour : 22
         * startMonth : 5
         * modifyTime : 1495807983000
         * endSecond : null
         * endMonth : null
         * orderBusinessNumber : 27592787039267755770990122317-R-9a2934870064448d87ae93232748528b-1495807982550
         * orderId : 27592787039267755770990122318
         * createUserId : 27592679900578175665914336372
         */

        private String startMinute;
        private String endYear;
        private long createTime;
        private String endDay;
        private String endMinute;
        private String endHour;
        private String version;
        private String id;
        private String isCrossDay;
        private String modifyUserId;
        private String startYear;
        private String startDay;
        private String active;
        private String startSecond;
        private String startHour;
        private String startMonth;
        private long modifyTime;
        private String endSecond;
        private String endMonth;
        private String orderBusinessNumber;
        private String orderId;
        private String createUserId;

        public String getStartMinute() {
            return startMinute;
        }

        public void setStartMinute(String startMinute) {
            this.startMinute = startMinute;
        }

        public String getEndYear() {
            return endYear;
        }

        public void setEndYear(String endYear) {
            this.endYear = endYear;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getEndDay() {
            return endDay;
        }

        public void setEndDay(String endDay) {
            this.endDay = endDay;
        }

        public String getEndMinute() {
            return endMinute;
        }

        public void setEndMinute(String endMinute) {
            this.endMinute = endMinute;
        }

        public String getEndHour() {
            return endHour;
        }

        public void setEndHour(String endHour) {
            this.endHour = endHour;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsCrossDay() {
            return isCrossDay;
        }

        public void setIsCrossDay(String isCrossDay) {
            this.isCrossDay = isCrossDay;
        }

        public String getModifyUserId() {
            return modifyUserId;
        }

        public void setModifyUserId(String modifyUserId) {
            this.modifyUserId = modifyUserId;
        }

        public String getStartYear() {
            return startYear;
        }

        public void setStartYear(String startYear) {
            this.startYear = startYear;
        }

        public String getStartDay() {
            return startDay;
        }

        public void setStartDay(String startDay) {
            this.startDay = startDay;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getStartSecond() {
            return startSecond;
        }

        public void setStartSecond(String startSecond) {
            this.startSecond = startSecond;
        }

        public String getStartHour() {
            return startHour;
        }

        public void setStartHour(String startHour) {
            this.startHour = startHour;
        }

        public String getStartMonth() {
            return startMonth;
        }

        public void setStartMonth(String startMonth) {
            this.startMonth = startMonth;
        }

        public long getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(long modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getEndSecond() {
            return endSecond;
        }

        public void setEndSecond(String endSecond) {
            this.endSecond = endSecond;
        }

        public String getEndMonth() {
            return endMonth;
        }

        public void setEndMonth(String endMonth) {
            this.endMonth = endMonth;
        }

        public String getOrderBusinessNumber() {
            return orderBusinessNumber;
        }

        public void setOrderBusinessNumber(String orderBusinessNumber) {
            this.orderBusinessNumber = orderBusinessNumber;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(String createUserId) {
            this.createUserId = createUserId;
        }
    }
}
