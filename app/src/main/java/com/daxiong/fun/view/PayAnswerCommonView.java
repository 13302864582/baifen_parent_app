package com.daxiong.fun.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.NetworkImageView.OnImageLoadListener;
import com.daxiong.fun.R;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.function.question.PayAnswerQuestionDetailActivity;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.QuestionModelGson;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.MediaUtil.ResetImageSourceCallback;

public class PayAnswerCommonView extends FrameLayout implements OnClickListener {

	private NetworkImageView mPayAnswerUserAvatar;
	private TextView mPayAnswerNick;
	private TextView mPayAnswerRole;
	private TextView mPayAnswerCredit;
	private NetworkImageView mPayAnswerQuestionImg;
	private ImageView mPayAnswerVoicePlay;
	private TextView mPayAnswerGrade;
	private TextView mPayAnswerSubject;
	private TextView mPayAnswerReword;
	private ImageView mPayAnswerPlay;
	private TextView mAnswerDesc;
	private FrameLayout mAudioAnswerContainer;
	private AnimationDrawable animationDrawable;
	// private TextView mQuestionIdView;

	private String mAudioPath;
	private String mImgUrl;
	private QuestionModelGson mQuestionModelGson;

	private Activity mActivity;
	private ImageView vipTag_iv;
	
//	private boolean isShowQuestionImage = false;
	private int isShowQuestionImage = 0;

	private int avatarSize;
	
	private static final String TAG = PayAnswerCommonView.class.getSimpleName();

