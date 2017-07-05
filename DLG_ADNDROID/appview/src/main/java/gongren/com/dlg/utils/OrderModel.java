package gongren.com.dlg.utils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class OrderModel {

    /**
     * month : 2016-09
     * list : [{"misId":1001,"positionName":"普工","recruitNum":"20","workTime":20,"startDate":1472718609000,"endDate":1473325200000,"sumPay":31,"Falg":false}]
     */

    private String month;
    /**
     * misId : 1001
     * positionName : 普工
     * recruitNum : 20
     * workTime : 20
     * startDate : 1472718609000
     * endDate : 1473325200000
     * sumPay : 31
     * Falg : false
     */

    private List<ListBean> list;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private long misId;
        private String positionName;
        private String recruitNum;
        private double workTime;
        private long startDate;
        private long endDate;
        private String sumPay;
        private boolean Falg;

        public long getMisId() {
            return misId;
        }

        public void setMisId(long misId) {
            this.misId = misId;
        }

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }

        public String getRecruitNum() {
            return recruitNum;
        }

        public void setRecruitNum(String recruitNum) {
            this.recruitNum = recruitNum;
        }

        public double getWorkTime() {
            return workTime;
        }

        public void setWorkTime(double workTime) {
            this.workTime = workTime;
        }

        public long getStartDate() {
            return startDate;
        }

        public void setStartDate(long startDate) {
            this.startDate = startDate;
        }

        public long getEndDate() {
            return endDate;
        }

        public void setEndDate(long endDate) {
            this.endDate = endDate;
        }

        public String getSumPay() {
            return sumPay;
        }

        public void setSumPay(String sumPay) {
            this.sumPay = sumPay;
        }

        public boolean isFalg() {
            return Falg;
        }

        public void setFalg(boolean Falg) {
            this.Falg = Falg;
        }
    }
}
