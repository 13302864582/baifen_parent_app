package com.daxiong.fun.function.course.holder;

import android.view.View;

/**
 * 此类应配合WBaseAdapter使用，包含一个可复用的RootView及其子View的引用
 *
 * @param <T> 数据类型
 */
public abstract class BaseHolder<T> {
	protected int mParamInt;
	protected boolean mParamBool;
	protected String mParamStr;

	protected View mRootView;
	protected int mPosition;
	protected T mData;
	
	public BaseHolder(){
		mRootView = initView();
		mRootView.setTag(this);
	}
	
	public View getRootView() {
		return mRootView;
	}

	public void setRootView(View rootView) {
		mRootView = rootView;
	}

	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int position) {
		mPosition = position;
	}
	
	public int getParamInt() {
		return mParamInt;
	}

	public void setParamInt(int param) {
		mParamInt = param;
	}
	
	public String getParamStr() {
		return mParamStr;
	}

	public void setParamStr(String param) {
		mParamStr = param;
	}

	public boolean isParamBool() {
		return mParamBool;
	}

	public void setParamBool(boolean param) {
		mParamBool = param;
	}

	public T getData() {
		return mData;
	}

	public void setData(T data) {
		mData = data;
		
		//设置数据后， 刷新View
		refreshView();
	}
	
	/**  初始化View。 */
	public abstract View initView();
	
	/** 刷新View */
	public abstract void refreshView();
	
	/** 用于回收 */
	public void recycle() {

	}
}
