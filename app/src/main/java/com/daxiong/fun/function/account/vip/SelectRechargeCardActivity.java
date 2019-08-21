
package com.daxiong.fun.function.account.vip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.adapter.CardAdapter;
import com.daxiong.fun.adapter.MyGridAdapter;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.account.model.PayCardModel;
import com.daxiong.fun.function.goldnotless.PayActivity;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.model.ViewPageModel;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.MyAsyncTask;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.MyGridView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 购买充值卡
 * 
 * @author: sky
 */
public class SelectRechargeCardActivity extends BaseActivity implements OnClickListener, HttpListener {
	private int screenWidth;
	private Button mButton;
	private ViewPager mViewPager;
	private EditText rechargeNumEt;
	private LinearLayout dots_ll;
	private ImageView deleteNumIv;
	private ArrayList<View> dotLists;
	private TextView youhui_textview, tv_lunbo;
	private TextView userNameTv;
	private List<ViewPageModel> urlList = new ArrayList<ViewPageModel>();

	private MyGridView MyGridView;

	private ArrayList<PayCardModel> pcmList = new ArrayList<PayCardModel>();

	private UserInfoModel uInfo;

	private InputMethodManager imm;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				closeDialog();
				break;
			}
		}
	};

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_select_recharge_card);
		ViewPageModel viewPageModel1 = new ViewPageModel();
		viewPageModel1.setImageurl("http://e.hiphotos.baidu.com/image/pic/item/5fdf8db1cb13495405b54f62544e9258d1094a08.jpg");
		viewPageModel1.setWeburl("http://www.baidu.com");
		viewPageModel1.setText("瀑布");
		
		ViewPageModel viewPageModel2 = new ViewPageModel();
		viewPageModel2.setImageurl("http://d.hiphotos.baidu.com/image/pic/item/b999a9014c086e06964e984d00087bf40bd1cbd6.jpg");
		viewPageModel2.setWeburl("http://www.qq.com");
		viewPageModel2.setText("冰块");
		ViewPageModel viewPageModel3 = new ViewPageModel();
		viewPageModel3.setImageurl("http://g.hiphotos.baidu.com/image/pic/item/c8177f3e6709c93d3ed556bc9c3df8dcd1005451.jpg");
		viewPageModel3.setWeburl("http://www.163.com");
		viewPageModel3.setText("荷花");
		ViewPageModel viewPageModel4 = new ViewPageModel();
		viewPageModel4.setImageurl("http://f.hiphotos.baidu.com/image/pic/item/14ce36d3d539b600078f2676eb50352ac75cb7a9.jpg");
		viewPageModel4.setWeburl("http://www.sina.com");
		viewPageModel4.setText("蛋糕");
		urlList.add(viewPageModel1);
		urlList.add(viewPageModel2);
		urlList.add(viewPageModel3);
		urlList.add(viewPageModel4);
		initView();

	}

	@Override
	public void initView() {
		super.initView();
		setWelearnTitle(R.string.select_recharge_page_title);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		dots_ll = (LinearLayout) findViewById(R.id.dots_ll);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		screenWidth = DensityUtil.getScreenWidth(SelectRechargeCardActivity.this);
		mViewPager.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth, screenWidth * 5 / 16));
		mButton = (Button) findViewById(R.id.tel_pay_submit_bt);
		youhui_textview = (TextView) findViewById(R.id.youhui_textview);
		userNameTv = (TextView) findViewById(R.id.user_name_tv);
		tv_lunbo = (TextView) findViewById(R.id.tv_lunbo);
		rechargeNumEt = (EditText) findViewById(R.id.recharge_num_et);
		deleteNumIv = (ImageView) findViewById(R.id.recharge_num_delete_iv);
		mButton.setOnClickListener(this);
		deleteNumIv.setOnClickListener(this);
		rechargeNumEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				int len = s.toString().length();
				if (len > 0) {
					deleteNumIv.setVisibility(View.VISIBLE);
					if (len >= 5) {
						WeLearnApi.getContactInfo(SelectRechargeCardActivity.this, Integer.parseInt(s.toString()),
								SelectRechargeCardActivity.this);
					} else {
						uInfo = null;
						userNameTv.setText("");
					}
				} else {
					uInfo = null;
					deleteNumIv.setVisibility(View.INVISIBLE);
					userNameTv.setText("");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		UserInfoModel uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if (null != uInfo) {
			rechargeNumEt.setText(String.valueOf(uInfo.getUserid()));
			rechargeNumEt.setSelection(rechargeNumEt.getText().toString().length());
		}

		findViewById(R.id.back_layout).setOnClickListener(this);
		MyGridView = (MyGridView) findViewById(R.id.MyGridView);
		new GetCardsTask().excute();
		refreshView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tel_pay_submit_bt:
			int checkedItemPosition = MyGridView.getCheckedItemPosition();
			if (checkedItemPosition >= 0) {
				Intent i = new Intent(SelectRechargeCardActivity.this, PayActivity.class);
				i.putExtra(PayActivity.EXTRA_TAG_PAY_MODEL, pcmList.get(checkedItemPosition));
				i.putExtra(PayActivity.EXTRA_TAG_UID, uInfo.getUserid());
				startActivity(i);
			} else {
				ToastUtils.show("请选择要充的学点");
			}
			break;
		case R.id.back_layout:
			finish();
			break;
		case R.id.recharge_num_delete_iv:
			if (null != rechargeNumEt) {
				rechargeNumEt.setText("");
			}
			break;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {
				imm.hideSoftInputFromWindow(rechargeNumEt.getWindowToken(), 0);
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	private class GetCardsTask extends MyAsyncTask {
		private int code;

		private String errmsg;

		private String content;

		@Override
		public void preTask() {
			showDialog(getString(R.string.get_cards_info));
		}

		@Override
		public void doInBack() {
			HttpPost post = new HttpPost(AppConfig.RECHARGE_CONFIG_URL);
			HttpResponse httpResponse = null;
			String result = "";
			try {
				UserInfoModel userinfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
				if (userinfo != null) {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("userid", userinfo.getUserid() + ""));
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				}
				httpResponse = new DefaultHttpClient().execute(post);
				result = EntityUtils.toString(httpResponse.getEntity());
			} catch (Exception e) {
				e.printStackTrace();
			}
			LogUtils.e("RESULTresult:", result);
			code = JsonUtil.getInt(result, "code", 0);
			if (code == 0) {
				content = JsonUtil.getString(result, "content", "");
				pcmList.clear();
				if (!TextUtils.isEmpty(content)) {
					try {
						JSONArray ja = new JSONArray(content);
						if (null != ja) {
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jobj = ja.getJSONObject(i);
								if (null == jobj) {
									continue;
								}
								PayCardModel pm = new Gson().fromJson(jobj.toString(), PayCardModel.class);
								if (null == pm) {
									continue;
								}
								if (i % 2 == 0) {
									pm.setDefResId(R.drawable.card_50);
								} else {
									pm.setDefResId(R.drawable.card_100);
								}
								pcmList.add(pm);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else if (code == 1) {
				errmsg = JsonUtil.getString(result, "errmsg", "");
			}
		}

		@Override
		public void postTask() {
			mHandler.sendEmptyMessageDelayed(1, 500);
			isShowDialog = false;
			if (code == 0 && pcmList.size() > 0) {
				MyGridAdapter myGridAdapter = new MyGridAdapter(pcmList, SelectRechargeCardActivity.this);
				MyGridView.setAdapter(myGridAdapter);
				MyGridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						youhui_textview.setText(pcmList.get(position).getRemark());

					}
				});

			} else {
				if (!TextUtils.isEmpty(errmsg)) {
					ToastUtils.show(errmsg);
				} else {
					ToastUtils.show(R.string.get_cards_error);
				}
				finish();
			}
		}
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		userNameTv.setText("");
		if (code == 0) {
			uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);
			if (null != uInfo && !TextUtils.isEmpty(uInfo.getName())) {
				userNameTv.setText(uInfo.getName());
			} else {
				userNameTv.setText("");
			}
		} else {
			uInfo = null;
			userNameTv.setText("");
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		uInfo = null;
		userNameTv.setText("");
	}

	public void refreshView() {
		initDot();
		tv_lunbo.setText(urlList.get(0).getText());
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int index =arg0 % urlList.size();
				tv_lunbo.setText(urlList.get(index).getText());
				for(View dot:dotLists){
					
					dot.setBackgroundResource(R.drawable.dot_normal);
				}

				dotLists.get(index).setBackgroundResource(R.drawable.dot_focus);
				

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		mViewPager.setAdapter(new CardAdapter(SelectRechargeCardActivity.this, urlList));
		mViewPager.setCurrentItem(10*urlList.size());// 设置起始的位置

		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					runTask.stop();
					break;
				case MotionEvent.ACTION_CANCEL: // 事件的取消
				case MotionEvent.ACTION_UP:
					runTask.start();
					break;
				}

				return false; // viewPager 触摸事件 返回值要是false
			}
		});
		runTask = new AuToRunTask();
		runTask.start();
	}

	boolean flag;
	private AuToRunTask runTask;

	public class AuToRunTask implements Runnable {

		@Override
		public void run() {
			if (flag) {
				// 取消之前
				MyApplication.getMainThreadHandler().removeCallbacks(AuToRunTask.this);

				int currentItem = mViewPager.getCurrentItem();
				currentItem++;
				mViewPager.setCurrentItem(currentItem);
				// 延迟执行当前的任务
				MyApplication.getMainThreadHandler().postDelayed(AuToRunTask.this, 5000);// 递归调用
			}
		}

		public void start() {
			if (!flag) {
				MyApplication.getMainThreadHandler().removeCallbacks(AuToRunTask.this); // 取消之前
				flag = true;
				MyApplication.getMainThreadHandler().postDelayed(AuToRunTask.this, 5000);// 递归调用
			}
		}

		public void stop() {
			if (flag) {
				flag = false;
				MyApplication.getMainThreadHandler().removeCallbacks(AuToRunTask.this);
			}
		}

	}

	// 初始化点
	private void initDot() {
		dotLists = new ArrayList<View>();
		dots_ll.removeAllViews();
		for (int i = 0; i < urlList.size(); i++) {
			// 设置点的宽高
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 6),
					DensityUtil.dip2px(this, 6));
			// 设置点的间距
			params.setMargins(7, 0, 7, 0);
			// 初始化点的对象
			View m = new View(this);
			// 把点的宽高设置到view里面
			m.setLayoutParams(params);
			
			// 默认情况下，首先会调用第一个点。就必须展示选中的点
			if(i == 0){
			m.setBackgroundResource(R.drawable.dot_focus);
			}
			// 把所有的点装载进集合
			dotLists.add(m);
			// 现在的点进入到了布局里面
			dots_ll.addView(m);
		}
	}
}
