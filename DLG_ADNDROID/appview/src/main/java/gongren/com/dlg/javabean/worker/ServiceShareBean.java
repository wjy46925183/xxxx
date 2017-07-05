package gongren.com.dlg.javabean.worker;

import java.util.List;

/**
 * 作者：王进亚
 * 主要功能：
 * 创建时间：2017/6/29 11:45
 */

public class ServiceShareBean {


    /**
     * code : 0
     * msg : 成功
     * total : 0
     * totalpage : 0
     * data : [{"timestamp":null,"noncestr":null,"signature":null,"appid":null,"userLogo":"http://s.gongren.com/dds/img/oddjob/logo/145644/27624587012441542170574619676.jpg","detailsUrl":"http://dlg.dalinggong.com/api****?serviceId=27635567326170953780839289249","serviceTitle":"【这个事三个歌】  2元/单，点击开始雇TA吧!","serviceDescription":"发生的发谁发生的发山东发生的发水淀粉","serviceId":"27635567326170953780839289249"}]
     */

    private int code;
    private String msg;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * timestamp : null
         * noncestr : null
         * signature : null
         * appid : null
         * userLogo : http://s.gongren.com/dds/img/oddjob/logo/145644/27624587012441542170574619676.jpg
         * detailsUrl : http://dlg.dalinggong.com/api****?serviceId=27635567326170953780839289249
         * serviceTitle : 【这个事三个歌】  2元/单，点击开始雇TA吧!
         * serviceDescription : 发生的发谁发生的发山东发生的发水淀粉
         * serviceId : 27635567326170953780839289249
         */

        private String timestamp;
        private String noncestr;
        private String signature;
        private String appid;
        private String userLogo;
        private String detailsUrl;
        private String serviceTitle;
        private String serviceDescription;
        private String serviceId;

        public Object getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getUserLogo() {
            return userLogo;
        }

        public void setUserLogo(String userLogo) {
            this.userLogo = userLogo;
        }

        public String getDetailsUrl() {
            return detailsUrl;
        }

        public void setDetailsUrl(String detailsUrl) {
            this.detailsUrl = detailsUrl;
        }

        public String getServiceTitle() {
            return serviceTitle;
        }

        public void setServiceTitle(String serviceTitle) {
            this.serviceTitle = serviceTitle;
        }

        public String getServiceDescription() {
            return serviceDescription;
        }

        public void setServiceDescription(String serviceDescription) {
            this.serviceDescription = serviceDescription;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }
    }
}
