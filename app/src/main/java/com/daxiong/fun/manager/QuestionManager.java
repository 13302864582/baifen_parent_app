
package com.daxiong.fun.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.daxiong.fun.function.homework.PublishHomeWorkActivity;
import com.daxiong.fun.function.question.PayAnswerAskVipActivity;
import com.daxiong.fun.function.question.WaibaoPayAnswerAskActivity;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.model.UserInfoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 提问的管理类 此类的描述：
 * 
 * @author: sky
 */
public class QuestionManager {

    private static QuestionManager instance;

    private QuestionManager() {
    }

    public static QuestionManager getInstance() {
        if (null == instance) {
            instance = new QuestionManager();
        }
        return instance;
    }

    public void goToOutsouringQuestionActivity(Activity context, String orgname, int orgid,
            ArrayList<OrgModel> listModels, int type) {
        Bundle data = new Bundle();
        data.putSerializable(OrgModel.TAG, listModels);
        data.putInt("orgid", orgid);
        data.putString("orgname", orgname);
        data.putInt("type", type);// 是否是特殊帐号
        boolean isFinish = false;
        if (context instanceof PublishHomeWorkActivity) {
            isFinish = true;
        }
       HomeworkManager.getInstance().openActivity(context, WaibaoPayAnswerAskActivity.class, data, isFinish);

    }
    
    public void goToStuPublishQuestionVipActivity(FragmentActivity context, String orgname,
            int orgid, List<OrgModel> listModels) {
        Bundle data = new Bundle();
        data.putSerializable(OrgModel.TAG, (Serializable)listModels);
        data.putInt("orgid", orgid);
        data.putString("orgname", orgname);
        boolean isFinish = false;
        if (context instanceof PublishHomeWorkActivity) {
            isFinish = true;
        }
        HomeworkManager.getInstance().openActivity(context, PayAnswerAskVipActivity.class, data, isFinish);

    }
    
    
    public void goNotPublishQuestionVipActivity(FragmentActivity activity, Class<? extends Activity> activityClazz,
            Bundle bundle, boolean isFinish) {
        try {
            Intent intent = new Intent(activity, activityClazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            activity.startActivity(intent);
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            if (isFinish) {
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 此方法描述的是：判断选择发题者的uid是否在作业权限中
     * @author:  sky
     * @param uid
     * @param dataJson
     * @return boolean
     */
    public boolean getUseridInQuestionlist(int uid,String dataJson)  {        
        List<String> userids = new ArrayList<String>();       
        try {
            JSONObject json= new JSONObject(dataJson);
            JSONArray homeworkArray = json.optJSONArray("questionlist");
            UserInfoModel userinfo = null;
            if (homeworkArray != null && homeworkArray.length() > 0) {
                for (int i = 0; i < homeworkArray.length(); i++) {
                    JSONObject obj = homeworkArray.getJSONObject(i);
                    int userid=obj.optInt("userid");                
                    userids.add(userid+"");
                }
            }
            
            if (userids.contains(uid+"")) {//是否在
                return true;
            }    
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
       
    }
    
    
    /**
     * 此方法描述的是：解析特殊学生列表
     * 
     * @author: sky
     * @最后修改日期:2015年8月8日 下午8:56:49 parseSpecialStudent
     * @param dataJson
     * @throws JSONException void
     */
    public List<UserInfoModel> parseSpecialStudent(String dataJson) throws JSONException {
        List<UserInfoModel> publiserList = new ArrayList<UserInfoModel>();
        JSONObject json = new JSONObject(dataJson);
        JSONArray homeworkArray = json.optJSONArray("homeworklist");
        JSONArray questionArray = json.optJSONArray("questionlist");

//        if (homeworkArray != null && homeworkArray.length() > 0) {
//            UserInfoModel userinfo = null;
//            for (int i = 0; i < homeworkArray.length(); i++) {
//                JSONObject obj = homeworkArray.getJSONObject(i);
//                userinfo = new UserInfoModel();
//                userinfo.setUserid(obj.optInt("userid"));
//                userinfo.setName(obj.optString("name"));
//                userinfo.setGrade(obj.optString("grade"));
//                publiserList.add(userinfo);
//            }
//        }

        if (questionArray != null && questionArray.length() > 0) {
            UserInfoModel userinfo = null;
            for (int i = 0; i < questionArray.length(); i++) {
                JSONObject obj = questionArray.getJSONObject(i);
                userinfo = new UserInfoModel();
                userinfo.setUserid(obj.optInt("userid"));
                userinfo.setName(obj.optString("name"));
                userinfo.setGrade(obj.optString("grade"));
                publiserList.add(userinfo);
            }
        }
        return publiserList;
    }


}
