package com.daxiong.fun.util;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.function.account.GuideActivity;
import com.daxiong.fun.view.MySpUtil;

public class DebugActvity extends BaseActivity implements OnClickListener {
	private Button changTo13Btn;
	private Button changTo18Btn;
	private Button changTo195Btn;
	private TextView pythonTv;
	private TextView goTv;
	private Button changTo20Btn;
	private Button submitBtn;
	private EditText pyET;
	private EditText goET;
	
	private Button btn_shang;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.fragment_debug_changeip);

		pythonTv = (TextView) findViewById(R.id.python_tv_debug);
		goTv = (TextView) findViewById(R.id.go_tv_debug);
		changTo13Btn = (Button) findViewById(R.id.bt_13_172_debug_changeip);
		changTo18Btn = (Button) findViewById(R.id.bt_18_172_debug_changeip);
		changTo20Btn = (Button) findViewById(R.id.bt_20_172_debug_changeip);
		changTo195Btn = (Button) findViewById(R.id.bt_195_debug_changeip);

		pyET = (EditText) findViewById(R.id.py_et_debug);
		goET = (EditText) findViewById(R.id.go_et_debug);
		submitBtn = (Button) findViewById(R.id.submit_btn_debug);

		btn_shang=(Button)findViewById(R.id.btn_shang);
		
		
		findViewById(R.id.back_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		changTo13Btn.setOnClickListener(this);
		changTo18Btn.setOnClickListener(this);
		changTo195Btn.setOnClickListener(this);
		changTo20Btn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		btn_shang.setOnClickListener(this);

		pythonTv.setText(MySpUtil.getInstance().getPYTHONTP());
		goTv.setText(MySpUtil.getInstance().getGOTP());
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.submit_btn_debug) {
			String pyStr = pyET.getText().toString().trim();
			String goStr = goET.getText().toString().trim();
			if (TextUtils.isEmpty(goStr) ||TextUtils.isEmpty(pyStr)  ) {
				ToastUtils.show("不能为空!");
				return;
			}
			MySpUtil.getInstance().setPYTHONTP(pyStr);
			MySpUtil.getInstance().setGOTP(goStr);
		} else {
			TextView tv = (TextView) view;
			String GOIPStr = tv.getText().toString();
			ToastUtils.show("已切换至" + GOIPStr + "环境");
			goTv.setText(GOIPStr);
			MySpUtil.getInstance().setGOTP(GOIPStr);
		}
		DBHelper.getInstance().getWeLearnDB().deleteCurrentUserInfo();
		AppConfig.loadIP();
		Intent i = new Intent(this, GuideActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(i);
		finish();
	}
}
