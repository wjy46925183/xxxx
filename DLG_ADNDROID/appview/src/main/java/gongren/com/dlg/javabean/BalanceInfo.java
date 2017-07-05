package gongren.com.dlg.javabean;

import java.io.Serializable;

/**
 * 收支详情传的对象
 */
public class BalanceInfo implements Serializable{

    /**
     * id : 796590236275707900
     * entityId : 781048309899989000
     * bizsys : casual-p
     * tradeId : 796590235969523712
     * beforCashAva : 10
     * cashAva : -10
     * afterCashAva : 0
     * createDate : 1478756887000
     * subject : 预付款
     * transtype : trans_40000
     * memo : 诚心胡同|null|1|10|1|4.0
     */

    private double cashAva;
    private double afterCashAva;
    private String subject;
    private String memo;

    public BalanceInfo(double cashAva, double afterCashAva, String subject, String memo) {
        this.cashAva = cashAva;
        this.afterCashAva = afterCashAva;
        this.subject = subject;
        this.memo = memo;
    }

    public double getCashAva() {
        return cashAva;
    }

    public void setCashAva(double cashAva) {
        this.cashAva = cashAva;
    }

    public double getAfterCashAva() {
        return afterCashAva;
    }

    public void setAfterCashAva(double afterCashAva) {
        this.afterCashAva = afterCashAva;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
