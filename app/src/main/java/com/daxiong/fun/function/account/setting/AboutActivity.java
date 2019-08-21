package com.daxiong.fun.function.account.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.common.WebViewActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.manager.IntentManager;

/**
 * 此类的描述：关于大熊作业
 * @author:  sky
 * @最后修改人： sky
 * @最后修改日期:2015年8月7日 下午4:42:26
 */
public class AboutActivity extends BaseActivity {

	private RelativeLayout back_layout;
	private RelativeLayout rl_feedback;
	private  RelativeLayout rl_yonghuxieyi;
	private RelativeLayout rl_about;
	private TextView version;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_about1);
		initView();
		initListener();
	}


	@Override
	public void initView() {
		super.initView();
		back_layout= (RelativeLayout) findViewById(R.id.back_layout);
		rl_feedback= (RelativeLayout) findViewById(R.id.rl_feedback);
		rl_yonghuxieyi= (RelativeLayout) findViewById(R.id.rl_yonghuxieyi);
		rl_about= (RelativeLayout) findViewById(R.id.rl_about);
		version = (TextView) findViewById(R.id.version_name_about);
		version.setText(getString(R.string.version_format_str, MyApplication.versionName));
		setWelearnTitle(R.string.about);
		if (AppConfig.IS_DEBUG) {
			//ToastUtils.show("该应用安装包来自渠道:" + MyApplication.getChannelValue());
		}
	}

	@Override
	public void initListener() {
		super.initListener();
		back_layout.setOnClickListener(this);
		rl_feedback.setOnClickListener(this);
		rl_yonghuxieyi.setOnClickListener(this);
		rl_about.setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()){
			case R.id.back_layout:
				finish();
				break;
			case R.id.rl_feedback:
				MobclickAgent.onEvent(AboutActivity.this, "FreeBack");
				IntentManager.goToUserRequest(AboutActivity.this);
				break;
			case R.id.rl_yonghuxieyi:
				String baseUrl = AppConfig.FUDAOTUAN_URL + "/abouth.html";
				Intent intent = new Intent(AboutActivity.this, WebViewActivity.class);
				intent.putExtra("title", "用户协议");
				intent.putExtra("url", baseUrl);
				startActivity(intent);
				break;
			case R.id.rl_about:
				Intent  intentx=new Intent(this,AboutAppActivity.class);
				startActivity(intentx);
				break;

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
