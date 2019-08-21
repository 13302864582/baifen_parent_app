package com.daxiong.fun.http;

import android.text.TextUtils;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.util.MySharePerfenceUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestParamUtils {
	
	public static final String APP_NAME = "android_fdt_parents_phone";
//	public static final String APP_NAME = "fdt_head_teacher_web";

//	public static String getParamStr(JSONObject obj) {
//		// ver = app版本号
//		// appname = APP端类型
//		// sourcechan = 渠道
//		StringBuffer sb = new StringBuffer();
//
//		int ver = WApplication.versionCode;
//		String appname = "";
//		String sourcechan = WApplication.getContext().getString(R.string.channel_str_res);
//
//		sb.append("ver=\"" + ver);
//		sb.append("\"&");
//		sb.append("appname=\"" + appname);
//		sb.append("\"&");
//		sb.append("sourcechan=\"" + sourcechan);
//		sb.append("\"&");
//
//		String data = null;
//		if (null != obj) {
//			data = obj.toString();
//		} else {
//			data = "[]";
//		}
//		sb.append("data=\"" + data);
//		sb.append("\"");
//
//		return sb.toString();
//	}
	
	public static List<NameValuePair> getParam(JSONObject dataJson) {
		// ver = app版本号
		// appname = APP端类型
		// sourcechan = 渠道
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		int ver = MyApplication.versionCode;
		
		String sourcechan = MyApplication.getChannelValue();
		if (TextUtils.isEmpty(sourcechan)) {
			sourcechan = "welearn";
		}
		String data = null;
		if (null != dataJson) {
			data = dataJson.toString();
		} else {
			data = "{}";
		}
		params.add(new BasicNameValuePair("ver","" + ver));
		params.add(new BasicNameValuePair("appname", APP_NAME));
		params.add(new BasicNameValuePair("sourcechan", sourcechan));
		params.add(new BasicNameValuePair("tokenid", MySharePerfenceUtil.getInstance().getWelearnTokenId()));
		params.add(new BasicNameValuePair("data", data));
		
		return params;
	}


	public static Map<String, String> getMapParam(JSONObject dataJson) {
		// ver = app版本号
		// appname = APP端类型
		// sourcechan = 渠道
		Map<String, String> params = new HashMap<String, String>();
		int ver = MyApplication.versionCode;

		String sourcechan = MyApplication.getChannelValue();
		if (TextUtils.isEmpty(sourcechan)) {
			sourcechan = "welearn";
		}
		String data = null;
		if (null != dataJson) {
			data = dataJson.toString();
		} else {
			data = "{}";
		}
		params.put("ver", "" + ver);
		params.put("appname", APP_NAME);
		params.put("sourcechan", sourcechan);
		params.put("tokenid",MySharePerfenceUtil.getInstance().getWelearnTokenId());
		params.put("data", data);
		return params;
	}
}
