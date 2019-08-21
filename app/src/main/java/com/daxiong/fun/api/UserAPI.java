
package com.daxiong.fun.api;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.http.volley.VolleyRequestClientAPI;
import com.daxiong.fun.model.PhoneLoginModel;
import com.daxiong.fun.util.MD5Util;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类的描述：关于用户的一些操作Api
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015年8月6日 下午2:54:53
 */
public class UserAPI extends VolleyRequestClientAPI {

	/**
	 * 获取闪屏图片
	 * 
	 * @author: sky
	 * @param queue
	 * @param listener
	 * @param requestCode
	 *            void
	 */
	public void getWelcomeImage(RequestQueue queue, final BaseActivity listener, final int requestCode) {
		Map<String, Object> subParams = new HashMap<String, Object>();
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "org/getsplashurl", dataStr, listener,
				requestCode);

	}

	/**
	 * 执行登录操作
	 * 
	 * @author: sky
	 * @param queue
	 * @param item
	 * @param listener
	 * @param requestCode
	 *            void
	 */
	public void execLogin(RequestQueue queue, PhoneLoginModel item, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/user/tellogin
		String dataStr = JSON.toJSONString(item);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "user/tellogin", dataStr, listener,
				requestCode);

	}

	/**
	 * 得到用户信息
	 * 
	 * @param queue
	 * @param userid
	 * @param listener
	 * @param requestCode
	 */
	public void getUserInfo(RequestQueue queue, int userid, final BaseActivity listener, final int requestCode) {
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("userid", userid);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "user/getuserinfo", dataStr, listener,
				requestCode);

	}

	/************************************************* 华丽的分割线 *************************************************/
	/**
	 * 家长版协议 发送验证码
	 * 
	 * @author: sky
	 * @param queue
	 * @param tel
	 * @param listener
	 * @param requestCode
	 *            void
	 */
	public void sendPhoneValidateCode(RequestQueue queue, String tel, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/guest/sendsecuritycode
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("tel", tel);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "guest/sendsecuritycode", dataStr, listener,
				requestCode);

	}

	/**
	 * 家长版协议 修改密码功能
	 * 
	 * @author: sky
	 * @param queue
	 * @param tel
	 * @param code
	 * @param oldpassword
	 * @param newpassword
	 * @param listener
	 * @param requestCode
	 *            void
	 */
	public void modifyPassword(RequestQueue queue, String tel, String password, String code,
			final BaseActivity listener, final int requestCode) {
		// http://www.welearn.com:8080/api/guest/modifypassword
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("tel", tel);
		subParams.put("password", MD5Util.getMD5String(password));
		subParams.put("code", code);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "guest/resetpassword", dataStr, listener,
				requestCode);

	}

	/**
	 * 家长版协议 重置密码
	 * 
	 * @author: sky
	 * @param queue
	 * @param tel
	 * @param code
	 * @param password
	 * @param newpassword
	 * @param listener
	 * @param requestCode
	 *            void
	 */
	public void resetPassword(RequestQueue queue, String tel, String code, String password, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/guest/resetpassword
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("tel", tel);
		subParams.put("code", code);
		subParams.put("password", password);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "guest/resetpassword", dataStr, listener,
				requestCode);

	}

	/**
	 * 家长版协议 更换手机号
	 * 
	 * @author: sky
	 * @param queue
	 * @param tel
	 * @param code
	 * @param password
	 * @param listener
	 * @param requestCode
	 *            void
	 */
	public void modifyTel(RequestQueue queue, String tel, String code, String password, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/parents/modifytel
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("tel", tel);
		subParams.put("code", code);
		subParams.put("passwd", MD5Util.getMD5String(password));
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/modifytel", dataStr, listener,
				requestCode);

	}

	/**
	 * 家长版协议 注销
	 * 
	 * @author: sky
	 * @param queue
	 * @param type
	 * @param pageindex
	 * @param pagecount
	 * @param listener
	 * @param requestCode
	 *            void
	 */
	public void loginOut(RequestQueue queue, int type, int pageindex, int pagecount, final BaseActivity listener,
			final int requestCode) {
		Map<String, Object> subParams = new HashMap<String, Object>();
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "user/logout", dataStr, listener, requestCode);

	}

	/**
	 * 家长版协议 个人中心更新用户信息
	 * 
	 * @author: sky
	 * @param queue
	 * @param name
	 * @param background
	 * @param avatar
	 * @param gradeid
	 * @param school
	 * @param sex
	 * @param province
	 * @param city
	 * @param listener
	 * @param requestCode
	 *            void
	 */
	public void getUserinfos(RequestQueue queue, final BaseActivity listener, final int requestCode) {
		// http://www.welearn.com:8080/api/parents/userinfos
		Map<String, Object> subParams = new HashMap<String, Object>();
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/userinfos ", dataStr, listener,
				requestCode);

	}

	/**
	 * 家长版协议 个人中心更新用户信息
	 * @param queue
	 * @param listener
	 * @param requestCode
	 */
	public void getUserinfos(RequestQueue queue, final BaseFragment listener, final int requestCode) {
		// http://www.welearn.com:8080/api/parents/userinfos
		Map<String, Object> subParams = new HashMap<String, Object>();
		String dataStr = JSON.toJSONString(subParams);
		requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "parents/userinfos ", dataStr, listener,
				requestCode);

	}

}
