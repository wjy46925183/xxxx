package gongren.com.dlg.javabean.master.masterlist;

import java.io.Serializable;
import java.util.List;

public class OrderStatusListBean implements Serializable{
    /**
     * status : 10
     * list : [{"modifyUserId":null,"modifyTime":1495706447000,"employeeId":"27590720007807320410013293581","employerId":"27590418532668923774811233102","status":10,"demandType":1,"businessNumber":"27590914030661487687077190858-R-a8414b76168244b0bfcf26c05b42c919-1495706446837","isInProgress":1,"isCancel":0,"cancelCause":null,"currentOperateStatus":10,"evaluateLevel":null,"isEvaluate":null,"jobId":"27590913366578701033533332680","jobTitle":"","postName":"酷兔兔","postType":"job.type_3","postTypeName":"生活服务","name":"刘嘉雯","phone":"13142114843","logo":null,"releaseCount":8,"joinCount":10,"scoreCount":null,"creditCount":null,"distance":"10公里","distanceValue":null,"description":null,"xyCoordinateVo":null,"startDate":null,"endDate":null,"price":200,"totalPrice":200,"meterUnit":1,"meterUnitName":"天","provinceId":"310000","provinceName":"上海市","cityId":"310100","cityName":"上海市","areaId":"150000","areaName":"普陀区","villageId":"310107102","villageName":"长征镇","workAddress":"真北路958号","auditPermission":null,"parentJobId":null,"height":null,"weight":null,"sex":null,"identity":null,"location":null,"statusCode":null,"statusText":null,"buttonList":null,"amount":null,"serviceAmount":null,"cancelAmount":null,"tipAmount":null}]
     */

    public int status;
    public List<ListBean> list;
}