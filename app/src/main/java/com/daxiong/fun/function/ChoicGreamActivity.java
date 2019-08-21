package com.daxiong.fun.function;
//package com.welearn.welearn;
//
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.welearn.base.WeLearnApi;
//import com.welearn.base.view.SingleFragmentActivity;
//import com.welearn.constant.GlobalContant;
//import com.welearn.constant.MsgDef;
//import com.welearn.constant.ResponseCmdDef;
//import com.welearn.controller.ChoicGreamController;
//import com.welearn.db.WLDBHelper;
//import com.welearn.manager.INetWorkListener;
//import com.welearn.manager.IntentManager;
//import com.welearn.model.UnivGson;
//import com.welearn.model.UserInfoModel;
//import com.welearn.util.JSONUtils;
//import com.welearn.util.ThreadPoolUtil;
//import com.welearn.util.ToastUtils;
//import com.welearn.util.WeLearnSpUtil;
//
//public class ChoicGreamActivity extends SingleFragmentActivity implements OnClickListener, INetWorkListener {
//
//	private TextView pass;
//	private LinearLayout choic_school_1;
//	private LinearLayout choic_school_2;
//	private LinearLayout choic_school_3;
//	ChoicGreamController mChoicGreamController;
//	private TextView gream_school1_tv;
//	private TextView gream_school2_tv;
//	private TextView gream_school3_tv;
//	private TextView choic_school_sumbit;
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		if (mChoicGreamController == null) {
//			mChoicGreamController = new ChoicGreamController(this);
//
//		}
//		setGreamSchoolText();
//	}
//
//	private void setGreamSchoolText() {
//		String greamSchool1 = WeLearnSpUtil.getInstance().getGreamSchool1();
//		String greamSchool2 = WeLearnSpUtil.getInstance().getGreamSchool2();
//		String greamSchool3 = WeLearnSpUtil.getInstance().getGreamSchool3();
//		if (TextUtils.isEmpty(greamSchool1)) {
//			gream_school1_tv.setText(R.string.text_dream_school_1);
//			gream_school2_tv.setClickable(false);
//			gream_school3_tv.setClickable(false);
//		} else {
//			gream_school2_tv.setClickable(true);
//			// gream_school3_tv.setClickable(false);
//			gream_school1_tv.setText(greamSchool1);
//		}
//		if (TextUtils.isEmpty(greamSchool2)) {
//			gream_school3_tv.setClickable(false);
//			gream_school2_tv.setText(R.string.text_dream_school_2);
//		} else {
//			gream_school3_tv.setClickable(true);
//			gream_school2_tv.setText(greamSchool2);
//		}
//		if (TextUtils.isEmpty(greamSchool3)) {
//			gream_school3_tv.setText(R.string.text_dream_school_3);
//		} else {
//			gream_school3_tv.setText(greamSchool3);
//		}
//
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		if (mChoicGreamController != null) {
//			mChoicGreamController.removeMsgInQueue();
//		}
//		closeDialog();
//		mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
//		isSum = false;
//	}
//
//	private boolean isSetting = false;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		Intent intent = getIntent();
//		isSetting = intent.getBooleanExtra("isSetting", false);
//		// if (isSetting) {
//		// // mActionBar.setDisplayHomeAsUpEnabled(true);
//		// mActionBar.setHomeButtonEnabled(true);
//		// } else {
//		// mActionBar.setDisplayShowHomeEnabled(false);
//		// mActionBar.setDisplayUseLogoEnabled(false);
//		// mActionBar.setHomeButtonEnabled(false);
//		// }
//		//
//		// mActionBar.setTitle("选择梦想大学");
//
//		mChoicGreamController = new ChoicGreamController(this);
//		setContentView(R.layout.fragment_choic_gream_col);
//		
//		setWelearnTitle(R.string.text_choose_dream_school);
//		
//		findViewById(R.id.back_layout).setOnClickListener(this);
//
//		pass = (TextView) findViewById(R.id.choic_school_none);
//		pass.setOnClickListener(this);
//		choic_school_1 = (LinearLayout) findViewById(R.id.choic_school_1);
//		choic_school_2 = (LinearLayout) findViewById(R.id.choic_school_2);
//		choic_school_3 = (LinearLayout) findViewById(R.id.choic_school_3);
//		choic_school_1.setOnClickListener(this);
//		choic_school_2.setOnClickListener(this);
//		choic_school_3.setOnClickListener(this);
//		gream_school1_tv = (TextView) findViewById(R.id.gream_school1_tv);
//		gream_school2_tv = (TextView) findViewById(R.id.gream_school2_tv);
//		gream_school3_tv = (TextView) findViewById(R.id.gream_school3_tv);
//
//		choic_school_sumbit = (TextView) findViewById(R.id.choic_school_sumbit);
//		choic_school_sumbit.setOnClickListener(this);
//		// setGreamSchoolText();
//
//		if (!WeLearnSpUtil.getInstance().isDownUnivList()) {
//			WeLearnApi.getUnivList(this);
//		}
//		
//	}
//
//	private boolean isSum = false;
//
//	@Override
//	public void onClick(View v) {
//		Bundle data = new Bundle();
//		data.putBoolean("isSearch", true);
//		switch (v.getId()) {
//		case R.id.back_layout:
//			finish();
//			break;
//		case R.id.choic_school_none:
//			if (isSetting) {
//				finish();
//			} else {
////				IntentManager.goToMainViewStudent(this);
//				IntentManager.goToMainView(this);
//				finish();
//			}
//
//			break;
//
//		case R.id.choic_school_1:
//			data.putInt("choic", 1);
//			IntentManager.goToSearchSchoolActivity(this, data, false);
//			break;
//
//		case R.id.choic_school_2:
//			data.putInt("choic", 2);
//			IntentManager.goToSearchSchoolActivity(this, data, false);
//			break;
//
//		case R.id.choic_school_3:
//			data.putInt("choic", 3);
//			IntentManager.goToSearchSchoolActivity(this, data, false);
//			break;
//
//		case R.id.choic_school_sumbit:
//
//			int greamSchoolID1 = WeLearnSpUtil.getInstance().getGreamSchoolID1();
//
//			if (greamSchoolID1 == 0) {
//				ToastUtils.show(R.string.text_choose_dream_school_tip);
//				return;
//			}
//			int greamSchoolID2 = WeLearnSpUtil.getInstance().getGreamSchoolID2();
//			int greamSchoolID3 = WeLearnSpUtil.getInstance().getGreamSchoolID3();
//			String greamSchoolID = "" + greamSchoolID1;
//			String greamSchool1 = WeLearnSpUtil.getInstance().getGreamSchool1();
//			String greamSchool2 = WeLearnSpUtil.getInstance().getGreamSchool2();
//			String greamSchool3 = WeLearnSpUtil.getInstance().getGreamSchool3();
//			String greamSchool = "" + greamSchool1;
//
//			if (greamSchoolID2 != 0) {
//				greamSchool = greamSchool + ";" + greamSchool2;
//				greamSchoolID = greamSchoolID + ";" + greamSchoolID2;
//			}
//
//			if (greamSchoolID3 != 0) {
//				greamSchool = greamSchool + ";" + greamSchool3;
//				greamSchoolID = greamSchoolID + ";" + greamSchoolID3;
//			}
//
//			JSONObject jsonData = new JSONObject();
//			JSONObject jsonDic = new JSONObject();
//			try {
//				jsonDic.put("dreamuniv", greamSchool);
//				jsonDic.put("dreamunivid", greamSchoolID);
//				jsonData.put("update_dic", jsonDic);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			WeLearnApi.updateUserInfoFromServer(this, jsonDic, null);
//			showDialog(getString(R.string.text_commiting_please_wait));
//			isSum = true;
//			// Message msg = Message.obtain();
//			// msg.what = 721;
//			mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 7000);
//			// IntentManager.goToMainView(mActivity, false);
//			// mActivity.finish();
//			break;
//		}
//
//	}
//
//	@SuppressLint("HandlerLeak")
//	private Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if (msg.what == GlobalContant.CLOSEDIALOG) {
//				if (isSum) {
//					sumCloseDialog();
//				}
//			}
//		}
//
//	};
//
//	private void sumCloseDialog() {
//		closeDialog();
//		isSum = false;
//		ToastUtils.show(R.string.text_commit_timeout);
//	}
//
//	@Override
//	public void onPre() {
//
//	}
//
//	@Override
//	public void onException() {
//		sumCloseDialog();
//		mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
//	}
//
//	List<UnivGson> list = null;
//
//	@Override
//	public void onAfter(String jsonStr, int msgDef) {
//		int code = JSONUtils.getInt(jsonStr, "code", -1);
//		String errMsg = JSONUtils.getString(jsonStr, "errmsg", "");
//		switch (msgDef) {
//		case MsgDef.MSG_DEF_UNI_LIST:
//			if (ResponseCmdDef.CODE_RETURN_OK == code) {
//				JSONArray univs = JSONUtils.getJSONArray(jsonStr, "data", null);
//				// Log.e("大学列表--->", univs.toString());
//				if (univs != null) {
//					list = new Gson().fromJson(univs.toString(), new TypeToken<List<UnivGson>>() {
//					}.getType());
//				}
//
//				ThreadPoolUtil.execute(new Runnable() {
//
//					@Override
//					public void run() {
//
//						/*
//						 * 更改数据调用方式 modified by yh 2015-01-07 Start
//						 * ---------------------- OLD CODE
//						 * ---------------------- new
//						 * UnivListDBHelper(mActivity).insert(list);
//						 */
//						WLDBHelper.getInstance().getWeLearnDB().insertUniv(list);
//						// 更改数据调用方式 modified by yh 2015-01-07 End
//
//					}
//				});
//			} else {
//				ToastUtils.show(errMsg);
//			}
//
//			break;
//		case MsgDef.MSG_DEF_UPDATE_CONTACT_INFO:
//			if (ResponseCmdDef.CODE_RETURN_OK == code) {
//				JSONObject data = JSONUtils.getJSONObject(jsonStr, "data", null);
//				if (data != null) {
//					try {
////						final int infostate = data.getInt("infostate");
////						if (infostate != 0) {
////							WeLearnSpUtil.getInstance().setIsChoicGream(true);
////						}
//						final String dreamuniv = data.getString("dreamuniv");
//						ThreadPoolUtil.execute(new Runnable() {
//
//							@Override
//							public void run() {
//
//								/*
//								 * 更改数据调用方式 modified by yh 2015-01-07 Start
//								 * ---------------------- OLD CODE
//								 * ---------------------- new
//								 * UserInfoDBHelper(mActivity).update(infostate,
//								 * userid, dreamuniv);
//								 */
//								UserInfoModel uInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
//								if (null != uInfo) {
//									uInfo.setDreamuniv(dreamuniv);
//									WLDBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
//								}
//								// 更改数据调用方式 modified by yh 2015-01-07 End
//
//							}
//						});
//
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//					isSum = false;
//					closeDialog();
//					mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
//					if (isSetting) {
//						finish();
//					} else {
////						IntentManager.goToMainViewStudent(this);
//						IntentManager.goToMainView(this);
//					}
//					// ToastUtils.show(mActivity, "提交成功");
//				}
//			} else {
//				ToastUtils.show(errMsg);
//				closeDialog();
//				isSum = false;
//				mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
//			}
//			this.finish();
//			break;
//		}
//	}
//
//	@Override
//	public void onDisConnect() {
//		closeDialog();
//		isSum = false;
//		mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
//		showNetWorkExceptionToast();
//	}
//}
