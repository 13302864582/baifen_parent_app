package com.daxiong.fun.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.text.TextUtils;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.NetworkUtils;

public class NetworkReceiver extends BroadcastReceiver {

	@Override
	public synchronized void onReceive(Context context, Intent intent) {
		if (intent == null) {
			return;
		}
		if (context == null) {
			context = MyApplication.getContext();
		}

		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			return;
		}
		try {
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				State wifiState = wifiInfo.getState();
				State mobileState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
				boolean isConnected = wifiState != null
						&& mobileState != null
						&& (State.CONNECTED == wifiState || wifiState == State.CONNECTING
								|| State.CONNECTED == mobileState || State.CONNECTING == mobileState);
				if (!isConnected) {
					if (MyApplication.mNetworkUtil == null) {
						MyApplication.mNetworkUtil = NetworkUtils.getInstance();
					}
					MyApplication.mNetworkUtil.sendTimeoutMsg();
				} else {
					// WeLearnApi.reLogin();
					IntentManager.startWService(context);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}