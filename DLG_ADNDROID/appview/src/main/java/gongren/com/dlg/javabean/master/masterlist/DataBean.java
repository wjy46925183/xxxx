package gongren.com.dlg.javabean.master.masterlist;

import java.io.Serializable;
import java.util.List;

public class DataBean implements Serializable{
    /**
     * id : 27590913366578701033533332680
     * postName : 酷兔兔
     * postTypeName : 生活服务
     * price : 200
     * jobMeterUnit : 1
     * jobMeterUnitName : 天
     * demandType : 1
     * startYear : 2017
     * startMonth : 5
     * startDay : 25
     * startHour : 18
     * startMinute : 0
     * startSecond : 1
     * endYear : 2017
     * endMonth : 5
     * endDay : 26
     * endHour : 23
     * endMinute : 0
     * endSecond : 1
     * distance : null
     * userId : 27590418532668923774811233102
     * status : 10
     * provinceName : 上海市
     * cityName : 上海市
     * areaName : 普陀区
     * villageName : 长征镇
     * workAddress : 真北路958号
     * userCreditCount : null
     * userLogo : null
     * xCoordinate : 121.383976
     * yCoordinate : 31.233449
     * detailsUrl : http://dlg.dalinggong.com/api****?taskId=27590913366578701033533332680
     * orderStatusList : [{"status":10,"list":[{"modifyUserId":null,"modifyTime":1495706447000,"employeeId":"27590720007807320410013293581","employerId":"27590418532668923774811233102","status":10,"demandType":1,"businessNumber":"27590914030661487687077190858-R-a8414b76168244b0bfcf26c05b42c919-1495706446837","isInProgress":1,"isCancel":0,"cancelCause":null,"currentOperateStatus":10,"evaluateLevel":null,"isEvaluate":null,"jobId":"27590913366578701033533332680","jobTitle":"","postName":"酷兔兔","postType":"job.type_3","postTypeName":"生活服务","name":"刘嘉雯","phone":"13142114843","logo":null,"releaseCount":8,"joinCount":10,"scoreCount":null,"creditCount":null,"distance":"10公里","distanceValue":null,"description":null,"xyCoordinateVo":null,"startDate":null,"endDate":null,"price":200,"totalPrice":200,"meterUnit":1,"meterUnitName":"天","provinceId":"310000","provinceName":"上海市","cityId":"310100","cityName":"上海市","areaId":"150000","areaName":"普陀区","villageId":"310107102","villageName":"长征镇","workAddress":"真北路958号","auditPermission":null,"parentJobId":null,"height":null,"weight":null,"sex":null,"identity":null,"location":null,"statusCode":null,"statusText":null,"buttonList":null,"amount":null,"serviceAmount":null,"cancelAmount":null,"tipAmount":null}]}]
     */

    public String id;
    public String postName;
    public String postTypeName;
    public String taskDescription; //描述
    public double price;
    public int jobMeterUnit;
    public String jobMeterUnitName;
    public int demandType;
    public int startYear;
    public int startMonth;
    public int startDay;
    public int startHour;
    public int startMinute;
    public int recruitNumber;
    public int startSecond;
    public int endYear;
    public int endMonth;
    public int endDay;
    public int endHour;
    public int endMinute;
    public int endSecond;
    public String distance;
    public String userId;
    public int status;
    public String provinceName;
    public String cityName;
    public String areaName;
    public String villageName;
    public String workAddress;
    public String userCreditCount;
    public String userLogo;
    public double xCoordinate;
    public double yCoordinate;
    public String detailsUrl;
    public List<OrderStatusListBean> orderStatusList;
    public int isFarmersInsurance;
}