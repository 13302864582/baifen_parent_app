package com.daxiong.fun.function.account.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.function.communicate.ChatMsgViewActivity;
import com.daxiong.fun.function.question.PayAnswerImageGridActivity;
import com.daxiong.fun.function.question.PayAnswerQuestionDetailActivity;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.BtnUtils;
import com.daxiong.fun.view.ScaleImageView;

public class PreSendPicReViewActivity extends BaseActivity implements OnClickListener {

	private ScaleImageView scaleImg;
	// private LinearLayout mRootContainer;
	private String path;

	private int userid;
	private String username;

	private TextView nextStepTV;
	private RelativeLayout nextStepLayout;

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.fragment_presend_review_pic);

		// setWelearnTitle(R.string.text_retake);
		setWelearnTitle("");

		findViewById(R.id.back_layout).setOnClickListener(this);

		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.text_send_confirm);
		nextStepLayout.setOnClickListener(this);

		scaleImg = (ScaleImageView) findViewById(R.id.pic_scale_review_presend);
		Intent intent = getIntent();
		path = intent.getStringExtra(PayAnswerQuestionDetailActivity.IMG_PATH);
		userid = intent.getIntExtra(ChatMsgViewActivity.USER_ID, 0);
		username = intent.getStringExtra(ChatMsgViewActivity.USER_NAME);
		// scaleImg.loadImage(path);
		
			// 原来调用的压缩图片方法
			//scaleImg.setBitmap(path, this);
		
			// 现在的方法
			scaleImg.setBitmap2(path, this);
		
	}

	public void goPre() {
		if (userid != 0) {
			Bundle data = new Bundle();
			data.putBoolean("isFromNoti", true);
			data.putInt("userid", userid);
			data.putString(ChatMsgViewActivity.USER_NAME, username);
			Intent intent = new Intent();
			intent.putExtras(data);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(this, ChatMsgViewActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void onBackPressed() {
		goPre();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
	    if (BtnUtils.isFastClick()) {
            return ;
        }
		switch (v.getId()) {
		case R.id.back_layout:
			goPre();
			finish();
			break;
		case R.id.next_setp_layout:
			if (userid == 0) {
				if (GlobalVariable.mChatMsgViewActivity != null) {
					GlobalVariable.mChatMsgViewActivity.sendImageMsg(path);
				}
				finish();
			} else {
				Bundle data = new Bundle();
				data.putInt(ChatMsgViewActivity.USER_ID, userid);
				data.putString(ChatMsgViewActivity.USER_NAME, username);
				data.putString(PayAnswerImageGridActivity.IMAGE_PATH, path);
				IntentManager.goToChatListView(this, data, true);
			}
			break;
		}
	}

}
