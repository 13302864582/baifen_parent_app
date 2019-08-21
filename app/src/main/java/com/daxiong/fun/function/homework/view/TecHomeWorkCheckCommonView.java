package com.daxiong.fun.function.homework.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.daxiong.fun.R;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.function.AnswerListItemView;
import com.daxiong.fun.function.homework.adapter.TecHomeWrokCheckCommonGridViewAdapter;
import com.daxiong.fun.function.homework.model.HomeWorkModel;
import com.daxiong.fun.function.homework.model.StuPublishHomeWorkPageModel;
import com.daxiong.fun.function.homework.teacher.TecHomeWorkDetail_OnlyReadActivity;
import com.daxiong.fun.manager.IntentManager;

import java.util.ArrayList;
import java.util.Date;

public class TecHomeWorkCheckCommonView extends FrameLayout {

	private static final String TAG = TecHomeWorkCheckCommonView.class.getSimpleName();
	private Activity mActivity;
	private NetworkImageView mAvatar_iv;
	private TextView mNick_tv;
	private TextView mCredit_tv;
	private TextView mAskTime_tv;
	private TextView mNewUserTag;
	private ImageView mVipUserTag;
	private GridView mHomeWorkImgContainer_gv;
	private TextView mDesc_tv;
	private TextView mGrade_tv;
	private TextView mSubject_tv;
	private TextView mRewardVal_tv;
	private ImageView mNoQuestion_iv;
	private HomeWorkModel mHomeWorkModel;
	private int avatarSize;
	private TecHomeWrokCheckCommonGridViewAdapter gridViewAdapter;
	private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;

	public TecHomeWorkCheckCommonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setUpViews();
		this.mActivity = (Activity) context;
	}

	public TecHomeWorkCheckCommonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setUpViews();
		this.mActivity = (Activity) context;
	}

	public TecHomeWorkCheckCommonView(Context context) {
		super(context);
		setUpViews();
		this.mActivity = (Activity) context;
	}

	private void setUpViews() {
		avatarSize =getResources().getDimensionPixelSize(R.dimen.avatar_size_homework_check_common);

		View payAnswerView = LayoutInflater.from(getContext()).inflate(R.layout.homeworkcheckcommonview, null);
		mAvatar_iv = (NetworkImageView) payAnswerView.findViewById(R.id.avatar_iv_check_common);
		mNick_tv = (TextView) payAnswerView.findViewById(R.id.nick_tv_check_common);
		mCredit_tv = (TextView) payAnswerView.findViewById(R.id.credit_tv_check_common);
		mAskTime_tv = (TextView) payAnswerView.findViewById(R.id.ask_time_tv_check_common);
		mNewUserTag = (TextView) payAnswerView.findViewById(R.id.new_asker_tv_check_common);
		mVipUserTag = (ImageView) payAnswerView.findViewById(R.id.vip_asker_iv_check_common);
		mHomeWorkImgContainer_gv = (GridView) payAnswerView.findViewById(R.id.homework_img_container_gridview_common);
		mNoQuestion_iv = (ImageView) payAnswerView.findViewById(R.id.no_question_iv_check_common);
		mDesc_tv = (TextView) payAnswerView.findViewById(R.id.desc_tv_check_common);
		mGrade_tv = (TextView) payAnswerView.findViewById(R.id.asker_grade_tv_check_common);
		mSubject_tv = (TextView) payAnswerView.findViewById(R.id.asker_subject_tv_check_common);
		mRewardVal_tv = (TextView) payAnswerView.findViewById(R.id.reward_val_tv_check_common);
		addView(payAnswerView);
	}

	public void showData(HomeWorkModel homeWorkModel) {
		this.mHomeWorkModel = homeWorkModel;
		mNick_tv.setClickable(true);
		mAvatar_iv.setClickable(true);
		mHomeWorkImgContainer_gv.setClickable(true);
		mHomeWorkImgContainer_gv.setVisibility(View.VISIBLE);
		mNoQuestion_iv.setVisibility(View.GONE);

		mHomeWorkPageModelList = mHomeWorkModel.getPagelist();
		ImageLoader.getInstance().loadImageWithDefaultAvatar(homeWorkModel.getAvatar(), mAvatar_iv, avatarSize,
				avatarSize / 10);
		mAvatar_iv.setTag(homeWorkModel.getStudid());
		mAvatar_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Integer userid = (Integer) view.getTag();
				IntentManager.gotoPersonalPage(mActivity, userid, GlobalContant.ROLE_ID_STUDENT);
			}
		});
		
		if (mHomeWorkImgContainer_gv.getAdapter() == null) {
			gridViewAdapter = new TecHomeWrokCheckCommonGridViewAdapter(mHomeWorkPageModelList, mActivity);
			mHomeWorkImgContainer_gv.setAdapter(gridViewAdapter);
		} else {
			gridViewAdapter.setList(mHomeWorkPageModelList);
		}
		mHomeWorkImgContainer_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// gridViewAdapter.click(view , position);

				Bundle data = new Bundle();
				data.putInt(AnswerListItemView.EXTRA_POSITION, position);
				data.putSerializable(TecHomeWorkDetail_OnlyReadActivity.HOMEWROKDETAILPAGERLIST, mHomeWorkPageModelList);
				IntentManager.goToHomeWorkDetail_OnlyReadActivity(mActivity, data, false);
			}
		});
		if (homeWorkModel.getSupervip() > 0) {
			mVipUserTag.setVisibility(View.VISIBLE);
			mNewUserTag.setVisibility(View.INVISIBLE);
		} else {
			mVipUserTag.setVisibility(View.INVISIBLE);
			if (homeWorkModel.getIsnew() == 1) {
				mNewUserTag.setVisibility(View.VISIBLE);
			} else {
				mNewUserTag.setVisibility(View.INVISIBLE);
			}
		}
		mNick_tv.setText(homeWorkModel.getStudname());
		int credit = homeWorkModel.getCredit();
		mCredit_tv.setText(mActivity.getString(R.string.homework_check_common_credit_text, credit));
		String askTime = new Date(homeWorkModel.getDatatime()).toLocaleString().substring(5);
		mAskTime_tv.setText(askTime);
		mDesc_tv.setVisibility(View.VISIBLE);
		mDesc_tv.setText(homeWorkModel.getMemo());
		mGrade_tv.setText(homeWorkModel.getGrade());
		mSubject_tv.setText(homeWorkModel.getSubject());
		mRewardVal_tv.setText("￥ " + homeWorkModel.getBounty());
	}

	/**
	 * 换题发现为空
	 */
	public void showDataNullQuestion() {
		ImageLoader.getInstance().loadImage(null, mAvatar_iv, R.drawable.default_contact_image);
		mNoQuestion_iv.setVisibility(View.VISIBLE);
		mHomeWorkImgContainer_gv.setVisibility(View.GONE);

		mNewUserTag.setVisibility(View.INVISIBLE);
		mVipUserTag.setVisibility(View.INVISIBLE);
		mNick_tv.setText("");
		mCredit_tv.setVisibility(View.INVISIBLE);
		mDesc_tv.setVisibility(View.GONE);
		mGrade_tv.setText("");
		mSubject_tv.setText("");
		mRewardVal_tv.setText("");

		mNick_tv.setClickable(false);
		mAvatar_iv.setClickable(false);

	}

}
