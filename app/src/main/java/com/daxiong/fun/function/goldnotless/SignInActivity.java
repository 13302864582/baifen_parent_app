package com.daxiong.fun.function.goldnotless;
//package com.welearn.goldnotless;
//
//import java.util.ArrayList;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.android.volley.toolbox.NetworkImageView;
//import com.google.gson.Gson;
//import com.umeng.analytics.MobclickAgent;
//import com.welearn.base.ImageLoader;
//import com.welearn.base.WeLearnApi;
//import com.welearn.base.view.SingleFragmentActivity;
//import com.welearn.constant.MsgDef;
//import com.welearn.constant.ResponseCmdDef;
//import com.welearn.db.WLDBHelper;
//import com.welearn.manager.INetWorkListener;
//import com.welearn.model.SignInRuleModel;
//import com.welearn.model.SignInRuleModel.SignInLog;
//import com.welearn.model.UserInfoModel;
//import com.welearn.util.GoldToStringUtil;
//import com.welearn.util.JSONUtils;
//import com.welearn.util.ToastUtils;
//import com.welearn.util.WeLearnSpUtil;
//import com.welearn.welearn.R;
//
//public class SignInActivity extends SingleFragmentActivity implements INetWorkListener, OnClickListener {
//
//	// private SignInAdapter mAdapter;
//	private TextView day_sign;
//	private TextView gold_sign;
//	private NetworkImageView banner1_iv;
//	// private ImageView banner2_iv;
//	private ListView list;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		// mActionBar.setTitle("天天签到");
//		setContentView(R.layout.fragmengt_sign_in);
//		
//		findViewById(R.id.back_layout).setOnClickListener(this);
//
//		WeLearnApi.getSignLog();
//
//		day_sign = (TextView) findViewById(R.id.sum_sign_day);
//		gold_sign = (TextView) findViewById(R.id.sum_sign_gold);
//
//		banner1_iv = (NetworkImageView) findViewById(R.id.banner1_iv);
//		banner1_iv.setImageResource(R.drawable.ic_ad_banner1);
//		// banner2_iv = (ImageView) view.findViewById(R.id.banner2_iv);
//
//		list = (ListView) findViewById(R.id.list_sign_in_view);
//		// mAdapter = new SignInAdapter(null);
//		list.setAdapter(new SignInAdapter(null));
//
//		TextView title = (TextView) findViewById(R.id.title);
//		title.setText("天天签到");
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//	}
//
//	class SignInAdapter extends BaseAdapter {
//		SignInRuleModel mModel;
//
//		public SignInAdapter(SignInRuleModel mModel) {
//			super();
//			if (mModel == null) {
//				mModel = new SignInRuleModel();
//				mModel.setToday_signed(1);
//				mModel.setCycle(7);
//				mModel.setStartgold(0.5);
//				mModel.setUnit(0.1);
//				mModel.setSignInLogs(new ArrayList<SignInRuleModel.SignInLog>());
//			}
//			this.mModel = mModel;
//		}
//
//		public void setmModel(SignInRuleModel mModel) {
//			this.mModel = mModel;
//			notifyDataSetChanged();
//		}
//
//		@Override
//		public int getCount() {
//			return mModel.getCycle();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			View view = null;
//			if (convertView == null) {
//				view = View.inflate(SignInActivity.this, R.layout.item_sign_in_view, null);
//			} else {
//				view = convertView;
//			}
//			int pos = position + 1;
//			TextView day = (TextView) view.findViewById(R.id.day_sign_in_item);
//			day.setText("第" + pos + "天");
//
//			TextView gold = (TextView) view.findViewById(R.id.gold_sign_in_item);
//			final Button signInBtn = (Button) view.findViewById(R.id.sign_in_item_btn);
//			double startgold = mModel.getStartgold();
//			double endgold = startgold + position * mModel.getUnit();
//			String goldStr = GoldToStringUtil.GoldToString(endgold);
//			if (mModel.getRandommax() == 0 && mModel.getRandommin() == 0) {
//				gold.setText("可领" + goldStr + "学点");
//			} else {
//				gold.setText("可领神秘数量学点");
//			}
//
//			int signeds = mModel.getSignInLogs().size();
//			if (position < signeds) {
//				signInBtn.setBackgroundResource(R.drawable.ic_sign_in_ed);
//				signInBtn.setText("");
//				gold.setText("已领" + GoldToStringUtil.GoldToString(mModel.getSignInLogs().get(position).gotgold) + "学点");
//
//			} else if (position == signeds && mModel.getToday_signed() == 0) {
//				signInBtn.setBackgroundResource(R.drawable.bg_choic_school_sumbit_selector);
//				signInBtn.setText("签到");
//				signInBtn.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						signInBtn.setBackgroundResource(R.drawable.ic_sign_in_ed);
//						signInBtn.setText("");
//						if (System.currentTimeMillis() - clickTime > 500) {
//							WeLearnApi.SignIn();
//							clickTime = System.currentTimeMillis();
//							MobclickAgent.onEvent(SignInActivity.this, "signin");
//							signInBtn.setClickable(false);
//						}
//
//					}
//				});
//			} else {
//				signInBtn.setText("签到");
//				signInBtn.setBackgroundResource(R.drawable.ic_sign_btn_grey);
//			}
//
//			if (position == mModel.getCycle() - 1) {
//				view.findViewById(R.id.line_signin_item).setVisibility(View.GONE);
//			} else {
//				view.findViewById(R.id.line_signin_item).setVisibility(View.VISIBLE);
//			}
//			return view;
//		}
//
//	}
//
//	private long clickTime;
//
//	@Override
//	public void onPre() {
//
//	}
//
//	@Override
//	public void onException() {
//
//	}
//
//	@Override
//	public void onAfter(String jsonStr, int msgDef) {
//		int code = JSONUtils.getInt(jsonStr, "code", -1);
//		String errmsg = JSONUtils.getString(jsonStr, "errmsg", "");
//
//		switch (msgDef) {
//		case MsgDef.MSG_DEF_GET_SIGN_IN_LOG:
//			if (code == ResponseCmdDef.CODE_RETURN_OK) {
//				try {
//					JSONObject data = JSONUtils.getJSONObject(jsonStr, "data", null);
//					if (data == null) {
//						break;
//					}
//					Gson gson = new Gson();
//
//					String stat = JSONUtils.getString(data, "stat", null);
//					double sumgold = JSONUtils.getDouble(stat, "sumgold", 0);
//					int sumsign = JSONUtils.getInt(stat, "sumsign", 0);
//					day_sign.setText("" + sumsign);
//					gold_sign.setText("" + GoldToStringUtil.GoldToString(sumgold));
//
//					String rule = JSONUtils.getString(data, "rule", null);
//					SignInRuleModel model = gson.fromJson(rule, SignInRuleModel.class);
//
//					JSONArray logs = JSONUtils.getJSONArray(data, "log", null);
//					ArrayList<SignInLog> signInLogs = new ArrayList<SignInRuleModel.SignInLog>();
//					for (int i = 0; i < logs.length(); i++) {
//						signInLogs.add(gson.fromJson(logs.getString(i), SignInLog.class));
//					}
//					model.setSignInLogs(signInLogs);
//					int today_signed = JSONUtils.getInt(data, "today_signed", 0);
//					WeLearnSpUtil.getInstance().setIsTodaySignIn(today_signed);
//					model.setToday_signed(today_signed);
//					// mAdapter.setmModel(model);
//					list.setAdapter(new SignInAdapter(model));
//					if (!TextUtils.isEmpty(model.getBanner1())) {
//						// ImageLoader.getInstance().displayImage(model.getBanner1(), banner1_iv, null, null);
//						ImageLoader.getInstance().loadImage(model.getBanner1(), banner1_iv, R.drawable.ic_ad_banner1);
//						
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			} else {
//				ToastUtils.show(errmsg);
//			}
//			break;
//		case MsgDef.MSG_DEF_SIGN_IN:
//			WeLearnApi.getSignLog();
//			if (code == ResponseCmdDef.CODE_RETURN_OK) {
//				try {
//					JSONObject data = JSONUtils.getJSONObject(jsonStr, "data", null);
//					if (data == null) {
//						break;
//					}
//					WeLearnSpUtil.getInstance().setIsTodaySignIn(1);//签到
//					double today = JSONUtils.getDouble(data, "today", 0);
//					double total = JSONUtils.getDouble(data, "total", 0);
//					ToastUtils.show("签到成功!获得了" + GoldToStringUtil.GoldToString(today) + "个学点");
//					
//					UserInfoModel uInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
//					if(null != uInfo){
//						uInfo.setGold((float)total);
//						WLDBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			} else {
//				ToastUtils.show(errmsg);
//			}
//			break;
//		}
//
//	}
//
//	@Override
//	public void onDisConnect() {
//
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.back_layout:
//			finish();
//			break;
//		}
//	}
//}
