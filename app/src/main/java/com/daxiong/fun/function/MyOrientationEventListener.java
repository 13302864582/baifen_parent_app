package com.daxiong.fun.function;

import android.content.Context;
import android.view.OrientationEventListener;

public class MyOrientationEventListener extends OrientationEventListener {

	private OnOrientationChangedListener mOnOrientationChangedListener;

	public MyOrientationEventListener(Context context, OnOrientationChangedListener listener) {
		super(context);
		this.mOnOrientationChangedListener = listener;
	}

	@Override
	public void onOrientationChanged(int orientation) {
		if (null != mOnOrientationChangedListener) {
			int setOrientation = ORIENTATION_UNKNOWN;

			if ((orientation > 315 && orientation < 360) || (orientation >= 0 && orientation <= 45)) {
				setOrientation = 0;
			} else if (orientation > 45 && orientation <= 135) {
				setOrientation = 90;
			} else if (orientation > 135 && orientation <= 225) {
				setOrientation = 180;
			} else if (orientation > 225 && orientation <= 315) {
				setOrientation = 270;
			}

			if (setOrientation == ORIENTATION_UNKNOWN)
				return;

			mOnOrientationChangedListener.onOrientationChanged(setOrientation);
		}
	}

	public interface OnOrientationChangedListener {
		public void onOrientationChanged(int orientation);
	}

}
