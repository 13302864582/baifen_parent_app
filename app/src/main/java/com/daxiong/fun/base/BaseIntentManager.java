package com.daxiong.fun.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 抽象的IntentManager类
 * @author:  sky
 */
public abstract class BaseIntentManager {
    
    
    public static void openActivity(Activity activity, Class<? extends Activity> activityClazz,
            Bundle bundle, boolean isFinish) {
        try {
            Intent intent = new Intent(activity, activityClazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            activity.startActivity(intent);
            // add by milo 2014.09.11
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            if (isFinish) {
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
