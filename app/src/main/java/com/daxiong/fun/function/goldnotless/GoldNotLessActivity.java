package com.daxiong.fun.function.goldnotless;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.account.PhoneRegisterActivity;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.GoldToStringUtil;

public class GoldNotLessActivity extends BaseActivity implements OnClickListener, HttpListener {

	private LinearLayout bindingPhoneLL;
	private TextView mGold, bindingInfoTV, bindingPhoneSubTV;

	private UserInfoModel uInfo;
	public static final int REQUEST_CODE_BIND = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_gold_not_less);
		setWelearnTitle(R.string.text_gold_selction);
		uMengEvent("winlearnpoint");
		findViewById(R.id.back_layout).setOnClickListener(this);

		uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if (null != uInfo) {

		} else {
			finish();
		}

		findViewById(R.id.dayday_signin_container).setOnClickListener(this);
		findViewById(R.id.friend_gold_container).setOnClickListener(this);
		bindingPhoneLL = (LinearLayout) findViewById(R.id.binding_phone_container);
		bindingPhoneLL.setOnClickListener(this);
		findViewById(R.id.pay_gold_container).setOnClickListener(this);
		bindingInfoTV = (TextView) findViewById(R.id.binding_phone);
		bindingPhoneSubTV = (TextView) findViewById(R.id.binding_phone_sub_info);
		mGold = (TextView) findViewById(R.id.current_gold_num);
	}

	@Override
	public void onResume() {
		super.onResume();
		uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		updateUserInfo(uInfo);
	}

	private void updateUserInfo(UserInfoModel uInfo) {
		float gold = uInfo.getGold();
		String goldStr = GoldToStringUtil.GoldToString(gold);
		mGold.setText(goldStr);

		String phone = null;
		if (null != uInfo) {
			phone = uInfo.getTel();
		}
		if (TextUtils.isEmpty(phone)) {
			bindingPhoneSubTV.setVisibility(View.VISIBLE);
			bindingInfoTV.setText(R.string.text_binding_phone_item);
			bindingPhoneLL.setClickable(true);
		} else {
			bindingPhoneSubTV.setVisibility(View.GONE);
			bindingInfoTV.setText(getString(R.string.text_has_binding_phone, phone));
			bindingPhoneLL.setClickable(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.dayday_signin_container:
			// IntentManager.goSignInActivity(this);
			break;
		case R.id.friend_gold_container:
			IntentManager.gotoFriendGoldActivity(this);
			break;
		case R.id.pay_gold_container:
			uMengEvent("openrecharge");
			IntentManager.goPayActivity(this);
			break;
		case R.id.binding_phone_container:
			uMengEvent("openbindingphone");
			Intent i = new Intent(GoldNotLessActivity.this, PhoneRegisterActivity.class);
			i.putExtra(PhoneRegisterActivity.DO_TAG, PhoneRegisterActivity.DO_BIND);
			startActivityForResult(i, REQUEST_CODE_BIND);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_BIND) {
//				WeLearnApi.getUserInfoFromServer(this, this);
			}
		}
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		uInfo = DBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		updateUserInfo(uInfo);
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {

	}
}
