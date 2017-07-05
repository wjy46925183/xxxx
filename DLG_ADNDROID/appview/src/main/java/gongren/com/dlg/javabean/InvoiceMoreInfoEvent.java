package gongren.com.dlg.javabean;

/**
 * 开发票-发票更多信息
 */
public class InvoiceMoreInfoEvent {
    private String id;
    private String taxpayerDistinguishNumber;
    private String companyAddress;
    private String companyPhone;
    private String bankAccount;
    private String bankAccountNumber;
    private String remarksExplain;

    public InvoiceMoreInfoEvent(String taxpayerDistinguishNumber, String companyAddress, String companyPhone, String bankAccount, String bankAccountNumber, String remarksExplain) {
        this.taxpayerDistinguishNumber = taxpayerDistinguishNumber;
        this.companyAddress = companyAddress;
        this.companyPhone = companyPhone;
        this.bankAccount = bankAccount;
        this.bankAccountNumber = bankAccountNumber;
        this.remarksExplain = remarksExplain;
    }

    public InvoiceMoreInfoEvent() {
    }

    public String getId() {
        return id;
    }

    public String getTaxpayerDistinguishNumber() {
        return taxpayerDistinguishNumber;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getRemarksExplain() {
        return remarksExplain;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTaxpayerDistinguishNumber(String taxpayerDistinguishNumber) {
        this.taxpayerDistinguishNumber = taxpayerDistinguishNumber;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public void setRemarksExplain(String remarksExplain) {
        this.remarksExplain = remarksExplain;
    }
}
