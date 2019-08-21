package com.daxiong.fun.function.homework.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.manager.UploadManager;
import com.daxiong.fun.manager.UploadManager.OnUploadListener;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.dialog.WelearnDialog;
import com.daxiong.fun.function.homework.model.HomeWorkCheckPointModel;
import com.daxiong.fun.function.homework.model.HomeWorkSinglePoint;
import com.daxiong.fun.function.homework.model.UpLoadCheckPointModel;
import com.daxiong.fun.function.homework.model.UpLoadEXPointModel;
import com.daxiong.fun.function.homework.view.AddPointCommonView;
import com.daxiong.fun.function.homework.view.InputExplainView;
import com.daxiong.fun.function.homework.view.InputExplainView.ResultListener;
import com.daxiong.fun.function.homework.view.VoiceOrTextPoint;
import com.daxiong.fun.http.RequestParamUtils;
import com.daxiong.fun.model.UploadResult;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TecHomeWorkSingleCheckActivity extends BaseActivity implements OnClickListener,
		OnLongClickListener, OnUploadListener {

	private static final String TAG = TecHomeWorkSingleCheckActivity.class.getSimpleName();
	private String func ;
	private TextView nextStepTV,tips_tec_single;
	private RelativeLayout nextStepLayout,rl;

	private AddPointCommonView mAddPointCommonView;
//	private Button voiceChooseIBtn;
//	private Button textChooseIBtn;
//	private ImageView infoTurnIBtn;
//	private RelativeLayout textInputLayout;
//	private Button inputOKBtn;
//	private EditText textInputET;
//
//	protected PopupWindow bgPopupWindow;
//	protected PopupWindow popupWindow;
//	protected RecordPopupViewOfHomeWork menuView;
	// protected SharePopupWindowView mSharePopupWindowView;

	private HomeWorkCheckPointModel mHomeWorkCheckPointModel;

//	private LinearLayout helpInfoL;
//
//	private LinearLayout answerChooseLayout;

	private String mCoordinate;
	private int baseExseqid;
	private HomeWorkSinglePoint mPointModel;
	private UpLoadCheckPointModel submitModel;
	private static final int OP_GIVE_UP = 0x1;
	private static final int OP_COMMIT = 0x2;
	private static final int OP_REMOVE_POINT = 0x3;
	protected boolean isTishi = false;
	protected WelearnDialog mWelearnDialogBuilder;
	private InputExplainView inputLayout;
	private InputMethodManager imm;
	private int checkpointid;

//	private int cpseqid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tec_single_check);
//		AndroidBug5497Workaround.assistActivity(this);
//		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		setWelearnTitle(R.string.single_homework_title_text);
//		slideClose = false;// 禁止滑动退出

		initView();
		//getDpi();
	}
	private int getDpi(){
        int dpi = 0;

        Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        DisplayMetrics dm = new DisplayMetrics();

        @SuppressWarnings("rawtypes")

        Class c;

        try {

            c = Class.forName("android.view.Display");

            @SuppressWarnings("unchecked")

            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);

            method.invoke(display, dm);

            dpi = dm.heightPixels;
            if ((dpi - height) > 10) {
                rl.setLayoutParams(
                        new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, height));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;

    }
	@Override
	public void onPause() {
		mAddPointCommonView.stopVoice();
		super.onPause();
		
	}
	
	public void initView() {
		findViewById(R.id.back_layout).setOnClickListener(this);
		findViewById(R.id.back_iv).setVisibility(View.GONE);
		TextView backTV = (TextView) findViewById(R.id.back_tv);
		backTV.setVisibility(View.VISIBLE);
		backTV.setText(R.string.go_back_text);

		rl = (RelativeLayout) findViewById(R.id.rl);
		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		tips_tec_single = (TextView) findViewById(R.id.tips_tec_single);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText("确定");
		nextStepLayout.setOnClickListener(this);

		mAddPointCommonView = (AddPointCommonView) findViewById(R.id.add_point_common_tec_single);
		inputLayout = (InputExplainView) findViewById(R.id.input_container_tec_single);
		inputLayout.setVisibility(View.GONE);
		
		
//		textChooseIBtn = (Button) findViewById(R.id.text_choice_ibtn);
//		infoTurnIBtn = (ImageView) findViewById(R.id.info_turn_ibtn);
//		voiceChooseIBtn = (Button) findViewById(R.id.voice_choice_ibtn);
//
//		answerChooseLayout = (LinearLayout) findViewById(R.id.answer_choice_layout);
//		textInputLayout = (RelativeLayout) findViewById(R.id.text_input_layout);
//		inputOKBtn = (Button) findViewById(R.id.input_sure_btn);
//		textInputET = (EditText) findViewById(R.id.text_input_et);
//
//		textInputET.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
//				int length = str.length();
//				if (length == 0) {
//					inputOKBtn.setText(TecHomeWorkSingleCheckActivity.this.getString(R.string.text_nav_cancel));
//				} else {
//					inputOKBtn.setText(TecHomeWorkSingleCheckActivity.this.getString(R.string.text_nav_submit));
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
//
//		voiceChooseIBtn.setOnClickListener(this);
//		textChooseIBtn.setOnClickListener(this);
//		infoTurnIBtn.setOnClickListener(this);
//		inputOKBtn.setOnClickListener(this);
		singlePointList = new ArrayList<HomeWorkSinglePoint>();

		Intent intent = getIntent();
		if (intent != null) {
			mHomeWorkCheckPointModel = (HomeWorkCheckPointModel) intent
					.getSerializableExtra(HomeWorkCheckPointModel.TAG);
			if (mHomeWorkCheckPointModel != null) {
				submitModel = new UpLoadCheckPointModel();
				
				int roleId = MySharePerfenceUtil.getInstance().getUserRoleId();
				
				checkpointid = mHomeWorkCheckPointModel.getId();
				submitModel.setCheckpointid(checkpointid);
				
				submitModel.setIsright(mHomeWorkCheckPointModel.getIsright());
				submitModel.setCoordinate(mHomeWorkCheckPointModel.getCoordinate());

				if (roleId == GlobalContant.ROLE_ID_STUDENT|roleId == GlobalContant.ROLE_ID_PARENTS) {//学生追问
					func = "reask";
//					infoTurnIBtn.setVisibility(View.GONE);
					mAddPointCommonView.setCheckPointImgAndShowExPoint(mHomeWorkCheckPointModel);
					baseExseqid = mHomeWorkCheckPointModel.getExplianlist().size();
				}else if (roleId == GlobalContant.ROLE_ID_COLLEAGE){//老师端
					
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
//				ToastUtils.show("请先选择您要追问的位置");
//			}
//			break;
//		case R.id.text_choice_ibtn: // 选择文字
//			if (mAddPointCommonView.frameDelView != null) {
//				answerChooseLayout.setVisibility(View.GONE);
//				textInputLayout.setVisibility(View.VISIBLE);
//				textInputET.requestFocus();
//				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//			} else {
//				ToastUtils.show("请先选择您要追问的位置");
//			}
//			break;
//		case R.id.info_turn_ibtn:
//			helpInfoL.setVisibility(helpInfoL.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//			break;
//		case R.id.input_sure_btn:
//			String desc = textInputET.getText().toString();
//			if (!TextUtils.isEmpty(desc)) {
//				// 添加一个文字点
//				mPointModel = new HomeWorkSinglePoint();
//				mPointModel.setText(desc);
//				mPointModel.setCoordinate(mCoordinate);
//				mPointModel.setExplaintype(GlobalContant.ANSWER_TEXT);
//				// mPointModel.setExseqid(++mExseqid);
//				mPointModel.setRoleid(WeLearnSpUtil.getInstance().getUserRoleId());
//				singlePointList.add(mPointModel);
//				VoiceOrTextPoint pointView = mAddPointCommonView.addVoiceOrTextPoint(mPointModel);
//				pointView.setTag(mPointModel);
//
//				pointView.setOnLongClickListener(TecHomeWorkSingleCheckActivity.this);
//
//			}
//			mAddPointCommonView.removeFrameDelView();
//			textInputLayout.setVisibility(View.GONE);
//			textInputET.setText("");
//			// 隐藏键盘
//			imm.hideSoftInputFromWindow(textInputET.getWindowToken(), 0);
//			break;
		case R.id.next_setp_layout:
			showDialog(R.string.teacher_home_work_single_check_confirm, OP_COMMIT);
			break;
		}
	}

	public interface DialogListener {
		void onConfirmBtnClick(HomeWorkSinglePoint mSinglePoint);

		void onCancelBtnClick();
	}

//	private void clearMenuView() {
//		if (menuView != null) {
//			menuView.reset();
//			menuView = null;
//		}
//	}

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
//			menuView = new RecordPopupViewOfHomeWork(this);
//
//			popupWindow = new PopupWindow(menuView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			popupWindow.setAnimationStyle(R.style.SharePopupAnimation);
//			popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
//
//			mPointModel = new HomeWorkSinglePoint();
//			mPointModel.setCoordinate(mCoordinate);
//			mPointModel.setExplaintype(GlobalContant.ANSWER_AUDIO);
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
//				public void onConfirmBtnClick(HomeWorkSinglePoint singlePoint) {
//					if (popupWindow != null && popupWindow.isShowing()) {
//						popupWindow.dismiss();
//					}
//					if (bgPopupWindow != null && bgPopupWindow.isShowing()) {
//						bgPopupWindow.dismiss();
//					}
//					mAddPointCommonView.removeFrameDelView();
//					// singlePoint.setExseqid(++mExseqid);
//					singlePointList.add(singlePoint);
//
//					VoiceOrTextPoint pointView = mAddPointCommonView.addVoiceOrTextPoint(singlePoint);
//					pointView.setTag(singlePoint);
//					pointView.setOnLongClickListener(TecHomeWorkSingleCheckActivity.this);
//				}
//			});
//
//		}
//	}

	// protected void resetView() {
	// isAnswer = false;
	// showBottom();
	// if (mCameraTextChoiceContainer != null) {
	// mCameraTextChoiceContainer.setVisibility(View.GONE);
	// }
	// if (mTextInputContainer != null) {
	// mTextInputContainer.setVisibility(View.GONE);
	// if (mImm != null) {
	// mImm.hideSoftInputFromWindow(mTextInputContainer.getWindowToken(), 0);
	// }
	// }
	// }

	@Override
	public void showAddPointBottomContainer(String coordinate,final int sum) {
		this.mCoordinate = coordinate;
//		answerChooseLayout.setVisibility(View.VISIBLE);
		
		inputLayout.setResultListener(new ResultListener() {

			@Override
			public void onReturn(int type, String text, String audioPath) {
				mAddPointCommonView.removeFrameDelView();
				mPointModel = new HomeWorkSinglePoint();
				mPointModel.setCoordinate(mCoordinate);
				mPointModel.setExplaintype(type);
				mPointModel.setExseqid(sum);
				mPointModel.setRoleid(MySharePerfenceUtil.getInstance().getUserRoleId());
				mPointModel.setSndpath(audioPath);
				mPointModel.setText(text);
				singlePointList.add(mPointModel);

				VoiceOrTextPoint pointView = mAddPointCommonView.addVoiceOrTextPoint(mPointModel);
				pointView.setTag(mPointModel);
				pointView.setOnLongClickListener(TecHomeWorkSingleCheckActivity.this);
				if(!isTishi){
					isTishi=true;
					tips_tec_single.setText("点击右上角“确定”，成功发布追问");	
//				final CustomTipDialogWithOneButton tipDialog = new CustomTipDialogWithOneButton(TecHomeWorkSingleCheckActivity.this, "",
//						"输入追问内容之后，记得点击右上角往完成按钮，才能追问成功哦！", "知道了");
//				final TextView positiveBtn = tipDialog.getPositiveButton();
//				
//				tipDialog.setOnPositiveListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						tipDialog.dismiss();
//
//					}
//				});
//				tipDialog.show();
				}
			}
		});
	}

	
	
	@Override
	public void hideAddPointBottomContainer() {
//		answerChooseLayout.setVisibility(View.GONE);
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
		} else if (mWelearnDialogBuilder != null && mWelearnDialogBuilder.isShowing()) {
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
					Intent intent = new Intent();
					intent.putExtra("isSubmit", false);
					setResult(RESULT_OK, intent);
					finish();
					break;
				case OP_COMMIT:
					submitSinglePoint();
					break;
				case OP_REMOVE_POINT:
					View view = (View) params[0];
					HomeWorkSinglePoint pointModel = (HomeWorkSinglePoint) view.getTag();
					singlePointList.remove(pointModel);
					// mExseqid--;
					// mAsnwerContainer.removeView(childConainer);// 移除该图标
					mAddPointCommonView.removeExPoint(view, pointModel.getCoordinate());
					break;
				}
			}
		});
		mWelearnDialogBuilder.show();
	}

	private void giveUp(){
		if (singlePointList.size() > 0) {
			showDialog(R.string.teacher_home_work_single_check_give_up_info, OP_GIVE_UP);
		}else {
			Intent intent = new Intent();
			intent.putExtra("isSubmit", false);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
	
	protected void submitSinglePoint() {
		int size = singlePointList.size();
		if (size == 0) {
			ToastUtils.show("请先添加您的文字/语音描述");
			return;
		}
		Map<String, List<File>> files = new HashMap<String, List<File>>();
		List<File> sndFileList = new ArrayList<File>();
		ArrayList<UpLoadEXPointModel>upLoadExplainList = new ArrayList<UpLoadEXPointModel>();
		submitModel.setExplainlist(upLoadExplainList);
		UpLoadEXPointModel exPointModel;
		for (int i = 0; i < size; i++) {
			HomeWorkSinglePoint singlePoint = singlePointList.get(i);
			String sndpath = singlePoint.getSndpath();
			
			int explaintype = singlePoint.getExplaintype();
			if (!TextUtils.isEmpty(sndpath) &&  explaintype == GlobalContant.ANSWER_AUDIO) {
				sndFileList.add(new File(sndpath));
			}
			
			exPointModel = new UpLoadEXPointModel();
			exPointModel.setCoordinate(singlePoint.getCoordinate());
			exPointModel.setExplaintype(explaintype);
			int exseqid = baseExseqid + i + 1;
			exPointModel.setExseqid(exseqid);
			singlePoint.setExseqid(exseqid);
			String text = singlePoint.getText();
			if (text != null) {
				exPointModel.setText(text);
			}
			upLoadExplainList.add(exPointModel);
		}
		files.put("sndfile", sndFileList);
		showDialog("提交中...");
		try {
			JSONObject data = new JSONObject(new Gson().toJson(submitModel));
			UploadManager.upload(AppConfig.GO_URL +"homework/" + func, RequestParamUtils.getParam(data), files,
					TecHomeWorkSingleCheckActivity.this, true, 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onUploadSuccess(UploadResult result, int index) {
		if (result.getCode() == 0) {
			uMengEvent("homework_reask");
//			 final CustomTipDialogWithNoButton customTipDialogWithNoButton = new CustomTipDialogWithNoButton(TecHomeWorkSingleCheckActivity.this);
//			
//			 customTipDialogWithNoButton.show();
//			 MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
//				
//				@Override
//				public void run() {
//					customTipDialogWithNoButton.dismiss();
//					Intent intent = new Intent();
//					intent.putExtra("isSubmit", true);
//					intent.putExtra(HomeWorkSinglePoint.TAG, singlePointList);
//					setResult(RESULT_OK, intent);
//					finish();	
//					
//				}
//			},1000);
			 Intent intent = new Intent();
				intent.putExtra("isSubmit", true);
				intent.putExtra(HomeWorkSinglePoint.TAG, singlePointList);
				setResult(RESULT_OK, intent);
				finish();	
				
		}
		
	}

	@Override
	public void onUploadError(String msg, int index) {
		if (!TextUtils.isEmpty(msg)) {
			ToastUtils.show(msg);
		}else{
			ToastUtils.show("提交失败");
		}
	}

	@Override
	public void onUploadFail(UploadResult result, int index) {
		String msg = result.getMsg();
		if (!TextUtils.isEmpty(msg)) {
			ToastUtils.show(msg);
		}else{
			ToastUtils.show("提交失败");
		}
		
	}
}
