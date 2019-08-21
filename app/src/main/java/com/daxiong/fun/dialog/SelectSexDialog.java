package com.daxiong.fun.dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.daxiong.fun.R;
import com.daxiong.fun.constant.GlobalContant;

/**
 * 性别选择dialog
 * @author:  sky
 */
public class SelectSexDialog extends Dialog implements android.view.View.OnClickListener {
	private Context context;

	private int oldSex;
	private int selectSex;
	private RadioGroup sexRadioGroup;
	private RadioButton sexManRb, sexWomanRb;

	private OnSexUpdatedListener mOnSexUpdatedListener;

	public void setmOnSexUpdatedListener(OnSexUpdatedListener mOnSexUpdatedListener) {
		this.mOnSexUpdatedListener = mOnSexUpdatedListener;
	}

	public SelectSexDialog(Context context) {
		super(context);
		this.context = context;
	}

	public SelectSexDialog(Context context, int oldSex, OnSexUpdatedListener listener) {
		super(context, R.style.adoptHomeWorkRatingBar);
		this.context = context;
		selectSex = this.oldSex = oldSex;
		this.mOnSexUpdatedListener = listener;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = View.inflate(context, R.layout.choose_sex_dialog, null);
		setContentView(view);
		ActionBar bar = getActionBar();
		if (bar != null) {
			bar.hide();
		}

		sexRadioGroup = (RadioGroup) findViewById(R.id.sex_group_layout);
		sexManRb = (RadioButton) findViewById(R.id.sex_man_rb);
		sexWomanRb = (RadioButton) findViewById(R.id.sex_women_rb);
		if (oldSex == GlobalContant.SEX_TYPE_MAN) {
			sexManRb.setChecked(true);
		} else if (oldSex == GlobalContant.SEX_TYPE_WOMEN) {
			sexWomanRb.setChecked(true);
		}
		sexRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.sex_man_rb:
					selectSex = GlobalContant.SEX_TYPE_MAN;
					break;
				case R.id.sex_women_rb:
					selectSex = GlobalContant.SEX_TYPE_WOMEN;
					break;
				}
				if (null != mOnSexUpdatedListener) {
					mOnSexUpdatedListener.onSexUpdated(selectSex);
				}
				SelectSexDialog.this.dismiss();
			}

		});
//		findViewById(R.id.submit_adopt_btn).setOnClickListener(this);
//		findViewById(R.id.dialog_layout).setOnClickListener(this);
//		findViewById(R.id.root_adopt_dialog).setOnClickListener(this);

		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.dialog_layout:
//			break;
//		case R.id.submit_adopt_btn:
//
//			if (selectSex == oldSex) {
//				this.cancel();
//				return;
//			}
//
//			JSONObject obj = new JSONObject();
//			try {
//				obj.put("sex", selectSex);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			WeLearnApi.updateUserInfoFromServer(context, obj, new HttpListener() {
//				@Override
//				public void onSuccess(int code, String dataJson, String errMsg) {
//					if (code == 0) {
//						ToastUtils.show(R.string.modifyinfosuccessful);
//						if (null != mOnSexUpdatedListener) {
//							mOnSexUpdatedListener.onSexUpdated();
//						}
//					} else {
//						if (!TextUtils.isEmpty(errMsg)) {
//							ToastUtils.show(errMsg);
//						} else {
//							ToastUtils.show(R.string.modifyinfofailed);
//						}
//					}
//					SelectSexDialog.this.dismiss();
//				}
//
//				@Override
//				public void onFail(int HttpCode) {
//					ToastUtils.show(R.string.modifyinfofailed);
//					SelectSexDialog.this.dismiss();
//				}
//			});
//			break;
//		case R.id.root_adopt_dialog:
//			dismiss();
//			break;
		}
	}

	public interface OnSexUpdatedListener {
		void onSexUpdated(int selectSex);
	}
}
