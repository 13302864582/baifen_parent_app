package com.daxiong.fun.function.course;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;
import com.daxiong.fun.base.ImageLoader;
import com.daxiong.fun.constant.GlobalContant;
import com.daxiong.fun.constant.GlobalVariable;
import com.daxiong.fun.function.course.adapter.CharpterDetailAdapter;
import com.daxiong.fun.function.course.fragment.CharpterDetailItemFragment;
import com.daxiong.fun.function.course.model.CharpterModel;
import com.daxiong.fun.function.course.model.CoursePageModel;
import com.daxiong.fun.function.question.OneQuestionMoreAnswersDetailItemFragment.OnTipsShowListener;
import com.daxiong.fun.http.OkHttpHelper;
import com.daxiong.fun.http.OkHttpHelper.HttpListener;
import com.daxiong.fun.manager.IntentManager;
import com.daxiong.fun.util.DensityUtil;
import com.daxiong.fun.util.MediaUtil;
import com.daxiong.fun.util.ToastUtils;
import com.daxiong.fun.view.MyViewPager;

import org.json.JSONObject;

import java.util.ArrayList;

public class CharpterDetailActivity extends BaseActivity
		implements OnClickListener, OnPageChangeListener, OnTipsShowListener {
	private ArrayList<View> dotLists;
	private LinearLayout dots_ll;
	private MyViewPager mViewPager;
	private int userid;
	private CharpterDetailAdapter mAdapter;
	private int currentPosition;
	private ArrayList<CoursePageModel> pagelist;
	private String charptername;
	private String avatar;
	private String name;
	private boolean[] isShowPoint/* = { true, true, true, true } */;
	private TextView nextStepTV;
	private RelativeLayout nextStepLayout;

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.charpter_detail_activity);

		findViewById(R.id.back_layout).setOnClickListener(this);

		int avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size_homework_check_common);

		dots_ll = (LinearLayout) findViewById(R.id.dots_ll);
		mViewPager = (MyViewPager) findViewById(R.id.page_viewpager_charpter);
		mViewPager.setOffscreenPageLimit(8);
		mViewPager.setOnPageChangeListener(this);
		TextView nameTv = (TextView) findViewById(R.id.name_tv_charpterdetail);
		NetworkImageView avatarIv = (NetworkImageView) findViewById(R.id.avatar_iv_charpterdetail);
		avatarIv.setOnClickListener(this);

		// mCollectTextTv = (TextView)
		// findViewById(R.id.collection_count_tv_stu_detail);
		// mCollectIconIv = (ImageView)
		// findViewById(R.id.collection_icon_iv_stu_detail);

		// findViewById(R.id.homework_detail_bottom_container_stu).setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		if (intent != null) {
			int charpterid = intent.getIntExtra("charpterid", 0);
			charptername = intent.getStringExtra("charptername");
			userid = intent.getIntExtra("userid", 0);
			avatar = intent.getStringExtra("avatar");
			name = intent.getStringExtra("name");
			boolean isBuyed = intent.getBooleanExtra("isBuyed", false);
			if (isBuyed) {
				View askBtn = findViewById(R.id.ask_btn_charpterdetail);
				askBtn.setOnClickListener(this);
				askBtn.setVisibility(View.VISIBLE);
			}
			setWelearnTitle(charptername);
			nameTv.setText(name);
			ImageLoader.getInstance().loadImageWithDefaultAvatar(avatar, avatarIv, avatarSize, avatarSize / 10);

			loadData(charpterid);
		}

		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.text_hide_tips);
		nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
		nextStepLayout.setOnClickListener(this);
		nextStepLayout.setVisibility(View.GONE);
	}

	@Override
	public void onPause() {
		MediaUtil.getInstance(false).stopPlay();
		super.onPause();
	}

	private void loadData(int charpterid) {
		if (charpterid != 0) {
			JSONObject data = new JSONObject();
			try {
				data.put("charpterid", charpterid);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			OkHttpHelper.post(this, "course", "charpterdetail", data, new HttpListener() {

				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					nextStepLayout.setVisibility(View.GONE);
					if (!TextUtils.isEmpty(dataJson)) {
						CharpterModel classHourModel = null;
						pagelist = null;
						try {
							classHourModel = new Gson().fromJson(dataJson, CharpterModel.class);
						} catch (Exception e) {
						}
						if (classHourModel != null) {
							String charptername = classHourModel.getCharptername();
							setWelearnTitle(charptername);
							pagelist = classHourModel.getPage();
							if (pagelist != null) {
								nextStepLayout.setVisibility(View.VISIBLE);
								int size = pagelist.size();
								isShowPoint = new boolean[size];
								for (int i = 0; i < size; i++) {
									isShowPoint[i] = true;
								}
								mAdapter = new CharpterDetailAdapter(getSupportFragmentManager(), pagelist);
								mViewPager.setAdapter(mAdapter);

								for (int i = 0; i < size; i++) {
									if (null != mAdapter.getFragment(i)) {
										mAdapter.getFragment(i).setOnTipsShowListener(CharpterDetailActivity.this);
									}
								}

								initDot(size, 0);
							}
						}
					}

				}

				@Override
				public void onFail(int HttpCode,String errMsg) {

				}
			});
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		GlobalVariable.mViewPager = mViewPager;
	}

	// 初始化点
	private void initDot(int size, int defalutPosition) {
		dotLists = new ArrayList<View>();
		dots_ll.removeAllViews();
		for (int i = 0; i < size; i++) {
			// 设置点的宽高
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 6),
					DensityUtil.dip2px(this, 6));
			// 设置点的间距
			params.setMargins(7, 0, 7, 0);
			// 初始化点的对象
			View m = new View(this);
			// 把点的宽高设置到view里面
			m.setLayoutParams(params);
			if (i == defalutPosition) {
				// 默认情况下，首先会调用第一个点。就必须展示选中的点
				m.setBackgroundResource(R.drawable.dot_focus);
			} else {
				// 其他的点都是默认的。
				m.setBackgroundResource(R.drawable.dot_normal);
			}
			// 把所有的点装载进集合
			dotLists.add(m);
			// 现在的点进入到了布局里面
			dots_ll.addView(m);
		}
	}

	private void selectDot(int postion) {
		for (View dot : dotLists) {
			dot.setBackgroundResource(R.drawable.dot_normal);
		}
		dotLists.get(postion).setBackgroundResource(R.drawable.dot_focus);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.next_setp_layout:
			if (mAdapter == null) {
				return;
			}
			CharpterDetailItemFragment mFragment = mAdapter.getFragment(currentPosition);
			if (null == mFragment) {
				return;
			}
			isShowPoint[currentPosition] = !isShowPoint[currentPosition];

			boolean isShow = isShowPoint[currentPosition];

			mFragment.showTips(isShow);

			if (null != nextStepTV) {
				nextStepTV.setText(isShow ? R.string.text_hide_tips : R.string.text_show_tips);
			}
			break;
		case R.id.avatar_iv_charpterdetail:
			IntentManager.gotoPersonalPage(this, userid, GlobalContant.ROLE_ID_COLLEAGE);
			break;
		case R.id.ask_btn_charpterdetail:// 追问
			// mViewPager.setBanScroll();
			MobclickAgent.onEvent(this, "Homewrok_Append");

			if (mAdapter == null) {
				ToastUtils.show("正在加载数据中，请稍候");
				return;
			}
			String pointlist = mAdapter.getPageData(currentPosition);
			if (pagelist == null) {
				ToastUtils.show("正在加载数据中，请稍候");
				return;
			}
			CoursePageModel coursePageModel = null;
			if (currentPosition < pagelist.size()) {
				coursePageModel = pagelist.get(currentPosition);
			}
			if (coursePageModel == null) {
				ToastUtils.show("正在加载数据中，请稍候");
				return;
			}
			IntentManager.goToSingleStudentQAActivity(this, coursePageModel.getPageid(), coursePageModel.getImgurl(),
					charptername, pointlist, userid, avatar, name);
			break;
		case R.id.root_adopt_dialog:// 点击dialog的时候
			// case R.id.degree_ratingBar_adopt_dialog://点击dialog的时候
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(view.findViewById(R.id.comment_et_adopt_dialog).getWindowToken(), 0);
			break;

		default:
			break;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		currentPosition = position;
		selectDot(position);

		if (isShowPoint[position]) {
			nextStepTV.setText(R.string.text_hide_tips);
		} else {
			nextStepTV.setText(R.string.text_show_tips);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1102:
				if (mAdapter != null) {
					mAdapter.refreshCurrentPage(currentPosition);
				}
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onTipsShow(boolean isShow) {
		isShowPoint[currentPosition] = isShow;
		if (null != nextStepTV) {
			nextStepTV.setText(isShow ? R.string.text_hide_tips : R.string.text_show_tips);
		}

	}

	// @Override
	// public void onBackPressed() {
	// if (mAdoptDialog != null && mAdoptDialog.isShowing()) {
	// mAdoptDialog.dismiss();
	// } else if (mRefusePopWindow != null && mRefusePopWindow.isShowing()) {
	// mRefusePopWindow.dismiss();
	// } else {
	// super.onBackPressed();
	// }
	// }
}
