package com.daxiong.fun.function.homework.view;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.function.study.StuHomeWorkCheckDetailActivity;

public class RefuseHomeWorkPopWindow extends PopupWindow implements OnClickListener {
	private FrameLayout mTopContainer;

	private TextView mMoreQuesPicBtn;
	private TextView mCancelBtn;
	private RadioGroup rg;
	private View view;
	private StuHomeWorkCheckDetailActivity mActivity;

	public RefuseHomeWorkPopWindow(final StuHomeWorkCheckDetailActivity activity, View parent) {
		this.mActivity = activity;
		view = View.inflate(mActivity, R.layout.report_question, null);

		view.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.fade_ins));
		mTopContainer = (FrameLayout) view.findViewById(R.id.top_container);
		mTopContainer.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.question_filter_popupwindow_out));


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
	}

	@Override
	public void onClick(View view) {
		dismiss();
		switch (view.getId()) {
			case R.id.report_cancel_btn:
				dismiss();
				break;

			case R.id.report_btn:

				switch (rg.getCheckedRadioButtonId()) {
					case R.id.rb1:
						mActivity.refuseAnswer("答案错误");
						break;
					case R.id.rb2:
						mActivity.refuseAnswer("讲解过于简单");
						break;
					case R.id.rb3:
						mActivity.refuseAnswer("图片与问题无关");


				}
					dismiss();
				break;
		}
	}
}
