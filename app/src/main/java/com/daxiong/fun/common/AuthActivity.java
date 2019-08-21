package com.daxiong.fun.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.ToastUtils;

public class AuthActivity extends BaseActivity implements OnClickListener {

	public static final String AUTH_URL = "auth_url";
	private static final String TAG = AuthActivity.class.getSimpleName();
	// public static AuthFragment authFragment = null;
	private ProgressBar mProgressBar;
	private WebView mWebView;

	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.auth_fragment);

		setWelearnTitle(R.string.text_login);

		findViewById(R.id.back_layout).setOnClickListener(this);
		
		mWebView = (WebView) findViewById(R.id.auth_webview);
		mWebView.setWebChromeClient(webChromeClient);
		mWebView.setWebViewClient(webViewClient);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().supportMultipleWindows();
		mWebView.setDownloadListener(mDownloadListener);

		String url = getIntent().getStringExtra(AUTH_URL);
		mWebView.loadUrl(url);
		mProgressBar = (ProgressBar) findViewById(R.id.auth_progress);
		// authFragment = this;
	}

	private DownloadListener mDownloadListener = new DownloadListener() {

		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
				long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	};

	private WebChromeClient webChromeClient = new WebChromeClient() {

		public boolean onJsAlert(WebView view, String url, String message, android.webkit.JsResult result) {
			return true;
		};

		public void onProgressChanged(WebView view, int newProgress) {
			AuthActivity.this.setProgress(newProgress * 100);
			if (newProgress == 100) {
				mProgressBar.setVisibility(View.GONE);
			}
		};
	};

	private WebViewClient webViewClient = new WebViewClient() {
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			if (AppConfig.IS_DEBUG) {
				ToastUtils.show("errorCode:" + errorCode + ";错误描述:" + description + ";失败的链接地址:"
						+ failingUrl, 1);
			}
		};

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			LogUtils.e(TAG, " onPageStarted " + url);
			if (url.contains(AppConfig.YEEURL + "callback?data=")) {
				
//				if (GlobalVariable.payFragment != null) {
//					Message msg = GlobalVariable.payFragment.getHandler().obtainMessage();
//					msg.what = PayFragment.YEEPAY;
//					msg.obj = orderid;
//					GlobalVariable.payFragment.getHandler().sendMessage(msg);
//					GlobalVariable.payFragment = null;
//				}
				
				// ToastUtils.show(mActivity, "充值成功");
				// mActivity.finish();
			}
			// mWebView.stopLoading();
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		}
	}
}
