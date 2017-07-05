package gongren.com.dlg.javabean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class DoingTaskOrderButtonModel implements Serializable {
    public String id;                                  //订单id
    public String roleType;                           //角色类型(1.雇员,2.雇主,3.系统)
    public String statusCode;                         //状态代码
    public String statusText;                         //状态文本
    public String operateStatusCode;                 //(按钮)操作状态值
    public String operateStatusText;                 //(按钮)操作状态文本
    public String nextStatusCode;                     //下一个状态值
    public String messageTemplate;                   //对应状态的消息模版
    public String mapPromptText;                     //地图提示文本
}
