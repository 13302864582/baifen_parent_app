package com.daxiong.fun.dispatch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;

import com.daxiong.fun.constant.ActionDef;
import com.daxiong.fun.function.communicate.ChatMsgViewActivity;
import com.daxiong.fun.function.question.PayAnswerAlbumActivity;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.BaseModel;
import com.daxiong.fun.model.ImageBucket;

import java.io.Serializable;

public class PayAnswerAlbumController extends BaseController {

	private Activity mActivity;
	
	public void setActivity(Activity activity) {
		this.mActivity = activity;
	}
	
	public PayAnswerAlbumController(BaseModel model) {
		super(model, null, PayAnswerAlbumController.class.getSimpleName());
	}

	@Override
	protected void handleEventMessage(Message msg) {
		switch (msg.what) {
		case ActionDef.ACTION_IMAGE_GIRD:
			ImageBucket imageBucket = getModel();
			Bundle data = new Bundle();
			data.putInt("tag", msg.arg1);
			data.putInt(ChatMsgViewActivity.USER_ID, msg.arg2);
			data.putString(ChatMsgViewActivity.USER_NAME, (String) msg.obj);
			data.putSerializable(PayAnswerAlbumActivity.IAMGE_LIST, (Serializable) imageBucket.getImageList());
			IntentManager.goToImageGridView(mActivity, data);
		}
	}
	
	private ImageBucket getModel() {
		ImageBucket imageBucket = null;
		boolean isImageBucket = this.mModel instanceof ImageBucket;
		if (isImageBucket) {
			imageBucket = (ImageBucket) mModel;
		}
		return imageBucket;
	}
}
