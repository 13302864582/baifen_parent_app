package com.daxiong.fun.api;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;

import java.util.HashMap;
import java.util.Map;

public class MainAPI extends VolleyRequestClientAPI {


	/**
	 * 获取分享参数
	 * 
	 * @param queue
	 * @param listener
	 * @param requestCode
	 * @param  type 1-ios(家长)/2-andorid 家长/3-andorid 老师',
	 */
	public void getshareTip(RequestQueue queue, final BaseActivity listener, final int requestCode) {
		
		Map<String, Object> subParams = new HashMap<String, Object>();
		String dataStr = JSON.toJSONString(subParams);		
		requestHttpActivity(queue, HTTP_METHOD_GET, AppConfig.WELEARN_URL+"app_share.php?type=2", dataStr, listener,
				requestCode);
		
	}
	/**
	 * 家长版 得到主界面的提示框
	 * 
	 * @param queue
	 * @param listener
	 * @param requestCode
	 */
	public void getMainTip(RequestQueue queue, final BaseActivity listener, final int requestCode) {
		// http://www.welearn.com:8080/api/parents/welcomepage
		Map<String, Object> subParams = new HashMap<String, Object>();
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/welcomepage", dataStr, listener,
				requestCode);
		
	}

	/**
	 * 家长版 获取年级列表
	 * 
	 * @param queue
	 * @param listener
	 * @param requestCode
	 */
	public void getGradeList(RequestQueue queue, final BaseActivity listener, final int requestCode) {
		// http://www.welearn.com:8080/api/parents/gradelist
		Map<String, Object> subParams = new HashMap<String, Object>();
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/gradelist", dataStr, listener,
				requestCode);

	}



	/**
	 * 点击事件记录
	 * 
	 * @param queue
	 * @param listener
	 * @param requestCode
	 */
	public void clickevent( String event_code, final BaseActivity listener) {
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("event_code", event_code);

		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(listener.requestQueue, HTTP_METHOD_POST, AppConfig.GO_URL + "user/clickevent", dataStr, listener,
				RequestConstant.TONGJI_CODE);

	}

}
