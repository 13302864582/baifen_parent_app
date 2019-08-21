package com.daxiong.fun.function.homework.view;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.daxiong.fun.MainActivity;
import com.daxiong.fun.R;
import com.daxiong.fun.function.homework.ShakeUtil;
import com.daxiong.fun.function.homework.ShakeUtil.OnShakeListener;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.WeLearnFileUtil;

import java.io.File;

/**
 * 采纳答案并且提交满意度评价的dialog
 */
public class GrabRedPackageDialog extends Dialog implements android.view.View.OnClickListener, OnShakeListener
		 {

	private static final int AUTO_RETURN = 2000;
	public static final String VOICENAME = "cunqianguan.mp3";
	private static final int SHAKE_CONSTANT = 1;
	private MainActivity mActivity;
	private View yaoyiyaoContainer;
	private View resultContainer;
	private long[] shakeTimes = new long[SHAKE_CONSTANT + 1];
	private TextView zanzhushangTv;
	private TextView timesTv;
	private TextView tomorrowTv;
	private TextView resultTv;
	private TextView msgTv;
	private int times;
	private ShakeUtil shakeUtil;
	private String sponsor;
	private long lastShake;

	public static String mAudioPath = WeLearnFileUtil.getVoiceFile().getAbsolutePath() + File.separator + VOICENAME;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHAKE_CONSTANT:
				startVibrator();
				OkHttpHelper.post(mActivity, "system", "gethb", null, new HttpListener() {
					
					@Override
					public void onSuccess(int code, String dataJson, String errMsg) {
						resultContainer.setVisibility(View.VISIBLE);
						yaoyiyaoContainer.setVisibility(View.GONE);
						String msg = JsonUtil.getString(dataJson, "msg", "");
						double gold = JsonUtil.getDouble(dataJson, "gold", 0);
						times--;
						if (gold > 0) {
							resultTv.setText("恭喜");
							tipsTv.setVisibility(View.VISIBLE);
//							WeLearnApi.getUserInfoFromServer(mActivity, null);
						} else {
							tipsTv.setVisibility(View.GONE);
							resultTv.setText("遗憾");
						}
						msgTv.setText(msg);
						setTimes();
						mHandler.sendEmptyMessageDelayed(AUTO_RETURN, AUTO_RETURN);					
						
					}
					
					@Override
					public void onFail(int HttpCode,String errMsg) {
						
						
					}
				});
				break;
			case AUTO_RETURN:
				int visibility = resultContainer.getVisibility();
				if (visibility == View.VISIBLE) {
					returnYaoyiyao();
				} 
				break;
			}
		}
	};

	private Vibrator vibrator;
	private TextView tipsTv;

	public GrabRedPackageDialog(Context context, String sponsor, int times) {
		super(context, R.style.adoptHomeWorkRatingBar);
		this.mActivity = (MainActivity) context;
		this.times = times;
		this.sponsor = sponsor;
	}

	public void setData(String sponsor, int times) {
		this.times = times;
		this.sponsor = sponsor;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.grab_red_package_dialog);

		/*
		 * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
		 */
		vibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);

		yaoyiyaoContainer = findViewById(R.id.yaoyiyao_container_grabred);
		resultContainer = findViewById(R.id.result_container_grabred);
		zanzhushangTv = (TextView) findViewById(R.id.zanzhushang_text_tv);
		timesTv = (TextView) findViewById(R.id.haisheng_ci_text_tv);
		tomorrowTv = (TextView) findViewById(R.id.tomorrow_text_tv);
		tipsTv = (TextView) findViewById(R.id.tips_text_tv_grabred);

		resultTv = (TextView) findViewById(R.id.result_text_tv);
		msgTv = (TextView) findViewById(R.id.resultmsg_text_tv);

		ActionBar bar = getActionBar();
		if (bar != null) {
			bar.hide();
		}
		if (null == shakeUtil) {
			shakeUtil = new ShakeUtil(mActivity);
		}
		shakeUtil.setOnShakeListener(this);
		findViewById(R.id.cancel_btn_grab).setOnClickListener(this);
		findViewById(R.id.cancel_btn_open).setOnClickListener(this);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 去掉周围黑边

		setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface arg0) {
				if (TextUtils.isEmpty(sponsor)) {
					zanzhushangTv.setText("大熊作业");
				} else {
					zanzhushangTv.setText(sponsor + "赞助");
				}
				setTimes();
			}
		});
	}

	@Override
	public void show() {
		super.show();
		if (null != shakeUtil) {
			shakeUtil.start();
		}
	}

	@Override
	public void cancel() {
		super.cancel();
		if(null != shakeUtil){
			shakeUtil.stop();
		}
	}

	private void startVibrator() {
		long[] pattern = { 100, 400, 100, 400 }; // 停止 开启 停止 开启
		vibrator.vibrate(pattern, -1); // 重复两次上面的pattern 如果只想震动一次，index设为-1
	}

	private void cancelVibrator() {
		vibrator.cancel();
	};

	private void setTimes() {
		if (times > 0) {
			timesTv.setVisibility(View.VISIBLE);
			tomorrowTv.setVisibility(View.GONE);
			timesTv.setText(mActivity.getString(R.string.grabred_times_text, times));
		} else {
			tomorrowTv.setVisibility(View.VISIBLE);
			timesTv.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_btn_open:
			returnYaoyiyao();
			break;
		case R.id.cancel_btn_grab:
			cancelVibrator();
			if (null != shakeUtil) {
				shakeUtil.stop();
			}
			dismiss();
			break;
		}
	}

	private void returnYaoyiyao() {
		mHandler.removeMessages(AUTO_RETURN);
		resultContainer.setVisibility(View.GONE);
		yaoyiyaoContainer.setVisibility(View.VISIBLE);
	}

	@Override
	public void onShake() {
		int visibility = yaoyiyaoContainer.getVisibility();
		if (visibility == View.VISIBLE && times > 0 && isShowing()) {
			if (System.currentTimeMillis() - lastShake > 1000) {
				System.arraycopy(shakeTimes, 1, shakeTimes, 0, SHAKE_CONSTANT);
				shakeTimes[SHAKE_CONSTANT] = System.currentTimeMillis();
				if (shakeTimes[SHAKE_CONSTANT] - shakeTimes[0] < 500) {
					shake();
				}
			}
		}
	}

	private synchronized void shake() {
		mActivity.uMengEvent("shakePhone");
		lastShake = System.currentTimeMillis();
		File file = new File(mAudioPath);
		if (!TextUtils.isEmpty(mAudioPath) && file.exists()) {
			MediaUtil.getInstance(false).playVoice(true, mAudioPath, null, null, null);
			mHandler.sendEmptyMessageDelayed(SHAKE_CONSTANT, 200);
		}

	}

	@Override
	public void onBackPressed() {
		int visibility = resultContainer.getVisibility();
		if (visibility == View.VISIBLE) {
			returnYaoyiyao();
		} else {
			cancelVibrator();
			super.onBackPressed();
		}
	}



	
}
