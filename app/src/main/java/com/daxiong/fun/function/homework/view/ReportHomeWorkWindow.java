package com.daxiong.fun.function.homework.view;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;

public class ReportHomeWorkWindow extends PopupWindow implements OnClickListener {

	private FrameLayout mTopContainer;
	private ImageView mIcDel;
	private TextView reasonBtn1;
	private TextView reasonBtn2;
	private TextView reasonBtn3;
	private TextView reasonBtn4;
	private BaseActivity mActivity;

	public ReportHomeWorkWindow(View parent, BaseActivity mActivity) {
		this.mActivity = mActivity;
		View view = View.inflate(mActivity, R.layout.report_grab_question, null);
		view.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.fade_ins));
		mTopContainer = (FrameLayout) view.findViewById(R.id.top_container);
		mTopContainer.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.question_filter_popupwindow_out));

		mIcDel = (ImageView) view.findViewById(R.id.ic_close_dialog_grab);
		mIcDel.setOnClickListener(this);

		reasonBtn1 = (TextView) view.findViewById(R.id.report_reason_btn1);
		reasonBtn1.setOnClickListener(this);

		reasonBtn2 = (TextView) view.findViewById(R.id.report_reason_btn2);
		reasonBtn2.setOnClickListener(this);

		reasonBtn3 = (TextView) view.findViewById(R.id.report_reason_btn3);
		reasonBtn3.setOnClickListener(this);

		reasonBtn4 = (TextView) view.findViewById(R.id.report_reason_btn4);
		reasonBtn4.setOnClickListener(this);

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
		case R.id.ic_close_dialog_grab:
			break;
		case R.id.report_reason_btn1:
			mActivity.report(MyApplication.getContext().getResources().getString(R.string.text_report_reason1));
			break;
		case R.id.report_reason_btn2:
			mActivity.report(MyApplication.getContext().getResources().getString(R.string.text_report_reason2));
			break;
		case R.id.report_reason_btn3:
			mActivity.report(MyApplication.getContext().getResources().getString(R.string.text_report_reason3));
			break;
		case R.id.report_reason_btn4:
			mActivity.report(MyApplication.getContext().getResources().getString(R.string.text_report_reason4));
			break;
		}
	}
}
