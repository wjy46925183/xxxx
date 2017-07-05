package gongren.com.dlg.javabean;

import java.util.ArrayList;

/**
 * 快捷支付短信返回
 */
public class QuickPaymentSmsJson {
    public String code;
    public ArrayList<QuickPaymentNumber>data;
    public String msg;
    public String total;
    public String totalpage;
}
