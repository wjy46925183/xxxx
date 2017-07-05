package gongren.com.dlg.javabean;

import java.util.List;

import gongren.com.dlg.javabean.base.AssistButtonListBean;
import gongren.com.dlg.javabean.base.ButtonListBean;
import gongren.com.dlg.javabean.base.CountdownVoBean;

/**
 * Created by luoxiaohui .
 * on 2017/5/19
 * 文件描述：
 */

public class OrderButtonInfo {

    /**
     * data : [{"statusCode":10,"roleType":2,"statusText":"等待同意","assistButtonList":null,"businessNumber":"27579647792398933994104096013-R-946df9eecaca44a5bb71e82c289ec0f6-1495095702543","countdownVo":null,"buttonList":[{"operateStatusText":"拒绝","operateStatusCode":11,"nextStatusCode":11,"isGray":false},{"operateStatusText":"同意","operateStatusCode":20,"nextStatusCode":20,"isGray":false}]}]
     * total : 0
     * code : 0
     * totalpage : 0
     * msg : 成功
     */

    private int total;
    private int code;
    private int totalpage;
    private String msg;
    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
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
         * statusCode : 10
         * roleType : 2
         * statusText : 等待同意
         * assistButtonList : null
         * businessNumber : 27579647792398933994104096013-R-946df9eecaca44a5bb71e82c289ec0f6-1495095702543
         * countdownVo : null
         * buttonList : [{"operateStatusText":"拒绝","operateStatusCode":11,"nextStatusCode":11,"isGray":false},{"operateStatusText":"同意","operateStatusCode":20,"nextStatusCode":20,"isGray":false}]
         */
        private int statusCode;
        private int roleType;
        private String statusText;
        private List<AssistButtonListBean> assistButtonList;
        private String businessNumber;
        private CountdownVoBean countdownVo;
        private List<ButtonListBean> buttonList;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public int getRoleType() {
            return roleType;
        }

        public void setRoleType(int roleType) {
            this.roleType = roleType;
        }

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }

        public List<AssistButtonListBean> getAssistButtonList() {
            return assistButtonList;
        }

        public void setAssistButtonList(List<AssistButtonListBean> assistButtonList) {
            this.assistButtonList = assistButtonList;
        }

        public String getBusinessNumber() {
            return businessNumber;
        }

        public void setBusinessNumber(String businessNumber) {
            this.businessNumber = businessNumber;
        }

        public CountdownVoBean getCountdownVo() {
            return countdownVo;
        }

        public void setCountdownVo(CountdownVoBean countdownVo) {
            this.countdownVo = countdownVo;
        }

        public List<ButtonListBean> getButtonList() {
            return buttonList;
        }

        public void setButtonList(List<ButtonListBean> buttonList) {
            this.buttonList = buttonList;
        }


    }
}
