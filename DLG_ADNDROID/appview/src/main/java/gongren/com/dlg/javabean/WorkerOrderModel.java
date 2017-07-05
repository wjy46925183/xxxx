package gongren.com.dlg.javabean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */
public class WorkerOrderModel {


    /**
     * code : 0
     * total : 0
     * totalpage : 0
     * data : [[{"id":"27501847187295293324954543702","postName":"Post名字","price":200,"jobMeterUnit":1,"jobMeterUnitName":"单","demandType":1,"startYear":"","startMonth":"","startDay":"","startHour":"","startMinute":"","startSecond":"","endYear":"","endMonth":"","endDay":"","endHour":"","endMinute":"","endSecond":"","distance":1.0033359200000001E7}]]
     */

    private int code;
    private int total;
    private int totalpage;
    private List<List<DataBean>> data;

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

    public List<List<DataBean>> getData() {
        return data;
    }

    public void setData(List<List<DataBean>> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 27501847187295293324954543702
         * postName : Post名字
         * price : 200
         * jobMeterUnit : 1
         * jobMeterUnitName : 单
         * demandType : 1
         * startYear :
         * startMonth :
         * startDay :
         * startHour :
         * startMinute :
         * startSecond :
         * endYear :
         * endMonth :
         * endDay :
         * endHour :
         * endMinute :
         * endSecond :
         * distance : 1.0033359200000001E7
         */

        private String id;
        private String postName;
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
        private double distance;

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

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }
}