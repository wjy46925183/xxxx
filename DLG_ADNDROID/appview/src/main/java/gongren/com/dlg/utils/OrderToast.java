package gongren.com.dlg.utils;

import gongren.com.dlg.activity.BaseActivity;

/**
 * Created by luoxiaohui .
 * on 2017/5/20
 * 文件描述：
 */

public class OrderToast {
    public static void OrderToastShow(BaseActivity activity,boolean isNet){
       /* Display display = activity.getWindowManager().getDefaultDisplay();
        // 获取屏幕高度
        int height = display.getHeight();
        Toast toast = Toast.makeText(activity,
                "进入下一个订单", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0,height*3/5-100);
        toast.show();*/
        if(isNet){
            ToastUtils.showToastShort(activity, "进入下一个订单");
        }else {
            ToastUtils.showToastShort(activity, "进入上一个订单");
        }

    }
}
