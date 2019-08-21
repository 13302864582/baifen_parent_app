
package com.daxiong.fun.function.goldnotless;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MainActivity;
import com.daxiong.fun.R;
import com.daxiong.fun.api.VIPAPI;
import com.daxiong.fun.api.WXPayApi;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.common.WebViewActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.constant.WxConstant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.account.model.PayCardModel;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MyAsyncTask;
import com.daxiong.fun.util.NetworkUtils;
import com.daxiong.fun.util.ThreadPoolUtil;
import com.daxiong.fun.util.ToastUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 此类的描述： 支付页面
 * 
 * @author: sky
 */
public class PayActivity extends BaseActivity implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {

	private CheckBox weChatCheckBox;

	private CheckBox aliCheckBox;

	private CheckBox uniCheckBox;

	public static final String EXTRA_TAG_PAY_MODEL = "extra_tag_pay_model";

	public static final String EXTRA_TAG_UID = "extra_tag_uid";

	public static final String EXTRA_TAG_MONEY = "extra_tag_money";

	public static final String EXTRA_TAG_TEXT = "extra_tag_text";

	private IWXAPI msgApi;

	private PayCardModel payCardModel;

	private int fromUserId, toUserId;
	private String golangorderid;// 预支付订单

	private TextView payInfoTV;

	private TextView tel_pay_submit_bt;

	private float money = 0F;

	private LinearLayout layout_chongzhixieyi;

	private RelativeLayout layout_zhifubao;
	private RelativeLayout layout_weixin;

	public static final int CONFIRM_PAY = 3232;

	private SwipeRefreshLayout mSwipeLayout;

	private static final int REFRESH_COMPLETE = 0X110;

	private VIPAPI vipApi = null;
	
	private String content2="";
	private String body="";

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String msgStr = (String) msg.obj;
			switch (msg.what) {
			case GlobalContant.CLOSEDIALOG:
				if (isShow) {
					closeDialog();
					isShow = false;
					ToastUtils.show("连接超时");
				}
				break;
			case GlobalContant.ERROR_MSG:
				ToastUtils.show(msgStr);
				break;
			case OrderHelper.RQF_PAY:
			case OrderHelper.RQF_LOGIN:
				Result resultObj = new Result(msgStr);
				String result = resultObj.getResult();
				if (!TextUtils.isEmpty(result)) {
					Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
					//Toast.makeText(PayActivity.this, "支付成功,支付码:" + resultObj.resultStatus, Toast.LENGTH_SHORT).show();
					mTask = new PayAsyncTask(result, "content");
					mTask.excute();
				}
				break;
			case YEEPAY:
				mTask = new PayAsyncTask(msgStr, "orderid");
				mTask.excute();
				break;
			case CONFIRM_PAY:// 支付确认
				String resultStr = (String) msg.obj;
				int code = JsonUtil.getInt(resultStr, "code", 0);
				double trade_coin = JsonUtil.getDouble(resultStr, "trade_coin", 0);
				double all_coin = JsonUtil.getDouble(resultStr, "all_coin", 0);
				String errmsg = JsonUtil.getString(resultStr, "errmsg", "");

				closeDialog();
				mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
				isShow = false;
				// ToastUtils.show("code-->"+code);
				if (code == 0) {
					UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
					if (null != uInfo) {
						uInfo.setGold((float) all_coin);
						DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
						Intent intent2 = new Intent(PayActivity.this, MainActivity.class);
						Bundle data2 = new Bundle();
						data2.putString("layout", "layout_home");
						intent2.putExtras(data2);
						startActivity(intent2);
						finish();
					}
				} else {
					if (!TextUtils.isEmpty(errmsg)) {
						ToastUtils.show(errmsg, 1);
					}
				}
				break;
			case REFRESH_COMPLETE: // 下拉刷新
				mSwipeLayout.setRefreshing(false);
				ToastUtils.showCustomToast(PayActivity.this, "刷新成功!");
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pay);
		initView();
		initListener();
	}

	@Override
	public void initView() {
		super.initView();
		setWelearnTitle(R.string.recharge);

		findViewById(R.id.back_layout).setOnClickListener(this);

		payInfoTV = (TextView) findViewById(R.id.pay_text_info_tv);
		tv_shijian = (TextView) findViewById(R.id.tv_shijian);
		tv_feiyong = (TextView) findViewById(R.id.tv_feiyong);
		tel_pay_submit_bt = (TextView) findViewById(R.id.tel_pay_submit_bt);
		tel_pay_submit_bt.setOnClickListener(this);
		layout_chongzhixieyi = (LinearLayout) this.findViewById(R.id.layout_chongzhixieyi);
		layout_zhifubao = (RelativeLayout) this.findViewById(R.id.layout_zhifubao);
		layout_weixin = (RelativeLayout) this.findViewById(R.id.layout_weixin);

		// 微信支付方式选择状态CheckBox
		weChatCheckBox = (CheckBox) findViewById(R.id.wechat_pay_radiobtn);
		// 支付宝支付方式选择状态CheckBox
		aliCheckBox = (CheckBox) findViewById(R.id.alipay_radio_btn);
		// 银联方式支付选择状态CheckBox
		uniCheckBox = (CheckBox) findViewById(R.id.union_radio_btn);

		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);

		mSwipeLayout.setOnRefreshListener(this);
