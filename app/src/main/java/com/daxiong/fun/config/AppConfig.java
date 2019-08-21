
package com.daxiong.fun.config;

import com.daxiong.fun.view.MySpUtil;

/**
 * 学生端url配置
 * 
 * @author: sky @最后修改人： sky
 * @最后修改日期:2015年8月7日 上午10:21:57
 */
public class AppConfig {     
	// api接口url
	public static String GO_URL;

	//private static String WORKURL = "api.welearn.com";
	/*api.daxiong.fun*/

	private static String WORKURL = "api.baifen.lantel.net";
	// 用于活动的url
	public static String FUDAOTUAN_URL = "http://www.daxiong.fun";
	//用于活动分享的url
	//public static String WELEARN_URL="http://www.welearn.com/";
	public static String WELEARN_URL="http://www.daxiong.fun/";
	// app更新url
	public static final String UPDATEURL;
	// 聊天websocket服务器
	public static String PYTHON_URL;
	// 支付宝
	public static final String ALIURL;
	// 网银
	public static final String YEEURL;
	// 易宝支付(银联支付)
	public static final String YEEID = "10012423489";
	// 手机
	public static final String CARDURL;
	// 微信支付url
	public static String GET_WXPAY = "";
	// 错误邮件url
	public static String MAIL_URL;

	public static final String PAY_FOR_OTHER_HOST;

	public static final String WELCOME_IMAGE_CHECK_URL = "http://manage.daxiong.fun/app_guide.php";

	public static String RECHARGE_CONFIG_URL;

	// 作业分析分享Url
	public static final String HOMEWORK_STUDY_ANALYSIS_SHARE_URL;

	public static final String SHARE_URL = "http://d.daxiong.fun/?app=1&userid=";

	// 线上测试环境
	private static final String PAY_ONLINE_TEST = "http://192.168.1.100:9111/welearn/";

	// 是否是调试模式
	public static final boolean IS_DEBUG =false;
	// 是否是线上测试环境
	public static final boolean IS_ONLINE = false;

	public static void loadIP() {
		GO_URL = "http://" + MySpUtil.getInstance().getGOTP() + "/api/";
		PYTHON_URL = "ws://" + MySpUtil.getInstance().getPYTHONTP() + "/ws";

//		 execUrl();

	}

	static {
		// IS_DEBUG 控制是否是测试环境 IS_195控制测试环境的正式环境
		// 发布版本的时候注意 更改IS_DEBUG=false IS_195=false;

		if (IS_DEBUG) {
			loadIP();

			// 检测升级地址
			UPDATEURL = "http://manage.daxiong.fun/app_version.php?os=2&roleid=3";
			// 线上测试
			MAIL_URL = "http://192.168.1.100:7001/api/mail/errormail";
			// 线上测试 由于本地无法测试需要微信公网回调，下面的地址是线上测试环境
			GET_WXPAY = "http://192.168.1.100:9111/welearn/parentswechat";

			RECHARGE_CONFIG_URL = PAY_ONLINE_TEST + "payment/configure";
			ALIURL = PAY_ONLINE_TEST + "alipay/";
			YEEURL = PAY_ONLINE_TEST + "yeepay/";
			CARDURL = PAY_ONLINE_TEST + "card_yeepay/";
			PAY_FOR_OTHER_HOST = PAY_ONLINE_TEST + "payment/pay2friend";
			HOMEWORK_STUDY_ANALYSIS_SHARE_URL = "http://172.16.1.100/share%s-%s.html";
			FUDAOTUAN_URL = "http://test.fudaotuan.com";
			WELEARN_URL="http://test.welearn.com/";
		} else {
			GO_URL = "https://" + WORKURL + "/api/";// homework/";
			PYTHON_URL = "ws://" + WORKURL + "/ws";
            UPDATEURL = "http://manage.baifen.lantel.net/app_version.php?os=2&roleid=3"; // 1是学生 3是家长
			MAIL_URL = "https://" + WORKURL + "/api/mail/errormail";
            RECHARGE_CONFIG_URL = "https://" + WORKURL + "/welearn/payment/configure";
            ALIURL = "https://" + WORKURL + "/welearn/alipay/";
            YEEURL = "https://" + WORKURL + "/welearn/yeepay/";
            CARDURL = "https://" + WORKURL + "/welearn/card_yeepay/";
            GET_WXPAY = "https://" + WORKURL + "/welearn/parentswechat";
            PAY_FOR_OTHER_HOST = "https://" + WORKURL + "/welearn/payment/pay2friend";
            HOMEWORK_STUDY_ANALYSIS_SHARE_URL = FUDAOTUAN_URL + "/share%s-%s.html";
            FUDAOTUAN_URL = "http://web.baifen.lantel.net";
            WELEARN_URL="http://web.baifen.lantel.net/";
		}
	}

	public static void execUrl() {
		GO_URL = "http://192.168.1.100:7001/api/";//
		// GO_URL = "http://172.16.1.24:82/api/";
		// GO_URL = "http://172.16.1.30:10000/api/";//
		// PYTHON_URL = "ws://" + "172.16.1.13:9800" + "/ws";// temp
		PYTHON_URL = "ws://" + "192.168.1.100:6000" + "/ws";
		GET_WXPAY = "http://192.168.1.100:9111/welearn/wechat";
		MAIL_URL = "http://192.168.1.100:7001/api/mail/errormail";

	}

}
