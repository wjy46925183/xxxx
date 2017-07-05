package gongren.com.dlg.utils;

import android.support.v7.app.AppCompatActivity;

import java.util.Stack;

/**
 * Created by Wangjinya on 2017/5/31.
 * Activity管理器
 */

public class ActivityController {
    private static Stack<AppCompatActivity> activityStack;// activity栈

    public static void removeAll() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                AppCompatActivity activity = getFirstElement();
                if (activity == null) {
                    break;
                }
                activity.finish();
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 移除一个activity
     * @param activity
     */
    public static void removerActivity(AppCompatActivity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activity.finish();
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 保留几个
     * postion
     */
    public static void finishSpecifiedActivity(int postion) {
        if (activityStack != null) {
            while (activityStack.size() > postion) {
                AppCompatActivity activity = activityStack.get(postion);
                if (activity == null) {
                    break;
                }
                activity.finish();
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 把一个activity压入栈中
     * @param actvity
     */
    public static void addActivity(AppCompatActivity actvity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(actvity);
    }

    /**
     * 获取栈顶的activity，先进后出原则
     * @return
     */
    public static AppCompatActivity getLastActivity() {
        return activityStack.lastElement();
    }

    /**
     * 获取栈底的activity，先进后出原则
     * @return
     */
    public static AppCompatActivity getFirstElement() {
        return activityStack.firstElement();
    }

}