//		mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//				android.R.color.holo_orange_light, android.R.color.holo_red_light);

		vipApi = new VIPAPI();

		Intent extraIntent = getIntent();
		if (null != extraIntent) {
			payCardModel = (PayCardModel) extraIntent.getSerializableExtra(EXTRA_TAG_PAY_MODEL);
			toUserId = extraIntent.getIntExtra(EXTRA_TAG_UID, 0);
			golangorderid = extraIntent.getStringExtra("golangorderid");

			UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
			if (null == uInfo) {
				ToastUtils.show(R.string.params_error);
				finish();
			}

			fromUserId = uInfo.getUserid();

			if (null == payCardModel || toUserId == 0) {
				ToastUtils.show(R.string.params_error);
				finish();
			}
			money = payCardModel.getMoney();
			//money = 0.01f;
			content2 = payCardModel.getContent_2();
			String product = payCardModel.getProduct();
			uMengEvent("openrecharge_" + (int) money);
			payInfoTV.setText(payCardModel.getContent_2());
			tv_shijian.setText("时间 ：" + payCardModel.getServer_time());
			tv_feiyong.setText("费用 :" + payCardModel.getMoney() + "元");
		} else {
			ToastUtils.show(R.string.params_error);
			finish();
		}

	}

	public void initListener() {
		layout_chongzhixieyi.setOnClickListener(this);
		layout_zhifubao.setOnClickListener(this);
		layout_weixin.setOnClickListener(this);
		weChatCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					aliCheckBox.setChecked(false);
					uniCheckBox.setChecked(false);
				}

			}
		});
		aliCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					weChatCheckBox.setChecked(false);
					uniCheckBox.setChecked(false);
				}

			}
		});
		uniCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					aliCheckBox.setChecked(false);
					weChatCheckBox.setChecked(false);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.tel_pay_submit_bt:
			MobclickAgent.onEvent(this, "Buy_Confirm");
			if (money == 0) {
				ToastUtils.show(R.string.pay_tips_select_count);
				break;
			}
			if (weChatCheckBox.isChecked()) {
				wxpay();
			}
			if (aliCheckBox.isChecked()) {
				alipay();
			}
			if (uniCheckBox.isChecked()) {
				yeepay();
			}
			break;
		case R.id.layout_zhifubao:
			aliCheckBox.setChecked(true);
			weChatCheckBox.setChecked(false);
			uniCheckBox.setChecked(false);
			break;
		case R.id.layout_weixin:
			weChatCheckBox.setChecked(true);
			aliCheckBox.setChecked(false);
			uniCheckBox.setChecked(false);
			break;
		case R.id.layout_chongzhixieyi:
			Intent chongzhiIntent = new Intent(this, WebViewActivity.class);
			chongzhiIntent.putExtra("title", "充值协议");
			chongzhiIntent.putExtra("url", AppConfig.FUDAOTUAN_URL + "/aboutf.html");
			startActivity(chongzhiIntent);
			break;

		}
	}

	private void phonepay() {
		Bundle bundle = new Bundle();
		int mon = (int) (money + 0.005);
		uMengEvent("rechargcard_" + mon);
		bundle.putString(EXTRA_TAG_TEXT, "充值" + mon + "学点");

		bundle.putInt(EXTRA_TAG_MONEY, mon);
		bundle.putInt(EXTRA_TAG_UID, toUserId);

		IntentManager.goToPhoneCardPayActivity(this, bundle);
	}

	/**
	 * 微信支付
	 */
	private void wxpay() {
		uMengEvent("wxpay_" + (int) money);
		msgApi = WXAPIFactory.createWXAPI(PayActivity.this, WxConstant.APP_ID_WW, true);
		boolean registerApp = msgApi.registerApp(WxConstant.APP_ID_WW);
		if (!msgApi.isWXAppInstalled()) {
			Toast.makeText(PayActivity.this, "微信未安装", Toast.LENGTH_SHORT).show();
			return;
		}

		if (!registerApp) {
			Toast.makeText(PayActivity.this, "微信注册失败", Toast.LENGTH_SHORT).show();
			return;
		}
		showDialog(getString(R.string.getting_prepayid));

		 
		if (fromUserId == toUserId) {
			body = getString(R.string.pay_user_x_pay, fromUserId);
		} else {
			body = getString(R.string.pay_for_others_user_x_pay, fromUserId, toUserId);
		}
		body=content2;
		new Thread(new Runnable() {
			public void run() {
				executeWXHttp(toUserId, fromUserId, money, body, payCardModel.getType(), golangorderid);
			}
		}).start();

	}

	/**
	 * 阿里支付
	 */
	private void alipay() {
		uMengEvent("alipay_" + (int) money);
		isShow = true;
		showDialog(getString(R.string.please_wait));
		mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 40000);
		final OrderModel order = new OrderModel();
		MobclickAgent.onEvent(this, "recharge_" + money);
		order.price = money;
		// order.price=0.01f;//暂时设置为0.01
		order.userid = toUserId;
		order.orderid = OrderHelper.getOutTradeNo();
		order.body = "可购得学点" + money + "个";
		if (fromUserId == toUserId) {
			order.subject = getString(R.string.pay_user_x_pay, order.userid);
		} else {
			order.subject = getString(R.string.pay_for_others_user_x_pay, fromUserId, order.userid);
		}
		order.subject = content2;
		final String info = OrderHelper.getNewOrderInfo(order);

		ThreadPoolUtil.execute(new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				// /Uri uri = Uri.parse(Config.URL);
				HttpPost post = new HttpPost(AppConfig.ALIURL + "orderencrypt");

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("content", info));
				params.add(new BasicNameValuePair("userid", String.valueOf(order.userid)));
				params.add(new BasicNameValuePair("fromuserid", String.valueOf(fromUserId)));
				params.add(new BasicNameValuePair("servertype", String.valueOf(payCardModel.getType())));

				params.add(new BasicNameValuePair("golangorderid", golangorderid));
				HttpResponse httpResponse = null;
				String result = "";
				try {
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					httpResponse = new DefaultHttpClient().execute(post);
					result = EntityUtils.toString(httpResponse.getEntity());
				} catch (Exception e) {
					e.printStackTrace();
				}

				// if (httpResponse.getStatusLine().getStatusCode() ==
				// 200)
				// {
				int code = JsonUtil.getInt(result, "code", 0);
				String sign = JsonUtil.getString(result, "content", "");
				String errmsg = JsonUtil.getString(result, "errmsg", "");

				System.out.println("转换qian--->" + sign);
				sign = URLEncoder.encode(sign);
				System.out.println("转换hou--->" + sign);
				final String orderinfo = info + "&sign=\"" + sign + "\"&" + getSignType();
				closeDialog();
				mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
				isShow = false;
				if (code == 0) {
					//AliPay alipay = new AliPay(PayActivity.this, mHandler);
					PayTask alipay = new PayTask(PayActivity.this);
					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);
					String payResult = alipay.pay(orderinfo,true);
					// Log.i(TAG, "result = " + result);
					Message msg = new Message();
					msg.what = OrderHelper.RQF_PAY;
					msg.obj = payResult;
					mHandler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = 987;
					msg.obj = errmsg;
					mHandler.sendMessage(msg);
				}
			}
		});
	}

	/**
	 * 易宝支付(银联支付)
	 * 
	 * @author: sky void
	 */
	private void yeepay() {
		uMengEvent("bankcard_" + (int) money);
		double amount = money * 100;
		if (AppConfig.IS_DEBUG) {
			// amount = 20;
		}
		isShow = true;
		showDialog(getString(R.string.please_wait));
		mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 40000);
		MobclickAgent.onEvent(this, "recharge_" + money + "_yeepay");
		final JSONObject json = new JSONObject();
		final String orderid = OrderHelper.getOutTradeNo();
		try {
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			json.put("merchantaccount", AppConfig.YEEID);

			json.put("orderid", orderid);
			json.put("transtime", (int) (System.currentTimeMillis() / 1000));
			json.put("amount", amount);// 单位是分
			json.put("productcatalog", "10");// 10代表行政教育 1是测试
			if (fromUserId == toUserId) {
				json.put("productname", "大熊作业-用户" + toUserId + "学点充值" + money + "元");
			} else {
				json.put("productname", "大熊作业-用户" + fromUserId + "为用户" + toUserId + "学点充值" + money + "元");
			}
			json.put("productdesc", "");
			json.put("identityid", String.valueOf(toUserId));
			json.put("identitytype", 2);// 用户标示类型, 2代表用户ID
			json.put("terminaltype", 0);// 设备标示类型, 0代表设备IMEI
			json.put("terminalid", tm.getDeviceId());
			json.put("userip", OrderHelper.getPsdnIp());
			json.put("userua", OrderHelper.getUserAgent());
			json.put("callbackurl", AppConfig.YEEURL + "callback");
			json.put("fcallbackurl", AppConfig.YEEURL + "callback");
			LogUtils.e("json:", json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ThreadPoolUtil.execute(new Runnable() {
			@Override
			public void run() {
				HttpPost post = new HttpPost(AppConfig.YEEURL + "orderencrypt");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("content", json.toString()));
				params.add(new BasicNameValuePair("userid", String.valueOf(toUserId)));
				params.add(new BasicNameValuePair("fromuserid", String.valueOf(fromUserId)));
				params.add(new BasicNameValuePair("servertype", String.valueOf(payCardModel.getType())));
				HttpResponse httpResponse = null;
				String result = "";
				try {
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					httpResponse = new DefaultHttpClient().execute(post);
					result = EntityUtils.toString(httpResponse.getEntity());
					LogUtils.e("result:", result);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// if (httpResponse.getStatusLine().getStatusCode() == 200)
				// {
				int code = JsonUtil.getInt(result, "code", -1);
				String errmsg = JsonUtil.getString(result, "errmsg", "");
				closeDialog();
				mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
				isShow = false;

				if (code == 0) {
					String resultJson = JsonUtil.getString(result, "content", "");
					String data = JsonUtil.getString(resultJson, "data", "");
					String merchantaccount = JsonUtil.getString(resultJson, "merchantaccount", "");
					String encryptkey = JsonUtil.getString(resultJson, "encryptkey", "");
					String okURL = "https://ok.yeepay.com/paymobile/mobile/pay/request";
					@SuppressWarnings("deprecation")
					String yeepayUrl = okURL + "?merchantaccount=" + URLEncoder.encode(merchantaccount) + "&data="
							+ URLEncoder.encode(data) + "&encryptkey=" + URLEncoder.encode(encryptkey);
					// Bundle bundle = new Bundle();
					// // GlobalVariable.payFragment = PayActivity.this;
					// bundle.putString(AuthActivity.AUTH_URL, yeepayUrl);
					// bundle.putString("orderid", orderid);
					// IntentManager.gotoAuthView(PayActivity.this, bundle);
					Intent intent = new Intent(PayActivity.this, WebViewActivity.class);
					intent.putExtra("title", "大熊作业");
					intent.putExtra("url", yeepayUrl);
					startActivity(intent);
				} else {
					Message msg = new Message();
					msg.what = GlobalContant.ERROR_MSG;
					msg.obj = errmsg;
					mHandler.sendMessage(msg);
				}
			}
		});
	}

	private String getSignType() {
		return "sign_type=\"RSA2\"";
	}

	private class PayAsyncTask extends MyAsyncTask {
		private String mResult;

		private int code;

		@SuppressWarnings("unused")
		private double trade_coin;

		private String tag;

		public PayAsyncTask(String result, String tag) {
			super();
			this.mResult = result;
			this.tag = tag;
		}

		@Override
		public void preTask() {
			isShow = true;
			showDialog(getString(R.string.please_wait));
			mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 40000);
		}

		@Override
		public void doInBack() {
			String url = "";
			if ("content".equals(tag)) {
				url = AppConfig.ALIURL;
			} else if ("orderid".equals(tag)) {
				url = AppConfig.YEEURL;
			}
			HttpPost post = new HttpPost(url + "orderconfirm");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(tag, mResult));
			params.add(new BasicNameValuePair("user_id", String.valueOf(toUserId)));
			HttpResponse httpResponse = null;
			String result = "";
			try {
				post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				httpResponse = new DefaultHttpClient().execute(post);
				result = EntityUtils.toString(httpResponse.getEntity());
			} catch (Exception e) {
				e.printStackTrace();
			}
			LogUtils.e("result:", result);
			// if (httpResponse.getStatusLine().getStatusCode() == 200) {
			// code = JsonUtil.getInt(result, "code", 0);
			// trade_coin = JsonUtil.getDouble(result, "trade_coin", 0);
			// all_coin = JsonUtil.getDouble(result, "all_coin", 0);
			// errmsg = JsonUtil.getString(result, "errmsg", "");
			// }

			Message msg = Message.obtain();
			msg.what = CONFIRM_PAY;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

		private String errmsg;

		private double all_coin;

		@Override
		public void postTask() {

			// closeDialog();
			// mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
			// isShow = false;
			// // ToastUtils.show("code-->"+code);
			// if (code == 0) {
			// UserInfoModel uInfo =
			// DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
			// // if (null != uInfo) {
			// Toast.makeText(PayActivity.this, "支付成功",
			// Toast.LENGTH_SHORT).show();
			// uInfo.setGold((float) all_coin);
			// DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
			// Intent intent2 = new Intent(PayActivity.this,
			// MainActivity.class);
			// Bundle data2 = new Bundle();
			// data2.putString("layout", "layout_home");
			// intent2.putExtras(data2);
			// startActivity(intent2);
			// finish();
			// // }
			// }
			// if (!TextUtils.isEmpty(errmsg)) {
			// ToastUtils.show(errmsg, 1);
			// }

		}
	}

	private PayAsyncTask mTask;

	private boolean isShow = false;

	public Handler getHandler() {
		return mHandler;
	}

	public static final int YEEPAY = 111;

	private TextView tv_shijian;

	private TextView tv_feiyong;

	public void executeWXHttp(int userid, int fromuserid, float totalfee, String body, int servertype,
			String golangorderid) {
		new WXPayApi().WXpay(requestQueue, userid, fromuserid, totalfee, body, servertype, golangorderid,
				PayActivity.this, RequestConstant.GET_WXPAY);

	}

	public void executePay(JSONObject dataJson) {

		PayReq req = new PayReq();

		String transactionNo = dataJson.optString("transactionNo");
		String partnerid = dataJson.optString("partnerid");
		String appid = dataJson.optString("appid");
		String prepayid = dataJson.optString("prepayid");
		String timestamp = dataJson.optString("timestamp");
		String noncestr = dataJson.optString("noncestr");
		String packages = dataJson.optString("package");
		String sign = dataJson.optString("sign");

		req.appId = appid;
		req.partnerId = partnerid;
		req.prepayId = prepayid;
		req.packageValue = packages;
		req.nonceStr = noncestr;
		req.timeStamp = timestamp;
		req.sign = sign;

		closeDialog();
		msgApi.registerApp(WxConstant.APP_ID_WW);
		msgApi.sendReq(req);

	}

	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);
		int flag = ((Integer) param[0]).intValue();
		switch (flag) {
		case RequestConstant.GET_WXPAY:// 微信支付请求
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtil.getInt(datas, "Code", -1);
				String msg = JsonUtil.getString(datas, "Msg", "");
				if (code == 0) {
					try {
						String dataJson = JsonUtil.getString(datas, "Data", "");
						if (!TextUtils.isEmpty(dataJson)) {
							JSONObject rootJson = new JSONObject(dataJson);
							executePay(rootJson);
						} else {
							closeDialog();
							ToastUtils.show(msg);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					closeDialog();
					ToastUtils.show(msg);
				}

			}
			break;
		case RequestConstant.REFRESH_PAY_INFO:// 刷新支付信息
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtil.getInt(datas, "Code", -1);
				String msg = JsonUtil.getString(datas, "Msg", "");
				if (code == 0) {
					try {
						closeDialog();
						String dataJson = JsonUtil.getString(datas, "Data", "");
						if (!TextUtils.isEmpty(dataJson)) {
							float price = (float) JsonUtil.getDouble(dataJson, "price", 0D);
							payCardModel.setMoney(price);
							tv_feiyong.setText("费用 :" + payCardModel.getMoney() + "元");

						} else {
							closeDialog();
							ToastUtils.show(msg);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					closeDialog();
					ToastUtils.show(msg);
				}

			}
			break;
		case -1:
			closeDialog();
			Toast.makeText(PayActivity.this, "通讯异常", Toast.LENGTH_SHORT).show();
			break;
		}

	}

	@Override
	public void onRefresh() {
		if (NetworkUtils.getInstance().isInternetConnected(this)) {
			showDialog("加载中");
			vipApi.refreshPaymentInfos(requestQueue, golangorderid, this, RequestConstant.REFRESH_PAY_INFO);
			mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
		}else {
			ToastUtils.show("没有网络连接，请检查网络");
		}
		
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		closeDialog();
		mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
	}

}
