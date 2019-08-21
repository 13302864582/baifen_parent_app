package com.daxiong.fun.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.model.PhoneLoginModel;
import com.daxiong.fun.model.UserInfoModel;

/**
 * SharedPreferences 工具类
 * 
 * @author: sky
 */
public class MySharePerfenceUtil {

	public static final String TAG = MySharePerfenceUtil.class.getSimpleName();

	private SharedPreferences mSharePreferences;

	private static final String SP_NAME = "welearn_sp";

	private static final String SP_KEY_SHOW_FIRST_TIPS = "sp_key_show_first_tips";

	public static final String WELEARN_DEFAULT_TOKEN_ID = "fudaotuan";

	public static final String QQ_LOGIN_INFO_KEY = "qq_login_info_key";
	public static final String PHONE_LOGIN_INFO_KEY = "phone_login_info_key";

	public static final String LOGIN_TYPE_KEY = "login_type_key";
	public static final int LOGIN_TYPE_QQ = 1;
	public static final int LOGIN_TYPE_PHONE = 2;

	public static final String FIST_REGISTER = "isFirstRegister";// 是否是第一次注册

	private MySharePerfenceUtil() {
		mSharePreferences = MyApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}

	private static class WeLearnSpUtilHolder {
		private static final MySharePerfenceUtil INSANCE = new MySharePerfenceUtil();
	}

	public static MySharePerfenceUtil getInstance() {
		return WeLearnSpUtilHolder.INSANCE;
	}

	public void setGradeGroupId(int gradeGroupId) {
		mSharePreferences.edit().putInt("gradeGroupId", gradeGroupId).commit();
	}

	public void setSubjectGroupId(int subjectGroupId) {
		mSharePreferences.edit().putInt("subjectGroupId", subjectGroupId).commit();
	}

	public void setChapterGroupId(int chapterGroupId) {
		mSharePreferences.edit().putInt("chapterGroupId", chapterGroupId).commit();
	}

	public void setKnowPointGroupId(int KnowPointGroupId) {
		mSharePreferences.edit().putInt("KnowPointGroupId", KnowPointGroupId).commit();
	}

	public int getKnowPointGroupId() {
		return mSharePreferences.getInt("KnowPointGroupId", 0);
	}

	public int getChapterGroupId() {
		return mSharePreferences.getInt("chapterGroupId", 0);
	}

	public int getSubjectGroupId() {
		return mSharePreferences.getInt("subjectGroupId", 0);
	}

	public int getGradeGroupId() {
		return mSharePreferences.getInt("gradeGroupId", -1);
	}

	public void setGoodSubject(String session) {
		if (!TextUtils.isEmpty(session)) {
			mSharePreferences.edit().putString(GlobalContant.SP_EDITOR_KEY_GOOD_SUBJECT, session).commit();
		}
	}

	public String getGoodSubject() {
		return mSharePreferences.getString(GlobalContant.SP_EDITOR_KEY_GOOD_SUBJECT, "");
	}

	public int getUserId() {
		UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if (null == uInfo) {
			return 0;
		} else {
			return uInfo.getUserid();
		}
	}

	public void setUserRoleId(int roleId) {
		mSharePreferences.edit().putInt(GlobalContant.SP_EDITOR_ROLE_ID, roleId).commit();
	}

	public int getUserRoleId() {
		return mSharePreferences.getInt(GlobalContant.SP_EDITOR_ROLE_ID, 0);
	}

	public void setUserId(int userid) {
		mSharePreferences.edit().putInt(GlobalContant.SP_EDITOR_USER_ID, userid).commit();
	}

	public int getspUserId() {
		return mSharePreferences.getInt(GlobalContant.SP_EDITOR_USER_ID, 0);
	}

	public void setInviteNum(int num) {
		mSharePreferences.edit().putInt("invitenum", num).commit();
	}

	public int getInviteNum() {
		return mSharePreferences.getInt("invitenum", 0);
	}

