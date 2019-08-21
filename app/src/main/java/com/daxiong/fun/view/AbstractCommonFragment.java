package com.daxiong.fun.view;

import android.app.Activity;
import android.app.ProgressDialog;

import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseFragment;
import com.daxiong.fun.util.ToastUtils;

public abstract class AbstractCommonFragment extends BaseFragment {

	protected Activity mActivity;

	protected ProgressDialog mDialog;

	protected abstract void goBack();

	protected void closeDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		  //MobclickAgent.onPageEnd(this.getClass().getSimpleName()); 
	}

	@Override
	public void onResume() {
		super.onResume();
		  //MobclickAgent.onPageStart(this.getClass().getSimpleName());
		 // ToastUtils.show(mActivity, this.getClass().getSimpleName());
		
	}

	public void showDialog(String text) {
		if (isAdded()) {
			mDialog = ProgressDialog.show(MyApplication.getContext(), "", text);
			if (mDialog != null && !mDialog.isShowing()) {
				mDialog.show();
			}
		}
		// new Handler().postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// WApplication.mNetworkUtil.sendTimeoutMsg();
		// }
		//
		// }, 15000);
	}

	protected void showNetWorkExceptionToast() {
		if (isAdded()) {
			ToastUtils.show(getString(R.string.network_connect_fail_msg));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}
}
