package com.daxiong.fun.function.homework.view;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.util.MD5Util;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.MediaUtil.MaxRecordTime;
import com.daxiong.fun.util.MediaUtil.RecordCallback;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;

public class InputExplainView extends FrameLayout implements OnClickListener{
	public static final String TAG = InputExplainView.class.getSimpleName();
	public static final String VOICE_FILE_NAME = "voice";
	private BaseActivity mActivity;
	private View recordingTv;
	private View choiceTextBtn;
	private Button recordBtn;
	private View choiceVoiceBtn;
	private View textInputView;
	private Button inputSureBtn;
	private EditText texeInputEt;
	private ResultListener mListener ;
	public boolean isRecording;
	private double voiceValue = 0.0;
	private String mAudioName;
	private String mAudioPath;
	protected float mRecordTime;
	private Handler mhandler = new Handler() {
		// private String openId;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case 13:
				float	recordTime=(float) msg.obj;
				recordingTv.setVisibility(View.GONE);
				recordBtn.setText(mActivity.getString(R.string.click_to_record_text));

				blankView.setVisibility(View.GONE);
				mAudioPath = WeLearnFileUtil.VOICE_PATH + mAudioName + ".amr";
				InputExplainView.this.mRecordTime = recordTime;
				isRecording = false;
				
				returnResult(GlobalContant.ANSWER_AUDIO, "", mAudioPath);
				break;
			}

		};
	};
	private RecordCallback mCallback = new RecordCallback() {
		

		@Override
		public void onAfterRecord(float recordTime) {
			
			Message obtain = Message.obtain();
			obtain.what=13;
			obtain.obj=recordTime;
			mhandler.sendMessage(obtain);
			
		}
	}; 

	private InputMethodManager imm;
	private View blankView;	public interface ResultListener{
		/**
		 * 
		 * @param type GlobalContant.ANSWER_AUDIO 声音  ,  GlobalContant.ANSWER_TEXT 文字
		 * @param text  文字内容 
		 * @param audioPath 语音地址
		 */
		void onReturn(int type , String text , String audioPath);
	}
	public InputExplainView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mActivity = (BaseActivity) context;
		setUpViews();
	}

	public InputExplainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mActivity = (BaseActivity) context;
		setUpViews();
	}

	public InputExplainView(Context context) {
		super(context);
		this.mActivity = (BaseActivity) context;
		setUpViews();
	}
	
	private void setUpViews() {
		View view = LayoutInflater.from(mActivity).inflate(R.layout.add_explain_bottom, null);
		imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		blankView = view.findViewById(R.id.blank_layout_input_view);
		blankView.setOnClickListener(this);
		blankView.setVisibility(View.GONE);
		recordingTv = view.findViewById(R.id.recording_tv_input_view);
		choiceTextBtn = view.findViewById(R.id.choice_text_btn_input_view);
		recordBtn = (Button) view.findViewById(R.id.record_btn_input_view);
		textInputView = view.findViewById(R.id.text_input_layout_input_view);
		choiceVoiceBtn = view.findViewById(R.id.choice_voice_btn_input_view);
		inputSureBtn = (Button) view.findViewById(R.id.input_sure_btn_input_view);
		
		choiceTextBtn.setOnClickListener(this);
		recordBtn.setOnClickListener(this);
		choiceVoiceBtn.setOnClickListener(this);
		inputSureBtn.setOnClickListener(this);
		
		texeInputEt = (EditText) view.findViewById(R.id.text_input_et_input_view);
		texeInputEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
				int length = str.length();
				if (length == 0) {
					inputSureBtn.setText(mActivity.getString(R.string.text_nav_cancel));
				} else {
					inputSureBtn.setText(mActivity.getString(R.string.text_nav_submit));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
		
		
		addView(view);
	}
	public void setResultListener( ResultListener listener){
		this.mListener = listener;
		setVisibility(View.VISIBLE);
	}
	
	private void returnResult(int type , String text , String audioPath){
		setVisibility(View.GONE);
		mListener.onReturn(type ,text , audioPath );
		texeInputEt.setText("");
		textInputView.setVisibility(View.GONE);
	}
	
	/**
	 * 正在录音的话返回false   View不隐藏
	 * @return
	 */
	public boolean onBackPress(){
		if (isRecording) {
			ToastUtils.show("正在录音中!请先停止");
		}else {
			setVisibility(View.GONE);
		}
		return !isRecording;
	}
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.choice_text_btn_input_view:
			if (!isRecording) {
				
			texeInputEt.requestFocus();
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			textInputView.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.record_btn_input_view://点击开始录音
			int visibility = textInputView.getVisibility();
			String textStr = recordBtn.getText().toString().trim();
			if (visibility != View.VISIBLE) {
				MediaUtil.getInstance(false).stopVoice(null);
				if (textStr.equals(mActivity.getString(R.string.click_to_record_text))) {//点击开始录音
					blankView.setVisibility(View.VISIBLE);
					recordingTv.setVisibility(View.VISIBLE);
					recordBtn.setText(mActivity.getString(R.string.click_to_stop_text));
					mAudioName = MD5Util.getMD5String(VOICE_FILE_NAME + System.currentTimeMillis());
					isRecording = true;
					MediaUtil.getInstance(false).record(mAudioName,mCallback, mActivity,MaxRecordTime.MAX_OF_COURSE);
				}else if (textStr.equals(mActivity.getString(R.string.click_to_stop_text))) {
					stopRecording();
				}
			}
			break;
		case R.id.choice_voice_btn_input_view:
			textInputView.setVisibility(View.GONE);
			// 隐藏键盘
			imm.hideSoftInputFromWindow(texeInputEt.getWindowToken(), 0);
			break;
		case R.id.input_sure_btn_input_view:
			textStr = inputSureBtn.getText().toString().trim();
			imm.hideSoftInputFromWindow(texeInputEt.getWindowToken(), 0);
			if (textStr.equals(mActivity.getString(R.string.text_nav_cancel))) {//取消
				mActivity.onBackPressed();
			}else if (textStr.equals(mActivity.getString(R.string.text_nav_submit))) {//确定
				// 隐藏键盘
				returnResult(GlobalContant.ANSWER_TEXT, texeInputEt.getText().toString().trim(), "");
			}

			break;

		default:
			break;
		}
	}

	private void stopRecording() {
		recordBtn.setText(mActivity.getString(R.string.saving_record_text));
		MediaUtil.getInstance(false).stopRecord(voiceValue, mCallback);
		
	}

	
}
