package gongren.com.dlg.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */
public class YiKaTongBean implements Serializable {

    /**
     * code : 0
     * msg : 成功
     * total : 0
     * totalpage : 0
     * data : [{"returnRul":null,"billBusinessNumber":null,"sign":null,"nonceStr":null,"mchId":null,"time":null,"appId":null,"prepayId":null,"tradeType":null,"outTradeNo":null,"oddPackage":null,"jsonRquestData":"{\"reqData\":{\"dateTime\":\"20170518170624\",\"date\":\"20170518\",\"amount\":\"100.0\",\"orderNo\":\"5098383794\",\"payNoticePara\":\"5098383794|100.0\",\"merchantSerialNo\":\"20170518170623077993\",\"agrNo\":\"20170518170623330945\",\"signNoticePara\":\"27579322318046497824857254115|打零工|20170518170623330945\",\"signNoticeUrl\":\"http://paytest.chengxinhutong.com/ywtNotify/agreement\",\"payNoticeUrl\":\"http://paytest.chengxinhutong.com/ywtNotify/cmbPayVerify\",\"returnUrl\":\"http://www.merchant.com/path/return.do\",\"branchNo\":\"0021\",\"merchantNo\":\"006455\"},\"charset\":\"UTF-8\",\"sign\":\"6107a6bed6b517592d03573d3f9b37b05c1a32f4262db7e569ada61f7496a837\",\"signType\":\"SHA-256\",\"version\":\"1.0\"}","payUrl":"https://netpay.cmbchina.com/netpayment/BaseHttp.dll?MB_EUserPay"}]
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

    public static class DataBean {
        /**
         * returnRul : null
         * billBusinessNumber : null
         * sign : null
         * nonceStr : null
         * mchId : null
         * time : null
         * appId : null
         * prepayId : null
         * tradeType : null
         * outTradeNo : null
         * oddPackage : null
         * jsonRquestData : {"reqData":{"dateTime":"20170518170624","date":"20170518","amount":"100.0","orderNo":"5098383794","payNoticePara":"5098383794|100.0","merchantSerialNo":"20170518170623077993","agrNo":"20170518170623330945","signNoticePara":"27579322318046497824857254115|打零工|20170518170623330945","signNoticeUrl":"http://paytest.chengxinhutong.com/ywtNotify/agreement","payNoticeUrl":"http://paytest.chengxinhutong.com/ywtNotify/cmbPayVerify","returnUrl":"http://www.merchant.com/path/return.do","branchNo":"0021","merchantNo":"006455"},"charset":"UTF-8","sign":"6107a6bed6b517592d03573d3f9b37b05c1a32f4262db7e569ada61f7496a837","signType":"SHA-256","version":"1.0"}
         * payUrl : https://netpay.cmbchina.com/netpayment/BaseHttp.dll?MB_EUserPay
         */

        private Object returnRul;
        private Object billBusinessNumber;
        private Object sign;
        private Object nonceStr;
        private Object mchId;
        private Object time;
        private Object appId;
        private Object prepayId;
        private Object tradeType;
        private Object outTradeNo;
        private Object oddPackage;
        private String jsonRquestData;
        private String payUrl;

        public Object getReturnRul() {
            return returnRul;
        }

        public void setReturnRul(Object returnRul) {
            this.returnRul = returnRul;
        }

        public Object getBillBusinessNumber() {
            return billBusinessNumber;
        }

        public void setBillBusinessNumber(Object billBusinessNumber) {
            this.billBusinessNumber = billBusinessNumber;
        }

        public Object getSign() {
            return sign;
        }

        public void setSign(Object sign) {
            this.sign = sign;
        }

        public Object getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(Object nonceStr) {
            this.nonceStr = nonceStr;
        }

        public Object getMchId() {
            return mchId;
        }

        public void setMchId(Object mchId) {
            this.mchId = mchId;
        }

        public Object getTime() {
            return time;
        }

        public void setTime(Object time) {
            this.time = time;
        }

        public Object getAppId() {
            return appId;
        }

        public void setAppId(Object appId) {
            this.appId = appId;
        }

        public Object getPrepayId() {
            return prepayId;
        }

        public void setPrepayId(Object prepayId) {
            this.prepayId = prepayId;
        }

        public Object getTradeType() {
            return tradeType;
        }

        public void setTradeType(Object tradeType) {
            this.tradeType = tradeType;
        }

        public Object getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(Object outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public Object getOddPackage() {
            return oddPackage;
        }

        public void setOddPackage(Object oddPackage) {
            this.oddPackage = oddPackage;
        }

        public String getJsonRquestData() {
            return jsonRquestData;
        }

        public void setJsonRquestData(String jsonRquestData) {
            this.jsonRquestData = jsonRquestData;
        }

        public String getPayUrl() {
            return payUrl;
        }

        public void setPayUrl(String payUrl) {
            this.payUrl = payUrl;
        }
    }
}
