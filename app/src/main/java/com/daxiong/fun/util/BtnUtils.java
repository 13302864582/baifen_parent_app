
package com.daxiong.fun.util;

public class BtnUtils {

    private static long lastClickTime;

    /**
     * 防止按钮重复点击
     * 
     * @author: sky
     * @return boolean
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    

}
