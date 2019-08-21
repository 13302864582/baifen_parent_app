
package com.daxiong.fun.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseIntentManager;
import com.daxiong.fun.common.AuthActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.account.CropPhotoActivity;
import com.daxiong.fun.function.account.GradeChooseActivity;
import com.daxiong.fun.function.account.GuideActivity;
import com.daxiong.fun.function.account.PersonHomePageActivity;
import com.daxiong.fun.function.account.PhoneLoginActivity;
import com.daxiong.fun.function.account.RegisterActivity;
import com.daxiong.fun.function.account.StuModifiedInfoActivity;
import com.daxiong.fun.function.account.setting.AboutActivity;
import com.daxiong.fun.function.account.setting.DoNotDisturbActivity;
import com.daxiong.fun.function.account.setting.FeedbackActivity;
import com.daxiong.fun.function.account.setting.PreSendPicReViewActivity;
import com.daxiong.fun.function.account.setting.SystemSettingActivity;
import com.daxiong.fun.function.communicate.ChatMsgViewActivity;
import com.daxiong.fun.function.course.BuyCourseActivity;
import com.daxiong.fun.function.course.CharpterDetailActivity;
import com.daxiong.fun.function.course.CourseDetailsActivity;
import com.daxiong.fun.function.course.MastersCourseActivity;
import com.daxiong.fun.function.course.SearchCourseActivity;
import com.daxiong.fun.function.course.SingleStudentQAActivity;
import com.daxiong.fun.function.cram.ChoiceFudaoActivity;
import com.daxiong.fun.function.cram.CramSchoolDetailsActivity;
import com.daxiong.fun.function.cram.FamousTeacherListActivity;
import com.daxiong.fun.function.cram.GoodsNotesActivity;
import com.daxiong.fun.function.cram.MyCramSchoolActivity;
import com.daxiong.fun.function.goldnotless.FriendGoldActivity;
import com.daxiong.fun.function.goldnotless.GoldNotLessActivity;
import com.daxiong.fun.function.goldnotless.PhoneCardPayAcvitity;
import com.daxiong.fun.function.account.vip.SelectRechargeCardActivity;
import com.daxiong.fun.function.homework.HomeWorkHallActivity;
import com.daxiong.fun.function.homework.HomeWorkReadOnlyActivity;
import com.daxiong.fun.function.homework.LearningSituationAnalysisActivity;
import com.daxiong.fun.function.homework.PublishHomeWorkActivity;
import com.daxiong.fun.function.homework.PublishHomeWorkVipActivity;
import com.daxiong.fun.function.homework.SelectPicPopupWindow;
import com.daxiong.fun.function.homework.SingleAnalysisActivity;
import com.daxiong.fun.function.homework.StuPublishHomeworkGuideActivity;
import com.daxiong.fun.function.homework.teacher.TecHomeWorkSingleCheckActivity;
import com.daxiong.fun.function.partner.SearchSchoolActivity;
import com.daxiong.fun.function.partner.SingleEditTextActivity;
import com.daxiong.fun.function.partner.StudentAssessmentActivity;
import com.daxiong.fun.function.partner.StudentInfoActivityNew;
import com.daxiong.fun.function.partner.TeacherInfoActivityNew;
import com.daxiong.fun.function.question.ChoiceListActivity;
import com.daxiong.fun.function.question.MyQuestionActivity;
import com.daxiong.fun.function.question.PayAnswerAlbumActivity;
import com.daxiong.fun.function.question.PayAnswerAskActivity;
import com.daxiong.fun.function.question.PayAnswerAskVipActivity;
import com.daxiong.fun.function.question.PayAnswerImageGridActivity;
import com.daxiong.fun.function.question.PayAnswerPhotoViewActivity;
import com.daxiong.fun.function.question.PayAnswerQuestionDetailActivity;
import com.daxiong.fun.function.question.PayAnswerQuestionPhotoViewActivity;
import com.daxiong.fun.function.question.PayAnswerTextAnswerActivity;
import com.daxiong.fun.function.question.QAHallActivity;
import com.daxiong.fun.function.study.HomeWorkStudyAnalysisActivity;
import com.daxiong.fun.function.study.StuHomeWorkCheckDetailActivity;
import com.daxiong.fun.function.study.StuHomeWorkSingleCheckActivity;
import com.daxiong.fun.function.study.yeartopic.SearchKnowledgeActivity;
import com.daxiong.fun.function.study.yeartopic.YearQuestionActivity;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.model.OrgModel;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.service.PushService;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理所有的intent跳转
 * 
 * @author parsonswang
 */
