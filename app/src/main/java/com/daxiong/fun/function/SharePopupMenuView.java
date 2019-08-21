package com.daxiong.fun.function;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.dialog.WelearnDialog;
import com.daxiong.fun.function.question.PayAnswerAppendAskActivity;
import com.daxiong.fun.function.question.PayAnswerPhotoViewActivity;
import com.daxiong.fun.function.question.PayAnswerPhotoViewFragment.ResetView;
import com.daxiong.fun.model.ExplainPoint;
import com.daxiong.fun.util.MD5Util;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.MediaUtil.RecordCallback;
import com.daxiong.fun.util.MediaUtil.ResetImageSourceCallback;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;

public class SharePopupMenuView extends FrameLayout implements OnClickListener, OnTouchListener {

	private TextView mAudioCancel;
	private RelativeLayout mAsnwerContainer;
	private CameraFrameWithDel mImg;
	private ResetView mResetCallback;
	private FrameLayout mAddAnswerVoiceContainer;
	private ProgressBar mAudioLoadProgressBar;
	private ExplainPoint point;
	private PublishRecordCallback mCallback;
	private String mAudioPath;
	private TextView mRecordBtn;
	private TextView mAddVoiceAnswer;
	private ImageView mPlayAnimation;
	private BaseActivity mActivity;
	private double voiceValue = 0.0;
	private TextView mAudioConfirm;
	
	private WelearnDialog mWelearnDialogBuilder;
	
	public static final String VOICE_FILE_NAME = "voice";
	
	public SharePopupMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (context instanceof PayAnswerPhotoViewActivity) {
			mActivity = (PayAnswerPhotoViewActivity) context;
		} else if (context instanceof PayAnswerAppendAskActivity) {
			mActivity = (PayAnswerAppendAskActivity) context;
		}
		
