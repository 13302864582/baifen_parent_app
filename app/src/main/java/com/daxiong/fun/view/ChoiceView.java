package com.daxiong.fun.view;

import android.content.Context;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.daxiong.fun.R;

public class ChoiceView extends FrameLayout implements Checkable {
	private RadioButton mRadioButton;

	public ChoiceView(Context context) {
		super(context);

		View.inflate(context, R.layout.item_grid_img, this);
		mRadioButton= (RadioButton) findViewById(R.id.checkedView);

	}

	public void setText(String text) {
		mRadioButton.setText(text);
	}

	@Override
	public void setChecked(boolean checked) {
		mRadioButton.setChecked(checked);

	}

	@Override
	public boolean isChecked() {
		 return mRadioButton.isChecked();
	}

	@Override
	public void toggle() {
		mRadioButton.toggle();

	}

}
