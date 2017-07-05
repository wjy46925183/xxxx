package gongren.com.dlg.javabean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class NearbyTask implements Serializable{
    public String id;                                  //任务id
    public String price;                              //单价，橙子数量
    public String jobMeterUnit;                      //计量单位
    public String jobMeterUnitName;                 // 计量单位名称时,天,单
    public String xCoordinate;                       //x坐标
    public String yCoordinate;                       //y坐标


}
