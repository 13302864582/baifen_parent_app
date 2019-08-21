package com.daxiong.fun.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FitImageView extends ImageView {
	
	protected int mImageWidth;
	
	private float mRatio = 0.0f;

	/**
	 * 自动适配准备就绪的监听器
	 */
	private OnAutoFitReadyListener mOnAutoFitReadyListener;

	public FitImageView(Context context) {
		super(context);
	}


	public FitImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public FitImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}	


	public float getRatio() {
		return mRatio;
	}

	/**
	 * 设置高宽比例 H:W
	 * @param mRatio
	 */
	public void setRatio(float mRatio) {
		this.mRatio = mRatio;
	}
	
	/**
	 * 适配图片
	 */
	public void fitImage() {
		if (mRatio != 0.0f) {
			setImageParams(mRatio);
		}
	}
	
	/**
	 * 传比例并适配
	 * @param ratio
	 */
	public void fitImage(float ratio) {
		setImageParams(ratio);
	}
	
	/**
	 * 设置图片参数
	 * @param ratio
	 */
	private void setImageParams(float ratio){
		if (mImageWidth != 0) {
			android.view.ViewGroup.LayoutParams lp = getLayoutParams();
			int height = (int) (mImageWidth * ratio);
			lp.height = height;
			setLayoutParams(lp);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mImageWidth = getMeasuredWidth();
		if (mOnAutoFitReadyListener != null) {
			mOnAutoFitReadyListener.onAutoFitReady();
		}
	}
	
	/**
	 * 适配就绪的监听器
	 *
	 */
	public interface OnAutoFitReadyListener{
		
		/**
		 * 自动适配已经就绪
		 * @param width
		 */
		public void onAutoFitReady();
	}
	
	public void setOnAutoFitReadyListener(OnAutoFitReadyListener l) {
		mOnAutoFitReadyListener = l;
	}
	
	public void removeOnAutoFitReadyListener() {
		mOnAutoFitReadyListener = null;
	}
}
