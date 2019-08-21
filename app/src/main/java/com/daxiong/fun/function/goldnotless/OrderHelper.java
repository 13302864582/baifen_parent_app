package com.daxiong.fun.function.goldnotless;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.util.LogUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

public class OrderHelper {
	
	/**
	 * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
	 * 
	 * @return
	 * @author SHANHY
	 */
	public static String getPsdnIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						// if (!inetAddress.isLoopbackAddress() && inetAddress
						// instanceof Inet6Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}
	
	
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
			LogUtils.e("ifo", e.toString());
		}
		return "";
	}

	public static String getUserAgent() {
		WebView webview;
		webview = new WebView(MyApplication.getContext());
		webview.layout(0, 0, 0, 0);
		WebSettings settings = webview.getSettings();
		String ua = settings.getUserAgentString();
		LogUtils.i("UA", ua);
		return ua;
	}

	@SuppressWarnings("deprecation")
	public static String getNewOrderInfo(OrderModel order) {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder sb = new StringBuilder();
		sb.append("app_id=2018031402368430");
		sb.append("&method=alipay.trade.app.pay");
		sb.append("&format=json");
		sb.append("&charset=UTF-8");
		sb.append("&sign_type=RSA2");
		sb.append("&timestamp="+sdf.format(d));
		sb.append("&biz_content={\"out_trade_no\":\""+order.orderid+"\", \"subject\":\""+order.subject+"\", \"body\":\""+order.body+"\", \"total_fee\":\""+order.price+"\"}");
		sb.append("&version=1.0");
		sb.append("&notify_url="+AppConfig.ALIURL + "callback");
		return new String(sb);
	}
	/*public static String getNewOrderInfo(OrderModel order) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(order.orderid);
		sb.append("\"&subject=\"");
		sb.append(order.subject);
		sb.append("\"&body=\"");
		sb.append(order.body);
		sb.append("\"&total_fee=\"");
		sb.append(order.price);

		sb.append("\"&notify_url=\"");
		// 网址需要做URL编码
		sb.append(URLEncoder.encode(AppConfig.ALIURL + "callback"));

		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}*/

	@SuppressLint("SimpleDateFormat")
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		// Log.d(TAG, "outTradeNo: " + key);
		return key;
	}

	public static final String TAG = "alipay-sdk";

	public static final int RQF_PAY = 1;

	public static final int RQF_LOGIN = 2;

	public static String getSignType() {
		return "sign_type=\"RSA2\"";
	}

}
