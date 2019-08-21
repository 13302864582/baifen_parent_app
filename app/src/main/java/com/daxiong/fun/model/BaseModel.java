package com.daxiong.fun.model;

import android.database.Observable;

import com.daxiong.fun.base.Observer;
import com.daxiong.fun.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Asynchronous Model
 * 
 * @author parsonswang
 */
public class BaseModel extends Observable<Observer> {
	static final String TAG = "Model";

	@Override
	public void registerObserver(Observer observer) {
		super.registerObserver(observer);
	}

	@Override
	public void unregisterObserver(Observer observer) {
		super.unregisterObserver(observer);
	}

	@Override
	public void unregisterAll() {
		super.unregisterAll();
	}

	public void notifyChanged() {
		notifyChanged(0, null);
	}

	public void notifyChanged(int what) {
		notifyChanged(what, null);
	}

	public void notifyChanged(Object data) {
		notifyChanged(0, data);
	}

	public void notifyChanged(int what, Object data) {
		List<Observer> snapshot = null;
		synchronized (mObservers) {
			snapshot = new ArrayList<Observer>(mObservers);
		}
		for (Observer observer : snapshot) {
			if (observer != null) {
				int[] whats = observer.getWhats();
				if ((whats != null) && (whats.length > 0)
						&& ArrayUtils.contains(whats, what)) {
					observer.notifyChanged(what, this, data);
				}
			}
		}
	}
}
