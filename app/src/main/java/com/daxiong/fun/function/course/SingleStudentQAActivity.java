package com.daxiong.fun.function.course;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.dialog.WelearnDialog;
import com.daxiong.fun.function.MyOrientationEventListener;
import com.daxiong.fun.function.MyOrientationEventListener.OnOrientationChangedListener;
import com.daxiong.fun.function.course.model.CoursePoint;
import com.daxiong.fun.function.course.model.UpLoadPointsModel;
import com.daxiong.fun.function.course.view.AddPointCommonView;
import com.daxiong.fun.function.homework.view.InputExplainView;
import com.daxiong.fun.function.homework.view.InputExplainView.ResultListener;
import com.daxiong.fun.function.homework.view.VoiceOrTextPoint;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleStudentQAActivity extends BaseActivity implements OnClickListener,
		OnOrientationChangedListener, OnLongClickListener, OnUploadListener {

	public static final String KNOWLEDGE_NAME = "knowledgeName";
	private static final int CHOICE_KNOWLEDGE = 10010;
	// private static final String TAG =
	// TecHomeWorkSingleCheckActivity.class.getSimpleName();
	private AddPointCommonView mAddPointCommonView;
	private MyOrientationEventListener moraientation;
//	private Button voiceChooseIBtn;
//	private Button textChooseIBtn;
//	private RelativeLayout textInputLayout;
//	private Button inputOKBtn;
//	private EditText textInputET;
//	protected PopupWindow bgPopupWindow;
//	protected PopupWindow popupWindow;
//	protected RecordPopupViewOfCourse menuView;
//
//	private LinearLayout answerChooseLayout;

	private String mCoordinate;
	private CoursePoint mPointModel;
	private UpLoadPointsModel submitModel;
	private ArrayList<CoursePoint> mPointList;
	private static final int OP_GIVE_UP = 0x1;
	private static final int OP_COMMIT = 0x2;
	private static final int OP_REMOVE_POINT = 0x3;
	private InputExplainView inputLayout;
	protected WelearnDialog mWelearnDialogBuilder;

	private InputMethodManager imm;
	private int userid;

	public interface DialogListener {
		void onConfirmBtnClick(CoursePoint mSinglePoint);

		void onCancelBtnClick();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		setContentView(R.layout.single_student_qa_activity);
//		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		moraientation = new MyOrientationEventListener(this, this);
		initView();
	}

	@Override
	public void onPause() {
		mAddPointCommonView.stopVoice();
		moraientation.disable();
		super.onPause();

	}

	@Override
	public void onResume() {
		super.onResume();
		moraientation.enable();
	}
	
	public void initView() {
		findViewById(R.id.back_layout).setOnClickListener(this);
		int avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size_homework_check_common);

		findViewById(R.id.next_setp_layout).setOnClickListener(this);
		TextView nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
		nextStepTV.setText(R.string.submit_text);

		mAddPointCommonView = (AddPointCommonView) findViewById(R.id.add_point_common_tec_single);
		inputLayout = (InputExplainView) findViewById(R.id.input_container_tec_single);
		inputLayout.setVisibility(View.GONE);
	
//		textChooseIBtn = (Button) findViewById(R.id.text_choice_ibtn);
//		voiceChooseIBtn = (Button) findViewById(R.id.voice_choice_ibtn);
//
//		answerChooseLayout = (LinearLayout) findViewById(R.id.answer_choice_layout);
//		textInputLayout = (RelativeLayout) findViewById(R.id.text_input_layout);
//		inputOKBtn = (Button) findViewById(R.id.input_sure_btn);
//		textInputET = (EditText) findViewById(R.id.text_input_et);

		NetworkImageView avatarIv = (NetworkImageView) findViewById(R.id.avatar_iv_single_stu);
		avatarIv.setOnClickListener(this);
		TextView nameTv = (TextView) findViewById(R.id.name_iv_single_stu);

//		textInputET.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
//				int length = str.length();
//				if (length == 0) {
//					inputOKBtn.setText(SingleStudentQAActivity.this.getString(R.string.text_nav_cancel));
//				} else {
//					inputOKBtn.setText(SingleStudentQAActivity.this.getString(R.string.text_nav_submit));
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//
//			}
//		});
//
//		voiceChooseIBtn.setOnClickListener(this);
//		textChooseIBtn.setOnClickListener(this);
//		inputOKBtn.setOnClickListener(this);
		mPointList = new ArrayList<CoursePoint>();
		submitModel = new UpLoadPointsModel();
		Intent intent = getIntent();
		if (intent != null) {
			int pageid = intent.getIntExtra("pageid", 0);
			userid = intent.getIntExtra("userid", 0);
			String imgpath = intent.getStringExtra("imgpath");
			String avatar = intent.getStringExtra("avatar");
			String name = intent.getStringExtra("name");
			String charptername = intent.getStringExtra("charptername");
			String pointlistStr = intent.getStringExtra("pointlist");

			setWelearnTitle(charptername);
			ImageLoader.getInstance().loadImage(avatar, avatarIv, R.drawable.ic_default_avatar, avatarSize,
					avatarSize / 10);
			nameTv.setText("" + name);

			int belongcourse = 0;// 1 课程 0 追问或者追问回答

			submitModel.setStudentid(MySharePerfenceUtil.getInstance().getUserId());
			submitModel.setPageid(pageid);
			submitModel.setBelongcourse(belongcourse);

			mAddPointCommonView.setImgPath(imgpath, true);
			if (!TextUtils.isEmpty(pointlistStr)) {
				ArrayList<CoursePoint> pointList = null;
				try {
					pointList = new Gson().fromJson(pointlistStr, new TypeToken<ArrayList<CoursePoint>>() {
					}.getType());
				} catch (Exception e) {
				}
				if (null != pointList) {
					mAddPointCommonView.addPoints(pointList);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:// 返回
			giveUp();
			break;

//		case R.id.voice_choice_ibtn: // 选择语音
//			// answerChooseLayout.setVisibility(View.GONE);
//			// 显示录音Dialog
//			if (mAddPointCommonView.frameDelView != null) {
//				showDialog();
//			} else {
//				ToastUtils.show("请先选择您要打点的位置");
//			}
//			break;
//		case R.id.text_choice_ibtn: // 选择文字
//			if (mAddPointCommonView.frameDelView != null) {
//				answerChooseLayout.setVisibility(View.GONE);
//				textInputLayout.setVisibility(View.VISIBLE);
//				textInputET.requestFocus();
//				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//			} else {
//				ToastUtils.show("请先选择您要打点的位置");
//			}
//			break;
//		case R.id.input_sure_btn:
//			String desc = textInputET.getText().toString();
//			if (!TextUtils.isEmpty(desc)) {
//				// 添加一个文字点
//				mPointModel = new CoursePoint();
//				mPointModel.setText(desc);
//				mPointModel.setCoordinate(mCoordinate);
//				mPointModel.setType(GlobalContant.ANSWER_TEXT);
//				// mPointModel.setExseqid(++mExseqid);
//				mPointModel.setRoleid(WeLearnSpUtil.getInstance().getUserRoleId());
//				mPointList.add(mPointModel);
//				VoiceOrTextPoint pointView = mAddPointCommonView.addVoiceOrTextPoint(mPointModel);
//				pointView.setTag(mPointModel);
//
//				pointView.setOnLongClickListener(SingleStudentQAActivity.this);
//
//			}
//			mAddPointCommonView.removeFrameDelView();
//			textInputLayout.setVisibility(View.GONE);
//			textInputET.setText("");
//			// 隐藏键盘
//			imm.hideSoftInputFromWindow(textInputET.getWindowToken(), 0);
//			break;
		case R.id.next_setp_layout:
			submitSinglePoint();
			break;
		case R.id.op_layout:
			break;
		case R.id.avatar_iv_single_stu:
			if (userid > 10) {
				IntentManager.gotoPersonalPage(this, userid, GlobalContant.ROLE_ID_COLLEAGE);
			}
			break;
		}
	}

	// public interface DialogListener {
	// void onConfirmBtnClick(Point mSinglePoint);
	//
	// void onCancelBtnClick();
	// }

	/**
	 * 显示输入语音dialog
	 */
//	private void showDialog() {
//		// if (!isAnswer) {
//		// return;
//		// }
//		View parent = getWindow().getDecorView();
//
//		View transView = new View(this);
//		transView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//		transView.setBackgroundResource(R.color.transparent_bg);
//		bgPopupWindow = new PopupWindow(transView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		bgPopupWindow.setAnimationStyle(R.style.WAlphaAnimation);
//		bgPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
//
//		if (bgPopupWindow.isShowing()) {
//
//			// mSharePopupWindowView = new SharePopupWindowView(this);
//			menuView = new RecordPopupViewOfCourse(this);
//
//			popupWindow = new PopupWindow(menuView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			popupWindow.setAnimationStyle(R.style.SharePopupAnimation);
//			popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
//
//			mPointModel = new CoursePoint();
//			mPointModel.setCoordinate(mCoordinate);
//			mPointModel.setType(GlobalContant.ANSWER_AUDIO);
//			mPointModel.setRoleid(WeLearnSpUtil.getInstance().getUserRoleId());
//
//			menuView.setOnBtnClickListener(mPointModel, new DialogListener() {
//
//				@Override
//				public void onCancelBtnClick() {
//					if (popupWindow != null && popupWindow.isShowing()) {
//						popupWindow.dismiss();
//					}
//					if (bgPopupWindow != null && bgPopupWindow.isShowing()) {
//						bgPopupWindow.dismiss();
//					}
//					// resetView();
//					mAddPointCommonView.removeFrameDelView();
//				}
//
//				@Override
//				public void onConfirmBtnClick(CoursePoint singlePoint) {
//					if (popupWindow != null && popupWindow.isShowing()) {
//						popupWindow.dismiss();
//					}
//					if (bgPopupWindow != null && bgPopupWindow.isShowing()) {
//						bgPopupWindow.dismiss();
//					}
//					mAddPointCommonView.removeFrameDelView();
//					mPointList.add(singlePoint);
//
//					VoiceOrTextPoint pointView = mAddPointCommonView.addVoiceOrTextPoint(singlePoint);
//					pointView.setTag(singlePoint);
//					pointView.setOnLongClickListener(SingleStudentQAActivity.this);
//				}
//			});
//
//		}
//	}

	// @Override
	public void showAddPointBottomContainer(String coordinate) {
		this.mCoordinate = coordinate;
		inputLayout.setResultListener(new ResultListener() {

			@Override
			public void onReturn(int type, String text, String audioPath) {
				mAddPointCommonView.removeFrameDelView();
				mPointModel = new CoursePoint();
				mPointModel.setCoordinate(mCoordinate);
				mPointModel.setType(type);
				mPointModel.setRoleid(MySharePerfenceUtil.getInstance().getUserRoleId());
				mPointModel.setSndurl(audioPath);
				mPointModel.setText(text);
				mPointList.add(mPointModel);

				VoiceOrTextPoint pointView = mAddPointCommonView.addVoiceOrTextPoint(mPointModel);
				pointView.setTag(mPointModel);
				pointView.setOnLongClickListener(SingleStudentQAActivity.this);
				
			}
		});
	}

	@Override
	public void hideAddPointBottomContainer() {
		inputLayout.setVisibility(View.GONE);
	}

	@Override
	public boolean onLongClick(View view) {
		showDialog(R.string.text_dialog_tips_del_carmera_frame, OP_REMOVE_POINT, view);
		return true;
	}

	@Override
	public void onBackPressed() {
		if (GlobalVariable.answertextPopupWindow!=null&&GlobalVariable.answertextPopupWindow.isShowing()) {
    		GlobalVariable.answertextPopupWindow.dismiss();
            return;
        }
		if (mAddPointCommonView.frameDelView != null || inputLayout.getVisibility() == View.VISIBLE) {
			boolean isRemove = inputLayout.onBackPress();
			if (isRemove) {
				mAddPointCommonView.removeFrameDelView();
			}
		}else if (mWelearnDialogBuilder != null && mWelearnDialogBuilder.isShowing()) {
			mWelearnDialogBuilder.dismiss();
		} else {
			giveUp();
		}

	}

	private void showDialog(int msgId, final int op, final Object... params) {
		if (null == mWelearnDialogBuilder) {
			mWelearnDialogBuilder = WelearnDialog.getDialog(this);
		}
		mWelearnDialogBuilder.withMessage(msgId).setOkButtonClick(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mWelearnDialogBuilder.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}

				switch (op) {
				case OP_GIVE_UP:
					finish();
					break;
				case OP_COMMIT:
					submitSinglePoint();
					break;
				case OP_REMOVE_POINT:
					View view = (View) params[0];
					CoursePoint pointModel = (CoursePoint) view.getTag();
					mPointList.remove(pointModel);
					// mExseqid--;
					// mAsnwerContainer.removeView(childConainer);// 移除该图标
					mAddPointCommonView.removeExPoint(view, pointModel.getCoordinate());
					break;
				}
			}
		});
		mWelearnDialogBuilder.show();
	}

	private void giveUp() {
		if (mPointList.size() > 0) {
			showDialog(R.string.teacher_home_work_single_check_give_up_info, OP_GIVE_UP);
		} else {
			finish();
		}
	}

	protected void submitSinglePoint() {
		int size = mPointList.size();
		if (size == 0) {
			ToastUtils.show("请先添加您的文字/语音描述");
			return;
		}
		Map<String, List<File>> files = new HashMap<String, List<File>>();

		showDialog("请稍候");
		List<File> sndFileList = new ArrayList<File>();
		submitModel.setPoint(mPointList);

		for (int i = 0; i < size; i++) {
			CoursePoint singlePoint = mPointList.get(i);
			String sndpath = singlePoint.getSndurl();
			if (!TextUtils.isEmpty(sndpath)) {
				sndFileList.add(new File(sndpath));
			}
		}
		files.put("sndfile", sndFileList);

		try {
			JSONObject data = new JSONObject(new Gson().toJson(submitModel));
			UploadManager.upload(AppConfig.GO_URL + "course/addpoint", RequestParamUtils.getParam(data), files,
					SingleStudentQAActivity.this, true, 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onUploadSuccess(UploadResult result, int index) {
		closeDialog();
		if (result.getCode() == 0) {
			ToastUtils.show("提交成功");
			setResult(RESULT_OK);
			finish();
		} else {
			ToastUtils.show(result.getMsg());
		}

	}

	@Override
	public void onUploadError(String msg, int index) {
		closeDialog();
		ToastUtils.show(msg);
	}

	@Override
	public void onUploadFail(UploadResult result, int index) {
		closeDialog();
		String msg = result.getMsg();
		if (msg != null) {
			ToastUtils.show(msg);
		}

	}

	@Override
	public void onOrientationChanged(int orientation) {
		if (null != mAddPointCommonView) {
			mAddPointCommonView.setOrientation(orientation);
		}
	}
}
