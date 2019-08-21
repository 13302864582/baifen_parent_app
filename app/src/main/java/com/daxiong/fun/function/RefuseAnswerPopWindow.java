package com.daxiong.fun.function;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.function.question.OneQuestionMoreAnswersDetailItemFragment;
import com.daxiong.fun.util.ToastUtils;

public class RefuseAnswerPopWindow extends PopupWindow implements OnClickListener {
	private FrameLayout mTopContainer;

	private TextView mMoreQuesPicBtn;
	private TextView mCancelBtn;
	private RadioGroup rg;
	private View view;

	private OneQuestionMoreAnswersDetailItemFragment mFragment;

	public RefuseAnswerPopWindow(final Activity mContext, View parent, OneQuestionMoreAnswersDetailItemFragment fragment) {
		this.mFragment = fragment;
		 view = View.inflate(mContext, R.layout.report_question, null);

		view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
		mTopContainer = (FrameLayout) view.findViewById(R.id.top_container);
		mTopContainer.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.question_filter_popupwindow_out));




		mCancelBtn = (TextView) view.findViewById(R.id.report_cancel_btn);
		mMoreQuesPicBtn = (TextView) view.findViewById(R.id.report_btn);
		rg = (RadioGroup) view.findViewById(R.id.rg);

		mCancelBtn.setOnClickListener(this);
		mMoreQuesPicBtn.setOnClickListener(this);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setContentView(view);
		setOutsideTouchable(false);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();
		GlobalVariable.mRefuseAnswerPopWindow = this;
		setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				GlobalVariable.mRefuseAnswerPopWindow = null;
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.report_cancel_btn:
			dismiss();
			break;

		case R.id.report_btn:
			String text = ((RadioButton) view.findViewById(rg.getCheckedRadioButtonId())).getText().toString();
			ToastUtils.show(text);
			mCancelBtn.setText(text);
			//mFragment.refuseAnswer(text);
		//	dismiss();
			break;
		}
	}
}
