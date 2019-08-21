
package com.daxiong.fun.function.study;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.R;
import com.daxiong.fun.api.HomeListAPI;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.dialog.TousuDialog;
import com.daxiong.fun.dialog.WelearnDialog;
import com.daxiong.fun.function.MyOrientationEventListener;
import com.daxiong.fun.function.MyOrientationEventListener.OnOrientationChangedListener;
import com.daxiong.fun.function.homework.model.HomeWorkCheckPointModel;
import com.daxiong.fun.function.homework.model.HomeWorkSinglePoint;
import com.daxiong.fun.function.homework.view.AddPointCommonView;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.ExplainfeedbackreasonsModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.SharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.CropCircleTransformation;
import com.daxiong.fun.view.DragImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StuHomeWorkSingleCheckActivity extends BaseActivity
		implements OnClickListener, OnOrientationChangedListener {

	private static final String TAG = StuHomeWorkSingleCheckActivity.class.getSimpleName();

	private FrameLayout divParentLayout;
	private TousuDialog tousuDialog;

	private DragImageView mDragImageView;

	private int window_height, window_width, showcomplainttype;

	private AddPointCommonView mAddPointCommonView;

	private HomeWorkCheckPointModel mHomeWorkCheckPointModel;

	protected WelearnDialog mWelearnDialogBuilder;

	private ImageView mAvatarIv;

	private TextView mNickTv;
	private boolean isFankui2 = false;;

	private LinearLayout ll;

	private TextView mNumTv;

	private Button mAskBtn;

	private int checkpointid;
	public boolean isFankui = false;

	private TextView next_step_btn2;

	private TextView next_step_btn;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				loadData();
				break;
			}
		}
	};

	private RelativeLayout nextStepLayout;

	private TextView nextStepTV;

	private int taskid;

	private MyOrientationEventListener moraientation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_check_stu_activity);
		setWelearnTitle(R.string.single_homework_title_text);
		// slideClose = false;// 禁止滑动退出
		initView();

		moraientation = new MyOrientationEventListener(this, this);
	}

	@Override
	public void onResume() {
		super.onResume();
		moraientation.enable();
	}

	@Override
	public void onPause() {
		mAddPointCommonView.stopVoice();
		super.onPause();
		moraientation.disable();
	}

	public void initView() {
		// 右边的按钮
		next_step_btn2 = (TextView) findViewById(R.id.next_step_btn2);
		next_step_btn2.setVisibility(View.VISIBLE);
		next_step_btn = (TextView) this.findViewById(R.id.next_step_btn);
		next_step_btn2.setVisibility(View.VISIBLE);
		next_step_btn.setText("");
		next_step_btn.setHeight(30);
		next_step_btn.setWidth(30);
		next_step_btn.setBackgroundResource(R.drawable.biaozhu_close_eye);
		next_step_btn2.setOnClickListener(this);

		divParentLayout = (FrameLayout) findViewById(R.id.div_parent_layout);
		mAddPointCommonView = (AddPointCommonView) findViewById(R.id.add_point_common_stu_single);
		mDragImageView = (DragImageView) findViewById(R.id.pic_iv_add_point);
		divParentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (window_height == 0 || window_width == 0) {
					window_height = divParentLayout.getHeight();
					window_width = divParentLayout.getWidth();
					mDragImageView.setScreenSize(window_width, window_height);
				}
			}
		});
		ll = (LinearLayout) findViewById(R.id.ll);
		int avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size_homework_check_common);
		findViewById(R.id.back_layout).setOnClickListener(this);
		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);

		mAvatarIv = (ImageView) findViewById(R.id.tec_avatar_iv_single);
		mNickTv = (TextView) findViewById(R.id.tec_nick_tv_single);
		mNumTv = (TextView) findViewById(R.id.tec_num_tv_single);

		mAskBtn = (Button) findViewById(R.id.append_btn_single);
		findViewById(R.id.analysis_btn_stu_detail).setOnClickListener(this);
		mAvatarIv.setOnClickListener(this);
		mAskBtn.setOnClickListener(this);
		Intent intent = getIntent();
		if (intent != null) {
			boolean fromMsg = intent.getBooleanExtra("fromMsg", false);
			isFankui2 = intent.getBooleanExtra("isFankui2", false);
			if (fromMsg) {
				nextStepTV.setVisibility(View.VISIBLE);
				// nextStepTV.setText(R.string.look_homework_text);
				nextStepLayout.setOnClickListener(this);

				taskid = intent.getIntExtra("taskid", 0);
				showcomplainttype = intent.getIntExtra("showcomplainttype", 0);

			}
			mHomeWorkCheckPointModel = (HomeWorkCheckPointModel) intent
					.getSerializableExtra(HomeWorkCheckPointModel.TAG);
			int stuid=intent.getIntExtra("stuid", 0);
			if (mHomeWorkCheckPointModel != null) {
				boolean allowAppendAsk = mHomeWorkCheckPointModel.isAllowAppendAsk();
				int userId=MySharePerfenceUtil.getInstance().getUserId();
				if (userId==stuid) {//如果是自己的作业才有追问
					if (allowAppendAsk&&(mHomeWorkCheckPointModel.getShowcomplainttype()==0 || mHomeWorkCheckPointModel.getShowcomplainttype()==2)) {
						mAskBtn.setVisibility(View.VISIBLE);					
					} else {
						findViewById(R.id.ll_dangban).setVisibility(View.VISIBLE);
					}
				}
			
				checkpointid = mHomeWorkCheckPointModel.getId();
				mAddPointCommonView.setCheckPointImg(mHomeWorkCheckPointModel, false);

				String teacheravatar = mHomeWorkCheckPointModel.getTeacheravatar();
				if (teacheravatar != null) {
					// ImageLoader.getInstance().loadImageWithDefaultAvatar(teacheravatar,
					// mAvatarIv, avatarSize,
					// avatarSize / 10);

					Glide.with(this).load(teacheravatar).bitmapTransform(new CropCircleTransformation(this))
							.diskCacheStrategy(DiskCacheStrategy.ALL)
							.placeholder(R.drawable.default_icon_circle_avatar).into(mAvatarIv);
				}
				String teachername = mHomeWorkCheckPointModel.getTeachername();
				if (teachername != null) {
					mNickTv.setText(teachername);
				}
				int homeworkcnt = mHomeWorkCheckPointModel.getTeacherhomeworkcnt();
				mNumTv.setText(getString(R.string.answer_num_text, homeworkcnt + ""));
				
				

				loadData();
				// mHandler.sendMessageDelayed(mHandler.obtainMessage(1), 10);
			}
		}
	}

	private void loadData() {
		JSONObject data = new JSONObject();
		try {
			data.put("checkpointid", checkpointid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(this, "parents","homeworkexplain", data, new HttpListener() {

			@Override
			public void onFail(int code,String errMsg) {

			}

			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				if (code == 0) {
					ArrayList<HomeWorkSinglePoint> pointList = null;
					try {
						pointList = new Gson().fromJson(dataJson, new TypeToken<ArrayList<HomeWorkSinglePoint>>() {
						}.getType());
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
					}
					if (pointList != null) {
						mHomeWorkCheckPointModel.setExplianlist(pointList);
						mAddPointCommonView.showCheckPoint(pointList);

					}
				} else {
					ToastUtils.show(errMsg);
				}

			}
		});
	}

	boolean isShow = false;

	@Override
	public void onClick(View v) {
		Bundle data = new Bundle();
		switch (v.getId()) {
		case R.id.back_layout:
			
				setResult(GlobalContant.RESULT_OK);
			
			finish();
			break;
		case R.id.analysis_btn_stu_detail:
			new HomeListAPI().getExplainfeedbackreasons(requestQueue, this, RequestConstant.GET_FANKUILIYOU_CODE);
			break;
		case R.id.next_step_btn2:// 显示隐藏标注
			View popuView = View.inflate(StuHomeWorkSingleCheckActivity.this, R.layout.popu_xianshi_menu, null);


			TextView tv1 = (TextView) popuView.findViewById(R.id.tv1);
			TextView tv2 = (TextView) popuView.findViewById(R.id.tv2);


			final PopupWindow pw = new PopupWindow(popuView, FrameLayout.LayoutParams.WRAP_CONTENT,
					FrameLayout.LayoutParams.WRAP_CONTENT, true);
			tv1.setOnClickListener(new OnClickListener() {
									   @Override
									   public void onClick(View v) {
										   pw.dismiss();
										   mAddPointCommonView.showPoints(3);
									   }
								   }
			);
			tv2.setOnClickListener(new OnClickListener() {
									   @Override
									   public void onClick(View v) {
										   pw.dismiss();
										   mAddPointCommonView.showPoints(2);
									   }
								   }
			);

			pw.setBackgroundDrawable(new ColorDrawable(0));

			pw.showAsDropDown(next_step_btn2, 0, 0);
			break;
		case R.id.tec_avatar_iv_single:
			int userid = mHomeWorkCheckPointModel.getGrabuserid();
			IntentManager.gotoPersonalPage(this, userid, GlobalContant.ROLE_ID_COLLEAGE);
			break;
		case R.id.append_btn_single://追问			
			data.putSerializable(HomeWorkCheckPointModel.TAG, mHomeWorkCheckPointModel);
			IntentManager.goToTecSingleCheckActivity(StuHomeWorkSingleCheckActivity.this, data, false, 10086);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 10086) {
				boolean isSubmit = data.getBooleanExtra("isSubmit", false);
				if (isSubmit) {
					onhidefankui(false);
					ArrayList<HomeWorkSinglePoint> pointList = null;
					try {
						pointList = (ArrayList<HomeWorkSinglePoint>) data.getSerializableExtra(HomeWorkSinglePoint.TAG);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (pointList != null) {
						mHomeWorkCheckPointModel.getExplianlist().addAll(pointList);
						mAddPointCommonView.showCheckPoint(pointList);
					}
				}
			}
		}
	}

	@Override
	public void onOrientationChanged(int orientation) {
		if (null != mAddPointCommonView) {
			mAddPointCommonView.setOrientation(orientation);
		}
	}
	public void onhidefankui(boolean falg) {

		if(falg){
			findViewById(R.id.ll_dangban).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onBackPressed() {
		if (GlobalVariable.answertextPopupWindow != null && GlobalVariable.answertextPopupWindow.isShowing()) {
			GlobalVariable.answertextPopupWindow.dismiss();

			return;
		}
		
			setResult(GlobalContant.RESULT_OK);
	
		finish();
		super.onBackPressed();

	}

	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);
		int flag = ((Integer) param[0]).intValue();
		switch (flag) {

		case RequestConstant.GET_FANKUILIYOU_CODE:
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtil.getInt(datas, "Code", -1);
				if (code == 0) {
					String Data = JsonUtil.getString(datas, "Data", "");
					if (!TextUtils.isEmpty(Data)) {
						List<ExplainfeedbackreasonsModel> list = JSON.parseArray(Data,
								ExplainfeedbackreasonsModel.class);
						if(taskid==0){
							taskid=SharePerfenceUtil.getInt("HomeWorkTaskid", 0);
						}
						tousuDialog = new TousuDialog(StuHomeWorkSingleCheckActivity.this, list, checkpointid, taskid,1);
						tousuDialog.show();
					}

				}

			}

			break;

		case -1:
			ToastUtils.show("网络异常");

			break;

		}

	}

}
