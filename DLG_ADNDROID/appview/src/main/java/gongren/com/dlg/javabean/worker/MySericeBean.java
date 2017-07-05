package gongren.com.dlg.javabean.worker;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2017/6/22.
 */

public class MySericeBean implements Serializable{


    /**
     * code : 0
     * msg : 查询零工服务列表成功
     * total : 1
     * totalpage : 1
     * data : [{"id":"27635627278089192867703415851","active":1,"version":3,"createTime":1498130356000,"createUserId":"27633909222133144190460190848","modifyTime":1498131194000,"modifyUserId":null,"userId":"27633909222133144190460190848","demandType":1,"serviceName":"的都","serviceType":"job.type_4","serviceTypeName":"搬家速运","serviceMeterUnit":1,"price":36,"serviceDescription":"痛快淋漓","auditStatus":2,"auditUserId":null,"auditUserName":null,"auditRemark":null,"auditDate":null,"images":null,"imagesUrl":null,"name":null,"logo":null,"imagesUrlList":["http://s.gongren.com/dds/img/oddjob/business/793496/27635642736460726663563740415.jpg"]}]
     */

    private int code;
    private String msg;
    private int total;
    private int totalpage;
    private List<DataBean> data;

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

    public static class DataBean implements Serializable{
        /**
         * id : 27635627278089192867703415851
         * active : 1
         * version : 3
         * createTime : 1498130356000
         * createUserId : 27633909222133144190460190848
         * modifyTime : 1498131194000
         * modifyUserId : null
         * userId : 27633909222133144190460190848
         * demandType : 1
         * serviceName : 的都
         * serviceType : job.type_4
         * serviceTypeName : 搬家速运
         * serviceMeterUnit : 1
         * price : 36
         * serviceDescription : 痛快淋漓
         * auditStatus : 2
         * auditUserId : null
         * auditUserName : null
         * auditRemark : null
         * auditDate : null
         * images : null
         * imagesUrl : null
         * name : null
         * logo : null
         * imagesUrlList : ["http://s.gongren.com/dds/img/oddjob/business/793496/27635642736460726663563740415.jpg"]
         */

        private String id;
        private int active;
        private int version;
        private long createTime;
        private String createUserId;
        private long modifyTime;
        private String modifyUserId;
        private String userId;
        private int demandType;
        private String serviceName;
        private String serviceType;
        private String serviceTypeName;
        private int serviceMeterUnit;
        private int price;
        private String serviceDescription;
        private int auditStatus;
        private String auditUserId;
        private String auditUserName;
        private String auditRemark;
        private String auditDate;
        private String images;
        private String imagesUrl;
        private String name;
        private String logo;
        private List<String> imagesUrlList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(String createUserId) {
            this.createUserId = createUserId;
        }

        public long getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(long modifyTime) {
            this.modifyTime = modifyTime;
        }

        public Object getModifyUserId() {
            return modifyUserId;
        }

        public void setModifyUserId(String modifyUserId) {
            this.modifyUserId = modifyUserId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getDemandType() {
            return demandType;
        }

        public void setDemandType(int demandType) {
            this.demandType = demandType;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        public String getServiceTypeName() {
            return serviceTypeName;
        }

        public void setServiceTypeName(String serviceTypeName) {
            this.serviceTypeName = serviceTypeName;
        }

        public int getServiceMeterUnit() {
            return serviceMeterUnit;
        }

        public void setServiceMeterUnit(int serviceMeterUnit) {
            this.serviceMeterUnit = serviceMeterUnit;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getServiceDescription() {
            return serviceDescription;
        }

        public void setServiceDescription(String serviceDescription) {
            this.serviceDescription = serviceDescription;
        }

        public int getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(int auditStatus) {
            this.auditStatus = auditStatus;
        }

        public Object getAuditUserId() {
            return auditUserId;
        }

        public void setAuditUserId(String auditUserId) {
            this.auditUserId = auditUserId;
        }

        public Object getAuditUserName() {
            return auditUserName;
        }

        public void setAuditUserName(String auditUserName) {
            this.auditUserName = auditUserName;
        }

        public Object getAuditRemark() {
            return auditRemark;
        }

        public void setAuditRemark(String auditRemark) {
            this.auditRemark = auditRemark;
        }

        public String getAuditDate() {
            return auditDate;
        }

        public void setAuditDate(String auditDate) {
            this.auditDate = auditDate;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getImagesUrl() {
            return imagesUrl;
        }

        public void setImagesUrl(String imagesUrl) {
            this.imagesUrl = imagesUrl;
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

        public List<String> getImagesUrlList() {
            return imagesUrlList;
        }

        public void setImagesUrlList(List<String> imagesUrlList) {
            this.imagesUrlList = imagesUrlList;
        }
    }
}
