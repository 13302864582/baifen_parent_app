package com.daxiong.fun.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.daxiong.fun.R;
import com.daxiong.fun.function.communicate.ChatMsgViewActivity;
import com.daxiong.fun.model.ChatInfo;

public class ResendPopupWindow extends PopupWindow {

	@SuppressWarnings("deprecation")
	public ResendPopupWindow(final Activity mContext, View parent, final ChatMsgViewActivity sendView,
			final ChatInfo chatInfo) {
		View view = View.inflate(mContext, R.layout.resend_item_popupwindows, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_popupwindow_in));

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setContentView(view);
		setOutsideTouchable(false);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);

		update();

		Button resendBtn = (Button) view.findViewById(R.id.item_popupwindows_resend);
		Button cancelBtn = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		resendBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendView.resend(chatInfo);
				dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
