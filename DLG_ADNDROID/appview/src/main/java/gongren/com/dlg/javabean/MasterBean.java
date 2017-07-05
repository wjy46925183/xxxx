package gongren.com.dlg.javabean;

import java.util.List;

/**
 * Created by lenovo on 2017/4/14.
 */

public class MasterBean {

    /**
     * code : 0
     * msg : 成功
     * total : 0
     * totalpage : 0
     * data : [{"id":123321321123123,"active":1,"version":9,"createTime":1492148975000,"createUserId":788909895532548100,"modifyTime":1492501135000,"modifyUserId":null,"employeeId":788909895532548100,"employerId":789009063844384800,"status":21,"demandType":3,"businessNumber":"1235321345312","isInProgress":0,"currentOperateStatus":21,"jobId":158158158,"jobTitle":null,"postName":"ces","postType":"21","postTypeName":"其他","name":"刘奎","phone":"18321271116","logo":"http://s.gongren.com/dds/img/oddjob/logo/922512/s27526821140839383213591176060.jpg","releaseCount":44,"joinCount":5,"scoreCount":4,"creditCount":null,"distance":"jd.scope_2","description":"江苏诚信聘人力资源有限公司。人才中介；职业中介；计算机领域内的技术开发、技术咨询、技术转让、技术服务；网络工程；计算机系统集成；计算机软件开发与销售；计算机及配件的上门安装、上门维修；电脑图文设计、制作。（依法须经批准的项目，经相关部门批准后方可开展经营活动）\\r\\n\\r\\n\\r\\n（依法须经批准的项目，经相关部门批准后方可开展经营活动）。核心团队包括：葛猛任监事;董亮任执行董事兼总经理;本公司主要股东包括：董亮;赵腾达;我公司地址为昆山市花桥镇徐公桥路2号352号房，欢迎前来洽谈合作业务。","xyCoordinateVo":{"jobXCoordinate":121.392577,"jobYCoordinate":31.242348,"userXCoordinate":11,"userYCoordinate":22,"distanceToJob":12243.73},"startDate":null,"endDate":null,"price":150,"totalPrice":null,"meterUnit":1,"meterUnitName":"天"}]
     */

    public int code;
    public String msg;
    public int total;
    public int totalpage;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 123321321123123
         * active : 1
         * version : 9
         * createTime : 1492148975000
         * createUserId : 788909895532548100
         * modifyTime : 1492501135000
         * modifyUserId : null
         * employeeId : 788909895532548100
         * employerId : 789009063844384800
         * status : 21
         * demandType : 3
         * businessNumber : 1235321345312
         * isInProgress : 0
         * currentOperateStatus : 21
         * jobId : 158158158
         * jobTitle : null
         * postName : ces
         * postType : 21
         * postTypeName : 其他
         * name : 刘奎
         * phone : 18321271116
         * logo : http://s.gongren.com/dds/img/oddjob/logo/922512/s27526821140839383213591176060.jpg
         * releaseCount : 44
         * joinCount : 5
         * scoreCount : 4
         * creditCount : null
         * distance : jd.scope_2
         * description : 江苏诚信聘人力资源有限公司。人才中介；职业中介；计算机领域内的技术开发、技术咨询、技术转让、技术服务；网络工程；计算机系统集成；计算机软件开发与销售；计算机及配件的上门安装、上门维修；电脑图文设计、制作。（依法须经批准的项目，经相关部门批准后方可开展经营活动）\r\n\r\n\r\n（依法须经批准的项目，经相关部门批准后方可开展经营活动）。核心团队包括：葛猛任监事;董亮任执行董事兼总经理;本公司主要股东包括：董亮;赵腾达;我公司地址为昆山市花桥镇徐公桥路2号352号房，欢迎前来洽谈合作业务。
         * xyCoordinateVo : {"jobXCoordinate":121.392577,"jobYCoordinate":31.242348,"userXCoordinate":11,"userYCoordinate":22,"distanceToJob":12243.73}
         * startDate : null
         * endDate : null
         * price : 150
         * totalPrice : null
         * meterUnit : 1
         * meterUnitName : 天
         */

        public String id;
        public String active;
        public String version;
        public String createTime;
        public String createUserId;
        public String modifyTime;
        public String modifyUserId;
        public String employeeId;
        public String employerId;
        public String status;
        public String demandType;
        public String businessNumber;
        public String isInProgress;
        public String currentOperateStatus;
        public String jobId;
        public String jobTitle;
        public String postName;
        public String identity;
        public String postType;
        public String postTypeName;
        public String name;
        public String phone;
        public String logo;
        public String releaseCount;
        public String joinCount;
        public String scoreCount;
        public String creditCount;
        public String distance;
        public String description;
        public XyCoordinateVoBean xyCoordinateVo;
        public String startDate;
        public String endDate;
        public String price;
        public String totalPrice;
        public String meterUnit;
        public String meterUnitName;

        public static class XyCoordinateVoBean {
            /**
             * jobXCoordinate : 121.392577
             * jobYCoordinate : 31.242348
             * userXCoordinate : 11
             * userYCoordinate : 22
             * distanceToJob : 12243.73
             */
            public String jobXCoordinate;
            public String jobYCoordinate;
            public String userXCoordinate;
            public String userYCoordinate;
            public String distanceToJob;
        }
    }
}
