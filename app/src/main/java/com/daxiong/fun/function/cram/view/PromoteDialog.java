package com.daxiong.fun.function.cram.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.daxiong.fun.R;

public class PromoteDialog extends Dialog implements android.view.View.OnClickListener{
	
	private OnButtonClickListener mListener;

	public PromoteDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public PromoteDialog(Context context, int theme) {
		super(context, theme);
	}

	public PromoteDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_promote);
		
		findViewById(R.id.dialog_promote_bt_ok).setOnClickListener(this);
		
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 去掉周围黑边
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.85);
		params.height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.42);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_promote_bt_ok:
			if(mListener != null){
				mListener.onClickOK();
			}
			break;
		}
		dismiss();
	}
	
	public void setOnButtonClickListener(OnButtonClickListener l){
		mListener = l;
	}
	
	public interface OnButtonClickListener{
		void onClickOK();
	}
}
