package gongren.com.dlg.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * 发票历史数据
 */
public class ReceiptHistoryDataJson implements Serializable{




        /**
         * data : [{"createTime":1496404120000,"status":1,"invoiceAmount":0.1,"expressOrderNumber":null,"expressCompany":null,"version":2,"id":"27603783825939624860505284449","invoiceInformationRestVo":{"taxpayerIdentificationCode":"恩","createTime":1496404120000,"checkTakerName":"莫","remark":null,"invoiceType":null,"checkTakerAddress":"莫","bankName":"恩","registerAddress":"莫","registerPhone":"恩","companyName":"恩","checkTakerPhone":"五","id":"27603783825939624860505284448","userId":"27602029927959840769259977175","bankAccount":"恩"},"modifyUserId":"27602029927959840769259977175","invoiceInformationId":"27603783825939624860505284448","active":1,"invoiceTax":0,"modifyTime":1496404120000,"createUserId":"27602029927959840769259977175"}]
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

        public static class DataBean implements Serializable{
                /**
                 * createTime : 1496404120000
                 * status : 1
                 * invoiceAmount : 0.1
                 * expressOrderNumber : null
                 * expressCompany : null
                 * version : 2
                 * id : 27603783825939624860505284449
                 * invoiceInformationRestVo : {"taxpayerIdentificationCode":"恩","createTime":1496404120000,"checkTakerName":"莫","remark":null,"invoiceType":null,"checkTakerAddress":"莫","bankName":"恩","registerAddress":"莫","registerPhone":"恩","companyName":"恩","checkTakerPhone":"五","id":"27603783825939624860505284448","userId":"27602029927959840769259977175","bankAccount":"恩"}
                 * modifyUserId : 27602029927959840769259977175
                 * invoiceInformationId : 27603783825939624860505284448
                 * active : 1
                 * invoiceTax : 0
                 * modifyTime : 1496404120000
                 * createUserId : 27602029927959840769259977175
                 */

                private String createTime;
                private int status;
                private double invoiceAmount;
                private Object expressOrderNumber;
                private Object expressCompany;
                private int version;
                private String id;
                private InvoiceInformationRestVoBean invoiceInformationRestVo;
                private String modifyUserId;
                private String invoiceInformationId;
                private int active;
                private float invoiceTax;
                private long modifyTime;
                private String createUserId;

                public String getCreateTime() {
                        return createTime;
                }

                public void setCreateTime(String createTime) {
                        this.createTime = createTime;
                }

                public int getStatus() {
                        return status;
                }

                public void setStatus(int status) {
                        this.status = status;
                }

                public double getInvoiceAmount() {
                        return invoiceAmount;
                }

                public void setInvoiceAmount(double invoiceAmount) {
                        this.invoiceAmount = invoiceAmount;
                }

                public Object getExpressOrderNumber() {
                        return expressOrderNumber;
                }

                public void setExpressOrderNumber(Object expressOrderNumber) {
                        this.expressOrderNumber = expressOrderNumber;
                }

                public Object getExpressCompany() {
                        return expressCompany;
                }

                public void setExpressCompany(Object expressCompany) {
                        this.expressCompany = expressCompany;
                }

                public int getVersion() {
                        return version;
                }

                public void setVersion(int version) {
                        this.version = version;
                }

                public String getId() {
                        return id;
                }

                public void setId(String id) {
                        this.id = id;
                }

                public InvoiceInformationRestVoBean getInvoiceInformationRestVo() {
                        return invoiceInformationRestVo;
                }

                public void setInvoiceInformationRestVo(InvoiceInformationRestVoBean invoiceInformationRestVo) {
                        this.invoiceInformationRestVo = invoiceInformationRestVo;
                }

                public String getModifyUserId() {
                        return modifyUserId;
                }

                public void setModifyUserId(String modifyUserId) {
                        this.modifyUserId = modifyUserId;
                }

                public String getInvoiceInformationId() {
                        return invoiceInformationId;
                }

                public void setInvoiceInformationId(String invoiceInformationId) {
                        this.invoiceInformationId = invoiceInformationId;
                }

