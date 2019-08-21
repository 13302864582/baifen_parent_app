package com.daxiong.fun.base;

import android.os.Handler;
import android.os.Looper;

import com.daxiong.fun.model.BaseModel;

public abstract class Observer {
	static final String TAG = "Observer";

	private Handler mHandler;
	private int[] mWhats;

	public Observer() {
		this(new Handler(Looper.getMainLooper()));
	}

	public Observer(Handler handler) {
		mHandler = handler;
	}

	public void setWhats(int[] whats) {
		mWhats = whats;
	}

	public int[] getWhats() {
		return mWhats;
	}

	protected void onChanged(int what, BaseModel model, Object data) {
		if (mHandler != null) {
			sendMsg(mHandler);
		}
	}

	public final void notifyChanged(int what, BaseModel model, Object data) {
		if (mHandler == null) {
			onChanged(what, model, data);
		} else {
			mHandler.post(new NotificationRunnable(what, model, data));
		}
	}

	protected abstract void sendMsg(Handler handler);
	
	private final class NotificationRunnable implements Runnable {
		private final int mWhat;
		private final BaseModel mModel;
		private final Object mData;

		public NotificationRunnable(int what, BaseModel model, Object data) {
			mWhat = what;
			mModel = model;
			mData = data;
		}

		@Override
		public void run() {
			Observer.this.onChanged(mWhat, mModel, mData);
		}
	}
}
