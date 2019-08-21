
package com.daxiong.fun.util;

import android.content.Context;

import com.daxiong.fun.MyApplication;

public class DensityUtil {

    /**
     * 取得屏幕的宽度 px
     * 
     * @return
     */
    public static int getScreenWidth() {
        int width = MyApplication.getContext().getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 取得屏幕的高度
     * 
     * @return
     */
    public static int getScreenHeight() {
        int height = MyApplication.getContext().getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = getScreenDensity(context);
        return (int)(dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    // 获取手机型号
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    // SDK版本
    public static String getSDKVersion() {
        return android.os.Build.VERSION.SDK;
    }

    // 系统版本
    public static String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

}
