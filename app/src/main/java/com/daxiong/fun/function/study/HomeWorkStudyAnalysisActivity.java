package com.daxiong.fun.function.study;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.ShareActivity;
import com.daxiong.fun.function.homework.CoordinateSystem;
import com.daxiong.fun.function.homework.model.HomeWorkCheckPointModel;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.ShareModel;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.StirngUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class HomeWorkStudyAnalysisActivity extends BaseActivity implements OnClickListener {
	private View thisAnaContainer;
	private View monthAnaContainer;
	private CoordinateSystem kType;
	private CoordinateSystem sType;
	private LinearLayout wrongBtnContainer;
	private Button thisBtn;
	private Button monthBtn;
	
	private TextView nextStepTV;
	private RelativeLayout nextStepLayout;
	private int stuid;
	private boolean isCurrentUser;
	
	private int subjectid;
	
	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.homework_analysis_activity);
		setWelearnTitle("作业学情分析");
		findViewById(R.id.back_layout).setOnClickListener(this);
		
		thisBtn = (Button) findViewById(R.id.this_analysis_btn_stu_homework);
		thisBtn.setOnClickListener(this);
		thisBtn.setClickable(false);
		monthBtn = (Button) findViewById(R.id.month_analysis_btn_stu_homework);
		monthBtn.setOnClickListener(this);
		thisAnaContainer = findViewById(R.id.this_ana_container);
		monthAnaContainer = findViewById(R.id.month_ana_container);
		thisAnaContainer.setVisibility(View.VISIBLE);
		monthAnaContainer.setVisibility(View.GONE);

		wrongBtnContainer = (LinearLayout) findViewById(R.id.wrong_btn_container_analysis);
		TextView sTypeNumTv = (TextView) findViewById(R.id.s_type_num_tv);
		TextView kTypeNumTv = (TextView) findViewById(R.id.k_type_num_tv);
		kType = (CoordinateSystem) findViewById(R.id.k_type_coordinate);
		sType = (CoordinateSystem) findViewById(R.id.s_type_coordinate);

		NetworkImageView stuAvatarIv = (NetworkImageView) findViewById(R.id.stu_avatar_iv_analysis);
		TextView stuNickNameTv = (TextView) findViewById(R.id.stu_nick_tv_analysis);
		TextView stuIdTv = (TextView) findViewById(R.id.stu_id_tv_analysis);
		
		Intent intent = getIntent();
		if (intent != null) {
			@SuppressWarnings("unchecked")
			ArrayList<HomeWorkCheckPointModel> sWrongPointList = (ArrayList<HomeWorkCheckPointModel>) intent
					.getSerializableExtra("sWrongPointList");
			@SuppressWarnings("unchecked")
			ArrayList<HomeWorkCheckPointModel> kWrongPointList = (ArrayList<HomeWorkCheckPointModel>) intent
					.getSerializableExtra("kWrongPointList");
			subjectid = intent.getIntExtra("subjectid", 0);
			boolean checkingFlag = intent.getBooleanExtra("checkingFlag", false);
			
			isCurrentUser = intent.getBooleanExtra("isCurrentUser", true);
			if (isCurrentUser) {
				nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
				nextStepTV = (TextView) findViewById(R.id.next_step_btn);
				nextStepTV.setVisibility(View.VISIBLE);
				nextStepTV.setText(R.string.share);
				nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
				nextStepLayout.setOnClickListener(this);
				
				findViewById(R.id.choice_analysis_container).setVisibility(View.VISIBLE);
			}
			String stuavatar = intent.getStringExtra("stuavatar");
			String stuname = intent.getStringExtra("stuname");
			stuid = intent.getIntExtra("stuid", 0);
			stuNickNameTv.setText(stuname);
			stuIdTv.setText(getString(R.string.userid_text, stuid));
			int avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size_homework_check_common);
			ImageLoader.getInstance().loadImageWithDefaultAvatar(stuavatar, stuAvatarIv, avatarSize,
					avatarSize / 10);
			
			stuAvatarIv.setOnClickListener(this);
			
			JSONObject data = new JSONObject();
			try {
				data.put("subjectid", subjectid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			OkHttpHelper.post(this,"homework","analyzeone", data , new HttpListener() {
				
				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (code == 0) {
						String jsonStr = JsonUtil.getString(dataJson, "" + subjectid, "");
						int[] kwrongS  = null;
						int[] swrongS  = null;
						try {
							kwrongS = new Gson().fromJson(JsonUtil.getString(jsonStr, "kwrong", ""),
									new TypeToken<int[]>() {
									}.getType());
							swrongS = new Gson().fromJson(JsonUtil.getString(jsonStr, "swrong", ""),
									new TypeToken<int[]>() {
									}.getType());
							
						} catch (Exception e) {
						}
						if (kwrongS != null) {
							kType.SetInfo(new String[] { "0", "5", "10", "15", "20", "25", "30", "35" }, // X轴刻度
									new String[] { "", "5", "10", "15", "20", "25" }, // Y轴刻度
									new String[] { "15", "23", "10", "22", "17", "12", "18" }, // 数据
									kwrongS);
							
						}
						if (swrongS != null) {
							sType.SetInfo(new String[] { "0", "5", "10", "15", "20", "25", "30", "35" }, // X轴刻度
									new String[] { "", "5", "10", "15", "20", "25" }, // Y轴刻度
									new String[] { "15", "23", "10", "22", "17", "12", "18" }, // 数据
									swrongS);
						}
					}else {
						ToastUtils.show(errMsg);
					}
				}
				
				@Override
				public void onFail(int HttpCode,String errMsg) {
					
				}
			});
			if (sWrongPointList != null && kWrongPointList != null) {
				int sSize = sWrongPointList.size();
				int kSize = kWrongPointList.size();
				sTypeNumTv.setText(sSize + "道");
				kTypeNumTv.setText(kSize + "道");
				for (int i = 0; i < (sSize > kSize ? sSize : kSize); i++) {
					View item = View.inflate(this, R.layout.study_analysis_item, null);
					if (i < sSize) {
						HomeWorkCheckPointModel checkPointModel = sWrongPointList.get(i);
						checkPointModel.setAllowAppendAsk(checkingFlag);
						Button sBtn = (Button) item.findViewById(R.id.s_wrong_analysis_btn_item);
						sBtn.setVisibility(View.VISIBLE);
						sBtn.setTag(checkPointModel);
						sBtn.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View btn) {
								uMengEvent("homework_analysisintowrong");
								HomeWorkCheckPointModel checkPointModel = (HomeWorkCheckPointModel) btn.getTag();
								Bundle data = new Bundle();
								data.putSerializable(HomeWorkCheckPointModel.TAG, checkPointModel);
								IntentManager.goToStuSingleCheckActivity(HomeWorkStudyAnalysisActivity.this, data, false);
							}
						});
					}
					if (i < kSize) {
						HomeWorkCheckPointModel checkPointModel = kWrongPointList.get(i);
						checkPointModel.setAllowAppendAsk(checkingFlag);
						Button kBtn = (Button) item.findViewById(R.id.k_wrong_analysis_btn_item);
						Button kpointBtn = (Button) item.findViewById(R.id.kpoint_analysis_btn_item);
						kBtn.setVisibility(View.VISIBLE);
						String kpoint = checkPointModel.getKpoint();
						if (!TextUtils.isEmpty(kpoint)) {
							kpointBtn.setVisibility(View.VISIBLE);
							kpointBtn.setText(kpoint);
						}
						kBtn.setTag(checkPointModel);
						kBtn.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View btn) {
								uMengEvent("homework_analysisintowrong");
								HomeWorkCheckPointModel checkPointModel = (HomeWorkCheckPointModel) btn.getTag();
								Bundle data = new Bundle();
								data.putSerializable(HomeWorkCheckPointModel.TAG, checkPointModel);
								IntentManager.goToStuSingleCheckActivity(HomeWorkStudyAnalysisActivity.this, data, false);
							}
						});
					}
					wrongBtnContainer.addView(item);
				}
			}
		}

	
	}

	private void setThisBtnClick() {
		uMengEvent("homework_analysisfromdetail");
		thisAnaContainer.setVisibility(View.VISIBLE);
		monthAnaContainer.setVisibility(View.GONE);
		thisBtn.setClickable(false);
		thisBtn.setBackgroundResource(R.drawable.adopt_homework_btn_selector_new);
		thisBtn.setTextColor(getResources().getColor(R.color.green));
		monthBtn.setClickable(true);
		monthBtn.setBackgroundResource(R.drawable.refuse_homework_btn_selector_new);
		monthBtn.setTextColor(getResources().getColor(R.color.white));
	}

	private void setMonthBtnClick() {
		uMengEvent("homework_monthanalysisfromdetail");
		thisAnaContainer.setVisibility(View.GONE);
		monthAnaContainer.setVisibility(View.VISIBLE);
		monthBtn.setClickable(false);
		monthBtn.setBackgroundResource(R.drawable.adopt_homework_btn_selector_new);
		monthBtn.setTextColor(getResources().getColor(R.color.green));
		thisBtn.setClickable(true);
		thisBtn.setBackgroundResource(R.drawable.refuse_homework_btn_selector_new);
		thisBtn.setTextColor(getResources().getColor(R.color.white));
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.stu_avatar_iv_analysis:
			if (isCurrentUser) {
				UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
				if (null != uInfo) {
					Bundle data = new Bundle();
					data.putInt("userid", uInfo.getUserid());
					data.putInt("roleid", GlobalContant.ROLE_ID_STUDENT);
//					IntentManager.goToStudentCenterView(this, data);
				}
			} else {
				Bundle data = new Bundle();
				data.putInt("userid", stuid);
				data.putInt("roleid", GlobalContant.ROLE_ID_STUDENT);
				IntentManager.goToStudentInfoView(this, data);
			}
			break;
		case R.id.this_analysis_btn_stu_homework:
			setThisBtnClick();
			break;
		case R.id.month_analysis_btn_stu_homework:
			setMonthBtnClick();
			break;
		case R.id.next_setp_layout:
			//TODO 先截屏...
			shoot(this, new File(WeLearnFileUtil.getShotImagePath()));
			
			OkHttpHelper.post(this, "guest", "getappconfigs",  null, new HttpListener() {
				
				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {					
					String shareTitle = JsonUtil.getString(dataJson, "sharetitle2", getString(R.string.share_title)); 
					String shareDesc = JsonUtil.getString(dataJson, "sharedesc2", getString(R.string.share_desc));  
					String shareUrl = JsonUtil.getString(dataJson, "shareurl2",AppConfig.HOMEWORK_STUDY_ANALYSIS_SHARE_URL);  
					
					String stuReString = Integer.toHexString(stuid);
					if (null != stuReString) {
						stuReString = new StringBuffer(stuReString).reverse().toString();
					}
					
					stuReString = StirngUtil.getRadomChar() + stuReString + StirngUtil.getRadomChar();

					String shareUrlFormated = String.format(shareUrl, stuReString.toUpperCase(), subjectid);

					ShareModel sm = new ShareModel();
					sm.setShareTitle(shareTitle);
					sm.setShareDesc(shareDesc);
					sm.setShareUrl(shareUrlFormated);
					
					Intent i = new Intent(HomeWorkStudyAnalysisActivity.this, ShareActivity.class);
					i.putExtra(ShareActivity.SHARE_MODEL_TAG, sm);
					startActivity(i);
				
					
				}
				
				@Override
				public void onFail(int HttpCode,String errMsg) {
					
					
				}
			});
			
		
			break;
		}
	}

	private  Bitmap takeScreenShot(Activity activity) {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();

		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		// 获取屏幕长和高
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay().getHeight();
		// 去掉标题栏
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height- statusBarHeight);
		view.destroyDrawingCache();
		return b;
	}

	private  void savePic(Bitmap b, File filePath) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			 e.printStackTrace();
		} catch (IOException e) {
			 e.printStackTrace();
		}
	}

	public  void shoot(Activity a, File filePath) {
		if (filePath == null) {
			return;
		}
		if (!filePath.getParentFile().exists()) {
			filePath.getParentFile().mkdirs();
		}
		savePic(takeScreenShot(a), filePath);
	}
}