	public void setGrades(String grade) {
		if (!TextUtils.isEmpty(grade)) {
			mSharePreferences.edit().putString(GlobalContant.SP_EDITOR_GRADE, grade).commit();
		}
	}

	public String getGrades() {
		return mSharePreferences.getString(GlobalContant.SP_EDITOR_GRADE, "");
	}

	public void setSubject(String subject) {
		mSharePreferences.edit().putString(GlobalContant.SP_EDITOR_SUBJECT, subject).commit();
	}

	public String getSubject() {
		return mSharePreferences.getString(GlobalContant.SP_EDITOR_SUBJECT, "");
	}

	public void setNick(String nick) {
		if (!TextUtils.isEmpty(nick)) {
			mSharePreferences.edit().putString(GlobalContant.SP_EDITOR_NICK, nick).commit();
		}
	}

	public String getNick() {
		return mSharePreferences.getString(GlobalContant.SP_EDITOR_NICK, "");
	}

	public void setTokenId(String tokenid) {
		if (!TextUtils.isEmpty(tokenid)) {
			mSharePreferences.edit().putString(GlobalContant.SP_EDITOR_TOKEN, tokenid).commit();
		}
	}

	public String getTokenId() {
		return mSharePreferences.getString(GlobalContant.SP_EDITOR_TOKEN, "");
	}

	public void setGold(float goldVal) {
		mSharePreferences.edit().putFloat(GlobalContant.SP_EDITOR_GOLD, goldVal).commit();
	}

	public float getGold() {
		return mSharePreferences.getFloat(GlobalContant.SP_EDITOR_GOLD, 0.0f);
	}

	public void setRecordGold(float recordGold) {
		mSharePreferences.edit().putFloat("recordGold", recordGold).commit();
	}

	public float getRecordGold() {
		return mSharePreferences.getFloat("recordGold", 0.0f);
	}

	public void setAuthUrl(String url) {
		if (!TextUtils.isEmpty(url)) {
			mSharePreferences.edit().putString(GlobalContant.SP_EDITOR_URL, url).commit();
		}
	}

	public String getAuthUrl() {
		return mSharePreferences.getString(GlobalContant.SP_EDITOR_URL, "");
	}

	public void setFirstFlag() {
		mSharePreferences.edit().putBoolean(GlobalContant.SP_EDITOR_FIRST, false).commit();
	}

	public boolean getFirstFlag() {
		return mSharePreferences.getBoolean(GlobalContant.SP_EDITOR_FIRST, true);
	}

	public void setMsgNotifyFlag(boolean flag) {
		mSharePreferences.edit().putBoolean(GlobalContant.SP_EDITOR_NOTIFY_FLAG, flag).commit();
	}

	public boolean getMsgNotifyFlag() {
		return mSharePreferences.getBoolean(GlobalContant.SP_EDITOR_NOTIFY_FLAG, true);
	}

	public void setMsgNotifyVibrate(boolean vibrate) {
		mSharePreferences.edit().putBoolean(GlobalContant.SP_EDITOR_NOTIFY_VIBRATE, vibrate).commit();
	}

	public boolean getMsgNotifyVibrate() {
		return mSharePreferences.getBoolean(GlobalContant.SP_EDITOR_NOTIFY_VIBRATE, true);
	}

	public void setDayNotDis(boolean dayDoNotDis) {
		mSharePreferences.edit().putBoolean("dayDoNotDis", dayDoNotDis).commit();
	}

	public boolean getDayNotDis() {
		return mSharePreferences.getBoolean("dayDoNotDis", false);
	}

	public void setNightNotDis(boolean nightDoNotDis) {
		mSharePreferences.edit().putBoolean("nightDoNotDis", nightDoNotDis).commit();
	}

	public boolean getNightNotDis() {
		// return mSharePreferences.getBoolean("nightDoNotDis", false);
		return mSharePreferences.getBoolean("nightDoNotDis", true);
	}

