package com.daxiong.fun.function.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.api.WeLearnApi;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.constant.EventConstant;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.db.DBHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.model.UserInfoModel;
import com.daxiong.fun.util.LogUtils;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.CropCircleTransformation;
import com.daxiong.fun.view.glide.BlurTransformation;

/**
 * 个人中心-班主任资料页面
 * 
 * @author Administrator
 *
 */
public class BanzhurenActivity extends BaseActivity implements HttpListener {

	public static final String TAG = BanzhurenActivity.class.getSimpleName();

	private UserInfoModel mTeacherInfo = null;


	public static final String EXTRA_TAG_FROM_FRIENDS = "extra_tag_from_friends";

	private int target_user_id;

	private int count;

	private boolean isFromFriends;


	private RelativeLayout mRl;
	private ImageView mTec_info_bg_iv;
	private ImageView mIv_back;
	private ImageView mTec_info_head_iv;
	private TextView mTec_info_nick_tv;
	private TextView mTec_info_sex_tv;
	private TextView mUserid_tv_tec_cen;
	private TextView mTec_info_school_tv;


	private TextView mTv_congyejingli;


	private Button mCommunicate_btn;





	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.banzhuren_activity);
		initView();

		initListener();
		initExtraData();
	}

	public void initExtraData() {

		Intent intent = getIntent();
		if (null == intent) {
			finish();
		}
		target_user_id = intent.getIntExtra("userid", 0);
		isFromFriends = intent.getBooleanExtra(EXTRA_TAG_FROM_FRIENDS, false);

		if (target_user_id == 0) {
			ToastUtils.show(R.string.userid_error);
			finish();
		}

		if (isFromFriends) {
			updateUiInfo();
		}

		WeLearnApi.getContactInfo(this, target_user_id, this);

	}

	public void initView() {
		mRl = (RelativeLayout) findViewById(R.id.rl);
		mTec_info_bg_iv = (ImageView) findViewById(R.id.tec_info_bg_iv);
		mIv_back = (ImageView) findViewById(R.id.iv_back);
		mTec_info_head_iv = (ImageView) findViewById(R.id.tec_info_head_iv);
		mTec_info_nick_tv = (TextView) findViewById(R.id.tec_info_nick_tv);
		mTec_info_sex_tv = (TextView) findViewById(R.id.tec_info_sex_tv);
		mUserid_tv_tec_cen = (TextView) findViewById(R.id.userid_tv_tec_cen);
		mTec_info_school_tv = (TextView) findViewById(R.id.tec_info_school_tv);

		mTv_congyejingli = (TextView) findViewById(R.id.tv_congyejingli);

		mCommunicate_btn = (Button) findViewById(R.id.communicate_btn);

	}

	@Override
	public void initListener() {
		super.initListener();
		mIv_back.setOnClickListener(this);
		findViewById(R.id.communicate_btn).setOnClickListener(this);

	}

	private void updateUiInfo() {
		if (mTeacherInfo!=null) {
			mTeacherInfo = DBHelper.getInstance().getWeLearnDB().queryContactInfoById(target_user_id);
			if (null == mTeacherInfo) {
				LogUtils.e(TAG, "Contact info gson is null !");
				return;
			}



			Glide.with(BanzhurenActivity.this).load(mTeacherInfo.getAvatar_100())
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.bitmapTransform(new CropCircleTransformation(BanzhurenActivity.this))
					.placeholder(R.drawable.default_icon_circle_avatar).into(mTec_info_head_iv);

			Glide.with(BanzhurenActivity.this)
					.load(mTeacherInfo.getAvatar_100())
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.bitmapTransform(new BlurTransformation(BanzhurenActivity.this, 100))
					.placeholder(R.drawable.mohubg)
					.into(mTec_info_bg_iv);

			// 昵称
			String name = TextUtils.isEmpty(mTeacherInfo.getName()) ? getString(R.string.persion_info) : mTeacherInfo.getName();
			mTec_info_nick_tv.setText(name);
			

			String grade = TextUtils.isEmpty(mTeacherInfo.getSchools()) ? "" : mTeacherInfo.getSchools();
			mTec_info_school_tv.setText(grade);


			int sex = mTeacherInfo.getSex();
			switch (sex) {
			case GlobalContant.SEX_TYPE_MAN:
				mTec_info_sex_tv.setText("男");
				break;
			case GlobalContant.SEX_TYPE_WOMEN:
				mTec_info_sex_tv.setText("女");
				break;

			default:
				mTec_info_sex_tv.setVisibility(View.GONE);
				break;
			}

			mUserid_tv_tec_cen.setText(mTeacherInfo.getAge()+"年教龄");
			
			//从业经历
			String Work_descriptions=mTeacherInfo.getWork_descriptions();
			if (!TextUtils.isEmpty(Work_descriptions)) {
				if (Work_descriptions.contains("{}")) {//多条
					Work_descriptions = Work_descriptions.replace("{}", "\n");

					mTv_congyejingli.setText(Work_descriptions);
				}else {//单条

					mTv_congyejingli.setText(Work_descriptions);

				}
				
				
				
			}else {
				//显示暂无从业经历

				mTv_congyejingli.setText("暂无从业经历");

			}
			
			







		}
		
	}

	private View getItem(int titleResId, String leftStr, String rightStr, boolean showArrow) {
		View v = View.inflate(this, R.layout.view_stu_info_item, null);

		TextView titleTV = (TextView) v.findViewById(R.id.item_title_tv);
		TextView titleValueTV = (TextView) v.findViewById(R.id.item_title_value_tv);
		TextView arrowValueTV = (TextView) v.findViewById(R.id.item_arrow_value_tv);
		ImageView arrowIv = (ImageView) v.findViewById(R.id.item_arrow_iv);

		titleTV.setText(titleResId);
		if (TextUtils.isEmpty(leftStr)) {
			leftStr = "";
		}
		titleValueTV.setText(leftStr);

		if (TextUtils.isEmpty(rightStr)) {
			rightStr = "";
		}
		arrowValueTV.setText(rightStr);

		arrowIv.setVisibility(showArrow ? View.VISIBLE : View.GONE);

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;





		case R.id.communicate_btn:
			if (null != mTeacherInfo && mTeacherInfo.getRoleid() != 0 && mTeacherInfo.getUserid() != 0) {
				Bundle data3 = new Bundle();
				data3.putInt("userid", mTeacherInfo.getUserid());
				data3.putInt("roleid", mTeacherInfo.getRoleid());
				data3.putString("username", mTeacherInfo.getName());
				MobclickAgent.onEvent(this, EventConstant.CUSTOM_EVENT_CHAT);
				IntentManager.goToChatListView(this, data3, false);
			}
			break;
		}
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (code == 0) {

			mTeacherInfo = JSON.parseObject(dataJson, UserInfoModel.class);
			if (null != mTeacherInfo) {
				count = mTeacherInfo.getCount();
				DBHelper.getInstance().getWeLearnDB().insertOrUpdatetContactInfo(mTeacherInfo);
				DBHelper.getInstance().getWeLearnDB().insertorUpdate(mTeacherInfo.getUserid(), mTeacherInfo.getRoleid(),
						mTeacherInfo.getName(), mTeacherInfo.getAvatar_100());
			}

			updateUiInfo();
		} else {
			if (!TextUtils.isEmpty(errMsg)) {
				ToastUtils.show(errMsg);
			}
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {

	}

}
