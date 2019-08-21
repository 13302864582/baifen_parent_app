package com.daxiong.fun.function.course.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;

/**
 * 页面数据加载View
 *
 */
public abstract class  LoadingPageView extends FrameLayout{
	
	private static final int STATE_UNLOADED = 0;// 未知状态
	private static final int STATE_LOADING = 1;// 加载状态
	private static final int STATE_ERROR = 2;// 加载完毕，但是出错状态
	private static final int STATE_EMPTY = 3;// 加载完毕，但是没有数据状态
	private static final int STATE_SUCCEED = 4;// 加载成功

	private View mLoadingView;
	private View mErrorView;
	private View mEmptyView;
	private View mSucceedView;

	private int mState;
	
	public LoadingPageView(Context context) {
		super(context);
		init();
	}

	private void init() {
		mState = STATE_UNLOADED;

		mLoadingView = createLoadingView();
		if (null != mLoadingView) {
			addView(mLoadingView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mLoadingView.setVisibility(View.INVISIBLE);
		}

		mErrorView = createErrorView();
		if (null != mErrorView) {
			addView(mErrorView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mErrorView.setVisibility(View.INVISIBLE);
		}

		mEmptyView = createEmptyView();
		if (null != mEmptyView) {
			addView(mEmptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mEmptyView.setVisibility(View.INVISIBLE);
		}
		
		mSucceedView = createLoadedView();
		if (null != mSucceedView) {
			addView(mSucceedView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mSucceedView.setVisibility(View.INVISIBLE);
		}
		
		showPageSafeView();
	}

	public View createLoadingView(){
		return View.inflate(getContext(), R.layout.loading_page_loading, null);
	}

	public View createErrorView() {
		return new View(getContext());
	}

	public View createEmptyView() {
		View view = View.inflate(getContext(), R.layout.master_course_empty_view, null);
		return view;
	}
	
	/** 线程安全 显示View */
	private void showPageSafeView() {
		//运行到主线程
		MyApplication.getMainThreadHandler().post(new Runnable() {
			
			@Override
			public void run() {
				showPageView();
			}
		});
	}
	
	/** 显示对应View */
	private void showPageView() {
		
		if (null != mLoadingView) {
			mLoadingView.setVisibility(mState == STATE_UNLOADED || mState == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
		}
		if (null != mErrorView) {
			mErrorView.setVisibility(mState == STATE_ERROR ? View.VISIBLE : View.INVISIBLE);
		}
		if (null != mEmptyView) {
			mEmptyView.setVisibility(mState == STATE_EMPTY ? View.VISIBLE : View.INVISIBLE);
		}
		if (null != mSucceedView) {
			mSucceedView.setVisibility(mState == STATE_SUCCEED ? View.VISIBLE : View.INVISIBLE);
		}
	}

	/** 数据加载成功显示的View */
	public abstract View createLoadedView();
	
	public synchronized void show(LoadResult result) {
		int value = result.getValue();
		if(mState != value){ //状态改变
			mState = value;
			showPageSafeView();
		}
	}
	
	public enum LoadResult {
		LOADING(STATE_LOADING), ERROR(STATE_ERROR), EMPTY(STATE_EMPTY), SUCCEED(STATE_SUCCEED);
		int value;

		LoadResult(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
}
