package gongren.com.dlg.javabean.master;

import java.util.List;

/**
 * Created by lenovo on 2017/4/15.
 */

public class MasterAllBean {

    /**
     * code : 0
     * total : 0
     * totalpage : 0
     * data : [{"id":"27526904040507250550759269319","postName":"huu","postTypeName":"市场销售  ","price":280,"jobMeterUnit":1,"jobMeterUnitName":"天","demandType":1,"startYear":null,"startMonth":null,"startDay":null,"startHour":null,"startMinute":null,"startSecond":null,"endYear":null,"endMonth":null,"endDay":null,"endHour":null,"endMinute":null,"endSecond":null,"distance":null,"userId":788909895532548096,"status":0}]
     */

    private int code;
    private int total;
    private int totalpage;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 27526904040507250550759269319
         * postName : huu
         * postTypeName : 市场销售
         * price : 280
         * jobMeterUnit : 1
         * jobMeterUnitName : 天
         * demandType : 1
         * startYear : null
         * startMonth : null
         * startDay : null
         * startHour : null
         * startMinute : null
         * startSecond : null
         * endYear : null
         * endMonth : null
         * endDay : null
         * endHour : null
         * endMinute : null
         * endSecond : null
         * distance : null
         * userId : 788909895532548096
         * status : 0
         */

        private String id;
        private String postName;
        private String postTypeName;
        private int price;
        private int jobMeterUnit;
        private String jobMeterUnitName;
        private int demandType;
        private String startYear;
        private String startMonth;
        private String startDay;
        private String startHour;
        private String startMinute;
        private String startSecond;
        private String endYear;
        private String endMonth;
        private String endDay;
        private String endHour;
        private String endMinute;
        private String endSecond;
        private String distance;
        private String userId;
        private int status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPostName() {
            return postName;
        }

        public void setPostName(String postName) {
            this.postName = postName;
        }

        public String getPostTypeName() {
            return postTypeName;
        }

        public void setPostTypeName(String postTypeName) {
            this.postTypeName = postTypeName;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getJobMeterUnit() {
            return jobMeterUnit;
        }

        public void setJobMeterUnit(int jobMeterUnit) {
            this.jobMeterUnit = jobMeterUnit;
        }

        public String getJobMeterUnitName() {
            return jobMeterUnitName;
        }

        public void setJobMeterUnitName(String jobMeterUnitName) {
            this.jobMeterUnitName = jobMeterUnitName;
        }

        public int getDemandType() {
            return demandType;
        }

        public void setDemandType(int demandType) {
            this.demandType = demandType;
        }

        public String getStartYear() {
            return startYear;
        }

        public void setStartYear(String startYear) {
            this.startYear = startYear;
        }

        public String getStartMonth() {
            return startMonth;
        }

        public void setStartMonth(String startMonth) {
            this.startMonth = startMonth;
        }

        public String getStartDay() {
            return startDay;
        }

        public void setStartDay(String startDay) {
            this.startDay = startDay;
        }

        public String getStartHour() {
            return startHour;
        }

        public void setStartHour(String startHour) {
            this.startHour = startHour;
        }

        public String getStartMinute() {
            return startMinute;
        }

        public void setStartMinute(String startMinute) {
            this.startMinute = startMinute;
        }

        public String getStartSecond() {
            return startSecond;
        }

        public void setStartSecond(String startSecond) {
            this.startSecond = startSecond;
        }

        public String getEndYear() {
            return endYear;
        }

        public void setEndYear(String endYear) {
            this.endYear = endYear;
        }

        public String getEndMonth() {
            return endMonth;
        }

        public void setEndMonth(String endMonth) {
            this.endMonth = endMonth;
        }

        public String getEndDay() {
            return endDay;
        }

        public void setEndDay(String endDay) {
            this.endDay = endDay;
        }

        public String getEndHour() {
            return endHour;
        }

        public void setEndHour(String endHour) {
            this.endHour = endHour;
        }

        public String getEndMinute() {
            return endMinute;
        }

        public void setEndMinute(String endMinute) {
            this.endMinute = endMinute;
        }

        public String getEndSecond() {
            return endSecond;
        }

        public void setEndSecond(String endSecond) {
            this.endSecond = endSecond;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