	public void setContactInfo(String contactInfo) {
		if (!TextUtils.isEmpty(contactInfo)) {
			mSharePreferences.edit().putString("contactInfo", contactInfo).commit();
		}
	}

	public String getContactInfo() {
		return mSharePreferences.getString("contactInfo", "");
	}

	public void setContactList(String contactList) {
		if (!TextUtils.isEmpty(contactList)) {
			mSharePreferences.edit().putString("contactList", contactList).commit();
		}
	}

	public String getContactList() {
		return mSharePreferences.getString("contactList", "");
	}

	public void setIsChoicGream(boolean isChoicGream) {
		mSharePreferences.edit().putBoolean("isChoicGream", isChoicGream).commit();
	}

	public boolean isChoicGream() {
		return mSharePreferences.getBoolean("isChoicGream", false);

	}

	public void setIsDownUnivList(boolean isDownUnivList) {
		mSharePreferences.edit().putBoolean("isDownUnivList", isDownUnivList).commit();
	}

	public boolean isDownUnivList() {
		return mSharePreferences.getBoolean("isDownUnivList", false);
	}

	public void setGreamSchool1(String greamSchool1) {
		if (!TextUtils.isEmpty(greamSchool1)) {
			mSharePreferences.edit().putString("greamSchool1", greamSchool1).commit();
		}
	}

	public String getGreamSchool1() {
		return mSharePreferences.getString("greamSchool1", "");
	}

	public void setGreamSchool2(String greamSchool2) {
		if (!TextUtils.isEmpty(greamSchool2)) {
			mSharePreferences.edit().putString("greamSchool2", greamSchool2).commit();
		}
	}

	public String getGreamSchool2() {
		return mSharePreferences.getString("greamSchool2", "");
	}

	public void setGreamSchool3(String greamSchool3) {
		if (!TextUtils.isEmpty(greamSchool3)) {
			mSharePreferences.edit().putString("greamSchool3", greamSchool3).commit();
		}
	}

	public String getGreamSchool3() {
		return mSharePreferences.getString("greamSchool3", "");
	}

	public void setGreamSchoolID1(int schoolId) {
		mSharePreferences.edit().putInt("greamSchoolID1", schoolId).commit();
	}

	public int getGreamSchoolID1() {
		return mSharePreferences.getInt("greamSchoolID1", 0);
	}

	public void setGreamSchoolID2(int schoolId) {
		mSharePreferences.edit().putInt("greamSchoolID2", schoolId).commit();
	}

	public int getGreamSchoolID2() {
		return mSharePreferences.getInt("greamSchoolID2", 0);
	}

	public void setGreamSchoolID3(int schoolId) {
		mSharePreferences.edit().putInt("greamSchoolID3", schoolId).commit();
	}

	public int getGreamSchoolID3() {
		return mSharePreferences.getInt("greamSchoolID3", 0);
	}

	public void setUpDateUnivListTime() {
		mSharePreferences.edit().putLong("upDateUnivListTime", System.currentTimeMillis()).commit();
	}

	public long getUpDateUnivListTime() {
		return mSharePreferences.getLong("upDateUnivListTime", 0);
	}

	public void setUpDatePayAskGoldTime() {
		mSharePreferences.edit().putLong("upDatePayAskGoldTime", System.currentTimeMillis()).commit();
	}

	public long getUpDatePayAskGoldTime() {
		return mSharePreferences.getLong("upDatePayAskGoldTime", 0);
	}

	// public void setGradeId(int gradeId) {
	// mEditor = mSp.edit();
	// mEditor.putInt("gradeId", gradeId);
	// mEditor.apply();
	// }

	// public int getGradeId() {
	// return mSp.getInt("gradeId", 0);
	// }

	/*
	 * public void setOpenId(String openid) { mEditor = mSp.edit();
	 * mEditor.putString("openid", openid); mEditor.apply(); }
	 * 
	 * public String getOpenId() { return mSp.getString("openid", ""); }
	 */

	public void setQACount(String qacount) {
		mSharePreferences.edit().putString("qacount", qacount).commit();
	}

