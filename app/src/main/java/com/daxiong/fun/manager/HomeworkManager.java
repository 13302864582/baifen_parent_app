
package com.daxiong.fun.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.function.homework.PublishHomeWorkActivity;
import com.daxiong.fun.function.homework.WaibaoHomeWorkActivity;
import com.daxiong.fun.model.MyOrgModel;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.MySharePerfenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类的描述：homework逻辑控制类
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015年8月8日 下午6:25:57
 */

public class HomeworkManager {

	private static HomeworkManager instance;

	private HomeworkManager() {
	};

	public static HomeworkManager getInstance() {
		synchronized (HomeworkManager.class) {			
			if (instance == null) {
				instance = new HomeworkManager();
			}
			return instance;
		}
	}

	public MyOrgModel parseJsonForMyOrg(String dataJson) {
		MyOrgModel myOrgModel = null;
		try {

			// JSONObject json = new JSONObject(dataJson);
			// JSONObject specialUserObject = json.getJSONObject("specialuser");
			// int type = specialUserObject.optInt("type");
			// int sorgid = specialUserObject.optInt("orgid");
			// Specialuser specialuser = new Specialuser();
			// specialuser.setOrgid(sorgid);
			// specialuser.setType(type);
			//
			// List<OrgModel> orgList = new ArrayList<OrgModel>();
			// JSONArray orgListArray = json.getJSONArray("orgList");
			// OrgModel orgModel = null;
			// for (int i = 0; i < orgListArray.length(); i++) {
			// orgModel = new OrgModel();
			// JSONObject eachObj = (JSONObject)orgListArray.get(i);
			// String logo = eachObj.optString("logo");
			// int orgid = eachObj.optInt("orgid");
			// String orgname = eachObj.optString("orgname");
			// int relationtype = eachObj.optInt("relationtype");
			// String sidlist = eachObj.optString("sidlist");
			//
			// orgModel.setOrgid(orgid);
			// orgModel.setOrgname(orgname);
			// orgModel.setLogo(logo);
			// orgModel.setRelationtype(relationtype);
			// orgModel.setSidlist(sidlist);
			// orgList.add(orgModel);
			// }
			//
			// myOrgModel = new MyOrgModel();
			// myOrgModel.setSpecialUser(specialuser);
			// myOrgModel.setOrgList(orgList);

			myOrgModel = new Gson().fromJson(dataJson, MyOrgModel.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return myOrgModel;

	}

	public void isVip(Activity ctx, String dataJson) {
		ArrayList<OrgModel> orgList = null;

		if (!TextUtils.isEmpty(dataJson)) {
			ArrayList<OrgModel> listModels = null;
			try {
				listModels = new Gson().fromJson(dataJson, new TypeToken<ArrayList<OrgModel>>() {
				}.getType());
				if (listModels != null && listModels.size() > 0) {
					// 设置是否是补习班成员
					MySharePerfenceUtil.getInstance().setOrgVip();
					IntentManager.goToStuPublishHomeWorkVipActivity(ctx, "", 0, listModels);
				} else {
					// 不是补习班成员
					MySharePerfenceUtil.getInstance().setNotOrgVip();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			MySharePerfenceUtil.getInstance().setNotOrgVip();
		}
	}

	public void isVipOrg(Activity ctx, String dataJson) {
		ArrayList<OrgModel> orgList = null;

		if (!TextUtils.isEmpty(dataJson)) {
			List<OrgModel> listModels = null;
			try {
				// listModels = new Gson().fromJson(dataJson, new
				// TypeToken<ArrayList<OrgModel>>() {
				// }.getType());

				listModels = parseJsonForMyOrg(dataJson).getOrgList();
				if (listModels != null && listModels.size() > 0) {
					// 设置是否是补习班成员
					MySharePerfenceUtil.getInstance().setOrgVip();
				} else {
					// 不是补习班成员
					MySharePerfenceUtil.getInstance().setNotOrgVip();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			MySharePerfenceUtil.getInstance().setNotOrgVip();
		}
	}

	public void goToOutsouringHomeWorkActivity(Activity context, String orgname, int orgid,
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
		openActivity(context, WaibaoHomeWorkActivity.class, data, isFinish);

	}

	public void openActivity(Activity activity, Class<? extends Activity> activityClazz, Bundle bundle,
			boolean isFinish) {
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

	/**
	 * 此方法描述的是：跳转到不是vip
	 * 
	 * @author: sky
	 * @最后修改日期:2015年8月8日 下午6:27:33 goNotHomeworkVipActivity
	 * @param activity
	 * @param activityClazz
	 * @param bundle
	 * @param isFinish
	 *            void
	 */
	public void goNotHomeworkVipActivity(Activity activity, Class<? extends Activity> activityClazz, Bundle bundle,
			boolean isFinish) {
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
	 * 此方法描述的是：解析特殊学生列表
	 * 
	 * @author: sky
	 * @最后修改日期:2015年8月8日 下午8:56:49 parseSpecialStudent
	 * @param dataJson
	 * @throws JSONException
	 *             void
	 */
	public List<UserInfoModel> parseSpecialStudent(String dataJson) throws JSONException {
		List<UserInfoModel> publiserList = new ArrayList<UserInfoModel>();
		JSONObject json = new JSONObject(dataJson);
		JSONArray homeworkArray = json.optJSONArray("homeworklist");
		JSONArray questionArray = json.optJSONArray("questionlist");

		if (homeworkArray != null && homeworkArray.length() > 0) {
			UserInfoModel userinfo = null;
			for (int i = 0; i < homeworkArray.length(); i++) {
				JSONObject obj = homeworkArray.getJSONObject(i);
				userinfo = new UserInfoModel();
				userinfo.setUserid(obj.optInt("userid"));
				userinfo.setName(obj.optString("name"));
				userinfo.setGrade(obj.optString("grade"));
				publiserList.add(userinfo);
			}
		}

		// if (questionArray != null && questionArray.length() > 0) {
		// UserInfoModel userinfo = null;
		// for (int i = 0; i < questionArray.length(); i++) {
		// JSONObject obj = questionArray.getJSONObject(i);
		// userinfo = new UserInfoModel();
		// userinfo.setUserid(obj.optInt("userid"));
		// userinfo.setName(obj.optString("name"));
		// userinfo.setGrade(obj.optString("grade"));
		// publiserList.add(userinfo);
		// }
		// }
		return publiserList;
	}

	/**
	 * 此方法描述的是：判断选择发题者的uid是否在作业权限中
	 * 
	 * @author: sky
	 * @param uid
	 * @param dataJson
	 * @return boolean
	 */
	public boolean getUseridInHomeworklist(int uid, String dataJson) {
		List<String> userids = new ArrayList<String>();
		try {
			JSONObject json = new JSONObject(dataJson);
			JSONArray homeworkArray = json.optJSONArray("homeworklist");
			UserInfoModel userinfo = null;
			if (homeworkArray != null && homeworkArray.length() > 0) {
				for (int i = 0; i < homeworkArray.length(); i++) {
					JSONObject obj = homeworkArray.getJSONObject(i);
					int userid = obj.optInt("userid");
					userids.add(userid + "");
				}
			}

			if (userids.contains(uid + "")) {// 是否在
				return true;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 此方法描述的是：判断是否是外包
	 * 
	 * @author: sky
	 * @param strArr
	 * @return boolean
	 */
	public boolean compareValueIsWaibao(ArrayList<OrgModel> listModels) {
		String[] strArr = new String[listModels.size()];
		for (int i = 0; i < listModels.size(); i++) {
			int orgtype = listModels.get(i).getOrgtype();
			strArr[i] = orgtype + "";
		}

		for (int i = 0; i < strArr.length; i++) {
			if (strArr[0].equals(strArr[i]) && "2".equals(strArr[0])) {
				return true;
			}
		}
		return false;
	}

	

}
