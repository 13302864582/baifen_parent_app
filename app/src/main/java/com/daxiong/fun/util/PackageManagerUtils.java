package com.daxiong.fun.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.daxiong.fun.MyApplication;


/**
 * 包管理器工具类
 * 此类的描述： 
 * @author:  sky
 */

public class PackageManagerUtils {

    
    
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getVersionName() {
        try {
            PackageManager manager = MyApplication.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo( MyApplication.getContext().getPackageName(), 0);
            String versionName = info.versionName;
            return  versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 获取版本号
     * 此方法描述的是：
     * @author:  sky
     * @return int
     */
    public static int getVersionCode() {
        try {
            PackageManager manager = MyApplication.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo( MyApplication.getContext().getPackageName(), 0);
            int versionCode = info.versionCode;
            return  versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
        
    }
    
    
    
}