	public String getQACount() {
		return mSharePreferences.getString("qacount", "0");
	}

	public void setAccessToken(String access_token) {
		mSharePreferences.edit().putString("access_token", access_token).commit();
	}

	public String getAccessToken() {
		return mSharePreferences.getString("openid", "");
	}

	public void setIsNewUser(boolean isNewUser) {
		mSharePreferences.edit().putBoolean("isNewUser", isNewUser).commit();
	}

	public boolean IsNewUser() {
		return mSharePreferences.getBoolean("isNewUser", true);
	}

	public void setLoginType(String login_type) {
		mSharePreferences.edit().putString("login_type", login_type).commit();
	}

	public String getLoginType() {
		return mSharePreferences.getString("login_type", "");
	}

	public void setPhoneNum(String phone_num) {
		mSharePreferences.edit().putString("phone_num", phone_num).commit();
	}

	public String getPhoneNum() {
		return mSharePreferences.getString("phone_num", "");
	}

	public void setLatestVersion(int latest_version) {
		LogUtils.d(TAG, "set lastest version = " + latest_version);
		mSharePreferences.edit().putInt("latest_version", latest_version).commit();
	}

	public int getLatestVersion() {
		return mSharePreferences.getInt("latest_version", 0);
	}

	public void setUpdateTitle(String UpdateTitle) {
		mSharePreferences.edit().putString("UpdateTitle", UpdateTitle).commit();
	}

	public String getUpdateTitle() {
		return mSharePreferences.getString("UpdateTitle", "升级提示");
	}

	public void setUpdateContent(String UpdateContent) {
		mSharePreferences.edit().putString("UpdateContent", UpdateContent).commit();
	}

	public String getUpdateContent() {
		return mSharePreferences.getString("UpdateContent", "亲！有您最新的安装包哦，速度升级呀~");
	}

	public void setUpdateUrl(String UpdateUrl) {
		mSharePreferences.edit().putString("UpdateUrl", UpdateUrl).commit();
	}

	public String getUpdateUrl() {
		return mSharePreferences.getString("UpdateUrl", "");
	}

	public void setDescription(String description) {
		if (TextUtils.isEmpty(description)) {
			mSharePreferences.edit().putString("description", "").commit();
		} else {
			mSharePreferences.edit().putString("description", description).commit();
		}
	}

	public String getDescription() {
		return mSharePreferences.getString("description", "");
	}

	public void setIsTodaySignIn(int today_signed) {
		mSharePreferences.edit().putInt("today_signed", today_signed).commit();
	}

	public int isTodaySignIn() {// 0是未签
		return mSharePreferences.getInt("today_signed", 1);
	}

	public boolean isShowFirstUseTip() {
		return mSharePreferences.getBoolean(SP_KEY_SHOW_FIRST_TIPS, true);
	}

	public void setFirstUseFalse() {
		mSharePreferences.edit().putBoolean(SP_KEY_SHOW_FIRST_TIPS, false).commit();
	}

	public boolean isShowFirstSingleTips() {
		return mSharePreferences.getBoolean("isShowFirstSingleTips", true);
	}

	public void setFirstSingleFalse() {
		mSharePreferences.edit().putBoolean("isShowFirstSingleTips", false).commit();
	}

	public boolean isShowLoginGuide() {
		return mSharePreferences.getBoolean("isShowLoginGuide", true);
	}

	public void setShowLoginGuideFalse() {
		mSharePreferences.edit().putBoolean("isShowLoginGuide", false).commit();
	}

	public boolean isShowAskGuide() {
		return mSharePreferences.getBoolean("isShowAskGuide", true);
	}

	public void setShowAskGuideFalse() {
		mSharePreferences.edit().putBoolean("isShowAskGuide", false).commit();
	}

	public boolean isShowHomeworkGuide() {
		return mSharePreferences.getBoolean("isShowHomeworkGuide", true);
	}

