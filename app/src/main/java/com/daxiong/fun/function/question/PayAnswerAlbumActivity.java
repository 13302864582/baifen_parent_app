package com.daxiong.fun.function.question;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.daxiong.fun.R;
import com.daxiong.fun.adapter.ImageBucketAdapter;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.ActionDef;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.dispatch.PayAnswerAlbumController;
import com.daxiong.fun.function.communicate.ChatMsgViewActivity;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.ImageBucket;
import com.daxiong.fun.util.AlbumUtil;
import com.daxiong.fun.util.MySharePerfenceUtil;

import java.util.List;

public class PayAnswerAlbumActivity extends BaseActivity implements OnItemClickListener, OnClickListener {

	private AlbumUtil mAlbumUtil;
	private List<ImageBucket> mDataList;
	private ImageBucketAdapter mAdapter;
	private PayAnswerAlbumController mPayAnswerAlbumController;
	private int mTag;
	private int userid;
	private String username;

	public static final String IAMGE_LIST = "image_list";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_album);

		setWelearnTitle(R.string.text_album);
		
		findViewById(R.id.back_layout).setOnClickListener(this);
		
		mAlbumUtil = AlbumUtil.getInstance();
		mAlbumUtil.init(this);

		mDataList = mAlbumUtil.getImagesBucketList(false);
		mAdapter = new ImageBucketAdapter(this, mDataList);
		Intent i = getIntent();
		if (i != null) {
			mTag = i.getIntExtra("tag", 0);
			userid = getIntent().getIntExtra(ChatMsgViewActivity.USER_ID, 0);
			username = getIntent().getStringExtra(ChatMsgViewActivity.USER_NAME);
		}

		GridView gridView = (GridView) findViewById(R.id.album_gridview);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(this);

	}

	@Override
	public void onResume() {
		super.onResume();
		mPayAnswerAlbumController = new PayAnswerAlbumController(null);
		mPayAnswerAlbumController.setActivity(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ImageBucket imageBucket = new ImageBucket();
		imageBucket.setImageList(mDataList.get(position).getImageList());
		mPayAnswerAlbumController.setModel(imageBucket);
		Message msg = mPayAnswerAlbumController.getHandler().obtainMessage();
		msg.what = ActionDef.ACTION_IMAGE_GIRD;
		msg.arg1 = mTag;
		msg.arg2 = userid;
		msg.obj = username;
		mPayAnswerAlbumController.getHandler().sendMessage(msg);
	}

	public void goPrevious() {
		switch (mTag) {
		case GlobalContant.PAY_ANSWER_ASK:
			IntentManager.goToPayAnswerAskView(this, true);
			break;
		case GlobalContant.SEND_IMG_MSG:
			Bundle data = new Bundle();
			data.putInt(ChatMsgViewActivity.USER_ID, userid);
			data.putString(ChatMsgViewActivity.USER_NAME, username);
			IntentManager.goToChatListView(this, data, true);
			break;
		case GlobalContant.CONTACT_SET_USER_IMG:
			Bundle data1 = new Bundle();
			data1.putInt("userid", MySharePerfenceUtil.getInstance().getUserId());
			data1.putInt("roleid", MySharePerfenceUtil.getInstance().getUserRoleId());
//			IntentManager.goToStudentCenterView(this, data1);
			finish();
			break;
		case GlobalContant.CONTACT_SET_BG_IMG:
			Bundle data2 = new Bundle();
			data2.putInt("userid", MySharePerfenceUtil.getInstance().getUserId());
			data2.putInt("roleid", MySharePerfenceUtil.getInstance().getUserRoleId());
//			IntentManager.goToStudentCenterView(this, data2);
			finish();
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPayAnswerAlbumController.removeMsgInQueue();
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
