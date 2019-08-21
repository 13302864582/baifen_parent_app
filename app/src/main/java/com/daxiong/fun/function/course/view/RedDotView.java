package com.daxiong.fun.function.course.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

public class RedDotView extends TextView {

	private static Animation fadeIn;
	private static Animation fadeOut;

	private int position;

	private boolean isShown;

	public RedDotView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RedDotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public RedDotView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		isShown = false;
		initAnimation();
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	private void initAnimation() {
		fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(200);

		fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator());
		fadeOut.setDuration(200);
	}

	public void show() {
		show(false, null);
	}

	public void show(boolean animate) {
		show(animate, fadeIn);
	}

	public void show(Animation anim) {
		show(true, anim);
	}

	public void hide() {
		hide(false, null);
	}

	public void hide(boolean animate) {
		hide(animate, fadeOut);
	}

	public void hide(Animation anim) {
		hide(true, anim);
	}

	public void toggle() {
		toggle(false, null, null);
	}

	public void toggle(boolean animate) {
		toggle(animate, fadeIn, fadeOut);
	}

	public void toggle(Animation animIn, Animation animOut) {
		toggle(true, animIn, animOut);
	}

	private void show(boolean animate, Animation anim) {
		if (animate) {
			this.startAnimation(anim);
		}
		this.setVisibility(View.VISIBLE);
		isShown = true;
	}

	private void hide(boolean animate, Animation anim) {
		this.setVisibility(View.GONE);
		if (animate) {
			this.startAnimation(anim);
		}
		isShown = false;
	}

	private void toggle(boolean animate, Animation animIn, Animation animOut) {
		if (isShown) {
			hide(animate && (animOut != null), animOut);
		} else {
			show(animate && (animIn != null), animIn);
		}
	}

	@Override
	public boolean isShown() {
		return isShown;
	}
}
