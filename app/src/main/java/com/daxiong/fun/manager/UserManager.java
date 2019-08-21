
package com.daxiong.fun.manager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.MySharePerfenceUtil;


/**
 * 处理用户的一些逻辑的函数
 * @author:  sky
 */
public class UserManager {
  
    
    private UserManager(){}
    
    public static  class AccountManagerHolder{
        private static UserManager instance=new UserManager();
    } 
    
    public static UserManager getInstance(){
        return AccountManagerHolder.instance;
    }

    /**
     * 判断是跳转主页还是引导页
     * @author: sky
     * @return boolean
     */
    public  boolean judgeGoGuideWithMain() {
        boolean isLogin = false;
        UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null != uInfo) {
            isLogin = true;
        }

        int type = MySharePerfenceUtil.getInstance().getGoLoginType();
        if (type != MySharePerfenceUtil.LOGIN_TYPE_PHONE) {
            isLogin = false;
        }
        return isLogin;
    }


    /**
     * 判断是否是vip
     * @return
     */
    public boolean judgeIsVIP(UserInfoModel mUserInfo) {
        if (mUserInfo.getSupervip() == 1) {// 正式vip
            return true;
        } else {//不是vip
            return false;
        }

    }


    /**
     * 直接打电话
     */
    public void callPhone(BaseActivity activity) {
        // 用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "400-6755-222"));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        activity.startActivity(intent);
        activity.finish();
    }


    /**
     * 根据路径添加图片model
     * @param path
     * @return
     */
    public  StuPublishHomeWorkPageModel getAddPhoto(String path) {
        StuPublishHomeWorkPageModel newModel = new StuPublishHomeWorkPageModel();
        newModel.setImgpath(path);
        return newModel;
    }




}
