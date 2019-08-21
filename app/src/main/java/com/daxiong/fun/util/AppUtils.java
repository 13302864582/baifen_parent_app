
package com.daxiong.fun.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.daxiong.fun.api.MainAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.model.UserInfoModel;

import java.util.List;

public class AppUtils {

    /**
     * 通过RunningTaskInfo类判断 判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过RunningAppProcessInfo类判断（不需要额外权限）： 
     * @author:  sky
     * @param context
     * @return boolean
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager)context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
    private static MainAPI mainAPI;
    /**
     * 点击事件记录
     */
    public static void clickevent(String event_code, BaseActivity listener) {
        //判断是否登录
        UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        int type = MySharePerfenceUtil.getInstance().getGoLoginType();
        if (null != uInfo && type != -1) {
            if(mainAPI==null){
                mainAPI=new MainAPI();
            }
            mainAPI.clickevent(event_code, listener);
        }


    }
  
}
