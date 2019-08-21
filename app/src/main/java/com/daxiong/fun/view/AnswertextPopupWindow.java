package com.daxiong.fun.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daxiong.fun.R;

public class AnswertextPopupWindow extends PopupWindow {

	public AnswertextPopupWindow(Activity mContext, String text) {
		View view = View.inflate(mContext, R.layout.dadian_popupwindows, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
		RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl);
		ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		TextView tv = (TextView) view.findViewById(R.id.tv);
		scrollView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_popupwindow_in));

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setContentView(view);
		setOutsideTouchable(false);
		showAtLocation(mContext.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
		update();
		tv.setText(text);
		rl.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

	}

}
