package gongren.com.dlg.javabean.base;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：按钮控件值
 * 创建时间：2017/5/25 12:51
 */
public class ButtonListBean implements Serializable{
    /**
     * operateStatusCode : 21
     * operateStatusText : 开工打卡
     * nextStatusCode : 21
     * isGray : true
     */
    public int operateStatusCode;
    public String operateStatusText;
    public int nextStatusCode;
    public boolean isGray;
}
