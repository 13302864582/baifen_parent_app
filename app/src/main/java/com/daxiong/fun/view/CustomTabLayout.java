package com.daxiong.fun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daxiong.fun.R;

public class CustomTabLayout extends RelativeLayout {

	private ImageView iconImageView;
	private TextView nameTextView;
	private RelativeLayout tipsLayout;
	private ImageView singleTipImageView;
	private TextView muteTipsTextView;

	public CustomTabLayout(Context context) {
		this(context, null);
		init(context);
	}

	public CustomTabLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init(context);
	}

	public CustomTabLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.view_tab_layout, this, true);
		iconImageView = (ImageView) findViewById(R.id.tab_icon);
		nameTextView = (TextView) findViewById(R.id.tab_name);
		tipsLayout = (RelativeLayout) findViewById(R.id.tab_tips_layout);
		singleTipImageView = (ImageView) findViewById(R.id.tab_single_tip);
		muteTipsTextView = (TextView) findViewById(R.id.tab_tips_count);
	}

	public void setIcon(int iconResId) {
		if (iconResId != 0) {
			iconImageView.setImageResource(iconResId);
		}
	}

	public void setName(int nameResId) {
		if (nameResId != 0) {
			nameTextView.setText(nameResId);
		}
	}

	public void setTips(int count) {
		if (count <= 0) {
			tipsLayout.setVisibility(View.GONE);
			return;
		}

		tipsLayout.setVisibility(View.VISIBLE);
		if (count == 1) {
			singleTipImageView.setVisibility(View.VISIBLE);
			muteTipsTextView.setVisibility(View.GONE);
		} else if (count > 1 && count < 100) {
			singleTipImageView.setVisibility(View.GONE);
			muteTipsTextView.setVisibility(View.VISIBLE);
			muteTipsTextView.setText(String.valueOf(count));
		} else {
			singleTipImageView.setVisibility(View.GONE);
			muteTipsTextView.setVisibility(View.VISIBLE);
			muteTipsTextView.setText("99+");
		}
	}
}
