package com.daxiong.fun.api;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.MessageConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.dispatch.WelearnHandler;
import com.daxiong.fun.function.account.PersonHomePageActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.http.VolleyRequestQueue;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.HomeWorkRuleModel;
import com.daxiong.fun.model.LoginModel;
import com.daxiong.fun.model.PhoneLoginModel;
import com.daxiong.fun.model.QuestionRuleModel;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MySharePerfenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeLearnApi {
 
	private static final String TAG = "WeLearnApi";

	public static void sendPingMsg() {
		JSONObject json = new JSONObject();
		try {
			json.put("action", "ping");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyApplication.mNetworkUtil.sendTextmessage(json.toString());
	}

	/**
	 * 用户反馈
	 * 
	 * @param msg
	 *            反馈内容
	 * @param listener
	 *            提交回调
	 */
	public static void addFeedBack(Context context, String msg, HttpListener listener) {
		if (TextUtils.isEmpty(msg)) {
			return;
		}
		try {
			JSONObject data = new JSONObject();
			data.put("message", msg);
			OkHttpHelper.post(context, "system", "feedback", data, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getContactsList(Context context, HttpListener listener) {
		OkHttpHelper.post(context, "user", "getcontacts", null, listener);
	}

	/**
	 * 获取联系人信息
	 *
	 * @param userId
	 *            用户id
	 * @param roleId
	 *            角色id
	 * @param listener
	 *            回调
	 */
	public static void getContactInfo(Context context, int userId, HttpListener listener) {
		try {
			JSONObject data = new JSONObject();
			data.put("userid", userId);
			OkHttpHelper.post(context, "user", "getuserinfo", data, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getTeacherCommList(Context context, int userId, int pageIndex, int pageCount,
			HttpListener listener) {
		try {
			JSONObject data = new JSONObject();
			data.put("userid", userId);
			data.put("pageindex", pageIndex);
			data.put("pagecount", pageCount);
			OkHttpHelper.post(context, "user", "teachercontent", data, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除好友
	 * 
	 * @param userId
	 * @param listener
	 */
	public static void deleteFriend(Context context, int userId, HttpListener listener) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("contactid", userId);
			OkHttpHelper.post(context, "user", "removecontact", obj, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加好友
	 * 
	 * @param userId
	 * @param listener
	 */
	public static void addFriend(Context context, int userId, HttpListener listener) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("contactid", userId);
			OkHttpHelper.post(context, "user", "addcontact", obj, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void talkMsgReceivedVerity(String respText) {
		try {
			LogUtils.e("收到服务器的消息:", respText);
			JSONObject json = new JSONObject();
			json.put("sessionid", MySharePerfenceUtil.getInstance().getWelearnTokenId());
			json.put("type", JsonUtil.getInt(respText, "type", 0));
			json.put("subtype", GlobalContant.TALKMSGVERITY);
			json.put("timestamp", JsonUtil.getDouble(respText, "timestamp", 0.0));
			double svrtag = JsonUtil.getDouble(respText, "svrtag", 0.0);
			if (svrtag == 0) {
				return;
			}
			json.put("svrtag", svrtag);
			// LogUtils.e("我发的回包:", json.toString());
			MyApplication.mNetworkUtil.sendTextmessage(json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 新版本的发送消息
	 * 
	 * @param contentType
	 * @param receiveUser
	 * @param msgcontent
	 * @param recodrTime
	 * @param msgDef
	 * @param timestamp
	 */
	public static void sendMsg(int contentType, int receiveUser, String msgcontent, int recodrTime, int msgDef,
			double timestamp) {
		try {
			JSONObject json = new JSONObject();
			// String session = WeLearnSpUtil.getInstance().getSession();
			String session = MySharePerfenceUtil.getInstance().getWelearnTokenId();
			json.put("sessionid", session);
			json.put("platform", "android");
			// double timestamp = (double) new Date().getTime() / 1000;
			json.put("timestamp", timestamp);
			if (msgDef > 0) {
				MyApplication.time2CmdMap.put(timestamp, msgDef);
			}
			json.put("version", MyApplication.versionCode);
			json.put("touser", receiveUser);
			json.put("fromuser", MySharePerfenceUtil.getInstance().getUserId());
			json.put("type", 2);
			json.put("subtype", 1);
			json.put("fromroleid", MySharePerfenceUtil.getInstance().getUserRoleId());
			json.put("contenttype", contentType);
			json.put("msgcontent", msgcontent);
			if (recodrTime > 0) {
				json.put("audiotime", recodrTime);
			}

			LogUtils.i(TAG, "sendMsg send to " + json.toString());
			// LogUtils.e("发送的聊天消息:", json.toString());
			MyApplication.mNetworkUtil.sendTextmessage(json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 搜索联系人
	 * 
	 * @param context
	 *            上下文
	 * @param content
	 *            搜索关键字
	 * @param pageindex
	 *            当前页数
	 * @param pagecount
	 *            总页数
	 * @param listener
	 *            回调
	 */
	public static void searchFriend(Context context, String content, int pageindex, int pagecount,
			HttpListener listener) {
		if (TextUtils.isEmpty(content)) {
			return;
		}
		try {
			JSONObject data = new JSONObject();
			data.put("content", content);
			data.put("serachtype", 0);// 查找类型:0 所有, 1 姓名 2 学号
			data.put("usertype", 0);// 用户类型:0表示所有, 1 表示学生, 2 表示老师
			data.put("pageindex", pageindex);
			data.put("pagecount", pagecount);
			// HttpHelper.post(context, "user", "search", data, listener);
			BaseActivity activity = null;
			if (context instanceof BaseActivity) {
				activity = (BaseActivity) context;
			}
			OkHttpHelper.post(context, "user", "search",  data, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void checkUpdate() {
		VolleyRequestQueue.getQueue()
				.add(new JsonObjectRequest(Method.GET, AppConfig.UPDATEURL, null, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						if (response != null) {
							handleMessage(MessageConstant.MSG_DEF_OBTAIN_UPDATE_SUCCESS, response);
						}
					}
				}, null));
	}

	/**
	 * 通过handler发送消息.
	 * 
	 * @param msg_what
	 * @param obj
	 */
	private static void handleMessage(int msg_what, JSONObject obj) {
		Message message = new Message();
		message.what = msg_what;
		message.obj = obj;
		WelearnHandler.getInstance().getHandler().sendMessage(message);
	}

	/**
	 * 从服务器获取用户信息
	 * 
	 * @param listener
	 */
	public static void getUserInfoFromServer(Context context, final HttpListener listener) {
		OkHttpHelper.post(context, "user", "getuserinfo", null, new HttpListener() {
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				try {
					UserInfoModel uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);
					JSONArray dreamuniv2 = JsonUtil.getJSONArray(dataJson, "dreamuniv2", null);
					if (dreamuniv2 != null) {
						for (int i = 0; i < dreamuniv2.length(); i++) {
							JSONObject univ = dreamuniv2.getJSONObject(i);
							if (univ != null) {
								String name = JsonUtil.getString(univ, "name", "");
								switch (i) {
								case 0:
									MySharePerfenceUtil.getInstance().setGreamSchool1(name);
									break;
								case 1:
									MySharePerfenceUtil.getInstance().setGreamSchool2(name);
									break;
								case 2:
									MySharePerfenceUtil.getInstance().setGreamSchool3(name);
									break;
								}
							}
						}
					}

					DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
					if (null != listener) {
						listener.onSuccess(code, dataJson, errMsg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFail(int HttpCode,String errMsg) {
				if (null != listener) {
					listener.onFail(HttpCode,errMsg);
				}
			}
		});
	}

	/**
	 * 
	 * 此方法描述的是：修改用户资料
	 * 
	 * @author: qhw @最后修改人： qhw
	 * @最后修改日期:2015-7-22 下午4:33:52
	 * @version: 2.0
	 *
	 *           updateUserInfoFromServer
	 * @param context
	 * @param path
	 * @param name
	 * @param sex
	 * @param gradeid
	 * @param listener
	 *            void
	 */
	public static void updateUserInfoFromServer(BaseActivity context, String path, String name, int sex, int gradeid,
			String province, String city, String schools, OnUploadListener listener) {
		JSONObject data = new JSONObject();
		Map<String, List<File>> files = null;
		try {
			data.put("schools", schools);
			data.put("province", province);
			data.put("city", city);
			data.put("name", name);
			data.put("sex", sex);
			data.put("gradeid", gradeid);
			if (!TextUtils.isEmpty(path)) {// 更新头像
				files = new HashMap<String, List<File>>();
				List<File> fList = new ArrayList<File>();
				fList.add(new File(path));
				files.put("picfile", fList);
				data.put("avatar", "picfile");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		UploadManager.upload(PersonHomePageActivity.UPLOAD_URL, RequestParamUtils.getParam(data), files, listener, true,
				0);

	}

	/**
	 * 更新用户信息
	 * 
	 * @param listener
	 */
	public static void updateUserInfoFromServer(Context context, JSONObject data, final HttpListener listener) {
		OkHttpHelper.post(context, "user", "update", data, listener);
	}

	public static void getRuleInfo(Context context, final HttpListener listener) {
		OkHttpHelper.post(context, "system", "gettaskrule", null, new HttpListener() {

			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				if (null != dataJson) {
					// LogUtils.d(TAG, dataJson);
					try {
						Gson gson = new Gson();

						JSONObject jobj = new JSONObject(dataJson);
						JSONArray jarrayHomeWork = jobj.getJSONArray("homework_rule");
						JSONArray jarrayQuestion = jobj.getJSONArray("question_rule");
						if (null != jarrayHomeWork && jarrayHomeWork.length() > 0) {
							for (int i = 0; i < jarrayHomeWork.length(); i++) {
								JSONObject jInfo = jarrayHomeWork.getJSONObject(i);
								if (null == jInfo) {
									continue;
								}
								DBHelper.getInstance().getWeLearnDB().insertOrUpdateHomeWorkRuleInfo(
										gson.fromJson(jInfo.toString(), HomeWorkRuleModel.class));
							}
						}

						if (null != jarrayQuestion && jarrayQuestion.length() > 0) {
							for (int i = 0; i < jarrayQuestion.length(); i++) {
								JSONObject jInfo = jarrayQuestion.getJSONObject(i);
								if (null == jInfo) {
									continue;
								}
								DBHelper.getInstance().getWeLearnDB().insertOrUpdateQuestionRuleInfo(
										gson.fromJson(jInfo.toString(), QuestionRuleModel.class));
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				if (null != listener) {
					listener.onSuccess(code, dataJson, errMsg);
				}

			}

			@Override
			public void onFail(int HttpCode,String errMsg) {
				if (null != listener) {
					listener.onFail(HttpCode,errMsg);
				}
			}
		});
	}

	public static void relogin(Context context, HttpListener listener) {
		try {
			String func = "relogin";
			JSONObject obj = null;
			int type = MySharePerfenceUtil.getInstance().getGoLoginType();
			if (type == MySharePerfenceUtil.LOGIN_TYPE_PHONE) {
				LoginModel model = MySharePerfenceUtil.getInstance().getPhoneLoginInfo();
				if (null == model) {
					if (null != context) {
						IntentManager.goToGuideActivity(context);
					}
					return;
				}
				func = "telrelogin";
				obj = new JSONObject(new Gson().toJson((PhoneLoginModel) model));
			} else if (type == MySharePerfenceUtil.LOGIN_TYPE_QQ) {
				String openId = MySharePerfenceUtil.getInstance().getQQLoginInfo();
				if (TextUtils.isEmpty(openId)) {
					if (null != context) {
						IntentManager.goToGuideActivity(context);
					}
					return;
				}
				func = "relogin";
				obj = new JSONObject();
				obj.put("openid", openId);
			}else {
				LoginModel model = MySharePerfenceUtil.getInstance().getPhoneLoginInfo();
				if (null == model) {
					if (null != context) {
						IntentManager.goToGuideActivity(context);

					}
					return;
				}
				func = "telrelogin";
				obj = new JSONObject(new Gson().toJson((PhoneLoginModel) model));			
				
			}
			OkHttpHelper.post(context, "user", func, obj, listener);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
