package com.wtm.library.base;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;


/**
 * 将实现标记接口I_Activity的Activity进行堆栈管理
 * 如果实现了接口I_Activity,就认为是Activity
 * @author wtm
 */
public class WActivityManager {

    private WActivityManager() {
        aStack = new Stack<>();
    };

    private static class ManagerHolder {
        private static final WActivityManager instance = new WActivityManager();
    }

    private Stack<I_Activity> aStack;

    public static WActivityManager getInstance() {
        return ManagerHolder.instance;
    }

    /**
     * 获取当前栈内数量
     *
     * @return
     */
    public int getCount() {
        return aStack.size();
    }

    /**
     * 添加activity到栈
     *
     * @param activity
     */
    public void addActivity2Stack(I_Activity activity) {
        if (activity != null) {
            aStack.push(activity);
            Log.d("DEBUG", activity.getClass().getName()+"加入管理栈");
        }
    }

    /**
     * 获取当前activity
     *
     * @return
     */
    public Activity topActivity() {
        if (aStack.isEmpty()) {
            return null;
        }
        return (Activity) aStack.peek();
    }

    /**
     * 根据Class查找Activity 没有找到返回空
     *
     * @param clazz
     * @return
     */
    public Activity findActivity(Class<?> clazz) {
        for (I_Activity aty : aStack) {
            if (aty.getClass().equals(clazz)) {
                return (Activity) aty;
            }
        }
        return null;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity() {
        Activity activity = (Activity) aStack.pop();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && aStack.remove(activity)) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        for (I_Activity activity : aStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity((Activity)activity);
            }
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        for (I_Activity activity : aStack) {
            if (!(activity.getClass().equals(cls))) {
                finishActivity((Activity)activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = aStack.size(); i < size; i++) {
            if (null != aStack.get(i)) {
                ((Activity)aStack.get(i)).finish();
            }
        }
        aStack.clear();
    }

    /**
     * 应用程序退出
     */
    public void appExit(Context context) {
        Log.d("DEBUG", getClass().getName() + "应用程序退出");
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            System.exit(-1);
        }
    }

}
