package com.daxiong.fun.function.partner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daxiong.fun.R;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.util.ToastUtils;

import org.json.JSONObject;

public class SingleEditTextActivity extends BaseActivity implements OnClickListener, HttpListener {
	public static final String EDIT_TEXT_TYPE = "edit_text_type";
	public static final int SINGLE_EDIT_TEXT_TYPE_NAME = 0;
	public static final int SINGLE_EDIT_TEXT_TYPE_SCHOOL = 1;
	public static final int SINGLE_EDIT_TEXT_TYPE_MAJOR = 2;

	private int mEditTextType = -1;
	private EditText edit_text = null;
	private String oldStr;
	private ImageView deleteIV;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.fragment_single_edit_text);

		findViewById(R.id.back_layout).setOnClickListener(this);

		RelativeLayout nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		TextView nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.text_nav_submit);
		nextStepLayout.setOnClickListener(this);

		deleteIV = (ImageView) findViewById(R.id.delete_iv);
		deleteIV.setOnClickListener(this);

		Intent intent = getIntent();
		oldStr = intent.getStringExtra("value");
		edit_text = (EditText) findViewById(R.id.editText);
		edit_text.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					deleteIV.setVisibility(View.VISIBLE);
				} else {
					deleteIV.setVisibility(View.GONE);
				}
			}
		});
		edit_text.setText(oldStr);
		edit_text.setCursorVisible(true);
		edit_text.setSelection(edit_text.getText().length());
		edit_text.setFocusable(true);

		int editTextType = intent.getIntExtra(EDIT_TEXT_TYPE, -1);

		switch (editTextType) {
		case SINGLE_EDIT_TEXT_TYPE_NAME:
			setWelearnTitle(R.string.text_change_nickname);
			break;
		case SINGLE_EDIT_TEXT_TYPE_SCHOOL:
			setWelearnTitle(R.string.text_change_school);
			break;
		case SINGLE_EDIT_TEXT_TYPE_MAJOR:
			setWelearnTitle(R.string.text_change_major);
			break;
		}

		mEditTextType = editTextType;
	}

	public boolean onSaveMenuClicked() {
		String value = edit_text.getText().toString();

		if (null != value && value.equals(oldStr)) {
			finish();
			return true;
		}

		String key = null;

		switch (mEditTextType) {
		case SINGLE_EDIT_TEXT_TYPE_NAME:
			key = "name";
			break;
		case SINGLE_EDIT_TEXT_TYPE_SCHOOL:
			key = "schools";
			break;
		case SINGLE_EDIT_TEXT_TYPE_MAJOR:
			key = "major";
			break;
		}

		if (null != key) {
			try {
				JSONObject json = new JSONObject();
				json.put(key, value);
				WeLearnApi.updateUserInfoFromServer(this, json, this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.next_setp_layout:
			onSaveMenuClicked();
			break;
		case R.id.delete_iv:
			if (null != edit_text) {
				edit_text.setText("");
			}
			break;
		}
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (code == 0) {
			ToastUtils.show(R.string.modifyinfosuccessful);
			setResult(GlobalContant.RESULT_OK);
			finish();
		} else {
			if (!TextUtils.isEmpty(errMsg)) {
				ToastUtils.show(errMsg);
			} else {
				ToastUtils.show(R.string.modifyinfofailed);
			}
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		ToastUtils.show(R.string.modifyinfofailed);
	}
}
