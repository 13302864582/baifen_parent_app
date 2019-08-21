package com.daxiong.fun.util;

import android.text.TextUtils;
import android.util.Log;

import com.daxiong.fun.config.AppConfig;

public class LogUtils {

	/** 默认TAG */
	private static final String DEFAULT_TAG = "default_tag";
	/** 默认MSG */
	private static final String DEFAULT_MSG = "msg is null !";
	
	
	// private static final boolean DEBUG = true;

	public static void v(String tag, String msg) {
		if (AppConfig.IS_DEBUG) {
			tag = getTag(tag);
			msg = getMsg(msg);
			Log.v(tag, msg);
		}
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (AppConfig.IS_DEBUG) {
			tag = getTag(tag);
			msg = getMsg(msg);
			Log.v(tag, msg, tr);
		}
	}

	public static void d(String tag, String msg) {
		if (AppConfig.IS_DEBUG) {
			tag = getTag(tag);
			msg = getMsg(msg);
			Log.d(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (AppConfig.IS_DEBUG) {
			tag = getTag(tag);
			msg = getMsg(msg);
			Log.d(tag, msg, tr);
		}
	}

	public static void i(String tag, String msg) {
		if (AppConfig.IS_DEBUG) {
			tag = getTag(tag);
			msg = getMsg(msg);
			Log.i(tag, msg);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (AppConfig.IS_DEBUG) {
			tag = getTag(tag);
			msg = getMsg(msg);
			Log.i(tag, msg, tr);
		}
	}

	public static void w(String tag, String msg) {
		if (AppConfig.IS_DEBUG) {
			tag = getTag(tag);
			msg = getMsg(msg);
			Log.w(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (AppConfig.IS_DEBUG) {
			tag = getTag(tag);
			msg = getMsg(msg);
			Log.w(tag, msg, tr);
		}
	}

	public static void e(String tag, String msg) {
		if (AppConfig.IS_DEBUG) {
			tag = getTag(tag);
			msg = getMsg(msg);
			Log.e(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (AppConfig.IS_DEBUG) {
			tag = getTag(tag);
			msg = getMsg(msg);
			Log.e(tag, msg, tr);
		}
	}

	/**
	 * 处理tag为“null”或者为“”的情况，保证Log不会空指针异常
	 * 
	 * @param tag
	 * @return 如果tag不为“null”且不为“”，返回tag；否则返回{@link DEFAULT_TAG}
	 */
	private static String getTag(String tag) {
		if (TextUtils.isEmpty(tag)) {
			return DEFAULT_TAG;
		}
		return tag;
	}

	/**
	 * 处理msg为“null”或者为“”的情况，保证Log不会空指针异常
	 * 
	 * @param msg
	 * @return 如果msg不为“null”且不为“”，返回msg；否则返回{@link DEFAULT_MSG}
	 */
	private static String getMsg(String msg) {
		if (TextUtils.isEmpty(msg)) {
			return DEFAULT_MSG;
		}
		return msg;
	}

}