	public PayAnswerCommonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setUpViews();
		this.mActivity = (Activity) context;
	}

	public PayAnswerCommonView(Context context) {
		super(context);
		setUpViews();
	}

	@SuppressLint("InflateParams")
	private void setUpViews() {
		View payAnswerView = LayoutInflater.from(getContext()).inflate(R.layout.pay_answer_common, null);
		mPayAnswerUserAvatar = (NetworkImageView) payAnswerView.findViewById(R.id.pay_answer_user_avatar);
		avatarSize = getResources().getDimensionPixelSize(R.dimen.pay_answer_user_avatar_size);
		mPayAnswerNick = (TextView) payAnswerView.findViewById(R.id.pay_answer_nick);
		mPayAnswerRole = (TextView) payAnswerView.findViewById(R.id.pay_answer_role);
		vipTag_iv = (ImageView) payAnswerView.findViewById(R.id.pay_answer_role_viptag);
		mPayAnswerCredit = (TextView) payAnswerView.findViewById(R.id.pay_answer_credit);
		mPayAnswerQuestionImg = (NetworkImageView) payAnswerView.findViewById(R.id.pay_answer_question_img);
		mPayAnswerVoicePlay = (ImageView) payAnswerView.findViewById(R.id.pay_answer_voice_play);
		mPayAnswerGrade = (TextView) payAnswerView.findViewById(R.id.pay_answer_grade);
		mPayAnswerSubject = (TextView) payAnswerView.findViewById(R.id.pay_answer_subject);
		mPayAnswerReword = (TextView) payAnswerView.findViewById(R.id.pay_answer_reward_val);
		mPayAnswerPlay = (ImageView) payAnswerView.findViewById(R.id.pay_answer_ic_play);
		mAnswerDesc = (TextView) payAnswerView.findViewById(R.id.pay_answer_desc);
		mAudioAnswerContainer = (FrameLayout) payAnswerView.findViewById(R.id.pay_answer_voice_play_container);
		// mQuestionIdView = (TextView)
		// payAnswerView.findViewById(R.id.question_id);

		mPayAnswerNick.setOnClickListener(this);
		mPayAnswerUserAvatar.setOnClickListener(this);
		mPayAnswerQuestionImg.setOnClickListener(this);
		mPayAnswerVoicePlay.setOnClickListener(this);
		addView(payAnswerView);
	}

	public void showData(QuestionModelGson mObj) {
		mPayAnswerNick.setClickable(true);
		mPayAnswerUserAvatar.setClickable(true);
		mPayAnswerQuestionImg.setClickable(true);
		mPayAnswerVoicePlay.setClickable(true);

		LogUtils.i(TAG, mObj.toString());
		mQuestionModelGson = mObj;
		// mQuestionIdView.setText("问题id: " + mObj.getQuestion_id());
		mImgUrl = mObj.getImgurl();
		loadQImg();
		//ImageLoader.ajaxQuestionPic(mObj.getImgurl(), mPayAnswerQuestionImg);
		ImageLoader.getInstance().loadImageWithDefaultAvatar(mObj.getAvatar(), mPayAnswerUserAvatar, avatarSize,
				avatarSize / 10);
		if (mObj.getSupervip() > 0) {
			vipTag_iv.setVisibility(View.VISIBLE);
			mPayAnswerRole.setVisibility(View.INVISIBLE);
		} else {
			vipTag_iv.setVisibility(View.INVISIBLE);
			if (mObj.isNewUser()) {
				mPayAnswerRole.setVisibility(View.VISIBLE);
			} else {
				mPayAnswerRole.setVisibility(View.INVISIBLE);
			}
		}
		mPayAnswerNick.setText(mObj.getName());
		mPayAnswerCredit.setText("信用 " + mObj.getCredit());
		String sdnUrl = mObj.getSndurl();
		if (TextUtils.isEmpty(sdnUrl)) {
			mAudioAnswerContainer.setVisibility(View.GONE);
			mAnswerDesc.setVisibility(View.VISIBLE);
			mAnswerDesc.setText(mObj.getDescription());
		} else {
			mAudioPath = mObj.getSndurl();
			mAudioAnswerContainer.setVisibility(View.VISIBLE);
			mAnswerDesc.setVisibility(View.GONE);
		}
		mPayAnswerGrade.setText(mObj.getGrade());
		mPayAnswerSubject.setText(mObj.getSubject());
		mPayAnswerReword.setText("￥ " + mObj.getBounty());
	}

	private void loadQImg() {
		isShowQuestionImage = 0;//0是加载中
		ImageLoader.getInstance().resetUrl(mPayAnswerQuestionImg);
		mPayAnswerQuestionImg.setImageResource(R.drawable.loading);
		ImageLoader.getInstance().loadImage(mImgUrl, mPayAnswerQuestionImg, R.drawable.loading, R.drawable.retry, new OnImageLoadListener() {
			@Override
			public void onSuccess(ImageContainer arg0) {
				if (null != arg0.getBitmap()) {
					// mPayAnswerQuestionImg.setImageBitmap(arg0.getBitmap());
				}
				//isShowQuestionImage = true;
				isShowQuestionImage = 1;//1是成功
			}

			@Override
			public void onFailed(VolleyError arg0) {
	
//				isShowQuestionImage = false;
				isShowQuestionImage = 2;//2是失败
			}
		});
	}

	/**
	 * 换题发现为空
	 */
	public void showDataNullQuestion() {
		ImageLoader.getInstance().loadImage(null, mPayAnswerUserAvatar, R.drawable.default_contact_image);
		ImageLoader.getInstance().loadImage(null, mPayAnswerQuestionImg, R.drawable.ic_no_question);
		
//		mPayAnswerUserAvatar.setImageResource(R.drawable.default_contact_image);
//		mPayAnswerQuestionImg.setImageResource(R.drawable.ic_no_question);
		//mPayAnswerQuestionImg
		mPayAnswerRole.setVisibility(View.INVISIBLE);
		vipTag_iv.setVisibility(View.INVISIBLE);
		mPayAnswerNick.setText("");
		mPayAnswerCredit.setVisibility(View.INVISIBLE);
		mAudioAnswerContainer.setVisibility(View.GONE);
		mAnswerDesc.setVisibility(View.GONE);
		mPayAnswerGrade.setText("");
		mPayAnswerSubject.setText("");
		mPayAnswerReword.setText("");

		mPayAnswerNick.setClickable(false);
		mPayAnswerUserAvatar.setClickable(false);
		mPayAnswerQuestionImg.setClickable(false);
		mPayAnswerVoicePlay.setClickable(false);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay_answer_voice_play:
			if (!TextUtils.isEmpty(mAudioPath)) {
				mPayAnswerPlay.setImageResource(R.drawable.play_animation);
				animationDrawable = (AnimationDrawable) mPayAnswerPlay.getDrawable();

				MediaUtil.getInstance(false).playVoice(false, mAudioPath, animationDrawable,
						new ResetImageSourceCallback() {

							@Override
							public void reset() {
								mPayAnswerPlay.setImageResource(R.drawable.ic_play2);
							}

							@Override
							public void playAnimation() {
							}

							@Override
							public void beforePlay() {
							}
						}, null);
			}
			break;
		case R.id.pay_answer_question_img:
			if(isShowQuestionImage == 1){
				if (!TextUtils.isEmpty(mImgUrl)) {//1是成功
					Bundle data = new Bundle();
					data.putString(PayAnswerQuestionDetailActivity.IMG_URL, mImgUrl);
					IntentManager.goToQuestionDetailPicView(mActivity, data);
				}
			} else if(isShowQuestionImage == 2){//2是失败
				loadQImg();
			}
			break;
		case R.id.pay_answer_nick:
		case R.id.pay_answer_user_avatar:
			if (mQuestionModelGson == null || mQuestionModelGson.getStudent_id() == 0
					|| mQuestionModelGson.getRoleid() == 2) {
				break;
			}
			IntentManager.gotoPersonalPage(mActivity, mQuestionModelGson.getStudent_id(),
					mQuestionModelGson.getRoleid());
			mQuestionModelGson = null;
			break;
		}
	}

	public void stopAudio() {
		MediaUtil.getInstance(false).stopVoice(animationDrawable);
		mPayAnswerPlay.setImageResource(R.drawable.ic_play2);
	}
}
