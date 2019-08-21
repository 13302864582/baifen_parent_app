package com.daxiong.fun.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.daxiong.fun.dialog.WaitingDialog;

public abstract class CustomFragment extends AbstractCommonFragment {
	// protected ActionBar mActionBar;
	
	protected abstract void goBack();
	
	protected Activity mActivity;
	
	protected ProgressDialog mDialog;
	
	
	
	public void showDialog(String text) {
//		if (isAdded()) {
//			mDialog = ProgressDialog.show(mActivity, "", text);
//			if (mDialog != null && !mDialog.isShowing()) {
//				mDialog.show();
//			}
//		}
	    
	    if (mProgressDialog == null) {
            mProgressDialog = WaitingDialog.createLoadingDialog2(getActivity(), "正在加载中...");
            mProgressDialog.show();
        }
	}
	
	
	protected void closeDialog() {
//        if (mDialog != null && mDialog.isShowing()) {
//            mDialog.dismiss();
//        }
//        mDialog = null;
	    
	    if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
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
//		setHasOptionsMenu(true);
//		mActionBar = getActivity().getActionBar();
//		mActionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP  | 
//				 ActionBar.DISPLAY_SHOW_CUSTOM |
//				 ActionBar.DISPLAY_SHOW_TITLE);//不调用则自定义actionbar失效
//		mActionBar.setDisplayHomeAsUpEnabled(false);// 添加返回按钮
//		mActionBar.setHomeButtonEnabled(true);
//		mActionBar.setDisplayShowHomeEnabled(true);
		//mActionBar.setIcon(R.drawable.bg_actionbar_back_up_selector);
		
		//mActionBar.set
	/*	int upid = Resources.getSystem().getIdentifier("up", "id", "android");  
		ImageView img = (ImageView) mActivity.findViewById(upid);  
		img.setImageResource(R.drawable.bg_actionbar_back_up_selector);*/
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
