package com.daxiong.fun.function.course.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.daxiong.fun.R;

public class BuyCourseDialog extends Dialog implements android.view.View.OnClickListener {
	private String mTilte;
	private String mOKText;
	private String mCancelText;
	
	private OnClickDialogListener mOKListener;
	private OnClickDialogListener mCancelListener;

	public BuyCourseDialog(Context context) {
		super(context);
	}

	public BuyCourseDialog(Context context, String title) {
		super(context);
		mTilte = title;
	}
	
	public BuyCourseDialog(Context context, String title, String okText, String cancelText) {
		super(context);
		mTilte = title;
		mOKText = okText;
		mCancelText = cancelText;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_buy_isok);

		TextView tilte = (TextView) findViewById(R.id.dialog_buy_course_tilte);
		
		Button bt_ok = (Button) findViewById(R.id.dialog_buy_course_bt_ok);
		
		Button bt_cancel = (Button) findViewById(R.id.dialog_buy_course_bt_cancel);
		
		if (mTilte != null) {
			tilte.setText(mTilte);
		}
		
		if(!TextUtils.isEmpty(mOKText) && !TextUtils.isEmpty(mCancelText)){
			bt_ok.setText(mOKText);
			bt_cancel.setText(mCancelText);
		}

		bt_ok.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 去掉周围黑边
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.85);
		params.height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.42);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_buy_course_bt_ok:
			if(mOKListener != null){
				mOKListener.onClick();
			}
			break;
		case R.id.dialog_buy_course_bt_cancel:
			if(mCancelListener != null){
				mCancelListener.onClick();
			}
			break;
		}
		dismiss();
	}

	public void setOnClickButton1Listener(OnClickDialogListener listener){
		mOKListener = listener;
	}
	
	public void setOnClickButton2Listener(OnClickDialogListener listener){
		mCancelListener = listener;
	}
	
	public interface OnClickDialogListener{
		void onClick();
	}
}
