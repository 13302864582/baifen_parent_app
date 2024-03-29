package com.daxiong.fun.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.daxiong.fun.R;
import com.daxiong.fun.util.LogUtils;

public class MyScrollView extends ScrollView {

	private static final String TAG = MyScrollView.class.getSimpleName();

	private ScrollLayoutScrollListener mScrollLayoutScrollListener;

	int mTop = 0;

	int mMin = 0;
	
	int height;

	public MyScrollView(Context context) {
		super(context);
		init(context);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mTop = context.getResources().getDimensionPixelSize(R.dimen.welearn_detail_head_height);
		// mMin = context.getResources().getDimensionPixelSize(R.dimen.welearn_detail_move_min);
		mMin = 0;
		
		height = mTop - mMin;
	}

	int mPadingTop = 0;

	float down_Y, yLast = 0;

	public void setScrollLayoutScrollListener(ScrollLayoutScrollListener mScrollLayoutScrollListener) {
		this.mScrollLayoutScrollListener = mScrollLayoutScrollListener;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// yLast = ev.getY();
			// onTouchEvent(ev);
			yLast = ev.getY();
			down_Y = ev.getY();
			LogUtils.i(TAG, " onInterceptTouchEvent ACTION_DOWN:" + down_Y);
			RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) getLayoutParams();
			mPadingTop = params.topMargin;
			isMoveToBottom = false;
			isMoveToTop = false;
			if (mPadingTop == mTop) {// (mTop - mMin)
				isMoveToBottom = true;
				if (getScrollY() > 0) {
					isMoveToTop = true;
				} else {
					isMoveToTop = false;
				}
			} else if (mPadingTop == mMin) {
				isMoveToTop = true;
				if (getScrollY() > 0) {
					isMoveToBottom = true;
				} else {
					isMoveToBottom = false;
				}
			}
			LogUtils.i(TAG, " onInterceptTouchEvent ACTION_DOWN");
			break;
		case MotionEvent.ACTION_MOVE:
			LogUtils.i(TAG, "onInterceptTouchEvent ACTION_MOVE:" + Math.abs((ev.getY() - down_Y)));
			onTouchEvent(ev);
			if (Math.abs((ev.getY() - down_Y)) > 10) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (Math.abs((ev.getY() - down_Y)) > 10) {
				return false;
			}
			LogUtils.i(TAG, "onInterceptTouchEvent ACTION_UP:" + Math.abs((ev.getY() - down_Y)));
			break;
		}
		super.onInterceptTouchEvent(ev);
		return false;
	}

	SmoothScrollRunnable mSmoothScrollRunnable;

	protected void smoothTo(int currenMargin, int targetMargin) {
		if (this.mSmoothScrollRunnable != null) {
			this.mSmoothScrollRunnable.stop();
		}

		this.mSmoothScrollRunnable = new SmoothScrollRunnable(currenMargin, targetMargin);
		this.mHandler.post(this.mSmoothScrollRunnable);
	}

	final class SmoothScrollRunnable implements Runnable {
		private final Interpolator interpolator;

		private final int to;

		private final int from;

		private boolean continueRunning = true;

		private long startTime = -1L;

		private int current = -1;

		public SmoothScrollRunnable(int from, int to) {
			this.from = from;
			this.to = to;
			this.interpolator = new AccelerateDecelerateInterpolator();
		}

		@Override
		public void run() {
			if (this.startTime == -1L) {
				this.startTime = System.currentTimeMillis();
			} else {
				long l = 1000L * (System.currentTimeMillis() - this.startTime) / 190L;
				l = Math.max(Math.min(l, 1000L), 0L);

				int i = Math.round((this.from - this.to) * this.interpolator.getInterpolation(l / 1000.0F));

				this.current = (this.from - i);
				RelativeLayout.LayoutParams params2 = (android.widget.RelativeLayout.LayoutParams) getLayoutParams();
				params2.setMargins(0, current, 0, 0);
				setLayoutParams(params2);
				if (null != mScrollLayoutScrollListener) {
					mScrollLayoutScrollListener.onScroll((current - mMin) / (float) height);
				}
			}

			if ((this.continueRunning) && (this.to != this.current))
				mHandler.postDelayed(this, 10L);
		}

		public void stop() {
			this.continueRunning = false;
			mHandler.removeCallbacks(this);
		}
	}

	private final Handler mHandler = new Handler();

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	boolean isMoveToTop, isMoveToBottom;

	float move_distance;// 记录移动距离

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// return super.onInterceptTouchEvent(ev);
	// }

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// move_distance = 0;
		// yLast = ev.getY();
		// LogUtil.i(TAG, " onTouchEvent ACTION_DOWN:ev.getY():"+ev.getY());
		// RelativeLayout.LayoutParams params =
		// (android.widget.RelativeLayout.LayoutParams)getLayoutParams();
		// mPadingTop = params.topMargin;
		// isMoveToBottom = false;
		// isMoveToTop = false;
		// if (mPadingTop == mTop) {// (mTop - mMin)
		// isMoveToBottom = true;
		// if (getScrollY() > 0) {
		// isMoveToTop = true;
		// } else {
		// isMoveToTop = false;
		// }
		// } else if (mPadingTop == mMin) {
		// isMoveToTop = true;
		// if (getScrollY() > 0) {
		// isMoveToBottom = true;
		// } else {
		// isMoveToBottom = false;
		// }
		// }
		// break;
		case MotionEvent.ACTION_UP:
			LogUtils.i(TAG, " onTouchEvent ACTION_UP");
			RelativeLayout.LayoutParams params2 = (android.widget.RelativeLayout.LayoutParams) getLayoutParams();
			mPadingTop = params2.topMargin;

			// if(isMoveToTop && Math.abs((ev.getY() - down_Y)) < 10){
			// break;
			// }

			if (mPadingTop == mTop || mPadingTop == mMin) {// (mTop - mMin)
				return true;
			}
			if ((mTop - mPadingTop) > (mTop - mMin) / 2) {
				mPadingTop = mMin;
			} else {
				mPadingTop = mTop;
			}
			smoothTo(params2.topMargin, mPadingTop);
			if (mPadingTop == mTop || mPadingTop == mMin) {// (mTop - mMin)
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			LogUtils.i(TAG, " onTouchEvent ACTION_MOVE:" + ev.getY());
			float curY = ev.getY();
			Log.i(TAG, "curY:" + curY + " yLast:" + yLast);
			RelativeLayout.LayoutParams params3 = (android.widget.RelativeLayout.LayoutParams) getLayoutParams();
			mPadingTop = params3.topMargin;
			if (curY > yLast) {// 下滑
				if (isMoveToBottom) {
					yLast = curY;
					break;
				}
				move_distance = curY - yLast;
				LogUtils.i(TAG, " onTouchEvent move_distance:" + move_distance);
				int distance = (int) (curY - yLast) / 2;
				if (mPadingTop < mTop) {
					mPadingTop = mPadingTop + distance;

					if (mPadingTop > mTop) {
						mPadingTop = mTop;
					}
					params3.setMargins(0, mPadingTop, 0, 0);
					setLayoutParams(params3);
					if (null != mScrollLayoutScrollListener) {
						mScrollLayoutScrollListener.onScroll((mPadingTop - mMin) / (float) height);
					}
					return true;
				}
				if (mPadingTop == mTop) {
					return true;
				}
			} else if (curY < yLast) {// 上滑
				move_distance = yLast - curY;
				LogUtils.i(TAG, " onTouchEvent move_distance:" + move_distance);
				int distance = (int) (yLast - curY) / 2;
				if (isMoveToTop) {
					if (mPadingTop > mMin) {
						mPadingTop = mPadingTop - distance;
						if (mPadingTop < mMin) {
							mPadingTop = mMin;
						}
						params3.setMargins(0, mPadingTop, 0, 0);
						setLayoutParams(params3);
						if (null != mScrollLayoutScrollListener) {
							mScrollLayoutScrollListener.onScroll((mPadingTop - mMin) / (float) height);
						}
						return true;
					}
					break;
				}

				if (mPadingTop > mMin) {
					mPadingTop = mPadingTop - distance;
					if (mPadingTop < mMin) {
						mPadingTop = mMin;
					}
					params3.setMargins(0, mPadingTop, 0, 0);
					setLayoutParams(params3);
					if (null != mScrollLayoutScrollListener) {
						mScrollLayoutScrollListener.onScroll((mPadingTop - mMin) / (float) height);
					}
					return true;
				}
				if (mPadingTop == mMin) {
					return true;
				}
			} else {
				move_distance = 0;
			}
			yLast = curY;
			break;
		}

		return super.onTouchEvent(ev);
	}
	
	public void resetPosition() {
		RelativeLayout.LayoutParams params2 = (android.widget.RelativeLayout.LayoutParams) getLayoutParams();
		mPadingTop = params2.topMargin;

		// if(isMoveToTop && Math.abs((ev.getY() - down_Y)) < 10){
		// break;
		// }

		if (mPadingTop == mTop || mPadingTop == mMin) {// (mTop - mMin)
			return;
		}
		if ((mTop - mPadingTop) > (mTop - mMin) / 2) {
			mPadingTop = mMin;
		} else {
			mPadingTop = mTop;
		}
		smoothTo(params2.topMargin, mPadingTop);
		if (mPadingTop == mTop || mPadingTop == mMin) {// (mTop - mMin)
			return;
		}
	}

	public interface ScrollLayoutScrollListener {
		public void onScroll(float marginTopScale);
	}
}
