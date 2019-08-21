package com.daxiong.fun.view;

import android.content.Context;
import android.view.View;
import android.widget.Checkable;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daxiong.fun.R;

public class ChoiceView2 extends RelativeLayout implements Checkable {
	private RadioButton mRadioButton;
	private TextView tv;

	public ChoiceView2(Context context) {
		super(context);

		View.inflate(context, R.layout.item_grid_img2, this);
		mRadioButton= (RadioButton) findViewById(R.id.checkedView);
		tv= (TextView) findViewById(R.id.tv);

	}

	public void setText(String text) {
		tv.setText(text);
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
