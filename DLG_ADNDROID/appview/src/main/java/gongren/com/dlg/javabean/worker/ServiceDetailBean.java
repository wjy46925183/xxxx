package gongren.com.dlg.javabean.worker;

import java.util.List;

/**
 * 作者：王进亚
 * 主要功能：
 * 创建时间：2017/6/29 13:06
 */

public class ServiceDetailBean {

    /**
     * code : 0
     * msg : 查询零工服务详情成功
     * total : 0
     * totalpage : 0
     * data : [{"id":"27643248716423942923564025011","active":1,"version":2,"createTime":1498543515000,"createUserId":"27633909222133144190460190848","modifyTime":1498543515000,"modifyUserId":null,"userId":"27633909222133144190460190848","demandType":1,"serviceName":"玉女拜","serviceType":"job.type_4","serviceTypeName":"搬家速运","serviceMeterUnit":1,"serviceMeterUnitName":null,"price":20,"serviceDescription":"那你就就你那","auditStatus":2,"auditUserId":null,"auditUserName":null,"auditRemark":null,"auditDate":null,"images":null,"imagesUrl":null,"name":"王达宽","logo":"http://s.gongren.com/dds/img/oddjob/logo/604670/27644559652738284902590744079.jpg","imagesUrlList":["http://s.gongren.com/dds/img/oddjob/business/728742/27643248716423942659595601380.jpg","http://s.gongren.com/dds/img/oddjob/business/221682/27643248716423942659595601381.jpg","http://s.gongren.com/dds/img/oddjob/business/508510/27643248734870686733305152998.jpg"],"detailsUrl":"http://dlg.dalinggong.com/api****?serviceId=27643248716423942923564025011"}]
     */

    public int code;
    public String msg;
    public int total;
    public int totalpage;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 27643248716423942923564025011
         * active : 1
         * version : 2
         * createTime : 1498543515000
         * createUserId : 27633909222133144190460190848
         * modifyTime : 1498543515000
         * modifyUserId : null
         * userId : 27633909222133144190460190848
         * demandType : 1
         * serviceName : 玉女拜
         * serviceType : job.type_4
         * serviceTypeName : 搬家速运
         * serviceMeterUnit : 1
         * serviceMeterUnitName : null
         * price : 20
         * serviceDescription : 那你就就你那
         * auditStatus : 2
         * auditUserId : null
         * auditUserName : null
         * auditRemark : null
         * auditDate : null
         * images : null
         * imagesUrl : null
         * name : 王达宽
         * logo : http://s.gongren.com/dds/img/oddjob/logo/604670/27644559652738284902590744079.jpg
         * imagesUrlList : ["http://s.gongren.com/dds/img/oddjob/business/728742/27643248716423942659595601380.jpg","http://s.gongren.com/dds/img/oddjob/business/221682/27643248716423942659595601381.jpg","http://s.gongren.com/dds/img/oddjob/business/508510/27643248734870686733305152998.jpg"]
         * detailsUrl : http://dlg.dalinggong.com/api****?serviceId=27643248716423942923564025011
         */

        public String id;
        public int active;
        public int version;
        public long createTime;
        public String createUserId;
        public long modifyTime;
        public String modifyUserId;
        public String userId;
        public int demandType;
        public String serviceName;
        public String serviceType;
        public String serviceTypeName;
        public int serviceMeterUnit;
        public String serviceMeterUnitName;
        public int price;
        public String serviceDescription;
        public int auditStatus;
        public String auditUserId;
        public String auditUserName;
        public String auditRemark;
        public String auditDate;
        public String images;
        public String imagesUrl;
        public String name;
        public String logo;
        public String detailsUrl;
        public List<String> imagesUrlList;


    }
}
