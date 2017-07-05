package gongren.com.dlg.javabean;

import java.io.Serializable;

/**
 * 充值界面跳转招商银行网页界面传的参数
 */
public class Cmb implements Serializable {

    /**
     * payUrl : http://61.144.248.29:801/netpayment/BaseHttp.dll?PrePayEUserP
     * result : SUCCESS
     * msg :
     * branchID : 0021
     * coNo : 000141
     * billNo : 1000011
     * merchantUrl : null
     * merchantPara :
     * merchantCode : |VULnT4erMxA4rDs4jf2*ZC*gYMsyspmBH63Y48RRVAXrI1ZHs1kkHz8iTjuJ2J7pm0YEbzdMsU6pjV8NsNAuStWvgDKNN*vmcXm5XGHhE8qaj0qq8n8Ii27IovEnoeNz1MV62Bv8cebTw*kPI9rn6lC2s*yqXavw/ZCSrnMdQOSkBvc0ugeM8YgNwKnb0N5eIdCB8sxLmBdc8klzcbCL*YYIbJhjOavuZEKV*oFT3DFtY3ftREjHnCRGTgctt954e9*s4SIyYUQcudRg2whIpLRaSPi12wg6k50yzvCizk3qJOSkUaJEfgsuYAesb1A92zG/IrnP3iQI5SAVOiCstkz96YF5rw0emsGOazFrvcCX*4sqjX8=|8378088839162d5e7f9db707211239b7e05cd9e2
     * amount : 1
     */

    private String payUrl;
    private String branchID;
    private String coNo;
    private String billNo;
    private String merchantUrl;
    private String merchantPara;
    private String merchantCode;
    private String amount;

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getCoNo() {
        return coNo;
    }

    public void setCoNo(String coNo) {
        this.coNo = coNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getMerchantUrl() {
        return merchantUrl;
    }

    public void setMerchantUrl(String merchantUrl) {
        this.merchantUrl = merchantUrl;
    }

    public String getMerchantPara() {
        return merchantPara;
    }

    public void setMerchantPara(String merchantPara) {
        this.merchantPara = merchantPara;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Cmb(String payUrl, String branchID, String coNo, String billNo, String merchantUrl, String merchantPara, String merchantCode, String amount) {
        this.payUrl = payUrl;
        this.branchID = branchID;
        this.coNo = coNo;
        this.billNo = billNo;
        this.merchantUrl = merchantUrl;
        this.merchantPara = merchantPara;
        this.merchantCode = merchantCode;
        this.amount = amount;
    }
}
