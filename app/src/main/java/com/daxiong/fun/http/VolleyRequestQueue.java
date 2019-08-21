package com.daxiong.fun.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequestQueue {
	private static volatile RequestQueue requestQueue;

	public static void init(Context context) {
		if (null == requestQueue) {
			synchronized (VolleyRequestQueue.class) {
				if (null == requestQueue) {
					requestQueue = Volley.newRequestQueue(context);
					requestQueue.start();
				}
			}
		}
	}

	public static synchronized RequestQueue getQueue() {
		return requestQueue;
	}
}
