package com.daxiong.fun.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;

public abstract class BaseFragment extends AbstractCommonFragment {

//	protected ActionBar mActionBar;
	protected Activity mActivity;

	protected ProgressDialog mDialog;
	public boolean isShowDialog = false;
	public long clickTime ;
	protected float downX ;
	protected float downY ;
	protected long downTime;
	
	protected abstract void goBack();

	public void closeDialogHelp() {
		closeDialog();
		isShowDialog = false;
		//mHandler.removeMessages(GlobalContant.CLOSEDIALOG);
	}

	protected void closeDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			try {
				mDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void showDialog(String text) {
		if (isAdded()) {
			mDialog = ProgressDialog.show(mActivity, "", text);
			if (mDialog != null && !mDialog.isShowing()) {
				mDialog.show();
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);// 开启返回按钮
		
//		mActionBar = getActivity().getActionBar();
//		mActionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
//				| ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE);// 不调用则自定义actionbar失效
//
//		// mActionBar.setDisplayShowHomeEnabled(true);//显示icon图标
//		mActionBar.setDisplayHomeAsUpEnabled(false);// 添加返回按钮
//		mActionBar.setHomeButtonEnabled(true);
//		mActionBar.setDisplayShowHomeEnabled(true);

		// mActionBar.setIcon(R.drawable.bg_actionbar_back_up_selector);
		// mActionBar.setLogo(R.drawable.bg_actionbar_back_up_selector);
		/*
		 * mActionBar.setIcon(R.drawable.bg_actionbar_back_up_selector); int
		 * upid = Resources.getSystem().getIdentifier("up", "id", "android");
		 * ImageView img = (ImageView) mActivity.findViewById(upid);
		 * img.setImageResource(R.drawable.bg_actionbar_back_up_selector);
		 */
		// mActionBar.setHomeAsUpIndicator(R.drawable.ic_actiobar_homeasup);
		// mActionBar.set
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			goBack();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
