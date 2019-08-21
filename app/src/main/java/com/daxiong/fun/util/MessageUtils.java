package com.daxiong.fun.util;

import android.text.TextUtils;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.model.UserInfoModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 消息工具类
 * @author:  sky
 */
public class MessageUtils {
    
    private static final String TAG = "MessageUtils";
    
    
    /**
     * 每次GO服务器登陆之后，发送GO服务器返回的Session到Python服务器（用于聊天），以保证两边Session一致
     */
    public static void sendSessionToServer() {        
        String tokenId = MySharePerfenceUtil.getInstance().getWelearnTokenId();
        if (TextUtils.isEmpty(tokenId)) {
            LogUtils.e(TAG, "token id is empty !");
            return;
        }
        UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null == uInfo || uInfo.getUserid() <= 0) {
            LogUtils.e(TAG, "user id is empty !");
            return;
        }
        
        int userId = uInfo.getUserid();
        
        try {
            JSONObject json = new JSONObject();
            json.put("userid", userId);
            json.put("sessionid", tokenId);
            json.put("timestamp", System.currentTimeMillis());
            json.put("type", 2);
            json.put("subtype", 8);
            json.put("platform", "android");
            MyApplication.mNetworkUtil.sendTextmessage(json.toString());
            
            LogUtils.i(TAG, "send session to server ! session = " + tokenId);
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    
    
    
    

}
