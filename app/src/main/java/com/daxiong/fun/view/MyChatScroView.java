package com.daxiong.fun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MyChatScroView extends ScrollView {

	public MyChatScroView(Context context) {
		super(context);
	}

	public MyChatScroView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyChatScroView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int spec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, spec);
	}
	
}
