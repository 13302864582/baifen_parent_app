package com.daxiong.fun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.daxiong.fun.R;

public class CameraAudioTextChoiceWindow extends FrameLayout {

	public CameraAudioTextChoiceWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		setUpViews();
	}

	public CameraAudioTextChoiceWindow(Context context) {
		super(context);
		setUpViews();
	}
	
	
	private void setUpViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.item_camera_audio_text_choice_windows, null);
		addView(view);
	}
}
