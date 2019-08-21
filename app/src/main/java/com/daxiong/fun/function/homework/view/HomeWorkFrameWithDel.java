package com.daxiong.fun.function.homework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.daxiong.fun.R;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;

public class HomeWorkFrameWithDel extends FrameLayout {

	public HomeWorkFrameWithDel(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews(context);
	}

	public HomeWorkFrameWithDel(Context context) {
		super(context);
		setupViews(context);
	}

	private void setupViews(Context context) {
		ImageView img = new ImageView(context);
		if (MySharePerfenceUtil.getInstance().getUserRoleId() == GlobalContant.ROLE_ID_STUDENT) {
			img.setImageResource(R.drawable.me_v0_11_27);
		}else {
			img.setImageResource(R.drawable.v0_11);
		}
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(DensityUtil.dip2px(context, 29), DensityUtil.dip2px(context, 29));
		img.setLayoutParams(params);
		img.invalidate();
		addView(img);
	}
	
}