	public void setShowHomeworkGuideFalse() {
		mSharePreferences.edit().putBoolean("isShowHomeworkGuide", false).commit();
	}

	public String getWelearnTokenId() {
		String tid = mSharePreferences.getString("welearn_token_id", WELEARN_DEFAULT_TOKEN_ID);
		if (TextUtils.isEmpty(tid)) {
			return WELEARN_DEFAULT_TOKEN_ID;
		}
		return tid;
	}

	public void setWelearnTokenId(String sessionId) {
		if (TextUtils.isEmpty(sessionId)) {
			sessionId = WELEARN_DEFAULT_TOKEN_ID;
		}
		mSharePreferences.edit().putString("welearn_token_id", sessionId).commit();
	}

	public boolean isOrgVip() {
		return mSharePreferences.getBoolean("isOrgVip", false);
	}

	public void setOrgVip() {
		mSharePreferences.edit().putBoolean("isOrgVip", true).commit();
	}

	public void setNotOrgVip() {
		mSharePreferences.edit().putBoolean("isOrgVip", false).commit();
	}

	public long getWelcomeImageTime() {
		return mSharePreferences.getLong("welcome_time", 0L);
	}

	public void setWelcomeImageTime(long time) {
		mSharePreferences.edit().putLong("welcome_time", time).commit();
	}

	public void setWelcomeImageUrl(String welcome_url) {
		mSharePreferences.edit().putString("welcome_url", welcome_url).commit();
	}

	public String getWelcomeImageUrl() {
		return mSharePreferences.getString("welcome_url", "");
	}

	public void setPhotoPath(String PhotoPath) {
		mSharePreferences.edit().putString("PhotoPath", PhotoPath).commit();
	}

	public String getPhotoPath() {
		return mSharePreferences.getString("PhotoPath", "");
	}

	public void savePhoneLoginInfo(PhoneLoginModel model) {
		if (null != model) {			
//			mSharePreferences.edit().putString(PHONE_LOGIN_INFO_KEY, model.toString()).commit();
			mSharePreferences.edit().putString(PHONE_LOGIN_INFO_KEY, JSON.toJSONString(model)).commit();
			setGoLoginType(LOGIN_TYPE_PHONE);
		}
	}

	public PhoneLoginModel getPhoneLoginInfo() {
		String phoneLoginString = mSharePreferences.getString(PHONE_LOGIN_INFO_KEY, null);
		if (!TextUtils.isEmpty(phoneLoginString)) {
			try {
				PhoneLoginModel model = JSON.parseObject(phoneLoginString, PhoneLoginModel.class);
				return model;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void saveQQLoginInfo(String openId) {
		if (null != openId) {
			mSharePreferences.edit().putString(QQ_LOGIN_INFO_KEY, openId).commit();
			setGoLoginType(LOGIN_TYPE_QQ);
		}
	}

	public String getQQLoginInfo() {
		return mSharePreferences.getString(QQ_LOGIN_INFO_KEY, null);
	}

	public void setGoLoginType(int type) {
		mSharePreferences.edit().putInt(LOGIN_TYPE_KEY, type).commit();
	}

	public int getGoLoginType() {
		return mSharePreferences.getInt(LOGIN_TYPE_KEY, -1);
	}

	public void setWelcomeDialog(int welcomeTag) {
		mSharePreferences.edit().putInt(FIST_REGISTER, welcomeTag).commit();

	}

	public int getWelcomeDialog() {
		return mSharePreferences.getInt(FIST_REGISTER, -1);
	}

	/**
	 * 此方法描述的是：保存注册时候的选择的角色
	 * 
	 * @author: Sky @最后修改人： Sky
	 * @最后修改日期:2016年6月15日 下午4:26:59 setRoleTxt
	 * @param roletxt
	 *            void
	 */
	public void setRoleTxt(String roletxt) {
		mSharePreferences.edit().putString("roletxt", roletxt).commit();
	}

	public String getRoleTxt() {
		return mSharePreferences.getString("roletxt", "");
	}






}