                public int getActive() {
                        return active;
                }

                public void setActive(int active) {
                        this.active = active;
                }

                public float getInvoiceTax() {
                        return invoiceTax;
                }

                public void setInvoiceTax(float invoiceTax) {
                        this.invoiceTax = invoiceTax;
                }

                public long getModifyTime() {
                        return modifyTime;
                }

                public void setModifyTime(long modifyTime) {
                        this.modifyTime = modifyTime;
                }

                public String getCreateUserId() {
                        return createUserId;
                }

                public void setCreateUserId(String createUserId) {
                        this.createUserId = createUserId;
                }

                public static class InvoiceInformationRestVoBean implements Serializable{
                        /**
                         * taxpayerIdentificationCode : 恩
                         * createTime : 1496404120000
                         * checkTakerName : 莫
                         * remark : null
                         * invoiceType : null
                         * checkTakerAddress : 莫
                         * bankName : 恩
                         * registerAddress : 莫
                         * registerPhone : 恩
                         * companyName : 恩
                         * checkTakerPhone : 五
                         * id : 27603783825939624860505284448
                         * userId : 27602029927959840769259977175
                         * bankAccount : 恩
                         */

                        private String taxpayerIdentificationCode;
                        private long createTime;
                        private String checkTakerName;
                        private Object remark;
                        private Object invoiceType;
                        private String checkTakerAddress;
                        private String bankName;
                        private String registerAddress;
                        private String registerPhone;
                        private String companyName;
                        private String checkTakerPhone;
                        private String id;
                        private String userId;
                        private String bankAccount;

                        public String getTaxpayerIdentificationCode() {
                                return taxpayerIdentificationCode;
                        }

                        public void setTaxpayerIdentificationCode(String taxpayerIdentificationCode) {
                                this.taxpayerIdentificationCode = taxpayerIdentificationCode;
                        }

                        public long getCreateTime() {
                                return createTime;
                        }

                        public void setCreateTime(long createTime) {
                                this.createTime = createTime;
                        }

                        public String getCheckTakerName() {
                                return checkTakerName;
                        }

                        public void setCheckTakerName(String checkTakerName) {
                                this.checkTakerName = checkTakerName;
                        }

                        public Object getRemark() {
                                return remark;
                        }

                        public void setRemark(Object remark) {
                                this.remark = remark;
                        }

                        public Object getInvoiceType() {
                                return invoiceType;
                        }

                        public void setInvoiceType(Object invoiceType) {
                                this.invoiceType = invoiceType;
                        }

                        public String getCheckTakerAddress() {
                                return checkTakerAddress;
                        }

                        public void setCheckTakerAddress(String checkTakerAddress) {
                                this.checkTakerAddress = checkTakerAddress;
                        }

                        public String getBankName() {
                                return bankName;
                        }

                        public void setBankName(String bankName) {
                                this.bankName = bankName;
                        }

                        public String getRegisterAddress() {
                                return registerAddress;
                        }

                        public void setRegisterAddress(String registerAddress) {
                                this.registerAddress = registerAddress;
                        }

                        public String getRegisterPhone() {
                                return registerPhone;
                        }

                        public void setRegisterPhone(String registerPhone) {
                                this.registerPhone = registerPhone;
                        }

                        public String getCompanyName() {
                                return companyName;
                        }

                        public void setCompanyName(String companyName) {
                                this.companyName = companyName;
                        }

                        public String getCheckTakerPhone() {
                                return checkTakerPhone;
                        }

                        public void setCheckTakerPhone(String checkTakerPhone) {
                                this.checkTakerPhone = checkTakerPhone;
                        }

                        public String getId() {
                                return id;
                        }

                        public void setId(String id) {
                                this.id = id;
                        }

                        public String getUserId() {
                                return userId;
                        }

                        public void setUserId(String userId) {
                                this.userId = userId;
                        }

                        public String getBankAccount() {
                                return bankAccount;
                        }

                        public void setBankAccount(String bankAccount) {
                                this.bankAccount = bankAccount;
                        }
                }
        }
}
