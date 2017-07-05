package gongren.com.dlg.pushmsg.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class PushExtraData implements Serializable{
    //{"toRoleType":"1","businessNumber":"27592628286588257426588914739-R-58da38636d444f9991b9dc9ef9a16bb6-1495799376432"}
    public int toRoleType;                     //1:跳转订单详情
    public String businessNumber;         //订单业务编号
    public int orderStatus;         //订单业务编号
}