public class IntentManager extends BaseIntentManager {

	public static void startWService(Context context) {
		Intent intent = new Intent(context, PushService.class);
		context.startService(intent);
	}

	public static void stopWService(Context context) {
		Intent i = new Intent(context, PushService.class);
		i.setAction(PushService.ACTION_EXIT_WEBSOCKET_SERVICE);
		context.startService(i);
	}

	public static void goToPreSendPicReViewActivity(Activity activity, Bundle data, boolean isFinish) {
		openActivity(activity, PreSendPicReViewActivity.class, data, isFinish);
	}

	public static void goToPhoneRegActivity(Activity activity, Bundle data, boolean isFinish) {
		openActivity(activity, RegisterActivity.class, data, isFinish);
	}

	/**
	 * 跳转到登录界面
	 * @param activity
	 * @param data
	 * @param isFinish
     */
	public static void goToPhoneLoginActivity(Activity activity, Bundle data, boolean isFinish) {
		openActivity(activity, PhoneLoginActivity.class, data, isFinish);
	}




	public static void goToPayAnswerAskView(Activity activity, Bundle data, boolean isFinish) {
		openActivity(activity, PayAnswerAskActivity.class, data, isFinish);
	}

	public static void goToQuestionPhotoView(Activity context, Bundle data) {
		openActivity(context, PayAnswerQuestionPhotoViewActivity.class, data, true);
	}

	public static void goToSearchCourse(Activity context) {
		openActivity(context, SearchCourseActivity.class, false);
	}

	// /**
	// * 跳转邀请页面
	// *
	// * @param context
	// */
	// public static void goToInvitateActivity(Activity context) {
	// openActivity(context, InvitateActivity.class, true);
	// }

	// public static void goToChoicGream(Activity context) {
	// openActivity(context, ChoicGreamActivity.class, true);
	// }
	//
	// public static void goToChoicGream(Activity context, Bundle data, boolean
	// isFinish) {
	// openActivity(context, ChoicGreamActivity.class, data, isFinish);
	// }

	public static void goToDoNotDisturbActivity(Activity context, Bundle data, boolean isFinish) {
		openActivity(context, DoNotDisturbActivity.class, data, isFinish);
	}

	public static void goToMainView(Activity context) {
		UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if (null != uInfo) {
			int gradeId = uInfo.getGradeid();
			String roleTxt=MySharePerfenceUtil.getInstance().getRoleTxt();
			if (gradeId > 0) {// 进入学生主页
//				if (!TextUtils.isEmpty(roleTxt)) {
//					openActivity(context, MainActivity.class, true);
//				}else{					
//					openActivity(context, RoleChooseActivity.class, true);
//				}
				openActivity(context, MainActivity.class, true);
			} else {
				// goToGradeChoice(context, true);
				goToGradeChoose(context, true);
			}
		}
	}

