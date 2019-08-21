package com.daxiong.fun.function.goldnotless;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.config.AppConfig;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.util.JsonUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;

public class FriendGoldActivity extends BaseActivity implements OnClickListener/*, INetWorkListener*/ {

//	private int shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
//	private TextView invite_num;


	private String url = AppConfig.FUDAOTUAN_URL +"/mobile.html";
	private String desc;
	private String title;

	private int mExtarFlag = 0x01;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.fragmentfriendgold);
		
		findViewById(R.id.back_layout).setOnClickListener(this);
		
		LinearLayout quan = (LinearLayout) findViewById(R.id.friend_quan_container);
		LinearLayout qzone = (LinearLayout) findViewById(R.id.friend_qzone_container);
		LinearLayout qq = (LinearLayout) findViewById(R.id.friend_qq_container);
		LinearLayout ww = (LinearLayout) findViewById(R.id.friend_ww_container);
		setWelearnTitle(R.string.text_friend_gold);
//		invite_num = (TextView) findViewById(R.id.invite_num);
//		invite_num.setText(getString(R.string.invite_friends_count, WeLearnSpUtil.getInstance().getInviteNum()));
		quan.setOnClickListener(this);
		qzone.setOnClickListener(this);
		qq.setOnClickListener(this);
		ww.setOnClickListener(this);

//		WeLearnApi.getInviteNum(this);
		OkHttpHelper.post(this, "guest", "getappconfigs", null, new HttpListener() {			
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				int userId = MySharePerfenceUtil.getInstance().getUserId();
				title = JsonUtil.getString(dataJson, "sharetitle1", getString(R.string.invite_title)); 
				desc = JsonUtil.getString(dataJson, "sharedesc1", getString(R.string.invite_desc));  
				url = JsonUtil.getString(dataJson, "shareurl1",AppConfig.FUDAOTUAN_URL +"/mobile.html");  
				
				title =String.format(title, userId); 
				desc =String.format(desc, userId); 
				
			
				
			}
			
			@Override
			public void onFail(int HttpCode,String errMsg) {
				
				
			}
		});
		
		

	}

//	@Override
//	public void onResume() {
//		super.onResume();
//		if (mController == null) {
//			mController = new GetInvitateNumController(null, this);
//		}
//	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		MyApplication.mTencent.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.friend_ww_container:// 微信
			MobclickAgent.onEvent(this, "sharetoww");
			shareToWW();
			break;

		case R.id.friend_qq_container:// QQ
			MobclickAgent.onEvent(this, "sharetoqq");
			shareToQQ();
			break;

		case R.id.friend_quan_container:// 朋友圈
			MobclickAgent.onEvent(this, "sharetoquan");
			shareToQuan();
			break;

		case R.id.friend_qzone_container:// QQ空间
			MobclickAgent.onEvent(this, "sharetoqzone");
			shareToQzone();
			break;

		default:
			break;
		}
	}

	private void shareToQQ() {
		final Bundle bundle = new Bundle();
		mExtarFlag = 0;
		bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
		bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, desc);
		bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);
		bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
		bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);
		bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, AppConfig.FUDAOTUAN_URL +"/logo.png");
		MyApplication.mTencent.shareToQQ(this, bundle, new IUiListener() {

			@Override
			public void onError(UiError arg0) {
				ToastUtils.show(R.string.invite_error);
			}

			@Override
			public void onComplete(Object arg0) {
				ToastUtils.show(R.string.invite_success);
			}

			@Override
			public void onCancel() {
				ToastUtils.show(R.string.invite_cancel);
			}

		});
	}

	private void shareToQzone() {
		final Bundle bundle = new Bundle();
		mExtarFlag = 1;
		bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
		bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, desc);
		bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);
		bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
		bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);
		bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, AppConfig.FUDAOTUAN_URL +"/logo.png");
		MyApplication.mTencent.shareToQQ(this, bundle, new IUiListener() {

			@Override
			public void onError(UiError arg0) {
				ToastUtils.show(R.string.invite_error);
			}

			@Override
			public void onComplete(Object arg0) {
				ToastUtils.show(R.string.invite_success);
			}

			@Override
			public void onCancel() {
				ToastUtils.show(R.string.invite_cancel);
			}

		});
	}

	private void shareToQuan() {
		WXWebpageObject webObj = new WXWebpageObject(url);

		WXMediaMessage msg = new WXMediaMessage(webObj);
		msg.title = title;
		msg.description = desc;
		msg.mediaTagName = getString(R.string.app_name);
		Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
		msg.thumbData = Util.bmpToByteArray(thumb, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		/*boolean flag = */MyApplication.api.sendReq(req);
	}

	private void shareToWW() {
		WXWebpageObject webObj = new WXWebpageObject(url);

		WXMediaMessage msg = new WXMediaMessage(webObj);
		msg.title = title;
		msg.description = desc;
		msg.mediaTagName = getString(R.string.app_name);
		Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
		msg.thumbData = Util.bmpToByteArray(thumb, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;

		MyApplication.api.sendReq(req);
	}

//	@Override
//	public void onPre() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onException() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onAfter(String jsonStr, int msgDef) {
//		int code = JSONUtils.getInt(jsonStr, "code", 0);
//		String errmsg = JSONUtils.getString(jsonStr, "errmsg", "");
//		if (code == 0) {
//			switch (msgDef) {
//			case MsgDef.MSG_DEF_GET_INVITE_NUM:
//				int num = JSONUtils.getInt(JSONUtils.getJSONObject(jsonStr, "data", null), "invite_cnt", 0);
//				WeLearnSpUtil.getInstance().setInviteNum(num);
////				invite_num.setText(getString(R.string.invite_friends_count, num));
//				break;
//			}
//		} else {
//			ToastUtils.show(errmsg);
//		}
//	}
//
//	@Override
//	public void onDisConnect() {
//		// TODO Auto-generated method stub
//
//	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
