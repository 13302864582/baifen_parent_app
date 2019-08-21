package com.daxiong.fun.model;

import android.graphics.Bitmap;

public class FitBitmap {

	private Bitmap mBitmap;
	private float radio;
	private float mSrcWidth;
	private float mSrcHeight;
	
	public FitBitmap(Bitmap bitmap, float radio, float srcWidth, float srcHeight) {
		this.setmBitmap(bitmap);
		this.setRadio(radio);
		this.mSrcWidth = srcWidth;
		this.mSrcHeight = srcHeight;
	}
	
	public Bitmap getmBitmap() {
		return mBitmap;
	}
	public void setmBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}
	public float getRadio() {
		return radio;
	}
	public void setRadio(float radio) {
		this.radio = radio;
	}

	public float getSrcWidth() {
		return mSrcWidth;
	}

	public float getSrcHeight() {
		return mSrcHeight;
	}
}
