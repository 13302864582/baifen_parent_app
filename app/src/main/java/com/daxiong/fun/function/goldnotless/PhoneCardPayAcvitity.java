package com.daxiong.fun.function.goldnotless;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MyAsyncTask;
import com.daxiong.fun.util.ThreadPoolUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.SegmentedControl;
import com.daxiong.fun.view.SegmentedControl.OnSegmentChangedListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhoneCardPayAcvitity extends BaseActivity implements OnClickListener {

	private String pd_FrpId = "SZX";
	private int fromUserId, toUserId;
	private int money;
	private EditText cardId;
	private EditText num;
	private Button submit;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GlobalContant.CLOSEDIALOG:
				if (isShowDialog) {
					closeDialog();
					isShowDialog = false;
					ToastUtils.show("连接超时");
				}
				break;
			case GlobalContant.EXCUTE_ASYNCTASK:
				new PhoneCardPayAsyncTask((String) msg.obj).excute();
				break;
			case GlobalContant.ERROR_MSG:
				String errmsg = (String) msg.obj;
				if (isShowDialog) {
					closeDialog();
					isShowDialog = false;
					mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
				}
				if (!TextUtils.isEmpty(errmsg)) {
					ToastUtils.show(errmsg, 0);
				}
				break;
			}
		}
	};
	private String pa8_cardNo;
	private String pa9_cardPwd;
	private SegmentedControl chanel;
	private EditText face;
	private String pa7_cardAmt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_phone_card_pay);

		findViewById(R.id.back_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		Intent intent = getIntent();
		if (null == intent) {
			ToastUtils.show(R.string.params_error);
			finish();
		}
		money = intent.getIntExtra(PayActivity.EXTRA_TAG_MONEY, 0);
		toUserId = intent.getIntExtra(PayActivity.EXTRA_TAG_UID, 0);

		UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();

		if (money == 0 || toUserId == 0 || null == uInfo) {
			ToastUtils.show(R.string.params_error);
			finish();
		}

		fromUserId = uInfo.getUserid();

		chanel = (SegmentedControl) findViewById(R.id.cardtype_phone_card_pay);
		chanel.setStyle(SegmentedControl.SEGMENT);
		chanel.newButton("中国移动", 0);
		chanel.newButton("中国联通", 1);
		chanel.newButton("中国电信", 2);
		chanel.setSelectedIndex(0);

		cardId = (EditText) findViewById(R.id.cardid_phone_card_pay);
		num = (EditText) findViewById(R.id.num_phone_card_pay);
		face = (EditText) findViewById(R.id.face_phone_card_pay);
		if (AppConfig.IS_DEBUG) {
			pa8_cardNo = "14463221256083344";
			pa9_cardPwd = "221594653900332359";
			pa7_cardAmt = "30";

			cardId.setText(pa8_cardNo);
			num.setText(pa9_cardPwd);
			face.setText(pa7_cardAmt);
		}
		chanel.setOnSegmentChangedListener(new OnSegmentChangedListener() {
			@Override
			public void onSegmentChanged(int index) {
				chanel.setSelectedIndex(index);
				if (index == 0) {
					pd_FrpId = "SZX";
				} else if (index == 1) {
					pd_FrpId = "UNICOM";
				} else if (index == 2) {
					pd_FrpId = "TELECOM";
				}

				if (AppConfig.IS_DEBUG) {
					switch (index) {
					case 0:
						pa8_cardNo = "14463221256083344";
						pa9_cardPwd = "221594653900332359";
						pa7_cardAmt = "30";

						cardId.setText(pa8_cardNo);
						num.setText(pa9_cardPwd);
						face.setText(pa7_cardAmt);
						break;
					case 1:
						pa8_cardNo = "811401179264909";
						pa9_cardPwd = "8101384904622393858";
						pa7_cardAmt = "30";

						cardId.setText(pa8_cardNo);
						num.setText(pa9_cardPwd);
						face.setText(pa7_cardAmt);
						break;
					case 2:
						pa8_cardNo = "7551001305010016012";
						pa9_cardPwd = "205070241397288794";
						pa7_cardAmt = "50";
						cardId.setText(pa8_cardNo);
						num.setText(pa9_cardPwd);
						face.setText(pa7_cardAmt);
						break;
					}
				}
			}
		});

		submit = (Button) findViewById(R.id.submit_phone_card_pay);
		submit.setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText("手机卡充值");

	}

	@Override
	public void onClick(View v) {
		if (System.currentTimeMillis() - clickTime < 2000) {
			return;
		}
		clickTime = System.currentTimeMillis();
		pa7_cardAmt = face.getText().toString().trim();
		pa8_cardNo = cardId.getText().toString().trim();
		pa9_cardPwd = num.getText().toString().trim();
		if (TextUtils.isEmpty(pa8_cardNo) || TextUtils.isEmpty(pa9_cardPwd)) {
			ToastUtils.show("请输入充值卡号和密码");
			return;
		}

		if (TextUtils.isEmpty(pa7_cardAmt)) {
			pa7_cardAmt = money + "";
			face.setText(pa7_cardAmt);
		}

		isShowDialog = true;
		showDialog("请稍候");
		mHandler.sendEmptyMessageDelayed(GlobalContant.CLOSEDIALOG, 40000);
		final JSONObject json = new JSONObject();
		final String orderid = OrderHelper.getOutTradeNo();
		try {

			json.put("p0_Cmd", "ChargeCardDirect");// 非银行卡

			json.put("p1_MerId", AppConfig.YEEID);
			json.put("p2_Order", orderid);
			json.put("p3_Amt", money);// 单位是元,精确到分
			json.put("p4_verifyAmt", true);// true为检验金额

			// json.put("p5_Pid", "name");// 名称
			// json.put("p6_Pcat", "type");// 类型
			// json.put("p7_Pdesc", "desc");// 描述

			json.put("p8_Url", AppConfig.CARDURL + "callback");// 回调地址
			// json.put( "pa_MP", "pa_MP");// 拓展信息
			json.put("pa7_cardAmt", pa7_cardAmt);// 卡片面额
			json.put("pa8_cardNo", pa8_cardNo);// 卡号
			json.put("pa9_cardPwd", pa9_cardPwd);// 密码
			json.put("pd_FrpId", pd_FrpId);// 支付渠道

			/**
			 * 收到易宝支付服务器点对点支付成功通知，必须回写以”success”（无关大小写）开头的字符串，
			 * 即使您收到成功通知时发现该订单已经处理过，也要正确回写”success”，否则易宝支付将认为您的系统没有收到通知，
			 * 启动重发机制，直到收到”success”为止。
			 */
			json.put("pr_NeedResponse", 1);// 应答机制
			// json.put("pz_userId", WeLearnSpUtil.getInstance().getUserId());//
			// 用户ID
			// json.put("pz1_userRegTime", "1");// 用户注册时间
		} catch (Exception e) {
			e.printStackTrace();
		}

		ThreadPoolUtil.execute(new Runnable() {

			@Override
			public void run() {
				HttpPost post = new HttpPost(AppConfig.CARDURL + "orderencrypt");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("content", json.toString()));
				params.add(new BasicNameValuePair("userid", String.valueOf(toUserId)));
				params.add(new BasicNameValuePair("fromuserid", String.valueOf(fromUserId)));
				HttpResponse httpResponse = null;
				String result = "";
				try {
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					httpResponse = new DefaultHttpClient().execute(post);
					result = EntityUtils.toString(httpResponse.getEntity());
					LogUtils.e("ORDERresult:", result);
				} catch (Exception e) {
					e.printStackTrace();
				}
				int code = JsonUtil.getInt(result, "code", -1);
				String hmac_content = JsonUtil.getString(result, "content", "");
				String errmsg = JsonUtil.getString(result, "errmsg", "");
				if (code == 0) {
					// String hmac = JSONUtils.getString(result, "content", "");
					String okURL = "https://www.yeepay.com/app-merchant-proxy/command.action";
					post = new HttpPost(okURL);
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("p0_Cmd", "ChargeCardDirect"));
					params.add(new BasicNameValuePair("p1_MerId", AppConfig.YEEID));
					params.add(new BasicNameValuePair("p2_Order", orderid));
					params.add(new BasicNameValuePair("p3_Amt", "" + money));

					params.add(new BasicNameValuePair("p4_verifyAmt", "true"));

					// params.add(new BasicNameValuePair("p5_Pid", "name"));
					// params.add(new BasicNameValuePair("p6_Pcat", "type"));
					// params.add(new BasicNameValuePair("p7_Pdesc", "desc"));
					// params.add(new BasicNameValuePair("pa_MP", "pa_MP"));

					params.add(new BasicNameValuePair("p8_Url", AppConfig.CARDURL + "callback"));
					params.add(new BasicNameValuePair("pa7_cardAmt", pa7_cardAmt));
					params.add(new BasicNameValuePair("pa8_cardNo", pa8_cardNo));
					params.add(new BasicNameValuePair("pa9_cardPwd", pa9_cardPwd));
					params.add(new BasicNameValuePair("pd_FrpId", pd_FrpId));
					params.add(new BasicNameValuePair("pr_NeedResponse", "1"));
					// params.add(new BasicNameValuePair("pz_userId",
					// WeLearnSpUtil.getInstance().getUserId() + ""));
					// params.add(new BasicNameValuePair("pz1_userRegTime",
					// "1"));
					params.add(new BasicNameValuePair("hmac", hmac_content));

					try {
						post.setEntity(new UrlEncodedFormEntity(params, "GBK"));
						httpResponse = new DefaultHttpClient().execute(post);
						result = EntityUtils.toString(httpResponse.getEntity());
						LogUtils.e("YEEresult:", result);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Message msg = Message.obtain();
					msg.what = GlobalContant.EXCUTE_ASYNCTASK;
					msg.obj = orderid;
					mHandler.sendMessage(msg);

				} else {
					Message msg = Message.obtain();
					msg.what = GlobalContant.ERROR_MSG;
					msg.obj = errmsg;
					mHandler.sendMessage(msg);
				}

			}
		});
	}

	private class PhoneCardPayAsyncTask extends MyAsyncTask {
		private String mOrderid;
		private int code;
		@SuppressWarnings("unused")
		private double trade_coin;
		private String errmsg;
		private double all_coin;

		public PhoneCardPayAsyncTask(String orderid) {
			// super();
			this.mOrderid = orderid;
		}

		@Override
		public void preTask() {
		}

		@Override
		public void doInBack() {
			HttpPost post = new HttpPost(AppConfig.CARDURL + "orderconfirm");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("orderid", mOrderid));
			params.add(new BasicNameValuePair("user_id", String.valueOf(toUserId)));
			HttpResponse httpResponse = null;
			String result = "";
			LogUtils.e("充值完成确认:", AppConfig.CARDURL + "orderconfirm");
			try {
				post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				httpResponse = new DefaultHttpClient().execute(post);
				result = EntityUtils.toString(httpResponse.getEntity());
			} catch (Exception e) {
				e.printStackTrace();
			}
			LogUtils.e("RESULTresult:", result);
			// if (httpResponse.getStatusLine().getStatusCode() == 200) {
			code = JsonUtil.getInt(result, "code", 0);
			trade_coin = JsonUtil.getDouble(result, "trade_coin", 0);
			all_coin = JsonUtil.getDouble(result, "all_coin", 0);
			errmsg = JsonUtil.getString(result, "errmsg", "");
			// }
		}

		@Override
		public void postTask() {
			closeDialog();
			mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
			isShowDialog = false;
			if (!TextUtils.isEmpty(errmsg)) {
				ToastUtils.show(errmsg, 1);
			}
			if (code == 0) {
				UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
				if (null != uInfo) {
					uInfo.setGold((float) all_coin);
					DBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
				}
			}
		}
	}
}
