package com.daxiong.fun.function;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;

import com.daxiong.fun.R;
import com.daxiong.fun.function.question.OneQuestionMoreAnswersDetailItemFragment.AdoptSubmitBtnClick;
import com.daxiong.fun.util.ToastUtils;

/**
 * 采纳答案并且提交满意度评价的dialog
 */
public class AdoptAnswerAndAppraiseDialog extends Dialog implements
		android.view.View.OnClickListener {

	private AdoptSubmitBtnClick mReset;
	private Button submit;
	private RatingBar effect;
	private RatingBar attitude;

	public AdoptAnswerAndAppraiseDialog(Context context) {
		super(context);
	}

	public AdoptAnswerAndAppraiseDialog(Context context,
			AdoptSubmitBtnClick reset) {
		super(context, R.style.MyRatingBar);
		this.mReset = reset;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.adopt_manyidu_dialog);
		ActionBar bar = getActionBar();
		if (bar!=null) {
			bar.hide();
		}
		
		effect = (RatingBar) findViewById(R.id.ratingBar_adopt_effect);
		attitude = (RatingBar) findViewById(R.id.ratingBar_adopt_attitude);

		submit = (Button) findViewById(R.id.submit_adopt_btn);
		submit.setOnClickListener(this);

		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 去掉周围黑边
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.85);
		params.height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.42);
	}

	@Override
	public void onClick(View v) {
		int eff = effect.getProgress();
		int att = attitude.getProgress();
		if (eff == 0 || att == 0) {
			ToastUtils.show(R.string.text_please_choose_satisfaction);
			return;
		}
		mReset.ensure(eff, att);
		this.dismiss();
	}
}