	public static void goToGuideActivity(Context context) {
		// GuideActivity诡异跳转，暂先这样处理
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) { // 如果当前应用在后台或当前界面是PhoneLoginActivity就不跳转登录界面
			ComponentName topActivity = tasksInfo.get(0).topActivity;

			if ("com.daxiong.fun".equals(topActivity.getPackageName())
					&& !"PhoneLoginActivity".equals(topActivity.getClassName())) {
				openActivity(context, GuideActivity.class, true);
			}
		}
	}




	public static void goToAlbumView(Activity context, Bundle data) {
		openActivity(context, PayAnswerAlbumActivity.class, data, true);
	}

	public static void goToImageGridView(Activity context, Bundle bundle) {
		openActivity(context, PayAnswerImageGridActivity.class, bundle, true);
	}

	public static void goToPhotoView(Activity context, Bundle data) {
		openActivity(context, PayAnswerPhotoViewActivity.class, data, true);
		// openActivity(context, PayAnswerPhotoCropperViewActivity.class, data,
		// true);//先去裁剪页面
	}

	/**
	 * 裁剪后进入打点界面
	 * 
	 * @param context
	 * @param data
	 */
	public static void goToPayAnswerPhotoView(Activity context, Bundle data) {
		openActivity(context, PayAnswerPhotoViewActivity.class, data, true);
	}

	public static void goToAnswertextView(Activity context, Bundle data) {
		openActivity(context, PayAnswerTextAnswerActivity.class, data, false);
	}

	public static void goToGradeChoose(Context context, boolean isFinish) {
		openActivity(context, GradeChooseActivity.class, isFinish);
	}

	public static void goToGradeChoice(Context context, boolean isFinish) {
		openActivity(context, GradeChooseActivity.class, isFinish);
	}

	public static void goToGradeChoice(Activity context, Bundle data, boolean isFinish) {
		openActivity(context, GradeChooseActivity.class, data, isFinish);
	}

	/**
	 * 此方法描述的是：学生在个人主页修改年级
	 * 
	 * @author: qhw @最后修改人： qhw
	 * @最后修改日期:2015-7-22 下午5:32:16
	 * @version: 2.0 goToGradeChoiceFromCenter
	 * @param context
	 *            void
	 */
	public static void goToGradeChoiceFromCenter(Activity context) {
		Bundle data = new Bundle();
		data.putBoolean("isFromCenter", true);
		openActivityForResult(context, GradeChooseActivity.class, data, false,
				StuModifiedInfoActivity.REQUEST_CODE_GRADE);
	}

	public static void goToGradeChooseFromCenter(Activity context, Bundle data) {

		openActivityForResult(context, GradeChooseActivity.class, data, false,
				StuModifiedInfoActivity.REQUEST_CODE_GRADE);
	}

	public static void goToQAHallActivity(Activity context) {
		openActivity(context, QAHallActivity.class, false);
	}

	public static void goToAnswersListActivity(Activity context) {
		openActivity(context, YearQuestionActivity.class, false);
	}

	public static void goToAnswersListActivity(Activity context, Bundle data) {
		openActivity(context, YearQuestionActivity.class, data, false);
	}

	public static void goToMyQpadActivity(Activity context, boolean isFinish) {
		openActivity(context, MyQuestionActivity.class, isFinish);
	}

	public static void goToMyQpadActivity(Activity context) {
		openActivity(context, MyQuestionActivity.class, false);
	}

	/**
	 * 跳转到我的问题
	 * 
	 * @author: sky
	 * @param context
	 * @param data
	 *            void
	 */
	public static void goToTargetQpadActivity(Activity context, Bundle data) {
		openActivity(context, MyQuestionActivity.class, data, false);
	}

	/**
	 * 跳转到我的作业检查
	 * 
	 * @author: sky
	 * @param context
	 * @param activityClazz
	 * @param data
	 *            void
	 */
	public static void goToMyHomeworkActivity(Activity context, Class<? extends Activity> activityClazz, Bundle data) {
		openActivity(context, activityClazz, data, false);
	}

	public static void goPayActivity(Activity context) {
		// openActivity(context, PayActivity.class, false);
		openActivity(context, SelectRechargeCardActivity.class, false);
	}

	// public static void goSignInActivity(Activity context) {
	// openActivity(context, SignInActivity.class, false);
	// }

	public static void gotoFriendGoldActivity(Activity context) {
		openActivity(context, FriendGoldActivity.class, false);
	}

	public static void goGoldNotLessActivity(Activity context) {
		openActivity(context, GoldNotLessActivity.class, false);
	}

	public static void goToPhoneCardPayActivity(Activity context, Bundle data) {
		openActivity(context, PhoneCardPayAcvitity.class, data, false);
	}

	public static void goToAnswerDetail(Activity context, Class<? extends Activity> activityClazz, Bundle data) {
		openActivity(context, activityClazz, data, false);
	}

	public static void goToAnswerDetail(Activity context, Class<? extends Activity> activityClazz, Bundle data,
			int requestCode) {
		openActivityForResult(context, activityClazz, data, false, requestCode);
	}

	public static void goToQuestionDetailPicView(Activity context, Bundle data) {
		openActivity(context, PayAnswerQuestionDetailActivity.class, data, false);
	}

	public static void gotoAuthView(Activity context, Bundle data) {
		openActivity(context, AuthActivity.class, data, false);
	}

	public static void goToChatListView(Activity context, Bundle data, boolean isFinish) {
		openActivity(context, ChatMsgViewActivity.class, data, isFinish);
	}

	public static void goToChatListView(Activity context) {
		openActivity(context, ChatMsgViewActivity.class, false);
	}

	// 调用myorg ，接口已经变了废弃
	public static void goToPayAnswerAskView(final Context activity, boolean isFinish) {

		if (MySharePerfenceUtil.getInstance().isOrgVip()) {
			JSONObject data = new JSONObject();
			try {
				data.put("type", 1);
				data.put("pageindex", 1);
				data.put("pagecount", 1000);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			OkHttpHelper.post(activity, "org", "myorgs", data, new HttpListener() {
				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (!TextUtils.isEmpty(dataJson)) {
						ArrayList<OrgModel> listModels = null;
						try {
							listModels = new Gson().fromJson(dataJson, new TypeToken<ArrayList<OrgModel>>() {
							}.getType());
						} catch (Exception e) {
						}
						if (listModels != null && listModels.size() > 0) {
							MySharePerfenceUtil.getInstance().setOrgVip();
							goToPayAnswerAskVipActivity((Activity) activity, "", 0, listModels);
						} else {
							MySharePerfenceUtil.getInstance().setNotOrgVip();
						}
					} else {
						MySharePerfenceUtil.getInstance().setNotOrgVip();
					}

				}

				@Override
				public void onFail(int HttpCode,String errMsg) {

				}
			});
		} else {
			openActivity(activity, PayAnswerAskActivity.class, isFinish);
		}

	}

	public static void goToTeacherInfoView(Activity context, Bundle data) {
		// openActivity(context, TeacherInfoActivity.class, data, false);
		openActivity(context, TeacherInfoActivityNew.class, data, false);
	}

	public static void goToStudentInfoView(Activity context, Bundle data) {
		// openActivity(context, StudentInfoActivity.class, data, false);
		openActivity(context, StudentInfoActivityNew.class, data, false);
	}

	public static void goToStudentCenterView(Activity context, Bundle data) {
		// openActivity(context, StudentCenterActivity.class, data, false);
		// openActivity(context, PersonHomePageActivity.class, data, false);
//		openActivity(context, MyInfoActivity.class, data, false);
		openActivity(context, StuModifiedInfoActivity.class, data, false);
	}

	public static void goToSystemSetting(Activity context) {
		openActivity(context, SystemSettingActivity.class, false);
	}

	public static void goToLearningSituationAnalysis(Activity context) {
		openActivity(context, LearningSituationAnalysisActivity.class, false);
	}

	public static void goToAbout(Activity context) {
		openActivity(context, AboutActivity.class, false);
	}

	public static void goToUserRequest(Activity context) {
		openActivity(context, FeedbackActivity.class, false);
	}

	/**
	 * @param isFlags
	 *            是否加标记
	 * @param flags
	 *            如果 isFlags = flase. 此参数无效
	 */
	public static void goToMastersCourseActivity(Activity context, boolean isFlags, int flags) {
		goToMastersCourseActivity(context, null, isFlags, flags);
	}

	public static void goToMastersCourseActivity(Activity context, Bundle data, boolean isFlags, int flags) {
		try {
			Intent intent = new Intent(context, MastersCourseActivity.class);
			if (data != null) {
				intent.putExtras(data);
			}
			if (isFlags) {
				intent.setFlags(flags);
			}
			context.startActivity(intent);
			context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void goToSearchSchoolActivity(Activity context, Bundle data, boolean isFinish) {
		openActivity(context, SearchSchoolActivity.class, data, isFinish);
	}

	public static void goToMicrocoach(Activity context) {
		openActivity(context, MastersCourseActivity.class, false);
	}

	public static void goToCourseDetailsActivity(Activity context, Bundle data) {
		openActivity(context, CourseDetailsActivity.class, data, false);
	}

	public static void goToCourseDetailsActivityForResult(Activity context, Bundle data, int requestCode) {
		openActivityForResult(context, CourseDetailsActivity.class, data, false, requestCode);
	}

	public static void goToBuyCourseActivity(Activity context, Bundle data) {
		openActivity(context, BuyCourseActivity.class, data, false);
	}

	public static void goToBuyCourseActivityForResult(Activity context, Bundle data, int requestCode) {
		openActivityForResult(context, BuyCourseActivity.class, data, false, requestCode);
	}

	/**
	 * 老师换题时点击图片进入大图浏览
	 * 
	 * @param context
	 * @param data
	 * @param isFinish
	 */
	public static void goToHomeWorkDetail_OnlyReadActivity(Activity context, Bundle data, boolean isFinish) {
		// openActivity(context, TecHomeWorkDetail_OnlyReadActivity.class, data,
		// isFinish);
		openActivity(context, HomeWorkReadOnlyActivity.class, data, isFinish);
	}

	/**
	 * 老师第一次进入单点检查
	 * 
	 * @param context
	 * @param data
	 * @param isFinish
	 * @param requestCode
	 */
	public static void goToTecSingleCheckActivity(Activity context, Bundle data, boolean isFinish, int requestCode) {
		openActivityForResult(context, TecHomeWorkSingleCheckActivity.class, data, isFinish, requestCode);
	}

	/**
	 * 老师再次进入单点检查
	 * 
	 * @param context
	 * @param data
	 * @param isFinish
	 */
	public static void goToTecSingleCheckActivity(Activity context, Bundle data, boolean isFinish) {
		openActivity(context, TecHomeWorkSingleCheckActivity.class, data, isFinish);
	}

	/**
	 * 学生进入单点检查
	 * 
	 * @param context
	 * @param data
	 * @param isFinish
	 */
	public static void goToStuSingleCheckActivity(Activity context, Bundle data, boolean isFinish) {
		openActivityForResult(context, StuHomeWorkSingleCheckActivity.class, data, isFinish, 10010);
	}

	public static void goToStuHomeWorkDetailActivity(Activity context, Bundle data, boolean isFinish) {
		openActivity(context, StuHomeWorkCheckDetailActivity.class, data, isFinish);
	}

	/*
	 * public static void goToSearchSchoolActivity(Activity context) {
	 * openActivity(context, SearchSchoolActivity.class, false); }
	 */

	public static void goToSingleEditTextView(Activity context, Bundle data) {
		openActivityForResult(context, SingleEditTextActivity.class, data, false);
	}

	// public static void goToProvinceView(Activity context, Bundle data) {
	// openActivityForResult(context, ProvinceActivity.class, data, false);
	// }

	// public static void goToCityView(Activity context, Bundle data) {
	// openActivityForResult(context, CityActivity.class, data, false);
	// }

	public static void gotoPersonalPage(Activity context, int userid, int roleid) {
		Bundle data = new Bundle();
		data.putInt("userid", userid);
		data.putInt("roleid", roleid);
		if (roleid == GlobalContant.ROLE_ID_STUDENT || roleid == GlobalContant.ROLE_ID_PARENTS) {
			if (userid == MySharePerfenceUtil.getInstance().getUserId()) {
				IntentManager.goToStudentCenterView(context, data);
				// StatService.onEvent(context, "studentCenterView", "");
				MobclickAgent.onEvent(context, "studentCenterView");
			} else {
				// StatService.onEvent(context, "studentInfoView", "");
				MobclickAgent.onEvent(context, "studentInfoView");
				IntentManager.goToStudentInfoView(context, data);
			}
		} else if (roleid == GlobalContant.ROLE_ID_COLLEAGE || roleid == GlobalContant.ROLE_ID_CLASSTEACHER) {
			if (userid == MySharePerfenceUtil.getInstance().getUserId()) {
			} else {
				// StatService.onEvent(context, "teacherInfoView", "");
				MobclickAgent.onEvent(context, "teacherInfoView");
				IntentManager.goToTeacherInfoView(context, data);
			}
		}
	}

	/**
	 * 跳转学生作业大厅
	 * 
	 * @param context
	 */
	public static void goToStuHomeWorkHallActivity(Activity context) {
		openActivity(context, HomeWorkHallActivity.class, false);
	}

	/**
	 * 跳转学生发布作业引导页面
	 * 
	 * @param context
	 */
	public static void goToStuPublishHomeWorkGuideActivity(Activity context) {
		openActivity(context, StuPublishHomeworkGuideActivity.class, false);
	}

	/**
	 * 跳转学生发布作业页面
	 * 
	 * @param context
	 */
	public static void goToStuPublishHomeWorkActivity(final Activity context) {
		if (MySharePerfenceUtil.getInstance().isOrgVip()) {
			JSONObject data = new JSONObject();
			try {
				data.put("type", 1);
				data.put("pageindex", 1);
				data.put("pagecount", 1000);

				OkHttpHelper.post(context, "org", "myorgs", data, new HttpListener() {

					@Override
					public void onSuccess(int code, String dataJson, String errMsg) {
						if (!TextUtils.isEmpty(dataJson)) {
							ArrayList<OrgModel> listModels = null;
							try {
								listModels = new Gson().fromJson(dataJson, new TypeToken<ArrayList<OrgModel>>() {
								}.getType());
								if (listModels != null && listModels.size() > 0) {
									// 设置是否是补习班成员
									MySharePerfenceUtil.getInstance().setOrgVip();
									goToStuPublishHomeWorkVipActivity(context, "", 0, listModels);
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

					@Override
					public void onFail(int HttpCode,String errMsg) {

					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {
			openActivityForResult(context, PublishHomeWorkActivity.class, null, false, 1002);
		}
	}

	// // 获取特殊学生列表
	// public void executeGetSpecialStudentList(final Activity context, String
	// orgid) {
	// try {
	// JSONObject json = new JSONObject();
	// json.put("orgid", orgid);
	// HttpHelper.postOrg(context, "specialstudentslist", new SuccessListener()
	// {
	// @Override
	// public void onAfter(String dataJson) {
	//
	//
	// }
	// }, json, (SingleFragmentActivity)context);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// }

	/**
	 * 新过滤
	 * 
	 * @param context
	 * @param data
	 */
	public static void goToTargetFilterActivity(Activity context) {
		openActivityForResult(context, SearchKnowledgeActivity.class, null, false, 1002);
	}

	/**
	 * 跳转到选择过滤条件
	 * 
	 * @param context
	 */
	public static void goToChoiceListActivity(Activity context, Bundle data) {
		openActivityForResult(context, ChoiceListActivity.class, data, false, 1002);
	}

	/**
	 * 进入学情分析
	 * 
	 * @param context
	 * @param data
	 * @param isFinish
	 */
	public static void goToStudyAnalysisActivity(Activity context, Bundle data, boolean isFinish) {
		openActivity(context, HomeWorkStudyAnalysisActivity.class, data, false);
	}

	/**
	 * 单个作业分析
	 * 
	 * @author: sky
	 * @param context
	 * @param data
	 * @param isFinish
	 *            void
	 */
	public static void goToSingleAnalysisActivity(Activity context, Bundle data, boolean isFinish) {
		openActivity(context, SingleAnalysisActivity.class, data, false);
	}

	/**
	 * 进入课时详情
	 * 
	 * @param context
	 * @param charpterid
	 *            课时id
	 * @param charptername
	 *            课时名称
	 * @param tecuserid
	 *            老师ID
	 * @param tecavatar
	 *            老师头像
	 * @param tecname
	 *            老师名字
	 * @param isBuyed
	 *            是否已购买,该参数决定是否显示追问按钮
	 */
	public static void goToCharpterDetailActivity(Activity context, int charpterid, String charptername, int tecuserid,
			String tecavatar, String tecname, boolean isBuyed) {
		Bundle data = new Bundle();
		data.putInt("charpterid", charpterid);
		data.putString("charptername", charptername);
		data.putInt("userid", tecuserid);
		data.putString("avatar", tecavatar);
		data.putString("name", tecname);
		data.putBoolean("isBuyed", isBuyed);
		openActivity(context, CharpterDetailActivity.class, data, false);
	}

	/**
	 * 进入学生追问
	 * 
	 * @param context
	 * @param pageid
	 * @param imgpath
	 * @param charptername
	 * @param pointlist
	 * @param userid
	 * @param avatar
	 * @param name
	 */
	public static void goToSingleStudentQAActivity(Activity context, int pageid, String imgpath, String charptername,
			String pointlist, int userid, String avatar, String name) {
		Bundle data = new Bundle();
		data.putInt("pageid", pageid);
		data.putString("imgpath", imgpath);
		data.putString("charptername", charptername);
		data.putString("pointlist", pointlist);
		data.putInt("userid", userid);
		data.putString("avatar", avatar);
		data.putString("name", name);
		openActivityForResult(context, SingleStudentQAActivity.class, data, false, 1102);
	}

	public static void goToSelectPicPopupWindow(Activity context) {
		openActivityForResult(context, SelectPicPopupWindow.class, null, false,
				RequestConstant.REQUEST_CODE_GET_IMAGE_FROM_SYS);
		
//		openActivityForResult(context, TakePhoteActivity.class, null, false,
//				RequestConstant.REQUEST_CODE_GET_IMAGE_FROM_SYS);
//		openActivityForResult(context, CameraActivity.class, null, false,
//				Constants.REQUEST_CODE_GET_IMAGE_FROM_SYS);
	}

	/**
	 * 选传参数
	 * 
	 * @param context
	 * @param orgname
	 * @param orgid
	 * @param listModels
	 */
	public static void goToPayAnswerAskVipActivity(Activity context, String orgname, int orgid,
			ArrayList<OrgModel> listModels) {
		Bundle data = new Bundle();
		data.putSerializable(OrgModel.TAG, listModels);
		data.putInt("orgid", orgid);
		data.putString("orgname", orgname);
		boolean isFinish = false;
		if (context instanceof PayAnswerAskActivity) {
			isFinish = true;
		}
		openActivity(context, PayAnswerAskVipActivity.class, data, isFinish);
	}

	/**
	 * 选传参数
	 * 
	 * @param context
	 * @param orgname
	 * @param orgid
	 * @param listModels
	 */
	public static void goToStuPublishHomeWorkVipActivity(Activity context, String orgname, int orgid,
			List<OrgModel> listModels) {
		Bundle data = new Bundle();
		data.putSerializable(OrgModel.TAG, (Serializable) listModels);
		data.putInt("orgid", orgid);
		data.putString("orgname", orgname);
		boolean isFinish = false;
		if (context instanceof PublishHomeWorkActivity) {
			isFinish = true;
		}
		openActivity(context, PublishHomeWorkVipActivity.class, data, isFinish);

	}

	public static void goToChoiceFudaoActivity(Activity context, ArrayList<OrgModel> listModels) {
		Bundle data = new Bundle();
		data.putSerializable(OrgModel.TAG, listModels);
		openActivityForResult(context, ChoiceFudaoActivity.class, data, false, 1002);
	}

	/**
	 * 此方法描述的是：从学生主页进修改资料
	 * 
	 * @author: qhw @最后修改人： qhw
	 * @最后修改日期:2015-7-22 下午4:28:17
	 * @version: 2.0 goToStuModifiedInfoActivity
	 * @param context
	 *            void
	 */
	public static void goToStuModifiedInfoActivity(Activity context) {
		openActivityForResult(context, StuModifiedInfoActivity.class, null, false,
				PersonHomePageActivity.REQUEST_CODE_MODIFY);
	}

	/**
	 * 此方法描述的是：个人主页修改背景上传前截取
	 * 
	 * @author: qhw @最后修改人： qhw
	 * @最后修改日期:2015-7-23 下午3:06:03
	 * @version: 2.0 goToCropPhotoActivity
	 * @param context
	 * @param path
	 *            void
	 */
	public static void goToCropPhotoActivity(Activity context, String path) {
		Bundle data = new Bundle();
		data.putString("path", path);
		openActivityForResult(context, CropPhotoActivity.class, data, false, PersonHomePageActivity.REQUEST_CODE_CROP);
	}

	/**
	 * 此方法描述的是：查看学生对老师的评价
	 * 
	 * @author: qhw @最后修改人： qhw
	 * @最后修改日期:2015-7-23 下午3:55:34
	 * @version: 2.0 goToStudentAssessmentActivity
	 * @param context
	 * @param userid
	 *            void
	 */
	public static void goToStudentAssessmentActivity(Activity context, int userid) {
		Bundle data = new Bundle();
		data.putInt("userid", userid);
		openActivity(context, StudentAssessmentActivity.class, data, false);
	}

	private static void openActivity(Context context, Class<? extends Activity> clazz, boolean isFinish) {
		try {
			Intent intent = new Intent(context, clazz);
			context.startActivity(intent);
			if (isFinish) {
				((Activity) context).finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param activity
	 * @param activityClazz
	 * @param bundle
	 * @param isFinish
	 * @param requestCode
	 */
	public static void openActivityForResult(Activity activity, Class<? extends Activity> activityClazz, Bundle bundle,
			boolean isFinish, int requestCode) {
		try {
			Intent intent = new Intent(activity, activityClazz);
			if (bundle != null) {
				intent.putExtras(bundle);
			}
			activity.startActivityForResult(intent, requestCode);
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
	 * 过时,不建议使用
	 * 
	 * @param activity
	 * @param activityClazz
	 * @param bundle
	 * @param isFinish
	 */
	@Deprecated
	private static void openActivityForResult(Activity activity, Class<? extends Activity> activityClazz, Bundle bundle,
			boolean isFinish) {
		try {
			Intent intent = new Intent(activity, activityClazz);
			if (bundle != null) {
				intent.putExtras(bundle);
			}
			activity.startActivityForResult(intent, GlobalContant.RESULT_OK);
			// add by milo 2014.09.11
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			if (isFinish) {
				activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void goToMyCramSchoolActivity(Activity context) {
		openActivity(context, MyCramSchoolActivity.class, false);
	}

	public static void goToCramSchoolDetailsActivity(Activity context, Bundle bundle) {
		openActivity(context, CramSchoolDetailsActivity.class, bundle, false);
	}

	public static void goToCramSchoolDetailsActivity(Activity context, Bundle data, int flags) {
		try {
			Intent intent = new Intent(context, CramSchoolDetailsActivity.class);
			if (data != null) {
				intent.putExtras(data);
			}
			intent.setFlags(flags);
			context.startActivity(intent);
			context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void gotoFamousTeacherListActivity(Activity context, Bundle bundle) {
		openActivity(context, FamousTeacherListActivity.class, bundle, false);
	}

	public static void goToGoodsNotesActivity(Activity context, Bundle bundle) {
		openActivity(context, GoodsNotesActivity.class, bundle, false);
	}

	/**
	 * 跳转到公共的webview
	 * 
	 * @author: sky
	 * @param context
	 * @param activityClazz
	 * @param data
	 *            void
	 */
	public static void goToWebViewActivity(Activity context, Class<? extends Activity> activityClazz, Bundle data) {
		openActivity(context, activityClazz, data, false);
	}

	public static void startImageCapture(Context context, int tag) {
		if (!WeLearnFileUtil.sdCardIsAvailable()) {
			ToastUtils.show(R.string.text_toast_sdcard_not_available);
			return;
		}
		if (!WeLearnFileUtil.sdCardHasEnoughSpace()) {
			ToastUtils.show(R.string.text_toast_have_not_enough);
			return;
		}

		int requestCode = 0;
		File file = null;
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch (tag) {
		case GlobalContant.PAY_ANSWER_ASK:
			requestCode = GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_STUDENT;
			file = new File(WeLearnFileUtil.getQuestionFileFolder(), "publish.png");
			break;
		case GlobalContant.PAY_ASNWER:
			requestCode = GlobalContant.CAPTURE_IMAGE_REQUEST_CODE;
			file = new File(WeLearnFileUtil.getAnswerFile(), "publish.png");
			break;
		case GlobalContant.SEND_IMG_MSG:
			requestCode = GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_SEND_IMG;
			file = new File(WeLearnFileUtil.getImgMsgFile(), "publish.png");
			break;
		case GlobalContant.CONTACT_SET_USER_IMG:
			requestCode = GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_USER_LOGO;
			file = new File(WeLearnFileUtil.getContactImgFile(), "publish.png");
			break;
		case GlobalContant.CONTACT_SET_BG_IMG:
			requestCode = GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_USER_BG;
			file = new File(WeLearnFileUtil.getContactImgFile(), "publish.png");
			break;
		default:
			break;
		}
		Uri fileUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		try {
			((Activity) context).startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
