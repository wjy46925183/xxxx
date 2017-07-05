package gongren.com.dlg.utils;

/**
 * 时间，日期的处理函数
 * Created by Administrator on 2017/4/19.
 */
public class TimeUtils {

    /**
     * 根据微妙值，算出，日时分秒
     * @param weimiao
     * @return
     */
    public static String getRemainTimeByWM(long weimiao){

        if(weimiao<=0){
            return "";
        }

        int SEC = 1000;
        int MIN = 1000*60;
        int HOUR = 1000*60*60;
        int DAY = 1000*60*60*24;

        int day = (int)(weimiao/DAY);
        int hour = (int)(weimiao%DAY/HOUR);
        int minitu = (int)(weimiao%DAY%HOUR/MIN);
        int second = (int)(weimiao%DAY%HOUR%MIN/SEC);

        String result = "";
        if(day==0){
            if(hour==0){
                if(minitu ==0){
                    if(second != 0){
                        result = second+"秒";
                    }
                }else{
                    result = minitu+"分钟"+second+"秒";
                }
            }else{
                result = hour+"小时"+minitu+"分钟"+second+"秒";
            }
        }else{
            result = day+"天"+hour+"小时"+minitu+"分钟"+second+"秒";
        }
        return result;
    }
}
