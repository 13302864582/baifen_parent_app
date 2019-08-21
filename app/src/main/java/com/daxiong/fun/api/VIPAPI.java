package com.daxiong.fun.api;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理vip相关信息
 * 
 * @author sky *
 */
public class VIPAPI extends VolleyRequestClientAPI {

	/**
	 * 家长版协议 获取vip信息
	 * 
	 * @author: sky
	 * @param queue
	 * @param type
	 * @param listener
	 * @param requestCode
	 *            void 必须是versionCode>=3010
	 */
	public void getVipList(RequestQueue queue, int type, int from_location, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/parents/parentsvipinfos
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("type", type);
		subParams.put("from_location", from_location);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/parentsvipinfos", dataStr, listener,
				requestCode);

	}

	/**
	 * 更多vip信息/正式VIP
	 * 
	 * @param queue
	 * @param type
	 * @param listener
	 * @param requestCode
	 */
	public void getMoreVipInfo(RequestQueue queue, int from_location, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/parents/parentsvipinfos
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("from_location", from_location);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/vipmorepackages", dataStr, listener,
				requestCode);

	}

	/**
	 * 生成预支付订单
	 * 
	 * @param queue
	 * @param vip_type
	 * @param listener
	 * @param requestCode
	 */
	public void getPrePayOrder(RequestQueue queue, int vip_type, final BaseActivity listener, final int requestCode) {
		// http://www.welearn.com:8080/api/parents/generateprepaidorders
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("vip_type", vip_type);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/generateprepaidorders", dataStr,
				listener, requestCode);

	}

	/**
	 * 刷新预支付信息
	 * 
	 * @param queue
	 * @param orderid
	 * @param listener
	 * @param requestCode
	 */
	public void refreshPaymentInfos(RequestQueue queue, String orderid, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/parents/refreshpaymentinfos
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("orderid", orderid);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/refreshpaymentinfos", dataStr,
				listener, requestCode);

	}

	/**
	 * 订单列表
	 * 
	 * @param queue
	 * @param page
	 * @param count
	 * @param listener
	 * @param requestCode
	 */
	public void getOrderList(RequestQueue queue, int page, int count, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/parents/orderlist
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("page", page);
		subParams.put("count", count);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/orderlist", dataStr, listener,
				requestCode);

	}

	/**
	 * 取消订单
	 * @param queue
	 * @param orderid
	 * @param listener
	 * @param requestCode
	 */
	public void cancleOrder(RequestQueue queue, String orderid, final BaseActivity listener, final int requestCode) {
		// http://www.welearn.com:8080/api/parents/cancleorder
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("orderid", orderid);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/cancleorder", dataStr, listener,
				requestCode);

	}

}
