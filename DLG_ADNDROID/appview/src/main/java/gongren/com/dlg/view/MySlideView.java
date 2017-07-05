package gongren.com.dlg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/3/30.
 */
public class MySlideView extends LinearLayout {

    public MySlideView(Context context) {
        super(context);
    }

    public MySlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //超过40dp触发事件
//                if (DimensUtils.getScreenWith(activity) - event.getRawX() > 300) {
//                    worker_slid_layout.setPadding(DimensUtils.dip2px(activity, 2), DimensUtils.dip2px(activity, 2),
//                            (int) 2.5 * DimensUtils.px2dip(activity, 60)
//                            , DimensUtils.dip2px(activity, 2));
//                    //进入订单页面
//                    mapclickFlag = true;
//                    EventBus.getDefault().post(new MainEvent("", 4));
//                    ActingOrderModel();
//                }
//                //滑动在10dp和40dp之间
//                if (DimensUtils.getScreenWith(activity) - event.getRawX() >= 50 &&
//                        DimensUtils.getScreenWith(activity) - event.getRawX() <= 300) {
//                    worker_slid_layout.setPadding(DimensUtils.dip2px(activity, 2), DimensUtils.dip2px(activity, 2),
//                            (int) (2.5 * DimensUtils.px2dip(activity, (float)
//                                    (DimensUtils.getScreenWith(activity) - event.getRawX())))
//                            , DimensUtils.dip2px(activity, 2));
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                if (DimensUtils.getScreenWith(activity) - event.getRawX() >= 50 &&
//                        DimensUtils.getScreenWith(activity) - event.getRawX() <= 300) {
//                    worker_slid_layout.setPadding(DimensUtils.dip2px(activity, 2), DimensUtils.dip2px(activity, 2),
//                            (int) 2.5 * DimensUtils.px2dip(activity, 60)
//                            , DimensUtils.dip2px(activity, 2));
//                }
//                break;
//        }
//        return true;
//    }
}
