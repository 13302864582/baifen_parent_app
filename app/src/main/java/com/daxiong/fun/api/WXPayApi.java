
package com.daxiong.fun.api;

import com.android.volley.RequestQueue;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * 家长版 处理微信相关的api
 * 此类的描述：请求微信预订单 Api
 */
public class WXPayApi extends VolleyRequestClientAPI {

	/**
	 * 家长版
	 * 请求微信预订单 Api
	 * @param queue
	 * @param userid
	 * @param fromuserid
	 * @param totalfee
	 * @param body
	 * @param servertype
	 * @param listener
	 * @param requestCode
	 */
	public void WXpay(RequestQueue queue, int userid, int fromuserid, float totalfee, String body, int servertype, String golangorderid,
					  final BaseActivity listener, final int requestCode) {
		Map<String, Object> subParams = new HashMap<String, Object>();

//		requestWXHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GET_WXPAY, userid, fromuserid, totalfee, body,
//				servertype,golangorderid, listener, requestCode);

	}

	public void WXpay(RequestQueue queue, int userid, int fromuserid, float totalfee, String body, int servertype, String golangorderid,
					  final BaseFragment listener, final int requestCode) {
		Map<String, Object> subParams = new HashMap<String, Object>();

		requestWXHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GET_WXPAY, userid, fromuserid, totalfee, body,
				servertype,golangorderid, listener, requestCode);

	}


}
