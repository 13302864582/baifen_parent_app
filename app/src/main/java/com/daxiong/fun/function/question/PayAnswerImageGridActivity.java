package com.daxiong.fun.function.question;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.daxiong.fun.R;
import com.daxiong.fun.adapter.ImageGridAdapter;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.ActionDef;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.function.communicate.ChatMsgViewActivity;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.ImageItem;
import com.daxiong.fun.util.MySharePerfenceUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.util.WeLearnFileUtil;

import java.util.List;

public class PayAnswerImageGridActivity extends BaseActivity implements OnClickListener{

	private List<ImageItem> mDataList;
	private ImageGridAdapter mImageGridAdapter;
	private int mTag;
	private int userid;
	private String username;

	public static final String IMAGE_PATH = "image_path";

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ActionDef.ACTION_PHOTO_SHOW:
				String path = msg.obj.toString();
				if (WeLearnFileUtil.isFileExist(path)) {
					Bundle data = new Bundle();
					data.putString(IMAGE_PATH, msg.obj + "");
					data.putBoolean("isFromPhotoList", true);
					switch (mTag) {
					case GlobalContant.PAY_ANSWER_ASK:
						IntentManager.goToQuestionPhotoView(PayAnswerImageGridActivity.this, data);
						break;
					case GlobalContant.PAY_ASNWER:
						IntentManager.goToPhotoView(PayAnswerImageGridActivity.this, data);
						break;
					case GlobalContant.SEND_IMG_MSG:
						data.putInt(ChatMsgViewActivity.USER_ID, userid);
						data.putString(PayAnswerQuestionDetailActivity.IMG_PATH, path);
						data.putString(ChatMsgViewActivity.USER_NAME, username);
						// IntentManager.goToChatListView(mActivity, data,
						// true);
						IntentManager.goToPreSendPicReViewActivity(PayAnswerImageGridActivity.this, data, true);
						break;
					case GlobalContant.CONTACT_SET_USER_IMG:
						// TODO
						data.putInt("userid", MySharePerfenceUtil.getInstance().getUserId());
						data.putInt("roleid", MySharePerfenceUtil.getInstance().getUserRoleId());
						data.putString("userlogo", path);
						if (MySharePerfenceUtil.getInstance().getUserRoleId() == 1) {
//							IntentManager.goToStudentCenterView(PayAnswerImageGridActivity.this, data);
						} else if (MySharePerfenceUtil.getInstance().getUserRoleId() == 2) {
//							IntentManager.goToTeacherCenterView(PayAnswerImageGridActivity.this, data);
						}
						PayAnswerImageGridActivity.this.finish();
						break;
					case GlobalContant.CONTACT_SET_BG_IMG:
						data.putInt("userid", MySharePerfenceUtil.getInstance().getUserId());
						data.putInt("roleid", MySharePerfenceUtil.getInstance().getUserRoleId());
						data.putString("bgimg", path);
//						IntentManager.goToStudentCenterView(PayAnswerImageGridActivity.this, data);
						PayAnswerImageGridActivity.this.finish();
						break;
					}
				} else {
					ToastUtils.show(R.string.text_file_not_exists);
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_image_grid);

		setWelearnTitle(R.string.text_album);

		findViewById(R.id.back_layout).setOnClickListener(this);
		
		GridView gridView = (GridView) findViewById(R.id.gridview);
		mDataList = (List<ImageItem>) getIntent().getSerializableExtra(PayAnswerAlbumActivity.IAMGE_LIST);
		mImageGridAdapter = new ImageGridAdapter(this, mDataList, mHandler);
		gridView.setAdapter(mImageGridAdapter);
		mImageGridAdapter.notifyDataSetChanged();

		Intent i = getIntent();
		if (i != null) {
			mTag = i.getIntExtra("tag", 0);
			switch (mTag) {
			case GlobalContant.SEND_IMG_MSG:
				userid = i.getIntExtra(ChatMsgViewActivity.USER_ID, 0);
				username = i.getStringExtra(ChatMsgViewActivity.USER_NAME);
				break;
			// case GlobalContant.CONTACT_SET_USER_IMG:
			// //TODO
			// break;
			// case GlobalContant.CONTACT_SET_BG_IMG:
			// break;
			}
		}
	}

	public void goPrevious() {
		Bundle data = new Bundle();
		data.putInt("tag", mTag);
		IntentManager.goToAlbumView(this, data);
	}

	@Override
	public void onBackPressed() {
		goPrevious();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			goPrevious();
			break;
		}
	}
}
