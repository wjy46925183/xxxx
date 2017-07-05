package gongren.com.dlg.javabean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class DoingTaskOrderDetailModel implements Serializable {
    public String id;                 //订单Id
    public String employeeId;       //雇员Id
    public String employerId;       //雇主Id
    public String status;            //订单状态
    public String businessNumber;   //订单编号
    public String logo;               //用户头像
    public String isInProgress;     //是否进行中(0.否,1.是)"
    public String currentOperateStatus;        //需求类型(1.工作日,2.双休日,3.计件)"
    public String jobId;             //任务id
    public String postName;          //任务名
    public String postType;          //任务类型
    public String name;               //任务对接人
    public String scoreCount;        //评分统计
    public String creditCount;       //诚信统计
    public String price;              //订单单价
    public String totalPrice;        //订单总价
    public String postTypeName;
    public String meterUnitName;         //1, 零工计量单位(1.天,2.时,3.单)
    public XYCoordinateVo xyCoordinateVo;   //任务坐标信息
    public String startDate;                //订单开始时间
    public String endDate;                  //订单结束时间
    public String provinceName;            //任务地址(省)
    public String tipAmount; //小费
    public String modifyUserId; //小费


    public String workAddress;             //任务地址信息(市)
    public String villageName;             //任务地址详情(镇)
    public String areaName;               //任务地址详情(区)
    public int demandType;
    public int isFarmersInsurance;

    public class XYCoordinateVo implements Serializable{
        public String jobXCoordinate;  //任务坐标x
        public String jobYCoordinate;  //任务坐标y
        public String userXCoordinate;  //x坐标
        public String userYCoordinate;  //y坐标
        public String distanceToJob;   //坐标距离（公里）

    }
}