		setupViews();
	}

	public SharePopupMenuView(Context context) {
		super(context);
		System.out.println("context" + context);
		mActivity = (PayAnswerPhotoViewActivity) context;
		setupViews();
	}

	private void setupViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.nav_share_popup_menu, null);
		mAudioCancel = (TextView) view.findViewById(R.id.audio_cancel);
		mAddAnswerVoiceContainer = (FrameLayout) view.findViewById(R.id.add_voice_answer_container);
		mRecordBtn = (TextView) view.findViewById(R.id.audio_record);
		mAddVoiceAnswer = (TextView) view.findViewById(R.id.add_voice_answer);
		mPlayAnimation = (ImageView) view.findViewById(R.id.icon_answer_audio);
		mAudioLoadProgressBar = (ProgressBar) view.findViewById(R.id.audio_load_progressbar);
		mAudioConfirm = (TextView) view.findViewById(R.id.audio_confirm);
		addView(view);
		
		mRecordBtn.setOnClickListener(this);
		mAudioConfirm.setOnClickListener(this);
		mAddAnswerVoiceContainer.setOnTouchListener(this);
		mAddAnswerVoiceContainer.setOnClickListener(this);
		
		mCallback = new PublishRecordCallback();
	}

	public void enableCancelBtn(RelativeLayout answerContainer, CameraFrameWithDel img, ResetView resetCallback) {
		this.mAsnwerContainer = answerContainer;
		this.mImg = img;
		this.mResetCallback = resetCallback;
		mAudioCancel.setOnClickListener(this);
	}
	
	private View view;
	/**
	 * 当确定按钮被点击时
	 * @param answerContainern
	 * @param img
	 */
	public void enableConfirmBtn(RelativeLayout answerContainern, CameraFrameWithDel img, ExplainPoint point, View view) {
		this.mAsnwerContainer = answerContainern;
		this.mImg = img;
		this.point = point;
		this.view = view;
		mAudioConfirm.setOnClickListener(this);
	}
	
	private void removeImgOnTheConainer() {
		if (mAsnwerContainer != null && mImg != null) {
			mAsnwerContainer.removeView(mImg);
		}
	}
	
	private void clear() {
		removePoint();
		removeImgOnTheConainer();
		this.mResetCallback.reset(true);
	}
	
	public void reset() {
		MyApplication.animationDrawables.clear();
		MyApplication.anmimationPlayViews.clear();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.audio_cancel:
			clear();
			removePoint();
			break;
		case R.id.add_voice_answer_container:
			playVoice();
			break;
		case R.id.audio_confirm:
			if (!isRecording) {
				if (!WeLearnFileUtil.isFileExist(mAudioPath)) {
					ToastUtils.show(R.string.text_input_voice_explain);
				} else {
					removeImgOnTheConainer();
					
					RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, 
							android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
					
					float x = point.getX();
					float y = point.getY();
					
					relativeParams.leftMargin = (int) (x) ;
					relativeParams.topMargin = (int) (y);
					
					final CameraChoiceIconWithDel childConainer = new CameraChoiceIconWithDel(getContext(),point.getSubtype());
					childConainer.setLayoutParams(relativeParams);
					mAsnwerContainer.addView(childConainer);
					
					final ImageView iconVoiceView = childConainer.getIcView();
					childConainer.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							iconVoiceView.setImageResource(R.drawable.play_animation);
							mAnimationDrawable = (AnimationDrawable) iconVoiceView.getDrawable();
							MyApplication.animationDrawables.add(mAnimationDrawable);
							MyApplication.anmimationPlayViews.add(iconVoiceView);
							stopPlay();
							MediaUtil.getInstance(false).playVoice(true, mAudioPath, mAnimationDrawable, new ResetImageSourceCallback() {
								
								@Override
								public void reset() {
									iconVoiceView.setImageResource(R.drawable.ic_play2);
								}
								
								@Override
								public void playAnimation() {}
								
								@Override
								public void beforePlay() {
									resetAnimationPlay(iconVoiceView);
								}
							}, null);
						}
					});
					childConainer.setOnLongClickListener(new OnLongClickListener() {
						
						@Override
						public boolean onLongClick(View v) {
							if (null == mWelearnDialogBuilder) {
								mWelearnDialogBuilder = WelearnDialog.getDialog(mActivity);
							}
							mWelearnDialogBuilder.withMessage(R.string.text_dialog_tips_del_carmera_frame)
									.setOkButtonClick(new View.OnClickListener() {
										@Override
										public void onClick(View v) {

											try {
												mWelearnDialogBuilder.dismiss();
											} catch (Exception e) {
												e.printStackTrace();
											}

											removePoint();
											mAsnwerContainer.removeView(childConainer);// 移除该图标
										}
									});
							mWelearnDialogBuilder.show();
							
							return true;
						}
					});
					this.mResetCallback.reset(false);
				}
			}
			break;
		case R.id.audio_record://重录
			stopAllPlay();
			mAddAnswerVoiceContainer.setOnClickListener(null);
			mAddAnswerVoiceContainer.setOnTouchListener(SharePopupMenuView.this);
			mRecordBtn.setVisibility(GONE);
			mAddVoiceAnswer.setVisibility(View.VISIBLE);
			mPlayAnimation.setVisibility(View.GONE);
			removePoint();
			break;
		}
	}
	
	public void removePoint() {
		if (WeLearnFileUtil.deleteFile(mAudioPath)) {
			MyApplication.coordinateAnswerIconSet.remove(point);
			if (MyApplication.coordinateAnswerIconSet.size() == 0) {
				if (this.mResetCallback!=null) {
					this.mResetCallback.showRotateBtn();
				}
			}
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

	private class PublishRecordCallback implements RecordCallback {

		@Override
		public void onAfterRecord(float recordTime) {
			mAddAnswerVoiceContainer.setOnClickListener(SharePopupMenuView.this);
			mAddAnswerVoiceContainer.setOnTouchListener(null);
			mRecordBtn.setVisibility(View.VISIBLE);//显示重录按钮
			mAddVoiceAnswer.setVisibility(View.GONE);
			mPlayAnimation.setVisibility(View.VISIBLE);
			mAudioPath = WeLearnFileUtil.VOICE_PATH + MD5Util.getMD5String(VOICE_FILE_NAME + point) + ".amr";
			point.setAudioPath(mAudioPath);
			MyApplication.coordinateAnswerIconSet.add(point);
			isRecording = false;
		}

	}
	private boolean isRecording = false;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 222:
				String audioName = (String) msg.obj;
				isRecording = true;
				MediaUtil.getInstance(false).record(audioName, mCallback, mActivity);
				break;
			}
		};
	};
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.add_voice_answer_container) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//UUID.fromString(VOICE_FILE_NAME + point).toString();
				final String audioName = MD5Util.getMD5String(VOICE_FILE_NAME + point);
				Message msg = mHandler.obtainMessage();
				msg.what = 222;
				msg.obj = audioName;
				mHandler.sendMessage(msg);
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
