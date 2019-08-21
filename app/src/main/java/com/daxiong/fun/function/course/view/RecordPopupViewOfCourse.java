package com.daxiong.fun.function.course.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.function.course.SingleStudentQAActivity.DialogListener;
import com.daxiong.fun.function.course.model.CoursePoint;
import com.daxiong.fun.util.MD5Util;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.MediaUtil.RecordCallback;
import com.daxiong.fun.util.MediaUtil.ResetImageSourceCallback;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;

public class RecordPopupViewOfCourse extends FrameLayout implements OnClickListener, OnTouchListener {

	private TextView mAudioCancel;
	private FrameLayout mAddAnswerVoiceContainer;
	private ProgressBar mAudioLoadProgressBar;
	private PublishRecordCallback mCallback;
	private String mAudioPath;
	private TextView mRecordBtn;
	private TextView mAddVoiceAnswer;
	private ImageView mPlayAnimation;
	private BaseActivity mActivity;
	private double voiceValue = 0.0;
	private TextView mAudioConfirm;
	
//	private WelearnDialogBuilder mWelearnDialogBuilder;
	
	public static final String VOICE_FILE_NAME = "voice";
	
	public RecordPopupViewOfCourse(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (context instanceof BaseActivity) {
			mActivity = (BaseActivity) context;
		} 
		
		setupViews();
	}

	public RecordPopupViewOfCourse(Context context) {
		super(context);
		if (context instanceof BaseActivity) {
			mActivity = (BaseActivity) context;
		} 
		setupViews();
	}

	private void setupViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.voice_homework_popup_window, null);
		mAudioCancel = (TextView) view.findViewById(R.id.audio_cancel);
		mAddAnswerVoiceContainer = (FrameLayout) view.findViewById(R.id.add_voice_answer_container);
		mRecordBtn = (TextView) view.findViewById(R.id.audio_record);
		mAddVoiceAnswer = (TextView) view.findViewById(R.id.add_voice_answer);
		mPlayAnimation = (ImageView) view.findViewById(R.id.icon_answer_audio);
		mAudioLoadProgressBar = (ProgressBar) view.findViewById(R.id.audio_load_progressbar);
		mAudioConfirm = (TextView) view.findViewById(R.id.audio_confirm);
		addView(view);
		
		mAudioCancel.setOnClickListener(this);
		mRecordBtn.setOnClickListener(this);
		mAudioConfirm.setOnClickListener(this);
		mAddAnswerVoiceContainer.setOnTouchListener(this);
		mAddAnswerVoiceContainer.setOnClickListener(this);
		
		mCallback = new PublishRecordCallback();
	}

	private DialogListener mListener;
	private CoursePoint mSinglePoint;
	
/**
 * 设置点击监听
 * @param listener
 */
	public void setOnBtnClickListener(CoursePoint singlePoint ,DialogListener listener) {
		this.mSinglePoint = singlePoint;
		this.mListener = listener;
	}
	
	public void reset() {
		MyApplication.animationDrawables.clear();
		MyApplication.anmimationPlayViews.clear();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.audio_cancel:
			removePoint();
			stopAllPlay();
			mListener.onCancelBtnClick();
			break;
		case R.id.add_voice_answer_container:
			playVoice();
			break;
		case R.id.audio_confirm:
			if (!isRecording) {
				if (!WeLearnFileUtil.isFileExist(mAudioPath)) {
					ToastUtils.show(R.string.text_input_voice_explain);
				} else {
					stopAllPlay();
					mSinglePoint.setSndurl(mAudioPath);
//					mSinglePoint.setAudioTime(mRecordTime);
					mListener.onConfirmBtnClick(mSinglePoint);
				}
			}
			break;
		case R.id.audio_record://重录
			stopAllPlay();
			mAddAnswerVoiceContainer.setOnClickListener(null);
			mAddAnswerVoiceContainer.setOnTouchListener(RecordPopupViewOfCourse.this);
			mRecordBtn.setVisibility(GONE);
			mAddVoiceAnswer.setVisibility(View.VISIBLE);
			mPlayAnimation.setVisibility(View.GONE);
			removePoint();
			break;
		}
	}
	
	public void removePoint() {
		if (WeLearnFileUtil.deleteFile(mAudioPath)) {
			mAudioPath = null;
		}
	}
	
	/**
	 * 重置播放动画
	 * @param iconVoiceView
	 */
	private void resetAnimationPlay(ImageView iconVoiceView) {
		for (ImageView currentView: MyApplication.anmimationPlayViews) {
			if (currentView != iconVoiceView) {
				currentView.setImageResource(R.drawable.ic_play2);
			}
		}
	}
	
	/**
	 * 停止其他播放
	 */
	private void stopPlay() {
		for (AnimationDrawable animation : MyApplication.animationDrawables) {
			MediaUtil.getInstance(false).stopVoice(animation);
		}
	}
	
	private void stopAllPlay() {
		stopPlay();
		MediaUtil.getInstance(false).stopVoice(mAnimationDrawable);
	}
	
	private AnimationDrawable mAnimationDrawable;
	
	private void playVoice() {
		mPlayAnimation.setImageResource(R.drawable.play_animation);
		mAnimationDrawable = (AnimationDrawable) mPlayAnimation.getDrawable();
		if (TextUtils.isEmpty(mAudioPath)) {
			ToastUtils.show(R.string.text_no_audio_play);
			return;
		}
		MediaUtil.getInstance(false).playVoice(true, mAudioPath, mAnimationDrawable, new ResetImageSourceCallback() {
			
			@Override
			public void reset() {
				mPlayAnimation.setImageResource(R.drawable.ic_play2);
			}
			
			@Override
			public void playAnimation() {
				mPlayAnimation.setVisibility(View.VISIBLE);
				mAudioLoadProgressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void beforePlay() {
				mAudioLoadProgressBar.setVisibility(GONE);
				mPlayAnimation.setVisibility(VISIBLE);
			}
		}, null);
	}

	private float mRecordTime;
	
	private class PublishRecordCallback implements RecordCallback {


		@Override
		public void onAfterRecord(float recordTime) {
			mAddAnswerVoiceContainer.setOnClickListener(RecordPopupViewOfCourse.this);
			mAddAnswerVoiceContainer.setOnTouchListener(null);
			mRecordBtn.setVisibility(View.VISIBLE);//显示重录按钮
			mAddVoiceAnswer.setVisibility(View.GONE);
			mPlayAnimation.setVisibility(View.VISIBLE);
			mAudioPath = WeLearnFileUtil.VOICE_PATH + mAudioName + ".amr";
			RecordPopupViewOfCourse.this.mRecordTime = recordTime;
//			point.setAudioPath(mAudioPath);
//			WApplication.coordinateAnswerIconSet.add(point);
			isRecording = false;
		}

	}
	private boolean isRecording = false;
	private String mAudioName;
//	private Handler mHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case 222:
//				String audioName = (String) msg.obj;
//				isRecording = true;
//				WeLearnMediaUtil.getInstance(false).record(audioName, mCallback, mActivity);
//				break;
//			}
//		};
//	};
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.add_voice_answer_container) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//UUID.fromString(VOICE_FILE_NAME + point).toString();
				mAudioName = MD5Util.getMD5String(VOICE_FILE_NAME + System.currentTimeMillis());
				isRecording = true;
				MediaUtil.getInstance(false).record(mAudioName, mCallback, mActivity);
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				MediaUtil.getInstance(false).stopRecord(voiceValue, mCallback);
				break;
			}
		}
		return true;
	}
}
