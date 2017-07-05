package gongren.com.dlg.javabean;

import java.util.List;

/**
 * Created by luoxiaohui .
 * on 2017/5/18
 * 文件描述：
 */

public class TaskOfOrderInfo {

    /**
     * data : [{"id":"27579635008805290913384826063","price":6699,"jobMeterUnitName":"天","postName":"搬砖","status":10,"usedNum":1,"list":[{"employeeId":"27579322318046497824857254115","phone":"13776054690","meterUnitName":"天","location":null,"cityId":"310100","cityName":"上海市","serviceAmount":null,"provinceName":"上海市","endDate":null,"amount":null,"xyCoordinateVo":{"jobYCoordinate":31.256879,"distanceToJob":null,"jobXCoordinate":121.401068,"userYCoordinate":null,"userXCoordinate":null},"employerId":"27579323277277189657753938151","height":null,"description":null,"modifyTime":1495095703000,"status":10,"isCancel":0,"price":6699,"businessNumber":"27579647792398933994104096013-R-946df9eecaca44a5bb71e82c289ec0f6-1495095702543","parentJobId":null,"demandType":1,"villageName":"真如镇","postTypeName":"其他","provinceId":"310000","startDate":null,"sex":null,"weight":null,"jobId":"27579635008805290913384826063","creditCount":null,"isInProgress":1,"isEvaluate":null,"areaId":"510000","identity":null,"distance":"10公里","postName":"搬砖","name":"曹伟","totalPrice":147378,"releaseCount":null,"jobTitle":"","logo":null,"cancelCause":null,"postType":"job.type_17","meterUnit":1,"evaluateLevel":null,"auditPermission":null,"workAddress":"36","villageId":"310107101","scoreCount":null,"areaName":"普陀区","modifyUserId":null,"joinCount":1,"currentOperateStatus":10,"cancelAmount":null}],"jobMeterUnit":1},{"id":"27579662014838614824168391992","price":55,"jobMeterUnitName":"时","postName":"开发开发","status":10,"usedNum":1,"list":[{"employeeId":"27579250800019724010924487534","phone":"15801955439","meterUnitName":"时","location":null,"cityId":"310100","cityName":"上海市","serviceAmount":null,"provinceName":"上海市","endDate":null,"amount":null,"xyCoordinateVo":{"jobYCoordinate":31.239582,"distanceToJob":null,"jobXCoordinate":121.434372,"userYCoordinate":null,"userXCoordinate":null},"employerId":"27579323277277189657753938151","height":null,"description":null,"modifyTime":1495096576000,"status":10,"isCancel":0,"price":55,"businessNumber":"27579663914853254416252208446-R-9760889997f54d60b726e3cc41dba75b-1495096576325","parentJobId":null,"demandType":1,"villageName":"长寿路街道","postTypeName":"互联网","provinceId":"310000","startDate":null,"sex":null,"weight":null,"jobId":"27579662014838614824168391992","creditCount":null,"isInProgress":1,"isEvaluate":null,"areaId":"510000","identity":null,"distance":"10公里","postName":"开发开发","name":"王进亚","totalPrice":178695,"releaseCount":null,"jobTitle":"","logo":"http://s.gongren.com/dds/img/oddjob/logo/984499/s27579580185081904198843100304.jpg","cancelCause":null,"postType":"job.type_13","meterUnit":2,"evaluateLevel":null,"auditPermission":null,"workAddress":"96","villageId":"310107015","scoreCount":null,"areaName":"普陀区","modifyUserId":null,"joinCount":5,"currentOperateStatus":10,"cancelAmount":null}],"jobMeterUnit":2}]
     * total : 0
     * totalpage : 0
     * code : 0
     */

    private int total;
    private int totalpage;
    private int code;
    private List<DataBean> data;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 27579635008805290913384826063
         * price : 6699
         * jobMeterUnitName : 天
         * postName : 搬砖
         * status : 10
         * usedNum : 1
         * list : [{"employeeId":"27579322318046497824857254115","phone":"13776054690","meterUnitName":"天","location":null,"cityId":"310100","cityName":"上海市","serviceAmount":null,"provinceName":"上海市","endDate":null,"amount":null,"xyCoordinateVo":{"jobYCoordinate":31.256879,"distanceToJob":null,"jobXCoordinate":121.401068,"userYCoordinate":null,"userXCoordinate":null},"employerId":"27579323277277189657753938151","height":null,"description":null,"modifyTime":1495095703000,"status":10,"isCancel":0,"price":6699,"businessNumber":"27579647792398933994104096013-R-946df9eecaca44a5bb71e82c289ec0f6-1495095702543","parentJobId":null,"demandType":1,"villageName":"真如镇","postTypeName":"其他","provinceId":"310000","startDate":null,"sex":null,"weight":null,"jobId":"27579635008805290913384826063","creditCount":null,"isInProgress":1,"isEvaluate":null,"areaId":"510000","identity":null,"distance":"10公里","postName":"搬砖","name":"曹伟","totalPrice":147378,"releaseCount":null,"jobTitle":"","logo":null,"cancelCause":null,"postType":"job.type_17","meterUnit":1,"evaluateLevel":null,"auditPermission":null,"workAddress":"36","villageId":"310107101","scoreCount":null,"areaName":"普陀区","modifyUserId":null,"joinCount":1,"currentOperateStatus":10,"cancelAmount":null}]
         * jobMeterUnit : 1
         */

        private String id;
        private String price;
        private String jobMeterUnitName;
        private String postName;
        private int status;
        private int usedNum;
        private int jobMeterUnit;
        private List<OrderCreateInfo> list;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getJobMeterUnitName() {
            return jobMeterUnitName;
        }

        public void setJobMeterUnitName(String jobMeterUnitName) {
            this.jobMeterUnitName = jobMeterUnitName;
        }

        public String getPostName() {
            return postName;
        }

        public void setPostName(String postName) {
            this.postName = postName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUsedNum() {
            return usedNum;
        }

        public void setUsedNum(int usedNum) {
            this.usedNum = usedNum;
        }

        public int getJobMeterUnit() {
            return jobMeterUnit;
        }

        public void setJobMeterUnit(int jobMeterUnit) {
            this.jobMeterUnit = jobMeterUnit;
        }

        public List<OrderCreateInfo> getList() {
            return list;
        }

        public void setList(List<OrderCreateInfo> list) {
            this.list = list;
        }
    }
}
