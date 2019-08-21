
package com.daxiong.fun.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.google.zxing.Result;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.constant.RequestConstant;
import com.daxiong.fun.dialog.WaitingDialog;
import com.daxiong.fun.function.homework.model.HomeWorkSinglePoint;
import com.daxiong.fun.http.volley.VolleyRequestQueueWrapper;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.zxing.decoding.CaptureActivityHandler;
import com.daxiong.fun.util.zxing.view.ViewfinderView;

import java.util.ArrayList;
import java.util.Random;

/**
 * 此类的描述： Activity基类
 * 
 * @author: Sky @最后修改人： Sky
 * @最后修改日期:2015年7月14日 上午10:10:43
 * @version: 2.0
 */
public abstract class BaseActivity extends FragmentActivity implements IBaseActivity, OnClickListener {
	private static final String TAG = BaseActivity.class.getSimpleName();

	private static BaseActivity mForegroundActivity = null;

	protected ProgressDialog mDialog;

	public ArrayList<HomeWorkSinglePoint> singlePointList;

	public boolean isShowDialog = false;

	public long clickTime;

	public Intent mintent;

	// sky add
	public MyApplication app;

	public RequestQueue requestQueue;

	public Dialog mProgressDialog;

	private CaptureActivityHandler handler;

	private ViewfinderView viewfinderView;

	private int progress = 0;
	private boolean flag = false;
	private boolean flag2 = false;

	private int progress2 = 2;

	private Runnable mtask;

	private long mtime = 100l;

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(Result result, Bitmap barcode) {
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(CaptureActivityHandler handler) {
		this.handler = handler;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public static BaseActivity getForegroundActivity() {
		return mForegroundActivity;
	}

	public <T extends View> T $(int resId) {
		return (T) super.findViewById(resId);
	}

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			// this.findViewById(android.R.id.content).setFitsSystemWindows(true);
		}

		setContentView(R.layout.activity_main);
		app = (MyApplication) this.getApplication();
		requestQueue = VolleyRequestQueueWrapper.getInstance(this).getRequestQueue();
		// addActivity(this);
		// mDialog = new ProgressDialog(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		super.onResume();
		mForegroundActivity = this;
		MobclickAgent.onResume(this);
		// StatService.onResume(this);
		// MobclickAgent.setSessionContinueMillis(60000);
	}

	@Override
	public void onPause() {
		super.onPause();
		mForegroundActivity = null;
		MobclickAgent.onPause(this);
		// StatService.onPause(this);
	}

	public void uMengEvent(String event) {
		MobclickAgent.onEvent(this, event);
	}

	protected void showNetWorkExceptionToast() {
		ToastUtils.show(R.string.network_connect_fail_msg);
	}

	public void showDialog(String text) {
		if (mProgressDialog == null) {
			mProgressDialog = WaitingDialog.createLoadingProgress(this, text);
			mProgressDialog.show();
		}

	}


	public void closeDialog() {
		if (mtask != null) {
			MyApplication.getMainThreadHandler().removeCallbacks(mtask);
			mtask = null;
		}
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			try {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			} catch (Exception e) {
				e.printStackTrace();
				mProgressDialog = null;
			}
		}

	}

	public void closeDialogHelp() {
		closeDialog();
		isShowDialog = false;
	}





	/**
	 * 水波纹效果的dialog
	 */
	public void showDialog3() {
		if (mProgressDialog == null) {
			mProgressDialog = WaitingDialog.createUploadDialog(this);
		}

		mProgressDialog.show();

		final int index = (new Random().nextInt(2) + 6) * 10;

		progress = 0;
		flag = false;
		flag2 = false;
		mtime = 100l;
		progress2 = 2;

		mtask = new Runnable() {

			@Override
			public void run() {
				if (WaitingDialog.waveProgress != null) {
					MyApplication.getMainThreadHandler().removeCallbacks(mtask);
					MyApplication.getMainThreadHandler().postDelayed(this, mtime);
					if (progress == 100) {
						WaitingDialog.waveProgress.setProgress(progress);
						MyApplication.getMainThreadHandler().removeCallbacks(mtask);
						closeDialog();
						ToastUtils.show("发送成功");
						if (mintent != null) {

							startActivity(mintent);
							finish();
						}

					} else {
						if (progress > 90 && progress2 == 10) {
							progress = 100;
							WaitingDialog.waveProgress.setProgress(progress);

						} else {
							if (!flag && (progress > index && progress < index + 10)) {

								flag = true;
								mtime = 1000l;
								progress2 = 1;
							}
							if (!flag2 && progress == 85) {
								if (mProgressDialog != null) {
									mProgressDialog.setCancelable(false);
								}
								flag2 = true;
								MyApplication.getMainThreadHandler().removeCallbacks(mtask);
								if (progress2 == 10) {
									MyApplication.getMainThreadHandler().post(mtask);
								}
							}

							WaitingDialog.waveProgress.setProgress(progress += progress2);
						}
					}
				}
			}

		};

		MyApplication.getMainThreadHandler().post(mtask);

	}

	protected void closeDialog3(Intent intent) {

		mtime = 100l;
		flag = true;
		flag2 = true;
		mintent = intent;
		MyApplication.getMainThreadHandler().removeCallbacks(mtask);
		progress2 = 10;
		MyApplication.getMainThreadHandler().post(mtask);
	}

	protected void hideBackLayout() {
		RelativeLayout backLayout = (RelativeLayout) findViewById(R.id.back_layout);
		if (null != backLayout) {
			backLayout.setVisibility(View.GONE);
		}
	}

	protected void setWelearnTitle(int resid) {
		TextView titleTV = (TextView) findViewById(R.id.title);
		if (null != titleTV) {
			try {
				titleTV.setText(resid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void setWelearnTitle(String titleStr) {
		TextView titleTV = (TextView) findViewById(R.id.title);
		if (null != titleTV && null != titleStr) {
			titleTV.setText(titleStr);
		}
	}

	public void showAddPointBottomContainer(String coordinate, int sum) {
		// mBottomContainer.setVisibility(View.VISIBLE);
	}

	public void hideAddPointBottomContainer() {
		// mBottomContainer.setVisibility(View.GONE);
	}

	public void report(String reason) {

	}

	@Override
	public void resultBack(Object... param) {
		int flag = ((Integer) param[0]).intValue();
		switch (flag) {
		case RequestConstant.ERROR:
			break;
		case RequestConstant.COOKIE_INVILD:
			break;
		default:
			String datas = param[1].toString();
			int code = JsonUtil.getInt(datas, "Code", -1);
			String msg = JsonUtil.getString(datas, "Msg", "");
			// 用户未登录
			if (code == RequestConstant.USER_UNLOGIN &&
					msg.contains("用户未登")) {
				//OkHttpHelper.doReLogin(this,this);
				ToastUtils.show("登录过期请重新登录");
				IntentManager.goToPhoneLoginActivity(this,null,true);
			}

			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (mProgressDialog != null) {
				mProgressDialog = null;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 取消所有的请求
		requestQueue.cancelAll(this);
		if (requestQueue != null) {
			requestQueue = null;
		}
		// removeActivity(this);
	}

	// private void addActivity(Activity activity) {
	// if(!app.activityList.contains(activity)){
	// synchronized (app) {
	// app.activityList.add(activity);
	// }
	// }
	//
	// }
	//
	// private void removeActivity(Activity activity) {
	// if(app.activityList.contains(activity)){
	// synchronized (app) {
	// app.activityList.remove(activity);
	// }
	// }
	// }

}
