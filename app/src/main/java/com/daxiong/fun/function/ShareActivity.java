package com.daxiong.fun.function;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.MyApplication;
import com.daxiong.fun.R;
import com.daxiong.fun.function.goldnotless.Util;
import com.daxiong.fun.model.ShareModel;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;

public class ShareActivity extends Activity implements OnClickListener {

	private static final String TAG = ShareActivity.class.getSimpleName();
	
	public static final String SHARE_MODEL_TAG = "share_model_tag";

	private static final int THUMB_SIZE = 150;

	private ShareModel shareModel;
	private String shareTitle, shareDesc, shareUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop_window_share);
		
		Intent i = getIntent();
		if (null == i) {
			LogUtils.e(TAG, "intent is null");
			finish();
		}
		
		shareModel = (ShareModel) i.getSerializableExtra(SHARE_MODEL_TAG);
		if(null == shareModel){
			LogUtils.e(TAG, "share model is null");
			finish();
		}
		
		shareTitle = shareModel.getShareTitle();
		shareDesc = shareModel.getShareDesc();
		shareUrl = shareModel.getShareUrl();
		
		findViewById(R.id.btn_share_wechat).setOnClickListener(this);
		findViewById(R.id.btn_share_qq).setOnClickListener(this);
		findViewById(R.id.btn_share_wechat_cof).setOnClickListener(this);
		findViewById(R.id.btn_share_qzone).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_share_wechat:
			MobclickAgent.onEvent(this, "sharetoww");
			//TODO 微信分享 shareToWW();
			shareToWWImg(WeLearnFileUtil.getShotImagePath());
			break;
		case R.id.btn_share_qq:
			MobclickAgent.onEvent(this, "sharetoqq");
			//TODO QQ分享 shareToQQ();
			shareToQQImg(WeLearnFileUtil.getShotImagePath(), getString(R.string.app_name));
			break;
		case R.id.btn_share_wechat_cof:
			MobclickAgent.onEvent(this, "sharetoquan");
			//TODO 微信朋友圈分享shareToQuan();
			shareToQuanImg(WeLearnFileUtil.getShotImagePath());
			break;
		case R.id.btn_share_qzone:
			MobclickAgent.onEvent(this, "sharetoqzone");
			//TODO QQ空间分享 shareToQzone();
			shareToQzoneImg(WeLearnFileUtil.getShotImagePath(), getString(R.string.app_name));
			break;
		case R.id.btn_cancel:
			break;
		}
		finish();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	

	/**
	 * 
	 * 此方法描述的是： 图片分享至QQ
	 * @author:  Ctao
	 * @最后修改人： Ctao 
	 * @最后修改日期:2015年7月21日 上午9:50:32
	 * @version: 2.0
	 *
	 * shareToQQImg
	 * @param imgPath 本地图片路径
	 * @param app_name app_name
	 */
	private void shareToQQImg(String imgPath, String app_name) {
	    Bundle params = new Bundle();
	    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imgPath);
	    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, app_name);
	    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
	    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0);
	    MyApplication.mTencent.shareToQQ(this, params, new IUiListener() {
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

	/**
	 * 
	 * 此方法描述的是： 图片分享至QQ空间
	 * @author:  Ctao
	 * @最后修改人： Ctao 
	 * @最后修改日期:2015年7月21日 上午11:37:19
	 * @version: 2.0
	 *
	 * shareToQzoneImg
	 * @param imgPath 本地图片路径
	 * @param app_name app_name
	 */
	private void shareToQzoneImg(String imgPath, String app_name) {
		Bundle params = new Bundle();
	    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imgPath);
	    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, app_name);
	    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
	    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
	    MyApplication.mTencent.shareToQQ(this, params, new IUiListener() {
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
	
	/**
	 * 
	 * 此方法描述的是：图片分享至微信好友
	 * @author:  Ctao
	 * @最后修改人： Ctao 
	 * @最后修改日期:2015年7月21日 下午2:55:54
	 * @version: 2.0
	 *
	 * shareToWWImg
	 * @param imgPath 本地图片路径
	 */
	private void shareToWWImg(String imgPath) {
		WXImageObject imgObj = new WXImageObject();
		imgObj.setImagePath(imgPath);
		
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		
		Bitmap bmp = BitmapFactory.decodeFile(imgPath);
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE - 50, THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
		
			
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		MyApplication.api.sendReq(req);
	}
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	/**
	 * 
	 * 此方法描述的是：图片分享至微信朋友圈
	 * @author:  Ctao
	 * @最后修改人： Ctao 
	 * @最后修改日期:2015年7月21日 下午3:02:26
	 * @version: 2.0
	 *
	 * shareToQuanImg
	 * @param imgPath 本地图片路径
	 */
	private void shareToQuanImg(String imgPath) {
		WXImageObject imgObj = new WXImageObject();
		imgObj.setImagePath(imgPath);
		
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		
		Bitmap bmp = BitmapFactory.decodeFile(imgPath);
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE - 50, THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		MyApplication.api.sendReq(req);
	}
	
}
